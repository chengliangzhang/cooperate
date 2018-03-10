//package com.maoding.FileServer;
//
//import com.maoding.CoreFileServer.CoreFileDTO;
//import com.maoding.CoreFileServer.CoreFileExtraDTO;
//import com.maoding.CoreFileServer.CoreFileServer;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
//* FtpServer Tester.
//*
//* @author Zhangchengliang
//* @since 11/01/2017
//* @version 1.0
//*/
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@SpringBootConfiguration
//@ComponentScan(basePackages = {"com.maoding"})
//public class CoreFileServerTest {
//    @Autowired
//    @Qualifier(value = "ftpServer")
//    private CoreFileServer ftpServer;
//
//    @Autowired
//    @Qualifier(value = "webFileServer")
//    private CoreFileServer webServer;
//
//
//    @Test
//    public void testCreateFile() throws Exception {
//        CoreFileExtraDTO request = new CoreFileExtraDTO();
//        request.setPid("7329033303e0410a900068918025317e");
//        request.setProjectId("c8c049f763d245b5aa9850c43166245e");
//        request.setCompanyId("5aeb14ea46dd4282b136736976d4e430");
//        request.setAccountId("4e658e6c7be0454cbb4c694977b2fd04");
//        request.setCompanyId("5aeb14ea46dd4282b136736976d4e430");
//        CoreFileDTO file = new CoreFileDTO();
//        file.setScope("澳门\\设计\\初步设计阶段");
//        file.setKey("aaaa.txt");
//        webServer.coreCreateFile(file,request);
//    }
//
//    private void uploadMethod() throws Exception{
//        CoreFileExtraDTO request = new CoreFileExtraDTO();
//        request.setPid("7329033303e0410a900068918025317e");
//        request.setProjectId("c8c049f763d245b5aa9850c43166245e");
//        request.setCompanyId("5aeb14ea46dd4282b136736976d4e430");
//        request.setAccountId("4e658e6c7be0454cbb4c694977b2fd04");
//        request.setCompanyId("5aeb14ea46dd4282b136736976d4e430");
//        CoreFileDTO file = new CoreFileDTO();
//        file.setScope("c:\\work");
//        file.setKey("upload_test.txt");
//        webServer.upload(file,request);
//    }
//}
