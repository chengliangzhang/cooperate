package com.maoding.common.servicePrx;

import com.maoding.common.zeroc.CustomException;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.SpringUtils;
import com.maoding.coreUtils.StringUtils;
import com.maoding.fileServer.zeroc.FileService;
import com.maoding.fileServer.zeroc.FileServicePrx;
import com.maoding.user.zeroc.AccountDTO;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 18:43
 * 描    述 :
 */
public class FileServerPrxImpl extends CoreRemoteService<FileServicePrx> implements FileServicePrx{
    private static FileServicePrx lastPrx = null;
    private static FileService localService = null;
    private static String lastService = null;
    private static String lastConfig = null;


    private FileService getLocalService(){
        if (localService == null) {
            localService = SpringUtils.getBean(FileService.class);
        }
        return localService;
    }

    /** 同步方式获取业务接口代理对象 */
    public static FileServicePrx getInstance(String service,String config) {
        if ((lastPrx == null) || (StringUtils.isNotSame(lastService,service)) || (StringUtils.isNotSame(lastConfig,config))){
            FileServerPrxImpl prx = new FileServerPrxImpl();
            if (StringUtils.isNotEmpty(service)) {
                lastPrx = prx.getServicePrx(service, config, FileServicePrx.class,  prx.getClass(), prx);
            } else {
                lastPrx = prx;
            }
            lastService = service;
            lastConfig = config;
        }
        return lastPrx;
    }

    public static FileServicePrx getInstance(String config) {
        String s = StringUtils.left(config,StringUtils.SPLIT_CONTENT);
        String c = StringUtils.right(config,StringUtils.SPLIT_CONTENT);
        return getInstance(s,c);
    }

    @Override
    public void clearAll(AccountDTO account) throws CustomException {
        getLocalService().clearAll(account,null);
    }

    @Override
    public void flushBuffer() throws CustomException {
        getLocalService().flushBuffer(null);
    }
}
