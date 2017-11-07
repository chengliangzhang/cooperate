package com.maoding.Base;

import com.maoding.Config.IceConfig;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 20:12
 * 描    述 :
 */
public class BaseRemoteService<P extends ObjectPrx> extends _ObjectPrxI {
    /** 日志对象 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /** 查找远程服务线程 */
    private class ConnectThread extends Thread {
        /** 配置环境 */

        private String serviceName;
        private Class<P> proxy;
        private Class<?> impl;

        ConnectThread(String serviceName, Class<P> proxy, Class<?> impl) {
            this.serviceName = serviceName;
            this.proxy = proxy;
            this.impl = impl;
        }

        @Override
        public void run() {
            //初始化连接器
            String gridLocation = IceConfig.getProperty(GRID_LOCATION);
            if ((communicator == null) || !StringUtils.isSame(gridLocation, lastGridLocation)) {
                communicator = (!StringUtils.isEmpty(gridLocation)) ?
                        Util.initialize(new String[]{"--" + GRID_LOCATION + "=" + gridLocation}) :
                        Util.initialize();
                lastGridLocation = gridLocation;
                if (gridLocation != null) log.info("IceGrid服务器切换到" + gridLocation);
            }

            assert (!StringUtils.isEmpty(serviceName));
            assert (communicator != null);

            P prx = null;
            //在IceGrid服务器上查找服务代理
            String adapterId = IceConfig.getProperty(serviceName + "." + ADAPTER_ID);
            if ((communicator.getDefaultLocator() != null) && (!StringUtils.isEmpty(adapterId))) {
                String svr = serviceName + "@" + adapterId;
                try {
                    prx = ObjectPrx._checkedCast(communicator.stringToProxy(svr),
                            P.ice_staticId(), proxy, impl);
                    log.info("在" + communicator.getDefaultLocator().toString() + "找到" + svr + "服务");
                } catch (ConnectionRefusedException e) {
                    log.info("在" + communicator.getDefaultLocator().toString() + "无法找到" + svr + "服务");
                    prx = null;
                }
            }

            //直接查找服务代理
            String endPoints = IceConfig.getProperty(serviceName + "." + END_POINTS);
            if ((prx == null) && (!StringUtils.isEmpty(endPoints))){
                String svr = serviceName + ":default " + StringUtils.getParam(endPoints,"-p ");
                try {
                    prx = ObjectPrx._checkedCast(communicator.stringToProxy(svr),
                            P.ice_staticId(), proxy, impl);
                    log.info("找到" + svr + "服务");
                } catch (ConnectionRefusedException e) {
                    log.info("无法找到" + svr + "服务");
                    prx = null;
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
    private static String lastGridLocation = null;

    /** 远程服务代理 */
    private volatile P remotePrx = null;

    /** 最后远程连接地址 */
    private String lastConnect = null;

    /** 查找远程服务线程 */
    private volatile ConnectThread connectThread = null;

    protected P getServicePrx(final String serviceName, final Class<P> proxy, final Class<?> impl, final P defaultPrx) {
        assert !StringUtils.isEmpty(serviceName);
        assert proxy != null;

        if (connectThread == null){
            connectThread = new ConnectThread(serviceName,proxy,((impl != null)?impl:proxy));
            connectThread.start();
            //如果未配置默认服务代理等待连接动作完成，否则立即返回默认服务代理
            try {
                if (defaultPrx == null) {
                    connectThread.join();
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                log.warn("远程连接线程被中断:" + e.getMessage());
                e.printStackTrace();
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
    protected P getServicePrx(final String serviceName, final Class<P> proxy, final Class<?> impl) {
        return getServicePrx(serviceName,proxy,impl,null);
    }
}
