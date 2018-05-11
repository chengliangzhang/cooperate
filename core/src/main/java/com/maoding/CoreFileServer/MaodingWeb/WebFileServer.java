package com.maoding.CoreFileServer.MaodingWeb;

import com.maoding.Bean.CoreResponse;
import com.maoding.CoreFileServer.CoreCreateFileRequest;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreUtils.BeanUtils;
import com.maoding.CoreUtils.HttpUtils;
import com.maoding.CoreUtils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/22 14:03
 * 描    述 :
 */
@Service("webFileServer")
public class WebFileServer implements CoreFileServer {
    /** 日志对象 */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Integer MAX_BLOCK_SIZE = (8192 * 1024);
    private static final String DEFAULT_URL = "http://127.0.0.1:8071/";
    private static final String DEFAULT_FUNCTION = "fileCenter/netFile/uploadFile";
    private String lastUrl = null;
    private String lastBaseDir = null;

    /**
     * 设定文件服务器地址
     *
     * @param serverAddress
     */
    @Override
    public void coreSetServerAddress(String serverAddress) {
        lastUrl = serverAddress;
    }

    /**
     * 获取文件服务器地址
     */
    @Override
    public String coreGetServerAddress() {
//        return DEFAULT_URL;
        return (lastUrl != null) ? lastUrl : DEFAULT_URL;
    }

    @Override
    public String coreGetBaseDir() {
        return (lastBaseDir != null) ? lastBaseDir : DEFAULT_FUNCTION;
    }

    private String getUrlString(){
        return coreGetServerAddress() + "/" + coreGetBaseDir();
    }

    private CoreResponse<FastdfsUploadResult> convertResponse(CoreResponse<?> resultUnknownType){
        if (resultUnknownType == null) return null;
        CoreResponse<FastdfsUploadResult> result = new CoreResponse<>();
        result.setCode(resultUnknownType.getCode());
        result.setMsg(resultUnknownType.getMsg());
        result.setStatus(resultUnknownType.getStatus());
        result.setInfo(resultUnknownType.getInfo());
        Map<?,?> webDataMap = HttpUtils.getResponseData(resultUnknownType,Map.class);
        FastdfsUploadResult webData = BeanUtils.createFrom(webDataMap,FastdfsUploadResult.class);
        result.setData(webData);
        return result;
    }

    /**
     * 创建文件
     *
     * @param createRequest
     */
    @Override
    public String coreCreateFile(CoreCreateFileRequest createRequest) {
        //设定要使用的url
        String urlString = getUrlString();

        // 设定要上传的Field及其对应的value
        File srcFile = null;
        Map<String,String> ptyMap = new HashMap<>();
        ptyMap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
        ptyMap.put("type", HttpUtils.DEFAULT_FILE_CONTENT_TYPE);
        if (createRequest != null) {
            //设定要上传的文件名及要上传的文件
            String fileName = null;
            if (createRequest.getSrcFile() != null) {
                srcFile = createRequest.getSrcFile();
                fileName = StringUtils.getFileName(srcFile.getPath());
            }
            if (StringUtils.isNotEmpty(createRequest.getPath())){
                fileName = StringUtils.getFileName(createRequest.getPath());
                //设定其他属性
                String dirName = StringUtils.getDirName(createRequest.getPath());
                String ptyName = StringUtils.getFileName(dirName);
                String[] nodeArray = ptyName.split("-");
                final int PROJECT_ID_POS = 0;
                final int COMPANY_ID_POS = 1;
                final int TASK_ID_POS = 2;
                final int SKY_PID_POS = 3;
                final int ACCOUNT_ID_POS = 4;

                if (nodeArray.length >= PROJECT_ID_POS){
                    ptyMap.put("projectId", StringUtils.left(nodeArray[PROJECT_ID_POS],StringUtils.DEFAULT_ID_LENGTH));
                }
                if (nodeArray.length >= COMPANY_ID_POS){
                    ptyMap.put("companyId", StringUtils.left(nodeArray[COMPANY_ID_POS],StringUtils.DEFAULT_ID_LENGTH));
                }
                if (nodeArray.length >= TASK_ID_POS){
                    ptyMap.put("taskId", StringUtils.left(nodeArray[TASK_ID_POS],StringUtils.DEFAULT_ID_LENGTH));
                }
                if (nodeArray.length >= SKY_PID_POS){
                    ptyMap.put("pid", StringUtils.left(nodeArray[SKY_PID_POS],StringUtils.DEFAULT_ID_LENGTH));
                }
                if (nodeArray.length >= ACCOUNT_ID_POS){
                    ptyMap.put("accountId", StringUtils.left(nodeArray[ACCOUNT_ID_POS],StringUtils.DEFAULT_ID_LENGTH));
                }
            }
            if (StringUtils.isNotEmpty(fileName)) ptyMap.put("name",fileName);

            ptyMap.put("lastModifiedDate",StringUtils.getTimeStamp());
        }

        CoreResponse<FastdfsUploadResult> response = sendRequest(srcFile,0,-1,ptyMap,urlString);

        assert (response != null);
        FastdfsUploadResult webResponse = response.getData();
        assert (webResponse != null);
        return webResponse.getFastdfsPath();
    }

    private CoreResponse<FastdfsUploadResult> sendRequest(File file, long pos, int size, Map<String,String> ptyMap, String urlString){

        CoreResponse<FastdfsUploadResult> result = null;

        // 设定服务地址和文件相关属性，计算和调整文件长度
        String url = (StringUtils.isNotEmpty(urlString)) ? urlString : getUrlString();

        String path = null;
        CoreUploadFileItem fileItem = null;
        if ((file != null) && (file.exists()) && !(file.isDirectory())) {
            try {
                path = StringUtils.formatPath(file.getCanonicalPath());
            } catch (IOException e) {
                log.error("获取文件路径时错误",e);
            }
            if (StringUtils.isNotEmpty(path)) {
                if (pos > file.length()) pos = file.length();
                if ((size < 0) || ((pos + size) > file.length())) size = (int)(file.length() - pos);
                fileItem = new CoreUploadFileItem("file", path);
            }
        } else if (size < 0){
            size = 0;
        }

        //未经测试web是否能随机存取部分文件，设定为总是从文件头开始传输文件
        pos = 0;

        //计算和补充文件相关属性
        ptyMap.put("size", Integer.toString(size));
        Integer perSize = MAX_BLOCK_SIZE;
        ptyMap.put("chunkPerSize", perSize.toString());

        long realPos = pos;
        int realSize = size;
        if (realSize > perSize) realSize = perSize;
        Integer minChunk = (int)(pos / perSize);
        Integer maxChunk = (int)((pos + size) / perSize) + 1;
        if (maxChunk > (minChunk + 1)) {
            ptyMap.put("chunks", maxChunk.toString());
        } else {
            ptyMap.put("chunks", "0");
        }
        for (Integer chunk=minChunk; chunk<maxChunk; chunk++) {
            ptyMap.put("chunk",chunk.toString());
            CoreResponse<?> resultUnknownType = HttpUtils.postFileData(url,ptyMap,fileItem,realPos,realSize);
            result = convertResponse(resultUnknownType);
            if ((result == null) || (result.isError())) break;
            realPos += realSize;
            if ((realPos + realSize) > (pos + size)) realSize = (int)(pos + size - realPos);
            if (chunk.equals(minChunk)) {
                FastdfsUploadResult webResponse = result.getData();
                ptyMap.put("fastdfsGroup",webResponse.getFastdfsGroup());
                ptyMap.put("fastdfsPath",webResponse.getFastdfsPath());
                ptyMap.put("uploadId",webResponse.getUploadId());
            }
        }

        return result;
    }
}
