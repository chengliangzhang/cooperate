package com.maoding.notice.config;

import com.maoding.common.remoteService.RemoteTopicManagerPrx;
import com.maoding.coreNotice.CoreNoticeService;
import com.maoding.coreNotice.activeMQ.ActiveMQClient;
import com.maoding.coreUtils.SpringUtils;
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
    private final static String DEFAULT_TOPIC_MANAGER_SERVICE = "IceStorm/TopicManager@StormSvr";
    private final static String DEFAULT_COMMUNICATE_CONFIG = "--Ice.Default.Locator=IceGrid/Locator:tcp -h 127.0.0.1 -p 4061";

    private static CoreNoticeService activeMQ = null;

    private String topicService;
    private String userServiceAdapter;
    private String commonTopic;
    private String communicateConfig;

    public String getCommunicateConfig() {
        return communicateConfig;
    }

    public void setCommunicateConfig(String communicateConfig) {
        this.communicateConfig = communicateConfig;
    }

    public String getTopicService() {
        return topicService;
    }

    public void setTopicService(String topicService) {
        this.topicService = topicService;
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

    public TopicManagerPrx getTopicManager(String service, String config){
        return RemoteTopicManagerPrx.getInstance(service,config);
    }
    public TopicManagerPrx getTopicManager(){
        return getTopicManager(getTopicService(),getCommunicateConfig());
    }
}
