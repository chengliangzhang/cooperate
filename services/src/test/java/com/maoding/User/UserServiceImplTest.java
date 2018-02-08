package com.maoding.User;

import com.maoding.User.zeroc.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    @Autowired
    UserService userService;
    private UserServicePrx userServicePrx = UserServiceImpl.getInstance(":tcp -h 192.168.13.140 -p 10005");

    @Test
    public void testListRole() throws Exception {
        String projectId = "0049eafd6c8a47a4bcc551fa4a491de4";
        List<ProjectRoleDTO> list = userService.listProjectRoleByProjectId(projectId,null);
        Assert.assertNotNull(list);
    }

    @Test
    public void testListUserJoin() throws Exception {
        LoginDTO loginDTO = new LoginDTO(null,null,false,"123456","13680809727");
        userService.login(loginDTO,null);
        UserJoinDTO j = userService.listUserJoin(null);
        Assert.assertNotNull(j);
    }
    /** for method: login(LoginDTO loginInfo, Current current) */
    @Test
    public void testLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
//        loginDTO.setAccountId("13680809727");
//        loginDTO.setPassword("123456");
//        Assert.assertTrue(userService.login(loginDTO,null));
//        Assert.assertEquals("5ffee496fa814ea4b6d26a9208b00a0b",userService.getCurrent(null).getId());
        loginDTO.setCellphone("13680809727");
        loginDTO.setPassword("123456");
        Assert.assertTrue(userServicePrx.login(loginDTO));
        Assert.assertEquals("41d244733ec54f09a255836637f2b21d",userServicePrx.getCurrent().getId());
    }
    /** for method: setOrganization(String organizationId, Current current) */ 
    @Test
    public void testSetOrganization() throws Exception { 
        //TODO: Test goes here... 
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
