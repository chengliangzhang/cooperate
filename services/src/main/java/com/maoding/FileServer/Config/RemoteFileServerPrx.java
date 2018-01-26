package com.maoding.FileServer.Config;

import com.maoding.Base.BaseRemoteService;
import com.maoding.FileServer.zeroc.FileServicePrx;
import com.maoding.FileServer.zeroc._FileServicePrxI;
import com.maoding.Utils.StringUtils;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 18:43
 * 描    述 :
 */
public class RemoteFileServerPrx extends BaseRemoteService<FileServicePrx> implements FileServicePrx{
    private static FileServicePrx lastPrx = null;
    private static String lastAdapterName = null;

    /** 同步方式获取业务接口代理对象 */
    public static FileServicePrx getInstance(String adapterName) {
        if ((lastPrx == null) || (!StringUtils.isSame(adapterName,lastAdapterName))){
            RemoteFileServerPrx prx = new RemoteFileServerPrx();
            lastPrx = prx.getServicePrx("FileService",adapterName,FileServicePrx.class,_FileServicePrxI.class);
            lastAdapterName = adapterName;
        }
        return lastPrx;
    }
    public static FileServicePrx getInstance(){
        return getInstance(null);
    }
}
