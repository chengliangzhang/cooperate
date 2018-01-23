package com.maoding.Notice;

import com.maoding.Base.BaseLocalService;
import com.maoding.Common.ConstService;
import com.maoding.Config.IceConfig;
import com.maoding.CoreNotice.CoreNoticeClient;
import com.maoding.Notice.Config.NoticeConfig;
import com.maoding.Notice.zeroc.*;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.IceStorm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 11:31
 * 描    述 :
 */
@Service("noticeService")
public class NoticeServiceImpl extends BaseLocalService<NoticeServicePrx> implements NoticeService, NoticeServicePrx, CoreNoticeClient {

    @Autowired
    IceConfig iceConfig;

    @Autowired
    NoticeConfig noticeConfig;

    @Override
    public void subscribeTopicForWeb(String topic, NoticeClientPrx client, Current current) {
        TopicPrx topicPrx = getTopic("Web" + topic,current);
        subscribeTopic("Web" + topic,client,current);
        NoticeClientImpl noticeClient = new NoticeClientImpl(topicPrx);
        noticeConfig.getActiveMQ().subscribeTopic(topic,noticeClient);
    }

    @Override
    public void subscribeTopicForUser(String id, NoticeClientPrx client, Current current) {
        subscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_USER) + id,client,current);
    }

    @Override
    public void subscribeTopicForTask(String id, NoticeClientPrx client, Current current) {
        subscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_TASK) + id,client,current);
    }

    @Override
    public void subscribeTopicForProject(String id, NoticeClientPrx client, Current current) {
        subscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_PROJECT) + id,client,current);
    }

    @Override
    public void subscribeTopicForCompany(String id, NoticeClientPrx client, Current current) {
        subscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_COMPANY) + id,client,current);
    }

    @Override
    public void subscribeTopicForTaskList(List<String> idList, NoticeClientPrx client, Current current) {
        for (String id : idList) {
            subscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_TASK) + id, client, current);
        }
    }

    @Override
    public void subscribeTopicForProjectList(List<String> idList, NoticeClientPrx client, Current current) {
        for (String id : idList) {
            subscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_PROJECT) + id, client, current);
        }
    }

    @Override
    public void subscribeTopicForCompanyList(List<String> idList, NoticeClientPrx client, Current current) {
        for (String id : idList) {
            subscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_COMPANY) + id, client, current);
        }
    }

    @Override
    public void unSubscribeTopicForUser(String id, NoticeClientPrx client, Current current) {
        unSubscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_USER) + id, client, current);
    }

    @Override
    public void unSubscribeTopicForTask(String id, NoticeClientPrx client, Current current) {
        unSubscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_TASK) + id, client, current);
    }

    @Override
    public void unSubscribeTopicForProject(String id, NoticeClientPrx client, Current current) {
        unSubscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_PROJECT) + id, client, current);
    }

    @Override
    public void unSubscribeTopicForCompany(String id, NoticeClientPrx client, Current current) {
        unSubscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_COMPANY) + id, client, current);
    }

    @Override
    public void unSubscribeTopicForTaskList(List<String> idList, NoticeClientPrx client, Current current) {
        for (String id : idList) {
            unSubscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_TASK) + id, client, current);
        }
    }

    @Override
    public void unSubscribeTopicForProjectList(List<String> idList, NoticeClientPrx client, Current current) {
        for (String id : idList) {
            unSubscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_PROJECT) + id, client, current);
        }
    }

    @Override
    public void unSubscribeTopicForCompanyList(List<String> idList, NoticeClientPrx client, Current current) {
        for (String id : idList) {
            unSubscribeTopic(ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_COMPANY) + id, client, current);
        }
    }

    private TopicManagerPrx getTopicManager(){
        return RemoteTopicManagerPrx.getTopicManager("StormSvr;192.168.13.140");
    }

    @Override
    public List<String> listTopic(Current current) {
        List<String> topicList = new ArrayList<>();
        Map<String, TopicPrx> topicMap = getTopicManager().retrieveAll();
        for (Map.Entry<String,TopicPrx> entry : topicMap.entrySet()){
            topicList.add(entry.getKey());
        }
        return topicList;
    }

    @Override
    public List<String> listSubscribedTopic(String userId, Current current) {
        List<String> topicList = new ArrayList<>();
        Map<String, TopicPrx> topicMap = getTopicManager().retrieveAll();
        for (Map.Entry<String,TopicPrx> entry : topicMap.entrySet()){
            TopicPrx topicPrx = entry.getValue();
            Identity[] identities = topicPrx.getSubscribers();
            boolean isSubscribed = false;
            if (identities != null){
                for (Identity id : identities){
                    if (StringUtils.isSame(id.name,"userId") && StringUtils.isSame(id.category,userId)){
                        isSubscribed = true;
                        break;
                    }
                }
            }
            if (isSubscribed) {
                topicList.add(entry.getKey());
            }
        }
        return topicList;
    }

    @Override
    public void noticeToUser(MessageDTO message, String id, Current current) {
        notice(message, ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_USER) + id,current);
    }

    @Override
    public void noticeToTask(MessageDTO message, String id, Current current) {
        notice(message, ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_TASK) + id,current);
    }

    @Override
    public void noticeToProject(MessageDTO message, String id, Current current) {
        notice(message, ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_PROJECT) + id,current);
    }

    @Override
    public void noticeToCompany(MessageDTO message, String id, Current current) {
        notice(message, ConstService.getTopicPrefix(ConstService.NOTICE_SCOPE_COMPANY) + id,current);
    }

    @Override
    public void notice(MessageDTO message, String topic, Current current) {
        TopicPrx topicPrx = getTopic(topic,current);
        assert (topicPrx != null);
        ObjectPrx publisher = topicPrx.getPublisher().ice_oneway();
        assert (publisher != null);
        NoticeClientPrx clientPrx = NoticeClientPrx.uncheckedCast(publisher);
        assert (clientPrx != null);
        clientPrx.notice(message);
    }

    @Override
    public void subscribeTopic(String topic, NoticeClientPrx client, Current current) {
        final String RETRY_COUNT_KEY = "retryCount";
        final Integer RETRY_COUNT = 1;
        final String RELIABILITY_KEY = "reliability";
        final String RELIABILITY_METHOD = "ordered";
        try {
            Map<String,String> qos = new HashMap<>();
            qos.put(RETRY_COUNT_KEY,RETRY_COUNT.toString());
            qos.put(RELIABILITY_KEY,RELIABILITY_METHOD);
            TopicPrx topicPrx = getTopic(topic,current);
            assert (topicPrx != null);
            for (Identity id : topicPrx.getSubscribers()){
                if (id.equals(client.ice_getIdentity())){
                    topicPrx.unsubscribe(client);
                    break;
                }
            }
            topicPrx.subscribeAndGetPublisher(qos, client);
        } catch (AlreadySubscribed | InvalidSubscriber | BadQoS e) {
            log.warn("无法订阅" + topic + "频道，" + e);
        }
    }

    @Override
    public void unSubscribeTopic(String topic, NoticeClientPrx client, Current current) {
        try {
            TopicPrx topicPrx = getTopicManager().retrieve(topic);
            assert (topicPrx != null);
            topicPrx.unsubscribe(client);
            Identity[] identities = topicPrx.getSubscribers();
            if ((identities == null) || (identities.length == 0)){
                topicPrx.destroy();
            }
        } catch (NoSuchTopic noSuchTopic) {
            log.warn("没有相关频道");
        }
    }

    private TopicPrx getTopic(String topic, Current current){
        createTopic(topic,current);
        TopicPrx topicPrx = null;
        try {
            topicPrx = getTopicManager().retrieve(topic);
        } catch (NoSuchTopic e) {
            log.warn("没有" + topic + "频道");
        }
        return topicPrx;
    }

    @Override
    public void createTopic(String topic, Current current) {
        try {
            getTopicManager().retrieve(topic);
        } catch(NoSuchTopic e) {
            try {
                getTopicManager().create(topic);
            } catch(TopicExists ex) {
                log.warn("无法创建" + topic + "频道" + e);
            }
        }
    }

    /**
     * 同步方式获取业务接口代理对象
     */
    public static NoticeServicePrx getInstance(String adapterName) {
        NoticeServiceImpl prx = new NoticeServiceImpl();
        return prx.getServicePrx("NoticeService", adapterName, NoticeServicePrx.class, _NoticeServicePrxI.class);
    }

    public static NoticeServicePrx getInstance() {
        return getInstance(null);
    }
}
