package com.maoding.FileServer.Config;

import com.maoding.Base.BaseRemoteService;
import com.maoding.Storage.zeroc.*;
import com.maoding.Utils.SpringUtils;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 11:54
 * 描    述 :
 */
public class RemoteStorageServicePrx extends BaseRemoteService<StorageServicePrx> implements StorageServicePrx {

    private static StorageServicePrx lastPrx = null;
    private StorageService storageService = null;

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

    private StorageService getStorageService(){
        if (storageService == null) {
            storageService = SpringUtils.getBean(StorageService.class);
        }
        return storageService;
    }

    @Override
    public SimpleNodeDTO createNode(UpdateNodeDTO request) {
        return getStorageService().createNode(request,null);
    }

    @Override
    public SimpleNodeDTO updateNode(SimpleNodeDTO src, UpdateNodeDTO request) {
        return getStorageService().updateNode(src,request,null);
    }

    @Override
    public List<SimpleNodeDTO> listAllNode(String userId) {
        return getStorageService().listAllNode(userId,null);
    }

    @Override
    public List<SimpleNodeDTO> listNode(QueryNodeDTO query) {
        return getStorageService().listNode(query,null);
    }

    @Override
    public List<FileNodeDTO> listFileNodeInfo(QueryNodeDTO query, boolean withHistory) {
        return getStorageService().listFileNodeInfo(query,withHistory,null);
    }

    @Override
    public FileNodeDTO getFileNodeInfo(SimpleNodeDTO node, boolean withHistory) {
        return getStorageService().getFileNodeInfo(node,withHistory,null);
    }

    @Override
    public boolean deleteNodeById(String id) {
        return getStorageService().deleteNodeById(id,null);
    }

    @Override
    public FullNodeDTO getFullNodeInfo(SimpleNodeDTO node) {
        return getStorageService().getFullNodeInfo(node,null);
    }
}
