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
import com.maoding.Storage.Dto.DesignTaskDTO;
import com.maoding.Storage.Dto.FileUnionDTO;
import com.maoding.Storage.Dto.QueryNodeDTO;
import com.maoding.Storage.Entity.StorageEntity;
import com.maoding.Storage.Entity.StorageFileEntity;
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
    public SimpleNodeDTO commitFile(CommitRequestDTO request, Current current) {
        return commitFileForAccount(userService.getCurrent(current),request,current);
    }

    @Override
    public SimpleNodeDTO commitFileForAccount(AccountDTO account, CommitRequestDTO request, Current current) {
        assert (request != null);
        request = BeanUtils.cleanProperties(request);

        //查找原始文件
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(StringUtils.formatPath(request.getPath()));
        query.setNodeId(request.getNodeId());
        FileUnionDTO fileUnionDTO = storageDao.selectFileEntityUnion(query);
        assert (fileUnionDTO != null);
        StorageEntity oldNodeEntity = BeanUtils.createFrom(fileUnionDTO,StorageEntity.class);
        StorageFileEntity oldFileEntity = fileUnionDTO.getFileEntity();

        //生成最新发布文件
        //查找目标目录
        SimpleNodeDTO commitRootNode = storageDao.getCommitRoot(query);
        assert (commitRootNode != null);
        String path = commitRootNode.getPath() + StringUtils.SPLIT_PATH + request.getMajor() +
                StringUtils.SPLIT_PATH + "第" + request.getCommitTimes() + "次发布" +
                StringUtils.SPLIT_PATH + oldNodeEntity.getNodeName();


        //创建目标文件
        CreateNodeRequestDTO createRequest = new CreateNodeRequestDTO();
        createRequest.setTypeId(oldNodeEntity.getTypeId());
        createRequest.setFullName(path);
        if (account != null) createRequest.setUserId(account.getId());
        createRequest.setFileTypeId(oldFileEntity.getFileTypeId());
        FileUnionDTO newNodeEntity = createNodeEntity(createRequest,current);
        assert (newNodeEntity != null);
        StorageEntity resultEntity = newNodeEntity;

        //复制原始文件
        FileDTO src = new FileDTO();
        src.setScope(oldFileEntity.getFileScope());
        src.setKey(oldFileEntity.getFileKey());
        FileDTO dst = new FileDTO();
        dst.setScope(StringUtils.getDirName(path));
        dst.setKey(StringUtils.getFileName(path));

        FileDTO realDst = fileService.copyFile(src,dst,current);

        StorageFileEntity newFileEntity = newNodeEntity.getFileEntity();
        newFileEntity.setFileScope(realDst.getScope());
        newFileEntity.setFileKey(realDst.getKey());
        storageFileDao.updateById(newFileEntity);

        //生成发布版本文件
        //创造版本目录
        path = commitRootNode.getPath() + StringUtils.SPLIT_PATH + request.getMajor() +
                StringUtils.SPLIT_PATH + "第" + request.getCommitTimes() + "次发布" +
                StringUtils.SPLIT_PATH + "历史版本";
        createRequest = new CreateNodeRequestDTO();
        createRequest.setTypeId(StorageConst.STORAGE_NODE_TYPE_DIR_BACK);
        createRequest.setFullName(path);
        if (account != null) createRequest.setUserId(account.getId());
        createNodeEntity(createRequest,current);

        //创建历史版本
        path += StringUtils.SPLIT_PATH + StringUtils.getFileName(oldNodeEntity.getNodeName())
                + StringUtils.SPLIT_NAME_PART + request.getCommitTimes() + StringUtils.getFileExt(oldNodeEntity.getNodeName());
        createRequest = new CreateNodeRequestDTO();
        createRequest.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_HIS);
        createRequest.setFullName(path);
        createRequest.setFileTypeId(oldFileEntity.getFileTypeId());
        if (account != null) createRequest.setUserId(account.getId());
        newNodeEntity = createNodeEntity(createRequest,current);

        //复制原始文件
        dst = new FileDTO();
        dst.setScope(StringUtils.getDirName(path));
        dst.setKey(StringUtils.getFileName(path));

        realDst = fileService.copyFile(src,dst,current);

        newFileEntity = storageFileDao.selectById(newNodeEntity.getId());
        assert (newFileEntity != null);
        newFileEntity.setFileScope(realDst.getScope());
        newFileEntity.setFileKey(realDst.getKey());
        storageFileDao.updateById(newFileEntity);

        return convertToSimpleNodeDTO(resultEntity);
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
    public SimpleNodeDTO getNodeByPathForCurrent(String path, Current current) {
        return getNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public SimpleNodeDTO getNodeByIdForAccount(AccountDTO account, String id, Current current) {
        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        query.setNodeId(id);
        query.setUserId(account.getId());

        SimpleNodeDTO node = getNodeInfo(query);

        log.info("===>getNodeByIdForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    @Override
    public SimpleNodeDTO getNodeByIdForCurrent(String id, Current current) {
        return getNodeByIdForAccount(userService.getCurrent(current),id,current);
    }

    private SimpleNodeDTO getNodeInfo(QueryNodeDTO query){
        assert (query != null);
        SimpleNodeDTO node = null;
        if ((query.getNodeId() == null) && (StringUtils.isRootPath(query.getPath()))){
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
        query.setNodeId(pid);
        query.setUserId(account.getId());

        List<SimpleNodeDTO> nodeList = storageDao.listSubNode(query);

        log.info("===>listSubNodeByPNodeIdForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return nodeList;
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
        query.setUserId(account.getId());
        List<SimpleNodeDTO> list = storageDao.listSubNode(query);

        log.info("===>listSubNodeByPathForAccount:" + (System.currentTimeMillis()-t) + "ms");
        return list;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPathForCurrent(String path, Current current) {
        return listSubNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public boolean moveNode(String oldPath, String newPath, Current current) {
        assert (oldPath != null) && (newPath != null);

        long t = System.currentTimeMillis();

        int n = 0;

        oldPath = StringUtils.formatPath(oldPath);
        newPath = StringUtils.formatPath(newPath);

        QueryNodeDTO query = new QueryNodeDTO();
        AccountDTO account = userService.getCurrent(current);
        if (account != null) query.setUserId(account.getId());
        query.setPath(newPath);
        SimpleNodeDTO targetNode = storageDao.getNodeInfo(query);
        if (targetNode == null) {
            query.setPath(oldPath);
            StorageEntity node = storageDao.selectByPath(oldPath);
            if (node != null) {
                String targetPath = StringUtils.getDirName(newPath);
                String targetName = StringUtils.getFileName(newPath);
                String targetPid = null;
                Short targetPTypeId = StorageConst.STORAGE_NODE_TYPE_DIR_USER;
                if (!StringUtils.isEmpty(targetPath)) {
                    query.setPath(targetPath);
                    SimpleNodeDTO targetPNode = storageDao.getNodeInfo(query);
                    if (targetPNode != null) {
                        targetPid = targetPNode.getId();
                        targetPTypeId = targetPNode.getTypeId();
                    }
                }
                node.setPid(targetPid);
                node.setPidTypeId(targetPTypeId);
                node.setNodeName(targetName);
                node.setPath(targetPath + StringUtils.SPLIT_PATH + targetName);
                if (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX){
                    StorageFileEntity fileEntity = storageFileDao.selectById(node.getId());
                    FileDTO src = new FileDTO();
                    src.setScope(fileEntity.getFileScope());
                    src.setKey(fileEntity.getFileKey());
                    FileDTO dst = new FileDTO();
                    dst.setScope(targetPath);
                    dst.setKey(targetName);
                    FileDTO targetFile = fileService.moveFile(src,dst,current);
                    if ((targetFile != null) && (!StringUtils.isEmpty(targetFile.getKey()))){
                        fileEntity.setFileScope(targetFile.getScope());
                        fileEntity.setFileKey(targetFile.getKey());
                        fileEntity.setUploadScope(null);
                        fileEntity.setUploadKey(null);
                        fileEntity.update();
                        n += storageFileDao.updateExactById(fileEntity,fileEntity.getId());
                    } else {
                        node.setFileLength(0L);
                    }
                } else if (StorageConst.STORAGE_NODE_TYPE_DIR_USER.equals(node.getTypeId())) {
                    List<String> idList = storageDao.listAllSubNodeIdByPath(oldPath);
                    if ((idList != null) && (!idList.isEmpty())) {
                        List<StorageFileEntity> fileList = storageFileDao.listFileEntity(idList);
                        for (StorageFileEntity file : fileList) {
                            FileDTO src = new FileDTO();
                            src.setScope(file.getFileScope());
                            src.setKey(file.getFileKey());
                            FileDTO dst = new FileDTO();
                            dst.setScope(newPath);
                            dst.setKey(file.getFileKey());
                            FileDTO targetFile = fileService.moveFile(src, dst, current);
                            if ((targetFile != null) && (!StringUtils.isEmpty(targetFile.getKey()))) {
                                file.setFileScope(targetFile.getScope());
                                file.setFileKey(targetFile.getKey());
                                file.update();
                                n += storageFileDao.updateById(file);
                            } else {
                                node.setFileLength(0L);
                            }
                        }
                    }
                }
                node.update();
                n += storageDao.updateExactById(node, node.getId());
                n += storageDao.updateParentPath(oldPath, newPath);
            }
        }

        log.info("===>moveNode:" + (System.currentTimeMillis()-t) + "ms");
        return (n > 0);
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

    private boolean isFile(StorageEntity node){
        assert (node != null);
        return (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX);
    }

    private boolean canBeLock(StorageEntity node,String userId) {
        return (node != null)
                && node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_DIR_MAX
                && ((node.getLockUserId() == null)
                    || (StringUtils.isSame(node.getLockUserId(), userId)));
    }

    @Override
    public boolean unlockNode(String path, String userId, Current current) {
        assert (path != null);
        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        if ((node == null) || (node.getTypeId() > StorageConst.STORAGE_NODE_TYPE_DIR_MAX)) return false;
        if ((node.getLockUserId() != null) && (!StringUtils.isSame(node.getLockUserId(),userId))) return false;

        node.setLockUserId(null);
        int n = storageDao.updateExactById(node,node.getId());
        return (n > 0);
    }

    @Override
    public boolean isLocking(String path, Current current) {
        assert (path != null);
        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        return isLocking(node,current);
    }
    private boolean isLocking(StorageEntity node, Current current){
        if (node == null) return false;
        Boolean locking = false;
        if (node.getTypeId() > StorageConst.STORAGE_NODE_TYPE_DIR_MAX) {
            locking = true;
        } else if (node.getLockUserId() != null) {
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
        } else if (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX){
            StorageFileEntity fileNode = storageFileDao.selectById(node.getId());
            if ((fileNode.getUploadScope() != null) || (fileNode.getUploadKey() != null)) isSafe = false;
        } else {
            if (!isDirectoryEmpty(node.getPath(),current)) isSafe = false;
        }
        return isSafe;
    }

    @Override
    public boolean setFileLength(String path, long fileLength, Current current) {
        assert (path != null);
        assert (fileLength >= 0);

        long t = System.currentTimeMillis();

        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        if ((node == null) || (node.getTypeId() > StorageConst.STORAGE_NODE_TYPE_FILE_MAX)) return false;
        node.setFileLength(fileLength);
        int n = storageDao.updateById(node,node.getId());

        StorageFileEntity fileEntity = storageFileDao.selectById(node.getId());
        if (fileEntity != null) {
            if ((fileEntity.getFileScope() != null) && (fileEntity.getFileKey() != null)) {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setScope(fileEntity.getFileScope());
                fileDTO.setKey(fileEntity.getFileKey());
                boolean isOk = fileService.setFileLength(fileDTO, fileLength, current);
                assert (isOk);
            }
        }

        log.info("===>setFileLength:" + (System.currentTimeMillis()-t) + "ms");
        return (n > 0);
    }

    @Override
    public boolean isDirectoryEmpty(String path, Current current) {
        long t = System.currentTimeMillis();

        Boolean isEmpty = false;

        path = StringUtils.formatPath(path);
        QueryNodeDTO query = new QueryNodeDTO();
        if (StringUtils.isRootPath(path)){
            AccountDTO account = userService.getCurrent(current);
            if (account != null) query.setUserId(account.getId());
            isEmpty = (storageDao.hasRootChild(query) == null);
        } else {
            query.setPath(path);
            isEmpty = (storageDao.hasChild(query) == null);
        }

        log.info("===>isDirectoryEmpty:" + (System.currentTimeMillis()-t) + "ms");
        return isEmpty;
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
    public boolean closeFileForCurrent(String path, Current current) {
        return closeFileForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public boolean closeFileForAccount(AccountDTO account, String path, Current current) {
        long t = System.currentTimeMillis();
        boolean isOk = true;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(StringUtils.formatPath(path));
        FileUnionDTO fileUnion = storageDao.selectFileEntityUnion(query);
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
    public FileRequestDTO openFileForCurrent(String path, Current current) {
        return openFileForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public FileRequestDTO openFileForAccount(AccountDTO account, String path, Current current) {
        assert (path != null);

        long t = System.currentTimeMillis();

        FileRequestDTO fileRequestDTO = null;

        //获取节点信息
        QueryNodeDTO query = new QueryNodeDTO();
        if (account != null) query.setUserId(account.getId());
        query.setPath(StringUtils.formatPath(path));
        FileUnionDTO fileUnion = storageDao.selectFileEntityUnion(query);
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
                fileDTO.setScope(StringUtils.getDirName(fileUnion.getPath()));
            }
            if (StringUtils.isEmpty(fileDTO.getKey())) {
                fileDTO.setKey(fileUnion.getNodeName());
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
            fileDTO.setScope(((fileEntity.getFileScope() != null) ? fileEntity.getFileScope() : StringUtils.getTimeStamp(StringUtils.DATA_STAMP_FORMAT)));
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
            fileDTO.setScope(((fileEntity.getFileScope() != null) ? fileEntity.getFileScope() : StringUtils.getTimeStamp(StringUtils.DATA_STAMP_FORMAT)));
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
            fileDTO.setScope(StringUtils.getTimeStamp(StringUtils.DATA_STAMP_FORMAT));
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
        fileDTO.setScope(((fileEntity.getFileScope() != null) ? fileEntity.getFileScope() : StringUtils.getTimeStamp(StringUtils.DATA_STAMP_FORMAT)));
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
    public boolean deleteNode(String path, boolean force, Current current) {
        assert (path != null);

        long t = System.currentTimeMillis();

        int n = 0;

        path = StringUtils.formatPath(path);
        StorageEntity node = storageDao.selectByPath(path);
        if (node != null) {
            List<String> idList = (StorageConst.STORAGE_NODE_TYPE_FILE_MAX >= node.getTypeId())
                    ? new ArrayList<>() : storageDao.listAllSubNodeIdByPath(node.getPath());
            idList.add(node.getId());
            n += storageDao.fakeDeleteById(idList);
            if (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX) {
                StorageFileEntity fileNode = storageFileDao.selectById(node.getId());
                FileDTO fileDTO = new FileDTO();
                fileDTO.setScope(fileNode.getFileScope());
                fileDTO.setKey(fileNode.getFileKey());
                fileService.deleteFile(fileDTO,current);
                n += storageFileDao.fakeDeleteById(idList);
            } else if (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_DIR_MAX){
                FileDTO dir = new FileDTO();
                dir.setScope(StringUtils.getDirName(node.getPath()));
                dir.setKey(node.getNodeName());
                fileService.deleteFile(dir,current);
            }
        }

        log.info("===>deleteNode:" + (System.currentTimeMillis()-t) + "ms");
        return (n > 0);
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

    @Override
    public SimpleNodeDTO createCustomerDir(CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        if (request.getTypeId() == StorageConst.STORAGE_NODE_TYPE_UNKNOWN) request.setTypeId(StorageConst.STORAGE_NODE_TYPE_DIR_USER);
        assert (StorageConst.STORAGE_NODE_TYPE_DIR_MIN <= request.getTypeId()) && (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_DIR_MAX);
        return createStorageNode(request,current);
    }

    @Override
    public SimpleNodeDTO createCustomerFile(CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        if (request.getTypeId() == StorageConst.STORAGE_NODE_TYPE_UNKNOWN) request.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_MAIN);
        assert (StorageConst.STORAGE_NODE_TYPE_FILE_MIN <= request.getTypeId()) && (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX);
        return createStorageNode(request,current);
    }

    @Override
    public String createFile(CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        if (request.getTypeId() == StorageConst.STORAGE_NODE_TYPE_UNKNOWN) request.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_MAIN);
        assert (StorageConst.STORAGE_NODE_TYPE_FILE_MIN <= request.getTypeId()) && (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX);
        return createNode(request,current);
    }

    @Override
    public String createDirectory(CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        if (request.getTypeId() == StorageConst.STORAGE_NODE_TYPE_UNKNOWN) request.setTypeId(StorageConst.STORAGE_NODE_TYPE_DIR_USER);
        assert (StorageConst.STORAGE_NODE_TYPE_DIR_MIN <= request.getTypeId()) && (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_DIR_MAX);
        return createNode(request,current);
    }

    private SimpleNodeDTO convertToSimpleNodeDTO(StorageEntity entity){
        SimpleNodeDTO node = BeanUtils.createFrom(entity,SimpleNodeDTO.class);
        node.setName(entity.getNodeName());
        node.setIsTaskDirectory(entity.getTypeId() > StorageConst.STORAGE_NODE_TYPE_FILE_MAX);
        node.setCreateTimeStamp(entity.getCreateTime().getTime());
        node.setCreateTimeText(StringUtils.getTimeStamp(entity.getCreateTime(),StringUtils.DEFAULT_STAMP_FORMAT));
        node.setLastModifyTimeStamp(entity.getLastModifyTime().getTime());
        node.setLastModifyTimeText(StringUtils.getTimeStamp(entity.getLastModifyTime(),StringUtils.DEFAULT_STAMP_FORMAT));
        if (StorageConst.STORAGE_NODE_TYPE_DIR_USER.equals(node.getTypeId())){
            node.setTypeName("用户目录");
        } else if (StorageConst.STORAGE_NODE_TYPE_FILE_MAIN.equals(node.getTypeId())){
            node.setTypeName("主文件");
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
        assert (node.getIsProjectDirectory());
        return projectService.getProjectInfoById(node.getId(),current);
    }

    @Override
    public SimpleNodeDTO createStorageNode(CreateNodeRequestDTO request, Current current) {
        StorageEntity nodeEntity = createNodeEntity(request,current);
        return convertToSimpleNodeDTO(nodeEntity);
    }

    @Override
    public String createNode(CreateNodeRequestDTO request, Current current) {
        StorageEntity nodeEntity = createNodeEntity(request,current);
        return nodeEntity.getId();
    }

    private FileUnionDTO createNodeEntity(CreateNodeRequestDTO request, Current current){
        long t = System.currentTimeMillis();
        assert (request != null);
        assert (request.getFullName() != null);

        //把空字符串格式化为null
        request = BeanUtils.cleanProperties(request);

        //补充参数
        if (StringUtils.isEmpty(request.getUserId())){
            AccountDTO accountDTO = userService.getCurrent(current);
            if (accountDTO != null) request.setUserId(accountDTO.getId());
        }

        //格式化路径
        String fullName = StringUtils.formatPath(request.getFullName());

        //获取要创建的目标的绝对路径
        if ((request.getPNodeId() != null) && (!fullName.startsWith(StringUtils.SPLIT_PATH))) {
            SimpleNodeDTO pNode = getNodeByIdForCurrent(request.getPNodeId(),current);
            if (pNode != null) {
                fullName = pNode.getPath() + StringUtils.SPLIT_PATH + fullName;
            }
        }

        //查找起始节点并获取相对路径
        QueryNodeDTO query = new QueryNodeDTO();
        query.setUserId(request.getUserId());
        query.setPath(fullName);
        SimpleNodeDTO rootNode = storageDao.getStorageNodeByRedundancyPath(query);
        if ((rootNode != null) && (StringUtils.isSame(rootNode.getPath(),fullName))){
            FileUnionDTO fileUnionDTO = BeanUtils.createFrom(rootNode,FileUnionDTO.class);
            StorageFileEntity fileEntity = storageFileDao.selectById(rootNode.getId());
            fileUnionDTO.setFileEntity(fileEntity);
            return fileUnionDTO;
        }
        String pid = null;
        Short pTypeId = StorageConst.STORAGE_NODE_TYPE_DIR_USER;
        StringBuilder pathBuilder = new StringBuilder();
        if (rootNode != null) {
            pid = rootNode.getId();
            pTypeId = rootNode.getTypeId();
            fullName = StringUtils.substring(fullName,StringUtils.length(rootNode.getPath()) + StringUtils.SPLIT_PATH.length());
            pathBuilder.append(rootNode.getPath());
        }

        //创建节点
        assert (fullName != null);
        String[] nodeNameArray = fullName.split(StringUtils.SPLIT_PATH);
        List<StorageEntity> nodeList = new ArrayList<>();
        List<StorageFileEntity> fileList = new ArrayList<>();

        //建立中间路径节点
        for (int i=0; i<nodeNameArray.length-1; i++){
            if (StringUtils.isEmpty(nodeNameArray[i])) continue;
            StorageEntity pathNode = BeanUtils.createFrom(request,StorageEntity.class);
            pathNode.reset();
            pathNode.setPid(pid);
            pathNode.setPidTypeId(pTypeId);
            pathBuilder.append(StringUtils.SPLIT_PATH).append(nodeNameArray[i]);
            pathNode.setPath(pathBuilder.toString());
            pathNode.setNodeName(nodeNameArray[i]);
            if (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX){
                pathNode.setTypeId(StorageConst.STORAGE_NODE_TYPE_DIR_USER);
            }
            nodeList.add(pathNode);
            pid = pathNode.getId();
            pTypeId = StorageConst.STORAGE_NODE_TYPE_DIR_USER;
        }

        //建立叶节点
        String nodeName = nodeNameArray[nodeNameArray.length-1];
        pathBuilder.append(StringUtils.SPLIT_PATH).append(nodeName);
        StorageEntity nodeEntity = new StorageEntity();
        if (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX) {
            StorageFileEntity fileEntity = BeanUtils.createFrom(request,StorageFileEntity.class);
            fileEntity.reset();
            fileEntity.setLastModifyUserId(request.getUserId());
            QueryNodeDTO taskQuery = new QueryNodeDTO();
            taskQuery.setPath(pathBuilder.toString());
            DesignTaskDTO taskInfo = storageDao.getDesignTaskInfo(taskQuery);
            if (taskInfo != null) {
                fileEntity.setTaskId(taskInfo.getId());
                fileEntity.setIssueId(taskInfo.getIssueId());
                fileEntity.setProjectId(taskInfo.getProjectId());
                fileEntity.setOrganizationId(taskInfo.getCompanyId());
            }
            fileList.add(fileEntity);
            BeanUtils.copyProperties(fileEntity,nodeEntity);
        }
        nodeEntity.setPid(pid);
        nodeEntity.setPidTypeId(pTypeId);
        nodeEntity.setPath(pathBuilder.toString());
        nodeEntity.setNodeName(nodeName);
        nodeEntity.setTypeId(request.getTypeId());
        nodeList.add(nodeEntity);

        //把建立的节点添加到数据库
        if (fileList.size() > 0) storageFileDao.insertList(fileList);
        if (nodeList.size() > 0) storageDao.insertList(nodeList);

        FileUnionDTO fileUnionDTO = BeanUtils.createFrom(nodeEntity,FileUnionDTO.class);
        if (fileList.size() > 0) fileUnionDTO.setFileEntity(fileList.get(0));
        log.info("===>createNodeEntity:" + (System.currentTimeMillis()-t) + "ms");
        return fileUnionDTO;
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
        nodeEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_HIS);
        nodeEntity.setId(fileEntity.getId());
        nodeEntity.setPid(nodeId);
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
