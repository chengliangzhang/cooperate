package com.maoding.User;

import com.maoding.User.zeroc.LoginDTO;
import com.maoding.User.zeroc.UserService;
import com.maoding.User.zeroc.UserServicePrx;
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

/** 
* UserServiceImpl Tester. 
* 
* @author Zhangchengliang
* @since 12/06/2017 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication

public class UserServiceImplTest {
    @Autowired
    UserService userService;

    private UserServicePrx userServicePrx = UserServiceImpl.getInstance();

    /** for method: login(LoginDTO loginInfo, Current current) */
    @Test
    public void testLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setAccountId("13680809727");
        loginDTO.setPassword("123456");
        Assert.assertTrue(userService.login(loginDTO,null));
        Assert.assertEquals("5ffee496fa814ea4b6d26a9208b00a0b",userService.getCurrent(null).getId());
        Assert.assertTrue(userServicePrx.login(loginDTO));
        Assert.assertEquals("5ffee496fa814ea4b6d26a9208b00a0b",userServicePrx.getCurrent().getId());
    }
    /** for method: setOrganization(String organizationId, Current current) */ 
    @Test
    public void testSetOrganization() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: setDuty(String dutyId, Current current) */ 
    @Test
    public void testSetDuty() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: listDutyByUserId(String userId, Current current) */ 
    @Test
    public void testListDutyByUserId() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: listDutyForCurrent(Current current) */ 
    @Test
    public void testListDutyForCurrent() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: getCurrent(Current current) */ 
    @Test
    public void testGetCurrent() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: run() */ 
    @Test
    public void testRun() throws Exception { 
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
