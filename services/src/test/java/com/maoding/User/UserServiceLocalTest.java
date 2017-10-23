package com.maoding.User;

import com.maoding.User.zeroc.UserService;
import com.zeroc.Ice.Current;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotEquals;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/19 18:41
 * 描    述 :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceLocalTest {
    @Autowired
    private UserService userService;

    @Test
    public void getUserRelatedInfo() throws Exception {
        assertNotEquals(null,userService.getUserRelatedInfo("1111",(Current)null));
    }

}