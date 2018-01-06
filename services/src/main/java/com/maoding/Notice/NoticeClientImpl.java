package com.maoding.Notice;

import com.maoding.Base.BaseLocalService;
import com.maoding.Config.IceConfig;
import com.maoding.Notice.zeroc.*;
import com.maoding.User.zeroc.AccountDTO;
import com.maoding.User.zeroc.UserService;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.*;
import com.zeroc.IceStorm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/5 23:31
 * 描    述 :
 */
@Service("noticeClient")
public class NoticeClientImpl extends BaseLocalService<NoticeClientPrx> implements NoticeClientPrx,NoticeClient{

    @Autowired
    UserService userService;

    /**
     * 同步方式获取业务接口代理对象
     */
    public static NoticeClientPrx getInstance(String adapterName) {
        NoticeClientImpl prx = new NoticeClientImpl();
        return prx.getServicePrx("NoticeClient", adapterName, NoticeClientPrx.class, _NoticeClientPrxI.class);
    }

    public static NoticeClientPrx getInstance() {
        return getInstance(null);
    }

    @Autowired
    IceConfig iceConfig;

    private static Communicator communicator = null;
    private static TopicManagerPrx topicManager = null;
    private static ObjectAdapter adapter = null;

    private TopicManagerPrx getTopicManager(){
        if (topicManager == null) {
            if (communicator == null) {
                assert (iceConfig != null);
                communicator = iceConfig.getCommunicator();
            }
            assert (communicator != null);
            topicManager = TopicManagerPrx.checkedCast(communicator.stringToProxy("IceStorm/TopicManager@StormSvr"));
        }
        return topicManager;
    }

    @Override
    public void subscribeTopicForAccount(AccountDTO account, String topic, Current current) {
        final String RETRY_COUNT_KEY = "retryCount";
        final Integer RETRY_COUNT = 1;
        final String RELIABILITY_KEY = "reliability";
        final String RELIABILITY_METHOD = "ordered";
        try {
            Map<String,String> qos = new HashMap<>();
            qos.put(RETRY_COUNT_KEY,RETRY_COUNT.toString());
            qos.put(RELIABILITY_KEY,RELIABILITY_METHOD);
            TopicPrx topicPrx = getTopicManager().retrieve(topic);
            if (adapter == null) {
                Communicator communicator = this.ice_getCommunicator();
                assert (communicator != null);
                adapter = communicator.createObjectAdapterWithEndpoints(this.getClass().getName(),"tcp:udp");
                assert (adapter != null);
                adapter.activate();
            }
            Identity uid = new Identity("userId",((account != null) && !StringUtils.isEmpty(account.getId())) ? account.getId() : UUID.randomUUID().toString());
            ObjectPrx proxy = adapter.add(this, uid);
            topicPrx.subscribeAndGetPublisher(qos, proxy);
        } catch (NoSuchTopic | AlreadySubscribed | InvalidSubscriber | BadQoS e) {
            log.warn("无法订阅" + topic + "频道，" + e.getMessage());
        }
    }

    @Override
    public void subscribeTopic(String topic, Current current) {
        subscribeTopicForAccount(userService.getCurrent(current),topic,current);
    }

    @Override
    public void gotEvent(MessageDTO msg, Current current) {
        log.info(msg.getTitle());
    }
}
