package com.maoding.FileServer.Config;

import com.maoding.Common.ConstService;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreFileServer.Disk.DiskFileServer;
import com.maoding.CoreFileServer.MaodingWeb.WebFileServer;
import com.maoding.FileServer.zeroc.FileServicePrx;
import com.maoding.Notice.zeroc.NoticeServicePrx;
import com.maoding.Storage.zeroc.StorageServicePrx;
import com.maoding.User.zeroc.UserServicePrx;
import com.maoding.CoreUtils.StringUtils;
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

    private static final String DEFAULT_SERVER_TYPE_ID = ConstService.FILE_SERVER_TYPE_DISK.toString();
    private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
    private static final String DEFAULT_BASE_DIR = "c:/work/file_server";
    private static final String DEFAULT_MIRROR_BASE_DIR = "c:/work/file_server/.mirror";

    @Deprecated private CoreFileServer aliyunServer;
    @Deprecated private CoreFileServer jcifsServer;
    @Deprecated private CoreFileServer ftpServer;
    @Deprecated private CoreFileServer diskServer;
    @Deprecated private CoreFileServer webServer;
    private CoreFileServer localServer;

    private String storageServiceAdapter;
    private String userServiceAdapter;
    private String noticeServiceAdapter;

    private String serverTypeId;
    private String serverAddress;
    private String baseDir;
    private String mirrorBaseDir;

    public String getMirrorBaseDir(String mirrorBaseDir) {
        return StringUtils.isEmpty(mirrorBaseDir) ? getMirrorBaseDir() : mirrorBaseDir;
    }

    public String getMirrorBaseDir() {
        return StringUtils.isEmpty(mirrorBaseDir) ? DEFAULT_MIRROR_BASE_DIR : mirrorBaseDir;
    }

    public void setMirrorBaseDir(String mirrorBaseDir) {
        this.mirrorBaseDir = mirrorBaseDir;
    }

    public String getServerTypeId(String serverTypeId) {
        return StringUtils.isEmpty(serverTypeId) ? getServerTypeId() : serverTypeId;
    }

    public String getServerTypeId() {
        return StringUtils.isEmpty(serverTypeId) ? DEFAULT_SERVER_TYPE_ID : serverTypeId;
    }

    public void setServerTypeId(String serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

    public String getBaseDir(String baseDir) {
        return StringUtils.isEmpty(baseDir) ? getBaseDir() : baseDir;
    }

    public String getBaseDir() {
        return StringUtils.isEmpty(baseDir) ? DEFAULT_BASE_DIR : baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getServerAddress(String serverAddress) {
        return StringUtils.isEmpty(serverAddress) ? getServerAddress() : serverAddress;
    }

    public String getServerAddress() {
        return StringUtils.isEmpty(serverAddress) ? DEFAULT_SERVER_ADDRESS : serverAddress;
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

    public boolean isLocalServer(String serverTypeId,String serverAddress,String baseDir){
        return StringUtils.isSame(getServerTypeId(serverTypeId),getServerTypeId()) &&
                StringUtils.isSame(getServerAddress(serverAddress),getServerAddress()) &&
                StringUtils.isSame(getBaseDir(baseDir),getBaseDir());
    }
    private boolean isDiskServer(String serverTypeId){
        return StringUtils.isSame(ConstService.FILE_SERVER_TYPE_DISK.toString(),serverTypeId);
    }
    private boolean isWebServer(String serverTypeId){
        return StringUtils.isSame(ConstService.FILE_SERVER_TYPE_WEB.toString(),serverTypeId);
    }

    public CoreFileServer createCoreFileServer(String serverTypeId, String serverAddress,String baseDir){
        if (isDiskServer(serverTypeId)) {
            return new DiskFileServer();
        } else if (isWebServer(getServerTypeId(serverTypeId))) {
            return new WebFileServer();
        } else {
            return new PrxFileServer();
        }
    }

    public CoreFileServer getCoreFileServer(String serverTypeId, String serverAddress,String baseDir){
        serverTypeId = getServerTypeId(serverTypeId);
        serverAddress = getServerAddress(serverAddress);
        baseDir = getBaseDir(baseDir);
        CoreFileServer coreServer;
        if (isLocalServer(serverTypeId,serverAddress,baseDir)){
            if (localServer == null){
                localServer = createCoreFileServer(serverTypeId,serverAddress,baseDir);
            }
            coreServer = localServer;
        } else {
            coreServer = createCoreFileServer(serverTypeId,serverAddress,baseDir);
            baseDir = getMirrorBaseDir(baseDir);
        }

        coreServer.coreSetServerAddress(serverAddress,baseDir);
        return coreServer;
    }
    public CoreFileServer getCoreFileServer(String serverTypeId, String serverAddress){
        return getCoreFileServer(serverTypeId,serverAddress,null);
    }
    public CoreFileServer getCoreFileServer(String serverTypeId){
        return getCoreFileServer(serverTypeId,null);
    }
    public CoreFileServer getCoreFileServer(){
        return getCoreFileServer(null);
    }


    @Deprecated
    public CoreFileServer getCoreFileServer(Short serverTypeId,String serverAddress){
        return getCoreFileServer(serverTypeId.toString(),serverAddress);
    }

    @Deprecated
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
