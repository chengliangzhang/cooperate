package com.maoding.Notice.Config;

import com.maoding.Base.BaseRemoteService;
import com.maoding.User.zeroc.*;
import com.maoding.Utils.SpringUtils;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 11:54
 * 描    述 :
 */
public class RemoteUserServicePrx extends BaseRemoteService<UserServicePrx> implements UserServicePrx {

    private static UserServicePrx lastPrx = null;
    private static UserService userService = null;

    private UserService getUserService(){
        if (userService == null) {
            userService = SpringUtils.getBean(UserService.class);
        }
        return userService;
    }

    /** 同步方式获取业务接口代理对象 */
    public static UserServicePrx getInstance(String adapterName) {
        if (lastPrx == null){
            RemoteUserServicePrx prx = new RemoteUserServicePrx();
            lastPrx = prx.getServicePrx("UserService",adapterName,UserServicePrx.class,_UserServicePrxI.class,prx);
        }
        return lastPrx;
    }
    public static UserServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
    public UserJoinDTO listUserJoin() {
        return getUserService().listUserJoin(null);
    }

    @Override
    public UserJoinDTO listUserJoinForAccount(AccountDTO account) {
        return getUserService().listUserJoinForAccount(account,null);
    }
}
