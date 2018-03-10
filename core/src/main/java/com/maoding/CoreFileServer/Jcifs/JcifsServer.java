package com.maoding.CoreFileServer.Jcifs;

import com.maoding.Const.ApiResponseConst;
import com.maoding.CoreFileServer.*;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.FileUtils;
import com.maoding.Utils.StringUtils;
import jcifs.UniAddress;
import jcifs.smb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/30 19:09
 * 描    述 :
 */
@Service("jcifsServer")
@SuppressWarnings("deprecation")
public class JcifsServer implements CoreFileServer {
    /** 日志对象 */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取通过http方式上传文件数据库时的需要设置的部分参数
     *
     * @param src
     * @param callbackSetting
     */
    @Override
    public BasicFileRequestDTO getUploadRequest(CoreFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
        BasicFileRequestDTO result = new BasicFileRequestDTO();
        result.setUrl("http://localhost:8087/FileServer/upload");
        Map<String,String> params = new HashMap<>();
        if ((params != null) && (params.size() > 0)){
            result.getParams().putAll(params);
        }
        return result;

    }

    /**
     * 获取通过http方式下载文件数据库时的需要设置的部分参数
     *
     * @param src
     * @param callbackSetting
     */
    @Override
    public BasicFileRequestDTO getDownloadRequest(CoreFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
        return null;
    }

    /**
     * 上传文件分片内容
     *
     * @param request
     */
    @Override
    public BasicUploadResultDTO upload(BasicUploadRequestDTO request) {
        BasicUploadResultDTO result = BeanUtils.createFrom(request,BasicUploadResultDTO.class);
        assert result != null;
        CoreFileDataDTO multipart = request.getMultipart();
        assert multipart != null;

        SmbFileOutputStream smbOutput = null;
        try {
            String s = multipart.getScope() + "/" + multipart.getKey();
            Boolean b = (!StringUtils.isEmpty(request.getUploadId()));
            if (b) b = (Integer.parseInt(request.getUploadId())>0);
            UniAddress dc = UniAddress.getByName("192.168.33.103");
            NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication("idccapp.local", "idccapp25", "852");
            SmbSession.logon(dc, authentication);
            SmbFile remoteFile = new SmbFile(s,authentication);
            smbOutput = new SmbFileOutputStream(remoteFile,b);
        } catch (SmbException | MalformedURLException | UnknownHostException e) {
            log.error(e.getMessage(),e);
            FileUtils.close(smbOutput);
        }

        if (smbOutput != null) {
            try {
                smbOutput.write(multipart.getData());
                smbOutput.flush();
                smbOutput.close();
                result.setChunkSize(multipart.getSize());
                result.setStatus(ApiResponseConst.SUCCESS);
                result.setMsg(ApiResponseConst.DEFAULT_MESSAGE.get(ApiResponseConst.SUCCESS));
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                FileUtils.close(smbOutput);
                result.setChunkSize(0);
                result.setStatus(ApiResponseConst.ERROR);
                result.setMsg(ApiResponseConst.DEFAULT_MESSAGE.get(ApiResponseConst.ERROR));
            }
        }
        return result;
    }

    /**
     * 下载文件分片内容
     *
     * @param request
     */
    @Override
    public BasicDownloadResultDTO download(BasicDownloadRequestDTO request) {
        return null;
    }

    /**
     * 在文件服务器上复制文件，复制到同一空间，返回复制后的文件标识
     *
     * @param src
     */
    @Override
    public String duplicateFile(CoreFileDTO src) {
        return null;
    }

    /**
     * 判断在文件服务器上是否存在指定文件
     *
     * @param src
     */
    @Override
    public Boolean coreIsExist(CoreFileDTO src) {
        return null;
    }

    /**
     * 获取文件服务器上某一空间上的所有文件
     *
     * @param scope
     */
    @Override
    public List listFile(String scope) {
        return null;
    }

    /**
     * 获取文件服务器上的所有空间
     */
    @Override
    public List<String> listScope() {
        return null;
    }

    /**
     * 在文件服务器上删除指定文件
     *
     * @param src
     */
    @Override
    public void coreDeleteFile(CoreFileDTO src) {

    }
}
