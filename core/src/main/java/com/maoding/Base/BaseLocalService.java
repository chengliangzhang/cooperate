package com.maoding.Base;

import com.maoding.Utils.SpringContextUtils;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.Ice._ObjectPrxI;
import com.zeroc.IceBox.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/13 9:34
 * 描    述 :
 */
public class BaseLocalService extends _ObjectPrxI implements Service, com.zeroc.Ice.Object{
    /** 日志对象，不能用于static方法 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /** icebox代理 */
    private ObjectAdapter adapter = null;

    @Override
    public void start(String s, Communicator communicator, String[] strings) {
        adapter = communicator.createObjectAdapter(s);
        adapter.add(SpringContextUtils.getApplicationContext().getBean(this.getClass()), Util.stringToIdentity(s));
        adapter.activate();
    }

    @Override
    public void stop() {
        adapter.destroy();
    }
}
