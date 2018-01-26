package com.maoding.CoreFileServer.MaodingWeb;

import com.maoding.Bean.CoreKeyValuePair;
import com.maoding.Bean.CoreResponse;
import com.maoding.Bean.CoreUploadFileItem;
import com.maoding.CoreFileServer.CoreFileDTO;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreFileServer.CoreUploadRequest;
import com.maoding.CoreFileServer.CoreUploadResult;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.HttpUtils;
import com.maoding.Utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
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

    /**
     * 把文件传到服务器
     *
     * @param src
     * @param request
     */
    @Override
    public CoreResponse<CoreUploadResult> upload(CoreFileDTO src, CoreUploadRequest request) {
        final Integer MAX_BLOCK_SIZE = (8192 * 1024);
        // 设定服务地址
        String urlString = request.getAddress();

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
        CoreResponse<?> resultUnknownType = HttpUtils.uploadFile(urlString,ptyList,fileItem,pos,blockSize,MAX_BLOCK_SIZE);
        CoreResponse<CoreUploadResult> result = convertResponse(resultUnknownType);

        if (fileLength > blockSize) {
            CoreUploadResult uploadResult = result.getData();
            ptyList.add(new CoreKeyValuePair("fastdfsGroup", uploadResult.getScope()));
            ptyList.add(new CoreKeyValuePair("fastdfsPath", uploadResult.getKey()));
            Integer chunks = (int)(fileLength / MAX_BLOCK_SIZE) + 1;
            while (++chunkNum < chunks){
                ptyList.remove(chunk);
                chunk = new CoreKeyValuePair("chunk", chunkNum.toString());
                ptyList.add(chunk);
                pos += blockSize;
                if (fileLength < (pos + blockSize)) blockSize = (int)(fileLength - pos);
                resultUnknownType = HttpUtils.uploadFile(urlString,ptyList,fileItem,pos,blockSize,MAX_BLOCK_SIZE);
                result = convertResponse(resultUnknownType);
            }
        }

        return result;
    }

    private CoreResponse<CoreUploadResult> convertResponse(CoreResponse<?> resultUnknownType){
        CoreResponse<CoreUploadResult> result = new CoreResponse<>();
        result.setCode(resultUnknownType.getCode());
        result.setMsg(resultUnknownType.getMsg());
        result.setStatus(resultUnknownType.getStatus());
        result.setInfo(resultUnknownType.getInfo());
        Map<?,?> webDataMap = HttpUtils.getResponseData(resultUnknownType,Map.class);
        FastdfsUploadResult webData = BeanUtils.createFrom(webDataMap,FastdfsUploadResult.class);
        CoreUploadResult data = new CoreUploadResult();
        assert (webData != null);
        data.setScope(webData.getFastdfsGroup());
        data.setKey(webData.getFastdfsPath());
        result.setData(data);
        return result;
    }

}
