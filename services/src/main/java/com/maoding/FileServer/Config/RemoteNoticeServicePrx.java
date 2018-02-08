package com.maoding.FileServer.Config;

import com.maoding.Base.BaseRemoteService;
import com.maoding.Notice.zeroc.NoticeClientPrx;
import com.maoding.Notice.zeroc.NoticeService;
import com.maoding.Notice.zeroc.NoticeServicePrx;
import com.maoding.Notice.zeroc._NoticeServicePrxI;
import com.maoding.Utils.SpringUtils;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/17 9:50
 * 描    述 :
 */
public class RemoteNoticeServicePrx extends BaseRemoteService<NoticeServicePrx> implements NoticeServicePrx{

    private static NoticeServicePrx lastPrx = null;
    private static NoticeService noticeService = null;

    private NoticeService getUserService(){
        if (noticeService == null) {
            noticeService = SpringUtils.getBean(NoticeService.class);
        }
        return noticeService;
    }

    /** 同步方式获取业务接口代理对象 */
    public static NoticeServicePrx getInstance(String adapterName) {
        if (lastPrx == null){
            RemoteNoticeServicePrx prx = new RemoteNoticeServicePrx();
            lastPrx = prx.getServicePrx("NoticeService",adapterName,NoticeServicePrx.class,_NoticeServicePrxI.class,prx);
        }
        return lastPrx;
    }

    public static NoticeServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
    public void subscribeTopicForUser(String id, NoticeClientPrx client) {
        getUserService().subscribeTopicForUser(id,client,null);
    }

    @Override
    public List<String> listSubscribedTopic(String userId) {
        return getUserService().listSubscribedTopic(userId,null);
    }
}
