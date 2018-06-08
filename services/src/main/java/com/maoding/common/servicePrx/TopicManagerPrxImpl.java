package com.maoding.common.servicePrx;

import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.StringUtils;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm._TopicManagerPrxI;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/16 22:40
 * 描    述 :
 */
public class TopicManagerPrxImpl extends CoreRemoteService<TopicManagerPrx> implements TopicManagerPrx{
    private static TopicManagerPrx lastPrx = null;
    private static String lastService = null;
    private static String lastConfig = null;

    /** 同步方式获取业务接口代理对象 */
    public static TopicManagerPrx getInstance(String service, String config){
        if ((lastPrx == null) || (StringUtils.isNotSame(lastService,service)) || (StringUtils.isNotSame(lastConfig,config))){
            TopicManagerPrxImpl prx = new TopicManagerPrxImpl();
            lastPrx = prx.getServicePrx(service, config, TopicManagerPrx.class, _TopicManagerPrxI.class);
            lastService = service;
            lastConfig = config;
        }
        return lastPrx;
    }
}
