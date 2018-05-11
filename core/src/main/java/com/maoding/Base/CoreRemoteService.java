package com.maoding.Base;

import com.maoding.CoreUtils.StringUtils;
import com.zeroc.Ice.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 20:12
 * 描    述 :
 */
public class CoreRemoteService<P extends ObjectPrx> extends _ObjectPrxI {
    /** 日志对象 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /** ice配置对象 */
    @Autowired
    private CoreProperties iceProperties;

    /** 查找远程服务线程 */
    private class ConnectThread extends Thread {
        /** 配置环境 */
        private String locatorIp;
        private String serviceName;
        private String adapterName;
        private String fullServiceName;
        private Class<P> proxy;
        private Class<?> impl;

        ConnectThread(String serviceName, String adapterName, Class<P> proxy, Class<?> impl) {
            this.serviceName = serviceName;
            if (!StringUtils.isEmpty(adapterName)) {
                String s[] = adapterName.split(";");
                int n = 0;
                if (n < s.length) this.adapterName = s[n++];
                if (n < s.length) this.locatorIp = s[n];
            }
            this.proxy = proxy;
            this.impl = impl;
        }

        private Communicator getCommunicator(){
            Communicator c = (iceProperties != null) ? iceProperties.getCommunicator() : CoreProperties.getDirectCommunicator(locatorIp);
            assert (c != null);
            return c;
        }

        private String getFullServiceName(){
            if (fullServiceName == null) {
                if (StringUtils.isEmpty(adapterName)) {
                    if (iceProperties != null) {
                        if (getCommunicator().getDefaultLocator() != null) {
                            String adapterId = iceProperties.getProperty(serviceName + "." + ADAPTER_ID);
                            if (!StringUtils.isEmpty(adapterId)) adapterName = "@" + adapterId;
                        }
                        if (StringUtils.isEmpty(adapterName)) {
                            String endPoints = iceProperties.getProperty(serviceName + "." + END_POINTS);
                            if (!StringUtils.isEmpty(endPoints)) adapterName = ":" + endPoints;
                        }
                    }
                } else if (adapterName.contains(".") && !adapterName.contains("-h")) {
                    if (iceProperties != null) {
                        String endPoints = iceProperties.getProperty(serviceName + "." + END_POINTS);
                        adapterName = ":" + StringUtils.replaceParam(endPoints, "-h", adapterName);
                    } else {
                        adapterName = ":tcp -h " + adapterName;
                    }
                } else if (!adapterName.startsWith(":") && !adapterName.startsWith("@")) {
                    if (adapterName.contains(".")) {
                        adapterName = ":" + adapterName;
                    } else {
                        adapterName = "@" + adapterName;
                    }
                }

                if (!StringUtils.isEmpty(adapterName) && !adapterName.startsWith(serviceName)) {
                    fullServiceName = serviceName + adapterName;
                } else {
                    fullServiceName = adapterName;
                }
            }

            return fullServiceName;
        }

        @Override
        public void run() {

            //查找服务代理
            P prx = null;
            try {
                String fullServiceName = getFullServiceName();
                if (StringUtils.isNotEmpty(fullServiceName)) {
                    prx = ObjectPrx._checkedCast(getCommunicator().stringToProxy(getFullServiceName()),
                            P.ice_staticId(), proxy, impl);
                    String serviceString = ((getCommunicator().getDefaultLocator() != null) ? "在" + getCommunicator().getDefaultLocator().toString() : "") + "找到" + getFullServiceName() + "服务";
                    if (!StringUtils.isSame(lastServiceString, serviceString)) {
                        log.info(serviceString);
                        lastServiceString = serviceString;
                    }
                }
            } catch (ConnectFailedException e) {
                String serviceString = ((communicator.getDefaultLocator() != null) ? "在" + communicator.getDefaultLocator().toString() : "") + "无法找到" + getFullServiceName() + "服务";
                if (!StringUtils.isSame(lastServiceString,serviceString)) {
                    log.warn(serviceString);
                    lastServiceString = serviceString;
                }
            }

            remotePrx = prx;
            connectThread = null;
        }
    }

    /** 配置文件字段名 */
    private final static String GRID_LOCATION = "Ice.Default.Locator";
    private final static String ADAPTER_ID = "AdapterId";
    private final static String END_POINTS = "Endpoints";

    /** ice 连接对象 */
    private static Communicator communicator = null;

    /** 远程服务代理 */
    private volatile P remotePrx = null;

    /** 最后远程连接地址 */
    private static String lastConnect = null;
    private static String lastServiceString = null;

    /** 查找远程服务线程 */
    private volatile ConnectThread connectThread = null;

    protected P getServicePrx(String serviceName, String adapterName, Class<P> proxy, Class<?> impl, P defaultPrx) {
        if (connectThread == null){
            connectThread = new ConnectThread(serviceName,adapterName,proxy,impl);
            connectThread.start();
            //如果未配置默认服务代理等待连接动作完成，否则立即返回默认服务代理
            try {
                if (defaultPrx == null) {
                    connectThread.join();
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                log.warn("远程连接线程被中断:" + e);
            }
        }

        P prx = remotePrx;
        //如果远程服务代理尚未连接成功返回默认服务代理
        if (prx != null){
            String connect = remotePrx.ice_getConnection().toString().replace('\n', ',');
            if (!StringUtils.isSame(connect, lastConnect)) {
                log.info("切换到" + connect + "的" + serviceName + "服务");
                lastConnect = connect;
            }
        } else if (defaultPrx != null){
            prx = defaultPrx;
            if (lastConnect != null) {
                log.info("切换到默认的" + serviceName + "服务");
                lastConnect = null;
            }
            lastConnect = null;
        } else {
            log.info("无法找到" + serviceName + "服务");
            lastConnect = null;
        }
        return prx;
    }
    public P getServicePrx(String serviceName, String adapterName, Class<P> proxy, Class<?> impl) {
        return getServicePrx(serviceName,adapterName,proxy,impl,null);
    }
}
