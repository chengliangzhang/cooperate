package com.maoding.Storage;

import com.maoding.Const.FileServerConst;
import com.maoding.Const.StorageConst;
import com.maoding.FileServer.FileServiceImpl;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Storage.zeroc.*;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.HttpUtils;
import com.maoding.Utils.JsonUtils;
import com.maoding.Utils.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
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
@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication

public class StorageServiceImplTest {
    private static final String testUploadLocalFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\maoding\\FileServer\\upload_test.txt";
    private static final String testDownloadLocalFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\maoding\\FileServer\\upload_test_downloading.txt";
    private static final String testDir = "testForStorageService";

    @Autowired
    private StorageService storageService;
    private StorageServicePrx storageServicePrx = StorageServiceImpl.getInstance();
    @Autowired
    private FileService fileService;
    private FileServicePrx fileServicePrx = FileServiceImpl.getInstance();

    private Integer fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;

    /** 创建版本 */
    @Test
    public void testCreateVersion() throws Exception {
        CooperationQueryDTO query = new CooperationQueryDTO();
        CooperateDirDTO dirInfo = storageService.getCooperateDirInfo(query,null);
        Assert.assertNotNull(dirInfo);
        List<CooperateFileDTO> fileList = dirInfo.getFileList();
        Assert.assertNotNull(fileList);
        CooperateFileDTO file = fileList.get(0);
        String nodeId = storageService.createVersion(file,"v1.0",null);
        Assert.assertNotEquals(file.getNodeId(),nodeId);
    }

    /** 创建文件 */
    @Test
    public void testCreateFile() throws Exception {
        CreateNodeRequestDTO request = BeanUtils.cleanProperties(new CreateNodeRequestDTO());
        request.setFullName("r/s/t");
        request.setTypeId((short)0);
        request.setDirTypeId(StorageConst.STORAGE_DIR_TYPE_USER);
        String fullPath = storageService.createFile(request,null);
        String nodeId = StringUtils.getLastSplit(fullPath,StringUtils.SPLIT_ID);
        Assert.assertNotNull(nodeId);
    }

    /** 创建目录 */
    @Test
    public void testCreateDirectory() throws Exception {
        CreateNodeRequestDTO request = BeanUtils.cleanProperties(new CreateNodeRequestDTO());
        request.setFullName("a/b/c");
        request.setTypeId(StorageConst.STORAGE_DIR_TYPE_SYS);
        String fullPath = storageService.createDirectory(request,null);
        String nodeId = StringUtils.getLastSplit(fullPath,StringUtils.SPLIT_ID);
        request.setPNodeId(nodeId);
        request.setFullName("x/y/z");
        storageService.createDirectory(request,null);
    }

    /** 删除目录 */
    @Test
    public void testDeleteDirectory() throws Exception {
        CreateNodeRequestDTO request = BeanUtils.cleanProperties(new CreateNodeRequestDTO());
        request.setFullName("m/n");
        request.setTypeId(StorageConst.STORAGE_DIR_TYPE_USER);
        String fullPath = storageService.createDirectory(request,null);
        String nodeId = StringUtils.getLastSplit(fullPath,StringUtils.SPLIT_ID);
        boolean b = storageService.deleteDirectory(nodeId,true,null);
        Assert.assertTrue(b);
    }

    /** 获取目录文件列表 */
    @Test
    public void testGetCooperateDirInfo() throws Exception {
        CooperationQueryDTO query = new CooperationQueryDTO();
        CooperateDirDTO dirInfo = storageService.getCooperateDirInfo(query,null);
        Assert.assertNotNull(dirInfo);
        List<CooperateDirNodeDTO> subDirList = dirInfo.getSubDirList();
        Assert.assertNotNull(subDirList);
        Assert.assertTrue(subDirList.size() > 0);
        query.setNodeId(subDirList.get(0).getId());
        query.setNodeId("93FA51B7883E4F1BB1D5AC7DE9EE9E97");
        query.setLevel(2);
        dirInfo = storageService.getCooperateDirInfo(query,null);
        Assert.assertNotNull(dirInfo);
    }

    @Test
    public void testGetNodeInfo() throws Exception {
        CooperationQueryDTO query = new CooperationQueryDTO();
        NodeDTO nodeInfo = storageService.getNodeInfo(query,null);
        Assert.assertNotNull(nodeInfo);
        List<SimpleNodeDTO> subNodeList = nodeInfo.getSubNodeList();
        Assert.assertNotNull(subNodeList);
        Assert.assertTrue(subNodeList.size() > 0);
        query.setNodeId(subNodeList.get(0).getId());
        nodeInfo = storageService.getNodeInfo(query,null);
        Assert.assertNotNull(nodeInfo);
    }

    /** 文件服务器使用LocalServer,使用多个调用文件服务器接口 */
    @Test
    public void testUploadForLocal() throws Exception {
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
        upload(FileServerConst.FILE_SERVER_MODE_LOCAL);
//        upload(FileServerConst.FILE_SERVER_MODE_RPC);
//        upload(FileServerConst.FILE_SERVER_MODE_HTTP_POST);
    }

    @Test
    public void testDownloadForLocal() throws Exception {
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
        download(FileServerConst.FILE_SERVER_MODE_LOCAL);
        download(FileServerConst.FILE_SERVER_MODE_RPC);
//        download(FileServerConst.FILE_SERVER_MODE_HTTP_POST);
    }

    /** 文件服务器使用Aliyun,使用POST调用文件服务器接口 */
    @Test
    public void testUploadForAliyun() throws Exception {
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
//        upload(FileServerConst.FILE_SERVER_MODE_HTTP_POST);
        upload(FileServerConst.FILE_SERVER_MODE_DEFAULT_COM);
    }

    @Test
    public void testDownloadForAliyun() throws Exception {
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
//        download(FileServerConst.FILE_SERVER_MODE_HTTP_POST);
        download(FileServerConst.FILE_SERVER_MODE_DEFAULT_COM);
    }

    /** 通用测试接口 */
    //建立协作文件信息
    public CooperateFileDTO createCooperateFileDTO(String localFile,String dirName,String fileName) throws Exception {
        //补全参数
        if (localFile == null) localFile = testUploadLocalFile;
        if (dirName == null) dirName = testDir;
        if (fileName == null) fileName = StringUtils.getFileName(testUploadLocalFile);

        CooperateFileDTO fileInfo = new CooperateFileDTO();
        fileInfo.setLocalFile(localFile);
        fileInfo.setPathName(dirName);
        fileInfo.setName(fileName);
        return fileInfo;
    }
    public CooperateFileDTO createCooperateFileDTO() throws Exception {
        return createCooperateFileDTO(null,null,null);
    }

    //建立上传内容申请
    public UploadRequestDTO createUploadRequestDTO(File f,FileRequestDTO fileRequestDTO,Integer chunkId,Integer chunkSize,Integer chunkCount) throws Exception {
        final String SCOPE_KEY = "scope";
        final String KEY_KEY = "key";

        //检查参数
        assert ((f != null) && (f.exists()));
        assert ((fileRequestDTO != null) && (fileRequestDTO.getParams() != null));
        if (chunkId == null) chunkId = 0;
        if (chunkSize == null) chunkSize = FileServerConst.DEFAULT_CHUNK_SIZE;

        //建立上传内容
        Map<String,String> requestParams = fileRequestDTO.getParams();
        FileMultipartDTO multipart = BeanUtils.createFrom(requestParams, FileMultipartDTO.class);
        assert multipart != null;
        long pos = (long)chunkId * chunkSize;
        RandomAccessFile rf = new RandomAccessFile(f,"r");
        assert (pos < rf.length());
        rf.seek(pos);
        byte[] bytes = new byte[chunkSize];
        multipart.setScope(requestParams.get(SCOPE_KEY));
        multipart.setKey(requestParams.get(KEY_KEY));
        multipart.setPos(pos);
        multipart.setSize(rf.read(bytes));
        multipart.setData(bytes);
        rf.close();

        //建立上传申请对象
        UploadRequestDTO request = BeanUtils.createFrom(requestParams, UploadRequestDTO.class);
        assert request != null;
        request.setChunkCount(chunkCount);
        request.setChunkId(chunkId);
        request.setChunkPerSize(multipart.getSize());
        request.setChunkSize(multipart.getSize());
        request.setChunkPerSize(chunkSize);
        request.setMultipart(multipart);

        return request;
    }

    //申请上传
    public void upload(Integer mode,String localFile,String scope,String key) throws Exception {
        //初始化协同文件
        CooperateFileDTO fileInfo = createCooperateFileDTO(localFile,scope,key);

        //获取文件服务器接口
        FileRequestDTO fileRequestDTO = null;
        if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)){
            fileService.setFileServerType(fileServerType,null);
            fileRequestDTO = storageService.requestUpload(fileInfo, mode,null);
            fileRequestDTO.setMode(FileServerConst.FILE_SERVER_MODE_LOCAL);
        } else {
            fileServicePrx.setFileServerType(fileServerType);
            fileRequestDTO = storageServicePrx.requestUpload(fileInfo, mode);
        }

        //上传文件内容
        assert (fileRequestDTO != null);
        uploadContent(new File(localFile),fileRequestDTO);

        //通告上传完毕
        if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)){
            storageService.finishUpload(fileRequestDTO,true,null);
        } else {
            storageServicePrx.finishUpload(fileRequestDTO,true);
        }
    }
    public void upload(Integer mode) throws Exception {
        upload(mode,testUploadLocalFile,null,StringUtils.getFileName(testUploadLocalFile));
    }

    //申请下载
    public void download(Integer mode,String localFile,String scope,String key) throws Exception {
        //初始化协同文件
        if (key == null) key = StringUtils.getFileName(testUploadLocalFile);
        FileDTO fileDTO = new FileDTO(scope,key);
        List<CooperateFileDTO> fileInfoList = null;
        if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)){
            fileInfoList = storageService.listFileLink(fileDTO,null);
        } else {
            fileInfoList = storageServicePrx.listFileLink(fileDTO);
        }
        CooperateFileDTO fileInfo = fileInfoList.get(0);

        //获取文件服务器接口
        FileRequestDTO fileRequestDTO = null;
        if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)){
            fileService.setFileServerType(fileServerType,null);
            //初始化协同文件
            fileRequestDTO = storageService.requestDownload(fileInfo, mode,null);
            fileRequestDTO.setMode(FileServerConst.FILE_SERVER_MODE_LOCAL);
        } else {
            fileServicePrx.setFileServerType(fileServerType);
            fileRequestDTO = storageServicePrx.requestDownload(fileInfo, mode);
        }

        downloadContent(new File(localFile),fileRequestDTO);
    }
    public void download(Integer mode) throws Exception {
        download(mode,testDownloadLocalFile,null,StringUtils.getFileName(testUploadLocalFile));
    }

    //建立下载申请
    public DownloadRequestDTO createDownloadRequestDTO(FileRequestDTO fileRequestDTO,Integer chunkId,Integer chunkSize){
        final String SCOPE_KEY = "scope";
        final String KEY_KEY = "key";

        //检查参数
        assert ((fileRequestDTO != null) && (fileRequestDTO.getParams() != null));
        assert (chunkId != null);
        if (chunkSize == null) chunkSize = FileServerConst.DEFAULT_CHUNK_SIZE;
        Map<String,String> requestParams = fileRequestDTO.getParams();
        assert ((requestParams.containsKey(SCOPE_KEY)) && (requestParams.containsKey(KEY_KEY)));
        
        DownloadRequestDTO request = BeanUtils.createFrom(requestParams, DownloadRequestDTO.class);
        assert request != null;
        request.setChunkId(chunkId);
        request.setChunkSize(chunkSize);
        request.setScope(requestParams.get(SCOPE_KEY));
        request.setKey(requestParams.get(KEY_KEY));
        return request;
    }
    
    //实际下载文件内容
    public void downloadContent(File f, FileRequestDTO fileRequestDTO) throws Exception {
        assert ((f != null) && (fileRequestDTO != null));
        final Integer chunkSize = 15;//FileServerConst.DEFAULT_CHUNK_SIZE;
        //下载文件
        Integer chunkId = 0;
        Integer chunkCount = 0;
        do{
            //建立下载申请
            DownloadRequestDTO request = createDownloadRequestDTO(fileRequestDTO,chunkId,chunkSize);
            assert request != null;

            //发送下载申请
            DownloadResultDTO result = null;
            if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(fileRequestDTO.getMode())) result = downloadContentByLocal(fileRequestDTO,request);
            else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(fileRequestDTO.getMode())) result = downloadContentByRPC(fileRequestDTO,request);
            else if (FileServerConst.FILE_SERVER_MODE_HTTP_POST.equals(fileRequestDTO.getMode())) result = downloadContentByPost(fileRequestDTO,request);
            else if (FileServerConst.FILE_SERVER_MODE_OSS.equals(fileRequestDTO.getMode())) result = downloadContentByOSS(fileRequestDTO,request);
            assert result != null;
            Assert.assertEquals((Integer)0,(Integer)result.getStatus());

            FileMultipartDTO multipart = result.getData();
            assert (multipart != null);
            byte[] data = multipart.getData();
            long pos = (long) request.getChunkId() * request.getChunkSize();
            int len = result.getChunkSize();
            assert (len <= request.getChunkSize()) && (len <= data.length);
            
            RandomAccessFile rf = new RandomAccessFile(f,"rw");
            if (rf.length() < pos) rf.setLength(pos + len);
            rf.seek(pos);
            rf.write(data,0,len);
            rf.close();
            
            //申请下一块
            chunkId++;
            chunkCount = result.getChunkCount();
        } while(chunkCount > 0);
    }
    
    
    public DownloadResultDTO downloadContentByLocal(FileRequestDTO fileRequestDTO,DownloadRequestDTO request) throws Exception {
        //补全DownloadRequestDTO参数
        //发送下载申请
        fileService.setFileServerType(fileServerType,null);
        return fileService.download(request,null);
    }

    public DownloadResultDTO downloadContentByRPC(FileRequestDTO fileRequestDTO,DownloadRequestDTO request) throws Exception {
        //补全DownloadRequestDTO参数
        //发送下载申请
        fileServicePrx.setFileServerType(fileServerType);
        return fileServicePrx.download(request);
    }

    public DownloadResultDTO downloadContentByOSS(FileRequestDTO fileRequestDTO,DownloadRequestDTO request) throws Exception {
        //补全DownloadRequestDTO参数
        //发送下载申请
        return null;
    }

    public DownloadResultDTO downloadContentByPost(FileRequestDTO fileRequestDTO,DownloadRequestDTO request) throws Exception {
        if (fileRequestDTO == null) fileRequestDTO = new FileRequestDTO();
        if (fileRequestDTO.getUrl() == null) fileRequestDTO.setUrl("http://localhost:8087/FileServer/download");
        //补全DownloadRequestDTO参数
        Map<String,String> requestParams = fileRequestDTO.getParams();
        String downloadContentType = ((requestParams != null) && (requestParams.containsKey("contentType"))) ? downloadContentType = requestParams.get("contentType") : "application/json";
        String setFileServerTypeUrl = ((requestParams != null) && (requestParams.containsKey("fileServerTypeUrl"))) ? downloadContentType = requestParams.get("fileServerTypeUrl") : "http://localhost:8087/FileServer/setFileServerType";
        String setFileServerTypeContentType = ((requestParams != null) && (requestParams.containsKey("fileServerTypeContentType"))) ? downloadContentType = requestParams.get("fileServerTypeContentType") : "application/x-www-form-urlencoded";

        //初始化post客户端
        CloseableHttpClient client = HttpClients.createDefault();

        //选择文件服务器类型
        Map<String,Integer> params = new HashMap<>();
        params.put("type",fileServerType);
        CloseableHttpResponse r1 = HttpUtils.postData(client,setFileServerTypeUrl,
                setFileServerTypeContentType,params);
        Assert.assertEquals(200,r1.getStatusLine().getStatusCode());
        r1.close();

        //发送下载申请
        CloseableHttpResponse r2 = HttpUtils.postData(client,fileRequestDTO.getUrl(),downloadContentType,request);
        Assert.assertEquals(200,r2.getStatusLine().getStatusCode());
        String resultString = EntityUtils.toString(r2.getEntity());
        r2.close();

        client.close();

        DownloadResultDTO result = JsonUtils.json2Obj(resultString,DownloadResultDTO.class);
        return result;
    }

    //实际上传文件内容
    public void uploadContent(File f, FileRequestDTO fileRequestDTO) throws Exception {
        assert ((f != null) && (fileRequestDTO != null));
        final Integer chunkSize = 10;//FileServerConst.DEFAULT_CHUNK_SIZE;
        Integer chunkCount = (int)(f.length() / (long)chunkSize) + 1;
        //上传文件
        for (Integer chunkId=0; chunkId<chunkCount; chunkId++) {
            //建立上传申请
            UploadRequestDTO request = createUploadRequestDTO(f, fileRequestDTO, chunkId, chunkSize, chunkCount);
            //发送上传申请
            UploadResultDTO result = null;
            if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(fileRequestDTO.getMode())) result = uploadContentByLocal(fileRequestDTO,request);
            else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(fileRequestDTO.getMode())) result = uploadContentByRPC(fileRequestDTO,request);
            else if (FileServerConst.FILE_SERVER_MODE_HTTP_POST.equals(fileRequestDTO.getMode())) result = uploadContentByPost(fileRequestDTO,request);
            else if (FileServerConst.FILE_SERVER_MODE_OSS.equals(fileRequestDTO.getMode())) result = uploadContentByOSS(fileRequestDTO,request);
            assert result != null;
            Assert.assertEquals((Integer)0,(Integer)result.getStatus());
        }
    }
    
    
    public UploadResultDTO uploadContentByLocal(FileRequestDTO fileRequestDTO,UploadRequestDTO request) throws Exception {
        //补全UploadRequestDTO参数
        //发送上传申请
        fileService.setFileServerType(fileServerType,null);
        return fileService.upload(request,null);
    }

    public UploadResultDTO uploadContentByRPC(FileRequestDTO fileRequestDTO,UploadRequestDTO request) throws Exception {
        //补全UploadRequestDTO参数
        //发送上传申请
        fileServicePrx.setFileServerType(fileServerType);
        return fileServicePrx.upload(request);
    }

    public UploadResultDTO uploadContentByOSS(FileRequestDTO fileRequestDTO, UploadRequestDTO request) throws Exception {
        //补全UploadRequestDTO参数
        //发送上传申请
        return null;
    }

    public UploadResultDTO uploadContentByPost(FileRequestDTO fileRequestDTO, UploadRequestDTO request) throws Exception {
        if (fileRequestDTO == null) fileRequestDTO = new FileRequestDTO();
        if (fileRequestDTO.getUrl() == null) fileRequestDTO.setUrl("http://localhost:8087/FileServer/upload");

        //补全UploadRequestDTO参数
        Map<String,String> requestParams = fileRequestDTO.getParams();
        String uploadContentType = ((requestParams != null) && (requestParams.containsKey("contentType"))) ? uploadContentType = requestParams.get("contentType") : "application/json";
        String setFileServerTypeUrl = ((requestParams != null) && (requestParams.containsKey("fileServerTypeUrl"))) ? uploadContentType = requestParams.get("fileServerTypeUrl") : "http://localhost:8087/FileServer/setFileServerType";
        String setFileServerTypeContentType = ((requestParams != null) && (requestParams.containsKey("fileServerTypeContentType"))) ? uploadContentType = requestParams.get("fileServerTypeContentType") : "application/x-www-form-urlencoded";

        //初始化post客户端
        CloseableHttpClient client = HttpClients.createDefault();

        //选择文件服务器类型
        Map<String,Integer> params = new HashMap<>();
        params.put("type",fileServerType);
        CloseableHttpResponse r1 = HttpUtils.postData(client,setFileServerTypeUrl,
                setFileServerTypeContentType,params);
        Assert.assertEquals(200,r1.getStatusLine().getStatusCode());
        r1.close();

        //发送上传申请
        CloseableHttpResponse r2 = HttpUtils.postData(client,fileRequestDTO.getUrl(),uploadContentType,request);
        Assert.assertEquals(200,r2.getStatusLine().getStatusCode());
        String resultString = EntityUtils.toString(r2.getEntity());
        r2.close();

        client.close();

        UploadResultDTO result = JsonUtils.json2Obj(resultString,UploadResultDTO.class);
        return result;
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
