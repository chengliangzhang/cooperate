package com.maoding.Storage;

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

import java.io.File;
import java.io.RandomAccessFile;
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

    /** 文件服务器使用LocalServer,使用本地调用文件服务器接口 */
    @Test
    public void testUploadForLocal() throws Exception {
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
        testUpload(FileServerConst.FILE_SERVER_MODE_LOCAL);
        testUpload(FileServerConst.FILE_SERVER_MODE_RPC);
    }

    @Test
    public void testDownloadForLocal() throws Exception {
        fileServerType = FileServerConst.FILE_SERVER_TYPE_LOCAL;
        testDownload(FileServerConst.FILE_SERVER_MODE_LOCAL);
        testDownload(FileServerConst.FILE_SERVER_MODE_RPC);
    }

    /** 通用测试接口 */
    public void testUpload(Integer mode) throws Exception {
        if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)) {
            uploadByLocal();
        } else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(mode)){
            uploadByRPC();
        } else if (FileServerConst.FILE_SERVER_MODE_HTTP_POST.equals(mode)) {
            //uploadByPost();
        }
    }
     public void testDownload(Integer mode) throws Exception {
         if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(mode)) {
             downloadByLocal();
         } else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(mode)){
             downloadByRPC();
         } else if (FileServerConst.FILE_SERVER_MODE_HTTP_POST.equals(mode)) {
             //downloadByPost();
         }
    }

    //建立协作文件信息
    public CooperateFileDTO createCooperateFileDTO(String localFile,String dirName,String fileName) throws Exception {
        //补全参数
        if (localFile == null) localFile = testUploadLocalFile;
        if (dirName == null) dirName = testDir;
        if (fileName == null) fileName = StringUtils.getFileName(testUploadLocalFile);

        CooperateFileDTO fileInfo = new CooperateFileDTO();
        fileInfo.setLocalFile(localFile);
        fileInfo.setDirName(dirName);
        fileInfo.setFileName(fileName);
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
        assert ((fileRequestDTO != null) && (fileRequestDTO.params != null));
        if (chunkId == null) chunkId = 0;
        if (chunkSize == null) chunkSize = FileServerConst.DEFAULT_CHUNK_SIZE;
        Map<String,String> params = fileRequestDTO.getParams();
        assert ((params.containsKey(SCOPE_KEY)) && (params.containsKey(KEY_KEY)));

        //建立上传内容
        FileMultipartDTO multipart = BeanUtils.createFrom(params, FileMultipartDTO.class);
        assert multipart != null;
        long pos = (long)chunkId * chunkSize;
        RandomAccessFile rf = new RandomAccessFile(f,"r");
        assert (pos < rf.length());
        rf.seek(pos);
        byte[] bytes = new byte[chunkSize];
        multipart.setScope(params.get(SCOPE_KEY));
        multipart.setKey(params.get(KEY_KEY));
        multipart.setPos(pos);
        multipart.setSize(rf.read(bytes));
        multipart.setData(bytes);
        rf.close();

        //建立上传申请对象
        UploadRequestDTO request = BeanUtils.createFrom(params, UploadRequestDTO.class);
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
    public void uploadByLocal() throws Exception {
        //选择文件服务器类型
        fileService.setFileServerType(fileServerType,null);

        //获取文件服务器接口
        CooperateFileDTO fileInfo = createCooperateFileDTO();
        FileRequestDTO fileRequestDTO = storageService.requestUpload(fileInfo, FileServerConst.FILE_SERVER_MODE_DEFAULT,null);

        //上传文件内容
        fileRequestDTO.setMode(FileServerConst.FILE_SERVER_MODE_LOCAL);
        uploadContent(new File(fileInfo.getLocalFile()),fileRequestDTO);
    }

    public void uploadByRPC() throws Exception {
        //选择文件服务器类型
        fileServicePrx.setFileServerType(fileServerType);

        //获取文件服务器接口
        CooperateFileDTO fileInfo = createCooperateFileDTO();
        FileRequestDTO fileRequestDTO = storageServicePrx.requestUpload(fileInfo, FileServerConst.FILE_SERVER_MODE_RPC);

        //上传文件内容
        uploadContent(new File(fileInfo.getLocalFile()),fileRequestDTO);
    }

    //申请上传
    public void downloadByLocal() throws Exception {
        //选择文件服务器类型
        fileService.setFileServerType(fileServerType,null);

        //获取文件服务器接口
        CooperateFileDTO fileInfo = createCooperateFileDTO(testDownloadLocalFile,testDir,StringUtils.getFileName(testUploadLocalFile));
        FileRequestDTO fileRequestDTO = storageService.requestDownload(fileInfo, FileServerConst.FILE_SERVER_MODE_DEFAULT,null);

        //下载文件内容
        fileRequestDTO.setMode(FileServerConst.FILE_SERVER_MODE_LOCAL);
        downloadContent(new File(fileInfo.getLocalFile()),fileRequestDTO);
    }

    public void downloadByRPC() throws Exception {
        //选择文件服务器类型
        fileServicePrx.setFileServerType(fileServerType);

        //获取文件服务器接口
        CooperateFileDTO fileInfo = createCooperateFileDTO();
        FileRequestDTO fileRequestDTO = storageServicePrx.requestDownload(fileInfo, FileServerConst.FILE_SERVER_MODE_RPC);

        //上传文件内容
        downloadContent(new File(fileInfo.getLocalFile()),fileRequestDTO);
    }

    public DownloadRequestDTO createDownloadRequestDTO(FileRequestDTO fileRequestDTO,Integer chunkId,Integer chunkSize){
        final String SCOPE_KEY = "scope";
        final String KEY_KEY = "key";

        //检查参数
        assert ((fileRequestDTO != null) && (fileRequestDTO.params != null));
        assert (chunkId != null);
        if (chunkSize == null) chunkSize = FileServerConst.DEFAULT_CHUNK_SIZE;
        Map<String,String> params = fileRequestDTO.getParams();
        assert ((params.containsKey(SCOPE_KEY)) && (params.containsKey(KEY_KEY)));
        
        DownloadRequestDTO request = BeanUtils.createFrom(params, DownloadRequestDTO.class);
        assert request != null;
        request.setChunkId(chunkId);
        request.setChunkSize(chunkSize);
        request.setScope(params.get(SCOPE_KEY));
        request.setKey(params.get(KEY_KEY));
        return request;
    }
    
    //实际下载文件内容
    public void downloadContent(File f, FileRequestDTO fileRequestDTO) throws Exception {
        assert ((f != null) && (fileRequestDTO != null));
        final Integer chunkSize = 10;//FileServerConst.DEFAULT_CHUNK_SIZE;
        //下载文件
        Integer chunkId = 0;
        Integer chunkCount = 0;
        do{
            //建立下载申请
            DownloadRequestDTO request = createDownloadRequestDTO(fileRequestDTO,chunkId,chunkSize);
            assert request != null;

            //发送下载申请
            DownloadResultDTO result = null;
            if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(fileRequestDTO.getMode())) result = downloadContentByLocal(request);
            else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(fileRequestDTO.getMode())) result = downloadContentByRPC(request);
            assert result != null;
            Assert.assertEquals((Integer)0,result.getStatus());

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
    
    
    public DownloadResultDTO downloadContentByLocal(DownloadRequestDTO request) throws Exception {
        fileService.setFileServerType(fileServerType,null);
        return fileService.download(request,null);
    }

    public DownloadResultDTO downloadContentByRPC(DownloadRequestDTO request) throws Exception {
        fileServicePrx.setFileServerType(fileServerType);
        return fileServicePrx.download(request);
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
            if (FileServerConst.FILE_SERVER_MODE_LOCAL.equals(fileRequestDTO.getMode())) result = uploadContentByLocal(request);
            else if (FileServerConst.FILE_SERVER_MODE_RPC.equals(fileRequestDTO.getMode())) result = uploadContentByRPC(request);
            assert result != null;
            Assert.assertEquals((Integer)0,result.getStatus());
        }       
    }
    
    
    public UploadResultDTO uploadContentByLocal(UploadRequestDTO request) throws Exception {
        fileService.setFileServerType(fileServerType,null);
        return fileService.upload(request,null);
    }

    public UploadResultDTO uploadContentByRPC(UploadRequestDTO request) throws Exception {
        fileServicePrx.setFileServerType(fileServerType);
        return fileServicePrx.upload(request);
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
