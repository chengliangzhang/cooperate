package com.maoding.CoreNotice.Storm;

import com.maoding.Base.IceConfig;
import com.maoding.CoreNotice.CoreMessageDTO;
import com.maoding.CoreNotice.CoreNoticeService;
import com.maoding.CoreNotice.CoreReceiverDTO;
import com.zeroc.Ice.Communicator;
import com.zeroc.IceStorm.NoSuchTopic;
import com.zeroc.IceStorm.TopicExists;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm.TopicPrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 11:16
 * 描    述 :
 */
@Service("stormNotice")
public class StormClient implements CoreNoticeService {
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
                log.warn("无法创建" + topic + "频道" + e);
            }
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
