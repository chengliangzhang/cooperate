package com.maoding.FileServer.Config;

import com.maoding.Base.BaseRemoteService;
import com.maoding.Common.zeroc.CustomException;
import com.maoding.Common.zeroc.DeleteAskDTO;
import com.maoding.CoreUtils.SpringUtils;
import com.maoding.Storage.zeroc.*;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 11:54
 * 描    述 :
 */
@SuppressWarnings("deprecation")
public class RemoteStorageServicePrx extends BaseRemoteService<StorageServicePrx> implements StorageServicePrx {

    private static StorageServicePrx lastPrx = null;
    private static StorageService storageService = null;

    private StorageService getStorageService(){
        if (storageService == null) {
            storageService = SpringUtils.getBean(StorageService.class);
        }
        return storageService;
    }

    /** 同步方式获取业务接口代理对象 */
    public static StorageServicePrx getInstance(String adapterName) {
        if (lastPrx == null){
            RemoteStorageServicePrx prx = new RemoteStorageServicePrx();
            lastPrx = prx.getServicePrx("StorageService",adapterName,StorageServicePrx.class,_StorageServicePrxI.class,prx);
        }
        return lastPrx;
    }
    public static StorageServicePrx getInstance(){
        return getInstance(null);
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
    public List<SimpleNodeDTO> listChild(SimpleNodeDTO parent) throws CustomException {
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
    public List<SimpleNodeDTO> listRoot(String accountId) throws CustomException {
        return getStorageService().listRoot(accountId,null);
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
