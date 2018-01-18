package com.maoding.Notice;

import com.maoding.Notice.zeroc.MessageDTO;
import com.maoding.Notice.zeroc.NoticeClientPrx;
import com.maoding.Notice.zeroc.NoticeService;
import com.maoding.Notice.zeroc.ReceiverDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void tesNotice() throws Exception {
        NoticeClientPrx client1 = NoticeClientImpl.createNewClient("192.168.13.140","1");
        NoticeClientPrx client2 = NoticeClientImpl.createNewClient("192.168.13.140","2");
        noticeService.subscribeTopic("User1",client1,null);
        noticeService.subscribeTopic("User2",client2,null);
        MessageDTO msg = new MessageDTO();
        msg.setUserId("user3");
        msg.setTitle("title");
        msg.setContent("message");
        List<ReceiverDTO> list = new ArrayList<>();
        noticeService.noticeToUser(msg,"1",null);
        noticeService.unSubscribeTopic("User1",client1,null);
        noticeService.unSubscribeTopic("User2",client2,null);
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
