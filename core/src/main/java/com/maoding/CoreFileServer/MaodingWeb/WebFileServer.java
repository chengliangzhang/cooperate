package com.maoding.CoreFileServer.MaodingWeb;

import com.maoding.Bean.CoreKeyValuePair;
import com.maoding.Bean.CoreResponse;
import com.maoding.Bean.CoreUploadFileItem;
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
import java.util.ArrayList;
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
    private static final String DEFAULT_URL = "http://127.0.0.1:8071/fileCenter/netFile/uploadFile";
    private String lastUrl = null;

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
        return DEFAULT_URL;
//        return (lastUrl != null) ? lastUrl : DEFAULT_URL;
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
        String urlString = coreGetServerAddress();

        // 设定要上传的Field及其对应的value
        File srcFile = null;
        ArrayList<CoreKeyValuePair> ptyList = new ArrayList<>();
        ptyList.add(new CoreKeyValuePair("id", UUID.randomUUID().toString().replaceAll("-", "")));
        ptyList.add(new CoreKeyValuePair("type", HttpUtils.DEFAULT_FILE_CONTENT_TYPE));
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
                String[] nodeArray = createRequest.getPath().split("-");
                final int PROJECT_ID_POS = 0;
                final int COMPANY_ID_POS = 1;
                final int TASK_ID_POS = 2;
                final int SKY_PID_POS = 3;
                final int ACCOUNT_ID_POS = 4;

                if (nodeArray.length >= PROJECT_ID_POS){
                    ptyList.add(new CoreKeyValuePair("projectId", StringUtils.left(nodeArray[PROJECT_ID_POS],StringUtils.DEFAULT_ID_LENGTH)));
                }
                if (nodeArray.length >= COMPANY_ID_POS){
                    ptyList.add(new CoreKeyValuePair("companyId", StringUtils.left(nodeArray[COMPANY_ID_POS],StringUtils.DEFAULT_ID_LENGTH)));
                }
                if (nodeArray.length >= TASK_ID_POS){
                    ptyList.add(new CoreKeyValuePair("taskId", StringUtils.left(nodeArray[TASK_ID_POS],StringUtils.DEFAULT_ID_LENGTH)));
                }
                if (nodeArray.length >= SKY_PID_POS){
                    ptyList.add(new CoreKeyValuePair("pid", StringUtils.left(nodeArray[SKY_PID_POS],StringUtils.DEFAULT_ID_LENGTH)));
                }
                if (nodeArray.length >= ACCOUNT_ID_POS){
                    ptyList.add(new CoreKeyValuePair("accountId", StringUtils.left(nodeArray[ACCOUNT_ID_POS],StringUtils.DEFAULT_ID_LENGTH)));
                }
            }
            if (StringUtils.isNotEmpty(fileName)) ptyList.add(new CoreKeyValuePair("name",fileName));

            ptyList.add(new CoreKeyValuePair("lastModifiedDate",StringUtils.getTimeStamp()));
        }

        CoreResponse<FastdfsUploadResult> response = sendRequest(srcFile,0,-1,ptyList,urlString);

        String key = null;
        if ((response != null) && (response.isSuccessful())) {
            FastdfsUploadResult webResponse = response.getData();
            assert (webResponse != null);
            key = webResponse.getFastdfsPath();
        }
        return key;
    }

    private CoreResponse<FastdfsUploadResult> sendRequest(File file, long pos, int size, ArrayList<CoreKeyValuePair> ptyList, String urlString){

        CoreResponse<FastdfsUploadResult> result = null;

        // 设定服务地址和文件相关属性，计算和调整文件长度
        String url = coreGetServerAddress();
        if (StringUtils.isNotEmpty(urlString)) url = urlString;

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
        ptyList.add(new CoreKeyValuePair("size", Integer.toString(size)));
        Integer perSize = MAX_BLOCK_SIZE;
        ptyList.add(new CoreKeyValuePair("chunkPerSize", perSize.toString()));
        Integer maxChunk = (int)((pos + size) / perSize);
        ptyList.add(new CoreKeyValuePair("chunks", maxChunk.toString()));

        long realPos = pos;
        int realSize = size;
        if (realSize > perSize) realSize = perSize;
        Integer minChunk = (int)(pos / perSize);
        CoreKeyValuePair curChunkPty = null;
        for (Integer chunk=minChunk; chunk<(maxChunk+1); chunk++) {
            if (curChunkPty != null) ptyList.remove(curChunkPty);
            curChunkPty = new CoreKeyValuePair("chunk",chunk.toString());
            ptyList.add(curChunkPty);
            CoreResponse<?> resultUnknownType = HttpUtils.postFileData(url,ptyList,fileItem,realPos,realSize);
            result = convertResponse(resultUnknownType);
            if ((result == null) || (result.isError())) break;
            realPos += realSize;
            if ((realPos + realSize) > (pos + size)) realSize = (int)(pos + size - realPos);
        }

        return result;
    }
}
