package com.maoding.Storage;

import com.maoding.common.LocalConstService;
import com.maoding.common.zeroc.DeleteAskDTO;
import com.maoding.common.zeroc.QueryAskDTO;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.StringUtils;
import com.maoding.storage.zeroc.*;
import org.junit.Ignore;
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

    private final boolean TEST_LOCAL = false;
    private final boolean SAME_TEST = false;

    @Autowired
    private StorageService storageService;

    private StorageServicePrx remote = null;

    private StorageServicePrx getRemote(){
        if (remote == null) {
            CoreRemoteService<StorageServicePrx> prx = new CoreRemoteService<>();
            remote = prx.getServicePrx("StorageService","StorageServer;192.168.13.140",StorageServicePrx.class,_StorageServicePrxI.class);
        }
        return remote;
    }

    @Ignore
    @Test
    public void testListNodeFileByKey() throws Exception {
        log.debug("\t>>>>>>>> testListNodeFileByKey");
        List<NodeFileDTO> list;
        QueryNodeFileDTO query = new QueryNodeFileDTO();
        query.setKey("c_3.txt");
        list = storageService.listNodeFile(query,null);
        list = storageService.listNodeFile(query,null);
        list = storageService.listNodeFile(query,null);
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

    @Ignore
    @Test
    public void testCreateElement() throws Exception {
        UpdateElementDTO request = new UpdateElementDTO();
        request.setDataArray(new byte[]{1,2,3});
        storageService.createEmbedElement(request,null);
    }

    @Ignore
    @Test
    public void testUpdateElement() throws Exception {
        UpdateElementDTO request = new UpdateElementDTO();
        request.setDataArray(new byte[]{0x37,0x35,0x36});
        storageService.updateEmbedElement(getLocalElement(),request,null);
    }

    @Ignore
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

    @Ignore
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

    @Ignore
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
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;
        QuerySummaryDTO query = new QuerySummaryDTO();
        query.setAccountId(getLocalAccountId());

        SummaryFileDTO summary;
        for (int i=0; i<1; i++){
            long t = System.currentTimeMillis();
            if (isTestLocal) {
                summary = storageService.summaryFile(query,null);;
            } else {
                summary = getRemote().summaryFile(query,null);;
            }
            log.info("\t>>>>>>>> testSummary:" + (System.currentTimeMillis()-t) + "ms");
        }
    }

    @Ignore
    @Test
    public void testDelete() throws Exception{
        deleteByNode();
        deleteById();
    }

    private void deleteById() throws Exception {
        log.debug("\t>>>>>>>> deleteById");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("tttt");
        SimpleNodeDTO node = storageService.createNode(getLocalParent(),request,null);
        storageService.deleteNodeById(node.getId(),getLocalDeleteAsk(),null);
    }

    private void deleteByNode() throws Exception {
        log.debug("\t>>>>>>>> deleteByNode");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("tttt");
        SimpleNodeDTO node = storageService.createNode(getLocalParent(),request,null);
        storageService.deleteNode(node,getLocalDeleteAsk(),null);
    }

    private DeleteAskDTO getLocalDeleteAsk(){
        DeleteAskDTO ask = new DeleteAskDTO();
        ask.setLastModifyUserId("41d244733ec54f09a255836637f2b21d");
        return ask;
    }

    @Ignore
    @Test
    public void testGetNodeInfo() throws Exception {
        SimpleNodeDTO node = getLocalFileNode();
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

    private SimpleNodeDTO getLocalParent() throws Exception {
        return getLocalNode("3C06760164134131B711F5D744BAA54C-2");
    }

    private SimpleNodeDTO getLocalTask() throws Exception {
        return getLocalNode("4efcc0c056984d27bffe0c2817d292c5-1");
    }

    private SimpleNodeDTO getLocalRange() throws Exception {
        return getLocalNode("01bb5bf70fab49dfbf180f64f0594be3-2");
    }

    private SimpleNodeDTO getLocalProject() throws Exception {
        return getLocalNode("01bb5bf70fab49dfbf180f64f0594be3");
    }

    private SimpleNodeDTO getLocalFileNode() throws Exception {
        return getLocalNode("0CC161F7AB7141FC9B833F15EA4ACF99-1");
    }

    private SimpleNodeDTO getLocalNode(String id) throws Exception {
        return getNode(id,true);
    }

    private SimpleNodeDTO getNode(String id,boolean isLocal) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> list = (isLocal) ? storageService.listNode(query,null) :
                getRemote().listNode(query);
        return list.get(0);
    }



    private FullNodeDTO getLocalFullNode() throws Exception {
        SimpleNodeDTO node = getLocalFileNode();
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
        return getLocalNodeFile("434553E123F24ADCA64083047D31FE79-2");
    }
    private NodeFileDTO getLocalNodeFile(String id) throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<NodeFileDTO> list = storageService.listFile(query,null);
        return list.get(0);
    }

    @Test
    public void testListChild() throws Exception {
        log.debug("\t>>>>>>>> testListChild");
        List<SimpleNodeDTO> list;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid("d1ba184a668d49789a224e8e8200fb17-1");
        query.setNeedPath("1");
        query.setNeedOwnerName("1");
        query.setNeedTaskName("1");
        query.setNeedCompanyName("1");
        query.setNeedLastModifyUserName("sun");
        list = storageService.listNode(query,null);
        query.setNeedProjectName("1");
        list = storageService.listNode(query,null);
        query.setNeedLastModifyUserName("1");
        list = storageService.listNode(query,null);
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

    private String getLocalAccountId(){
        return "d437448683314cad91dc30b68879901d";
    }

    private QueryNodeDTO getAccountQuery() {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setAccountId(getLocalAccountId());
        return query;
    }

    private QueryNodeDTO getFileQuery() {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setDirectoryMode("0");
        return query;
    }

    private QueryNodeDTO getQueryTaskChild() {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid("d1ba184a668d49789a224e8e8200fb17-1");
//        query.setNeedPath("1");
//        query.setNeedOwnerName("1");
//        query.setNeedProjectName("1");
//        query.setNeedTaskName("1");
        return query;
    }

    private QueryNodeDTO getQueryPath() {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath("/dd新项目/设计");
        query.setNeedPath("1");
        query.setNeedOwnerName("1");
        query.setNeedProjectName("1");
        query.setNeedTaskName("1");
        return query;
    }

    @Test
    public void testListFileByAccount() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;
        QueryNodeDTO query = getFileQuery();
        query.setAccountId(getLocalAccountId());

        List<NodeFileDTO> list;
        for (int i=0; i<1; i++){
            long t = System.currentTimeMillis();
            if (isTestLocal) {
                list = storageService.listFile(query,null);;
            } else {
                list = getRemote().listFile(query,null);;
            }
            log.info("\t>>>>>>>> testListFileByAccount:" + (System.currentTimeMillis()-t) + "ms");
        }
    }

    @Test
    public void testListFileByFileId() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyId(StringUtils.left(getLocalFileNode().getId(), StringUtils.DEFAULT_ID_LENGTH));

        List<NodeFileDTO> list;
        for (int i=0; i<1; i++){
            long t = System.currentTimeMillis();
            if (isTestLocal) {
                list = storageService.listFile(query,null);;
            } else {
                list = getRemote().listFile(query,null);;
            }
            log.info("\t>>>>>>>> testListFileByFileId:" + (System.currentTimeMillis()-t) + "ms");
        }
    }

    @Ignore
    @Test
    public void testListCANode() throws Exception {
        log.debug("\t>>>>>>>> testListCANode");
        QueryCANodeDTO query = new QueryCANodeDTO();
        query.setIsDesign("1");
        List<CANodeDTO> list;
        list = storageService.listCANode(query,null);
        list = storageService.listCANode(query,null);
        list = storageService.listCANode(query,null);
    }

    @Test
    public void testListNodeByPath() throws Exception {
        log.debug("\t>>>>>>>> testListNodeByPath");
        List<SimpleNodeDTO> list;
        QueryNodeDTO query = getQueryPath();
        list = storageService.listNode(query,null);
        list = storageService.listNode(query,null);
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



    private SimpleNodeDTO createParentChild() throws Exception {
        return storageService.createNode(getLocalParent(),getUpdateFileAsk(),null);
    }

    @Ignore
    @Test
    public void testCreateTaskChild() throws Exception {
        SimpleNodeDTO node;
        node = storageService.createNode(getLocalTask(),getUpdateFileAsk(),null);
    }

    private SimpleNodeDTO createRangeChild() throws Exception {
        return storageService.createNode(getLocalRange(),getUpdateFileAsk(),null);
    }

    private SimpleNodeDTO createProjectChild() throws Exception {
        return storageService.createNode(getLocalProject(),getUpdateFileAsk(),null);
    }

    private SimpleNodeDTO createRootChild() throws Exception {
        return storageService.createNode(null,getUpdateFileAsk(),null);
    }

    private UpdateNodeDTO getUpdateFileAsk() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("userId");
        request.setLastModifyRoleId("roleId");
        request.setOwnerUserId("owner");
        request.setPath("测试.txt");
        request.setServerTypeId("1");
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("/x/y/abcde.txt");
        request.setWritableKey("/x/y/aaaa.txt");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_FILE_COMMIT));
        request.setFileLength("50");
        request.setFileMd5("fileMd5");
        request.setActionTypeId(Short.toString(LocalConstService.STORAGE_ACTION_TYPE_COMMIT));
        request.setRemark("这是a 历史记录");
        return request;
    }

    private SimpleNodeDTO createHisFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("hisaccount");
        request.setLastModifyRoleId("hisaccountRoleId");
        request.setOwnerUserId("3333");
        request.setPath("abcde.txt");
        request.setServerTypeId("1");
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("/x/y/abcde.txt");
        request.setWritableKey("/x/y/aaaa.txt");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_FILE_COMMIT));
        request.setFileLength("50");
        request.setRemark("这是a 历史记录");
        return storageService.createNode(getLocalParent(),request,null);
    }

    private void createMirrorFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setOwnerUserId("3333");
        request.setPath("abcde.txt");
        request.setServerTypeId("1");
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("/x/y/abcde.txt");
        request.setWritableKey("/x/y/aaaa.txt");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_FILE_COMMIT));
        request.setFileLength("50");
        request.setMirrorBaseDir("c:/work/file_server");
        request.setReadOnlyMirrorKey("mirror.txt");
        SimpleNodeDTO dto = storageService.createNode(getLocalParent(),request,null);
    }

    private void createPathFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setLastModifyUserId("11111");
        request.setLastModifyRoleId("11111");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_UNKNOWN));
        request.setPath("abcde.txt");
        request.setServerTypeId("1");
        request.setServerAddress("c:");
        request.setBaseDir("/work/file_server");
        request.setReadOnlyKey("/x/y/abcde.txt");
        request.setWritableKey("/x/y/aaaa.txt");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_FILE_COMMIT));
        request.setMainFileId("xxx");
        request.setFileLength("10");
        request.setMajorTypeId("1222");
        request.setFileVersion("v1.0");
        SimpleNodeDTO dto = storageService.createNode(getLocalParent(),request,null);
    }

    private void createAbsoluteDir() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN));
        request.setPath("/1/2/3/abcde");
        SimpleNodeDTO dto = storageService.createNode(null,request,null);
    }

    private void createPathDir() throws Exception {
        SimpleNodeDTO parent = getLocalParent();
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN));
        request.setPath("/1/2/3/abcde");
        request.setLastModifyUserId("111");
        request.setLastModifyRoleId("111");
        request.setOwnerUserId("222");
        request.setLastModifyUserId("222");
        SimpleNodeDTO dto = storageService.createNode(parent,request,null);
    }

    private void createSimpleDir() throws Exception {
        SimpleNodeDTO parent = getLocalParent();
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("12345");
        request.setTypeId(Short.toString(LocalConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN));
        request.setPath("abcde");
        SimpleNodeDTO dto = storageService.createNode(parent,request,null);
    }


    @Ignore
    @Test
    public void updateNodeFileSimple() throws Exception {
        NodeFileDTO file;
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setPassAudit("1");
        file = storageService.updateFile(getLocalNodeFile(),request,null);
        file = storageService.updateFile(getLocalNodeFile(),request,null);
        file = storageService.updateFile(getLocalNodeFile(),request,null);
    }


    @Ignore
    @Test
    public void testUpdateNodeTree() throws Exception {
        SimpleNodeDTO node;
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("ownerUserId");
        request.setLastModifyUserId("accountId");
        request.setLastModifyRoleId("accountRoleId");
        request.setPath("axax");
        storageService.updateNode(getLocalParent(),null,request,null);
    }

    private void updateNodeMovePath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        SimpleNodeDTO parent = getLocalNode("f43cb268d3334c76b920db4298b0239f-1");
        request.setPath("/bbb/haili");
        SimpleNodeDTO dir = storageService.updateNode(getLocalParent(),parent,request,null);
        parent = getLocalNode("f38eecabf0fe4217ad372750374fdd0b-1");
        request.setPath("aaa/海里haili");
        storageService.updateNode(dir,parent,request,null);
    }

    private void updateNodeRenamePath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("haili");
        SimpleNodeDTO dir = storageService.updateNode(getLocalParent(),null,request,null);
        request.setPath("海里haili");
        storageService.updateNode(dir,null,request,null);
    }

    private void updateNodeMoveFileByPid() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        SimpleNodeDTO parent = getLocalNode("f43cb268d3334c76b920db4298b0239f-1");
        request.setPath("/xx/xxxx.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalFileNode(),parent,request,null);
        parent = getLocalNode(getLocalParent().getId());
        request.setPath("kk/kkk.txt");
        storageService.updateNode(file,parent,request,null);
    }

    private void updateNodeMoveFileByPath() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("/maoding/设计/施工配合/施工图配合1/ww/yyy.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalFileNode(),null,request,null);
        request.setPath("/海狸大厦-生产安排设置人员测试/设计/施工图设计阶段/给排水施工图/废水系统/yy.txt");
        storageService.updateNode(file,null,request,null);
    }

    private void updateNodeRenameFile() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setPath("xxxx.txt");
        SimpleNodeDTO file = storageService.updateNode(getLocalFileNode(),null,request,null);
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
