package com.maoding.Utils;

import com.maoding.CoreNotice.ActiveMQ.imDTO;
import com.maoding.CoreNotice.ActiveMQ.imDetailDTO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* JsonUtils Tester. 
* 
* @author Zhangchengliang
* @since 01/18/2018 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class JsonUtilsTest { 
    @Test
    public void testObj2Json() throws Exception { 
        imDTO m = new imDTO();
        m.setFixMode(true);
        imDetailDTO md = new imDetailDTO();
        md.setAccountId("aaa");
        m.setContent(md);
        String t = JsonUtils.obj2Json(m);
        Assert.assertNotNull(t);
    } 
    /** for method: json2Obj(String json, Class<T> clazz) */ 
    @Test
    public void testJson2Obj() throws Exception { 
        String t = "{\"id\":\"b164e65f106648d19b4b04e9e3b0751c\",\"queueNo\":2,\"operation\":\"im:account:create\",\"content\":{\"accountId\":\"73486fd0972d40b1b0652364ddb2ba1b\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\"},\"fixMode\":\"true\"}";
        JsonUtils ju = new JsonUtils();
        imDTO m = ju.json2Obj(t,imDTO.class);
        Assert.assertNotNull(m);
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
