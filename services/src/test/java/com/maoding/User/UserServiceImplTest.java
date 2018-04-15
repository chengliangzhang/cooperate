package com.maoding.User;

import com.maoding.FileServer.Config.RemoteFileServerPrx;
import com.maoding.FileServer.Config.RemoteUserServicePrx;
import com.maoding.FileServer.zeroc.FileServicePrx;
import com.maoding.User.zeroc.*;
import com.sun.tools.classfile.Code_attribute;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
* UserServiceImpl Tester.
*
* @author Zhangchengliang
* @since 12/06/2017
* @version 1.0
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class UserServiceImplTest {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    private UserServicePrx remote = null;

    private UserServicePrx getRemote(){
        if (remote == null) {
            remote = RemoteUserServicePrx.getInstance("UserServer;120.24.238.128");
        }
        return remote;
    }

    @Test
    public void testSetRoleStatus() throws Exception {
        userService.setWebRoleStatus(getLocalWebRole(),"0",null);
    }

    private WebRoleDTO getLocalWebRole() throws Exception {
        QueryWebRoleDTO query = new QueryWebRoleDTO();
        query.setId("5-287441b514ba4d08bcc7bdd91eada151-0fac3579212a4d598c162122770257f3");
        List<WebRoleDTO> list = userService.listWebRole(query,null);
        return list.get(0);
    }


    /** action before each test */
    @Before
    public void before() throws Exception {
    }

    /** action after every test */
    @After
    public void after() throws Exception {
    }
}
