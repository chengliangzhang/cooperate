package com.maoding.User;

import com.maoding.Base.CoreRemoteService;
import com.maoding.User.zeroc.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private UserService userService;

    private UserServicePrx remote = null;

    private UserServicePrx getRemote(){
        if (remote == null) {
            CoreRemoteService<UserServicePrx> prx = new CoreRemoteService<>();
            remote = prx.getServicePrx("UserService","UserServer;192.168.13.140",UserServicePrx.class,_UserServicePrxI.class);
        }
        return remote;
    }

    @Test
    public void testLogin() throws Exception{
        loginRemote();
    }

    private boolean loginRemote() throws Exception {
        log.debug("\t>>>>>>>> loginRemote");
        return getRemote().login(getLocalLoginInfo());
    }

    private LoginDTO getLocalLoginInfo() {
        return new LoginDTO(
                "",
                "",
                false,
                "123456",
                "13680809727");
    }

    @Test
    public void testSetRoleStatus() throws Exception {
        userService.setWebRoleStatus(getLocalWebRole(),"1",null);
    }

    private WebRoleDTO getLocalWebRole() throws Exception {
        QueryWebRoleDTO query = new QueryWebRoleDTO();
        query.setId("4-f56e157939754cd1aa2ea77e5f90de9a-d437448683314cad91dc30b68879901d");
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
