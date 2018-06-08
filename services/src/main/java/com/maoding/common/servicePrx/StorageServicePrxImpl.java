package com.maoding.common.servicePrx;

import com.maoding.common.zeroc.CustomException;
import com.maoding.common.zeroc.DeleteAskDTO;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.SpringUtils;
import com.maoding.coreUtils.StringUtils;
import com.maoding.storage.zeroc.*;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 11:54
 * 描    述 :
 */
public class StorageServicePrxImpl extends CoreRemoteService<StorageServicePrx> implements StorageServicePrx {

    private static StorageServicePrx lastPrx = null;
    private static StorageService storageService = null;
    private static String lastService = null;
    private static String lastConfig = null;

    private StorageService getStorageService(){
        if (storageService == null) {
            storageService = SpringUtils.getBean(StorageService.class);
        }
        return storageService;
    }

    /** 异步方式获取业务接口代理对象 */
    public static StorageServicePrx getInstance(String service, String config) {
        if ((lastPrx == null) || (StringUtils.isNotSame(lastService,service)) || (StringUtils.isNotSame(lastConfig,config))){
            StorageServicePrxImpl prx = new StorageServicePrxImpl();
            lastPrx = prx.getServicePrx(service, config, StorageServicePrx.class, _StorageServicePrxI.class, prx);
            lastService = service;
            lastConfig = config;
        }
        return lastPrx;
    }

    @Override
    public SummaryFileDTO summaryFile(QuerySummaryDTO query) throws CustomException {
        return getStorageService().summaryFile(query,null);
    }

    @Override
    public AnnotateDTO createAnnotate(NodeFileDTO file, UpdateAnnotateDTO request) throws CustomException {
        return getStorageService().createAnnotate(file,request,null);
    }

    @Override
    public EmbedElementDTO createEmbedElement(UpdateElementDTO request) throws CustomException {
        return getStorageService().createEmbedElement(request,null);
    }

    @Override
    public List<NodeFileDTO> listNodeFile(QueryNodeFileDTO query) throws CustomException {
        return getStorageService().listNodeFile(query,null);
    }

    @Override
    public NodeFileDTO createNodeFileWithRequestOnly(UpdateNodeFileDTO request) throws CustomException {
        return getStorageService().createNodeFileWithRequestOnly(request,null);
    }

    @Override
    public FullNodeDTO getNodeInfo(SimpleNodeDTO node, QueryNodeInfoDTO request) throws CustomException {
        return getStorageService().getNodeInfo(node,request,null);
    }

    @Override
    public List<FullNodeDTO> listFullNode(QueryNodeDTO query) throws CustomException {
        return getStorageService().listFullNode(query,null);
    }

    @Override
    public List<SimpleNodeDTO> listChild(SimpleNodeDTO parent) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parent.getId());
        return getStorageService().listChild(parent,null);
    }

    @Override
    public List<CANodeDTO> listCANode(QueryCANodeDTO query) throws CustomException {
        return getStorageService().listCANode(query,null);
    }

    @Override
    public List<SimpleNodeDTO> listChildren(SimpleNodeDTO parent) throws CustomException {
        return getStorageService().listChildren(parent,null);
    }

    @Override
    public List<NodeFileDTO> listFile(QueryNodeDTO query) throws CustomException {
        return getStorageService().listFile(query,null);
    }

    @Override
    public List<SimpleNodeDTO> listRoot(String accountId) throws CustomException {
        return getStorageService().listRoot(accountId,null);
    }

    @Override
    public NodeFileDTO createFileWithId(UpdateNodeFileDTO request, String id) throws CustomException {
        return getStorageService().createFileWithId(request,id,null);
    }

    @Override
    public NodeFileDTO createFile(UpdateNodeFileDTO request) throws CustomException {
        return getStorageService().createFile(request,null);
    }

    @Override
    public NodeFileDTO updateFile(NodeFileDTO src, UpdateNodeFileDTO request) throws CustomException {
        return getStorageService().updateFile(src,request,null);
    }

    @Override
    public NodeFileDTO updateNodeFile(NodeFileDTO src, UpdateNodeFileDTO request) throws CustomException {
        return getStorageService().updateNodeFile(src,request,null);
    }

    @Override
    public SimpleNodeDTO updateNode(SimpleNodeDTO src, SimpleNodeDTO parent, UpdateNodeDTO request) throws CustomException {
        return getStorageService().updateNode(src,parent,request,null);
    }

    @Override
    public SimpleNodeDTO createNode(SimpleNodeDTO parent, UpdateNodeDTO request) throws CustomException {
        return getStorageService().createNode(parent,request,null);
    }

    @Override
    public SimpleNodeDTO updateNodeSimple(SimpleNodeDTO src, UpdateNodeDTO request) throws CustomException {
        return getStorageService().updateNodeSimple(src,request,null);
    }

    @Override
    public List<SimpleNodeDTO> listNode(QueryNodeDTO query) throws CustomException{
        return getStorageService().listNode(query,null);
    }


    @Override
    public void deleteNode(SimpleNodeDTO node, DeleteAskDTO request) throws CustomException {
        getStorageService().deleteNode(node,request,null);
    }


    @Override
    public List<SimpleNodeDTO> listOldNode(QueryNodeDTO query) throws CustomException  {
        return getStorageService().listOldNode(query,null);
    }
}
