package com.maoding.User;

import com.maoding.Base.BaseLocalService;
import com.maoding.User.zeroc.UserRelatedDTO;
import com.maoding.User.zeroc.UserService;
import com.maoding.User.zeroc.UserServicePrx;
import com.maoding.User.zeroc._UserServicePrxI;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/19 18:34
 * 描    述 :
 */
@Service("userService")
public class UserServiceImpl extends BaseLocalService<UserServicePrx> implements UserService,UserServicePrx {

    /** 同步方式获取业务接口代理对象 */
    public static UserServicePrx getInstance(String adapterName) {
        UserServiceImpl prx = new UserServiceImpl();
        return prx.getServicePrx("StorageService",adapterName, UserServicePrx.class,_UserServicePrxI.class);
    }
    public static UserServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
    public UserRelatedDTO getUserRelatedInfo(String userId, Current current) {
        return null;
    }
}
