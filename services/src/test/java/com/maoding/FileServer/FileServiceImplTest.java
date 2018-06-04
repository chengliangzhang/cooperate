package com.maoding.FileServer;


import com.maoding.common.config.IceConfig;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.StringUtils;
import com.maoding.fileServer.zeroc.*;
import com.maoding.storage.zeroc.*;
import com.maoding.user.zeroc.AccountDTO;
import com.maoding.user.zeroc.LoginDTO;
import com.maoding.user.zeroc.WebRoleDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.RandomAccessFile;
import java.util.ArrayList;
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

    private final boolean TEST_LOCAL = false;
    private final boolean SAME_TEST = false;

    @Autowired
    IceConfig iceConfig;

    @Autowired
    private FileService fileService;
    
    private FileServicePrx remote = null;
    
    private FileServicePrx getRemote(){
        if (remote == null) {
            CoreRemoteService<FileServicePrx> prx = new CoreRemoteService<>();
            remote = prx.getServicePrx("FileService@FileServer",
                    "--Ice.Default.Locator=IceGrid/Locator:tcp -h 192.168.13.140 -p 4061",
                    FileServicePrx.class,_FileServicePrxI.class);
//            remote = prx.getServicePrx("FileService","FileServer;120.24.238.128",FileServicePrx.class,_FileServicePrxI.class);
        }
        return remote;
    }

    @Test
    public void testClearAll() throws Exception {
        long t = System.currentTimeMillis();
        //*//本行以单斜杠开始则执行远端测试，双斜杠则执行本地测试
            fileService.clearAll(getLocalAccount(),null);
        /*///下面是远端测试代码
            getRemote().clearAll(getRemoteAccount());
        //*/
        log.info("\t>>>>>>>> testClearAll:" + (System.currentTimeMillis()-t) + "ms");
    }

    @Test
    public void testSetWebRoleStatus() throws Exception {
        fileService.setWebRoleStatus(getLocalWebRole(),"1",null);
    }

    private WebRoleDTO getLocalWebRole() throws Exception {
        return fileService.getWebRole(getLocalAccount(),getLocalFileNode(),null);
    }
    @Test
    public void testCreateAnnotate() throws Exception {
        AnnotateRequestDTO request = new AnnotateRequestDTO();
        request.setData(new byte[]{0x33,0x34,0x35});
        fileService.createAnnotateCheck(getLocalAccount(),getLocalFileNode(),request,null);
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
    public void testDeleteDir() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        CreateNodeRequestDTO dirCreate = new CreateNodeRequestDTO();
        dirCreate.setIsDirectory(true);
        dirCreate.setFullName("TaskDirForDelete");
        CreateNodeRequestDTO fileCreate = new CreateNodeRequestDTO();
        fileCreate.setIsDirectory(false);
        fileCreate.setFullName("TaskDirChildForDelete.text");
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
//                SimpleNodeDTO dirNode = fileService.createNodeForAccount(getLocalAccount(), getLocalTask(),dirCreate,null);
//                SimpleNodeDTO fileNode = fileService.createNodeForAccount(getLocalAccount(), dirNode,fileCreate,null);
                SimpleNodeDTO dirNode = getLocalNode("F271E5C3901C4C99A69611881811F6CB-1",false);
                fileService.deleteNodeForAccount(getLocalAccount(),dirNode,null);
                fileService.deleteNodeForAccount(getLocalAccount(),dirNode,null);
            } else {
                SimpleNodeDTO dirNode = getRemote().createNodeForAccount(getLocalAccount(), getLocalTask(),dirCreate);
                SimpleNodeDTO fileNode = getRemote().createNodeForAccount(getLocalAccount(), dirNode,fileCreate);
                getRemote().deleteNodeForAccount(getRemoteAccount(),dirNode,null);
                getRemote().deleteNodeForAccount(getRemoteAccount(),dirNode,null);
            }
            log.info("\t>>>>>>>> deleteLocalDir:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testDeleteFile() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFullName("TaskFileForDelete.txt");
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                SimpleNodeDTO node = fileService.createNodeForAccount(getLocalAccount(), getLocalTask(),request,null);
                fileService.deleteNodeForAccount(getLocalAccount(),node,null);
                fileService.deleteNodeForAccount(getLocalAccount(),node,null);
            } else {
                SimpleNodeDTO node = getRemote().createNodeForAccount(getRemoteAccount(), getRemoteTask(),request,null);
                getRemote().deleteNodeForAccount(getRemoteAccount(),node,null);
                getRemote().deleteNodeForAccount(getRemoteAccount(),node,null);
            }
            log.info("\t>>>>>>>> testDeleteFile:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testUpdateVersion() throws Exception {
        updateVersion();
    }

    private SimpleNodeDTO updateVersion() throws Exception {
        log.debug("\t>>>>>>>> updateVersion");
        CommitRequestDTO request = new CommitRequestDTO();
        request.setFileVersion("v2.0");
        return fileService.updateVersion(getLocalAccount(),getLocalFileNode(),getLocalFileNode(),request,null);
    }


    @Test
    public void testCreateVersion() throws Exception {
        log.debug("\t>>>>>>>> testCreateVersion");
        CommitRequestDTO request = new CommitRequestDTO();
        request.setFileVersion("v1.0");
        SimpleNodeDTO node = fileService.createVersion(getLocalAccount(),getLocalFileNode(),"a.txt",request,null);
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

    @Test
    public void testMoveFile() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("abc/xxx.txt");
        SimpleNodeDTO node;
        for (int i=0; i<1; i++) {
            long t = System.currentTimeMillis();
            if (isTestLocal) {
                node = fileService.moveNodeForAccount(getLocalAccount(), getLocalFileNode(),getLocalTask(),request,null);
            } else {
                node = getRemote().moveNodeForAccount(getRemoteAccount(), getRemoteFileNode(), null, request);
            }
            log.info("\t>>>>>>>> testMoveFile:" + (System.currentTimeMillis() - t) + "ms");
        }
    }

    private SimpleNodeDTO moveRemoteDir() throws Exception {
        log.debug("\t>>>>>>>> moveRemoteDir");
        return getRemote().moveNodeForAccount(getRemoteAccount(), getRemoteDirNode(),getRemoteTask(),getMoveDirRequest());
    }

    private SimpleNodeDTO changeRemoteDir() throws Exception {
        log.debug("\t>>>>>>>> changeRemoteDir");
        return getRemote().moveNodeForAccount(getRemoteAccount(), getRemoteDirNode(),null,getMoveDirRequest());
    }

    private SimpleNodeDTO moveDir() throws Exception {
        log.debug("\t>>>>>>>> moveDir");
        return fileService.moveNodeForAccount(getLocalAccount(), getLocalDirNode(),getLocalTask(),getMoveDirRequest(),null);
    }

    private SimpleNodeDTO changeDirName() throws Exception {
        log.debug("\t>>>>>>>> changeDirName");
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("abcde");
        return fileService.moveNodeForAccount(getLocalAccount(), getLocalDirNode(),null,request,null);
    }

    private SimpleNodeDTO changeFileName() throws Exception {
        log.debug("\t>>>>>>>> changeFileName");
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("axax.txt");
        return fileService.moveNodeForAccount(getLocalAccount(),getLocalFileNode(), getLocalDirNode(),request,null);
    }

    private SimpleNodeDTO moveFile() throws Exception {
        log.debug("\t>>>>>>>> moveFile");
        MoveNodeRequestDTO request = new MoveNodeRequestDTO();
        request.setFullName("x/y/aaaa.txt");
        return fileService.moveNodeForAccount(getLocalAccount(),getLocalFileNode(), getLocalDirNode(),request,null);
    }

    private NodeFileDTO getLocalNodeFile() throws Exception {
        SimpleNodeDTO node = getLocalFileNode();
        AskFileDTO query = new AskFileDTO();
        query.setFuzzyId(StringUtils.left(node.getId(),StringUtils.DEFAULT_ID_LENGTH));
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


    private List<CANodeDTO> listRemoteDesignNode() throws Exception {
        log.debug("\t>>>>>>>> listRemoteDesignNode");
        return getRemote().listDesignNode(getRemoteAccount());
    }

    private List<CANodeDTO> listDesignNode() throws Exception {
        log.debug("\t>>>>>>>> listDesignNode");
        return fileService.listDesignNode(getLocalAccount(),null);
    }

    @Test
    public void testListCANode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        List<CANodeDTO> list;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                list = fileService.listCANode(getRemoteAccount(), null);
            } else {
                list = getRemote().listCANode(getRemoteAccount());
            }
            log.info("\t>>>>>>>> testListCANode:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testListDesignNode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        List<CANodeDTO> list;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                list = fileService.listDesignNode(getLocalAccount(), null);
            } else {
                list = getRemote().listDesignNode(getRemoteAccount());
            }
            log.info("\t>>>>>>>> testListDesignNode:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
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

    @Test
    public void listNotRoleNode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;

        List<SimpleNodeDTO> list;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                list = fileService.listChildNodeForAccount(getLocalAccount(),getLocalTask(),null);
            } else {
                list = getRemote().listChildNodeForAccount(getRemoteAccount(),getRemoteCATask());
            }
            log.info("\t>>>>>>>> testListDesignNode:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    private SimpleNodeDTO getRemoteByPath() throws Exception {
        log.debug("\t>>>>>>>> getRemoteByPath");
        return getRemote().getNodeByPathForAccount(getRemoteAccount(),"/卯丁软件开发/设计/三月份大家一起努力吧！/新建 Microsoft Word 文档.docx");
    }

    @Test
    public void testListRemoteAll() throws Exception {
        log.debug("\t>>>>>>>> testListRemoteAll");

    }

    @Test
    public void testListChild() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;

        List<SimpleNodeDTO> list;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                list = fileService.listChildNodeForAccount(getLocalAccount(),getLocalRange(),null);

                assert(list.size() > 0);
            } else {
                list = getRemote().listChildNodeForAccount(getRemoteAccount(),getRemoteRange());

                list = getRemote().listAllNodeForAccount(getRemoteAccount());
                List<SimpleNodeDTO> dstList = new ArrayList<>();
                for (SimpleNodeDTO node : list){
                    if (StringUtils.isSame("ddb7d626b9a1403893b8e39347478453-1",node.getPid())){
                        dstList.add(node);
                    }
                }
                assert(dstList.size() > 0);
            }
            log.info("\t>>>>>>>> testListChild:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    private List<SimpleNodeDTO> listRemoteRoot() throws Exception {
        log.debug("\t>>>>>>>> listRemoteRoot");
        return getRemote().listRootNodeForAccount(getRemoteAccount());
    }

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

    @Test
    public void testListAllNode() throws Exception {
        boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;

        List<SimpleNodeDTO> list;
        long t = System.currentTimeMillis();
        if (isTestLocal) {
            list = fileService.listAllNodeForAccount(getRemoteAccount(),null);
        } else {
            list = getRemote().listAllNodeForAccount(getRemoteAccount());
        }
        log.info("\t>>>>>>>> testListAllNode:" + (System.currentTimeMillis()-t) + "ms");
    }

    private AccountDTO getRemoteAccount(){
        return getAccount("07649b3d23094f28bfce78930bf4d4ac");
    }

    private AccountDTO getLocalAccount(){
//        return getAccount("d437448683314cad91dc30b68879901d");
        return getAccount("07649b3d23094f28bfce78930bf4d4ac");
    }

    private AccountDTO getAccount(String id){
        AccountDTO account = new AccountDTO();
        account.setId(id);
        if ("d437448683314cad91dc30b68879901d".equals(id)){
            account.setName("张成亮");
        }
        return account;
    }

    private SimpleNodeDTO getLocalNode(String id,boolean isFuzzy) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        if (isFuzzy) {
            query.setFuzzyId(StringUtils.left(id, StringUtils.DEFAULT_ID_LENGTH));
        } else {
            query.setId(id);
        }
        return getNode(query,true);
    }

    private SimpleNodeDTO getRemoteNode(String id,boolean isFuzzy) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        if (isFuzzy) {
            query.setFuzzyId(StringUtils.left(id, StringUtils.DEFAULT_ID_LENGTH));
        } else {
            query.setId(id);
        }
        return getNode(query,false);
    }

    private SimpleNodeDTO getNode(QueryNodeDTO query,boolean isLocal) throws Exception {
        List<SimpleNodeDTO> list;
        if (isLocal) {
            list = fileService.listNodeForAccount(getLocalAccount(), query, null);
        } else {
            list = getRemote().listNodeForAccount(getLocalAccount(), query);
        }
        return list.get(0);
    }

    private SimpleNodeDTO getLocalDirNode() throws Exception {
        return getLocalNode("2D4DEF1EDD21401D95848A1A1ED3910C-3",false);
    }

    private SimpleNodeDTO getLocalTaskParent() throws Exception {
        return getLocalNode("552f9b02dc2d4e9c9ee9a9ce402b7e07-1",false);
    }

    private SimpleNodeDTO getLocalTask() throws Exception {
        return getNode("cf8a3c262709479d97a364cf2ccf1569-1",true,false);
    }

    private SimpleNodeDTO getRemoteTask() throws Exception {
        return getNode("728dc784ed4145628ee4a5f85ee811a8-1",false,false);
    }

    private SimpleNodeDTO getRemoteRange() throws Exception {
        return getNode("ddb7d626b9a1403893b8e39347478453-1",false,false);
    }

    private SimpleNodeDTO getLocalRange() throws Exception {
        return getNode("ddb7d626b9a1403893b8e39347478453-1",true,false);
    }

    private SimpleNodeDTO getRemoteCATask() throws Exception {
        return getNode("2963f576c6dc46bba0624153ce53c308-2",false,false);
    }

    private SimpleNodeDTO getRemoteDirNode() throws Exception {
        return getNode("3D044277618A472C9F2DCF81E62194D9-1",false,false);
    }

    private SimpleNodeDTO getRemoteFileNode() throws Exception {
        return getNode("0417A569A3B04C9EAF02191A355A071D-1",false,false);
    }

    private SimpleNodeDTO getLocalFileNode() throws Exception {
        return getLocalNode("4A5B0B122331489B98BFE847B9B788AC-1",false);
    }

    private List<SimpleNodeDTO> getLocalFileNodeList() throws Exception {
        List<SimpleNodeDTO> nodeList = new ArrayList<>();
        nodeList.add(getLocalNode("4A5B0B122331489B98BFE847B9B788AC-1",true));
        return nodeList;
    }

    private List<SimpleNodeDTO> getRemoteFileNodeList() throws Exception {
        List<SimpleNodeDTO> nodeList = new ArrayList<>();
        nodeList.add(getRemoteNode("2544BB1F7CA34A9DB134F3EA50796D33-1",true));
        return nodeList;
    }


    private SimpleNodeDTO getRemoteNode() throws Exception {
        return getRemoteNode("4EC9E8A5DCED400A83D4F09C8DD7F6CB-1");
    }

    private SimpleNodeDTO getRemoteNode(String id) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> list = getRemote().listNodeForAccount(getRemoteAccount(),query);
        return list.get(0);
    }

    private FullNodeDTO getLocalFullNode() throws Exception {
        return fileService.getFullNodeForAccount(getLocalAccount(),getLocalFileNode(),null);
    }

    @Test
    public void testReadNode() throws Exception {
        FileDataDTO data;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++) {
            /*//本行以单斜杠开始则执行远端测试，双斜杠则执行本地测试
                data = fileService.readNodeForAccount(getLocalAccount(),getLocalFileNode(),0,0,null);
                log.info("\t>>>>>>>> testReadNode:" + (System.currentTimeMillis()-t) + "ms");t = System.currentTimeMillis();
            /*///下面是远端测试代码
                data = getRemote().readNodeForAccount(getRemoteAccount(), getRemoteFileNode(), 0, 0);
                log.info("\t>>>>>>>> testReadNode:" + (System.currentTimeMillis() - t) + "ms");t = System.currentTimeMillis();
            //*/
        }
    }

    private FileDataDTO readFile() throws Exception {
        log.debug("\t>>>>>>>> readFile");
        return fileService.readFile(getLocalAccount(),getLocalNodeFile(),0,0,null);
    }


    @Test
    public void testLogin() throws Exception {
        boolean b;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++) {
            /*//本行以单斜杠开始则执行远端测试，双斜杠则执行本地测试
                b = fileService.login(getLocalAccount(),getLocalFileNode(),0,0,null);
                log.info("\t>>>>>>>> testLogin:" + (System.currentTimeMillis()-t) + "ms");t = System.currentTimeMillis();
            /*///下面是远端测试代码
                b = getRemote().login(getRemoteLoginInfo());
                log.info("\t>>>>>>>> testLogin:" + (System.currentTimeMillis() - t) + "ms");t = System.currentTimeMillis();
            //*/
        }
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
    public void testAuditCANode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                CANodeDTO caNode = getLocalCANode();
                caNode.setIsPassAudit(!caNode.getIsPassAudit());
                fileService.auditNodeRequestForAccount(getLocalAccount(), caNode,null);
            } else {
                CANodeDTO caNode = getRemoteCANode();
                caNode.setIsPassAudit(!caNode.getIsPassAudit());
                getRemote().askCANodeRequestForAccount(getRemoteAccount(), caNode);
            }
            log.info("\t>>>>>>>> testAuditCANode:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testAskCA() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        SimpleNodeDTO node;
        for (int i=0; i<2; i++){
            long t = System.currentTimeMillis();
            if (isTestLocal) {
                node = fileService.askCANodeRequestForAccount(getLocalAccount(), getLocalDesignNode(),null);
            } else {
                node = getRemote().askCANodeRequestForAccount(getRemoteAccount(), getRemoteDesignNode());
            }
            log.info("\t>>>>>>>> testAskCA:" + (System.currentTimeMillis()-t) + "ms");
        }
    }

    private SimpleNodeDTO askLocalCA() throws Exception {
        return fileService.askCANodeRequestForAccount(getLocalAccount(), getLocalDesignNode(),null);
    }

    private CANodeDTO getLocalCANode() throws Exception {
        return getCANode("F6CD4843A3F94ADBB6CA10F0965890F8",true);
    }

    private CANodeDTO getRemoteCANode() throws Exception {
        return getCANode("4A5B0B122331489B98BFE847B9B788AC",false);
    }

    private CANodeDTO getLocalDesignNode() throws Exception {
        return getDesignNode("4A5B0B122331489B98BFE847B9B788AC",true);
    }

    private List<CANodeDTO> getLocalDesignNodeList() throws Exception {
        List<CANodeDTO> list = new ArrayList<>();
        list.add(getDesignNode("4A5B0B122331489B98BFE847B9B788AC",true));
        return list;
    }

    private List<CANodeDTO> getRemoteDesignNodeList() throws Exception {
        List<CANodeDTO> list = new ArrayList<>();
        list.add(getDesignNode("DA82F7D2555C442797E298001379C0EE",false));
        return list;
    }

    private CANodeDTO getRemoteDesignNode() throws Exception {
        List<CANodeDTO> list = getRemote().listDesignNode(getRemoteAccount());
        return getDesignNode("DA82F7D2555C442797E298001379C0EE",false);
    }

    private CANodeDTO getDesignNode(String id,boolean isLocal) throws Exception {
        return getCANode(id,true,isLocal);
    }

    private CANodeDTO getCANode(String id,boolean isLocal) throws Exception {
        return getCANode(id,false,isLocal);
    }

    private CANodeDTO getCANode(String id,boolean isDesign,boolean isLocal) throws Exception {
        List<CANodeDTO> list;
        if (isLocal){
            if (isDesign){
                list = fileService.listDesignNode(getLocalAccount(),null);
            } else {
                list = fileService.listCANode(getLocalAccount(),null);
            }
        } else {
            if (isDesign){
                list = getRemote().listDesignNode(getRemoteAccount());
            } else {
                list = getRemote().listCANode(getRemoteAccount());
            }
        }
        CANodeDTO caNode = null;
        for (CANodeDTO ca : list){
            if (StringUtils.isSame(StringUtils.left(id,StringUtils.DEFAULT_ID_LENGTH),ca.getId())){
                caNode = ca;
                break;
            }
        }
        if (!isValid(caNode)){
            log.error("没找到CANode");
        }
        return caNode;
    }

    private boolean isValid(CANodeDTO ca){
        return (ca != null) && (StringUtils.isNotEmpty(ca.getId()));
    }


    @Test
    public void testSummaryFile() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        QuerySummarySimpleDTO query = new QuerySummarySimpleDTO();

        SummaryFileDTO result;
        if (isTestLocal) {
            query.setAccountId(getLocalAccount().getId());
            result = fileService.summaryFile(query,null);
        } else {
            query.setAccountId(getRemoteAccount().getId());
            result = getRemote().summaryFile(query,null);
        }
    }

    @Test
    public void testIssueNode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;

        CommitRequestDTO request = new CommitRequestDTO();
        request.setOwnerUserId("123");
        request.setMajorName("建筑");
        request.setFileVersion("v3.0");
        SimpleNodeDTO node;
        long t = System.currentTimeMillis();
        if (isTestLocal) {
            request.setPid(getRemoteSkyParent().getId());
            node = fileService.issueNodeForAccount(getLocalAccount(),getLocalFileNode(),request,null);
        } else {
            request.setPid(getRemoteSkyParent().getId());
            node = getRemote().issueNodeForAccount(getRemoteAccount(),getRemoteFileNode(),request,null);
        }
        log.info("\t>>>>>>>> testIssueNode:" + (System.currentTimeMillis()-t) + "ms");
    }

    private SimpleNodeDTO getLocalSkyParent() throws Exception {
        return getSkyNode("29f323c5072d4341a21bae6927cd3b46",true);
    }

    private SimpleNodeDTO getRemoteSkyParent() throws Exception {
        return getSkyNode("29f323c5072d4341a21bae6927cd3b46",false);
    }

    private SimpleNodeDTO getSkyNode(String id, boolean isLocal) throws Exception {
        SimpleNodeDTO node = null;
        List<SimpleNodeDTO> list;
        if (isLocal) {
            list = fileService.listWebArchiveDirForAccount(getLocalAccount(),null,null);
        } else {
            list = getRemote().listWebArchiveDirForAccount(getRemoteAccount(),null,null);
        }
        for (SimpleNodeDTO n : list){
            if (StringUtils.isSame(id,n.getId())){
                node = n;
                break;
            }
        }
        return node;
    }

    @Test
    public void checkNode() throws Exception {
        SimpleNodeDTO node;
        CANodeDTO caNode = getLocalDesignNode();
        caNode.setIsPassCheck(!caNode.getIsPassCheck());
        node = fileService.checkNodeRequestForAccount(getLocalAccount(),caNode,null);
    }

    @Test
    public void testCommitNode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        CommitRequestDTO request = new CommitRequestDTO();
        request.setOwnerUserId("123");
        request.setMajorName("建筑");
        request.setFileVersion("v3.0");
        SimpleNodeDTO node;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                node = fileService.commitNodeForAccount(getLocalAccount(), getLocalFileNode(), request, null);
            } else {
                node = getRemote().commitNodeForAccount(getRemoteAccount(), getRemoteFileNode(), request);
            }
            log.info("\t>>>>>>>> testReadNode:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testCommitCANodeList() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        CommitListResultDTO result;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                result = fileService.requestCommitListForAccount(getLocalAccount(),getLocalDesignNodeList(), null);
            } else {
                result = getRemote().requestCommitListForAccount(getRemoteAccount(), getRemoteDesignNodeList());
            }
            log.info("\t>>>>>>>> testCommitCANodeList:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testCommitList() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        CommitRequestDTO request = new CommitRequestDTO();
        request.setOwnerUserId("123");
        request.setMajorName("建筑");
        request.setFileVersion("v3.0");
        CommitListResultDTO result;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            if (isTestLocal) {
                result = fileService.commitNodeListForAccount(getLocalAccount(), getLocalFileNodeList(), request, null);
            } else {
                result = getRemote().commitNodeListForAccount(getRemoteAccount(), getRemoteFileNodeList(), request);
            }
            log.info("\t>>>>>>>> testCommitList:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testGetWebRole() throws Exception {
        getWebRole();
    }

    private WebRoleDTO getWebRole() throws Exception {
        log.debug("\t>>>>>>>> getWebRole");
        return  fileService.getWebRole(getLocalAccount(),getLocalFileNode(),null);
    }


//    @Ignore
    @Test
    public void testReleaseNodeWithLength() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;
        final int FILE_LENGTH = 300;
        long t = System.currentTimeMillis();
        if (isTestLocal) {
            fileService.releaseNodeForAccount(getLocalAccount(), getLocalFileNode(), FILE_LENGTH, null);
        } else {
            getRemote().releaseNodeForAccount(getRemoteAccount(), getRemoteFileNode(), FILE_LENGTH,null);
        }
        log.info("\t>>>>>>>> testReleaseNodeWithLength:" + (System.currentTimeMillis()-t) + "ms");
    }

    @Test
    public void testSetNodeLength() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;
        final int FILE_LENGTH = 150;
        for (int i=0; i<3; i++) {
            long t = System.currentTimeMillis();
            if (isTestLocal) {
                fileService.setNodeLengthForAccount(getLocalAccount(), getLocalFileNode(), FILE_LENGTH + i, null);
            } else {
                getRemote().setNodeLengthForAccount(getRemoteAccount(), getRemoteFileNode(), FILE_LENGTH + i, null);
            }
            log.info("\t>>>>>>>> testSetNodeLength:" + (System.currentTimeMillis() - t) + "ms");
        }
    }

    @Test
    public void testReleaseNode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++) {
            if (isTestLocal) {
                fileService.releaseNodeForAccount(getLocalAccount(), getLocalFileNode(), 0, null);
            } else {
                getRemote().releaseNodeForAccount(getRemoteAccount(), getRemoteFileNode(), 0);
            }
            log.info("\t>>>>>>>> testReleaseNode:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testReleaseFile() throws Exception {
        log.debug("\t>>>>>>>> testReleaseFile");
        fileService.releaseFile(getLocalAccount(),getLocalNodeFile(),"a/b/c.txt",null);
    }

    @Test
    public void testReloadNode() throws Exception {
        log.debug("\t>>>>>>>> testReloadNode");
        long t = System.currentTimeMillis();
        fileService.reloadNodeForAccount(getLocalAccount(),getLocalFileNode(),null);
        log.info("\t>>>>>>>> testReloadNode:" + (System.currentTimeMillis()-t) + "ms");t = System.currentTimeMillis();
    }

    @Test
    public void testWriteNode() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        int size = 11;
        long pos = 0;
        for (int times=0; times<10; times++){
            long t = System.currentTimeMillis();
            FileDataDTO fileData = createFileData(pos, size);
            if (isTestLocal) {
                pos += fileService.writeNodeForAccount(getLocalAccount(), getLocalFileNode(), fileData, null);
            } else {
                pos += getRemote().writeNodeForAccount(getLocalAccount(), getRemoteFileNode(), fileData);
            }
            log.info("\t>>>>>>>> testWriteNode:" + (System.currentTimeMillis()-t) + "ms");
        }
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

    private FileDataDTO createFileData(long pos, int size) throws Exception {
        //建立上传内容
        byte[] bytes = new byte[size];
        char ch = '0';
        for (int i=0; i<size; i++){
            if (('0' <= ch) && (ch <= '9')) {
                bytes[i] = (byte) ch++;
            } else {
                bytes[i] = (byte) '\r';
                ch = '0';
            }
        }
        FileDataDTO fileData = new FileDataDTO();
        fileData.setPos(pos);
        fileData.setSize(size);
        fileData.setData(bytes);

        return fileData;
    }


    private CreateNodeRequestDTO getCreateFileRequest() throws Exception{
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFullName("aaaa.txt");
        return request;
    }

    @Test
    public void testCreateTaskFile() throws Exception{
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(false);
        request.setFullName("small.txt");
        SimpleNodeDTO node;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++) {
            if (isTestLocal) {
                node = fileService.createNodeForAccount(getLocalAccount(), getLocalTask(), request, null);
            } else {
                node = getRemote().createNodeForAccount(getRemoteAccount(), getRemoteTask(), request);
            }
            log.info("\t>>>>>>>> testCreateTaskFile:" + (System.currentTimeMillis() - t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testCreateTaskDir() throws Exception{
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : false;
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(true);
        request.setFullName("TaskDir");
        SimpleNodeDTO node;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++) {
            if (isTestLocal) {
                node = fileService.createNodeForAccount(getRemoteAccount(), getRemoteTask(), request, null);
            } else {
                node = getRemote().createNodeForAccount(getRemoteAccount(), getRemoteTask(), request);
            }
            log.info("\t>>>>>>>> testCreateTaskFile:" + (System.currentTimeMillis() - t) + "ms");
            t = System.currentTimeMillis();
        }
    }

    @Test
    public void testCreateStorageFile() throws Exception{
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setFullName("StorageFile.txt");
        request.setIsDirectory(false);
        request.setFileLength(100);
        SimpleNodeDTO node;
        long t = System.currentTimeMillis();
        for (int i=0; i<1; i++){
            /*//本行以单斜杠开始则执行远端测试，双斜杠则执行本地测试
                node = fileService.createNodeForAccount(getLocalAccount(), getLocalDirNode(),request,null);
            /*///下面是远端测试代码
                node = getRemote().createNodeForAccount(getRemoteAccount(), getRemoteDirNode(),request);
            //*/
            log.info("\t>>>>>>>> testCreateStorageFile:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
        }
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

    private SimpleNodeDTO getNode(String id,boolean isLocal,boolean isFuzzy) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        if (isFuzzy) {
            query.setFuzzyId(StringUtils.left(id, StringUtils.DEFAULT_ID_LENGTH));
        } else {
            query.setId(id);
        }
        List<SimpleNodeDTO> list = (isLocal) ? fileService.listNodeForAccount(getLocalAccount(),query,null) :
                getRemote().listNodeForAccount(getLocalAccount(),query);
        return list.get(0);
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
        return fileService.createNodeForAccount(getLocalAccount(), getLocalDirNode(),request,null);
    }

    @Test
    public void testCreateLocalDirectory() throws Exception{
        log.debug("\t>>>>>>>> testCreateLocalDirectory");
        CreateNodeRequestDTO request = new CreateNodeRequestDTO();
        request.setIsDirectory(true);
        request.setFullName("father/child/child3");
        SimpleNodeDTO node;
        node = fileService.createNodeForAccount(getLocalAccount(), getLocalTask(),request,null);
    }

    @Test
    public void testGetNodeInfo() throws Exception {
        log.debug("\t>>>>>>>> testGetNodeInfo");
        QueryNodeInfoDTO query = new QueryNodeInfoDTO();
        QueryNodeInfoFileDTO fileQuery = new QueryNodeInfoFileDTO();
        query.setFileQuery(fileQuery);
        FullNodeDTO fullNode;
        fullNode = fileService.getNodeInfoForAccount(getLocalAccount(),getLocalFileNode(),query,null);
    }
}
