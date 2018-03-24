package com.maoding.Storage;

import com.maoding.Base.BaseRemoteService;
import com.maoding.Common.ConstService;
import com.maoding.Common.zeroc.CustomException;
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
            remote = prx.getServicePrx("StorageService","127.0.0.1",StorageServicePrx.class,_StorageServicePrxI.class);
        }
        return remote;
    }

    @Test
    public void testCreateSuggestion() throws Exception{
        UpdateSuggestionDTO request = new UpdateSuggestionDTO();
        request.setContent("aaa");
        request.setStatusTypeId("3");
        storageService.createSuggestion(request,null);
    }

    @Test
    public void testCreateElement() throws Exception {
        UpdateElementDTO request = new UpdateElementDTO();
        request.setDataArray(new byte[]{1,2,3});
        storageService.createEmbedElement(request,null);
    }

    @Test
    public void testCreateAnnonate() throws Exception {
        UpdateAnnotateDTO request = new UpdateAnnotateDTO();
        request.setContent("这是一个测试");
        storageService.createAnnotate(request,null);
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
        return storageService.createNodeFile(request,null);
    }

    private NodeFileDTO createNodeFileNoMirror() throws Exception {
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setServerTypeId("2");
        request.setServerAddress("127.0.0.1");
        request.setBaseDir("c:/work/file_server");
        request.setReadOnlyKey("a/b/c.txt");
        request.setWritableKey("a/b/c_2.txt");
        request.setFileTypeId("2");
        return storageService.createNodeFile(request,null);
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
        storageService.deleteNodeById(null,node.getId(),null);
    }

    private void deleteByNode() throws Exception {
        log.debug("\t>>>>>>>> deleteByNode");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("tttt");
        SimpleNodeDTO node = storageService.createNode(getLocalPath(),request,null);
        storageService.deleteNode(null,node,null);
    }

    @Test
    public void testCreateMirror() throws Exception {
        createMirror();
        createMirrorError();
    }

    private void createMirror() throws Exception {
        log.debug("\t>>>>>>>> createMirror");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setMirrorTypeId((short)2);
        request.setMirrorAddress("127.0.0.1");
        request.setMirrorBaseDir("c:\\work\\file_server");
        request.setReadOnlyMirrorKey("/.mirror/xxx.txt");
        FullNodeDTO src = getLocalFullNode();
        FullNodeDTO dst = storageService.createMirror(src,request,null);
    }

    private void createMirrorError() throws Exception {
        log.debug("\t>>>>>>>> createMirrorError");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setMirrorTypeId((short)1);
        request.setMirrorAddress("127.0.0.1");
        request.setMirrorBaseDir("c:\\work\\file_server");
        request.setReadOnlyMirrorKey("/.mirror/xxx.txt");
        FullNodeDTO src = getLocalFullNode();
        src.setBasic(null);
        thrown.expect(CustomException.class);
        thrown.expectMessage("系统异常");
        FullNodeDTO dst = storageService.createMirror(src,request,null);
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

    private NodeFileDTO getLocalNodeFile() throws Exception {
        FullNodeDTO fileNode = getLocalFullNode();
        NodeFileDTO fileInfo = fileNode.getFileInfo();
        fileInfo.setId(StringUtils.left(fileNode.getBasic().getId(), StringUtils.DEFAULT_ID_LENGTH));
        return fileInfo;
    }

    @Test
    public void testListNode() throws Exception {
        listNodeById();
        listNodeByIdString();
        listNodeByPath();
        listOldNodeByFuzzyPath();
        listOldNodeByFuzzyId();
        listOldNodeByTypeId();
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
        request.setAccountId("hisaccount");
        request.setAccountRoleId("hisaccountRoleId");
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
        request.setAccountId("accountId");
        request.setAccountRoleId("accountRoleId");
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
        request.setAccountId("11111");
        request.setAccountRoleId("11111");
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
        request.setAccountId("111");
        request.setAccountRoleId("111");
        request.setOwnerUserId("222");
        request.setAccountId("222");
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
    public void testUpdateNode() throws Exception {
//        updateNodeRenameFile();
//        updateNodeMoveFileByPath();
//        updateNodeMoveFileByPid();
//        updateNodeRenamePath();
//        updateNodeMovePath();
//        updateNodeTreePty();
//        updateNodeFilePty();
//        updateNodeHisPty();
//        updateNodeMirrorPty();
        updateNodeFile();
    }

    private NodeFileDTO updateNodeFile() throws Exception {
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        NodeFileDTO file = storageService.updateNodeFile(getLocalNodeFile(),request,null);
        return file;
    }

    private void updateNodeMirrorPty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setAccountId("accountId");
        request.setAccountRoleId("accountRoleId");
        request.setMirrorTypeId((short)1);
        request.setMirrorAddress("127.0.0.1");
        request.setMirrorBaseDir("c:/work/.mirror");
        request.setReadOnlyMirrorKey("readonlymirror");
        request.setWritableMirrorKey("writableMirror");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),request,null);
        request.setAccountId("41d244733ec54f09a255836637f2b21d");
        request.setAccountRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setMirrorTypeId((short)1);
        request.setMirrorAddress("127.0.0.1");
        request.setMirrorBaseDir("c:/work/.mirror");
        request.setReadOnlyMirrorKey("aaaa.txt");
        request.setWritableMirrorKey("bbbb.txt");
        storageService.updateNode(file,request,null);
    }

    private void updateNodeHisPty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setAccountId("accountId");
        request.setAccountRoleId("accountRoleId");
        request.setActionTypeId((short)2);
        request.setRemark("remark");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),request,null);
        request.setAccountId("41d244733ec54f09a255836637f2b21d");
        request.setAccountRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setActionTypeId((short)3);
        request.setRemark("1111111111");
        storageService.updateNode(file,request,null);
    }

    private void updateNodeFilePty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setAccountId("accountId");
        request.setAccountRoleId("accountRoleId");
        request.setServerTypeId((short)2);
        request.setServerAddress("serverAddress");
        request.setBaseDir("baseDir");
        request.setReadOnlyKey("readOnlyKey");
        request.setWritableKey("writableKey");
        request.setMainFileId("mainFieldId");
        request.setFileVersion("fileVersion");
        request.setMajorTypeId("majorTypeId");
        request.setFileChecksum("fileChecksum");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),request,null);
        request.setAccountId("41d244733ec54f09a255836637f2b21d");
        request.setAccountRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setServerTypeId((short)1);
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("a/b/c.txt");
        request.setWritableKey("a/b/d.txt");
        request.setMainFileId("2653ACA178AD49C29C2C81AB02006F69");
        request.setFileVersion("v1.0");
        request.setMajorTypeId("1");
        request.setFileChecksum("1111111111111111111111111111");
        storageService.updateNode(file,request,null);
    }

    private void updateNodeTreePty() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setTaskId("f05fefd3cb204314b74503290b8410f3");
        request.setOwnerUserId("ownerUserId");
        request.setAccountId("accountId");
        request.setAccountRoleId("accountRoleId");
        request.setFileLength(111L);
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),request,null);
        request.setTaskId("f04c86a1a7da43d5b27b951eb5c69a32");
        request.setOwnerUserId("0623b7a797ac4341aaf2220bb375d670");
        request.setAccountId("41d244733ec54f09a255836637f2b21d");
        request.setAccountRoleId("1-05ed53eaf0114c18be5a658c26f9cdce");
        request.setFileLength(153L);
        storageService.updateNode(file,request,null);
    }

    private void updateNodeMovePath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPid("f43cb268d3334c76b920db4298b0239f-1");
        request.setPath("/bbb/haili");
        SimpleNodeDTO dir = storageService.updateNode(getLocalPath(),request,null);
        request.setPid("f38eecabf0fe4217ad372750374fdd0b-1");
        request.setPath("aaa/海里haili");
        storageService.updateNode(dir,request,null);
    }

    private void updateNodeRenamePath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("haili");
        SimpleNodeDTO dir = storageService.updateNode(getLocalPath(),request,null);
        request.setPath("海里haili");
        storageService.updateNode(dir,request,null);
    }

    private void updateNodeMoveFileByPid() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPid("f43cb268d3334c76b920db4298b0239f-1");
        request.setPath("/xx/xxxx.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),request,null);
        request.setPid(getLocalPath().getId());
        request.setPath("kk/kkk.txt");
        storageService.updateNode(file,request,null);
    }

    private void updateNodeMoveFileByPath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("/maoding/设计/施工配合/施工图配合1/ww/yyy.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),request,null);
        request.setPath("/海狸大厦-生产安排设置人员测试/设计/施工图设计阶段/给排水施工图/废水系统/yy.txt");
        storageService.updateNode(file,request,null);
    }

    private void updateNodeRenameFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("xxxx.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalNode(),request,null);
        request.setPath("yyyy.txt");
        storageService.updateNode(file,request,null);
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
