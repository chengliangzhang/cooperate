package com.maoding.fileServer.config;

import com.maoding.common.remoteService.RemoteFileServerPrx;
import com.maoding.coreFileServer.CoreFileServer;
import com.maoding.fileServer.zeroc.FileServicePrx;
import com.maoding.coreUtils.StringUtils;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/15 19:23
 * 描    述 :
 */
public class PrxFileServer implements CoreFileServer{
    private static final String DEFAULT_SERVICE = "FileService:tcp -h 127.0.0.1 -p 10002";

    private FileServicePrx remote = null;
    private String service = null;
    private String config = null;

    @Override
    public String coreGetServerAddress() {
        return StringUtils.isEmpty(service) ? DEFAULT_SERVICE : service;
    }

    @Override
    public void coreSetServerAddress(String serverAddress) {
        this.service = serverAddress;
    }


    @Override
    public String coreGetBaseDir() {
        return config;
    }

    @Override
    public void coreSetBaseDir(String baseDir) {
        this.config = baseDir;
    }

    private String getService(){
        return coreGetServerAddress();
    }

    private String getConfig(){
        return coreGetBaseDir();
    }

    private FileServicePrx getRemote(){
        if (remote == null) {
            remote = RemoteFileServerPrx.getInstance(getService(),getConfig());
        }
        return remote;
    }


}
