package com.maoding.common.remoteService;

import com.maoding.common.zeroc.CommonService;
import com.maoding.common.zeroc.CommonServicePrx;
import com.maoding.common.zeroc._CommonServicePrxI;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.SpringUtils;
import com.maoding.coreUtils.StringUtils;
import com.maoding.fileServer.zeroc.FileServicePrx;
import com.maoding.notice.zeroc.NoticeServicePrx;
import com.maoding.storage.zeroc.StorageServicePrx;
import com.maoding.user.zeroc.UserServicePrx;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/5/29 15:19
 * 描    述 :
 */
public class RemoteCommonServicePrx extends CoreRemoteService<CommonServicePrx> implements CommonServicePrx {
    private static CommonServicePrx lastPrx = null;
    private static CommonService commonService = null;
    private static String lastService = null;
    private static String lastConfig = null;

    private CommonService getService(){
        if (commonService == null) {
            commonService = SpringUtils.getBean(CommonService.class);
        }
        return commonService;
    }

    /** 同步方式获取业务接口代理对象 */
    public static CommonServicePrx getInstance(String service, String config) {
        if ((lastPrx == null) || (StringUtils.isNotSame(lastService,service)) || (StringUtils.isNotSame(lastConfig,config))){
            RemoteCommonServicePrx prx = new RemoteCommonServicePrx();
            if (StringUtils.isNotEmpty(service)) {
                lastPrx = prx.getServicePrx(service, config, CommonServicePrx.class, _CommonServicePrxI.class);
            } else {
                lastPrx = prx;
            }
            lastService = service;
            lastConfig = config;
        }
        return lastPrx;
    }

    @Override
    public FileServicePrx getFileService(String service, String config) {
        return getService().getFileService(service,config,null);
    }

    @Override
    public StorageServicePrx getStorageService(String service, String config) {
        return getService().getStorageService(service,config,null);
    }

    @Override
    public UserServicePrx getUserService(String service, String config) {
        return null;
    }

    @Override
    public NoticeServicePrx getNoticeService(String service, String config) {
        return null;
    }
}
