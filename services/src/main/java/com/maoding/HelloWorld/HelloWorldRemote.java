package com.maoding.HelloWorld;

import com.maoding.Base.BaseRemoteService;
import com.maoding.HelloWorld.zeroc.HelloWorldService;
import com.maoding.HelloWorld.zeroc.HelloWorldServicePrx;
import com.maoding.HelloWorld.zeroc._HelloWorldServicePrxI;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 20:04
 * 描    述 :
 */
@Service("helloWorldRemote")
public class HelloWorldRemote extends BaseRemoteService<HelloWorldServicePrx> implements HelloWorldServicePrx {
    /** 异步方式获取业务接口代理对象 */
    public static HelloWorldServicePrx getInstance(String adapterName) {
        HelloWorldRemote prx = new HelloWorldRemote();
        return prx.getServicePrx("HelloWorldService",adapterName,HelloWorldServicePrx.class,_HelloWorldServicePrxI.class,prx);
    }
    public static HelloWorldServicePrx getInstance(){
        return getInstance(null);
    }

    @Autowired
    private HelloWorldService localService;

    /** 远程服务调用接口，如果未启动远程服务，可以调用本地实现 */
    @Override
    public String helloWorld() {
        HelloWorldServicePrx prx = getInstance();
        return ((prx != null) && (prx != this)) ? prx.helloWorld() : localService.helloWorld((Current) null);
    }

    /** 异步调用接口，如果未启动远程服务，不进行异步调用 */
    @Override
    public CompletableFuture<String> helloWorldAsync() {
        HelloWorldServicePrx prx = getInstance();
        return ((prx != null) && (prx != this)) ? prx.helloWorldAsync() : null;
    }

}
