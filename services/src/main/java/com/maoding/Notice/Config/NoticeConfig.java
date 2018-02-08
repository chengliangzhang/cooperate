package com.maoding.Notice.Config;

import com.maoding.CoreNotice.ActiveMQ.ActiveMQClient;
import com.maoding.CoreNotice.CoreNoticeService;
import com.maoding.User.zeroc.UserServicePrx;
import com.maoding.Utils.SpringUtils;
import com.zeroc.IceStorm.TopicManagerPrx;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 12:09
 * 描    述 :
 */
@Component
@ConfigurationProperties(prefix = "notice")
public class NoticeConfig {
    private static CoreNoticeService activeMQ = null;

    private String topicAdapter;
    private String userServiceAdapter;
    private String commonTopic;

    public String getTopicAdapter() {
        return topicAdapter;
    }

    public void setTopicAdapter(String topicAdapter) {
        this.topicAdapter = topicAdapter;
    }

    public String getCommonTopic() {
        return commonTopic;
    }

    public void setCommonTopic(String commonTopic) {
        this.commonTopic = commonTopic;
    }

    public String getUserServiceAdapter() {
        return userServiceAdapter;
    }

    public void setUserServiceAdapter(String userServiceAdapter) {
        this.userServiceAdapter = userServiceAdapter;
    }

    public CoreNoticeService getCommonNoticeService(){
        if (activeMQ == null){
            activeMQ = SpringUtils.getBean(ActiveMQClient.class);
        }
        return activeMQ;
    }

    public UserServicePrx getUserService(String serverAddress){
        return RemoteUserServicePrx.getInstance(serverAddress);
    }
    public UserServicePrx getUserService(){
        return getUserService(getUserServiceAdapter());
    }

    public TopicManagerPrx getTopicManager(String serverAddress){
        return RemoteTopicManagerPrx.getInstance(serverAddress);
    }
    public TopicManagerPrx getTopicManager(){
        return getTopicManager(getTopicAdapter());
    }
}
