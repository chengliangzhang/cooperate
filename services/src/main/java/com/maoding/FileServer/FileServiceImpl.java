package com.maoding.FileServer;

import com.maoding.Base.BaseLocalService;
import com.maoding.Bean.*;
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
public class FileServiceImpl extends BaseLocalService<FileServicePrx> implements FileService,FileServicePrx{

    private static final FileServerConfig fileServerSetting = new FileServerConfig();
    private BasicFileServerInterface fileServer = fileServerSetting.getFileServer();

    /** 同步方式获取业务接口代理对象 */
    private static volatile FileServicePrx instance = null;
    public static FileServicePrx getInstance() {
        if (instance == null){
            FileServiceImpl prx = new FileServiceImpl();
            instance = prx.getServicePrx("FileService",FileServicePrx.class,_FileServicePrxI.class);
        }
        return instance;
    }

    @Override
    public DownloadResultDTO download(DownloadRequestDTO request, Current current) {
        BasicDownloadRequestDTO basicRequest = BeanUtils.createFrom(request,BasicDownloadRequestDTO.class);
        BasicDownloadResultDTO basicResult = fileServer.download(basicRequest);
        return BeanUtils.createFrom(basicResult,DownloadResultDTO.class);
    }

    @Override
    public UploadResultDTO upload(UploadRequestDTO request, Current current)  {
        BasicUploadRequestDTO basicRequest = BeanUtils.createFrom(request,BasicUploadRequestDTO.class);
        BasicUploadResultDTO basicResult = fileServer.upload(basicRequest);
        return BeanUtils.createFrom(basicResult,UploadResultDTO.class);
    }

    @Override
    public void setFileServerType(Integer type, Current current) {
        assert fileServerSetting != null;
        fileServerSetting.setFileServerType(type);
        fileServer = fileServerSetting.getFileServer();
    }

    @Override
    public Integer getFileServerType(Current current) {
        assert fileServerSetting != null;
        return fileServerSetting.getFileServerType();
    }

    @Override
    public FileRequestDTO getUploadRequest(FileDTO src, Integer mode, CallbackDTO callback, Current current) {
        assert fileServer != null;
        BasicFileDTO basicSrc = BeanUtils.createFrom(src,BasicFileDTO.class);
        BasicCallbackDTO basicCallback = BeanUtils.createFrom(callback,BasicCallbackDTO.class);
        BasicFileRequestDTO basicResult = fileServer.getUploadRequest(basicSrc,mode,basicCallback);
        return BeanUtils.createFrom(basicResult,FileRequestDTO.class);
    }

    @Override
    public FileRequestDTO getDownloadRequest(FileDTO src, Integer mode, CallbackDTO callback, Current current) {
        BasicFileDTO fileDTO = BeanUtils.createFrom(src,BasicFileDTO.class);
        BasicFileRequestDTO basicResult = fileServer.getDownloadRequest(fileDTO,null);
        FileRequestDTO result = BeanUtils.createFrom(basicResult,FileRequestDTO.class);
        return result;
    }


    @Override
    public Boolean isExist(FileDTO src, Current current) {
        BasicFileDTO fileDTO = BeanUtils.createFrom(src,BasicFileDTO.class);
        return fileServer.isExist(fileDTO);
    }

    @Override
    public String duplicateFile(FileDTO src, Current current) {
        BasicFileDTO fileDTO = BeanUtils.createFrom(src,BasicFileDTO.class);
        return fileServer.duplicateFile(fileDTO);
    }

    @Override
    public void deleteFile(FileDTO src, Current current) {
        BasicFileDTO fileDTO = BeanUtils.createFrom(src,BasicFileDTO.class);
        fileServer.deleteFile(fileDTO);
    }

    @Override
    public List<String> listFile(String scope, Current current) {
        return fileServer.listFile(scope);
    }

    @Override
    public List<String> listScope(Current current) {
        return fileServer.listScope();
    }

}
