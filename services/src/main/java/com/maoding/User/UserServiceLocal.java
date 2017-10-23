package com.maoding.User;

import com.maoding.Base.BaseLocalService;
import com.maoding.User.zeroc.UserRelatedDTO;
import com.maoding.User.zeroc.UserService;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/19 18:34
 * 描    述 :
 */
@Service("userService")
public class UserServiceLocal extends BaseLocalService implements UserService {
    @Override
    public UserRelatedDTO getUserRelatedInfo(String userId, Current current) {
        return null;
    }
}
