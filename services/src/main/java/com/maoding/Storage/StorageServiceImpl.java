package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.FileServer.zeroc.FileDTO;
import com.maoding.FileServer.zeroc.FileRequestDTO;
import com.maoding.FileServer.zeroc.FileService;
import com.maoding.Storage.zeroc.CooperateFileDTO;
import com.maoding.Storage.zeroc.StorageService;
import com.maoding.Storage.zeroc.StorageServicePrx;
import com.maoding.Storage.zeroc._StorageServicePrxI;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/2 14:21
 * 描    述 :
 */
@Service("storageService")
public class StorageServiceImpl extends BaseLocalService<StorageServicePrx> implements StorageService,StorageServicePrx{

    @Autowired
    private FileService fileService;

    /** 同步方式获取业务接口代理对象 */
    public static StorageServicePrx getInstance(String adapterName) {
        StorageServiceImpl prx = new StorageServiceImpl();
        return prx.getServicePrx("StorageService",adapterName, StorageServicePrx.class,_StorageServicePrxI.class);
    }
    public static StorageServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
    public FileRequestDTO requestUpload(CooperateFileDTO fileInfo, int mode, Current current) {
        //补全参数
        if (fileInfo.getFileName() == null) fileInfo.setFileName(StringUtils.getFileName(fileInfo.getLocalFile()));

        //建立获取上传参数的对象
        FileDTO fileDTO = new FileDTO();
        fileDTO.setScope(fileInfo.getDirName());
        fileDTO.setKey(fileInfo.getFileName());

        //获取上传参数
        //if (fileService == null) fileService = FileServiceImpl.getInstance();
        assert fileService != null;
        return fileService.getUploadRequest(fileDTO,mode,null,null);
    }

    @Override
    public FileRequestDTO requestDownload(CooperateFileDTO fileInfo, int mode, Current current) {
        //建立获取下载参数的对象
        FileDTO fileDTO = new FileDTO();
        fileDTO.setScope(fileInfo.getDirName());
        fileDTO.setKey(fileInfo.getFileName());

        //获取下载参数
        //if (fileService == null) fileService = FileServiceImpl.getInstance();
        assert fileService != null;
        return fileService.getDownloadRequest(fileDTO,mode,null,null);
    }

}
