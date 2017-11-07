package com.maoding.Storage;

import com.maoding.Const.ApiResponseConst;
import com.maoding.Const.FileServerConst;
import com.maoding.FileServer.FileServiceImpl;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Storage.zeroc.CooperateFileDTO;
import com.maoding.Storage.zeroc.StorageService;
import com.maoding.Storage.zeroc.StorageServicePrx;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

/** 
* StorageServiceLocal Tester. 
* 
* @author Zhangchengliang
* @since 11/02/2017 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
//@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication

public class StorageServiceImplTest {
    private static final String testLocalFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\maoding\\FileServer\\upload_test.txt";
    private static final String testDir = "test";

    @Autowired
    private StorageService storageService;
    private StorageServicePrx storageServicePrx = StorageServiceImpl.getInstance();
    @Autowired
    private FileService fileService;
    private FileServicePrx fileServicePrx = FileServiceImpl.getInstance();

    /** 文件服务器使用LocalServer,使用本地调用文件服务器接口 */
    @Test
    public void testUploadUseLocalForLocal() throws Exception {
        testUploadUseLocal(FileServerConst.FILE_SERVER_TYPE_LOCAL);
    }

    @Test
    public void testDownloadUseLocalForLocal() throws Exception {
        testDownloadUseLocal(FileServerConst.FILE_SERVER_TYPE_LOCAL);
    }

    /** 使用RPC接口 */
    @Test
    public void testUploadUseRPCForLocal() throws Exception {
        testUploadUseRPC(FileServerConst.FILE_SERVER_TYPE_LOCAL);
    }

    @Test
    public void testDownloadUseRPCForLocal() throws Exception {
        testDownloadUseRPC(FileServerConst.FILE_SERVER_TYPE_LOCAL);
    }


    /** 通用测试接口 */
    /** 使用本地调用方式上传文件 */
    public void testUploadUseLocal(Integer fileServerType) throws Exception {
        fileService.setFileServerType(fileServerType,null);

        //建立协同文件
        CooperateFileDTO fileInfo = new CooperateFileDTO();
        fileInfo.setLocalFile(testLocalFile);
        fileInfo.setDirName(testDir);
        fileInfo.setFileName(StringUtils.getFileName(testLocalFile));

        //获取上传信息
        FileRequestDTO requestInfo = storageService.requestUpload(fileInfo, FileServerConst.FILE_SERVER_MODE_DEFAULT,null);
        Map<String,String> params = requestInfo.getParams();

        //上传文件
        final Integer CHUNK_SIZE = 10;//FileServerConst.DEFAULT_CHUNK_SIZE;
        File f = new File(testLocalFile);
        Integer chunkCount = (int)((f.length() / CHUNK_SIZE) + 1);
        FileInputStream is = new FileInputStream(testLocalFile);
        for (Integer chunkId=0; chunkId < chunkCount; chunkId++) {
            //建立上传内容
            FileMultipartDTO multipart = BeanUtils.createFrom(params, FileMultipartDTO.class);
            assert multipart != null;
            byte[] bytes = new byte[CHUNK_SIZE];
            multipart.setPos((long)chunkId * CHUNK_SIZE);
            multipart.setSize(is.read(bytes, 0, CHUNK_SIZE));
            bytes = Arrays.copyOfRange(bytes, 0, multipart.getSize());
            multipart.setData(bytes);

            //建立上传申请对象
            UploadRequestDTO request = BeanUtils.createFrom(requestInfo, UploadRequestDTO.class);
            assert request != null;
            if (params.containsKey("uploadId")) request.setUploadId(params.get("uploadId"));
            request.setChunkCount(chunkCount);
            request.setChunkId(chunkId);
            request.setChunkPerSize(CHUNK_SIZE);
            request.setChunkSize(multipart.getSize());
            request.setMultipart(multipart);

            //发送上传申请
            UploadResultDTO result = fileService.upload(request, null);
            Assert.assertEquals(ApiResponseConst.SUCCESS, result.getStatus());
        }
        is.close();
    }

    /** 使用本地调用方式下载文件 */
    public void testDownloadUseLocal(Integer fileServerType) throws Exception {
        fileService.setFileServerType(fileServerType,null);

        //建立协同文件
        CooperateFileDTO fileInfo = new CooperateFileDTO();
        fileInfo.setLocalFile(testLocalFile);
        fileInfo.setDirName(testDir);
        fileInfo.setFileName(StringUtils.getFileName(testLocalFile));

        //获取下载信息
        FileRequestDTO requestInfo = storageService.requestDownload(fileInfo, FileServerConst.FILE_SERVER_MODE_DEFAULT,null);
        Map<String,String> params = requestInfo.getParams();

        //下载文件
        final Integer CHUNK_SIZE = 10;//FileServerConst.DEFAULT_CHUNK_SIZE;
        Integer chunkCount = 999;
        if (params.containsKey("size")) {
            chunkCount = (int)((Integer.parseInt(params.get("size")) / (2 * CHUNK_SIZE)) + 1);
        }
        assert params.containsKey("scope");
        assert params.containsKey("key");

        FileMultipartDTO multipart = BeanUtils.createFrom(params, FileMultipartDTO.class);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(testLocalFile + "_downloading2.txt"));
        for (Integer chunkId=0; chunkId < chunkCount; chunkId++) {
            //建立下载申请
            DownloadRequestDTO request = BeanUtils.createFrom(params, DownloadRequestDTO.class);
            assert request != null;
            request.setChunkId(chunkId);
            request.setChunkSize(CHUNK_SIZE);
            request.setScope(params.get("scope"));
            request.setKey(params.get("key"));
            //发送下载申请
            DownloadResultDTO result = fileService.download(request,null);
            assert result != null;
            Assert.assertEquals(ApiResponseConst.SUCCESS,result.status);
            //写入文件
            os.write(result.data.getData());
        }
        os.close();
    }

    /** 使用RPC调用方式上传文件 */
    public void testUploadUseRPC(Integer fileServerType) throws Exception {
        fileServicePrx.setFileServerType(fileServerType);

        //建立协同文件
        CooperateFileDTO fileInfo = new CooperateFileDTO();
        fileInfo.setLocalFile(testLocalFile);
        fileInfo.setDirName(testDir);
        fileInfo.setFileName(StringUtils.getFileName(testLocalFile));

        //获取上传信息
        FileRequestDTO requestInfo = storageServicePrx.requestUpload(fileInfo, FileServerConst.FILE_SERVER_MODE_DEFAULT);
        Map<String,String> params = requestInfo.getParams();

        //上传文件
        final Integer CHUNK_SIZE = 10;//FileServerConst.DEFAULT_CHUNK_SIZE;
        File f = new File(testLocalFile);
        Integer chunkCount = (int)((f.length() / CHUNK_SIZE) + 1);
        FileInputStream is = new FileInputStream(testLocalFile);
        for (Integer chunkId=0; chunkId < chunkCount; chunkId++) {
            //建立上传内容
            FileMultipartDTO multipart = BeanUtils.createFrom(params, FileMultipartDTO.class);
            assert multipart != null;
            byte[] bytes = new byte[CHUNK_SIZE];
            multipart.setPos((long)chunkId * CHUNK_SIZE);
            multipart.setSize(is.read(bytes, 0, CHUNK_SIZE));
            bytes = Arrays.copyOfRange(bytes, 0, multipart.getSize());
            multipart.setData(bytes);

            //建立上传申请对象
            UploadRequestDTO request = BeanUtils.createFrom(requestInfo, UploadRequestDTO.class);
            assert request != null;
            if (params.containsKey("uploadId")) request.setUploadId(params.get("uploadId"));
            request.setChunkCount(chunkCount);
            request.setChunkId(chunkId);
            request.setChunkPerSize(CHUNK_SIZE);
            request.setChunkSize(multipart.getSize());
            request.setMultipart(multipart);

            //发送上传申请
            UploadResultDTO result = fileServicePrx.upload(request);
            Assert.assertEquals(ApiResponseConst.SUCCESS, result.getStatus());
        }
        is.close();
    }

    /** 使用RPC调用方式下载文件 */
    public void testDownloadUseRPC(Integer fileServerType) throws Exception {
        fileServicePrx.setFileServerType(fileServerType);

        //建立协同文件
        CooperateFileDTO fileInfo = new CooperateFileDTO();
        fileInfo.setLocalFile(testLocalFile);
        fileInfo.setDirName(testDir);
        fileInfo.setFileName(StringUtils.getFileName(testLocalFile));

        //获取下载信息
        FileRequestDTO requestInfo = storageServicePrx.requestDownload(fileInfo, FileServerConst.FILE_SERVER_MODE_DEFAULT);
        Map<String,String> params = requestInfo.getParams();

        //下载文件
        final Integer CHUNK_SIZE = 10;//FileServerConst.DEFAULT_CHUNK_SIZE;
        Integer chunkCount = 999;
        if (params.containsKey("size")) {
            chunkCount = (int)((Integer.parseInt(params.get("size")) / (2 * CHUNK_SIZE)) + 1);
        }
        assert params.containsKey("scope");
        assert params.containsKey("key");

        FileMultipartDTO multipart = BeanUtils.createFrom(params, FileMultipartDTO.class);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(testLocalFile + "_downloading2.txt"));
        for (Integer chunkId=0; chunkId < chunkCount; chunkId++) {
            //建立下载申请
            DownloadRequestDTO request = BeanUtils.createFrom(params, DownloadRequestDTO.class);
            assert request != null;
            request.setChunkId(chunkId);
            request.setChunkSize(CHUNK_SIZE);
            request.setScope(params.get("scope"));
            request.setKey(params.get("key"));
            //发送下载申请
            DownloadResultDTO result = fileServicePrx.download(request);
            assert result != null;
            Assert.assertEquals(ApiResponseConst.SUCCESS,result.status);
            //写入文件
            os.write(result.data.getData());
        }
        os.close();
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
