package com.maoding.FileServer.Config;

import com.maoding.Common.ConstService;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreFileServer.Disk.DiskFileServer;
import com.maoding.CoreFileServer.MaodingWeb.WebFileServer;
import com.maoding.FileServer.zeroc.FileServicePrx;
import com.maoding.Notice.zeroc.NoticeServicePrx;
import com.maoding.Storage.zeroc.StorageServicePrx;
import com.maoding.User.zeroc.UserServicePrx;
import com.maoding.Utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 12:09
 * 描    述 :
 */
@Component
@ConfigurationProperties(prefix = "fileserver")
public class FileServerConfig {
    public static final Integer SERVER_TYPE_LOCAL = 0;
    public static final Integer SERVER_TYPE_REMOTE = 1;

    private static final Short DEFAULT_SERVER_TYPE_ID = ConstService.FILE_SERVER_TYPE_DISK;
    private static final String DEFAULT_SERVER_ADDRESS = "FileServer";

    private CoreFileServer aliyunServer;
    private CoreFileServer jcifsServer;
    private CoreFileServer ftpServer;
    private CoreFileServer diskServer;
    private CoreFileServer webServer;

    private String storageServiceAdapter;
    private String userServiceAdapter;
    private String noticeServiceAdapter;

    private Short serverTypeId;
    private String serverAddress;

    public void setServerTypeId(Short serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

    public Short getServerTypeId() {
        return (serverTypeId == null) ? DEFAULT_SERVER_TYPE_ID : serverTypeId;
    }

    public String getServerAddress() {
        return (serverAddress == null) ? DEFAULT_SERVER_ADDRESS : serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getStorageServiceAdapter() {
        return storageServiceAdapter;
    }

    public void setStorageServiceAdapter(String storageServiceAdapter) {
        this.storageServiceAdapter = storageServiceAdapter;
    }

    public String getUserServiceAdapter() {
        return userServiceAdapter;
    }

    public void setUserServiceAdapter(String userServiceAdapter) {
        this.userServiceAdapter = userServiceAdapter;
    }

    public String getNoticeServiceAdapter() {
        return noticeServiceAdapter;
    }

    public void setNoticeServiceAdapter(String noticeServiceAdapter) {
        this.noticeServiceAdapter = noticeServiceAdapter;
    }

    public CoreFileServer getCoreFileServer(Short serverTypeId, String serverAddress){
        if ((serverTypeId == null) || (ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(serverTypeId))) {
            serverTypeId = getServerTypeId();
            if (serverTypeId == null) serverTypeId = ConstService.FILE_SERVER_TYPE_DISK;
        }
        CoreFileServer coreServer = null;
        if (ConstService.FILE_SERVER_TYPE_DISK.equals(serverTypeId)){
            if (diskServer == null) {
                diskServer = new DiskFileServer();
                diskServer.coreSetServerAddress(StringUtils.getFileServerBaseDir(serverAddress));
            }
            coreServer = diskServer;
        } else if (ConstService.FILE_SERVER_TYPE_WEB.equals(serverTypeId)) {
            if (webServer == null) {
                webServer = new WebFileServer();
                webServer.coreSetServerAddress(serverAddress);
            }
            coreServer = webServer;
        }
        return coreServer;
    }

    public CoreFileServer getCoreFileServer(){
        return getCoreFileServer(getServerTypeId(),getServerAddress());
//        if (ConstService.FILE_SERVER_TYPE_ALIYUN.equals(serverTypeId)) {
////            if (aliyunServer == null) aliyunServer = new AliyunServer();
//            return aliyunServer;
//        } else if (ConstService.FILE_SERVER_TYPE_CIFS.equals(serverTypeId)){
//            if (jcifsServer == null) jcifsServer = new JcifsServer();
//            return jcifsServer;
//        } else if (ConstService.FILE_SERVER_TYPE_FTP.equals(serverTypeId)){
//            if (ftpServer == null) ftpServer = new FtpServer();
//            return ftpServer;
//        } else if (ConstService.FILE_SERVER_TYPE_DISK.equals(serverTypeId)){
//            if (diskServer == null) diskServer = new DiskFileServer();
//            return diskServer;
//        } else {
//            if (diskServer == null) diskServer = new DiskFileServer();
//            return diskServer;
//        }
    }

    public FileServicePrx getRemoteFileService(String serverAddress){
        if (serverAddress == null) serverAddress = getServerAddress();
        return RemoteFileServerPrx.getInstance(StringUtils.getFileServerAddress(serverAddress));
    }

    public StorageServicePrx getStorageService(String serverAddress){
        return RemoteStorageServicePrx.getInstance(serverAddress);
    }
    public StorageServicePrx getStorageService(){
        return getStorageService(getStorageServiceAdapter());
    }

    public UserServicePrx getUserService(String serverAddress){
        return RemoteUserServicePrx.getInstance(serverAddress);
    }
    public UserServicePrx getUserService(){
        return getUserService(getUserServiceAdapter());
    }

    public NoticeServicePrx getNoticeService(String serverAddress){
        return RemoteNoticeServicePrx.getInstance(serverAddress);
    }
    public NoticeServicePrx getNoticeService(){
        return getNoticeService(getNoticeServiceAdapter());
    }
}
