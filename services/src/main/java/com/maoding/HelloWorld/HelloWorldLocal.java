package com.maoding.HelloWorld;

import com.maoding.Base.BaseLocalService;
import com.maoding.HelloWorld.zeroc.HelloWorldService;
import com.maoding.HelloWorld.zeroc.HelloWorldServicePrx;
import com.maoding.HelloWorld.zeroc._HelloWorldServicePrxI;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 20:04
 * 描    述 :
 */
@Service("helloWorldLocal")
public class HelloWorldLocal extends BaseLocalService<HelloWorldServicePrx> implements HelloWorldService,HelloWorldServicePrx {
    /** 同步方式获取业务接口代理对象 */
    public static HelloWorldServicePrx getInstance(String adapterName) {
        HelloWorldLocal prx = new HelloWorldLocal();
        return prx.getServicePrx("HelloWorldService",adapterName,HelloWorldServicePrx.class,_HelloWorldServicePrxI.class);
    }
    public static HelloWorldServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public String helloWorld(Current current) {
        StringBuilder s = new StringBuilder("hello");
        for (int i=0; i<10; i++) {
            s.append(",world").append(i);
        }
        return s.toString();
    }

}
