package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.Const.FileServerConst;
import com.maoding.Const.StorageConst;
import com.maoding.FileServer.zeroc.FileDTO;
import com.maoding.FileServer.zeroc.FileRequestDTO;
import com.maoding.FileServer.zeroc.FileService;
import com.maoding.Project.zeroc.ProjectDTO;
import com.maoding.Project.zeroc.ProjectService;
import com.maoding.Storage.Dao.StorageDao;
import com.maoding.Storage.Dao.StorageFileDao;
import com.maoding.Storage.Dao.StorageFileHisDao;
import com.maoding.Storage.Dto.QueryNodeDTO;
import com.maoding.Storage.Dto.StorageEntityUnionDTO;
import com.maoding.Storage.Entity.StorageEntity;
import com.maoding.Storage.Entity.StorageFileEntity;
import com.maoding.Storage.Entity.StorageFileHisEntity;
import com.maoding.Storage.zeroc.*;
import com.maoding.User.zeroc.AccountDTO;
import com.maoding.User.zeroc.UserService;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/2 14:21
 * 描    述 :
 */
@Service("storageService")
@Transactional(rollbackFor = Exception.class)
public class StorageServiceImpl extends BaseLocalService<StorageServicePrx> implements StorageService,StorageServicePrx{

    @Autowired
    private FileService fileService;

    @Autowired
    private StorageFileDao storageFileDao;

    @Autowired
    private StorageDao storageDao;

    @Autowired
    private StorageFileHisDao storageFileHisDao;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    /** 同步方式获取业务接口代理对象 */
    public static StorageServicePrx getInstance(String adapterName) {
        StorageServiceImpl prx = new StorageServiceImpl();
        return prx.getServicePrx("StorageService",adapterName, StorageServicePrx.class,_StorageServicePrxI.class);
    }
    public static StorageServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
    public FullNodeDTO getFullNodeInfoByIdForAccount(AccountDTO account, String id, Current current) {
        StorageQueryDTO query = new StorageQueryDTO();
        query.setId(id);
        if (account != null) query.setUserId(account.getId());
        return getFullNodeInfo(query,current);
    }

    @Override
    public FullNodeDTO getFullNodeInfoById(String id, Current current) {
        return getFullNodeInfoByIdForAccount(userService.getCurrent(current),id,current);
    }

    @Override
    public SimpleNodeDTO createCustomerDirForAccount(AccountDTO account, CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        if (request.getTypeId() == StorageConst.STORAGE_NODE_TYPE_UNKNOWN) {
            StorageQueryDTO query = new StorageQueryDTO();
            query.setPath(request.getPath());
            SimpleNodeDTO root = getNearSimpleNodeInfoForAccount(null,query,current);
            request.setTypeId((root == null) ? StorageConst.STORAGE_NODE_TYPE_DIR_USER : StorageConst.getPathType(root.typeId));
        }
        if (account != null) request.setUserId(account.getId());
        assert (!StorageConst.isFileType(request.getTypeId()));
        return createStorageNode(request,current);
    }

    @Override
    public SimpleNodeDTO createCustomerDir(CreateNodeRequestDTO request, Current current) {
        return createCustomerDirForAccount(userService.getCurrent(current),request,current);
    }


    @Override
    public SimpleNodeDTO createCustomerFileForAccount(AccountDTO account, CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        if (request.getTypeId() == StorageConst.STORAGE_NODE_TYPE_UNKNOWN) {
            StorageQueryDTO query = new StorageQueryDTO();
            query.setPath(request.getPath());
            SimpleNodeDTO root = getNearSimpleNodeInfoForAccount(null,query,current);
            request.setTypeId((root == null) ? StorageConst.STORAGE_NODE_TYPE_FILE_MAIN : StorageConst.getFileType(root.typeId));
        }
        if (account != null) request.setUserId(account.getId());
        assert (StorageConst.isFileType(request.getTypeId()));
        return createStorageNode(request,current);
    }

    @Override
    public SimpleNodeDTO createCustomerFile(CreateNodeRequestDTO request, Current current) {
        return createCustomerFileForAccount(userService.getCurrent(current),request,current);
    }

    @Override
    public FullNodeDTO getFullNodeInfoByPathForAccount(AccountDTO account, String path, Current current) {
        StorageQueryDTO query = new StorageQueryDTO();
        query.setPath(path);
        return getFullNodeInfo(query,current);
    }

    @Override
    public FullNodeDTO getFullNodeInfoByPath(String path, Current current) {
        return getFullNodeInfoByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public FullNodeDTO getFullNodeInfo(StorageQueryDTO query, Current current) {
        return getFullNodeInfoForAccount(userService.getCurrent(current),query,current);
    }

    @Override
    public FullNodeDTO getFullNodeInfoForAccount(AccountDTO account, StorageQueryDTO query, Current current) {
        long t = System.currentTimeMillis();

        assert (query != null);
        query = BeanUtils.cleanProperties(query);
        query.setPath(StringUtils.formatPath(query.getPath()));
        FullNodeDTO node = storageDao.getFullNodeInfo(query);

        log.info("===>getFullNodeInfoForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    @Override
    public FullNodeDTO getNearFullNodeInfoByPath(String path, Current current) {
        return getNearFullNodeInfoByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public FullNodeDTO getNearFullNodeInfoByPathForAccount(AccountDTO account, String path, Current current) {
        StorageQueryDTO query = new StorageQueryDTO();
        query.setPath(path);
        return getNearFullNodeInfoForAccount(account,query,current);
    }

    @Override
    public FullNodeDTO getNearFullNodeInfo(StorageQueryDTO query, Current current) {
        return getNearFullNodeInfoForAccount(userService.getCurrent(current),query,current);
    }

    @Override
    public FullNodeDTO getNearFullNodeInfoForAccount(AccountDTO account, StorageQueryDTO query, Current current) {

        long t = System.currentTimeMillis();

        assert (query != null);
        query = BeanUtils.cleanProperties(query);
        query.setPath(StringUtils.formatPath(query.getPath()));
        FullNodeDTO node = storageDao.getNearFullNodeInfo(query);

        log.info("===>getNearFullNodeInfoForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    @Override
    public List<SimpleNodeDTO> listAllSubNodeByPathForAccount(AccountDTO account, String path, Current current) {
        path = StringUtils.formatPath(path);
        if (StringUtils.isEmpty(path) || StringUtils.isSame(StringUtils.SPLIT_PATH,path)) return listRootNodeForAccount(account,current);

        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        path = StringUtils.formatPath(path);
        query.setPath(path);
        query.setUserId(account.getId());
        List<SimpleNodeDTO> list = storageDao.listAllSubNode(query);

        log.info("===>listAllSubNodeByPathForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return list;
    }

    @Override
    public List<SimpleNodeDTO> listAllSubNodeByPath(String path, Current current) {
        return listAllSubNodeByPathForAccount(userService.getCurrent(current),path,current);
    }


    private boolean copyNode(String oldPath, String newPath, Current current) {
        assert (oldPath != null) && (newPath != null);

        long t = System.currentTimeMillis();

        int n = 0;

        FullNodeDTO newNode = getFullNodeInfoByPath(newPath,current);
        if (newNode == null){
            FullNodeDTO rootNode = getNearFullNodeInfoByPath(newPath,current);
            assert (rootNode != null);

        }
        FullNodeDTO oldNode = getFullNodeInfoByPath(oldPath,current);
        oldPath = StringUtils.formatPath(oldPath);
        newPath = StringUtils.formatPath(newPath);

        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(newPath);
        SimpleNodeDTO targetNode = storageDao.getNodeInfo(query);
        if (targetNode == null) {
        }

        log.info("===>copyNode:" + (System.currentTimeMillis()-t) + "ms");
        return (n > 0);
    }

    private StorageEntityUnionDTO copyNode(AccountDTO account,FullNodeDTO srcNode,CreateNodeRequestDTO createRequest, Current current) {
        return copyNodeForAccount(userService.getCurrent(current),srcNode,createRequest,current);
    }

    private StorageEntityUnionDTO copyNodeForAccount(AccountDTO account, FullNodeDTO srcNode,CreateNodeRequestDTO createRequest, Current current){
        //复制最新发布文件
        assert (createRequest != null);
        String path = StringUtils.formatPath(createRequest.getPath());
        //获取要创建的目标的绝对路径
        if ((createRequest.getPid() != null) && (!path.startsWith(StringUtils.SPLIT_PATH))) {
            SimpleNodeDTO pNode = getNodeByIdForAccount(account,createRequest.getPid(),current);
            if (pNode != null) {
                path = pNode.getPath() + StringUtils.SPLIT_PATH + path;
            } else {
                path = StringUtils.SPLIT_PATH + path;
            }
            createRequest.setPid(null);
            createRequest.setPath(path);
        }
        StorageEntityUnionDTO nodeUnion = createNodeEntityForAccount(account,createRequest,current);
        assert (nodeUnion != null);
        if (StorageConst.isFileType(nodeUnion.getTypeId())) {
            assert (srcNode != null) && (!StringUtils.isEmpty(srcNode.getBasic().getId()));
            StorageFileEntity srcFile = storageFileDao.selectById(srcNode.getBasic().getId());
            assert (srcFile != null);
            FileDTO src = getRealFile(srcFile, true);
            StorageFileEntity dstFile = nodeUnion.getFileEntity();
            assert (dstFile != null);
            FileDTO dst = getRealFile(dstFile,false);
            if (StringUtils.isEmpty(dst.getScope())){
                assert (!StringUtils.isEmpty(path));
                dst.setScope(StringUtils.getDirName(path));
            }
            if (StringUtils.isEmpty(dst.getKey())) {
                assert (!StringUtils.isEmpty(path));
                dst.setKey(StringUtils.getFileName(path));
            }

            FileDTO realDst = fileService.copyFile(src, dst, current);

            dstFile.setFileScope(realDst.getScope());
            dstFile.setFileKey(realDst.getKey());
            dstFile.setFileTypeId(srcFile.getFileTypeId());
            dstFile.setUploadScope(null);
            dstFile.setUploadKey(null);
            storageFileDao.updateExactById(dstFile,dstFile.getId());
            if (!StringUtils.isSame(realDst.getKey(),dst.getKey())) {
                fileService.deleteFile(dst, current);
            }
        }
        return nodeUnion;
    }

    @Override
    public List<SimpleNodeDTO> commitFileList(List<CommitRequestDTO> requestList, Current current) {
        return commitFileListForAccount(userService.getCurrent(current),requestList,current);
    }

    @Override
    public List<SimpleNodeDTO> commitFileListForAccount(AccountDTO account, List<CommitRequestDTO> requestList, Current current) {
        List<SimpleNodeDTO> list = new ArrayList<>();
        for (CommitRequestDTO request : requestList){
            list.add(commitFileForAccount(account,request,current));
        }
        return list;
    }

    @Override
    public SimpleNodeDTO commitFile(CommitRequestDTO request, Current current) {
        return commitFileForAccount(userService.getCurrent(current),request,current);
    }

    @Override
    public SimpleNodeDTO commitFileForAccount(AccountDTO account, CommitRequestDTO request, Current current) {
        assert (request != null);
        request = BeanUtils.cleanProperties(request);

        //查找原始文件
        StorageQueryDTO srcQuery = new StorageQueryDTO();
        srcQuery.setPath(request.getPath());
        srcQuery = BeanUtils.cleanProperties(srcQuery);
        FullNodeDTO srcNode = storageDao.getFullNodeInfo(srcQuery);

        //生成最新发布文件
        CreateNodeRequestDTO createRequest = new CreateNodeRequestDTO();
        assert (srcNode != null) && (!StringUtils.isEmpty(srcNode.getProjectId()));
        createRequest.setPid(srcNode.getProjectId() + StorageConst.STORAGE_CLASSIC_TYPE_COMMIT);
        String path = srcNode.getIssuePath() + StringUtils.SPLIT_PATH
                + request.getMajor() + StringUtils.SPLIT_PATH
                + request.getTitle() + StringUtils.SPLIT_PATH
                + srcNode.getTaskPath() + StringUtils.SPLIT_PATH
                + srcNode.getStoragePath();
        path = StringUtils.formatPath(path);
        createRequest.setPath(path);
        assert (srcNode.getBasic() != null);
        createRequest.setFileLength(srcNode.getBasic().getFileLength());
        createRequest.setFileTypeId(StorageConst.STORAGE_FILE_TYPE_UNKNOWN);
        createRequest.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_COMMIT);
        createRequest = BeanUtils.cleanProperties(createRequest);
        StorageEntityUnionDTO dstUnion = copyNodeForAccount(account,srcNode,createRequest,current);

        //生成发布历史版本文件
        createRequest.setPid(srcNode.getProjectId() + StorageConst.STORAGE_CLASSIC_TYPE_COMMIT);
        String hisPath = StringUtils.getDirName(path)
                + StringUtils.SPLIT_PATH + "历史版本";
        hisPath += StringUtils.SPLIT_PATH
                + StringUtils.getFileNameWithoutExt(path)
                + StringUtils.SPLIT_NAME_PART
                + StringUtils.getTimeStamp(StringUtils.TIME_STAMP_FORMAT)
                + StringUtils.getFileExt(path);
        createRequest.setPath(hisPath);
        createRequest.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_COMMIT_HIS);
        StorageEntityUnionDTO hisUnion = copyNodeForAccount(account,srcNode,createRequest,current);
        assert (hisUnion != null);

        //添加历史记录
        StorageFileEntity hisFile = hisUnion.getFileEntity();
        assert (hisFile != null);
        StorageFileHisEntity his = BeanUtils.createFrom(hisFile,StorageFileHisEntity.class);
        assert (srcNode.getBasic() != null);
        his.setFileId(srcNode.getBasic().getId());
        his.setActionId(StorageConst.STORAGE_ACTION_TYPE_COMMIT);
        his.setRemark(request.getRemark());
        his.setLastModifyUserId(hisFile.getLastModifyUserId());
        storageFileHisDao.insert(his);

        SimpleNodeDTO dto = convertToSimpleNodeDTO(dstUnion);
        dto.setPath(path);
        return dto;
    }

    private FileDTO getRealFile(StorageFileEntity fileEntity, boolean isReadOnly){
        if (fileEntity == null) return null;
        FileDTO dto = new FileDTO();
        if (isReadOnly) {
            dto.setScope(fileEntity.getFileScope());
            dto.setKey(fileEntity.getFileKey());
        } else {
            dto.setScope(fileEntity.getUploadScope());
            if (StringUtils.isEmpty(dto.getScope())) dto.setScope(fileEntity.getFileScope());
            dto.setKey(fileEntity.getUploadKey());
            if (StringUtils.isEmpty(dto.getKey())) dto.setKey(fileEntity.getFileKey());
        }
        return dto;
    }

    @Override
    public List<String> listMajor(Current current) {
        return storageDao.listMajor();
    }

    @Override
    public List<SimpleNodeDTO> listAllNodeForAccount(AccountDTO account, Current current) {
        assert (account != null);

        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        query.setUserId(account.getId());

        List<SimpleNodeDTO> nodeList = storageDao.listAllNode(query);

        log.info("===>listAllNodeForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return nodeList;
    }

    @Override
    public List<SimpleNodeDTO> listAllNode(Current current) {
        return listAllNodeForAccount(userService.getCurrent(current),current);
    }

    @Override
    public List<SimpleNodeDTO> listRootNode(Current current) {
        return listRootNodeForAccount(userService.getCurrent(current),current);
    }

    @Override
    public List<SimpleNodeDTO> listRootNodeForCurrent(Current current) {
        return listRootNodeForAccount(userService.getCurrent(current),current);
    }

    @Override
    public List<SimpleNodeDTO> listRootNodeForAccount(AccountDTO account, Current current) {
        assert (account != null);

        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        query.setUserId(account.getId());

        List<SimpleNodeDTO> nodeList = storageDao.listRootNode(query);

        log.info("===>listRootNodeForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return nodeList;
    }

    @Override
    public SimpleNodeDTO getNodeByPathForAccount(AccountDTO account, String path, Current current) {
        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        if (account != null) query.setUserId(account.getId());
        query.setPath(StringUtils.formatPath(path));

        SimpleNodeDTO node = getNodeInfo(query);

        log.info("===>getNodeByPathForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    @Override
    public SimpleNodeDTO getNodeByPath(String path, Current current) {
        return getNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public SimpleNodeDTO getNodeByPathForCurrent(String path, Current current) {
        return getNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public SimpleNodeDTO getNodeByIdForAccount(AccountDTO account, String id, Current current) {
        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        if (account != null) query.setUserId(account.getId());

        SimpleNodeDTO node = getNodeInfo(query);

        log.info("===>getNodeByIdForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    @Override
    public SimpleNodeDTO getNodeById(String id, Current current) {
        return getNodeByIdForAccount(userService.getCurrent(current),id,current);
    }

    @Override
    public SimpleNodeDTO getNodeByIdForCurrent(String id, Current current) {
        return getNodeByIdForAccount(userService.getCurrent(current),id,current);
    }

    private SimpleNodeDTO getNodeInfo(QueryNodeDTO query){
        assert (query != null);
        SimpleNodeDTO node = null;
        if ((query.getId() == null) && (StringUtils.isRootPath(query.getPath()))){
            node = new SimpleNodeDTO();
            node.setName(StringUtils.SPLIT_PATH);
            node.setPath(StringUtils.SPLIT_PATH);
            node.setIsDirectory(true);
            node.setIsReadOnly(true);
        } else {
            node = storageDao.getNodeInfo(query);
        }
        return node;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPNodeIdForAccount(AccountDTO account, String pid, Current current) {
        if (pid == null) return listRootNodeForAccount(account,current);

        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(pid);
        query.setUserId(account.getId());

        List<SimpleNodeDTO> nodeList = storageDao.listSubNode(query);

        log.info("===>listSubNodeByPNodeIdForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return nodeList;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPNodeId(String pid, Current current) {
        return listSubNodeByPNodeIdForAccount(userService.getCurrent(current),pid,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPNodeIdForCurrent(String pid, Current current) {
        return listSubNodeByPNodeIdForAccount(userService.getCurrent(current),pid,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPathForAccount(AccountDTO account, String path, Current current) {
        path = StringUtils.formatPath(path);
        if (StringUtils.isEmpty(path) || StringUtils.isSame(StringUtils.SPLIT_PATH,path)) return listRootNodeForAccount(account,current);

        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        path = StringUtils.formatPath(path);
        query.setPath(path);
        if (account != null) query.setUserId(account.getId());
        List<SimpleNodeDTO> list = storageDao.listSubNode(query);

        log.info("===>listSubNodeByPathForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return list;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPath(String path, Current current) {
        return listSubNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPathForCurrent(String path, Current current) {
        return listSubNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    public boolean moveNodeForAccount(AccountDTO account,String oldPath,String newPath,Current current){
        assert (oldPath != null) && (newPath != null);

        long t = System.currentTimeMillis();

        int n = 0;

        StorageQueryDTO dstQuery = new StorageQueryDTO();
        if (account != null) dstQuery.setUserId(account.getId());
        newPath = StringUtils.formatPath(newPath);
        dstQuery.setPath(newPath);
        dstQuery = BeanUtils.cleanProperties(dstQuery);
        StorageEntityUnionDTO dstUnion = storageDao.selectStorageEntityUnion(dstQuery);
        if (dstUnion == null) {
            StorageQueryDTO srcQuery = new StorageQueryDTO();
            srcQuery.setPath(StringUtils.formatPath(oldPath));
            srcQuery = BeanUtils.cleanProperties(srcQuery);
            StorageEntityUnionDTO srcUnion = storageDao.selectStorageEntityUnion(srcQuery);
            if (srcUnion != null) {
                List<StorageFileEntity> fileList = null;
                if (StorageConst.isFileType(srcUnion.getTypeId())){
                    assert (srcUnion.getFileEntity() != null);
                    fileList = new ArrayList<>();
                    fileList.add(srcUnion.getFileEntity());
                } else {
                    fileList = storageFileDao.listFileEntityByPath(srcUnion.getPath());
                }

                if (fileList != null){
                    String dstScope = StringUtils.getDirName(newPath);
                    String dstKey = StringUtils.getFileName(newPath);
                    for (StorageFileEntity file : fileList) {
                        FileDTO src = new FileDTO();
                        src.setScope(file.getFileScope());
                        src.setKey(file.getFileKey());
                        FileDTO dst = new FileDTO();
                        dst.setScope(dstScope);
                        dst.setKey(dstKey);
                        FileDTO realFile = fileService.moveFile(src, dst, current);
                        if ((realFile != null) && (!StringUtils.isEmpty(realFile.getKey()))) {
                            file.setFileScope(realFile.getScope());
                            file.setFileKey(realFile.getKey());
                        } else {
                            file.setFileScope(null);
                            file.setFileKey(null);
                        }
                        file.setUploadScope(null);
                        file.setUploadKey(null);
                        file.update();
                        n += storageFileDao.updateExactById(file,file.getId());
                    }
                }

                dstQuery.setUserId(null);
                FullNodeDTO rootNode = storageDao.getNearFullNodeInfo(dstQuery);
                if (rootNode != null) {
                    SimpleNodeDTO basicNode = rootNode.getBasic();
                    assert (basicNode != null);
                    srcUnion.setPid(StringUtils.left(basicNode.getId(),StringUtils.DEFAULT_ID_LENGTH));
                    srcUnion.setPidTypeId(basicNode.getTypeId());
                    newPath = StringUtils.substring(newPath,
                            StringUtils.length(basicNode.getPath()) + StringUtils.SPLIT_PATH.length());
                    if (!StringUtils.isEmpty(rootNode.getStoragePath())) {
                        newPath = rootNode.getStoragePath() + StringUtils.SPLIT_PATH + newPath;
                    }
                    srcUnion.setPath(newPath);
                    srcUnion.setTaskId((StringUtils.isEmpty(rootNode.getTaskId()) ? rootNode.getIssueId() : rootNode.getTaskId()));
                    if (StorageConst.isFileType(srcUnion.getTypeId())){
                        srcUnion.setTypeId(StorageConst.getFileType(basicNode.getTypeId()));
                    } else {
                        srcUnion.setTypeId(StorageConst.getPathType(basicNode.getTypeId()));
                    }
                } else {
                    srcUnion.setPid(null);
                    srcUnion.setPidTypeId(null);
                    srcUnion.setPath(newPath);
                    srcUnion.setTaskId(null);
                    if (StorageConst.isFileType(srcUnion.getTypeId())){
                        srcUnion.setTypeId(StorageConst.STORAGE_NODE_TYPE_UNKNOWN);
                    } else {
                        srcUnion.setTypeId(StorageConst.STORAGE_NODE_TYPE_DIR_UNKNOWN);
                    }
                }
                srcUnion.setNodeName(StringUtils.getFileName(newPath));
                srcUnion.update();
                n += storageDao.updateExactById(srcUnion, srcUnion.getId());
                n += storageDao.updateParentPath(oldPath, newPath);
            }
        }

        log.info("===>moveNodeForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return (n > 0);
    }

    @Override
    public boolean moveNode(String oldPath, String newPath, Current current) {
        return moveNodeForAccount(userService.getCurrent(current),oldPath,newPath,current);
    }

    @Override
    @Deprecated
    public List<SimpleNodeDTO> listSubNode(String path, Current current) {
        return listSubNodeByPathForCurrent(path,current);
    }

    @Override
    public CooperateDirNodeDTO getDirNodeInfo(String path, Current current) {
        return storageDao.getDirNodeInfoByPath(StringUtils.formatPath(path));
    }

    @Override
    public FileNodeDTO getFileNodeInfo(String path, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(StringUtils.formatPath(path));
        AccountDTO account = userService.getCurrent(current);
        if (account != null) query.setUserId(account.getId());
        return storageDao.getFileNodeInfo(query);
    }

    @Override
    public boolean lockNode(String path, String userId, Current current) {
        assert (path != null);
        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        if (!canBeLock(node,userId)) return false;

        node.setLockUserId(userId);
        int n = storageDao.updateById(node,node.getId());
        return (n > 0);
    }

    @Deprecated
    private boolean isFile(StorageEntity node){
        assert (node != null);
        return (StorageConst.isFileType(node.getTypeId()));
    }

    @Deprecated
    private boolean canBeLock(StorageEntity node,String userId) {
        return (node != null)
                && ((node.getLockUserId() == null)
                    || (StringUtils.isSame(node.getLockUserId(), userId)));
    }

    @Override
    @Deprecated
    public boolean unlockNode(String path, String userId, Current current) {
        assert (path != null);
        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        if (node == null) return false;
        if ((node.getLockUserId() != null) && (!StringUtils.isSame(node.getLockUserId(),userId))) return false;

        node.setLockUserId(null);
        int n = storageDao.updateExactById(node,node.getId());
        return (n > 0);
    }

    @Override
    @Deprecated
    public boolean isLocking(String path, Current current) {
        assert (path != null);
        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        return isLocking(node,current);
    }
    @Deprecated
    private boolean isLocking(StorageEntity node, Current current){
        if (node == null) return false;
        Boolean locking = false;
        if (node.getLockUserId() != null) {
            locking = true;
        }
        return locking;
    }

    @Override
    public boolean canBeDeleted(String path, Current current) {
        assert (path != null);
        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        return canBeDeleted(node,current);
    }

    private boolean canBeDeleted(StorageEntity node, Current current){
        Boolean isSafe = true;
        if (node == null) {
            isSafe = false;
        } else if (isLocking(node,current)) {
            isSafe = false;
        } else if (!StorageConst.isFileType(node.getTypeId())){
            StorageFileEntity fileNode = storageFileDao.selectById(node.getId());
            if ((fileNode.getUploadScope() != null) || (fileNode.getUploadKey() != null)) isSafe = false;
        } else {
            if (!isDirectoryEmpty(node.getPath(),current)) isSafe = false;
        }
        return isSafe;
    }

    @Override
    public boolean setFileLengthForAccount(AccountDTO account, String path, long fileLength, Current current) {
        assert (path != null);
        assert (fileLength >= 0);

        long t = System.currentTimeMillis();

        int n = 0;

        StorageQueryDTO query = new StorageQueryDTO();
        query.setPath(StringUtils.formatPath(path));
        query = BeanUtils.cleanProperties(query);
        StorageEntityUnionDTO node = storageDao.selectStorageEntityUnion(query);
        if ((node != null) && (StorageConst.isFileType(node.getTypeId()))) {
            node.setFileLength(fileLength);
            n = storageDao.updateById(node, node.getId());

            StorageFileEntity fileEntity = node.getFileEntity();
            if (fileEntity != null) {
                if ((fileEntity.getFileScope() != null) && (fileEntity.getFileKey() != null)) {
                    FileDTO fileDTO = new FileDTO();
                    fileDTO.setScope(fileEntity.getFileScope());
                    fileDTO.setKey(fileEntity.getFileKey());
                    boolean isOk = fileService.setFileLength(fileDTO, fileLength, current);
                    assert (isOk);
                }
            }
        }

        log.info("===>setFileLengthForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return (n > 0);
    }

    @Override
    public boolean setFileLength(String path, long fileLength, Current current) {
        return setFileLengthForAccount(userService.getCurrent(current),path,fileLength,current);
    }

    @Override
    public boolean isDirectoryEmptyForAccount(AccountDTO account, String path, Current current) {
        long t = System.currentTimeMillis();

        Boolean isEmpty = false;

        path = StringUtils.formatPath(path);
        QueryNodeDTO query = new QueryNodeDTO();
        if (StringUtils.isRootPath(path)){
            if (account != null) query.setUserId(account.getId());
            isEmpty = (storageDao.hasRootChild(query) == null);
        } else {
            query.setPath(path);
            isEmpty = (storageDao.hasChild(query) == null);
        }

        log.info("===>isDirectoryEmptyForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return isEmpty;
    }

    @Override
    public boolean isDirectoryEmpty(String path, Current current) {
        return isDirectoryEmptyForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public SimpleNodeDTO getSimpleNodeInfo(String path, Current current) {
        return getNodeByPathForCurrent(path,current);
    }

    @Override
    @Deprecated
    public boolean changeNodeInfo(NodeModifyRequestDTO request, String nodeId, Current current) {
        return false;
    }

    private boolean lockNode(StorageEntity nodeEntity,String userId){
        if (!canBeLock(nodeEntity,userId)) return false;

        nodeEntity.setLockUserId(userId);
        int n = storageDao.updateById(nodeEntity,nodeEntity.getId());
        return (n > 0);
    }

    private boolean canBeDownload(StorageEntity nodeEntity, StorageFileEntity fileEntity){
        if (fileEntity == null) return false;
        if (fileEntity.getFileKey() == null) return false;
        return true;
    }

    @Override
    public FileRequestDTO requestDownloadByPath(String path, String userId, Current current) {
        assert (path != null);

        //获取节点信息
        path = StringUtils.formatPath(path);
        StorageEntity nodeEntity = storageDao.selectByPath(path);
        if (nodeEntity == null) return null;

        //建立获取下载参数的对象
        StorageFileEntity fileEntity = storageFileDao.selectById(nodeEntity.getId());
        assert (fileEntity != null);

        //从数据库内获取scope和key
        FileDTO fileDTO = new FileDTO();
        if ((fileEntity.getUploadKey() != null) && (StringUtils.isSame(nodeEntity.getLockUserId(),userId))){
            fileDTO.setScope(fileEntity.getUploadScope());
            fileDTO.setKey(fileEntity.getUploadKey());
        } else {
            fileDTO.setScope(fileEntity.getFileScope());
            fileDTO.setKey(fileEntity.getFileKey());
        }
        if (fileDTO.getKey() == null) return null;

        //获取下载参数
        //if (fileService == null) fileService = FileServiceImpl.getInstance();
        assert (fileService != null);
        FileRequestDTO fileRequestDTO = fileService.getDownloadRequest(fileDTO,FileServerConst.FILE_SERVER_MODE_DEFAULT_COM,null,current);
        fileRequestDTO.setId(nodeEntity.getId());
        return fileRequestDTO;
    }

    @Override
    public boolean closeFileForAccount(AccountDTO account, String path, Current current) {
        long t = System.currentTimeMillis();
        boolean isOk = true;
        StorageQueryDTO query = new StorageQueryDTO();
        query.setPath(StringUtils.formatPath(path));
        query = BeanUtils.cleanProperties(query);
        StorageEntityUnionDTO fileUnion = storageDao.selectStorageEntityUnion(query);
        if (isOwer(account,fileUnion)) {
            if (StringUtils.isEmpty(fileUnion.getLastModifyUserId())) fileUnion.setLastModifyUserId(account.getId());

            StorageFileEntity fileEntity = fileUnion.getFileEntity();
            if (!StringUtils.isEmpty(fileEntity.getUploadScope())) {
                fileEntity.setFileScope(fileEntity.getUploadScope());
                fileEntity.setUploadScope(null);
            }
            if (!StringUtils.isEmpty(fileEntity.getUploadKey())) {
                fileEntity.setFileKey(fileEntity.getUploadKey());
                fileEntity.setUploadKey(null);
            }
            FileDTO fileDTO = new FileDTO();
            fileDTO.setScope(fileEntity.getFileScope());
            fileDTO.setKey(fileEntity.getFileKey());
            fileUnion.setFileLength(fileService.getFileLength(fileDTO,null));

            int n = 0;
            n += storageFileDao.updateExactById(fileEntity,fileEntity.getId());
            n += storageDao.updateById(fileUnion,fileEntity.getId());
            isOk = (n > 0);
        }
        log.info("===>closeFileForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return isOk;
    }

    @Override
    public boolean closeFile(String path, Current current) {
        return closeFileForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public boolean closeFileForCurrent(String path, Current current) {
        return closeFileForAccount(userService.getCurrent(current),path,current);
    }

    private boolean isOwer(AccountDTO account,StorageEntity nodeEntity){
        assert (nodeEntity != null);
        return (StringUtils.isEmpty(nodeEntity.getLastModifyUserId()) ||
                ((account != null) && StringUtils.isSame(account.getId(),nodeEntity.getLastModifyUserId())));
    }

    private long getRealFileLength(String path){
        StorageEntity entity = storageDao.selectByPath(StringUtils.formatPath(path));
        assert (entity != null);
        StorageFileEntity fileEntity = storageFileDao.selectById(entity.getId());
        assert (fileEntity != null);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setScope(fileEntity.getFileScope());
        fileDTO.setKey(fileEntity.getFileKey());
        return fileService.getFileLength(fileDTO,null);
    }

    @Override
    public FileRequestDTO openFile(String path, Current current) {
        return openFileForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public FileRequestDTO openFileForCurrent(String path, Current current) {
        return openFileForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public FileRequestDTO openFileForAccount(AccountDTO account, String path, Current current) {
        assert (path != null);

        long t = System.currentTimeMillis();

        FileRequestDTO fileRequestDTO = null;

        //获取节点信息
        StorageQueryDTO query = new StorageQueryDTO();
        if (account != null) query.setUserId(account.getId());
        query.setPath(StringUtils.formatPath(path));
        query = BeanUtils.cleanProperties(query);
        StorageEntityUnionDTO fileUnion = storageDao.selectStorageEntityUnion(query);
        if (fileUnion != null) {
            StorageFileEntity fileEntity = fileUnion.getFileEntity();
            assert (fileEntity != null);

            FileDTO fileDTO = new FileDTO();
            if ((account == null) || !StringUtils.isSame(fileUnion.getLastModifyUserId(),account.getId())){
                fileDTO.setScope(fileEntity.getFileScope());
                fileDTO.setKey(fileEntity.getFileKey());
            } else {
                fileDTO.setScope(fileEntity.getUploadScope());
                if (StringUtils.isEmpty(fileDTO.getScope())) fileDTO.setScope(fileEntity.getFileScope());
                fileDTO.setKey(fileEntity.getUploadKey());
                if (StringUtils.isEmpty(fileDTO.getKey())) fileDTO.setKey(fileEntity.getFileKey());
            }

            if (StringUtils.isEmpty(fileDTO.getScope())) {
                fileDTO.setScope(StringUtils.getDirName(path));
            }
            if (StringUtils.isEmpty(fileDTO.getKey())) {
                fileDTO.setKey(StringUtils.getFileName(path));
            }

            //获取文件服务器连接信息
            short mode = FileServerConst.OPEN_MODE_READ_ONLY;
            if (isOwer(account,fileUnion)) {
                mode = FileServerConst.OPEN_MODE_READ_WRITE;
            }
            fileRequestDTO = fileService.getFileRequest(fileDTO, mode, current);
            ;

            //组装上传申请结果
            if ((fileRequestDTO != null) && (!StringUtils.isEmpty(fileRequestDTO.getKey()))) {
                fileRequestDTO.setId(fileUnion.getId());
                fileRequestDTO.setNodeId(fileUnion.getId());

                //如果实际文件有更新则保存
                if (isRealFileChanged(fileUnion.getFileEntity(),fileRequestDTO,isOwer(account,fileUnion))){
                    fileEntity = setRealFile(fileUnion.getFileEntity(),fileRequestDTO,isOwer(account,fileUnion));
                    fileEntity.resetLastModifyTime();
                    storageFileDao.updateById(fileEntity, fileEntity.getId());
                }
            }
        }

        log.info("===>openFileForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return fileRequestDTO;
    }

    private boolean isRealFileChanged(StorageFileEntity fileEntity,FileRequestDTO realFile,boolean isOwner){
        if (isOwner){
            assert (fileEntity != null) && (realFile != null);
            return (!StringUtils.isSame(fileEntity.getUploadScope(),realFile.getScope()))
                    || (!StringUtils.isSame(fileEntity.getUploadKey(),realFile.getKey()));
        } else {
            assert (fileEntity != null) && (realFile != null);
            return (!StringUtils.isSame(fileEntity.getFileScope(),realFile.getScope()))
                    || (!StringUtils.isSame(fileEntity.getFileKey(),realFile.getKey()));
        }
    }

    private StorageFileEntity setRealFile(StorageFileEntity fileEntity,FileRequestDTO realFile,boolean isOwner){
        if (isOwner){
            assert (fileEntity != null) && (realFile != null);
            fileEntity.setUploadScope(realFile.getScope());
            fileEntity.setUploadKey(realFile.getKey());
        } else {
            assert (fileEntity != null) && (realFile != null);
            fileEntity.setFileScope(realFile.getScope());
            fileEntity.setFileKey(realFile.getKey());
        }
        return fileEntity;
    }
    @Deprecated
    private boolean isSameScopeAndKey(FileDTO request,StorageFileEntity fileEntity){
        boolean b = StringUtils.isSame(request.getScope(), fileEntity.getFileScope());
        if (b) b = StringUtils.isSame(request.getKey(), fileEntity.getFileKey());
        return b;
    }

    private FileDTO getFileDtoByFileId(StorageFileEntity fileEntity,Boolean isCreator,String path){
        //设置fileServer功能调用参数
        FileDTO fileDTO = new FileDTO();
        fileDTO.setScope(fileEntity.getFileScope());
        if (StringUtils.isEmpty(fileDTO.getScope())) fileDTO.setScope(StringUtils.getDirName(path));
        fileDTO.setKey(fileEntity.getFileKey());
        if (StringUtils.isEmpty(fileDTO.getKey())) fileDTO.setKey(StringUtils.getFileName(path));
        return fileDTO;
    }

    @Override
    @Deprecated
    public FileRequestDTO requestUploadByPath(String path, String userId, Current current) {
        assert (path != null);

        //获取节点信息
        path = StringUtils.formatPath(path);
        StorageEntity nodeEntity = storageDao.selectByPath(path);
        if (nodeEntity == null) {
            CreateNodeRequestDTO request = new CreateNodeRequestDTO();
            request.setFullName(path);
            request.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_MAIN);
            request.setUserId(userId);
            String nodeId = createFile(request,current);
            if (StringUtils.isEmpty(nodeId)) return null;
            nodeEntity = storageDao.selectById(nodeId);
        }
        //锁定节点
        if (!canBeLock(nodeEntity,userId)) return null;
        nodeEntity.setLockUserId(userId);
        storageDao.updateExactById(nodeEntity,nodeEntity.getId());

        //获取文件信息
        StorageFileEntity fileEntity = storageFileDao.selectById(nodeEntity.getId());
        assert (fileEntity != null);

        //复制文件
        //设置fileServer功能调用参数
        FileDTO fileDTO = new FileDTO();
        if (fileEntity.getUploadKey() != null) {
            fileDTO.setScope(fileEntity.getUploadScope());
            fileDTO.setKey(fileEntity.getUploadKey());
        } else {
            fileDTO.setScope(fileEntity.getFileScope());
            fileDTO.setKey(fileEntity.getFileKey());
        }
        if (fileDTO.getKey() != null) {
            fileDTO.setKey(fileService.duplicateFile(fileDTO, current));
        } else {
            fileDTO.setKey(nodeEntity.getNodeName());
        }

        //组装上传申请结果
        FileRequestDTO fileRequestDTO = fileService.getUploadRequest(fileDTO, FileServerConst.FILE_SERVER_MODE_DEFAULT,null,current);
        fileRequestDTO.setId(nodeEntity.getId());
        fileRequestDTO.setNodeId(nodeEntity.getId());
        //保存上传的实际文件标志
        fileEntity.setUploadScope(fileRequestDTO.getScope());
        fileEntity.setUploadKey(fileRequestDTO.getKey());
        storageFileDao.updateById(fileEntity,fileEntity.getId());
        return fileRequestDTO;
    }

    @Override
    @Deprecated
    public FileRequestDTO requestUpload(CooperateFileDTO fileInfo, int mode, Current current) {
        assert (fileInfo != null);

        //补全参数
        if (fileInfo.getName() == null) fileInfo.setName(StringUtils.getFileName(fileInfo.getLocalFile()));

        //建立上传参数对象
        //返回前锁定记录
        //从数据库内获取树节点编号，scope和key
        //如果数据库内没有，scope使用当前日期生成，key使用文件名，如果有重名添加时间戳，并创建一条记录
        StorageEntity nodeEntity = null;
        StorageFileEntity fileEntity = null;
        FileDTO fileDTO = new FileDTO();
        if (!StringUtils.isEmpty(fileInfo.getId())) {
            //从数据库内读取文件所对应的scope和key
            fileEntity = storageFileDao.selectById(fileInfo.getId());
            assert (fileEntity != null);
            //锁定记录
            lockFile(fileEntity,current);
            //如果树节点缺失增加树节点
            if (StringUtils.isEmpty(fileInfo.getNodeId())){
                nodeEntity = new StorageEntity();
                nodeEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_MAIN);
                nodeEntity.setId(fileEntity.getId());
                storageDao.insert(nodeEntity);
                fileInfo.setNodeId(nodeEntity.getId());
            }
            //设置fileServer功能调用参数
            fileDTO.setScope(((fileEntity.getFileScope() != null) ? fileEntity.getFileScope() : StringUtils.getTimeStamp(StringUtils.DATE_STAMP_FORMAT)));
            fileDTO.setKey(((fileEntity.getFileKey() != null) ? fileEntity.getFileKey() : StringUtils.getTimeStamp(StringUtils.TIME_STAMP_FORMAT) + "_" + fileInfo.getName()));
        } else if (!StringUtils.isEmpty(fileInfo.getNodeId())) {
            //从数据库内读取文件所对应的scope和key
            nodeEntity = storageDao.selectById(fileInfo.getNodeId());
            assert ((nodeEntity != null) && (nodeEntity.getId() != null) && (StorageConst.STORAGE_NODE_TYPE_FILE_MAIN.equals(nodeEntity.getTypeId())));
            fileEntity = storageFileDao.selectById(nodeEntity.getId());
            assert (fileEntity != null);
            fileInfo.setId(fileEntity.getId());
            //锁定记录
            lockFile(fileEntity,current);
            //设置fileServer功能调用参数
            fileDTO.setScope(((fileEntity.getFileScope() != null) ? fileEntity.getFileScope() : StringUtils.getTimeStamp(StringUtils.DATE_STAMP_FORMAT)));
            fileDTO.setKey(((fileEntity.getFileKey() != null) ? fileEntity.getFileKey() : StringUtils.getTimeStamp(StringUtils.TIME_STAMP_FORMAT) + "_" + fileInfo.getName()));
        } else {
            fileEntity = new StorageFileEntity();
            storageFileDao.insert(fileEntity);
            fileInfo.setId(fileEntity.getId());
            nodeEntity = new StorageEntity();
            nodeEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_MAIN);
            nodeEntity.setId(fileEntity.getId());
            storageDao.insert(nodeEntity);
            fileInfo.setNodeId(nodeEntity.getId());
            fileDTO.setScope(StringUtils.getTimeStamp(StringUtils.DATE_STAMP_FORMAT));
            fileDTO.setKey(StringUtils.getTimeStamp(StringUtils.TIME_STAMP_FORMAT) + "_" + fileInfo.getName());
        }

        //获取上传参数
        //if (fileService == null) fileService = FileServiceImpl.getInstance();
        assert fileService != null;
        FileRequestDTO fileRequestDTO = fileService.getUploadRequest(fileDTO,mode,null,null);

        //补充字段并返回
        fileRequestDTO.setId(fileInfo.getId());
        fileRequestDTO.setNodeId(fileInfo.getNodeId());
        return fileRequestDTO;
    }

    @Override
    @Deprecated
    public FileRequestDTO requestDownload(CooperateFileDTO fileInfo, int mode, Current current) {
        assert (fileInfo != null);

        //补全参数
        if ((fileInfo.getId() == null) && (fileInfo.getNodeId() != null)){
            StorageEntity nodeEntity = storageDao.selectById(fileInfo.getNodeId());
            assert ((nodeEntity != null) && (nodeEntity.getId() != null) && (StorageConst.STORAGE_NODE_TYPE_FILE_MAIN.equals(nodeEntity.getTypeId())));
            fileInfo.setId(nodeEntity.getId());
        }
        assert (!StringUtils.isEmpty(fileInfo.getId()));

        //建立获取下载参数的对象
        //从数据库内获取scope和key
        FileDTO fileDTO = new FileDTO();
        StorageFileEntity fileEntity = storageFileDao.selectById(fileInfo.getId());
        assert (fileEntity != null);
        fileDTO.setScope(((fileEntity.getFileScope() != null) ? fileEntity.getFileScope() : StringUtils.getTimeStamp(StringUtils.DATE_STAMP_FORMAT)));
        fileDTO.setKey(((fileEntity.getFileKey() != null) ? fileEntity.getFileKey() : StringUtils.getTimeStamp(StringUtils.TIME_STAMP_FORMAT) + "_" + fileInfo.getName()));

        //获取下载参数
        //if (fileService == null) fileService = FileServiceImpl.getInstance();
        assert fileService != null;
        FileRequestDTO fileRequestDTO = fileService.getDownloadRequest(fileDTO,mode,null,null);
        fileRequestDTO.setId(fileInfo.getId());

        return fileRequestDTO;
    }

    @Override
    public NodeDTO getNodeInfo(CooperationQueryDTO query, Current current) {
        return getNodeInfo(getCooperateDirInfo(query,current));
    }
    private NodeDTO getNodeInfo(CooperateDirDTO dirInfo) {
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setNode(BeanUtils.createFrom(dirInfo.getNode(),SimpleNodeDTO.class));
        List<SimpleNodeDTO> nodeList = new ArrayList<>();
        if (dirInfo.getSubDirList() != null) {
            for (CooperateDirNodeDTO dirNodeDTO : dirInfo.getSubDirList()) {
                SimpleNodeDTO node = BeanUtils.createFrom(dirNodeDTO, SimpleNodeDTO.class);
                nodeList.add(node);
            }
        }
        if (dirInfo.getFileList() != null){
            for (CooperateFileDTO fileNodeDTO : dirInfo.getFileList()) {
                SimpleNodeDTO node = BeanUtils.createFrom(fileNodeDTO, SimpleNodeDTO.class);
                nodeList.add(node);
            }
        }
        nodeDTO.setSubNodeList(nodeList);
        return nodeDTO;
    }

    @Override
    @Deprecated
    public CooperateDirDTO getCooperateDirInfo(CooperationQueryDTO query, Current current) {
        assert (query != null);
        query = BeanUtils.cleanProperties(query);

        CooperateDirDTO dir = new CooperateDirDTO();
        //获取根目录信息
        if (query.getNodeId() == null){
            CooperateDirNodeDTO root = BeanUtils.cleanProperties(new CooperateDirNodeDTO());
            root.setName("/");
            root.setAliasName("/");
            dir.setNode(root);
        } else {
            String nodeId = query.getNodeId();
            dir.setNode(storageDao.getDirNodeInfoByNodeId(nodeId));
        }

        //获取子目录信息并填充子目录ID到查找目录列表
        List<String> dirIdList = new ArrayList<>();
        List<CooperateDirNodeDTO> subDirList = storageDao.listSubDir(query);
        if ((subDirList != null) && (!subDirList.isEmpty())) {
            dir.setSubDirList(subDirList);
            //暂时只获取本目录文件
//            for (CooperateDirNodeDTO subDir : dir.getSubDirList()) {
//                if (subDir.getId() != null) {
//                    dirIdList.add(subDir.getId());
//                }
//            }
        }

        //添加根目录ID到查找目录列表并获取文件信息
        assert (dir.getNode() != null);
        String rootId = dir.getNode().getId();
        if (rootId != null){
            dirIdList.add(rootId);
        }
        List<CooperateFileDTO> mainFileList = storageDao.listMainFile(dirIdList);
        if ((mainFileList != null) && (!mainFileList.isEmpty())) {
            dir.setFileList(mainFileList);
            //补充文件信息
            for (CooperateFileDTO file : dir.getFileList()){
                file.setNode(storageDao.getFileNodeInfoByNodeId(file.getNodeId()));
                file.setReferenceFileList(storageDao.listRelatedFile(file.getNodeId()));
                file.setVersionList(storageDao.listVersion(file.getNodeId()));
            }
        }

        return dir;
    }

    @Override
    public List<CooperateFileDTO> listFileLink(FileDTO fileDTO, Current current) {
        CooperationQueryDTO query = BeanUtils.cleanProperties(BeanUtils.createFrom(fileDTO,CooperationQueryDTO.class));
        List<CooperateFileDTO> list = storageDao.listFileByScopeAndKey(query);
        return ((list != null) && (!list.isEmpty())) ? list : null;
    }

    @Override
    public boolean modifyFileInfo(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public FileRequestDTO requestDownloadFromLast(CooperateFileDTO fileInfo, int mode, Current current) {
        return null;
    }

    @Override
    public CooperateFileDTO uploadCallback(Map<String, String> params, Current current) {
        return null;
    }

    @Override
    public void downloadCallback(Map<String, String> params, Current current) {

    }

    @Override
    public void finishUploadById(String nodeId, String userId, Current current) {
        assert (nodeId != null);
        //读取有关上传的信息
        StorageFileEntity fileEntity = storageFileDao.selectById(nodeId);
        assert (fileEntity != null);

        //通告文件服务器上传结束
//        fileService.finishUpload(null,current);

        //修改文件记录，保存已上传的文件
        fileEntity.setFileScope(fileEntity.getUploadScope());
        fileEntity.setFileKey(fileEntity.getUploadKey());
        fileEntity.setUploadScope(null);
        fileEntity.setUploadKey(null);
        fileEntity.setLastModifyAddress(StringUtils.getRemoteIp(current));
        storageFileDao.updateExactById(fileEntity,fileEntity.getId());

        //解锁节点
        StorageEntity nodeEntity = storageDao.selectById(fileEntity.getId());
        nodeEntity.setLockUserId(null);
        storageDao.updateExactById(nodeEntity,nodeEntity.getId());
    }

    @Override
    public void finishUpload(FileRequestDTO request, boolean succeeded, Current current) {
        //结束上传
        fileService.finishUpload(request,current);

        StorageFileEntity fileEntity = storageFileDao.selectById(request.getId());
        assert (fileEntity != null);
    }

    @Override
    public void finishDownload(FileRequestDTO fileInfo, boolean succeeded, Current current) {

    }

    @Override
    public boolean replaceFile(CooperateFileDTO fileInfo, FileDTO fileDTO, Current current) {
        return false;
    }

    @Override
    public boolean deleteNodeForAccount(AccountDTO account, String path, boolean force, Current current) {
        assert (path != null);

        long t = System.currentTimeMillis();

        int n = 0;

        StorageQueryDTO query = new StorageQueryDTO();
        path = StringUtils.formatPath(path);
        query.setPath(path);
        if (account != null) query.setUserId(account.getId());
        query = BeanUtils.cleanProperties(query);
        StorageEntityUnionDTO node = storageDao.selectStorageEntityUnion(query);
        if (node != null) {
            List<String> idList = (!StorageConst.isFileType(node.getTypeId()))
                    ? new ArrayList<>() : storageDao.listAllSubNodeIdByPath(node.getPath());
            idList.add(node.getId());
            n += storageDao.fakeDeleteById(idList);
            if (StorageConst.isFileType(node.getTypeId())) {
                StorageFileEntity fileNode = storageFileDao.selectById(node.getId());
                FileDTO fileDTO = new FileDTO();
                fileDTO.setScope(fileNode.getFileScope());
                fileDTO.setKey(fileNode.getFileKey());
                fileService.deleteFile(fileDTO,current);
                n += storageFileDao.fakeDeleteById(idList);
            } else {
                FileDTO dir = new FileDTO();
                dir.setScope(StringUtils.getDirName(node.getPath()));
                dir.setKey(node.getNodeName());
                fileService.deleteFile(dir,current);
            }
        }

        log.info("===>deleteNodeForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return (n > 0);
    }

    @Override
    public boolean deleteNode(String path, boolean force, Current current) {
        return deleteNodeForAccount(userService.getCurrent(current),path,force,current);
    }

    @Override
    @Deprecated
    public boolean deleteFile(CooperateFileDTO fileInfo, Current current) {
        //检查参数
        assert (fileInfo != null);

        //删除文件树节点记录
        int i = 0;
        if (fileInfo.getNodeId() != null) {
            i = storageDao.fakeDeleteById(fileInfo.getNodeId());
        } else if (fileInfo.getId() != null) {
            //todo 删除所有detailId为fileInfo.id的树节点
        }
        return (i > 0);
    }

    private SimpleNodeDTO getNearSimpleNodeInfo(StorageQueryDTO query,Current current){
        return getNearSimpleNodeInfoForAccount(userService.getCurrent(current),query,current);
    }

    private SimpleNodeDTO getNearSimpleNodeInfoForAccount(AccountDTO account,StorageQueryDTO query,Current current){
        assert (query != null);
        if (StringUtils.isEmpty(query.getUserId())) {
            if (account != null) query.setUserId(account.getId());
        }
        query.setPath(StringUtils.formatPath(query.getPath()));
        query = BeanUtils.cleanProperties(query);
        return storageDao.getNearSimpleNodeInfo(query);
    }

    @Override
    @Deprecated
    public String createFile(CreateNodeRequestDTO request, Current current) {
        SimpleNodeDTO node = createCustomerFile(request,current);
        assert (node != null);
        return node.getPath();
    }

    @Override
    @Deprecated
    public String createDirectory(CreateNodeRequestDTO request, Current current) {
        SimpleNodeDTO node = createCustomerDir(request,current);
        assert (node != null);
        return node.getPath();
    }

    private SimpleNodeDTO convertToSimpleNodeDTO(StorageEntity entity){
        SimpleNodeDTO node = BeanUtils.createFrom(entity,SimpleNodeDTO.class);
        node.setName(entity.getNodeName());
        if (entity.getCreateTime() != null) {
            node.setCreateTimeStamp(entity.getCreateTime().getTime());
            node.setCreateTimeText(StringUtils.getTimeStamp(entity.getCreateTime(), StringUtils.DEFAULT_STAMP_FORMAT));
        }
        if (entity.getLastModifyTime() != null) {
            node.setLastModifyTimeStamp(entity.getLastModifyTime().getTime());
            node.setLastModifyTimeText(StringUtils.getTimeStamp(entity.getLastModifyTime(), StringUtils.DEFAULT_STAMP_FORMAT));
        }
        if (entity.getTypeId() != null) {
            node.setIsDirectory(!StorageConst.isFileType(entity.getTypeId()));
            node.setIsProject(StorageConst.isProjectType(entity.getTypeId()));
            node.setIsTask(StorageConst.isTaskType(entity.getTypeId()) || StorageConst.isTaskType(entity.getTypeId()));
            node.setIsCommit(StorageConst.isCommitType(entity.getTypeId()));
            node.setIsHistory(StorageConst.isHistoryType(entity.getTypeId()));
            node.setTypeName(StorageConst.getTypeName(entity.getTypeId()));
        }
        return node;
    }

    @Override
    public ProjectDTO getProjectInfoByPath(String path, Current current){
        return getProjectInfoByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public ProjectDTO getProjectInfoByPathForAccount(AccountDTO account, String path, Current current){
        QueryNodeDTO query = new QueryNodeDTO();
        if (account != null) query.setUserId(account.getId());
        path = StringUtils.formatPath(path);
        assert (!StringUtils.isEmpty(path));
        query.setPath(path);
        SimpleNodeDTO node = storageDao.getNodeInfo(query);
        assert (node.getIsProject());
        return projectService.getProjectInfoById(node.getId(),current);
    }

    @Override
    public SimpleNodeDTO createStorageNode(CreateNodeRequestDTO request, Current current) {
        StorageEntity nodeEntity = createNodeEntity(request,current);
        SimpleNodeDTO dto = convertToSimpleNodeDTO(nodeEntity);
        dto.setPath(request.getPath());
        return dto;
    }

    @Override
    public String createNodeForAccount(AccountDTO account, CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        if (account != null) request.setUserId(account.getId());
        StorageEntity nodeEntity = createNodeEntity(request,current);
        return nodeEntity.getId();
    }

    @Override
    public String createNode(CreateNodeRequestDTO request, Current current) {
        return createNodeForAccount(userService.getCurrent(current),request,current);
    }

    private StorageEntityUnionDTO createNodeEntity(CreateNodeRequestDTO request, Current current){
        return createNodeEntityForAccount(userService.getCurrent(current),request,current);
    }

    private StorageEntityUnionDTO createNodeEntityForAccount(AccountDTO account, CreateNodeRequestDTO request, Current current){
        assert (request != null);
        //补充参数
        if (StringUtils.isEmpty(request.getPath())) request.setPath(request.getFullName());
        if (StringUtils.isEmpty(request.getPid())) request.setPid(request.getPNodeId());
        if (StringUtils.isEmpty(request.getUserId()) && (account != null)) request.setUserId(account.getId());

        long t = System.currentTimeMillis();

        StorageEntityUnionDTO nodeUnion = null;

        //把空字符串格式化为null
        request = BeanUtils.cleanProperties(request);

        //格式化路径
        assert (!StringUtils.isEmpty(request.getPath()));
        String path = StringUtils.formatPath(request.getPath());

        //获取要创建的目标的绝对路径
        if ((request.getPid() != null) && (!path.startsWith(StringUtils.SPLIT_PATH))) {
            SimpleNodeDTO pNode = getNodeByIdForCurrent(request.getPid(),current);
            if (pNode != null) {
                path = pNode.getPath() + StringUtils.SPLIT_PATH + path;
            } else {
                path = StringUtils.SPLIT_PATH + path;
            }
        }

        //查找起始节点并获取相对路径
        StorageQueryDTO query = new StorageQueryDTO();
        query.setTypeId(null);
        query.setPath(StringUtils.formatPath(path));
        query = BeanUtils.cleanProperties(query);
        FullNodeDTO rootNode = storageDao.getNearFullNodeInfo(query);
        if ((rootNode != null) && (rootNode.getBasic() != null) &&
                StringUtils.isSame(rootNode.getBasic().getPath(),path)){
            StorageQueryDTO unionQuery = new StorageQueryDTO();
            unionQuery.setId(rootNode.getBasic().getId());
            unionQuery = BeanUtils.cleanProperties(unionQuery);
            nodeUnion = storageDao.selectStorageEntityUnion(unionQuery);
        } else {
            String pid = null;
            Short pTypeId = StorageConst.STORAGE_NODE_TYPE_DIR_UNKNOWN;
            StringBuilder pathBuilder = new StringBuilder();
            if ((rootNode != null) && (rootNode.getBasic() != null)) {
                pid = rootNode.getBasic().getId();
                pTypeId = rootNode.getBasic().getTypeId();
                pathBuilder.append(rootNode.getStoragePath());
                path = StringUtils.substring(path,
                        StringUtils.length(rootNode.getBasic().getPath()) + StringUtils.SPLIT_PATH.length());
            }

            //创建节点
            assert (path != null);
            String[] nodeNameArray = path.split(StringUtils.SPLIT_PATH);
            List<StorageEntity> nodeList = new ArrayList<>();
            StorageFileEntity fileEntity = null;

            //建立中间路径节点
            for (int i = 0; i < nodeNameArray.length - 1; i++) {
                if (StringUtils.isEmpty(nodeNameArray[i])) continue;
                StorageEntity pathNode = BeanUtils.createFrom(request,StorageEntity.class);
                pathNode.reset();
                pathNode.setPid(StringUtils.left(pid, StringUtils.DEFAULT_ID_LENGTH));
                pathNode.setPidTypeId(pTypeId);
                if (pathBuilder.length() > 0) pathBuilder.append(StringUtils.SPLIT_PATH);
                pathBuilder.append(nodeNameArray[i]);
                pathNode.setPath(pathBuilder.toString());
                pathNode.setNodeName(nodeNameArray[i]);
                pathNode.setTypeId(StorageConst.getPathType(request.getTypeId()));
                pathNode.setLastModifyUserId(request.getUserId());
                pathNode.setLastModifyDutyId(request.getDutyId());
                pathNode.setFileLength(0L);
                if (rootNode != null) pathNode.setTaskId(rootNode.getTaskId());
                nodeList.add(pathNode);
                pid = pathNode.getId();
                pTypeId = pathNode.getTypeId();
            }

            //建立叶节点
            String nodeName = nodeNameArray[nodeNameArray.length - 1];
            if (pathBuilder.length() > 0) pathBuilder.append(StringUtils.SPLIT_PATH);
            pathBuilder.append(nodeName);
            StorageEntity nodeEntity = new StorageEntity();
            if (StorageConst.isFileType(request.getTypeId())) {
                fileEntity = BeanUtils.createFrom(request, StorageFileEntity.class);
                fileEntity.reset();
                fileEntity.setFileTypeId(request.getFileTypeId());
                fileEntity.setFileVersion(request.getFileVersion());
                fileEntity.setLastModifyUserId(request.getUserId());
                fileEntity.setLastModifyDutyId(request.getDutyId());
                nodeEntity.setId(fileEntity.getId());
                nodeEntity.setCreateTime(fileEntity.getCreateTime());
                nodeEntity.setLastModifyTime(fileEntity.getLastModifyTime());
                nodeEntity.setLastModifyUserId(fileEntity.getLastModifyUserId());
                nodeEntity.setLastModifyDutyId(fileEntity.getLastModifyDutyId());
            }
            nodeEntity.setPid(StringUtils.left(pid, StringUtils.DEFAULT_ID_LENGTH));
            nodeEntity.setPidTypeId(pTypeId);
            nodeEntity.setPath(pathBuilder.toString());
            nodeEntity.setNodeName(nodeName);
            nodeEntity.setTypeId(request.getTypeId());
            nodeEntity.setFileLength(request.getFileLength());
            if (rootNode != null) nodeEntity.setTaskId(rootNode.getTaskId());
            nodeList.add(nodeEntity);

            //把建立的节点添加到数据库
            if (fileEntity != null) storageFileDao.insert(fileEntity);
            if (nodeList.size() > 0) storageDao.insertList(nodeList);

            //返回节点
            nodeUnion = BeanUtils.createFrom(nodeEntity, StorageEntityUnionDTO.class);
            if (fileEntity != null) nodeUnion.setFileEntity(fileEntity);
        }

        log.info("===>createNodeEntity:" + (System.currentTimeMillis()-t) + "ms");
        return nodeUnion;
    }

    @Override
    @Deprecated
    public boolean initNodeInfo(CreateNodeRequestDTO request, Current current) {
        return false;
    }

    @Override
    public boolean deleteDirectory(String nodeId, boolean force, Current current) {
        assert (nodeId != null);
        CooperationQueryDTO query = BeanUtils.cleanProperties(new CooperationQueryDTO());
        query.setNodeId(nodeId);
        CooperateDirDTO dirInfo = getCooperateDirInfo(query,current);
        List<String> idList = new ArrayList<>();
        if ((dirInfo.getSubDirList() != null) && (!dirInfo.getSubDirList().isEmpty())){
            if (!force) return false;
            for (CooperateDirNodeDTO dirNode : dirInfo.getSubDirList()){
                if (dirNode.getId() != null){
                    idList.add(dirNode.getId());
                }
            }
        }
        if ((dirInfo.getFileList() != null) && (!dirInfo.getFileList().isEmpty())){
            if (!force) return false;
            for (CooperateFileDTO fileNode : dirInfo.getFileList()){
                if (fileNode.getNodeId() != null){
                    idList.add(fileNode.getNodeId());
                }
            }
        }
        idList.add(nodeId);
        int i = storageDao.fakeDeleteById(idList);
        return (i > 0);
    }

    @Override
    public CooperateFileDTO duplicateFile(CooperateFileDTO fileInfo, String path, Current current) {
        return null;
    }

    @Override
    public CooperateFileDTO createFileLink(CooperateFileDTO fileInfo, String path, Current current) {
        return null;
    }

    @Override
    public boolean duplicateDirectory(String path, String parent, Current current) {
        return false;
    }

    @Override
    public boolean restoreFile(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public boolean restoreDirectory(String path, Current current) {
        return false;
    }

    @Override
    public boolean lockFile(String fileId, String address, Current current) {
        return setFileLock(storageFileDao.selectById(fileId),true,address);
    }
    private void lockFile(StorageFileEntity fileEntity, Current current) {
            setFileLock(fileEntity, true);
    }

    @Override
    public boolean unlockFile(String fileId, Current current) {
        return setFileLock(storageFileDao.selectById(fileId),false);
    }
    private void unlockFile(StorageFileEntity fileEntity, Current current) {
        assert (current != null);
        String address = StringUtils.getRemoteIp(current);
        setFileLock(fileEntity,false,address);
    }

    private boolean setFileLock(StorageFileEntity fileEntity,Boolean locking,String address){
        assert ((fileEntity != null) && (!StringUtils.isEmpty(fileEntity.getId())));
        if (!StringUtils.isEmpty(address)) {
            fileEntity.setLastModifyAddress(address);
        }
        int i = storageFileDao.updateById(fileEntity,fileEntity.getId());
        return (i > 0);
    }
    private boolean setFileLock(StorageFileEntity fileEntity, Boolean locking){
        return setFileLock(fileEntity, locking, null);
    }

    @Override
    public boolean isFileLocking(String fileId, Current current) {
        return isFileLocking(storageFileDao.selectById(fileId));
    }
    private boolean isFileLocking(StorageFileEntity fileEntity){
        assert (fileEntity != null);
        return false;
    }

    @Override
    public long getUsage(StorageQueryDTO query, Current current) {
        return 0;
    }

    @Override
    @Deprecated
    public CooperateFileDTO getFileInfo(String nodeId, Current current) {
        CooperateFileDTO fileInfo = new CooperateFileDTO();
        fileInfo.setNode(storageDao.getFileNodeInfoByNodeId(fileInfo.getNodeId()));
        fileInfo.setReferenceFileList(storageDao.listRelatedFile(fileInfo.getNodeId()));
        fileInfo.setVersionList(storageDao.listVersion(fileInfo.getNodeId()));
        BeanUtils.copyProperties(fileInfo.getNode(),fileInfo);
        return fileInfo;
    }

    @Override
    public int getLinkCount(FileDTO fileDTO, Current current) {
        return 0;
    }

    @Override
    public String createVersion(CooperateFileDTO fileInfo, String version, Current current) {
        //检查参数
        assert ((fileInfo != null) && !StringUtils.isEmpty(fileInfo.getNodeId()));

        //补充参数
        String nodeId = fileInfo.getNodeId();
        if (StringUtils.isEmpty(fileInfo.getId())) {
            StorageEntity nodeEntity = storageDao.selectById(nodeId);
            fileInfo.setId(nodeEntity.getId());
        }

        //复制实体文件
        StorageFileEntity fileEntity = storageFileDao.selectById(fileInfo.getId());
        FileDTO fileDTO = BeanUtils.createFrom(fileEntity,FileDTO.class);
        String verKey = fileService.duplicateFile(fileDTO,current);
        fileEntity.reset();
        fileEntity.setFileKey(verKey);
        fileEntity.setFileVersion(version);
        storageFileDao.insert(fileEntity);
        StorageEntity nodeEntity = new StorageEntity();
        nodeEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_COMMIT_HIS);
        nodeEntity.setId(fileEntity.getId());
        nodeEntity.setPid(StringUtils.left(nodeId,StringUtils.DEFAULT_ID_LENGTH));
        String nodePath = nodeEntity.getId();
        if (nodeId != null){
            StorageEntity pNodeEntity = storageDao.selectById(nodeId);
            nodePath = pNodeEntity.getPath() + StringUtils.SPLIT_ID + nodePath;
        }
        nodeEntity.setPath(nodePath);
        storageDao.insert(nodeEntity);
        return nodeEntity.getPath();
    }

}
