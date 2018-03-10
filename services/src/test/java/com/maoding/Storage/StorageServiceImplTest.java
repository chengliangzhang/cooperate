package com.maoding.Storage;

import com.maoding.Base.BaseRemoteService;
import com.maoding.Storage.zeroc.*;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    public void testCreateMirror() throws Exception {
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setServerTypeId((short)2);
        request.setServerAddress("127.0.0.1");
        request.setMirrorPath("c:\\work\\file_server");
        request.setReadonlyMirrorPath("/.mirror/xxx.txt");
        FullNodeDTO dst = storageService.createMirror(getLocalFullNode(),request,null);
    }

    @Test
    public void testGetNodeInfo() throws Exception {
        SimpleNodeDTO node = getLocalNode();
        QueryFullNodeDTO query = new QueryFullNodeDTO();
        query.setTextQuery(true);
        query.setFileQuery(true);
        query.setHistoryQuery(true);
        FullNodeDTO nodeInfo = storageService.getNodeInfo(node,query,null);
        assert (nodeInfo != null);
    }

    private SimpleNodeDTO getLocalNode() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("BB6992A070FF4C5DA270122FC6668E1F-1");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        return ((list != null) && !(list.isEmpty())) ? list.get(0) : null;
    }

    private FullNodeDTO getLocalFullNode() throws Exception {
        SimpleNodeDTO node = getLocalNode();
        QueryFullNodeDTO query = new QueryFullNodeDTO();
        query.setFileQuery(true);
        return storageService.getNodeInfo(node,query,null);
    }

    @Test
    public void testListNode() throws Exception {
        listNodeById();
        listNodeByIdString();
        listNodeByPath();
        SimpleNodeDTO node = getLocalNode();
        assert (node != null);
    }

    private void listNodeByPath() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath("/dd新项目/设计");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        assert (list.size() == 1);
        query = new QueryNodeDTO();
        query.setFuzzyPath("/73立项/提资/项目前期/aaaa/bbbb/cc.txt");
        list = storageService.listNode(query,null);
        assert (list.size() > 0);
        query = new QueryNodeDTO();
        query.setParentPath("/");
        query.setAccountId("41d244733ec54f09a255836637f2b21d");
        list = storageService.listNode(query,null);
        assert (list.size() > 0);
    }

    private void listNodeById() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("C6AC5795406745BAB796990BB889C0E7-1");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        assert (list.size() == 1);
        query = new QueryNodeDTO();
        query.setAccountId("41d244733ec54f09a255836637f2b21d");
        query.setProjectId("c8c049f763d245b5aa9850c43166245e");
        list = storageService.listNode(query,null);
        assert (list.size() > 0);
        query = new QueryNodeDTO();
        query.setFuzzyId("BB6992A070FF4C5DA270122FC6668E1F");
        list = storageService.listNode(query,null);
        assert (list.size() > 0);
    }

    private void listNodeByIdString() throws Exception {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId("C6AC5795406745BAB796990BB889C0E7-1,E9AA0F7A81444B8D932848E61CDAFFC3-1");
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        assert (list.size() > 0);
        query = new QueryNodeDTO();
        query.setFuzzyId("C6AC5795406745BAB796990BB889C0E7,E9AA0F7A81444B8D932848E61CDAFFC3");
        list = storageService.listNode(query,null);
        assert (list.size() > 0);
    }

//    private StorageServicePrx storageServicePrx = StorageServiceImpl.getInstance("StorageServer;192.168.13.140");

//    @Test
//    public void testCreateNode() throws Exception {
//        createNodeSimple();
////        createNodeLongPNode();
//    }
//
//    @Test
//    public void testUpdateNode() throws Exception {
////        updateNodeOwnerId();
////        updateNodeRename();
////        updateNodeFileLength();
//        updateNodeMove();
//    }
//
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
//    private void updateNodeRename() throws Exception {
//        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC2");
//        UpdateNodeDTO request = new UpdateNodeDTO();
//        request.setFullName("xxx.txt");
//        storageService.updateNodeWithParent(node,null,request,null);
//    }
//
//    private void createNodeSimple() throws Exception {
//        SimpleNodeDTO parent = getNodeById("05ebc26ddef6476c92294dbd9de556281");
//        UpdateNodeDTO request = new UpdateNodeDTO();
//        request.setFullName("\\z/z\\abcde.txt");
//        request.setTypeId(ConstService.STORAGE_NODE_TYPE_FILE_COMMIT_HIS);
//        request.setFileLength(10);
//        request.setReadFileScope("/z/z");
//        request.setReadFileKey("abcde.txt");
//        request.setRemark("aaaaaa");
//        request.setMainFileId("89903A94A1C440AE8E4AD66091023BAB1");
//        SimpleNodeDTO dto = storageService.createNodeWithParent(parent,request,null);
//    }
//
//    private void createNodeLongPNode() throws Exception {
//        SimpleNodeDTO parent = getNodeById("00314e284e194df3b766f67a333830762");
//        UpdateNodeDTO request = new UpdateNodeDTO();
//        request.setFullName("/a/b/c/d/abcde.txt");
//        request.setTypeId(ConstService.STORAGE_NODE_TYPE_FILE_COMMIT);
//        request.setFileLength(10);
//        storageService.createNodeWithParent(parent,request,null);
//    }
//
//    private SimpleNodeDTO getNodeById(String id) {
//        QueryNodeDTO query = new QueryNodeDTO();
//        query.setId(id);
//        List<SimpleNodeDTO> list = storageService.listNode(query,null);
//        return list.get(0);
//    }

}
