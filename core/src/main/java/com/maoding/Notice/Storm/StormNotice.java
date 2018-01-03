package com.maoding.Notice.Storm;

import IceStorm.TopicManagerPrx;
import IceStorm.TopicPrx;
import com.maoding.Bean.CoreMessageDTO;
import com.maoding.Bean.CoreReceiverDTO;
import com.maoding.Config.IceConfig;
import com.maoding.Notice.CoreNotice;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 11:16
 * 描    述 :
 */
@Service("stormNotice")
public class StormNotice implements CoreNotice {
    /** 配置文件字段名 */
    private final static String GRID_LOCATION = "Ice.Default.Locator";
    private final static String ADAPTER_ID = "AdapterId";
    private final static String END_POINTS = "Endpoints";

    /** ice配置对象 */
    @Autowired
    private IceConfig iceConfig;

    private TopicManagerPrx topicManager;
    private TopicPrx topic;

    public void init(){
        //初始化连接器
        String gridLocation = (iceConfig != null) ? iceConfig.getProperty(GRID_LOCATION) : null;
        Communicator communicator = (!StringUtils.isEmpty(gridLocation)) ?
                    Util.initialize(new String[]{"--" + GRID_LOCATION + "=" + gridLocation}) :
                    Util.initialize();
    }

    /**
     * 创建通告频道
     *
     * @param topic
     */
    @Override
    public void createTopic(String topic) {
        if (topic == null) init();
    }

    /**
     * 订阅通告频道
     *
     * @param topic
     */
    @Override
    public void subscribeTopic(String topic) {

    }

    /**
     * 取消订阅
     *
     * @param topic
     */
    @Override
    public void unSubscribeTopic(String topic) {

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
