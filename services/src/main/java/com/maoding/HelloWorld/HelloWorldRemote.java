package com.maoding.HelloWorld;

import com.maoding.Base.BaseRemoteService;
import com.maoding.HelloWorld.zeroc.HelloWorldService;
import com.maoding.HelloWorld.zeroc.HelloWorldServicePrx;
import com.maoding.HelloWorld.zeroc._HelloWorldServicePrxI;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 20:04
 * 描    述 :
 */
@Service("helloWorldRemote")
public class HelloWorldRemote extends BaseRemoteService<HelloWorldServicePrx> implements HelloWorldServicePrx {
    private final String SERVICE_NAME = "HelloWorldService"; //服务名称

    @Autowired
    private HelloWorldService localService;

    /** 远程服务调用接口，如果未启动远程服务，可以调用本地实现 */
    @Override
    public String helloWorld() {
        HelloWorldServicePrx prx = getServicePrx();
        return ((prx != null) && (prx != this)) ? prx.helloWorld() : localService.helloWorld((Current) null);
    }

    /** 异步调用接口，如果未启动远程服务，不进行异步调用 */
    @Override
    public CompletableFuture<String> helloWorldAsync() {
        HelloWorldServicePrx prx = getServicePrx();
        return ((prx != null) && (prx != this)) ? prx.helloWorldAsync() : null;
    }

    /** 获取业务接口代理对象，
     * 最后一个参数（默认业务接口代理）为null或没有，使用同步方式初始化 */
    @PostConstruct
    public HelloWorldServicePrx getServicePrx(){
        return getServicePrx(SERVICE_NAME,HelloWorldServicePrx.class,_HelloWorldServicePrxI.class,this);
    }
}
