package com.maoding.FileServer;

import com.maoding.Base.CoreRemoteService;
import com.maoding.Common.Config.IceConfig;
import com.maoding.CoreUtils.BeanUtils;
import com.maoding.CoreUtils.ObjectUtils;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Storage.Dao.StorageDao;
import com.maoding.Storage.zeroc.*;
import com.maoding.User.zeroc.AccountDTO;
import com.maoding.User.zeroc.LoginDTO;
import com.maoding.User.zeroc.WebRoleDTO;
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
    private static final String testLocalLargeFile = System.getProperty("user.dir") + "\\src\\test\\java\\com\\maoding\\FileServer\\卯丁协同设计用户操作手册.docx";
    private static final String testDir = "testForFileService";

    @Autowired
    IceConfig iceConfig;

    @Autowired
    private FileService fileService;
    
    private FileServicePrx remote = null;
    
    private FileServicePrx getRemote(){
        if (remote == null) {
            CoreRemoteService<FileServicePrx> prx = new CoreRemoteService<>();
            remote = prx.getServicePrx("FileService","FileServer;192.168.13.140",FileServicePrx.class,_FileServicePrxI.class);
//            remote = prx.getServicePrx("FileService","FileServer;120.24.238.128",FileServicePrx.class,_FileServicePrxI.class);
        }
        return remote;
    }

    @Test
    public void testSetWebRoleStatus() throws Exception {
        fileService.setWebRoleStatus(getLocalWebRole(),"1",null);
    }

    private WebRoleDTO getLocalWebRole() throws Exception {
        return fileService.getWebRole(getLocalAccount(),getLocalNode(),null);
    }
    @Test
    public void testCreateAnnotate() throws Exception {
        AnnotateRequestDTO request = new AnnotateRequestDTO();
        request.setData(new byte[]{0x33,0x34,0x35});
        fileService.createAnnotateCheck(getLocalAccount(),getLocalNode(),request,null);
    }

    @Test
    public void testListWebArchive() throws Exception {
        List<SimpleNodeDTO> list;
        list = listWebArchive();

    }

    private List<SimpleNodeDTO> listWebArchive() throws Exception {
        log.debug("\t>>>>>>>> listWebArchive");
        return fileService.listWebArchiveDirForAccount(getLocalAccount(),null,null);
    }

    @Test
    public void testListWebRoleTask() throws Exception {
        List<WebRoleDTO> list = fileService.listWebRoleTask(getLocalAccount(),null);
    }

    @Test
    public void testDelete() throws Exception {
//        deleteLocalFile();
        deleteLocalDir();
//        deleteRemoteDir();
    }

    private void deleteRemoteDir() throws Exception {
        SimpleNodeDTO node = getRemote().createNodeForAccount(getRemoteAccount(),getRemoteTask(),getCreateDirRequest());
        getRemote().deleteNodeForAccount(getLocalAccount(),node);
        getRemote().deleteNodeForAccount(getLocalAccount(),node);
    }

    private void deleteLocalDir() throws Exception {
        SimpleNodeDTO node = fileService.createNodeForAccount(getLocalAccount(),getLocalDir(),getCreateDirRequest(),null);
        fileService.deleteNodeForAccount(getLocalAccount(),node,null);
        fileService.deleteNodeForAccount(getLocalAccount(),node,null);
    }

    private void deleteLocalFile() throws Exception {
        SimpleNodeDTO node = fileService.createNodeForAccount(getLocalAccount(),getLocalDir(),getCreateFileRequest(),null);
        fileService.deleteNodeForAccount(getLocalAccount(),node,null);
        fileService.deleteNodeForAccount(getLocalAccount(),node,null);
    }

    @Test
    public void testUpdateVersion() throws Exception {
        updateVersion();
    }

    private SimpleNodeDTO updateVersion() throws Exception {
        log.debug("\t>>>>>>>> updateVersion");
        CommitRequestDTO request = new CommitRequestDTO();
        request.setFileVersion("v2.0");
        return fileService.updateVersion(getLocalAccount(),getLocalNode(),getLocalNode(),request,null);
    }

    @Test
    public void testCreateVersion() throws Exception {
        createVersion();
    }

    private SimpleNodeDTO createVersion() throws Exception {
        log.debug("\t>>>>>>>> createVersion");
        CommitRequestDTO request = new CommitRequestDTO();
        request.setFileVersion("v1.0");
        return fileService.createVersion(getLocalAccount(),getLocalNode(),"a.txt",request,null);
    }

    @Test
    public void testMoveNode() throws Exception {
        SimpleNodeDTO node;
//        changeFileName();
//        moveFile();
//        node = changeDirName();
//        node = moveDir();
//        node = changeRemoteDir();
//        node = moveRemoteDir();
        node = moveRemoteFile();
    }

    private MoveNodeRequestDTO getMoveDirRequest() throws Exception{
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("cde");
        return request;
    }

    private MoveNodeRequestDTO getMoveFileRequest() throws Exception{
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("x.dwg");
        return request;
    }

    private SimpleNodeDTO moveRemoteFile() throws Exception {
        log.debug("\t>>>>>>>> moveRemoteFile");
        return getRemote().moveNodeForAccount(getRemoteAccount(), getRemoteFile(),getRemoteTask(),getMoveFileRequest());
    }

    private SimpleNodeDTO moveRemoteDir() throws Exception {
        log.debug("\t>>>>>>>> moveRemoteDir");
        return getRemote().moveNodeForAccount(getRemoteAccount(), getRemoteDir(),getRemoteTask(),getMoveDirRequest());
    }

    private SimpleNodeDTO changeRemoteDir() throws Exception {
        log.debug("\t>>>>>>>> changeRemoteDir");
        return getRemote().moveNodeForAccount(getRemoteAccount(), getRemoteDir(),null,getMoveDirRequest());
    }

    private SimpleNodeDTO moveDir() throws Exception {
        log.debug("\t>>>>>>>> moveDir");
        return fileService.moveNodeForAccount(getLocalAccount(), getLocalDir(),getLocalTask(),getMoveDirRequest(),null);
    }

    private SimpleNodeDTO changeDirName() throws Exception {
        log.debug("\t>>>>>>>> changeDirName");
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("abcde");
        return fileService.moveNodeForAccount(getLocalAccount(), getLocalDir(),null,request,null);
    }

    private SimpleNodeDTO changeFileName() throws Exception {
        log.debug("\t>>>>>>>> changeFileName");
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("axax.txt");
        return fileService.moveNodeForAccount(getLocalAccount(),getLocalNode(), getLocalDir(),request,null);
    }

    private SimpleNodeDTO moveFile() throws Exception {
        log.debug("\t>>>>>>>> moveFile");
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("x/y/aaaa.txt");
        return fileService.moveNodeForAccount(getLocalAccount(),getLocalNode(), getLocalDir(),request,null);
    }

    private NodeFileDTO getLocalNodeFile() throws Exception {
        QueryNodeFileDTO query = new QueryNodeFileDTO();
        query.setId("108163A863344C208E10FBEDC961F7F5");
        query.setServerTypeId("1");
        query.setServerAddress("127.0.0.1");
//        query.setServerAddress("c:/work/file_server");
        List<NodeFileDTO> fileList = fileService.listFile(getLocalAccount(),query,null);
        return fileList.get(0);
    }

    @Test
    public void testAddAccessory() throws Exception{
        addAccessorySimple();
        addAccessoryWithData();
    }

    private NodeFileDTO addAccessoryWithData() throws Exception{
        log.debug("\t>>>>>>>> addAccessoryWithData");
        AccessoryRequestDTO request = new AccessoryRequestDTO();
        request.setPath("a/b/c.txt");
        request.setData(new byte[]{1,2,3});
//        return fileService.createAccessory(getLocalAccount(),request,null);
        return null;
    }

    private NodeFileDTO addAccessorySimple() throws Exception{
        log.debug("\t>>>>>>>> addAccessorySimple");
        AccessoryRequestDTO request = new AccessoryRequestDTO();
        request.setPath("a/b/c.txt");
//        return fileService.addAccessory(getLocalAccount(),request,null);
        return null;
    }

    @Test
    public void testListCANode() throws Exception {
        List<CANodeDTO> list;
//        list = listCANode();
//        list = listDesignNode();
        list = listRemoteDesignNode();
    }

    private List<CANodeDTO> listRemoteDesignNode() throws Exception {
        log.debug("\t>>>>>>>> listRemoteDesignNode");
        return getRemote().listDesignNode(getRemoteAccount());
    }

    private List<CANodeDTO> listDesignNode() throws Exception {
        log.debug("\t>>>>>>>> listDesignNode");
        return fileService.listDesignNode(getLocalAccount(),null);
    }

    private List<CANodeDTO> listCANode() throws Exception {
        log.debug("\t>>>>>>>> listCANode");
        return fileService.listCANode(getLocalAccount(), null);
    }

    @Test
    public void testListNode() throws Exception{
        List<SimpleNodeDTO> list;
        SimpleNodeDTO node;
//        List<SimpleNodeDTO> list = listAllNode();
//        listRootNode();
//        list = listChildNode();
//        list = listChildrenNode();
//        list = listNotUserNode();
//        list = listRemoteChild();
//        list = listRemoteAll();
        node = getRemoteByPath();
//        list = listRemoteRoot();
//        list = listLocalRoot();
    }


    private QueryNodeDTO getQueryTaskChild() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(getLocalTask().getId());
        return query;
    }

    private QueryNodeDTO getQueryTaskChildren() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(getLocalTask().getPath());
        return query;
    }

    private List<SimpleNodeDTO> listNotUserNode() throws Exception {
        log.debug("\t>>>>>>>> listNotUserNode");
        QueryNodeDTO query = getQueryTaskChild();
        query.setNotOwnerUserId(getLocalAccount().getId());
        return fileService.listNodeForAccount(getLocalAccount(),query,null);
    }

    private SimpleNodeDTO getRemoteByPath() throws Exception {
        log.debug("\t>>>>>>>> getRemoteByPath");
        return getRemote().getNodeByPathForAccount(getRemoteAccount(),"/卯丁软件开发/设计/三月份大家一起努力吧！/新建 Microsoft Word 文档.docx");
    }

    private List<SimpleNodeDTO> listRemoteAll() throws Exception {
        log.debug("\t>>>>>>>> listRemoteAll");
        return getRemote().listAllNodeForAccount(getRemoteAccount());
    }

    private List<SimpleNodeDTO> listRemoteChild() throws Exception {
        log.debug("\t>>>>>>>> listRemoteChild");
        SimpleNodeDTO parent = getRemote().getNodeByIdForAccount(getRemoteAccount(), getRemoteTask().getId());
        return getRemote().listChildNodeForAccount(getRemoteAccount(),parent);
    }

    private List<SimpleNodeDTO> listRemoteRoot() throws Exception {
        log.debug("\t>>>>>>>> listRemoteRoot");
        return getRemote().listRootNodeForAccount(getRemoteAccount());
    }
    @Autowired
    StorageDao storageDao;

    private List<SimpleNodeDTO> listLocalRoot() throws Exception {
        log.debug("\t>>>>>>>> listLocalRoot");
        return fileService.listRootNodeForAccount(getRemoteAccount(),null);
    }


    private List<SimpleNodeDTO> listChildrenNode() throws Exception {
        log.debug("\t>>>>>>>> listChildrenNode");
        return fileService.listChildrenNodeForAccount(getLocalAccount(), getLocalTask(),null);
    }

    private List<SimpleNodeDTO> listChildNode() throws Exception {
        log.debug("\t>>>>>>>> listChild");
        return fileService.listChildNodeForAccount(getLocalAccount(), getLocalTaskParent(),null);
    }

    private List<SimpleNodeDTO> listRootNode() throws Exception {
        log.debug("\t>>>>>>>> listRootNode");
        return fileService.listRootNodeForAccount(getLocalAccount(), null);
    }

    private List<SimpleNodeDTO> listAllNode() throws Exception {
        log.debug("\t>>>>>>>> listAllNode");
        return fileService.listAllNodeForAccount(getLocalAccount(), null);
    }

    private AccountDTO getRemoteAccount(){
        return getAccount("07649b3d23094f28bfce78930bf4d4ac");
    }

    private AccountDTO getLocalAccount(){
        return getAccount("d437448683314cad91dc30b68879901d");
    }

    private AccountDTO getAccount(String id){
        AccountDTO account = new AccountDTO();
        account.setId(id);
        if ("d437448683314cad91dc30b68879901d".equals(id)){
            account.setName("张成亮");
        }
        return account;
    }

    private SimpleNodeDTO getLocalDir() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("2D4DEF1EDD21401D95848A1A1ED3910C-3");
        List<SimpleNodeDTO> list = fileService.listNodeForAccount(getLocalAccount(),query,null);
        return (ObjectUtils.isNotEmpty(list)) ? list.get(0) : null;
    }

    private SimpleNodeDTO getLocalTask() throws Exception {
        return getLocalNode("552f9b02dc2d4e9c9ee9a9ce402b7e07-3");
    }

    private SimpleNodeDTO getLocalTaskParent() throws Exception {
        return getLocalNode("552f9b02dc2d4e9c9ee9a9ce402b7e07-1");
    }

    private SimpleNodeDTO getRemoteTask() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("2963f576c6dc46bba0624153ce53c308-1");
        List<SimpleNodeDTO> list = getRemote().listNodeForAccount(getRemoteAccount(),query);
        return list.get(0);
    }

    private SimpleNodeDTO getLocalNode() throws Exception {
        return getLocalNode("038D7EC5153A4F839653696E21AC31D3-1");
    }

    private SimpleNodeDTO getLocalNode(String id) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> list = fileService.listNodeForAccount(getLocalAccount(),query,null);
        return list.get(0);
    }

    private SimpleNodeDTO getRemoteNode() throws Exception {
        return getRemoteNode("5A6CF6770631475BAF811A87B0AF7B34-1");
    }

    private SimpleNodeDTO getRemoteDir() throws Exception {
        return getRemoteNode("B7EBA3416F744B4E8CCFEF8BFCFA6EF8-3");
    }

    private SimpleNodeDTO getRemoteFile() throws Exception {
        return getRemoteNode("F0FB84C661974F35A690251961A409A1-1");
    }

    private SimpleNodeDTO getRemoteNode(String id) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> list = getRemote().listNodeForAccount(getRemoteAccount(),query);
        return list.get(0);
    }

    private FullNodeDTO getLocalFullNode() throws Exception {
        return fileService.getFullNodeForAccount(getLocalAccount(),getLocalNode(),null);
    }

    @Test
    public void testRead() throws Exception {
//        readFile();
        FileDataDTO data = readNode();
    }

    private FileDataDTO readNode() throws Exception {
        log.debug("\t>>>>>>>> readNode");
        return fileService.readNodeForAccount(getLocalAccount(),getLocalNode(),0,0,null);
    }

    private FileDataDTO readFile() throws Exception {
        log.debug("\t>>>>>>>> readFile");
        return fileService.readFile(getLocalAccount(),getLocalNodeFile(),0,0,null);
    }

    @Test
    public void testLogin() throws Exception {
//        assert (loginLocal());
        loginRemote();
    }
    private boolean loginRemote() throws Exception {
        log.debug("\t>>>>>>>> login");
        return getRemote().login(getRemoteLoginInfo());
    }

    private boolean loginLocal() throws Exception {
        log.debug("\t>>>>>>>> login");
        return fileService.login(getLocalLoginInfo(),null);
    }


    private LoginDTO getLocalLoginInfo() {
        return new LoginDTO(
                "",
                "",
                false,
                "123456",
                "13680809727");
    }

    private LoginDTO getRemoteLoginInfo() {
        return new LoginDTO(
                "",
                "",
                false,
                "123456",
                "13680809727");
    }


    @Test
    public void testCommit() throws Exception {
        SimpleNodeDTO node;
//        node = commitNode();
//        checkNode();
//        issueNode();
//        askLocalCA();
//        askRemoteCA();
//        audit();
    }

    private SimpleNodeDTO audit() throws Exception {
        CANodeDTO caNode = getLocalCANode();
        caNode.setIsPassAudit(!caNode.getIsPassAudit());
        return fileService.auditNodeRequestForAccount(getLocalAccount(),caNode,null);
    }

    private SimpleNodeDTO askRemoteCA() throws Exception {
        return getRemote().askCANodeRequestForAccount(getRemoteAccount(), getRemoteCANode());
    }

    private SimpleNodeDTO askLocalCA() throws Exception {
        return fileService.askCANodeRequestForAccount(getLocalAccount(), getLocalCANode(),null);
    }

    private CANodeDTO getLocalCANode() throws Exception {
        List<CANodeDTO> list = fileService.listCANode(null,null);
        return list.get(0);
    }

    private CANodeDTO getRemoteCANode() throws Exception {
        return BeanUtils.createCleanFrom(getRemoteNode(),CANodeDTO.class);
    }

    private SimpleNodeDTO issueNode() throws Exception {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setOwnerUserId("123");
        request.setMajorName("建筑");
        request.setFileVersion("v3.0");
        request.setPid(getLocalSkyParent().getId());
        return fileService.issueNodeForAccount(getLocalAccount(),getLocalNode(),request,null);
    }

    private SimpleNodeDTO getLocalSkyParent() throws Exception {
        List<SimpleNodeDTO> list = fileService.listWebArchiveDirForAccount(getLocalAccount(),getLocalNode().getProjectId(),null);
        return (ObjectUtils.isNotEmpty(list)) ? list.get(0) : null;
    }

//    private SimpleNodeDTO checkNode() throws Exception {
//        CommitRequestDTO request = new CommitRequestDTO();
//        request.setOwnerUserId("123");
//        request.setMajorName("建筑");
//        request.setFileVersion("v3.0");
//        return fileService.checkNodeRequestForAccount(getLocalAccount(),getLocalNode(),request,null);
//    }

    private SimpleNodeDTO commitNode() throws Exception {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setOwnerUserId("123");
        request.setMajorName("建筑");
        request.setFileVersion("v3.0");
        return fileService.commitNodeForAccount(getLocalAccount(),getLocalNode(),request,null);
    }

    @Test
    public void testGetWebRole() throws Exception {
        getWebRole();
    }

    private WebRoleDTO getWebRole() throws Exception {
        log.debug("\t>>>>>>>> getWebRole");
        return  fileService.getWebRole(getLocalAccount(),getLocalNode(),null);
    }


    @Test
    public void testRelease() throws Exception {
//        releaseFile();
        releaseNode();
//        releaseNodeWithLength();
    }

    private void releaseNodeWithLength() throws Exception {
        log.debug("\t>>>>>>>> releaseNodeWithLength");
        fileService.releaseNodeForAccount(getLocalAccount(),getLocalNode(),100,null);
    }

    private void releaseNode() throws Exception {
        log.debug("\t>>>>>>>> releaseNode");
        fileService.releaseNodeForAccount(getLocalAccount(),getLocalNode(),0,null);
    }

    private void releaseFile() throws Exception {
        log.debug("\t>>>>>>>> releaseFile");
        fileService.releaseFile(getLocalAccount(),getLocalNodeFile(),"a/b/c.txt",null);
    }

    @Test
    public void testReloadFileNode() throws Exception {
//        reloadFile();
        reloadNode();
    }

    private void reloadNode() throws Exception {
        log.debug("\t>>>>>>>> reloadNode");
        fileService.reloadNodeForAccount(getLocalAccount(),getLocalNode(),null);
    }

    private void reloadFile() throws Exception {
        log.debug("\t>>>>>>>> reloadFile");
        fileService.reloadFile(getLocalAccount(),getLocalNodeFile(),"a/b/c.txt",null);
    }

    @Test
    public void testWrite() throws Exception {
        int n;
//        writeNode(testLocalFile,getLocalNode());
//        writeNode(testLocalLargeFile,getLocalNode());
//        writeFile();
        n = writeRemoteNode();
    }

    private int writeRemoteNode() throws Exception{
        log.debug("\t>>>>>>>> writeRemoteNode");
        FileDataDTO data = new FileDataDTO();
        data.setData(new byte[]{0x30,0x31,0x32});
        return getRemote().writeNodeForAccount(getRemoteAccount(),getRemoteNode(),data,null);
    }

    private int writeFile() throws Exception{
        log.debug("\t>>>>>>>> writeFile");
        FileDataDTO data = new FileDataDTO();
        data.setData(new byte[]{0x30,0x31,0x32});
        return fileService.writeFile(getLocalAccount(),getLocalNodeFile(),data,"x/c.txt",null);
    }

    private long writeNode(String localFile, SimpleNodeDTO file) throws Exception {
        log.debug("\t>>>>>>>> writeNode");
        File f = new File(localFile);
        long length = f.length();
        int size = 8192000;

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

    @Test
    public void testCreateNode() throws Exception {
        SimpleNodeDTO node;
//        createLocalDirectory();
//        createLocalFileWithSubDir();
//        createLocalLargeFile();
//        createRangeChild();
//        createTaskFile();
        node = createRemoteTaskFile();
    }

    private SimpleNodeDTO createRemoteTaskFile() throws Exception{
        log.debug("\t>>>>>>>> createRemoteTaskFile");
        CreateNodeRequestDTO request = getCreateFileRequest();
        return getRemote().createNodeForAccount(getRemoteAccount(), getRemoteTask(),request);
    }

    private CreateNodeRequestDTO getCreateFileRequest() throws Exception{
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFullName("aaaa.txt");
        return request;
    }

    private CreateNodeRequestDTO getCreateDirRequest() throws Exception{
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(true);
        request.setFullName("xxxx");
        return request;
    }

    private SimpleNodeDTO createTaskFile() throws Exception{
        log.debug("\t>>>>>>>> createTaskFile");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFullName("aaaa.txt");
        return fileService.createNodeForAccount(getLocalAccount(), getLocalTask(),request,null);
    }


    private SimpleNodeDTO createRangeChild() throws Exception {
        SimpleNodeDTO parent = getLocalSimpleNode("d1ba184a668d49789a224e8e8200fb17-1");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFileLength(10);
        request.setFullName("aaaa.txt");
        SimpleNodeDTO node = fileService.createNodeForAccount(getLocalAccount(), parent,request,null);
        return node;
    }

    private SimpleNodeDTO getLocalSimpleNode(String id) throws Exception {
        return fileService.getNodeByIdForAccount(getLocalAccount(),id,null);
    }

    private SimpleNodeDTO createLocalLargeFile() throws Exception{
        log.debug("\t>>>>>>>> createLocalLargeFile");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFullName("扩初jx-lm.dwg");
        return fileService.createNodeForAccount(getLocalAccount(), getLocalTask(),request,null);
    }


    private SimpleNodeDTO createRemoteDirectory() throws Exception{
        log.debug("\t>>>>>>>> createRemoteDirectory");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(true);
        request.setFullName("child3");
        return fileService.createNodeForAccount(getRemoteAccount(), getRemoteTask(),request,null);
    }

    private SimpleNodeDTO createLocalFileWithSubDir() throws Exception{
        log.debug("\t>>>>>>>> createLocalFileWithSubDir");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFileLength(10);
        request.setFullName("/father2/aaaa.txt");
        return fileService.createNodeForAccount(getLocalAccount(), getLocalDir(),request,null);
    }

    private SimpleNodeDTO createLocalDirectory() throws Exception{
        log.debug("\t>>>>>>>> createLocalDirectory");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(true);
        request.setFullName("father/child/child3");
        return fileService.createNodeForAccount(getLocalAccount(), getLocalTask(),request,null);
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
