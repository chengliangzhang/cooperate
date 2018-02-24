package com.maoding.Common;

import com.maoding.Common.Dto.StringElementDTO;
import com.maoding.Utils.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* ConstService Tester. 
* 
* @author Zhangchengliang
* @since 01/13/2018 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class ConstServiceTest { 
    @Test
    public void testConvertPath() throws Exception {
        StringElementDTO pathElement = new StringElementDTO();
        pathElement.setSrcPath("a/b/c.txt");
        pathElement.setIssuePath("t1/t2");
        String s = StringUtils.formatPath(ConstService.convertString("/{Project}/{Classic1}/{IssuePath}/{File}",pathElement));
        Assert.assertNotNull(s);
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
