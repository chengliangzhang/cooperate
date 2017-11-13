package com.maoding.FileServer;

import com.maoding.Const.ApiResponseConst;
import com.maoding.Const.FileServerConst;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.HttpUtils;
import com.maoding.Utils.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/** 
* FileServerLocal Tester. 
* 
* @author Zhangchengliang
* @since 10/25/2017 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
public class FileServiceLocalTest {
    private static final String testLocalFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\maoding\\FileServer\\upload_test.txt";
    private static final String testDir = "testForFileService";

    @Autowired
    private FileService fileService;
    private FileServicePrx fileServicePrx = FileServiceImpl.getInstance();

    private Integer fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;

    /** FastFDS测试 */

    /** Aliyun测试 */
    @Test //上传
    public void testUploadForAliyun() throws Exception{
        fileServerType = FileServerConst.FILE_SERVER_TYPE_ALIYUN;
        testUpload(FileServerConst.FILE_SERVER_MODE_LOCAL);
    }

    /** cifs测试 */

    /** ftp测试 */
    @Test //上传
    public void testUploadForFtp() throws Exception{
        fileServerType = FileServerConst.FILE_SERVER_TYPE_FTP;
        testUpload(FileServerConst.FILE_SERVER_MODE_LOCAL);
    }

    /** LocalServer测试 */
    @Test //上传
    public void testUploadForLocal() throws Exception{
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
//        testUpload(FileServerConst.FILE_SERVER_MODE_LOCAL);
        testUpload(FileServerConst.FILE_SERVER_MODE_RPC);
//        testUpload(FileServerConst.FILE_SERVER_MODE_HTTP_POST);
    }

    @Test //下载
    public void testDownloadForLocal() throws Exception{
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
        testDownload(FileServerConst.FILE_SERVER_MODE_LOCAL);
        testDownload(FileServerConst.FILE_SERVER_MODE_RPC);
        testDownload(FileServerConst.FILE_SERVER_MODE_HTTP_POST);
    }

    /** 公共测试方法 */
    public void testUpload(Integer mode) throws Exception {
        if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)){
            uploadByLocal();
        } else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(mode)){
            uploadByRPC();
        } else if (FileServerConst.FILE_SERVER_MODE_OSS.equals(mode)){
            uploadByOSS();
        } else if (FileServerConst.FILE_SERVER_MODE_HTTP_POST.equals(mode)) {
            uploadByPost();
        }
    }

    public void testDownload(Integer mode) throws Exception {
        if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)) {
            downloadByLocal();
        } else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(mode)){
            downloadByRPC();
        } else if (FileServerConst.FILE_SERVER_MODE_OSS.equals(mode)){
            downloadByOSS();
        } else if (FileServerConst.FILE_SERVER_MODE_HTTP_POST.equals(mode)) {
            downloadByPost();
        }
    }

    //建立上传申请
    public UploadRequestDTO createUploadRequestDTO(FileDTO fileDTO,Map<String,String> params) throws Exception {
        //补全参数
        if (fileDTO == null) fileDTO = new FileDTO();
        if (fileDTO.getScope() == null) fileDTO.setScope(testDir);
        if (fileDTO.getKey() == null) fileDTO.setKey(StringUtils.getFileName(testLocalFile));

        //建立上传内容
        FileMultipartDTO multipart = BeanUtils.createFrom(params, FileMultipartDTO.class);
        assert multipart != null;
        File f = new File(testLocalFile);
        FileInputStream is = null;
        is = new FileInputStream(f);
        byte[] bytes = new byte[(int)f.length()];
        multipart.setScope(fileDTO.getScope());
        multipart.setKey(fileDTO.getKey());
        multipart.setPos(0L);
        multipart.setSize(is.read(bytes));
        multipart.setData(bytes);
        is.close();

        //建立上传申请对象
        UploadRequestDTO request = BeanUtils.createFrom(params, UploadRequestDTO.class);
        assert request != null;
        request.setChunkCount(1);
        request.setChunkId(0);
        request.setChunkPerSize(multipart.getSize());
        request.setChunkSize(multipart.getSize());
        request.setMultipart(multipart);

        return request;
    }
    public UploadRequestDTO createUploadRequestDTO(FileDTO fileDTO) throws Exception {
        return createUploadRequestDTO(fileDTO,null);
    }
    public UploadRequestDTO createUploadRequestDTO() throws Exception {
        return createUploadRequestDTO(new FileDTO(testDir, StringUtils.getFileName(testLocalFile)));
    }

    //建立下载申请
    public DownloadRequestDTO createDownloadRequestDTO(FileDTO fileDTO,Map<String,String> params) throws Exception {
        //补全参数
        if (fileDTO == null) fileDTO = new FileDTO();
        if (fileDTO.getScope() == null) fileDTO.setScope(testDir);
        if (fileDTO.getKey() == null) fileDTO.setKey(StringUtils.getFileName(testLocalFile));

        //建立下载申请对象
        DownloadRequestDTO request = BeanUtils.createFrom(params, DownloadRequestDTO.class);
        assert request != null;
        request.setChunkId(0);
        request.setChunkSize(FileServerConst.DEFAULT_CHUNK_SIZE);
        request.setScope(fileDTO.getScope());
        request.setKey(fileDTO.getKey());

        return request;
    }
    public DownloadRequestDTO createDownloadRequestDTO(FileDTO fileDTO) throws Exception {
        return createDownloadRequestDTO(fileDTO,null);
    }
    public DownloadRequestDTO createDownloadRequestDTO() throws Exception {
        return createDownloadRequestDTO(new FileDTO(testDir, StringUtils.getFileName(testLocalFile)));
    }

    public void uploadByLocal() throws Exception {
        //选择文件服务器类型
        fileService.setFileServerType(fileServerType,null);

        //建立上传申请
        UploadRequestDTO request = createUploadRequestDTO();

        //发送上传申请
        UploadResultDTO result = fileService.upload(request,null);
        Assert.assertEquals(ApiResponseConst.SUCCESS, result.getStatus());
    }

    public void uploadByRPC() throws Exception {
        //选择文件服务器类型
        fileServicePrx.setFileServerType(fileServerType);

        //建立上传申请
        UploadRequestDTO request = createUploadRequestDTO();

        //发送上传申请
        UploadResultDTO result = fileServicePrx.upload(request);
        Assert.assertEquals(ApiResponseConst.SUCCESS, result.getStatus());
    }

    public void downloadByLocal() throws Exception {
        //选择文件服务器类型
        fileService.setFileServerType(fileServerType,null);

        //建立下载申请
        DownloadRequestDTO request = createDownloadRequestDTO();

        //发送下载申请
        DownloadResultDTO result = fileService.download(request,null);
        Assert.assertEquals(ApiResponseConst.SUCCESS, result.getStatus());
    }

    public void uploadByOSS() throws Exception {
        //选择文件服务器类型
        fileServicePrx.setFileServerType(fileServerType);

        //建立上传申请
        UploadRequestDTO request = createUploadRequestDTO();

        //发送上传申请
        //TO DO 使用OSS发送上传申请
        //TO DO 进行发送结果检测
    }

    public void downloadByOSS() throws Exception {
        //选择文件服务器类型
        fileService.setFileServerType(fileServerType,null);

        //建立下载申请
        DownloadRequestDTO request = createDownloadRequestDTO();

        //发送下载申请
        //TO DO 使用OSS发送下载申请
        //TO DO 进行发送结果检测
    }

    public void downloadByRPC() throws Exception {
        //选择文件服务器类型
        fileServicePrx.setFileServerType(fileServerType);

        //建立下载申请
        DownloadRequestDTO request = createDownloadRequestDTO();

        //发送下载申请
        DownloadResultDTO result = fileServicePrx.download(request);
        Assert.assertEquals(ApiResponseConst.SUCCESS, result.getStatus());
    }

    public void uploadByPost() throws Exception {
        final String setFileServerTypeUrl = "http://localhost:8087/FileServer/setFileServerType";
        final String setFileServerTypeContentType = "application/x-www-form-urlencoded";
        final String uploadUrl = "http://localhost:8087/FileServer/upload";
        final String uploadContentType = "application/json";


        CloseableHttpClient client = HttpClients.createDefault();

        //选择文件服务器类型
        Map<String,Integer> params = new HashMap<>();
        params.put("type",fileServerType);
        CloseableHttpResponse r1 = HttpUtils.postData(client,setFileServerTypeUrl,
                setFileServerTypeContentType,params);
        Assert.assertEquals(200,r1.getStatusLine().getStatusCode());
        r1.close();

        //建立上传申请
        UploadRequestDTO request = createUploadRequestDTO();

        //发送上传申请
        CloseableHttpResponse r2 = HttpUtils.postData(client,uploadUrl,uploadContentType,request);
        Assert.assertEquals(200,r2.getStatusLine().getStatusCode());
        r2.close();

        client.close();
    }

    public void downloadByPost() throws Exception {
        final String setFileServerTypeUrl = "http://localhost:8087/FileServer/setFileServerType";
        final String setFileServerTypeContentType = "application/x-www-form-urlencoded";
        final String downloadUrl = "http://localhost:8087/FileServer/download";
        final String downloadContentType = "application/json";
        
        CloseableHttpClient client = HttpClients.createDefault();
        
        //选择文件服务器类型
        Map<String,Integer> params = new HashMap<>();
        params.put("type",fileServerType);
        CloseableHttpResponse r1 = HttpUtils.postData(client,setFileServerTypeUrl,
                setFileServerTypeContentType,params);
        Assert.assertEquals(200,r1.getStatusLine().getStatusCode());
        r1.close();

        //建立下载申请
        DownloadRequestDTO request = createDownloadRequestDTO();

        //发送下载申请
        CloseableHttpResponse r2 = HttpUtils.postData(client,downloadUrl,downloadContentType,request);
        Assert.assertEquals(200,r2.getStatusLine().getStatusCode());
        r2.close();
        
        client.close();
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
