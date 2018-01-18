package com.maoding.CoreNotice.Storm;

import com.maoding.Base.BaseRemoteService;
import com.zeroc.Ice.Current;
import com.zeroc.IceStorm.*;

import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/5 15:41
 * 描    述 :
 */
public class StormService extends BaseRemoteService<TopicManagerPrx> implements TopicManagerPrx,TopicManager{
    @Override
    public TopicPrx create(String s, Current current) throws TopicExists {
        return null;
    }

    @Override
    public TopicPrx retrieve(String s, Current current) throws NoSuchTopic {
        return null;
    }

    @Override
    public Map<String, TopicPrx> retrieveAll(Current current) {
        return null;
    }

    @Override
    public Map<String, String> getSliceChecksums(Current current) {
        return null;
    }
}
