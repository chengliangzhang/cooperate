package com.maoding.common.config;


import com.maoding.common.remoteService.RemoteCommonServicePrx;
import com.maoding.common.remoteService.RemoteTopicManagerPrx;
import com.maoding.common.zeroc.CommonServicePrx;
import com.maoding.coreFileServer.web.CoreKeyValuePair;
import com.maoding.coreUtils.StringUtils;
import com.maoding.fileServer.zeroc.FileServicePrx;
import com.maoding.notice.zeroc.NoticeServicePrx;
import com.maoding.storage.zeroc.StorageServicePrx;
import com.maoding.user.zeroc.UserServicePrx;
import com.zeroc.IceStorm.TopicManagerPrx;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/7 18:12
 * 描    述 :
 */
@EnableAutoConfiguration
@Component
@Configuration
@ConfigurationProperties(prefix = "ice")
public class IceConfig extends BaseConfig {
    private final static String ICE_CONFIG_FILE = "--Ice.Config=";
    private final static String DEFAULT_STORAGE = null;
    private final static String DEFAULT_USER = null;
    private final static String DEFAULT_NOTICE = null;
    private final static String DEFAULT_FILE = null;
    private final static String DEFAULT_TOPIC = "IceStorm/TopicManager@StormSvr";

    private String common;
    private String storage;
    private String file;
    private String user;
    private String notice;
    private String topic;

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private CoreKeyValuePair getServiceKeyValue(String serviceConfig){
        String service = StringUtils.left(serviceConfig,StringUtils.SPLIT_CONTENT);
        String config = StringUtils.right(serviceConfig,StringUtils.SPLIT_CONTENT);
        if (StringUtils.isEmpty(config) && StringUtils.isNotEmpty(getConfig())){
            config = ICE_CONFIG_FILE + getConfig();
        }
        return new CoreKeyValuePair(service,config);
    }

    public CommonServicePrx getCommonService() {
        String serviceConfig = getProperty("common",getCommon(),null);
        CoreKeyValuePair serviceKeyValue = getServiceKeyValue(serviceConfig);
        return RemoteCommonServicePrx.getInstance(serviceKeyValue.getKey(),serviceKeyValue.getValue());
    }

    public FileServicePrx getFileService() {
        String serviceConfig = getProperty("file",getFile(),null);
        CoreKeyValuePair serviceKeyValue = getServiceKeyValue(serviceConfig);
        return getCommonService().getFileService(serviceKeyValue.getKey(),serviceKeyValue.getValue());
    }

    public StorageServicePrx getStorageService() {
        String serviceConfig = getProperty("storage",getStorage(),null);
        CoreKeyValuePair serviceKeyValue = getServiceKeyValue(serviceConfig);
        return getCommonService().getStorageService(serviceKeyValue.getKey(),serviceKeyValue.getValue());
    }

    public NoticeServicePrx getNoticeService(){
        String serviceConfig = getProperty("notice",getNotice(),null);
        CoreKeyValuePair serviceKeyValue = getServiceKeyValue(serviceConfig);
        return getCommonService().getNoticeService(serviceKeyValue.getKey(),serviceKeyValue.getValue());
    }

    public UserServicePrx getUserService() {
        String serviceConfig = getProperty("user",getUser(),null);
        CoreKeyValuePair serviceKeyValue = getServiceKeyValue(serviceConfig);
        return getCommonService().getUserService(serviceKeyValue.getKey(),serviceKeyValue.getValue());
    }

    public TopicManagerPrx getTopicManager() {
        String serviceConfig = getProperty("topic",getTopic(),null);
        CoreKeyValuePair serviceKeyValue = getServiceKeyValue(serviceConfig);
        return RemoteTopicManagerPrx.getInstance(serviceKeyValue.getKey(),serviceKeyValue.getValue());
    }
}
