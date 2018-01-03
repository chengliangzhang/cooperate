package com.maoding.Notice;

import com.maoding.Notice.zeroc.NoticeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* NoticeServiceImpl Tester. 
* 
* @author Zhangchengliang
* @since 01/03/2018 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication

public class NoticeServiceImplTest {
    @Autowired
    NoticeService noticeService;

    /** for method: sendMessage(MessageDTO message, List<ReceiverDTO> receiverList, Current current) */
    @Test
    public void testSendMessage() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: createTopic(String topic, Current current) */ 
    @Test
    public void testCreateTopic() throws Exception { 
        noticeService.createTopic("abcde",null);
    } 
    /** for method: subscribeTopic(String topic, Current current) */ 
    @Test
    public void testSubscribeTopic() throws Exception { 
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
