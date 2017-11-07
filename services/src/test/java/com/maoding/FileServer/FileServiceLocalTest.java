package com.maoding.FileServer;

import com.maoding.Const.ApiResponseConst;
import com.maoding.Const.FileServerConst;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Utils.BeanUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;

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
    private static final String localPath = System.getProperty("user.dir") + "\\src\\test\\java\\com\\maoding\\FileServer";
    private static final Integer chunkSize = 8192;

    @Autowired
    private FileService fileServerService;

    /** FastFDS测试 */
    @Test
    public void testGetUploadRequestForFastFDS() throws Exception {
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_FASTFDS,null);
        //testGetUploadRequest(new FileDTO("aaa","bbb"));
    }

    @Test
    public void testGetDownloadRequestForFastFDS() throws Exception {
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_FASTFDS,null);
        //testGetDownloadRequest(new FileDTO("group1","M00/00/72/rBAGSVny92OAGfC7AAABPZrEvHI093.txt"));
    }

    /** Aliyun测试 */
    @Test
    public void testGetUploadRequestForAliyun() throws Exception {
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_ALIYUN,null);
        //testGetUploadRequest(new FileDTO("aaa","bbb"));
    }

    @Test
    public void testGetDownloadRequestForAliyun() throws Exception {
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_ALIYUN,null);
        //testGetDownloadRequest(new FileDTO("testmaoding","063c27c4dbbb46369a97f8fbd0b6d8bc.txt"),"GBK");
    }

    /** cifs测试 */
    @Test
    public void testGetUploadRequestForCifs() throws Exception {
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_FAKE_CIFS,null);
        //testGetUploadRequest(new FileDTO("smb://192.168.33.103/Downloads","upload_test.txt"));

    }

    @Test
    public void testUploadForCifs() throws Exception{
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_FAKE_CIFS,null);
        //testUpload(new FileDTO("smb://192.168.33.103/Downloads","upload_test.txt"));
    }

    @Test
    public void testGetDownloadRequestForCifs() throws Exception {
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_CIFS,null);
        //testGetDownloadRequest(new FileDTO("testmaoding","063c27c4dbbb46369a97f8fbd0b6d8bc.txt"),"GBK");
    }

    /** ftp测试 */

    /** LocalServer测试 */
    //上传
    @Test
    public void testUploadForLocal() throws Exception{
        fileServerService.setFileServerType(FileServerConst.FILE_SERVER_TYPE_LOCAL,null);
        //testUpload(new FileDTO("C:\\work\\file_server","upload_test.txt"));
    }

    @Test
    public void testUploadByUrlForLocal() throws Exception {

    }

    /** 公共测试方法 */
    public void testGetUploadRequest(FileDTO fileDTO) throws Exception {
        testGetUploadRequest(fileDTO,null);
    }

    public void testGetUploadRequest(FileDTO fileDTO,Integer mode) throws Exception {
        testGetUploadRequest(fileDTO,mode,null);
    }

    public void testGetUploadRequest(FileDTO fileDTO,Integer mode,CallbackDTO callback) throws Exception {
        FileRequestDTO request = fileServerService.getUploadRequest(fileDTO,mode,callback,null);
        Assert.assertNotEquals(null,request);

    }

    public void testUpload(FileDTO fileDTO) throws Exception {
        testUpload(fileDTO,null);
    }

    public void testUpload(FileDTO fileDTO,Integer mode) throws Exception {
        testUpload(fileDTO,mode,null);
    }

    public void testUpload(FileDTO fileDTO,Integer mode,CallbackDTO callback) throws Exception {
        //获取上传信息
        FileRequestDTO requestInfo = fileServerService.getUploadRequest(fileDTO,mode,callback,null);

        //建立上传内容
        FileMultipartDTO multipart = BeanUtils.createFrom(fileDTO,FileMultipartDTO.class);
        byte[] bytes = new byte[chunkSize];
        FileInputStream is = new FileInputStream(localPath + "\\" + fileDTO.getKey());
        is.read(bytes);
        is.close();
        multipart.setData(bytes);
        //建立上传申请对象
        UploadRequestDTO request = BeanUtils.createFrom(requestInfo,UploadRequestDTO.class);
        request.setMultipart(multipart);

        //发送上传申请
        UploadResultDTO result = fileServerService.upload(request,null);
        Assert.assertEquals(ApiResponseConst.SUCCESS, result.getStatus());
    }

    public void testUploadByUrl(FileDTO fileDTO) throws Exception {
        testUploadByUrl(fileDTO,null);
    }

    public void testUploadByUrl(FileDTO fileDTO,CallbackDTO callback) throws Exception {
//        //建立http post申请参数
//        // 设置HTTP POST请求参数必须用NameValuePair对象
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("multipart", JsonUtils.obj2Json(multipart)));
//        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
////        entity.setContentType("text/json");
////        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//        // 发起Post请求
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost(request.getUrl());
////        post.addHeader(HTTP.CONTENT_TYPE, "application/json");
//        post.setEntity(entity);
//        HttpResponse response = client.execute(post);
//        System.out.println(response.toString());
    }


    /** for method: getDownloadRequest(String src, Current current) */
    public void testGetDownloadRequest(FileDTO file) throws Exception {
        testGetDownloadRequest(file,null);
    }

    public void testGetDownloadRequest(FileDTO file,Integer mode) throws Exception {
        testGetDownloadRequest(file,null,null);
    }

    public void testGetDownloadRequest(FileDTO file,Integer mode,CallbackDTO callback) throws Exception {
        FileRequestDTO request = fileServerService.getDownloadRequest(file,mode,callback,null);
        Assert.assertNotEquals(null,request);
    }

    public void testDownload(FileDTO file) throws Exception {
        testDownload(file,null);
    }

    public void testDownload(FileDTO file, Integer mode) throws Exception {
        testDownload(file,null,null);
    }

    public void testDownload(FileDTO file, Integer mode,CallbackDTO callback) throws Exception {
    }

    public void testDownloadByUrl(FileDTO fileDTO) throws Exception {
        testDownloadByUrl(fileDTO,null);
    }

    public void testDownloadByUrl(FileDTO fileDTO,Integer mode) throws Exception {
        testDownloadByUrl(fileDTO,null,null);
    }

    public void testDownloadByUrl(FileDTO fileDTO, Integer mode, CallbackDTO callback) throws Exception {
//        URL url = new URL(request.url);
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestProperty("contentType", contentType);
//        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),contentType));
//        String temp = br.readLine();
//        while(temp != null) {
//            System.out.println(temp);
//            temp = br.readLine();
//        }
    }

    /** for method: isExist(String src, Current current) */ 
    public void testIsExist() throws Exception {
        //TODO: Test goes here... 
    } 
    /** for method: duplicateFile(String src, Current current) */ 
    public void testDuplicateFile() throws Exception {
        //TODO: Test goes here... 
    } 
    /** for method: deleteFile(String src, Current current) */ 
    public void testDeleteFile() throws Exception {
        //TODO: Test goes here... 
    } 
    /** for method: listFile(String scope, Current current) */ 
    public void testListFile() throws Exception {
        //TODO: Test goes here... 
    } 
    /** for method: listScope(Current current) */ 
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
