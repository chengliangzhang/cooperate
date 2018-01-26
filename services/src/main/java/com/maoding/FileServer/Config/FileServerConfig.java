package com.maoding.FileServer.Config;

import com.maoding.Common.ConstService;
import com.maoding.Const.FileServerConst;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreFileServer.Disk.DiskFileServer;
import com.maoding.CoreFileServer.FastFDS.FastFDSServer;
import com.maoding.CoreFileServer.Ftp.FtpServer;
import com.maoding.CoreFileServer.Jcifs.JcifsServer;
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

    private CoreFileServer fastFDSServer;
    private CoreFileServer aliyunServer;
    private CoreFileServer jcifsServer;
    private CoreFileServer ftpServer;
    private CoreFileServer discServer;
    private CoreFileServer webServer;

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

    public CoreFileServer getFileServer(Short serverTypeId, String serverAddress){
        if ((serverTypeId == null) || (ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(serverTypeId))) serverTypeId = Short.parseShort(getFileServerType().toString());
        CoreFileServer coreServer = null;
        if (ConstService.FILE_SERVER_TYPE_DISK.equals(serverTypeId)){
            if (discServer == null) discServer = new DiskFileServer();
            coreServer = discServer;
        } else if (ConstService.FILE_SERVER_TYPE_WEB.equals(serverTypeId)) {
            if (webServer == null) webServer = new WebFileServer();
            coreServer = webServer;
        }
        return coreServer;
    }

    private String getBaseDir(String serverAddress){
        if (StringUtils.isEmpty(serverAddress) || !serverAddress.contains("|")) return null;
        String[] s = StringUtils.split(serverAddress,"|");
        return s[s.length - 1];
    }

    private String getServerAddress(String serverAddress){
        if (StringUtils.isEmpty(serverAddress)) return null;
        String[] s = StringUtils.split(serverAddress,"|");
        return s[0];
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
            if (discServer == null) discServer = new DiskFileServer();
            return discServer;
        } else {
            if (discServer == null) discServer = new DiskFileServer();
            return discServer;
        }
    }

    public FileServicePrx getRemoteFileService(String serverAddress){
        return RemoteFileServerPrx.getInstance(serverAddress);
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
