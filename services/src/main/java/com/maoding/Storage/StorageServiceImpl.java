package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Storage.zeroc.*;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        if (fileInfo.getName() == null) fileInfo.setName(StringUtils.getFileName(fileInfo.getLocalFile()));

        //建立获取上传参数的对象
        FileDTO fileDTO = new FileDTO();
        fileDTO.setScope("");
        fileDTO.setKey(fileInfo.getName());

        //获取上传参数
        //if (fileService == null) fileService = FileServiceImpl.getInstance();
        assert fileService != null;
        return fileService.getUploadRequest(fileDTO,mode,null,null);
    }

    @Override
    public FileRequestDTO requestDownload(CooperateFileDTO fileInfo, int mode, Current current) {
        //建立获取下载参数的对象
        FileDTO fileDTO = new FileDTO();
        fileDTO.setScope("");
        fileDTO.setKey(fileInfo.getName());

        //获取下载参数
        //if (fileService == null) fileService = FileServiceImpl.getInstance();
        assert fileService != null;
        return fileService.getDownloadRequest(fileDTO,mode,null,null);
    }

    @Override
    public List<CooperateDirDTO> listCooperationDir(CooperationQueryDTO query, Current current) {
        return null;
    }

    @Override
    public List<CooperateFileDTO> listFileLink(FileDTO fileDTO, Current current) {
        return null;
    }

    @Override
    public boolean modifyFileInfo(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public DownloadResultDTO downloadFrom(DownloadRequestDTO request, String address, int mode, Current current) {
        return null;
    }

    @Override
    public CooperateFileDTO uploadCallback(Map<String, String> params, Current current) {
        return null;
    }

    @Override
    public void downloadCallback(Map<String, String> params, Current current) {

    }

    @Override
    public CooperateFileDTO finishUpload(CooperateFileDTO fileInfo, FileDTO fileDTO, Current current) {
        return null;
    }

    @Override
    public void finishDownload(CooperateFileDTO fileInfo, Current current) {

    }

    @Override
    public boolean replaceFile(CooperateFileDTO fileInfo, FileDTO fileDTO, Current current) {
        return false;
    }

    @Override
    public boolean deleteFile(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public boolean createDirectory(String path, Current current) {
        return false;
    }

    @Override
    public boolean deleteDirectory(String path, boolean force, Current current) {
        return false;
    }

    @Override
    public CooperateFileDTO duplicateFile(CooperateFileDTO fileInfo, String path, Current current) {
        return null;
    }

    @Override
    public CooperateFileDTO createFileLink(CooperateFileDTO fileInfo, String path, Current current) {
        return null;
    }

    @Override
    public boolean duplicateDirectory(String path, String parent, Current current) {
        return false;
    }

    @Override
    public boolean restoreFile(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public boolean restoreDirectory(String path, Current current) {
        return false;
    }

    @Override
    public boolean lockFile(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public boolean unlockFile(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public long getFree(CooperationQueryDTO query, Current current) {
        return 0;
    }

    @Override
    public boolean isLock(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public CooperateFileDTO getFileInfo(String fileId, Current current) {
        return null;
    }

    @Override
    public int getLinkCount(FileDTO fileDTO, Current current) {
        return 0;
    }

    @Override
    public CooperateFileDTO createVersion(CooperateFileDTO fileInfo, String version, Current current) {
        return null;
    }

    @Override
    public boolean abortUpload(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public boolean abortDownload(CooperateFileDTO fileInfo, Current current) {
        return false;
    }
}
