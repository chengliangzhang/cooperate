package com.maoding.Notice;

import com.maoding.Base.BaseRemoteService;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm._TopicManagerPrxI;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/16 22:40
 * 描    述 :
 */
public class RemoteTopicManagerPrx extends BaseRemoteService<TopicManagerPrx> implements TopicManagerPrx{

    private static TopicManagerPrx topicManagerPrx = null;

    public static TopicManagerPrx getTopicManager(String serviceName,String adapterName){
        if (topicManagerPrx == null) {
            RemoteTopicManagerPrx prx = new RemoteTopicManagerPrx();
            topicManagerPrx = prx.getServicePrx(serviceName,adapterName,TopicManagerPrx.class,_TopicManagerPrxI.class);
        }
        return topicManagerPrx;
    }
    public static TopicManagerPrx getTopicManager(String adapterName){
        return getTopicManager("IceStorm/TopicManager",adapterName);
    }
    public static TopicManagerPrx getTopicManager(){
        return getTopicManager("StormSvr");
    }
}
