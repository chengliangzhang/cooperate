package com.maoding.FileServer; 

import com.maoding.FileServer.zeroc.FileServerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* FileServerLocal Tester. 
* 
* @author Zhangchengliang
* @since 10/25/2017 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FileServerLocalTest {
    @Autowired
    FileServerService fileServerService;

    /** for method: getUploadRequestForHttp(Current current) */ 
    @Test
    public void testGetUploadRequestForHttp() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: getDownloadRequestForHttp(String src, Current current) */ 
    @Test
    public void testGetDownloadRequestForHttp() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: isExist(String src, Current current) */ 
    @Test
    public void testIsExist() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: duplicateFile(String src, Current current) */ 
    @Test
    public void testDuplicateFile() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: deleteFile(String src, Current current) */ 
    @Test
    public void testDeleteFile() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: listFile(String scope, Current current) */ 
    @Test
    public void testListFile() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: listScope(Current current) */ 
    @Test
    public void testListScope() throws Exception { 
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
