package com.maoding.FileServer;

import com.maoding.CoreFileServer.CoreFileDTO;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreFileServer.CoreUploadRequest;
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
//@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication
public class CoreFileServerTest {
    @Autowired
    @Qualifier(value = "ftpServer")
    private CoreFileServer ftpServer;

    @Autowired
    @Qualifier(value = "webFileServer")
    private CoreFileServer webServer;


    @Test
    public void testUploadToWeb() throws Exception {
        CoreUploadRequest request = new CoreUploadRequest();
        request.setAddress("http://172.16.6.73:8071/fileCenter/netFile/uploadFile");
        request.setPid("7329033303e0410a900068918025317e");
        request.setProjectId("c8c049f763d245b5aa9850c43166245e");
        request.setCompanyId("5aeb14ea46dd4282b136736976d4e430");
        request.setAccountId("4e658e6c7be0454cbb4c694977b2fd04");
        request.setCompanyId("5aeb14ea46dd4282b136736976d4e430");
        CoreFileDTO file = new CoreFileDTO();
        file.setScope("c:\\work");
        file.setKey("upload_test.txt");
        webServer.upload(file,request);
    }
}
