package com.maoding.FileServer;

import com.maoding.Base.BaseLocalService;
import com.maoding.Bean.*;
import com.maoding.Const.FileServerConst;
import com.maoding.FileServer.Config.FileServerConfig;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Utils.BeanUtils;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 10:08
 * 描    述 :
 */
@Service("fileService")
public class FileServiceImpl extends BaseLocalService<FileServicePrx> implements FileService, FileServicePrx {

    private static final FileServerConfig fileServerSetting = new FileServerConfig();
    private BasicFileServerInterface fileServer = fileServerSetting.getFileServer();

    /**
     * 同步方式获取业务接口代理对象
     */
    public static FileServicePrx getInstance(String adapterName) {
        FileServiceImpl prx = new FileServiceImpl();
        return prx.getServicePrx("FileService", adapterName, FileServicePrx.class, _FileServicePrxI.class);
    }

    public static FileServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public DownloadResultDTO download(DownloadRequestDTO request, Current current) {
        BasicDownloadRequestDTO basicRequest = BeanUtils.createFrom(request, BasicDownloadRequestDTO.class);
        BasicDownloadResultDTO basicResult = fileServer.download(basicRequest);
        return BeanUtils.createFrom(basicResult, DownloadResultDTO.class);
    }

    @Override
    public UploadResultDTO upload(UploadRequestDTO request, Current current) {
        BasicUploadRequestDTO basicRequest = BeanUtils.createFrom(request, BasicUploadRequestDTO.class);
        BasicUploadResultDTO basicResult = fileServer.upload(basicRequest);
        return BeanUtils.createFrom(basicResult, UploadResultDTO.class);
    }

    @Override
    public void setFileServerType(int type, Current current) {
        fileServerSetting.setFileServerType(type);
        fileServer = fileServerSetting.getFileServer();
    }

    @Override
    public int getFileServerType(Current current) {
        return fileServerSetting.getFileServerType();
    }

    @Override
    public FileRequestDTO getUploadRequest(FileDTO src, int mode, CallbackDTO callback, Current current) {
        if (fileServer == null) {
            this.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL, (Current) null);
        }
        assert (fileServer != null);
        BasicFileDTO basicSrc = BeanUtils.createFrom(src, BasicFileDTO.class);
        BasicCallbackDTO basicCallback = BeanUtils.createFrom(callback, BasicCallbackDTO.class, true);
        BasicFileRequestDTO basicResult = fileServer.getUploadRequest(basicSrc, mode, basicCallback);
        FileRequestDTO fileRequestDTO = BeanUtils.createFrom(basicResult, FileRequestDTO.class, true);

        //补充缺失属性
        assert (fileRequestDTO != null);
        if ((fileRequestDTO.getScope() == null) && (fileRequestDTO.getParams() != null) && (fileRequestDTO.getParams().containsKey("scope")))
            fileRequestDTO.setScope(fileRequestDTO.getParams().get("scope"));
        if ((fileRequestDTO.getKey() == null) && (fileRequestDTO.getParams() != null) && (fileRequestDTO.getParams().containsKey("key")))
            fileRequestDTO.setKey(fileRequestDTO.getParams().get("key"));

        return fileRequestDTO;
    }

    @Override
    public FileRequestDTO getDownloadRequest(FileDTO src, int mode, CallbackDTO callback, Current current) {
        if (fileServer == null) {
            this.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL, (Current) null);
        }
        assert fileServer != null;
        BasicFileDTO basicSrc = BeanUtils.createFrom(src, BasicFileDTO.class);
        BasicCallbackDTO basicCallback = BeanUtils.createFrom(callback, BasicCallbackDTO.class, true);
        BasicFileRequestDTO basicResult = fileServer.getDownloadRequest(basicSrc, mode, basicCallback);
        return BeanUtils.createFrom(basicResult, FileRequestDTO.class, true);
    }


    @Override
    public boolean isExist(FileDTO src, Current current) {
        assert fileServerSetting != null;
        if (fileServer == null) {
            this.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL, (Current) null);
        }
        BasicFileDTO fileDTO = BeanUtils.createFrom(src, BasicFileDTO.class);
        return fileServer.isExist(fileDTO);
    }

    @Override
    public String duplicateFile(FileDTO src, Current current) {
        assert fileServerSetting != null;
        if (fileServer == null) {
            this.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL, (Current) null);
        }
        BasicFileDTO fileDTO = BeanUtils.createFrom(src, BasicFileDTO.class);
        return fileServer.duplicateFile(fileDTO);
    }

    @Override
    public void deleteFile(FileDTO src, Current current) {
        assert fileServerSetting != null;
        if (fileServer == null) {
            this.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL, (Current) null);
        }
        BasicFileDTO fileDTO = BeanUtils.createFrom(src, BasicFileDTO.class);
        fileServer.deleteFile(fileDTO);
    }

    @Override
    public List<String> listFile(String scope, Current current) {
        assert fileServerSetting != null;
        if (fileServer == null) {
            this.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL, (Current) null);
        }
        return fileServer.listFile(scope);
    }

    @Override
    public List<String> listScope(Current current) {
        assert fileServerSetting != null;
        if (fileServer == null) {
            this.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL, (Current) null);
        }
        return fileServer.listScope();
    }

    @Override
    public void finishUpload(FileRequestDTO request, Current current) {

    }
}
