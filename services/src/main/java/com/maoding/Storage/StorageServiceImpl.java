package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.Const.StorageConst;
import com.maoding.FileServer.zeroc.FileDTO;
import com.maoding.FileServer.zeroc.FileRequestDTO;
import com.maoding.FileServer.zeroc.FileService;
import com.maoding.Storage.Dao.StorageDao;
import com.maoding.Storage.Dao.StorageDirDao;
import com.maoding.Storage.Dao.StorageFileDao;
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
            dir.setNode(storageDao.getDirNodeInfo(query.getNodeId()));
        }

        //获取子目录信息
        dir.setSubDirList(storageDao.listSubDir(query));

        //获取文件信息
        List<String> dirIdList = new ArrayList<>();
        assert (dir.getNode() != null);
        String rootId = dir.getNode().getId();
        if (rootId != null){
            dirIdList.add(rootId);
        }
        for (CooperateDirNodeDTO subDir : dir.getSubDirList()){
            if (subDir.getId() != null){
                dirIdList.add(subDir.getId());
            }
        }
        dir.setFileList(storageDao.listMainFile(dirIdList));

        //补充文件信息
        for (CooperateFileDTO file : dir.getFileList()){
            file.setNode(storageDao.getFileNodeInfo(file.getNodeId()));
            file.setReferenceFileList(storageDao.listRelatedFile(file.getNodeId()));
            file.setVersionList(storageDao.listVersion(file.getNodeId()));
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
    public boolean createDirectory(String path, Current current) {
        return false;
    }

    @Override
    public boolean deleteDirectory(String path, boolean force, Current current) {
        return false;
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
    public CooperateFileDTO getFileInfo(String fileId, Current current) {
        return null;
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
