package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.Const.StorageConst;
import com.maoding.FileServer.zeroc.FileDTO;
import com.maoding.FileServer.zeroc.FileRequestDTO;
import com.maoding.FileServer.zeroc.FileService;
import com.maoding.Storage.Dao.StorageDao;
import com.maoding.Storage.Dao.StorageDirDao;
import com.maoding.Storage.Dao.StorageFileDao;
import com.maoding.Storage.Entity.StorageDirEntity;
import com.maoding.Storage.Entity.StorageEntity;
import com.maoding.Storage.Entity.StorageFileEntity;
import com.maoding.Storage.zeroc.*;
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

    /** 同步方式获取业务接口代理对象 */
    public static StorageServicePrx getInstance(String adapterName) {
        StorageServiceImpl prx = new StorageServiceImpl();
        return prx.getServicePrx("StorageService",adapterName, StorageServicePrx.class,_StorageServicePrxI.class);
    }
    public static StorageServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
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
                nodeEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_MAIN_FILE);
                nodeEntity.setDetailId(fileEntity.getId());
                storageDao.insert(nodeEntity);
                fileInfo.setNodeId(nodeEntity.getId());
            }
            //设置fileServer功能调用参数
            fileDTO.setScope(((fileEntity.getFileScope() != null) ? fileEntity.getFileScope() : StringUtils.getTimeStamp(StringUtils.DATA_STAMP_FORMAT)));
            fileDTO.setKey(((fileEntity.getFileKey() != null) ? fileEntity.getFileKey() : StringUtils.getTimeStamp(StringUtils.TIME_STAMP_FORMAT) + "_" + fileInfo.getName()));
        } else if (!StringUtils.isEmpty(fileInfo.getNodeId())) {
            //从数据库内读取文件所对应的scope和key
            nodeEntity = storageDao.selectById(fileInfo.getNodeId());
            assert ((nodeEntity != null) && (nodeEntity.getDetailId() != null) && (StorageConst.STORAGE_NODE_TYPE_MAIN_FILE.equals(nodeEntity.getTypeId())));
            fileEntity = storageFileDao.selectById(nodeEntity.getDetailId());
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
            fileEntity.setLocking(true);
            storageFileDao.insert(fileEntity);
            fileInfo.setId(fileEntity.getId());
            nodeEntity = new StorageEntity();
            nodeEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_MAIN_FILE);
            nodeEntity.setDetailId(fileEntity.getId());
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
            assert ((nodeEntity != null) && (nodeEntity.getDetailId() != null) && (StorageConst.STORAGE_NODE_TYPE_MAIN_FILE.equals(nodeEntity.getTypeId())));
            fileInfo.setId(nodeEntity.getDetailId());
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
            dir.setNode(storageDao.getDirNodeInfoByNodeId(query.getNodeId()));
        }

        //获取子目录信息并填充子目录ID到查找目录列表
        List<String> dirIdList = new ArrayList<>();
        List<CooperateDirNodeDTO> subDirList = storageDao.listSubDir(query);
        if ((subDirList != null) && (!subDirList.isEmpty())) {
            dir.setSubDirList(subDirList);
            for (CooperateDirNodeDTO subDir : dir.getSubDirList()){
                if (subDir.getId() != null){
                    dirIdList.add(subDir.getId());
                }
            }
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
    public void finishUpload(FileRequestDTO request, boolean succeeded, Current current) {
        //结束上传
        fileService.finishUpload(request,current);

        if (succeeded) { //如果上传成功，修改文件记录，保存已上传的文件
            StorageFileEntity fileEntity = storageFileDao.selectById(request.getId());
            assert (fileEntity != null);
            if (fileEntity.getDownloading() <= 0) {
                fileEntity.setFileScope(request.getScope());
                fileEntity.setFileKey(request.getKey());
                fileEntity.setLastModifyAddress(StringUtils.getRemoteIp(current));
                fileEntity.setLocking(false);
            } else {
                fileEntity.setUploadedScope(request.getScope());
                fileEntity.setUploadedKey(request.getKey());
                fileEntity.setLastModifyAddress(StringUtils.getRemoteIp(current));
            }
            storageFileDao.updateById(fileEntity,fileEntity.getId());
        } else { //如果上传失败，删除已上传的文件，并解锁文件
            FileDTO fileDTO = BeanUtils.createFrom(request,FileDTO.class);
            fileService.deleteFile(fileDTO,current);
            unlockFile(request.getId(),current);
        }
    }

    @Override
    public void finishDownload(FileRequestDTO fileInfo, boolean succeeded, Current current) {

    }

    @Override
    public boolean replaceFile(CooperateFileDTO fileInfo, FileDTO fileDTO, Current current) {
        return false;
    }

    @Override
    public boolean deleteFile(CooperateFileDTO fileInfo, Current current) {
        return false;
    }

    @Override
    public String createFile(CreateNodeRequestDTO request, Current current) {
        assert (request != null);
        assert (request.getFullName() != null);

        StorageFileEntity fileEntity = BeanUtils.createFrom(request,StorageFileEntity.class);
        fileEntity.reset();
        fileEntity.setFileName(StringUtils.getFileName(request.getFullName()));
        fileEntity.setLastModifyAddress(StringUtils.getRemoteIp(current));
        fileEntity.setTypeId(request.getTypeId());
        storageFileDao.insert(fileEntity);
        String pNodeId = request.getPNodeId();
        StorageEntity pNodeEntity = null;
        if (!StringUtils.isEmpty(request.getPNodeId())) pNodeEntity = storageDao.selectById(pNodeId);
        String pathName = StringUtils.getDirName(request.getFullName());
        if (!StringUtils.isEmpty(pathName)) {
            if (pNodeEntity == null) pNodeEntity = new StorageEntity();
            request.setFullName(pathName);
            request.setTypeId(request.getDirTypeId());
            pNodeId = createDirectory(request, pNodeEntity, current);
        }
        StorageEntity nodeEntity = new StorageEntity();
        nodeEntity.setPid(pNodeId);
        nodeEntity.setDetailId(fileEntity.getId());
        nodeEntity.setTypeId(StorageConst.STORAGE_NODE_TYPE_MAIN_FILE);
        nodeEntity.setPath((pNodeEntity != null) ? (pNodeEntity.getPath() + "," + nodeEntity.getId()) : nodeEntity.getId());
        storageDao.insert(nodeEntity);
        return nodeEntity.getId();
    }

    private String createDirectory(CreateNodeRequestDTO request, StorageEntity nodeEntity, Current current) {
        assert (request != null);
        assert (request.getFullName() != null);

        String[] pathNodeArray = request.getFullName().replaceAll("\\\\","/").split("/");
        String pNodeId = request.getPNodeId();
        StringBuilder fullName = new StringBuilder();
        StringBuilder pathField = new StringBuilder();
        if (pNodeId != null) {
            StorageEntity node = storageDao.selectById(pNodeId);
            pathField.append(node.getPath());
            StorageDirEntity dir = storageDirDao.selectById(node.getDetailId());
            fullName.append(dir.getFullName());
        }

        CooperationQueryDTO query = BeanUtils.cleanProperties(new CooperationQueryDTO());
        for (int i=0; i<pathNodeArray.length; i++){
            query.setNodeName(pathNodeArray[i]);
            query.setPNodeId(pNodeId);
            CooperateDirNodeDTO dirNode = storageDao.getDirNodeInfo(query);
            if (dirNode == null) {
                for (int j=i; j<pathNodeArray.length; j++) {
                    if (fullName.length() > 0) fullName.append("/");
                    fullName.append(pathNodeArray[j]);
                    StorageDirEntity dir = BeanUtils.createFrom(request,StorageDirEntity.class);
                    dir.reset();
                    if (j < (pathNodeArray.length - 1)){
                        dir.setTypeId(request.getDirTypeId());
                    }
                    dir.setFullName(fullName.toString());
                    storageDirDao.insert(dir);
                    if (nodeEntity == null) nodeEntity = new StorageEntity();
                    nodeEntity.reset();
                    nodeEntity.setDetailId(dir.getId());
                    nodeEntity.setNodeName(pathNodeArray[j]);
                    nodeEntity.setTypeId(StorageConst.STORAGE_DIR_TYPE_USER);
                    nodeEntity.setPid(pNodeId);
                    if (pathField.length() > 0) pathField.append(",");
                    pathField.append(nodeEntity.getId());
                    nodeEntity.setPath(pathField.toString());
                    storageDao.insert(nodeEntity);
                    pNodeId = nodeEntity.getId();
                }
                break;
            } else {
                pNodeId = dirNode.getId();
                if (fullName.length() > 0) fullName.append("/");
                fullName.append(pathNodeArray[i]);
                if (pathField.length() > 0) pathField.append(",");
                pathField.append(dirNode.getId());
            }
        }
        return pNodeId;
    }
    @Override
    public String createDirectory(CreateNodeRequestDTO request, Current current) {
        return createDirectory(request,null,current);
    }

    @Override
    public boolean deleteDirectory(String nodeId, boolean force, Current current) {
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
        if (!fileEntity.getLocking()) {
            setFileLock(fileEntity, true);
        }
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
        fileEntity.setLocking(locking);
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
        return fileEntity.getLocking();
    }

    @Override
    public long getFree(CooperationQueryDTO query, Current current) {
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
    public CooperateFileDTO createVersion(CooperateFileDTO fileInfo, String version, Current current) {
        return null;
    }

}
