package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.Const.FileServerConst;
import com.maoding.Const.StorageConst;
import com.maoding.FileServer.zeroc.FileDTO;
import com.maoding.FileServer.zeroc.FileRequestDTO;
import com.maoding.FileServer.zeroc.FileService;
import com.maoding.Storage.Dao.StorageDao;
import com.maoding.Storage.Dao.StorageDirDao;
import com.maoding.Storage.Dao.StorageFileDao;
import com.maoding.Storage.Dto.QueryByPidAndNameDTO;
import com.maoding.Storage.Dto.QueryNodeDTO;
import com.maoding.Storage.Entity.StorageDirEntity;
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
    private StorageDirDao storageDirDao;

    @Autowired
    private StorageDao storageDao;

    @Autowired
    UserService userService;

    /** 同步方式获取业务接口代理对象 */
    public static StorageServicePrx getInstance(String adapterName) {
        StorageServiceImpl prx = new StorageServiceImpl();
        return prx.getServicePrx("StorageService",adapterName, StorageServicePrx.class,_StorageServicePrxI.class);
    }
    public static StorageServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
    public List<SimpleNodeDTO> listRootNodeForCurrent(Current current) {
        return listRootNodeForAccount(userService.getCurrent(current),current);
    }

    @Override
    public List<SimpleNodeDTO> listRootNodeForAccount(AccountDTO account, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setUserId(account.getId());

        List<SimpleNodeDTO> nodeList = new ArrayList<>();

        List<SimpleNodeDTO> projectList = storageDao.listProjectRootNode(query);
        nodeList.addAll(projectList);

        List<SimpleNodeDTO> storageList = storageDao.listStorageRootNode(query);
        nodeList.addAll(storageList);

//        List<SimpleNodeDTO> companyList = storageDao.listRootNodeOfCompany(query);

        return nodeList;
    }

    @Override
    public SimpleNodeDTO getNodeByPathForAccount(AccountDTO account, String path, Current current) {
        path = StringUtils.formatPath(path);
        if (StringUtils.isEmpty(path) || StringUtils.isSame(StringUtils.SPLIT_PATH,path)) {
            SimpleNodeDTO root = new SimpleNodeDTO();
            root.setId("0");
            root.setName(StringUtils.SPLIT_PATH);
            root.setPath(StringUtils.SPLIT_PATH);
            root.setIsDirectory(true);
            root.setIsReadOnly(true);
            return root;
        }

        QueryNodeDTO query = new QueryNodeDTO();
//        query.setUserId(account.getId());
        query.setPath(path);
        return getNode(query);
    }

    @Override
    public SimpleNodeDTO getNodeByPathForCurrent(String path, Current current) {
        return getNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public SimpleNodeDTO getNodeByIdForAccount(AccountDTO account, String id, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setNodeId(id);
//        query.setUserId(account.getId());
        return getNode(query);
    }

    @Override
    public SimpleNodeDTO getNodeByIdForCurrent(String id, Current current) {
        return getNodeByIdForAccount(userService.getCurrent(current),id,current);
    }

    private SimpleNodeDTO getNode(QueryNodeDTO query){
        SimpleNodeDTO node = storageDao.getProjectNode(query);
        if (node == null) node = storageDao.getTaskNode2(query);
        if (node == null) node = storageDao.getStorageNode(query);
        return node;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPNodeIdForAccount(AccountDTO account, String pid, Current current) {
        if (pid == null) return listRootNodeForAccount(account,current);

        QueryNodeDTO query = new QueryNodeDTO();
        query.setNodeId(pid);
        query.setUserId(account.getId());

        List<SimpleNodeDTO> nodeList = new ArrayList<>();

        List<SimpleNodeDTO> taskList = storageDao.listTaskSubNode2(query);
        nodeList.addAll(taskList);

        List<SimpleNodeDTO> storageList = storageDao.listStorageSubNode(query);
        nodeList.addAll(storageList);

        return nodeList;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPNodeIdForCurrent(String pid, Current current) {
        return listSubNodeByPNodeIdForAccount(userService.getCurrent(current),pid,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPathForAccount(AccountDTO account, String path, Current current) {
        path = StringUtils.formatPath(path);
        if (StringUtils.isEmpty(path) || StringUtils.isSame(StringUtils.SPLIT_PATH,path)) {
            return listRootNodeForAccount(account,current);
        }

        SimpleNodeDTO parent = getNodeByPathForAccount(account,path,current);
        if (parent == null) return null;

        return listSubNodeByPNodeIdForAccount(account,parent.getId(),current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPathForCurrent(String path, Current current) {
        return listSubNodeByPathForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public boolean moveNode(String oldPath, String newPath, Current current) {
        assert (oldPath != null);
        oldPath = StringUtils.formatPath(oldPath);
        newPath = StringUtils.formatPath(newPath);
        if (storageDao.selectByPath(newPath) != null) return false;
        StorageEntity node = storageDao.selectByPath(oldPath);
        if (node == null) return false;
        String targetPath = StringUtils.getDirName(newPath);
        String targetName = StringUtils.getFileName(newPath);
        String targetPid = null;
        if (!StringUtils.isEmpty(targetPath)) {
            StorageEntity targetPNode = storageDao.selectByPath(StringUtils.formatPath(targetPath));
            if (targetPNode == null) return false;
            targetPid = targetPNode.getId();
        }
        node.setPid(targetPid);
        node.setNodeName(targetName);
        node.setPath(targetPath + StringUtils.SPLIT_PATH + targetName);
        int n = storageDao.updateExactById(node,node.getId());
        if (!StringUtils.isSame(oldPath,node.getPath())) {
            n += storageDao.updateParentPath(oldPath, node.getPath());
        }
        return (n > 0);
    }

    @Override
    public List<SimpleNodeDTO> listSubNode(String path, Current current) {
        path = StringUtils.formatPath(path);
        if (StringUtils.isSame(path,StringUtils.SPLIT_PATH)) path = null;
        return storageDao.listSubNodeByPath(path);
    }

    @Override
    public CooperateDirNodeDTO getDirNodeInfo(String path, Current current) {
        return storageDao.getDirNodeInfoByPath(StringUtils.formatPath(path));
    }

    @Override
    public FileNodeDTO getFileNodeInfo(String path, Current current) {
        return storageDao.getFileNodeInfoByPath(StringUtils.formatPath(path));
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
            if (fileNode.getDownloading() > 0) isSafe = false;
            else if ((fileNode.getUploadedScope() != null) || (fileNode.getUploadedKey() != null)) isSafe = false;
        } else {
            if (!isDirectoryEmpty(node.getPath(),current)) isSafe = false;
        }
        return isSafe;
    }

    @Override
    public boolean setFileLength(String path, long fileLength, Current current) {
        assert (path != null);
        assert (fileLength >= 0);
        StorageEntity node = storageDao.selectByPath(StringUtils.formatPath(path));
        if ((node == null) || (node.getTypeId() > StorageConst.STORAGE_NODE_TYPE_FILE_MAX)) return false;
        node.setFileLength(fileLength);
        int n = storageDao.updateById(node,node.getId());
        return (n > 0);
    }

    @Override
    public boolean isDirectoryEmpty(String path, Current current) {
        path = StringUtils.formatPath(path);

        Integer cnt = 0;
        QueryNodeDTO query = new QueryNodeDTO();
        AccountDTO account = userService.getCurrent(current);
        if (account != null) query.setUserId(account.getId());
        query.setPath(path);

        if (StringUtils.isEmpty(path) || StringUtils.isSame(StringUtils.SPLIT_PATH,path)){
            cnt += storageDao.countProjectRootNode(query);
            cnt += storageDao.countStorageRootNode(query);
        } else {
            cnt += storageDao.countTaskSubNode2(query);
            cnt += storageDao.countStorageSubNode(query);
        }
        return (cnt < 0);
    }

    @Override
    public SimpleNodeDTO getSimpleNodeInfo(String path, Current current) {
        return getNodeByPathForCurrent(path,current);
    }

    @Override
    public boolean changeNodeInfo(NodeModifyRequestDTO request, String nodeId, Current current) {
        assert (request != null);
        request = BeanUtils.cleanProperties(request);

        StorageEntity nodeEntity = storageDao.selectById(nodeId);
        int i = 0;
        if (nodeEntity.getTypeId() >= StorageConst.STORAGE_NODE_TYPE_FILE_MAX){
            if (request.getName() != null) nodeEntity.setNodeName(request.getName());
            if (request.getPNodeId() != null) {
                StorageEntity pNodeEntity = storageDao.selectById(request.getPNodeId());
                if (pNodeEntity != null) {
                    nodeEntity.setPid(request.getPNodeId());
                    nodeEntity.setPath(pNodeEntity.getPath() + StringUtils.SPLIT_ID + nodeEntity.getId());
                    StorageDirEntity pdir = storageDirDao.selectById(pNodeEntity.getId());
                    StorageDirEntity dir = storageDirDao.selectById(nodeEntity.getId());
                    String path = (request.getName() != null) ? pdir.getFullName() + StringUtils.SPLIT_PATH + request.getName()
                            : pdir.getFullName() + StringUtils.SPLIT_PATH + nodeEntity.getNodeName();
                    dir.setFullName(path);
                    i += storageDirDao.updateById(dir, dir.getId());
                }
            }
            i += storageDao.updateById(nodeEntity,nodeEntity.getId());
        } else {
            if (request.getName() != null){
                StorageFileEntity file = storageFileDao.selectById(nodeEntity.getId());
                file.setFileName(request.getName());
                i += storageFileDao.updateById(file,file.getId());
            }
            if (request.getPNodeId() != null) {
                nodeEntity.setPid(request.getPNodeId());
                i += storageDao.updateById(nodeEntity, nodeEntity.getId());
            }
        }
        return (i > 0);
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
        if ((fileEntity.getUploadedKey() != null) && (StringUtils.isSame(nodeEntity.getLockUserId(),userId))){
            fileDTO.setScope(fileEntity.getUploadedScope());
            fileDTO.setKey(fileEntity.getUploadedKey());
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
        return true;
    }

    @Override
    public FileRequestDTO openFileForCurrent(String path, Current current) {
        return openFileForAccount(userService.getCurrent(current),path,current);
    }

    @Override
    public FileRequestDTO openFileForAccount(AccountDTO account, String path, Current current) {
        assert (path != null);
        //获取节点信息
        String nodeId = null;
        SimpleNodeDTO node = getNodeByPathForAccount(account,path,current);
        if (node == null) {
            CreateNodeRequestDTO request = new CreateNodeRequestDTO();
            request = BeanUtils.cleanProperties(request);
            request.setFullName(path);
            request.setTypeId(StorageConst.STORAGE_NODE_TYPE_FILE_MAIN);
            request.setFileTypeId(StorageConst.STORAGE_FILE_TYPE_UNKNOWN);
            nodeId = createFile(request,current);
        } else if (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX){
            nodeId = node.getId();
        }
        if (nodeId == null) return null;

        //获取文件信息
        StorageFileEntity fileEntity = storageFileDao.selectById(nodeId);
        assert (fileEntity != null);

        Boolean isCreator = StringUtils.isSame(account.getId(),fileEntity.getLastModifyUserId());
        FileDTO fileDTO = getFileDtoByFileId(fileEntity,isCreator,path);

        //组装上传申请结果
        FileRequestDTO fileRequestDTO = fileService.getUploadRequest(fileDTO, FileServerConst.FILE_SERVER_MODE_DEFAULT,null,current);
        fileRequestDTO.setId(nodeId);
        fileRequestDTO.setNodeId(nodeId);

        //保存上传的实际文件标志
        fileEntity.setFileScope(fileRequestDTO.getScope());
        fileEntity.setFileKey(fileRequestDTO.getKey());
        storageFileDao.updateById(fileEntity,fileEntity.getId());
        return fileRequestDTO;
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
        if (fileEntity.getUploadedKey() != null) {
            fileDTO.setScope(fileEntity.getUploadedScope());
            fileDTO.setKey(fileEntity.getUploadedKey());
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
        fileEntity.setUploadedScope(fileRequestDTO.getScope());
        fileEntity.setUploadedKey(fileRequestDTO.getKey());
        storageFileDao.updateById(fileEntity,fileEntity.getId());
        return fileRequestDTO;
    }

    @Override
    public FileRequestDTO requestUpload(CooperateFileDTO fileInfo, int mode, Current current) {
        assert (fileInfo != null);

        //补全参数
        if (fileInfo.getName() == null) fileInfo.setName(StringUtils.getFileName(fileInfo.getLocalFile()));

//        if (isFile(node)){
//            StorageFileEntity fileEntity = storageFileDao.selectById(node.getId());
//            assert (fileEntity != null);
//            FileDTO file = new FileDTO();
//            if (fileEntity.getUploadedKey() != null) {
//                file.setScope(fileEntity.getUploadedScope());
//                file.setKey(fileEntity.getUploadedKey());
//            } else {
//                file.setScope(fileEntity.getFileScope());
//                file.setKey(fileEntity.getFileKey());
//            }
//
//            String key = fileService.duplicateFile(file,current);
////            fileService.getUploadRequest()
//        }
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
            fileEntity.setFileName(fileInfo.getName());
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
        fileEntity.setFileScope(fileEntity.getUploadedScope());
        fileEntity.setFileKey(fileEntity.getUploadedKey());
        fileEntity.setUploadedScope(null);
        fileEntity.setUploadedKey(null);
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
        path = StringUtils.formatPath(path);
        StorageEntity node = storageDao.selectByPath(path);
        if (node == null) return false;
        int n = 0;
        if (force || canBeDeleted(path,current)){
            List<String> idList = storageDao.listAllSubNodeIdByPath(node.getPath());
            idList.add(node.getId());
            n += storageDao.fakeDeleteById(idList);
            if (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX) n += storageFileDao.fakeDeleteById(idList);
            else if (node.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_DIR_MAX) n += storageDirDao.fakeDeleteById(idList);
        }
        return (n > 0);
    }

    @Override
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
    public String createNode(CreateNodeRequestDTO request, Current current) {
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
        if (rootNode == null) rootNode = storageDao.getTaskNodeByRedundancyPath2(query);
        if (rootNode == null) rootNode = storageDao.getProjectNodeByRedundancyPath(query);
        if ((rootNode != null) && (StringUtils.isSame(rootNode.getPath(),fullName))) return rootNode.getId();
        String pid = null;
        Short pTypeId = StorageConst.STORAGE_NODE_TYPE_DIR_USER;
        StringBuilder path = new StringBuilder();
        if (rootNode != null) {
            pid = rootNode.getId();
            pTypeId = rootNode.getTypeId();
            fullName = StringUtils.substring(fullName,StringUtils.length(rootNode.getPath()) + StringUtils.SPLIT_PATH.length());
            path.append(rootNode.getPath());
        }

        //创建节点
        assert (fullName != null);
        String[] nodeNameArray = fullName.split(StringUtils.SPLIT_PATH);
        List<StorageEntity> nodeList = new ArrayList<>();
        List<StorageDirEntity> dirList = new ArrayList<>();
        List<StorageFileEntity> fileList = new ArrayList<>();

        //建立中间路径节点
        for (int i=0; i<nodeNameArray.length-1; i++){
            if (StringUtils.isEmpty(nodeNameArray[i])) continue;
            StorageDirEntity dir = BeanUtils.createFrom(request,StorageDirEntity.class);
            dir.reset();
            dirList.add(dir);
            StorageEntity pathNode = BeanUtils.createFrom(dir,StorageEntity.class);
            pathNode.setPid(pid);
            pathNode.setPidTypeId(pTypeId);
            path.append(StringUtils.SPLIT_PATH).append(nodeNameArray[i]);
            pathNode.setPath(path.toString());
            pathNode.setNodeName(nodeNameArray[i]);
            if (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX){
                pathNode.setTypeId(StorageConst.STORAGE_NODE_TYPE_DIR_USER);
            }
            nodeList.add(pathNode);
            pid = pathNode.getId();
            pTypeId = StorageConst.STORAGE_NODE_TYPE_DIR_USER;
        }

        //建立叶节点
        StorageEntity nodeEntity = new StorageEntity();
        if (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX) {
            StorageFileEntity fileEntity = BeanUtils.createFrom(request,StorageFileEntity.class);
            fileEntity.reset();
            fileEntity.setLastModifyUserId(request.getUserId());
            fileList.add(fileEntity);
            BeanUtils.copyProperties(fileEntity,nodeEntity);
        } else if (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_DIR_MAX) {
            StorageDirEntity dirEntity = BeanUtils.createFrom(request,StorageDirEntity.class);
            dirEntity.reset();
            dirEntity.setLastModifyUserId(request.getUserId());
            dirList.add(dirEntity);
            BeanUtils.copyProperties(dirEntity,nodeEntity);
        }
        nodeEntity.setPid(pid);
        nodeEntity.setPidTypeId(pTypeId);
        String nodeName = nodeNameArray[nodeNameArray.length-1];
        path.append(StringUtils.SPLIT_PATH).append(nodeName);
        nodeEntity.setPath(path.toString());
        nodeEntity.setNodeName(nodeName);
        nodeEntity.setTypeId(request.getTypeId());
        nodeList.add(nodeEntity);

        //把建立的节点添加到数据库
        if (fileList.size() > 0) storageFileDao.insertList(fileList);
        if (dirList.size() > 0) storageDirDao.insertList(dirList);
        if (nodeList.size() > 0) storageDao.insertList(nodeList);

        return nodeEntity.getId();
    }

    @Override
    public boolean initNodeInfo(CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        assert (request.getFullName() != null);

        //把空字符串格式化为null
        request = BeanUtils.cleanProperties(request);

        //分离路径
        String nodeName = StringUtils.getFileName(request.getFullName());
        String pathName = StringUtils.getDirName(request.getFullName());
        String[] pathNodeArray = pathName.split(StringUtils.SPLIT_PATH);

        //查找目录，没有则创建
        StringBuilder fullName = new StringBuilder();
        StringBuilder pathField = new StringBuilder();
        String pNodeId = request.getPNodeId();
        if (pNodeId != null) {
            StorageEntity pNode = storageDao.selectById(pNodeId);
            pathField.append(pNode.getPath());
            StorageDirEntity dir = storageDirDao.selectById(pNode.getId());
            fullName.append(dir.getFullName());
        }

        for (int i=0; i<pathNodeArray.length; i++){
            if (StringUtils.isEmpty(pathNodeArray[i])) continue;
            QueryByPidAndNameDTO query = new QueryByPidAndNameDTO();
            query.setName(pathNodeArray[i]);
            query.setPid(pNodeId);
            StorageEntity pathNode = storageDao.selectByPIdAndName(query);
            if (pathNode == null) {
                //创建子目录
                for (int j=i; j<pathNodeArray.length; j++) {
                    if (fullName.length() > 0) fullName.append("/");
                    fullName.append(pathNodeArray[j]);
                    StorageDirEntity dir = BeanUtils.createFrom(request,StorageDirEntity.class);
                    dir.reset();
                    dir.setFullName(fullName.toString());
                    dir.setTypeId(StorageConst.STORAGE_NODE_TYPE_UNKNOWN);
                    storageDirDao.insert(dir);
                    pathNode = new StorageEntity();
                    pathNode.setId(dir.getId());
                    pathNode.setNodeName(pathNodeArray[j]);
                    pathNode.setTypeId(StorageConst.STORAGE_NODE_TYPE_UNKNOWN);
                    pathNode.setPid(pNodeId);
                    if (pathField.length() > 0) pathField.append(",");
                    pathField.append(pathNode.getId());
                    pathNode.setPath(pathField.toString());
                    storageDao.insert(pathNode);
                    pNodeId = pathNode.getId();
                }
                break;
            } else {
                pNodeId = pathNode.getId();
                if (fullName.length() > 0) fullName.append("/");
                fullName.append(pathNodeArray[i]);
                if (pathField.length() > 0) pathField.append(",");
                pathField.append(pathNode.getId());
            }
        }

        //插入叶节点
        int n = 0;
        QueryByPidAndNameDTO query = new QueryByPidAndNameDTO();
        query.setName(nodeName);
        query.setPid(pNodeId);
        StorageEntity node = storageDao.selectByPIdAndName(query);
        if (node == null){
            String detailId = null;
            if (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_FILE_MAX) {
                StorageFileEntity fileEntity = BeanUtils.createFrom(request,StorageFileEntity.class);
                fileEntity.reset();
                fileEntity.setFileName(nodeName);
                fileEntity.setFileTypeId(StorageConst.STORAGE_NODE_TYPE_UNKNOWN);
                n += storageFileDao.insert(fileEntity);
                detailId = fileEntity.getId();
            } else if (request.getTypeId() <= StorageConst.STORAGE_NODE_TYPE_DIR_MAX) {
                if (fullName.length() > 0) fullName.append("/");
                fullName.append(nodeName);
                StorageDirEntity dirEntity = BeanUtils.createFrom(request,StorageDirEntity.class);
                dirEntity.reset();
                dirEntity.setFullName(fullName.toString());
                dirEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_UNKNOWN);
                n += storageDirDao.insert(dirEntity);
                detailId = dirEntity.getId();
            }
            node = new StorageEntity();
            node.setPid(pNodeId);
            node.setNodeName(nodeName);
            node.setTypeId(StorageConst.STORAGE_NODE_TYPE_UNKNOWN);
            node.setId(detailId);
            if (pathField.length() > 0) pathField.append(",");
            pathField.append(node.getId());
            node.setPath(pathField.toString());
            n += storageDao.insert(node);
        }
        return (n > 0);
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
