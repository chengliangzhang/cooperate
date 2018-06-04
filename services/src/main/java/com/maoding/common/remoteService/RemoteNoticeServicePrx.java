package com.maoding.common.remoteService;

import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.SpringUtils;
import com.maoding.coreUtils.StringUtils;
import com.maoding.notice.zeroc.*;
import com.maoding.user.zeroc.AccountDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/17 9:50
 * 描    述 :
 */
public class RemoteNoticeServicePrx extends CoreRemoteService<NoticeServicePrx> implements NoticeServicePrx{

    private static NoticeServicePrx lastPrx = null;
    private static NoticeService noticeService = null;
    private static String lastService = null;
    private static String lastConfig = null;

    private NoticeService getNoticeService(){
        if (noticeService == null) {
            noticeService = SpringUtils.getBean(NoticeService.class);
        }
        return noticeService;
    }

    /** 异步方式获取业务接口代理对象 */
    public static NoticeServicePrx getInstance(String service, String config) {
        if ((lastPrx == null) || (StringUtils.isNotSame(lastService,service)) || (StringUtils.isNotSame(lastConfig,config))){
            RemoteNoticeServicePrx prx = new RemoteNoticeServicePrx();
            lastPrx = prx.getServicePrx(service, config, NoticeServicePrx.class, _NoticeServicePrxI.class, prx);
            lastService = service;
            lastConfig = config;
        }
        return lastPrx;
    }

    @Override
    public void subscribeTopicForUser(String id, NoticeClientPrx client) {
        getNoticeService().subscribeTopicForUser(id,client,null);
    }

    @Override
    public List<String> listSubscribedTopic(String userId) {
        return getNoticeService().listSubscribedTopic(userId,null);
    }

    @Override
    public void sendNotice(NoticeRequestDTO request) {
        getNoticeService().sendNotice(request,null);
    }

    @Override
    public void sendNoticeForAccount(AccountDTO account, NoticeRequestDTO request) {
        getNoticeService().sendNoticeForAccount(account,request,null);
    }

    @Override
    public CompletableFuture<Void> sendNoticeAsync(NoticeRequestDTO request) {
        new Thread(){
            @Override
            public void run(){
                getNoticeService().sendNotice(request,null);
            }
        }.start();
        return null;
    }

    @Override
    public CompletableFuture<Void> sendNoticeForAccountAsync(AccountDTO account, NoticeRequestDTO request) {
        new Thread(){
            @Override
            public void run(){
                getNoticeService().sendNoticeForAccount(account,request,null);
            }
        }.start();
        return null;
    }
}
