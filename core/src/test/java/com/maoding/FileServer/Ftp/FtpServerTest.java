package com.maoding.FileServer.Ftp;

import com.maoding.FileServer.BasicFileServerInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* FtpServer Tester. 
* 
* @author Zhangchengliang
* @since 11/01/2017 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication
public class FtpServerTest {
    @Autowired
    @Qualifier(value = "ftpServer")
    private BasicFileServerInterface ftpServer;

    /** for method: getUploadRequest(BasicFileDTO src) */
    @Test
    public void testGetUploadRequestForHttp() throws Exception {
        //TODO: Test goes here... 
    } 
    /** for method: getDownloadRequest(BasicFileDTO src) */
    @Test
    public void testGetDownloadRequestForHttp() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: upload(BasicFileMultipartDTO multipart) */
    @Test
    public void testUpload() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: download(BasicDownloadRequest request) */ 
    @Test
    public void testDownload() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: duplicateFile(BasicFileDTO src) */ 
    @Test
    public void testDuplicateFile() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: isExist(BasicFileDTO src) */ 
    @Test
    public void testIsExist() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: listFile(String scope) */ 
    @Test
    public void testListFile() throws Exception { 
        //TODO: Test goes here... 
    } 
    /** for method: listScope() */ 
    @Test
    public void testListScope() throws Exception {
        ftpServer.listScope();
    } 
    /** for method: deleteFile(BasicFileDTO src) */ 
    @Test
    public void testDeleteFile() throws Exception { 
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
