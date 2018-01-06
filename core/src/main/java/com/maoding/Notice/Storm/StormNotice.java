package com.maoding.Notice.Storm;

import com.maoding.Bean.CoreMessageDTO;
import com.maoding.Bean.CoreReceiverDTO;
import com.maoding.Config.IceConfig;
import com.maoding.Notice.CoreNotice;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.IceStorm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 11:16
 * 描    述 :
 */
@Service("stormNotice")
public class StormNotice implements CoreNotice {
    /** 日志对象 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IceConfig iceConfig;

    private static Communicator communicator = null;
    private static TopicManagerPrx topicManager = null;

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

    /**
     * 创建通告频道
     *
     * @param topic
     */
    @Override
    public void createTopic(String topic) {
        TopicPrx topicPrx;
        try {
            topicPrx = getTopicManager().retrieve(topic);
        } catch(NoSuchTopic e) {
            try {
                topicPrx = getTopicManager().create(topic);
            } catch(TopicExists ex) {
                log.warn("无法创建" + topic + "频道" + e.getMessage());
            }
        }
    }

    /**
     * 订阅通告频道
     *
     * @param topic
     */
    @Override
    public void subscribeTopic(String topic,ObjectPrx handler) {
        try {
            Map<String,String> qos = new HashMap<>();
            qos.put("retryCount","1");
            qos.put("reliability","ordered");
            TopicPrx topicPrx = getTopicManager().retrieve(topic);
            topicPrx.subscribeAndGetPublisher(qos,handler);
        } catch (NoSuchTopic | AlreadySubscribed | InvalidSubscriber | BadQoS e) {
            log.warn("无法订阅" + topic + "频道，" + e.getMessage());
        }
    }

    /**
     * 取消订阅
     *
     * @param topic
     */
    @Override
    public void unSubscribeTopic(String topic, ObjectPrx handler) {
        try {
            TopicPrx topicPrx = getTopicManager().retrieve(topic);
            topicPrx.unsubscribe(handler);
        } catch (NoSuchTopic e) {
            log.warn("无法取消订阅" + topic + "频道，" + e.getMessage());
        }

    }

    /**
     * 发布通告
     *
     * @param msg
     * @param receiver
     */
    @Override
    public void sendMessage(CoreMessageDTO msg, CoreReceiverDTO receiver) {

    }
}
