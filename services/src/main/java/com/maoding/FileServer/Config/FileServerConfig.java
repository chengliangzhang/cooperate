package com.maoding.FileServer.Config;

import com.maoding.Const.FileServerConst;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreFileServer.Disk.DiskFileServer;
import com.maoding.CoreFileServer.FastFDS.FastFDSServer;
import com.maoding.CoreFileServer.Ftp.FtpServer;
import com.maoding.CoreFileServer.Jcifs.JcifsServer;
import com.maoding.Notice.zeroc.NoticeServicePrx;
import com.maoding.Storage.zeroc.StorageServicePrx;
import com.maoding.User.zeroc.UserServicePrx;
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

    private CoreFileServer fastFDSServer;
    private CoreFileServer aliyunServer;
    private CoreFileServer jcifsServer;
    private CoreFileServer ftpServer;
    private CoreFileServer localServer;

    private String storageServiceAdapter;
    private String userServiceAdapter;
    private String noticeServiceAdapter;

    private Integer type;

    public void setFileServerType(Integer type) {
        this.type = type;
    }

    public Integer getFileServerType() {
        return type;
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

    public CoreFileServer getFileServer(){
        if (FileServerConst.FILE_SERVER_TYPE_ALIYUN.equals(type)) {
//            if (aliyunServer == null) aliyunServer = new AliyunServer();
            return aliyunServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_FASTFDS.equals(type)){
            if (fastFDSServer == null) fastFDSServer= new FastFDSServer();
            return fastFDSServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_CIFS.equals(type)){
            if (jcifsServer == null) jcifsServer = new JcifsServer();
            return jcifsServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_FTP.equals(type)){
            if (ftpServer == null) ftpServer = new FtpServer();
            return ftpServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_LOCAL.equals(type)){
            if (localServer == null) localServer = new DiskFileServer();
            return localServer;
        } else {
            if (localServer == null) localServer = new DiskFileServer();
            return localServer;
        }
    }

    public StorageServicePrx getStorageService(){
        return RemoteStorageServicePrx.getInstance(getStorageServiceAdapter());
    }

    public UserServicePrx getUserService(){
        return RemoteUserServicePrx.getInstance(getUserServiceAdapter());
    }

    public NoticeServicePrx getNoticeService(){
        return RemoteNoticeServicePrx.getInstance(getNoticeServiceAdapter());
    }
}
