package com.maoding.Storage;

import com.maoding.Base.BaseRemoteService;
import com.maoding.Common.ConstService;
import com.maoding.Common.zeroc.DeleteAskDTO;
import com.maoding.Common.zeroc.QueryAskDTO;
import com.maoding.CoreUtils.StringUtils;
import com.maoding.Storage.zeroc.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
* StorageServiceImpl Tester.
*
* @author Zhangchengliang
* @since 01/25/2018
* @version 1.0
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class StorageServiceImplTest {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private StorageService storageService;

    private StorageServicePrx remote = null;

    private StorageServicePrx getRemote(){
        if (remote == null) {
            BaseRemoteService<StorageServicePrx> prx = new BaseRemoteService<>();
            remote = prx.getServicePrx("StorageService","192.168.13.140",StorageServicePrx.class,_StorageServicePrxI.class);
        }
        return remote;
    }

    @Test
    public void testListNodeFile() throws Exception {
//        listNodeFileById();
//        listNodeFileByServer();
        listNodeFileByKey();
    }

    private List<NodeFileDTO> listNodeFileByKey() throws Exception {
        QueryNodeFileDTO query = new QueryNodeFileDTO();
        query.setKey("c_3.txt");
        return storageService.listNodeFile(query,null);
    }

    private List<NodeFileDTO> listNodeFileByServer() throws Exception {
        QueryNodeFileDTO query = new QueryNodeFileDTO();
        query.setId("02419B839D9546DEB07C03DBECAF10A8");
        query.setServerTypeId("1");
        query.setServerAddress("127.0.0.1");
        return storageService.listNodeFile(query,null);
    }

    private List<NodeFileDTO> listNodeFileById() throws Exception {
        QueryNodeFileDTO query = new QueryNodeFileDTO();
        query.setId("02419B839D9546DEB07C03DBECAF10A8");
        return storageService.listNodeFile(query,null);
    }

    @Test
    public void testCreateElement() throws Exception {
        UpdateElementDTO request = new UpdateElementDTO();
        request.setDataArray(new byte[]{1,2,3});
        storageService.createEmbedElement(request,null);
    }

    @Test
    public void testUpdateElement() throws Exception {
        UpdateElementDTO request = new UpdateElementDTO();
        request.setDataArray(new byte[]{0x37,0x35,0x36});
        storageService.updateEmbedElement(getLocalElement(),request,null);
    }

    @Test
    public void testUpdateAnnotate() throws Exception {
//        updateAnnotateSimple();
//        updateAnnotateWithDeleteElement();
        updateAnnotateWithAddAndDeleteAttachment();
    }

    private AnnotateDTO updateAnnotateWithAddAndDeleteAttachment() throws Exception {
        log.debug("\t>>>>>>>> updateAnnotateWithAddAndDeleteElement");
        NodeFileDTO file = getLocalFile();
        List<String> addFileIdList = new ArrayList<>();
        addFileIdList.add(file.getId());
        UpdateAnnotateDTO request = new UpdateAnnotateDTO();
        request.setContent("添加删除元素变动");
        request.setDelAttachmentIdList(getLocalDeleteAttachmentIdList());
        request.setAddFileIdList(addFileIdList);
        return storageService.updateAnnotate(getLocalAnnotate(),request,null);
    }

    private AnnotateDTO updateAnnotateWithDeleteElement() throws Exception {
        log.debug("\t>>>>>>>> updateAnnotateWithDeleteElement");
        UpdateAnnotateDTO request = new UpdateAnnotateDTO();
        request.setContent("删除元素变动");
        request.setDelAttachmentIdList(getLocalDeleteAttachmentIdList());
        return storageService.updateAnnotate(getLocalAnnotate(),request,null);
    }

    private AnnotateDTO updateAnnotateSimple() throws Exception {
        log.debug("\t>>>>>>>> updateAnnotateSimple");
        UpdateAnnotateDTO request = new UpdateAnnotateDTO();
        request.setContent("这是一个测试变动");
        return storageService.updateAnnotate(getLocalAnnotate(),request,null);
    }

    private List<String> getLocalDeleteAttachmentIdList() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("846CB27685A44194957B694814587495");
        return list;
    }

    @Test
    public void testListAnnotate() throws Exception {
        listAnnotateByFileId();
//        listAnnotateByAnyFileId();
    }

    private List<AnnotateDTO> listAnnotateByAnyFileId() throws Exception {
        QueryAnnotateDTO query = new QueryAnnotateDTO();
        query.setAnyFileId("51A4D4354EB34A96B7B078ECEBC9C6C7");
        List<AnnotateDTO> list = storageService.listAnnotate(query,null);
        return list;
    }

    private List<AnnotateDTO> listAnnotateByFileId() throws Exception {
        QueryAnnotateDTO query = new QueryAnnotateDTO();
        query.setFileId("02419B839D9546DEB07C03DBECAF10A8");
        List<AnnotateDTO> list = storageService.listAnnotate(query,null);
        return list;
    }

    private AnnotateDTO getLocalAnnotate() throws Exception {
        QueryAnnotateDTO query = new QueryAnnotateDTO();
        query.setId("6C117810B46C44D5A14F6C108C118DEA");
        List<AnnotateDTO> list = storageService.listAnnotate(query,null);
        return list.get(0);
    }

    @Test
    public void testCreateAnnotate() throws Exception {
//        createAnnotateSimple();
        createAnnotateWithElement();
    }

    private AnnotateDTO createAnnotateWithElement() throws Exception {
        log.debug("\t>>>>>>>> createAnnotateWithElement");
        EmbedElementDTO element = getLocalElement();
        List<String> addElementIdList = new ArrayList<>();
        addElementIdList.add(element.getId());
        UpdateAnnotateDTO createAnnotateRequest = new UpdateAnnotateDTO();
        createAnnotateRequest.setContent("添加嵌入元素注解");
        createAnnotateRequest.setAddElementIdList(addElementIdList);
        return storageService.createAnnotate(getLocalFile(),createAnnotateRequest,null);
    }

    private AnnotateDTO createAnnotateSimple() throws Exception {
        log.debug("\t>>>>>>>> createAnnotateSimple");
        UpdateAnnotateDTO request = new UpdateAnnotateDTO();
        request.setContent("这是一个测试");
        return storageService.createAnnotate(getLocalNodeFile(),request,null);
    }

    private EmbedElementDTO getLocalElement() throws Exception {
        QueryAskDTO query = new QueryAskDTO();
        query.setId("19632ADACDF54D2AAA178F0D8DAB6359");
        List<EmbedElementDTO> list = storageService.listEmbedElement(query,null);
        return list.get(0);
    }

    @Test
    public void testCreateNodeFile() throws Exception {
        createNodeFileNoMirror();
        createNodeFileWithMirror();
    }

    private NodeFileDTO createNodeFileWithMirror() throws Exception {
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setServerTypeId("2");
        request.setServerAddress("127.0.0.1");
        request.setBaseDir("c:/work/file_server");
        request.setReadOnlyKey("a/b/c.txt");
        request.setWritableKey("a/b/c_2.txt");
        request.setMirrorTypeId("1");
        request.setMirrorAddress("127.0.0.1");
        request.setMirrorBaseDir("c:/work/file_server/.mirror");
        request.setReadOnlyMirrorKey("c_3.txt");
        request.setWritableMirrorKey("c_4.txt");
        return storageService.createNodeFileWithRequestOnly(request,null);
    }

    private NodeFileDTO createNodeFileNoMirror() throws Exception {
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setServerTypeId("2");
        request.setServerAddress("127.0.0.1");
        request.setBaseDir("c:/work/file_server");
        request.setReadOnlyKey("a/b/c.txt");
        request.setWritableKey("a/b/c_2.txt");
        request.setFileTypeId("2");
        return storageService.createNodeFileWithRequestOnly(request,null);
    }

    @Test
    public void testSummary() throws Exception {
        QuerySummaryDTO query = new QuerySummaryDTO();
        query.setCompanyId("5aeb14ea46dd4282b136736976d4e430");
        long size = storageService.summaryNodeLength(query,null);
    }

    @Test
    public void testDelete() throws Exception{
        deleteByNode();
        deleteById();
    }

    private void deleteById() throws Exception {
        log.debug("\t>>>>>>>> deleteById");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("tttt");
        SimpleNodeDTO node = storageService.createNode(getLocalPath(),request,null);
        storageService.deleteNodeById(node.getId(),getLocalDeleteAsk(),null);
    }

    private void deleteByNode() throws Exception {
        log.debug("\t>>>>>>>> deleteByNode");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("tttt");
        SimpleNodeDTO node = storageService.createNode(getLocalPath(),request,null);
        storageService.deleteNode(node,getLocalDeleteAsk(),null);
    }

    private DeleteAskDTO getLocalDeleteAsk(){
        DeleteAskDTO ask = new DeleteAskDTO();
        ask.setLastModifyUserId("41d244733ec54f09a255836637f2b21d");
        return ask;
    }

    @Test
    public void testGetNodeInfo() throws Exception {
        SimpleNodeDTO node = getLocalNode();
        QueryNodeInfoDTO query = new QueryNodeInfoDTO();
        QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
        query.setTextQuery(txtQuery);
        QueryNodeInfoFileDTO fileQuery = new QueryNodeInfoFileDTO();
        query.setFileQuery(fileQuery);
        QueryNodeInfoHistoryDTO hisQuery = new QueryNodeInfoHistoryDTO();
        query.setHistoryQuery(hisQuery);
        FullNodeDTO nodeInfo = storageService.getNodeInfo(node,query,null);
        assert (nodeInfo != null);
    }

    private SimpleNodeDTO getLocalPath() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("0D3BC52C785541269CDC1CFE6D837768-1");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        return ((list != null) && !(list.isEmpty())) ? list.get(0) : null;
    }

    private SimpleNodeDTO getLocalNode() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("02419B839D9546DEB07C03DBECAF10A8-1");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        return ((list != null) && !(list.isEmpty())) ? list.get(0) : null;
    }

    private FullNodeDTO getLocalFullNode() throws Exception {
        SimpleNodeDTO node = getLocalNode();
        QueryNodeInfoDTO query = new QueryNodeInfoDTO();
        QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
        query.setTextQuery(txtQuery);
        QueryNodeInfoFileDTO fileQuery = new QueryNodeInfoFileDTO();
        query.setFileQuery(fileQuery);
        return storageService.getNodeInfo(node,query,null);
    }

    private NodeFileDTO getLocalFile() throws Exception {
        QueryNodeFileDTO query = new QueryNodeFileDTO();
        query.setId("51A4D4354EB34A96B7B078ECEBC9C6C7");
        List<NodeFileDTO> list = storageService.listNodeFile(query,null);
        return list.get(0);
    }
    private NodeFileDTO getLocalNodeFile() throws Exception {
        FullNodeDTO fileNode = getLocalFullNode();
        NodeFileDTO fileInfo = fileNode.getFileInfo();
        return fileInfo;
    }

    @Test
    public void testListNode() throws Exception {
//        listNodeById();
//        listNodeByIdString();
//        listNodeByPath();
//        listOldNodeByFuzzyPath();
//        listOldNodeByFuzzyId();
//        listOldNodeByTypeId();
        listChild("d1ba184a668d49789a224e8e8200fb17-1");
    }

    private List<SimpleNodeDTO> listChild(String pid) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(pid);
        query.setAccountId("13221450717");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        return list;
    }

    private void listOldNodeByTypeId() throws Exception{
        QueryNodeDTO query = new QueryNodeDTO();
        query.setProjectId("39eb3f90b5dd4e5eb15225111a37aa71");
        query.setTypeId("42");
        List<SimpleNodeDTO> list = storageService.listOldNode(query,null);
    }

    private void listOldNodeByFuzzyId() throws Exception{
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyId("9f8d3fa624c34fa1825d47a59301fc11,b06145eb3879409a8bd40722f07e0385,aaa,bbb");
        List<SimpleNodeDTO> list = storageService.listOldNode(query,null);
    }

    private void listOldNodeByFuzzyPath() throws Exception{
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath("/华纳设计研究院/发布/设计成果/项目前期/项目前期 - 提交甲方报审/cc.txt");
        List<SimpleNodeDTO> list = storageService.listOldNode(query,null);
    }

    private void listNodeByPath() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath("/dd新项目/设计");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        query = new QueryNodeDTO();
        query.setFuzzyPath("/73立项/提资/项目前期/aaaa/bbbb/cc.txt");
        list = storageService.listNode(query,null);
        query = new QueryNodeDTO();
        query.setParentPath("");
        query.setAccountId("41d244733ec54f09a255836637f2b21d");
        list = storageService.listNode(query,null);
    }

    private void listNodeById() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("C6AC5795406745BAB796990BB889C0E7-1");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        query = new QueryNodeDTO();
        query.setAccountId("41d244733ec54f09a255836637f2b21d");
        query.setProjectId("c8c049f763d245b5aa9850c43166245e");
        list = storageService.listNode(query,null);
        query = new QueryNodeDTO();
        query.setFuzzyId("BB6992A070FF4C5DA270122FC6668E1F");
        list = storageService.listNode(query,null);
    }

    private void listNodeByIdString() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("C6AC5795406745BAB796990BB889C0E7-1,E9AA0F7A81444B8D932848E61CDAFFC3-1");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        query = new QueryNodeDTO();
        query.setFuzzyId("C6AC5795406745BAB796990BB889C0E7,E9AA0F7A81444B8D932848E61CDAFFC3");
        list = storageService.listNode(query,null);
    }

    @Test
    public void testCreateNode() throws Exception {
//        createSimpleDir();
//        createPathDir();
//        createAbsoluteDir();
//        createPathFile();
//        createMirrorFile();
        createHisFile();
    }

    private void createHisFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("hisaccount");
        request.setLastModifyRoleId("hisaccountRoleId");
        request.setOwnerUserId("3333");
        request.setPath("abcde.txt");
        request.setServerTypeId((short)1);
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("/x/y/abcde.txt");
        request.setWritableKey("/x/y/aaaa.txt");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_FILE_COMMIT);
        request.setFileLength(50L);
        request.setRemark("这是a 历史记录");
        SimpleNodeDTO dto = storageService.createNode(getLocalPath(),request,null);
    }

    private void createMirrorFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setOwnerUserId("3333");
        request.setPath("abcde.txt");
        request.setServerTypeId((short)1);
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("/x/y/abcde.txt");
        request.setWritableKey("/x/y/aaaa.txt");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_FILE_COMMIT);
        request.setFileLength(50L);
        request.setMirrorBaseDir("c:/work/file_server");
        request.setReadOnlyMirrorKey("mirror.txt");
        SimpleNodeDTO dto = storageService.createNode(getLocalPath(),request,null);
    }

    private void createPathFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setLastModifyUserId("11111");
        request.setLastModifyRoleId("11111");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_UNKNOWN);
        request.setPath("abcde.txt");
        request.setServerTypeId((short)1);
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("/x/y/abcde.txt");
        request.setWritableKey("/x/y/aaaa.txt");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_FILE_COMMIT);
        request.setMainFileId("xxx");
        request.setFileLength(10L);
        request.setMajorTypeId("1222");
        request.setFileVersion("v1.0");
        SimpleNodeDTO dto = storageService.createNode(getLocalPath(),request,null);
    }

    private void createAbsoluteDir() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN);
        request.setPath("/1/2/3/abcde");
        SimpleNodeDTO dto = storageService.createNode(null,request,null);
    }

    private void createPathDir() throws Exception {
        SimpleNodeDTO parent = getLocalPath();
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN);
        request.setPath("/1/2/3/abcde");
        request.setLastModifyUserId("111");
        request.setLastModifyRoleId("111");
        request.setOwnerUserId("222");
        request.setLastModifyUserId("222");
        SimpleNodeDTO dto = storageService.createNode(parent,request,null);
    }

    private void createSimpleDir() throws Exception {
        SimpleNodeDTO parent = getLocalPath();
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN);
        request.setPath("abcde");
        SimpleNodeDTO dto = storageService.createNode(parent,request,null);
    }

    @Test
    public void testUpdateNodeFile() throws Exception {
//        updateNodeFileSimple();
        updateNodeFileWithMirror();
    }

    private NodeFileDTO updateNodeFileWithMirror() throws Exception {
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setMainFileId(StringUtils.left(getLocalNode().getId(),32));
        request.setMirrorTypeId("1");
        request.setMirrorAddress("127.0.0.1");
        request.setBaseDir("c:/work/file_server");
        request.setWritableMirrorKey(".mirror/abc.txt");
        NodeFileDTO file = storageService.updateNodeFile(getLocalNodeFile(),request,null);
        return file;
    }

    private NodeFileDTO updateNodeFileSimple() throws Exception {
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        NodeFileDTO file = storageService.updateNodeFile(getLocalNodeFile(),request,null);
        return file;
    }


    @Test
    public void testUpdateNode() throws Exception {
        updateNodeRenameFile();
        updateNodeMoveFileByPath();
        updateNodeMoveFileByPid();
        updateNodeRenamePath();
        updateNodeMovePath();
        updateNodeTreePty();
        updateNodeFilePty();
        updateNodeHisPty();
        updateNodeMirrorPty();
    }

    private void updateNodeMirrorPty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setMirrorTypeId((short)1);
        request.setMirrorAddress("127.0.0.1");
        request.setMirrorBaseDir("c:/work/.mirror");
        request.setReadOnlyMirrorKey("readonlymirror");
        request.setWritableMirrorKey("writableMirror");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),null,request,null);
        request.setLastModifyUserId("41d244733ec54f09a255836637f2b21d");
        request.setLastModifyRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setMirrorTypeId((short)1);
        request.setMirrorAddress("127.0.0.1");
        request.setMirrorBaseDir("c:/work/.mirror");
        request.setReadOnlyMirrorKey("aaaa.txt");
        request.setWritableMirrorKey("bbbb.txt");
        storageService.updateNode(file,null,request,null);
    }

    private void updateNodeHisPty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setActionTypeId((short)2);
        request.setRemark("remark");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),null,request,null);
        request.setLastModifyUserId("41d244733ec54f09a255836637f2b21d");
        request.setLastModifyRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setActionTypeId((short)3);
        request.setRemark("1111111111");
        storageService.updateNode(file,null,request,null);
    }

    private void updateNodeFilePty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setServerTypeId((short)2);
        request.setServerAddress("serverAddress");
        request.setBaseDir("baseDir");
        request.setReadOnlyKey("readOnlyKey");
        request.setWritableKey("writableKey");
        request.setMainFileId("mainFieldId");
        request.setFileVersion("fileVersion");
        request.setMajorTypeId("majorTypeId");
        request.setFileChecksum("fileChecksum");
        SimpleNodeDTO file = storageService.updateNodeSimple(getLocalNode(),request,null);
        request.setLastModifyUserId("41d244733ec54f09a255836637f2b21d");
        request.setLastModifyRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setServerTypeId((short)1);
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("a/b/c.txt");
        request.setWritableKey("a/b/d.txt");
        request.setMainFileId("2653ACA178AD49C29C2C81AB02006F69");
        request.setFileVersion("v1.0");
        request.setMajorTypeId("1");
        request.setFileChecksum("1111111111111111111111111111");
        storageService.updateNode(file,null,request,null);
    }

    private void updateNodeTreePty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setTaskId("f05fefd3cb204314b74503290b8410f3");
        request.setOwnerUserId("ownerUserId");
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setFileLength(111L);
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),null,request,null);
        request.setTaskId("f04c86a1a7da43d5b27b951eb5c69a32");
        request.setOwnerUserId("0623b7a797ac4341aaf2220bb375d670");
        request.setLastModifyUserId("41d244733ec54f09a255836637f2b21d");
        request.setLastModifyRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setFileLength(153L);
        storageService.updateNode(file,null,request,null);
    }

    private void updateNodeMovePath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        SimpleNodeDTO parent = getLocalNode("f43cb268d3334c76b920db4298b0239f-1");
        request.setPath("/bbb/haili");
        SimpleNodeDTO dir = storageService.updateNode(getLocalPath(),parent,request,null);
        parent = getLocalNode("f38eecabf0fe4217ad372750374fdd0b-1");
        request.setPath("aaa/海里haili");
        storageService.updateNode(dir,parent,request,null);
    }

    private SimpleNodeDTO getLocalNode(String id) throws Exception {
        return storageService.getNodeById(id,null);
    }

    private void updateNodeRenamePath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("haili");
        SimpleNodeDTO dir = storageService.updateNode(getLocalPath(),null,request,null);
        request.setPath("海里haili");
        storageService.updateNode(dir,null,request,null);
    }

    private void updateNodeMoveFileByPid() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        SimpleNodeDTO parent = getLocalNode("f43cb268d3334c76b920db4298b0239f-1");
        request.setPath("/xx/xxxx.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),parent,request,null);
        parent = getLocalNode(getLocalPath().getId());
        request.setPath("kk/kkk.txt");
        storageService.updateNode(file,parent,request,null);
    }

    private void updateNodeMoveFileByPath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("/maoding/设计/施工配合/施工图配合1/ww/yyy.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),null,request,null);
        request.setPath("/海狸大厦-生产安排设置人员测试/设计/施工图设计阶段/给排水施工图/废水系统/yy.txt");
        storageService.updateNode(file,null,request,null);
    }

    private void updateNodeRenameFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("xxxx.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),null,request,null);
        request.setPath("yyyy.txt");
        storageService.updateNode(file,null,request,null);
    }

//    private void updateNodeMove() throws Exception {
//        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC1");
//        SimpleNodeDTO parent = getNodeById("B6A183D9A58B48A385D7950785BDA6242");
//        UpdateNodeDTO request = new UpdateNodeDTO();
//        request.setFileLength(20);
//        request.setFullName("x/y/z/yyy.txt");
//        storageService.updateNodeWithParent(node,parent,request,null);
//    }
//
//    private void updateNodeFileLength() throws Exception {
//        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC2");
//        UpdateNodeDTO request = new UpdateNodeDTO();
//        request.setFileLength(0);
//        storageService.updateNodeWithParent(node,null,request,null);
//    }
//
//    private void updateNodeOwnerId() throws Exception {
//        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC2");
//        UpdateNodeDTO request = new UpdateNodeDTO();
//        request.setOwnerUserId("123456");
//        storageService.updateNodeWithParent(node,null,request,null);
//    }
//


}
