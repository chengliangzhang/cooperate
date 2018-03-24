package com.maoding.FileServer.Config;

import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.FileServer.zeroc.FileServicePrx;
import com.maoding.CoreUtils.StringUtils;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/15 19:23
 * 描    述 :
 */
public class PrxFileServer implements CoreFileServer{
    private static final String DEFAULT_ADAPTER_NAME = "FileServer";
    private static final String DEFAULT_LOCATOR_IP = "127.0.0.1";

    private FileServicePrx remote = null;
    private String adapterName = null;
    private String locatorIp = null;

    @Override
    public String coreGetServerAddress() {
        return StringUtils.isEmpty(adapterName) ? DEFAULT_ADAPTER_NAME : adapterName;
    }

    @Override
    public void coreSetServerAddress(String serverAddress) {
        this.adapterName = serverAddress;
    }


    @Override
    public String coreGetBaseDir() {
        return StringUtils.isEmpty(locatorIp) ? DEFAULT_LOCATOR_IP : locatorIp;
    }

    @Override
    public void coreSetBaseDir(String baseDir) {
        this.locatorIp = baseDir;
    }

    private String getAdapter(){
        return coreGetServerAddress() + ";" + coreGetBaseDir();
    }

    private FileServicePrx getRemote(){
        if (remote == null) {
            remote = RemoteFileServerPrx.getInstance(getAdapter());
        }
        return remote;
    }


}
