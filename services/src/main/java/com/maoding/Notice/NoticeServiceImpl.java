package com.maoding.Notice;

import com.maoding.Base.BaseLocalService;
import com.maoding.Config.IceConfig;
import com.maoding.Notice.Config.NoticeConfig;
import com.maoding.Notice.zeroc.*;
import com.maoding.User.zeroc.AccountDTO;
import com.maoding.User.zeroc.UserService;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.IceStorm.NoSuchTopic;
import com.zeroc.IceStorm.TopicExists;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm.TopicPrx;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 11:31
 * 描    述 :
 */
@Service("noticeService")
public class NoticeServiceImpl extends BaseLocalService<NoticeServicePrx> implements NoticeService, NoticeServicePrx {

    @Autowired
    UserService userService;

    @Autowired
    private NoticeConfig noticeConfig;
    private CoreNotice coreNotice;
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
     * 同步方式获取业务接口代理对象
     */
    public static NoticeServicePrx getInstance(String adapterName) {
        NoticeServiceImpl prx = new NoticeServiceImpl();
        return prx.getServicePrx("NoticeService", adapterName, NoticeServicePrx.class, _NoticeServicePrxI.class);
    }

    public static NoticeServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public void sendMessage(MessageDTO message, List<ReceiverDTO> receiverList, Current current) {
        sendMessageForAccount(userService.getCurrent(current),message,receiverList,current);
    }

    @Override
    public void sendMessageForAccount(AccountDTO account, MessageDTO message, List<ReceiverDTO> receiverList, Current current) {
        if (StringUtil.isEmpty(message.getUserId())) {
            if (account != null) message.setUserId(account.getId());
        }

        assert (topicManager != null);
        TopicPrx topicPrx = null;
        try {
            for (ReceiverDTO receiver : receiverList) {
                assert (!StringUtils.isEmpty(receiver.getTopic()));
                topicPrx = getTopicManager().retrieve(receiver.getTopic());
                ObjectPrx publisher = topicPrx.getPublisher().ice_oneway();

                boolean isFilter = false;
                if (!StringUtils.isEmpty(receiver.getUserId())) {
                    Identity receiverId = new Identity("userId",receiver.getUserId());
                    assert (publisher != null);
                    Identity targetId = publisher.ice_getIdentity();
                    if (targetId.equals(receiverId)) {
                        isFilter = true;
                    }
                }

                if (!isFilter){
                    assert (publisher != null);
                    NoticeClientPrx clientPrx = NoticeClientPrx.uncheckedCast(publisher);
                    assert (clientPrx != null);
                    clientPrx.gotEvent(message);
                }
            }
        } catch (NoSuchTopic e) {
            log.warn("没有相关频道" + e.getMessage());
        }
    }

    @Override
    public void createTopic(String topic, Current current) {
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
}
