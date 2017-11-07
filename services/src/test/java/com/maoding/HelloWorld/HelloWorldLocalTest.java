package com.maoding.HelloWorld;

import com.maoding.HelloWorld.zeroc.HelloWorldServicePrx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* HelloWorldLocal Tester. 
* 
* @author Zhangchengliang
* @since 11/07/2017 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication
@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
//@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication
@PropertySource(value = {"classpath:properties/ice-config.properties"},encoding="utf-8",ignoreResourceNotFound=true)
public class HelloWorldLocalTest {
    /** for method: helloWorld(Current current) */ 
    @Test
    public void testHelloWorld() throws Exception {
        HelloWorldServicePrx prx = HelloWorldLocal.getInstance();
        prx.helloWorld();
    }

    /** for method: getServicePrx() */
    @Test
    public void testGetServicePrx() throws Exception {
        HelloWorldServicePrx prx = HelloWorldLocal.getInstance();
        Assert.assertNotNull(prx);
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
