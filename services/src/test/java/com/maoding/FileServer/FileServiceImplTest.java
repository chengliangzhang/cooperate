package com.maoding.FileServer;

import com.maoding.Base.IceConfig;
import com.maoding.FileServer.Config.RemoteFileServerPrx;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Storage.zeroc.*;
import com.maoding.User.zeroc.AccountDTO;
import com.maoding.User.zeroc.LoginDTO;
import com.maoding.CoreUtils.ObjectUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

/**
* FileServiceImpl Tester.
*
* @author Zhangchengliang
* @since 01/11/2018
* @version 1.0
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class FileServiceImplTest {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String testLocalFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\maoding\\FileServer\\upload_test.txt";
    private static final String testDir = "testForFileService";

    @Autowired
    IceConfig iceConfig;

    @Autowired
    private FileService fileService;
    
    private FileServicePrx remote = null;
    
    private FileServicePrx getRemote(){
        if (remote == null) {
            remote = RemoteFileServerPrx.getInstance("FileServer;192.168.13.140");
        }
        return remote;
    }

    @Test
    public void testWriteFile() throws Exception{
        writeNodeFileInfo();
    }

    private int writeNodeFileInfo() throws Exception{
        FileDataDTO data = new FileDataDTO();
        data.setData(new byte[]{1,2,3});
        return fileService.writeFile(getLocalAccount(),getLocalNodeFile(),data,null);
    }

    private NodeFileDTO getLocalNodeFile() throws Exception {
        return getLocalFullNode().getFileInfo();
    }

    @Test
    public void testCreateAccessory() throws Exception{
//        createAccessorySimple();
        createAccessoryWithData();
    }

    private NodeFileDTO createAccessoryWithData() throws Exception{
        AccessoryRequestDTO request = new AccessoryRequestDTO();
        request.setPath("a/b/c.txt");
        request.setData(new byte[]{1,2,3});
        return fileService.createAccessory(getLocalAccount(),request,null);
    }

    private NodeFileDTO createAccessorySimple() throws Exception{
        AccessoryRequestDTO request = new AccessoryRequestDTO();
        request.setPath("a/b/c.txt");
        return fileService.createAccessory(getLocalAccount(),request,null);
    }

    @Test
    public void testCreateSuggestion() throws Exception {
        createSuggestionSimple();
        createSuggestionWithEmbed();
    }

    private SuggestionDTO createSuggestionSimple() throws Exception {
        SuggestionRequestDTO request = new SuggestionRequestDTO();
        request.setTypeId("1");
        request.setContent("this is a test");
        return fileService.createSuggestion(getLocalAccount(),getLocalNode(),request,null);
    }

    private SuggestionDTO createSuggestionWithEmbed() throws Exception {
        SuggestionRequestDTO request = new SuggestionRequestDTO();
        request.setTypeId("1");
        request.setData(new byte[]{1,2,3});
        return fileService.createSuggestion(getLocalAccount(),getLocalNode(),request,null);
    }

    @Test
    public void testListNode() throws Exception{
//        listAllNode();
//        listRootNode();
//        listChildNode();
//        listChildrenNode();
        assert(getLocalFullNode() != null);
        assert(getLocalFullNode() != null);
    }

    private List<SimpleNodeDTO> listChildrenNode() throws Exception {
        log.debug("\t>>>>>>>> listChildrenNode");
        return fileService.listChildrenNodeForAccount(getLocalAccount(), getLocalPath(),null);
    }

    private List<SimpleNodeDTO> listChildNode() throws Exception {
        log.debug("\t>>>>>>>> listChild");
        return fileService.listChildNodeForAccount(getLocalAccount(), getLocalPath(),null);
    }

    private List<SimpleNodeDTO> listRootNode() throws Exception {
        log.debug("\t>>>>>>>> listRootNode");
        return fileService.listRootNodeForAccount(getLocalAccount(), null);
    }

    private List<SimpleNodeDTO> listAllNode() throws Exception {
        log.debug("\t>>>>>>>> listAllNode");
        return fileService.listAllNodeForAccount(getLocalAccount(), null);
    }

    private AccountDTO getLocalAccount(){
        AccountDTO account = new AccountDTO();
        account.setId("41d244733ec54f09a255836637f2b21d");
        return account;
    }

    private SimpleNodeDTO getLocalPath() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("2a1c1afc5dcf449ab01d237b63182fd6-1");
        List<SimpleNodeDTO> list = fileService.listNodeForAccount(getLocalAccount(),query,null);
        return (ObjectUtils.isNotEmpty(list)) ? list.get(0) : null;
    }

    private SimpleNodeDTO getLocalNode() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("436833ABCAED4105AFC53FFEC9E8AECF-1");
        List<SimpleNodeDTO> list = fileService.listNodeForAccount(getLocalAccount(),query,null);
        return (ObjectUtils.isNotEmpty(list)) ? list.get(0) : null;
    }

    private FullNodeDTO getLocalFullNode() throws Exception {
        return fileService.getFileInfoForAccount(getLocalAccount(),getLocalNode(),null);
    }


//
//    @Test
//    public void testCopyRealFile() throws Exception{
////        copyRealFileFromDiskToDisk();
//        copyRealFileFromDiskToWeb();
//    }
//
//    private void copyRealFileFromDiskToDisk() throws Exception {
//        FileNodeDTO src = getLocalFileNode();
//        CoreFileDTO dst = new CoreFileDTO(null,"立项测试1/设计/初步设计/初步设计/初设一层/初设二层/1112","bbbb.txt");
//        FileServiceImpl fv = SpringUtils.getBean(FileServiceImpl.class);
//        dst = fv.createRealFileForAccount(getLocalAccount(),ConstService.FILE_SERVER_TYPE_DISK,dst,src);
//        Assert.assertNotNull(dst);
//    }
//
//    private void copyRealFileFromDiskToWeb() throws Exception {
////        FileNodeDTO src = getLocalFileNode();
////        CoreFileDTO dst = new CoreFileDTO("http://172.16.6.73:8071/fileCenter/netFile/uploadFile","设计成果/施工图设计/建筑施工图","test1.txt");
////
////        FileServiceImpl fv = SpringUtils.getBean(FileServiceImpl.class);
////        dst = fv.createRealFileForAccount(getLocalAccount(),ConstService.FILE_SERVER_TYPE_WEB,dst,src);
////        Assert.assertNotNull(dst);
//    }
//
//    @Test
//    public void testChangeOwner() throws Exception{
//        UserDTO user = new UserDTO("075be84d8fd64921b9760a9779c69b10","沈习荣");
//        AccountDTO userAccount = BeanUtils.createFrom(user,AccountDTO.class);
//        UserDTO accountUser = BeanUtils.createFrom(getLocalAccount(),UserDTO.class);
//        fileService.changeNodeOwnerForAccount(getLocalAccount(),getLocalNode(),user,null);
//        fileService.changeNodeOwnerForAccount(userAccount,getLocalNode(),accountUser,null);
//    }

//
//    private FileNodeDTO getLocalFileNode() throws Exception{
//        SimpleNodeDTO node = getLocalNode();
//        return fileService.getFileForAccount(getLocalAccount(),node,false,null);
//    }
//    private SimpleNodeDTO getLocalNode() throws Exception {
//        return fileService.getNodeByIdForAccount(getLocalAccount(),"BB6992A070FF4C5DA270122FC6668E1F1",null);
//    }
//
//    @Test
//    public void testMoveNode() throws Exception{
//        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
//        request.setFullName("aaaa.txt");
//        fileService.moveNodeForAccount(getLocalAccount(),getLocalNode(),null,request,null);
//    }
//
    @Test
    public void testLogin() throws Exception {
        loginLocal();
        loginRemote();
    }

    private LoginDTO getLocalLoginInfo() {
        LoginDTO loginInfo = new LoginDTO();
        loginInfo.setAccountId("13680809727");
        loginInfo.setPassword("123456");
        return loginInfo;
    }

    private LoginDTO getRemoteLoginInfo() {
        LoginDTO loginInfo = new LoginDTO();
        loginInfo.setAccountId("13680809727");
        loginInfo.setPassword("123456");
        return loginInfo;
    }

    private boolean loginRemote() throws Exception {
        return getRemote().login(getRemoteLoginInfo());
    }

    private boolean loginLocal() throws Exception {
        return fileService.login(getLocalLoginInfo(),null);
    }
//    @Test
//    public void testCommitFile() throws Exception {
////        commitFileToDisk();
//        commitFileToWeb();
////        remoteCommitFileToDisk();
//    }
//
//    private void commitFileToWeb() throws Exception{
//        FileNodeDTO node = getLocalFileNode();
//        CommitRequestDTO request = new CommitRequestDTO();
//        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE);
//        request.setRemark("abcde");
//        request.setUserId("12345");
//        request.setFileVersion("二提");
//        request.setMajorName("水电");
//        SimpleNodeDTO target = fileService.commitFileForAccount(getLocalAccount(),node,request,null);
//        Assert.assertNotNull(target);
//    }
//
//    private void commitFileToDisk() throws Exception{
//        FileNodeDTO node = getLocalFileNode();
//        CommitRequestDTO request = new CommitRequestDTO();
//        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_COMMIT);
//        request.setRemark("abcde");
//        request.setUserId("12345");
//        request.setFileVersion("二提");
//        request.setMajorName("水电");
//        SimpleNodeDTO target = fileService.commitFileForAccount(getLocalAccount(),node,request,null);
//        Assert.assertNotNull(target);
//    }
//
//    private void remoteCommitFileToDisk() throws Exception{
//        SimpleNodeDTO node = getRemote().getNodeById("33BB8B903D2D484492BAC3FFB40904F51");
//        assert (node != null);
//        CommitRequestDTO request = new CommitRequestDTO();
//        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_COMMIT);
//        request.setRemark("abcde");
//        request.setUserId("12345");
//        request.setFileVersion("二提");
//        request.setMajorName("水电");
//        SimpleNodeDTO target = getRemote().commitNode(node,request);
//        Assert.assertNotNull(target);
//    }
//
//    @Test
//    public void testReleaseFileNode() throws Exception {
//        fileService.releaseNodeForAccount(getLocalAccount(),getLocalNode(),0,null);
//    }
//
//    @Test
//    public void testReloadFileNode() throws Exception {
//        fileService.reloadNodeForAccount(getLocalAccount(),getLocalNode(),null);
//    }
//
    @Test
    public void testWriteFileNode() throws Exception {
        writeFile(testLocalFile,getLocalNode());
    }

    private long writeFile(String localFile, SimpleNodeDTO file) throws Exception {
        File f = new File(localFile);
        long length = f.length();
        int size = 15;

        long writeLength = 0;
        RandomAccessFile in = new RandomAccessFile(f, "r");
        for (long pos=0; pos<length; pos+=size){
            FileDataDTO fileData = createFileData(in, pos, size);
            writeLength += fileService.writeNodeForAccount(getLocalAccount(),file,fileData,null);
        }
        in.close();
        return writeLength;
    }

    private FileDataDTO createFileData(RandomAccessFile in, long pos, int size) throws Exception {
        //建立上传内容
        FileDataDTO fileData = new FileDataDTO();
        byte[] bytes = new byte[size];
        fileData.setPos(pos);
        in.seek(pos);
        fileData.setSize(in.read(bytes));
        fileData.setData(bytes);

        return fileData;
    }
//
//    @Test
//    public void testReadFileNode() throws Exception {
//        FileNodeDTO file = getLocalFileNode();
//        readFile(file,testLocalFile + "download.txt");
//    }
//
//
//
//    private void readFile(FileNodeDTO file,String localFile) throws Exception {
//        File f = new File(localFile);
//        int size = 10;
//
//        RandomAccessFile out = new RandomAccessFile(f, "rw");
//        long pos = 0;
//        while (size > 0){
//            FileDataDTO fileData = fileService.readFileNode(file, pos,size,null);
//            if (fileData == null) break;
//            pos = fileData.getPos();
//            size = fileData.getSize();
//            if (out.length() < (pos + size)) out.setLength(pos + size);
//            out.seek(pos);
//            out.write(fileData.getData(),0,size);
//            pos += size;
//        }
//        out.close();
//    }
//
//    @Test
//    public void testIssueNode() throws Exception {
//        issueLocal();
//    }
//
//    private void issueLocal() throws Exception {
//        FileNodeDTO node = getLocalFileNode();
//        CommitRequestDTO request = new CommitRequestDTO();
//        request.setPid("2e6611ef581848048f76eaa64c385ca9");
//        request.setUserId("4736bc67a64f418e9a585f303e95eb2b");
//        LoginDTO user = new LoginDTO(null,null,false,"123456","15353032981");
//        fileService.login(user,null);
//        fileService.issueFileForAccount(getLocalAccount(),node,request,null);
//    }
//
    @Test
    public void testCreateNode() throws Exception {
        createLocalDirectory();
        createLocalFile();
    }


    private SimpleNodeDTO createLocalFile() throws Exception{
        log.debug("\t>>>>>>>> createLocalNode");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFileLength(10);
        request.setFullName("/father2/aaaa.txt");
        return fileService.createNodeForAccount(getLocalAccount(),getLocalPath(),request,null);
    }

    private SimpleNodeDTO createLocalDirectory() throws Exception{
        log.debug("\t>>>>>>>> createLocalDirectory");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(true);
        request.setFullName("father/child/child3");
        return fileService.createNodeForAccount(getLocalAccount(),getLocalPath(),request,null);
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
