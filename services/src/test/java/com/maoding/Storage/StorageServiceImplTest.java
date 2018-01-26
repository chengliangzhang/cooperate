package com.maoding.Storage;

import com.maoding.Common.ConstService;
import com.maoding.Storage.zeroc.QueryNodeDTO;
import com.maoding.Storage.zeroc.SimpleNodeDTO;
import com.maoding.Storage.zeroc.StorageService;
import com.maoding.Storage.zeroc.UpdateNodeDTO;
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
//    private StorageServicePrx storageServicePrx = StorageServiceImpl.getInstance("StorageServer;192.168.13.140");

    @Test
    public void testCreateNode() throws Exception {
        createNodeSimple();
//        createNodeLongPNode();
    }

    @Test
    public void testUpdateNode() throws Exception {
//        updateNodeOwnerId();
//        updateNodeRename();
//        updateNodeFileLength();
        updateNodeMove();
    }

    private void updateNodeMove() throws Exception {
        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC1");
        SimpleNodeDTO parent = getNodeById("B6A183D9A58B48A385D7950785BDA6242");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setFileLength(20);
        request.setFullName("x/y/z/yyy.txt");
        storageService.updateNodeWithParent(node,parent,request,null);
    }

    private void updateNodeFileLength() throws Exception {
        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC2");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setFileLength(0);
        storageService.updateNodeWithParent(node,null,request,null);
    }

    private void updateNodeOwnerId() throws Exception {
        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC2");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setOwnerUserId("123456");
        storageService.updateNodeWithParent(node,null,request,null);
    }

    private void updateNodeRename() throws Exception {
        SimpleNodeDTO node = getNodeById("5C2E7230740E4809BA381FB5685976EC2");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setFullName("xxx.txt");
        storageService.updateNodeWithParent(node,null,request,null);
    }

    private void createNodeSimple() throws Exception {
        SimpleNodeDTO parent = getNodeById("05ebc26ddef6476c92294dbd9de556281");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setFullName("\\z/z\\abcde.txt");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_FILE_COMMIT_HIS);
        request.setFileLength(10);
        request.setReadFileScope("/z/z");
        request.setReadFileKey("abcde.txt");
        request.setRemark("aaaaaa");
        request.setMainFileId("89903A94A1C440AE8E4AD66091023BAB1");
        SimpleNodeDTO dto = storageService.createNodeWithParent(parent,request,null);
    }

    private void createNodeLongPNode() throws Exception {
        SimpleNodeDTO parent = getNodeById("00314e284e194df3b766f67a333830762");
        UpdateNodeDTO request = new UpdateNodeDTO();
        request.setFullName("/a/b/c/d/abcde.txt");
        request.setTypeId(ConstService.STORAGE_NODE_TYPE_FILE_COMMIT);
        request.setFileLength(10);
        storageService.createNodeWithParent(parent,request,null);
    }

    private SimpleNodeDTO getNodeById(String id) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> list = storageService.listNode(query,null);
        return list.get(0);
    }

}
