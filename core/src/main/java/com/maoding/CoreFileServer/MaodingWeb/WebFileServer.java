package com.maoding.CoreFileServer.MaodingWeb;

import com.maoding.Bean.CoreKeyValuePair;
import com.maoding.Bean.CoreResponse;
import com.maoding.Bean.CoreUploadFileItem;
import com.maoding.CoreFileServer.CoreFileDTO;
import com.maoding.CoreFileServer.CoreFileExtraDTO;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.HttpUtils;
import com.maoding.Utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
    private static final String DEFAULT_URL = "http://172.16.6.73:8071/fileCenter/netFile/uploadFile";
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
        return (lastUrl == null) ? DEFAULT_URL : lastUrl;
    }

    /**
     * 把文件传到服务器
     *
     * @param src
     * @param request
     */
    @Override
    @Deprecated
    public CoreResponse<WebFileResponse> upload(CoreFileDTO src, CoreFileExtraDTO request) {

        // 设定服务地址
        String urlString = DEFAULT_URL;

        // 设定要上传的Field及其对应的value
        ArrayList<CoreKeyValuePair> ptyList = new ArrayList<>();
        ptyList.add(new CoreKeyValuePair("projectId", request.getProjectId()));
        ptyList.add(new CoreKeyValuePair("pid", request.getPid()));
        ptyList.add(new CoreKeyValuePair("accountId", request.getAccountId()));
        ptyList.add(new CoreKeyValuePair("companyId", request.getCompanyId()));
        ptyList.add(new CoreKeyValuePair("lastModifiedDate", (new Date()).toString()));


        ptyList.add(new CoreKeyValuePair("id", "WU_FILE_0"));
        ptyList.add(new CoreKeyValuePair("type", HttpUtils.DEFAULT_FILE_CONTENT_TYPE));

        String fileName = StringUtils.formatPath(src.getScope() + StringUtils.SPLIT_PATH + src.getKey());
        ptyList.add(new CoreKeyValuePair("name", fileName));
        File file = new File(fileName);
        Long fileLength = file.length();
        ptyList.add(new CoreKeyValuePair("size", fileLength.toString()));
        Integer chunkNum = 0;
        CoreKeyValuePair chunk = new CoreKeyValuePair("chunk", chunkNum.toString());
        ptyList.add(chunk);
        Long pos = 0L;
        Integer blockSize = fileLength.intValue();
        if (fileLength > MAX_BLOCK_SIZE) {
            Integer chunks = (int)(fileLength / MAX_BLOCK_SIZE) + 1;
            ptyList.add(new CoreKeyValuePair("chunks", chunks.toString()));
            ptyList.add(new CoreKeyValuePair("chunkPerSize", MAX_BLOCK_SIZE.toString()));
            blockSize = MAX_BLOCK_SIZE;
        }

        //文件内容
        CoreUploadFileItem fileItem = new CoreUploadFileItem("file", fileName);
        CoreResponse<?> resultUnknownType = HttpUtils.postFileData(urlString,ptyList,fileItem,pos,blockSize,MAX_BLOCK_SIZE);
        CoreResponse<FastdfsUploadResult> result = convertResponse(resultUnknownType);

        if (fileLength > blockSize) {
            FastdfsUploadResult uploadResult = result.getData();
            ptyList.add(new CoreKeyValuePair("fastdfsGroup", uploadResult.getFastdfsGroup()));
            ptyList.add(new CoreKeyValuePair("fastdfsPath", uploadResult.getFastdfsPath()));
            Integer chunks = (int)(fileLength / MAX_BLOCK_SIZE) + 1;
            while (++chunkNum < chunks){
                ptyList.remove(chunk);
                chunk = new CoreKeyValuePair("chunk", chunkNum.toString());
                ptyList.add(chunk);
                pos += blockSize;
                if (fileLength < (pos + blockSize)) blockSize = (int)(fileLength - pos);
                resultUnknownType = HttpUtils.postFileData(urlString,ptyList,fileItem,pos,blockSize,MAX_BLOCK_SIZE);
                result = convertResponse(resultUnknownType);
            }
        }

        CoreResponse<WebFileResponse> coreResponse = new CoreResponse<>();
        coreResponse.setCode(result.getCode());
        coreResponse.setMsg(result.getMsg());
        coreResponse.setStatus(result.getStatus());
        coreResponse.setInfo(result.getInfo());
        WebFileResponse data = BeanUtils.createCleanFrom(result.getData(),WebFileResponse.class);
        coreResponse.setData(data);

        return coreResponse;
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


    @Override
    public CoreFileDTO coreCreateFile(CoreFileDTO dst, CoreFileExtraDTO createRequest) {
        //设定要使用的url
        String urlString = coreGetServerAddress();
        if ((dst != null) && (StringUtils.isNotEmpty(dst.getServerAddress()))) urlString = dst.getServerAddress();

        // 设定要上传的Field及其对应的value
        ArrayList<CoreKeyValuePair> ptyList = new ArrayList<>();
        ptyList.add(new CoreKeyValuePair("id", "WU_FILE_0"));
        ptyList.add(new CoreKeyValuePair("type", HttpUtils.DEFAULT_FILE_CONTENT_TYPE));
        String fileName = (dst == null) ? null : StringUtils.getFileName(dst.getKey());
        File srcFile = null;
        if (createRequest != null) {
            if (StringUtils.isNotEmpty(createRequest.getPid())) ptyList.add(new CoreKeyValuePair("pid", createRequest.getPid()));
            if (StringUtils.isNotEmpty(createRequest.getProjectId())) ptyList.add(new CoreKeyValuePair("projectId", createRequest.getProjectId()));
            if (StringUtils.isNotEmpty(createRequest.getCompanyId())) ptyList.add(new CoreKeyValuePair("companyId", createRequest.getCompanyId()));
            if (StringUtils.isNotEmpty(createRequest.getTaskId())) ptyList.add(new CoreKeyValuePair("taskId", createRequest.getTaskId()));
            if (StringUtils.isNotEmpty(createRequest.getAccountId())) ptyList.add(new CoreKeyValuePair("accountId", createRequest.getAccountId()));
            ptyList.add(new CoreKeyValuePair("lastModifiedDate",
                    ((createRequest.getLastModifyTime() == null) ? (new Date()) : createRequest.getLastModifyTime()).toString()));
            srcFile = createRequest.getSrcFile();
            if ((fileName == null) && (srcFile != null)){
                fileName = srcFile.getName();
            }
        }
        ptyList.add(new CoreKeyValuePair("name", fileName));

        CoreResponse<FastdfsUploadResult> response = sendRequest(srcFile,0,-1,ptyList,urlString);

        CoreFileDTO resultFile = null;
        assert (response != null);
        if (response.isSuccessful()) {
            FastdfsUploadResult webResponse = response.getData();
            assert (webResponse != null);
            resultFile = new CoreFileDTO();
            resultFile.setServerAddress(urlString);
            resultFile.setScope(webResponse.getFastdfsGroup());
            resultFile.setKey(webResponse.getFastdfsPath());
        }
        return resultFile;
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
