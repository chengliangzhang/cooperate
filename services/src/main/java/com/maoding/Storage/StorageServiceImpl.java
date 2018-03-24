package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.Common.CheckService;
import com.maoding.Common.ConstService;
import com.maoding.Common.zeroc.CustomException;
import com.maoding.Storage.Dao.*;
import com.maoding.Storage.Dto.StorageEntityUnionDTO;
import com.maoding.Storage.Entity.*;
import com.maoding.Storage.zeroc.*;
import com.maoding.User.zeroc.AccountDTO;
import com.maoding.CoreUtils.BeanUtils;
import com.maoding.CoreUtils.ObjectUtils;
import com.maoding.CoreUtils.StringUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private StorageDao storageDao;

    @Autowired
    private StorageTreeDao storageTreeDao;

    @Autowired
    private StorageFileDao storageFileDao;

    @Autowired
    private StorageFileHisDao storageFileHisDao;

    @Autowired
    private ElementDao elementDao;

    @Autowired
    private AnnotateDao annotateDao;

    @Autowired
    private SuggestionDao suggestionDao;

    @Override
    public EmbedElementDTO createEmbedElement(UpdateElementDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        ElementEntity entity = BeanUtils.createCleanFrom(request,ElementEntity.class);
        String id = entity.getId();
        elementDao.insert(entity);
        EmbedElementDTO result = BeanUtils.createCleanFrom(entity,EmbedElementDTO.class);

        log.info("\t----> createEmbedElement花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return result;
    }

    @Override
    public AnnotateDTO createAnnotate(UpdateAnnotateDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        AnnotateEntity entity = BeanUtils.createCleanFrom(request,AnnotateEntity.class);
        entity.reset();
        String id = entity.getId();
        annotateDao.insert(entity);
        AnnotateDTO result = BeanUtils.createCleanFrom(entity,AnnotateDTO.class);

        log.info("\t----> createAnnotate花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return result;
    }

    @Override
    public NodeFileDTO updateNodeFile(@NotNull NodeFileDTO src, UpdateNodeFileDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        StorageFileEntity fileEntity = storageFileDao.selectById(src.getId());
        if (fileEntity != null) {
            BeanUtils.copyCleanProperties(request, fileEntity);
            fileEntity.update();
            storageFileDao.update(fileEntity);
        }
//        NodeFileDTO result = BeanUtils.createCleanFrom(fileEntity,NodeFileDTO.class);
//        if (isMirrorInfoValid(request)){
//            StorageFileEntity mirrorEntity = BeanUtils.createCleanFrom(request, StorageFileEntity.class);
//            updateMirrorEntity(request,fileEntity.getId(),mirrorEntity);
//            mirrorEntity = storageFileDao.selectOne(mirrorEntity);
//
//            result.setReadOnlyMirrorKey(StringUtils.formatPath(mirrorEntity.getReadOnlyKey()));
//            result.setWritableMirrorKey(StringUtils.formatPath(mirrorEntity.getWritableKey()));
//        }

        log.info("\t----> updateNodeFile花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return null;
    }

    @Override
    public NodeFileDTO createNodeFile(UpdateNodeFileDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        StorageFileEntity fileEntity = BeanUtils.createCleanFrom(request,StorageFileEntity.class);
        storageFileDao.insert(fileEntity);
        NodeFileDTO result = BeanUtils.createCleanFrom(fileEntity,NodeFileDTO.class);
        if (isMirrorInfoValid(request)){
            StorageFileEntity mirrorEntity = BeanUtils.createCleanFrom(request, StorageFileEntity.class);
            updateMirrorEntity(request,fileEntity.getId(),mirrorEntity);
            storageFileDao.insert(mirrorEntity);
            result.setReadOnlyMirrorKey(StringUtils.formatPath(mirrorEntity.getReadOnlyKey()));
            result.setWritableMirrorKey(StringUtils.formatPath(mirrorEntity.getWritableKey()));
        }

        log.info("\t----> createNodeFile花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return result;

    }

    @Override
    public SuggestionDTO createSuggestion(UpdateSuggestionDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        SuggestionEntity entity = BeanUtils.createCleanFrom(request,SuggestionEntity.class);
        suggestionDao.insert(entity);
        SuggestionDTO result = BeanUtils.createCleanFrom(entity,SuggestionDTO.class);

        log.info("\t----> createSuggestion花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return result;
    }

    @Override
    public long summaryNodeLength(QuerySummaryDTO query, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        Long summaryLength = storageDao.summaryNodeLength(BeanUtils.cleanProperties(query));

        log.info("\t----> summaryNodeLength花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return summaryLength;
    }

    /**
     * @param src
     * @param request
     * @param current The Current object for the invocation.  @deprecated 尚未实现
     **/
    @Override
    public FullNodeDTO createMirror(@NotNull FullNodeDTO src, @NotNull UpdateNodeDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        CheckService.check((src.getBasic() != null)
                        && (!ConstService.isDirectoryType((new Short(src.getBasic().getTypeId())).toString()))
                        && isMirrorInfoValid(request));
        String fileId = StringUtils.left(src.getBasic().getId(),StringUtils.DEFAULT_ID_LENGTH);
        CheckService.check((StringUtils.isNotEmpty(fileId)));
        StorageFileEntity mirrorEntity = selectMirrorFileEntity(fileId, request.getMirrorTypeId(), request.getMirrorAddress(), request.getMirrorBaseDir());
        if (mirrorEntity == null) {
            mirrorEntity = BeanUtils.createCleanFrom(src.getBasic(), StorageFileEntity.class);
            updateMirrorEntity(request,fileId,mirrorEntity);
            storageFileDao.insert(mirrorEntity);
        } else {
            updateMirrorEntity(request,fileId,mirrorEntity);
            mirrorEntity.update();
            storageFileDao.update(mirrorEntity);
        }
        if (mirrorEntity != null) {
            NodeFileDTO file = src.getFileInfo();
            if (file != null) {
                file.setReadOnlyMirrorKey(StringUtils.formatPath(mirrorEntity.getReadOnlyKey()));
                file.setWritableMirrorKey(StringUtils.formatPath(mirrorEntity.getWritableKey()));
            }
        }

        log.info("\t----> createMirror花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return src;
    }

    @Override
    public FullNodeDTO getNodeInfo(@NotNull SimpleNodeDTO node, @NotNull QueryNodeInfoDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        FullNodeDTO fullNode = storageDao.getNodeDetailByNodeId(node.getId(),cleanQueryNodeInfo(request));
        if (fullNode == null) fullNode = new FullNodeDTO();
        fullNode.setBasic(node);

        log.info("\t----> getNodeInfo花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return fullNode;
    }

    private QueryNodeInfoDTO cleanQueryNodeInfo(@NotNull QueryNodeInfoDTO request){
        request.setFileQuery(BeanUtils.cleanProperties(request.getFileQuery()));
        return request;
    }

    @Override
    public List<SimpleNodeDTO> listOldNode(@NotNull QueryNodeDTO query, Current current) {
        return storageDao.listOldNode(BeanUtils.cleanProperties(query));
    }

    @Override
    public SimpleNodeDTO createNodeWithParent(SimpleNodeDTO parent, @NotNull UpdateNodeDTO request, Current current) {
        long t = System.currentTimeMillis();
        //只在申请中包含的参数
        String fullName = StringUtils.formatPath(request.getFullName());
        String nodeName = StringUtils.getFileName(fullName);
        String lastModifyUserId = request.getOwnerUserId();

        //可使用父节点信息的参数
        String pid = StringUtils.left(request.getPid(), StringUtils.DEFAULT_ID_LENGTH);
        String taskId = request.getTaskId();
        Short pTypeId = ConstService.STORAGE_NODE_TYPE_DIR_DESIGN;
        String storagePath = fullName;
        StorageEntityUnionDTO parentUnion = null;
        if (parent != null){
            pTypeId = parent.getTypeId();
            pid = StringUtils.left(parent.getId(), StringUtils.DEFAULT_ID_LENGTH);
            taskId = parent.getTaskId();
            QueryNodeDTO unionQuery = new QueryNodeDTO();
            unionQuery.setId(pid);
            parentUnion = storageDao.quickSelectStorageEntityUnion(BeanUtils.cleanProperties(unionQuery));
            if (parentUnion != null) {
                pTypeId = Short.parseShort(parentUnion.getTypeId());
                taskId = parentUnion.getTaskId();
            }
        }

        String dirName = StringUtils.getDirName(fullName);
        if (!StringUtils.isEmpty(dirName)){
            parentUnion = insertPathNodeList(parentUnion,request,dirName,pid,taskId,lastModifyUserId,pTypeId);
            if (parentUnion != null) {
                pid = parentUnion.getId();
                taskId = parentUnion.getTaskId();
                pTypeId = Short.parseShort(parentUnion.getTypeId());
                storagePath = parentUnion.getPath() + StringUtils.SPLIT_PATH + nodeName;
            }
        }

        //格式化节点类型
        Short typeId = request.getTypeId();
        if ((ConstService.STORAGE_NODE_TYPE_UNKNOWN.equals(typeId)) || (ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN.equals(typeId))) {
            if (ConstService.STORAGE_NODE_TYPE_UNKNOWN.equals(typeId)) {
                typeId = Short.parseShort(ConstService.getFileType(pTypeId.toString()));
            } else {
                typeId = Short.parseShort(ConstService.getPathType(pTypeId.toString()));
            }
        }

        StorageEntityUnionDTO nodeUnion = BeanUtils.createCleanFrom(request,StorageEntityUnionDTO.class);
        nodeUnion.setNodeName(StringUtils.getFileName(fullName));
        nodeUnion.setPath(storagePath);
        nodeUnion.setTypeId(typeId.toString());
        if (!StringUtils.isEmpty(taskId)) nodeUnion.setTaskId(taskId);
        if (StringUtils.isEmpty(pid)) pid = StringUtils.SPLIT_PATH;
        nodeUnion.setPid(pid);
        if(!StringUtils.isEmpty(lastModifyUserId)) nodeUnion.setLastModifyUserId(lastModifyUserId);
        if (!ConstService.isDirectoryType(typeId.toString())){
            StorageFileEntity fileEntity = BeanUtils.createCleanFrom(nodeUnion,StorageFileEntity.class);
            BeanUtils.copyCleanProperties(request,fileEntity);
            nodeUnion.setFileEntity(fileEntity);
            if (ConstService.isHistoryType(typeId.toString())){
                StorageFileHisEntity hisEntity = BeanUtils.createCleanFrom(nodeUnion,StorageFileHisEntity.class);
                BeanUtils.copyCleanProperties(request,hisEntity);
                hisEntity.setFileId(request.getMainFileId());
                nodeUnion.setHisEntity(hisEntity);
            }
        }
        insertStorageUnion(nodeUnion);

        String path = fullName;
        if (parent != null) path = StringUtils.formatPath(parent.getPath() + StringUtils.SPLIT_PATH + nodeUnion.getPath());
        log.info("===>createNodeWithParent:" + (System.currentTimeMillis()-t) + "ms");
        return convertToSimpleNodeDTO(nodeUnion,path);
    }

    private StorageFileEntity updateMirrorEntity(@NotNull UpdateNodeFileDTO request, @NotNull String fileId, @NotNull StorageFileEntity mirrorEntity){
        request = BeanUtils.cleanProperties(request);
        assert (isMirrorInfoValid(request));
        if (StringUtils.isEmpty(request.getFileTypeId())){
            mirrorEntity.setFileTypeId(ConstService.STORAGE_FILE_TYPE_MIRROR.toString());
        } else {
            mirrorEntity.setFileTypeId(request.getFileTypeId());
        }
        mirrorEntity.setServerTypeId(request.getMirrorTypeId());
        mirrorEntity.setServerAddress(request.getMirrorAddress());
        mirrorEntity.setBaseDir(request.getMirrorBaseDir());
        mirrorEntity.setReadOnlyKey(request.getReadOnlyMirrorKey());
        mirrorEntity.setWritableKey(request.getWritableMirrorKey());
        assert (StringUtils.isNotEmpty(fileId));
        mirrorEntity.setMainFileId(fileId);
        return mirrorEntity;
    }

    private StorageFileEntity updateMirrorEntity(@NotNull UpdateNodeDTO request, @NotNull String fileId, @NotNull StorageFileEntity mirrorEntity){
        UpdateNodeFileDTO updateFileRequest = BeanUtils.createCleanFrom(request,UpdateNodeFileDTO.class);
        updateMirrorEntity(updateFileRequest,fileId,mirrorEntity);
        if (StringUtils.isNotEmpty(request.getAccountId())) {
            mirrorEntity.setLastModifyUserId(request.getAccountId());
        }
        if (StringUtils.isNotEmpty(request.getAccountRoleId())) {
            mirrorEntity.setLastModifyRoleId(request.getAccountRoleId());
        }
        return mirrorEntity;
    }

    private StorageFileEntity selectMirrorFileEntity(@NotNull String fileId, Short serverTypeId, String serverAddress, String baseDir){
        StorageFileEntity query = new StorageFileEntity();
        query.clear();
        query.setServerTypeId(serverTypeId.toString());
        query.setServerAddress(serverAddress);
        query.setBaseDir(baseDir);
        query.setMainFileId(fileId);
        List<StorageFileEntity> list = storageFileDao.select(query);
        return (ObjectUtils.isNotEmpty(list)) ? list.get(0) : null;
    }

    private boolean isHisInfoValid(@NotNull UpdateNodeDTO request){
        return (!ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(request.getActionTypeId()))
                || (ObjectUtils.isNotEmpty(request.getRemark()));

    }

    private boolean isFileInfoValid(@NotNull UpdateNodeDTO request){
        return (!ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(request.getServerTypeId()))
                || (ObjectUtils.isNotEmpty(request.getServerAddress()))
                || (ObjectUtils.isNotEmpty(request.getReadOnlyKey()))
                || (ObjectUtils.isNotEmpty(request.getWritableKey()));
    }

    private boolean isMirrorInfoValid(@NotNull UpdateNodeDTO request) {
        return (ObjectUtils.isNotEmpty(request.getReadOnlyMirrorKey()))
                || (ObjectUtils.isNotEmpty(request.getWritableMirrorKey()));
    }

    private boolean isMirrorInfoValid(@NotNull UpdateNodeFileDTO request) {
        return (ObjectUtils.isNotEmpty(request.getReadOnlyMirrorKey()))
                || (ObjectUtils.isNotEmpty(request.getWritableMirrorKey()));
    }

    private StorageTreeEntity insertPathNodeList(SimpleNodeDTO parent, String path, String accountId, String accountRoleId) {
        //获取父节点
        StorageTreeEntity lastEntity = null;
        if (parent != null) {
            lastEntity = storageTreeDao.selectById(StringUtils.left(parent.getId(),StringUtils.DEFAULT_ID_LENGTH));
            while (StringUtils.isStartWith(path,StringUtils.SPLIT_PATH)) {
                path = StringUtils.substring(path,StringUtils.SPLIT_PATH.length());
            }
        }
        if (lastEntity != null) path = StringUtils.formatPath(lastEntity.getPath() + StringUtils.SPLIT_PATH + path);
        String taskId = (parent != null) ? parent.getTaskId() : null;
        if (StringUtils.isNotEmpty(taskId) && StringUtils.isNotEmpty(path)) {
            lastEntity = storageTreeDao.selectByTaskIdAndFuzzyPath(taskId, path);
        }
        if (lastEntity != null) {
            path = StringUtils.substring(path, StringUtils.length(lastEntity.getPath()) + StringUtils.SPLIT_PATH.length());
        }

        //如果目标路径为空直接返回，否则将其拆解为数组
        if (StringUtils.isEmpty(path)) return lastEntity;
        String[] nodeNameArray = path.split(StringUtils.SPLIT_PATH);

        //插入中间节点
        List<StorageTreeEntity> nodeList = new ArrayList<>();
        StringBuilder pathBuilder = new StringBuilder();
        if (lastEntity != null) {
            pathBuilder.append(lastEntity.getPath());
        }

        for (String nodeName : nodeNameArray) {
            if (StringUtils.isEmpty(nodeName)) continue;
            if (pathBuilder.length() > 0) pathBuilder.append(StringUtils.SPLIT_PATH);
            pathBuilder.append(nodeName);
            StorageTreeEntity entity = new StorageTreeEntity();
            entity.reset();
            if (lastEntity != null) {
                entity.setPid(lastEntity.getId());
                entity.setTypeId(ConstService.getPathType(lastEntity.getTypeId()));
                entity.setTaskId(lastEntity.getTaskId());
            } else if (parent != null){
                entity.setTypeId(ConstService.getPathType((new Short(parent.getTypeId())).toString()));
                entity.setTaskId(parent.getTaskId());
            } else { //添加根节点
                lastEntity = new StorageTreeEntity();
                lastEntity.reset();
                lastEntity.setPath(StringUtils.SPLIT_PATH);
                lastEntity.setNodeName(StringUtils.SPLIT_PATH);
                lastEntity.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN.toString());
                lastEntity.setLastModifyUserId(accountId);
                lastEntity.setLastModifyRoleId(accountRoleId);
                nodeList.add(BeanUtils.cleanProperties(lastEntity));
                pathBuilder = new StringBuilder(StringUtils.SPLIT_PATH).append(nodeName);

                entity.setPid(lastEntity.getId());
                entity.setTypeId(ConstService.getPathType(lastEntity.getTypeId()));
            }

            entity.setNodeName(nodeName);
            entity.setPath(StringUtils.formatPath(pathBuilder.toString()));
            entity.setOwnerUserId(accountId);
            entity.setLastModifyUserId(accountId);
            entity.setLastModifyRoleId(accountRoleId);
            nodeList.add(BeanUtils.cleanProperties(entity));
            lastEntity = entity;
        }
        if (!nodeList.isEmpty()) {
            int n = storageTreeDao.insertList(nodeList);
            assert (n > 0);
        }

        return lastEntity;
    }

    @Deprecated
    private StorageEntityUnionDTO insertPathNodeList(StorageEntityUnionDTO parentUnion,
                                                     @NotNull UpdateNodeDTO request,
                                                     @NotNull String dirName,
                                                     String pid,
                                                     String taskId,
                                                     String lastModifyUserId,
                                                     Short pTypeId){
        List<StorageTreeEntity> nodeList = new ArrayList<>();
        StringBuilder pathBuilder = new StringBuilder();
        String parentPath = "";
        if (parentUnion != null) {
            parentPath = parentUnion.getPath();
            pathBuilder.append(parentPath);
        }

        StorageEntityUnionDTO lastPathNode = null;
        String[] nodeNameArray = dirName.split(StringUtils.SPLIT_PATH);
        for (String nodeName : nodeNameArray) {
            if (StringUtils.isEmpty(nodeName)) continue;
            QueryNodeDTO query = new QueryNodeDTO();
            query.setPid(pid);
            query.setName(nodeName);
            lastPathNode = storageDao.quickSelectStorageEntityUnion(BeanUtils.cleanProperties(query));
            if (lastPathNode == null){
                lastPathNode = BeanUtils.createCleanFrom(request, StorageEntityUnionDTO.class);
                lastPathNode.setPid(pid);
                lastPathNode.setNodeName(nodeName);
                if (pathBuilder.length() > 0) pathBuilder.append(StringUtils.SPLIT_PATH);
                pathBuilder.append(nodeName);
                lastPathNode.setPath(pathBuilder.toString());
                lastPathNode.setTypeId(ConstService.getPathType(pTypeId.toString()));
                if (!StringUtils.isEmpty(lastModifyUserId)) lastPathNode.setLastModifyUserId(lastModifyUserId);
                lastPathNode.setFileLength(0L);
                if (!StringUtils.isEmpty(taskId)) lastPathNode.setTaskId(taskId);
                nodeList.add(lastPathNode);
            }
            pid = lastPathNode.getId();
        }
        if (!nodeList.isEmpty()) {
            int n = storageTreeDao.insertList(nodeList);
            assert (n > 0);
        }

        return lastPathNode;
    }


    @Override
    public SimpleNodeDTO updateNodeWithParent(@NotNull SimpleNodeDTO src, SimpleNodeDTO parent, UpdateNodeDTO request, Current current) {
        long t = System.currentTimeMillis();
        //找目标节点
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(StringUtils.left(src.getId(),StringUtils.DEFAULT_ID_LENGTH));
        StorageEntityUnionDTO srcUnion = storageDao.quickSelectStorageEntityUnion(BeanUtils.cleanProperties(query));
        assert (srcUnion != null);


        String fullName = null;
        String lastModifyUserId = null;
        Long fileLength = 0L;
        String pid = null;
        String taskId = null;
        Short typeId = src.getTypeId();
        //申请变动属性
        if (request != null){
            lastModifyUserId = request.getOwnerUserId();
            fullName = StringUtils.formatPath(request.getFullName());
            fileLength = request.getFileLength();
            pid = StringUtils.left(request.getPid(), StringUtils.DEFAULT_ID_LENGTH);
            taskId = request.getTaskId();
            typeId = request.getTypeId();
        }
        if (!StringUtils.isEmpty(fullName)) {
            srcUnion.setNodeName(StringUtils.getFileName(fullName));
        }

        //父节点变动属性
        StorageEntityUnionDTO parentUnion = null;
        StringBuilder storagePathBuilder = new StringBuilder();
        if (!isSameParent(src,parent,request)){
            pid = StringUtils.left(parent.getId(), StringUtils.DEFAULT_ID_LENGTH);
            taskId = parent.getTaskId();
            QueryNodeDTO unionQuery = new QueryNodeDTO();
            unionQuery.setId(pid);
            parentUnion = storageDao.quickSelectStorageEntityUnion(BeanUtils.cleanProperties(unionQuery));
            Short pTypeId = parent.getTypeId();
            String dirName = StringUtils.getDirName(fullName);
            if (!StringUtils.isEmpty(dirName)){
                parentUnion = insertPathNodeList(parentUnion,request,dirName,pid,taskId,lastModifyUserId,pTypeId);
            }
            if (parentUnion != null) {
                pid = parentUnion.getId();
                storagePathBuilder.append(parentUnion.getPath());
                pTypeId = Short.parseShort(parentUnion.getTypeId());
            } else {
                pTypeId = parent.getTypeId();
                storagePathBuilder.append(parent.getStoragePath());
            }
            if (ConstService.isDirectoryType((new Short(src.getTypeId())).toString())) {
                typeId = Short.parseShort(ConstService.getPathType(pTypeId.toString()));
            } else {
                typeId = Short.parseShort(ConstService.getFileType(pTypeId.toString()));
            }
        } else {
            storagePathBuilder.append(StringUtils.getDirName(srcUnion.getPath()));
        }
        if (storagePathBuilder.length() > 0) storagePathBuilder.append(StringUtils.SPLIT_PATH);
        storagePathBuilder.append(srcUnion.getNodeName());
        srcUnion.setPath(StringUtils.formatPath(storagePathBuilder.toString()));

        if (!StringUtils.isEmpty(pid)) srcUnion.setPid(pid);
        if (!StringUtils.isEmpty(taskId)) srcUnion.setTaskId(taskId);
        if (!typeId.equals(src.getTypeId())) {
            srcUnion.setTypeId(typeId.toString());
        }

        if (!StringUtils.isEmpty(lastModifyUserId)) srcUnion.setLastModifyUserId(lastModifyUserId);
        if (fileLength > 0) srcUnion.setFileLength(fileLength);

        //文件节点属性
        if (!ConstService.isDirectoryType(srcUnion.getTypeId()) && (srcUnion.getFileEntity() == null)) srcUnion.setFileEntity(new StorageFileEntity());
        if (srcUnion.getFileEntity() != null) {
            StorageFileEntity fileEntity = srcUnion.getFileEntity();
            if (request != null) {
                BeanUtils.copyCleanProperties(request, fileEntity);
            }
            fileEntity.update();
        }

        //文件历史节点属性
        if (ConstService.isHistoryType(srcUnion.getTypeId()) && (srcUnion.getHisEntity() == null)) srcUnion.setHisEntity(new StorageFileHisEntity());
        if (srcUnion.getHisEntity() != null) {
            StorageFileHisEntity hisEntity = srcUnion.getHisEntity();
            if (request != null) {
                BeanUtils.copyCleanProperties(request, hisEntity);
                if (!StringUtils.isEmpty(request.getMainFileId())) hisEntity.setFileId(request.getMainFileId());
            }
            if (StringUtils.isEmpty(hisEntity.getFileId())) hisEntity.setFileId(srcUnion.getId());
            hisEntity.update();
        }

        srcUnion.update();
        updateStorageUnion(srcUnion);

        String path = StringUtils.replace(src.getPath(),src.getStoragePath(), srcUnion.getPath());
        log.info("===>updateNodeWithParent:" + (System.currentTimeMillis()-t) + "ms");
        return convertToSimpleNodeDTO(srcUnion,path);
    }

    private boolean isSameParent(@NotNull SimpleNodeDTO src, SimpleNodeDTO parent, UpdateNodeDTO request){
        StringBuilder pathBuilder = new StringBuilder();
        if (parent != null){
            pathBuilder.append(parent.getPath());
        } else {
            pathBuilder.append(StringUtils.getDirName(src.getPath()));
        }
        if ((request != null) && (!StringUtils.isEmpty(request.getFullName()))){
            String dirName = StringUtils.getDirName(request.getFullName());
            if (StringUtils.isNotEmpty(dirName)) {
                if (pathBuilder.length() > 0) pathBuilder.append(StringUtils.SPLIT_PATH);
                pathBuilder.append(dirName);
            }
        }
        return StringUtils.isSame(StringUtils.getDirName(src.getPath()),StringUtils.formatPath(pathBuilder.toString()));
    }

    @Override
    public void deleteNodeByIdList(AccountDTO account, List<String> idList, Current current) throws CustomException {
        if (ObjectUtils.isNotEmpty(idList)) {
            String accountId = (account != null) ? account.getId() : null;
            int n = 0;
            n += storageDao.fakeDeleteById(idList, accountId);
            n += storageFileDao.fakeDeleteById(idList, accountId);
            n += storageFileHisDao.fakeDeleteById(idList, accountId);
            log.debug("删除" + n + "条记录");
        }
    }

    @Override
    public void deleteNodeById(AccountDTO account, @NotNull String id, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        List<String> idList = new ArrayList<>();
        id = StringUtils.left(id, StringUtils.DEFAULT_ID_LENGTH);
        if (StringUtils.isNotEmpty(id)) {
            idList.add(id);
        }
        deleteNodeByIdList(account,idList,current);

        log.info("===>deleteNodeById:" + (System.currentTimeMillis()-t) + "ms");
    }

    @Override
    public void deleteNodeList(AccountDTO account, List<SimpleNodeDTO> nodeList, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        List<String> idList = new ArrayList<>();
        for (SimpleNodeDTO node : nodeList) {
            if (node.getIsDirectory()) {
                List<SimpleNodeDTO> children = listChildren(node, current);
                if (ObjectUtils.isNotEmpty(children)) {
                    for (SimpleNodeDTO child : children) {
                        idList.add(StringUtils.left(child.getId(), StringUtils.DEFAULT_ID_LENGTH));
                    }
                }
            }
            idList.add(StringUtils.left(node.getId(),StringUtils.DEFAULT_ID_LENGTH));
        }
        deleteNodeByIdList(account,idList,current);

        log.info("===>deleteNodeList:" + (System.currentTimeMillis()-t) + "ms");
    }

    @Override
    public void deleteNode(AccountDTO account, @NotNull SimpleNodeDTO node, Current current) throws CustomException {
        List<SimpleNodeDTO> list = new ArrayList<>();
        list.add(node);
        deleteNodeList(account,list,current);
    }

    private boolean isRoot(@NotNull SimpleNodeDTO parent){
        return StringUtils.isEmpty(parent.getId());
    }

    @Override
    public List<SimpleNodeDTO> listChild(@NotNull SimpleNodeDTO parent, Current current) throws CustomException {
        if (isRoot(parent)) return listRoot(null,current);

        CheckService.check(parent.getIsDirectory());
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parent.getId());
        return listNode(query,current);

    }

    @Override
    public List<SimpleNodeDTO> listChildren(@NotNull SimpleNodeDTO parent, Current current) throws CustomException {
        QueryNodeDTO nodeQuery = new QueryNodeDTO();
        if (!isRoot(parent)) {
            CheckService.check(parent.getIsDirectory());
            QueryNodeInfoDTO infoQuery = new QueryNodeInfoDTO();
            QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
            infoQuery.setTextQuery(txtQuery);
            FullNodeDTO parentInfo = getNodeInfo(parent, infoQuery, current);
            CheckService.check((parentInfo != null) && (parentInfo.getTextInfo() != null) && (StringUtils.isNotEmpty(parentInfo.getTextInfo().getPath())));
            nodeQuery.setParentPath(parentInfo.getTextInfo().getPath());
        }
        return listNode(nodeQuery,current);
    }

    @Override
    public List<SimpleNodeDTO> listRoot(String accountId, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid("-");
        query.setAccountId(accountId);
        return listNode(query,current);
    }


    @Override
    public SimpleNodeDTO updateNode(@NotNull SimpleNodeDTO src, @NotNull UpdateNodeDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(StringUtils.left(src.getId(),StringUtils.DEFAULT_ID_LENGTH));
        StorageEntityUnionDTO srcUnion = storageDao.selectStorageEntityUnion(BeanUtils.cleanProperties(query));
        CheckService.check(srcUnion != null);

        //更改属性
        if (!ConstService.STORAGE_NODE_TYPE_UNKNOWN.equals(request.getTypeId())) {
            srcUnion.setTypeId((Short.toString(request.getTypeId())));
        }
        if (StringUtils.isNotEmpty(request.getPath())) {
            srcUnion.setNodeName(StringUtils.getFileName(request.getPath()));
        }
        StorageTreeEntity parentEntity = getParentEntity(request,current);
        if (parentEntity != null) {
            srcUnion.setPid(parentEntity.getId());
            srcUnion.setPath(StringUtils.formatPath(parentEntity.getPath() + StringUtils.SPLIT_PATH + srcUnion.getNodeName()));
            srcUnion.setTaskId(parentEntity.getTaskId());
        } else {
            String dirName = StringUtils.getDirName(request.getPath());
            if (StringUtils.isNotEmpty(request.getPid()) || StringUtils.isNotEmpty(dirName)){
                srcUnion.setPid(null);
                srcUnion.setPath(srcUnion.getNodeName());
            } else {
                String path = StringUtils.getDirName(srcUnion.getPath());
                if (StringUtils.isNotEmpty(path)) {
                    path += StringUtils.SPLIT_PATH;
                }
                path += srcUnion.getNodeName();
                srcUnion.setPath(StringUtils.formatPath(path));
            }
            if (StringUtils.isNotEmpty(request.getTaskId())) {
                srcUnion.setTaskId(request.getTaskId());
            }
        }
        if (request.getFileLength() != 0) {
            srcUnion.setFileLength(request.getFileLength());
        }
        if (StringUtils.isNotEmpty(request.getOwnerUserId())) {
            srcUnion.setOwnerUserId(request.getOwnerUserId());
        }
        if (StringUtils.isNotEmpty(request.getAccountId())) {
            srcUnion.setLastModifyUserId(request.getAccountId());
        }
        if (StringUtils.isNotEmpty(request.getAccountRoleId())) {
            srcUnion.setLastModifyRoleId(request.getAccountRoleId());
        }
        if (!ConstService.isDirectoryType(srcUnion.getTypeId())){
            if  (isFileInfoValid(request)) {
                StorageFileEntity fileEntity = srcUnion.getFileEntity();
                if (fileEntity != null) {
                    BeanUtils.copyCleanProperties(request, fileEntity);
                    BeanUtils.copyCleanProperties(srcUnion, fileEntity);
                    fileEntity.update();
                    storageFileDao.update(fileEntity);
                } else {
                    fileEntity = BeanUtils.createCleanFrom(request, StorageFileEntity.class);
                    BeanUtils.copyCleanProperties(srcUnion, fileEntity);
                    fileEntity.update();
                    storageFileDao.insert(fileEntity);
                }
            }
            if (isHisInfoValid(request)){
                StorageFileHisEntity hisEntity = srcUnion.getHisEntity();
                if (hisEntity != null) {
                    BeanUtils.copyCleanProperties(request, hisEntity);
                    BeanUtils.copyCleanProperties(srcUnion, hisEntity);
                    if (StringUtils.isEmpty(hisEntity.getMainFileId())) {
                        hisEntity.setMainFileId(srcUnion.getId());
                    }
                    hisEntity.update();
                    storageFileHisDao.update(hisEntity);
                } else {
                    hisEntity = BeanUtils.createCleanFrom(request,StorageFileHisEntity.class);
                    BeanUtils.copyCleanProperties(srcUnion, hisEntity);
                    hisEntity.update();
                    if (StringUtils.isEmpty(hisEntity.getMainFileId())) {
                        hisEntity.setMainFileId(srcUnion.getId());
                    }
                    storageFileHisDao.insert(hisEntity);
                }
            }
            if (isMirrorInfoValid(request)){
                StorageFileEntity mirrorEntity = selectMirrorFileEntity(srcUnion.getId(),
                        request.getMirrorTypeId(),request.getMirrorAddress(),request.getMirrorBaseDir());
                if (mirrorEntity != null){
                    mirrorEntity = updateMirrorEntity(request,srcUnion.getId(),mirrorEntity);
                    mirrorEntity.update();
                    storageFileDao.update(mirrorEntity);
                } else {
                    mirrorEntity = BeanUtils.createCleanFrom(srcUnion,StorageFileEntity.class);
                    mirrorEntity = updateMirrorEntity(request,srcUnion.getId(),mirrorEntity);
                    mirrorEntity.reset();
                    storageFileDao.insert(mirrorEntity);
                }
            }
        }
        srcUnion.update();
        if (StringUtils.isNotSame(srcUnion.getTaskId(),src.getTaskId())) {
            CheckService.check(StringUtils.isNotEmpty(srcUnion.getId()));
            int n = storageTreeDao.updateTaskIdByPid(srcUnion.getId(),srcUnion.getTaskId());
            assert (n >= 0);
        }
        storageTreeDao.updateExact(srcUnion);
        SimpleNodeDTO node = convertToSimpleNodeDTO(srcUnion,null);

        log.info("\t----> updateNode:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    private void updateStorageUnion(@NotNull StorageEntityUnionDTO srcUnion){
        int n = 0;
        if (srcUnion.getHisEntity() != null) {
            StorageFileHisEntity hisEntity = srcUnion.getHisEntity();
            hisEntity.setFileId(StringUtils.left(hisEntity.getFileId(),StringUtils.DEFAULT_ID_LENGTH));
            n += storageFileHisDao.updateById(hisEntity);
            assert (n > 0);
        }
        if (srcUnion.getFileEntity() != null) {
            StorageFileEntity fileEntity = srcUnion.getFileEntity();
            fileEntity.setMainFileId(StringUtils.left(fileEntity.getMainFileId(),StringUtils.DEFAULT_ID_LENGTH));
            n += storageFileDao.updateById(fileEntity);
            assert (n > 0);
        }
        n += storageDao.updateById(srcUnion);
        assert (n > 0);
    }

    private void insertStorageUnion(@NotNull StorageEntityUnionDTO srcUnion){
        int n = 0;
        if (srcUnion.getHisEntity() != null) {
            StorageFileHisEntity hisEntity = srcUnion.getHisEntity();
            hisEntity.setFileId(StringUtils.left(hisEntity.getFileId(),StringUtils.DEFAULT_ID_LENGTH));
            n += storageFileHisDao.insert(BeanUtils.cleanProperties(hisEntity));
            assert (n > 0);
        }
        if (srcUnion.getFileEntity() != null) {
            StorageFileEntity fileEntity = srcUnion.getFileEntity();
            fileEntity.setMainFileId(StringUtils.left(fileEntity.getMainFileId(),StringUtils.DEFAULT_ID_LENGTH));
            n += storageFileDao.insert(BeanUtils.cleanProperties(fileEntity));
            assert (n > 0);
        }
        n += storageTreeDao.insert(BeanUtils.cleanProperties(srcUnion));
        assert (n > 0);
    }

    @Deprecated
    private String insertPathNodeList(StorageEntityUnionDTO parentUnion,
                                    @NotNull UpdateNodeDTO request,
                                    @NotNull String fullName,
                                    String pid,
                                    String taskId,
                                    String lastModifyUserId){
        List<StorageTreeEntity> nodeList = new ArrayList<>();
        StringBuilder pathBuilder = new StringBuilder();
        String parentPath = "";
        if (parentUnion != null) {
            parentPath = parentUnion.getPath();
            pathBuilder.append(parentPath);
        }
        String fullPath = StringUtils.getDirName(fullName);
        String[] nodeNameArray = fullPath.split(StringUtils.SPLIT_PATH);
        for (String nodeName : nodeNameArray) {
            if (StringUtils.isEmpty(nodeName)) continue;
            StorageTreeEntity pathNode = BeanUtils.createCleanFrom(request, StorageTreeEntity.class);
            pathNode.setPid(pid);
            pathNode.setNodeName(nodeName);
            if (pathBuilder.length() > 0) pathBuilder.append(StringUtils.SPLIT_PATH);
            pathBuilder.append(nodeName);
            pathNode.setPath(pathBuilder.toString());
            pathNode.setTypeId(ConstService.getPathType(Short.toString(request.getParentTypeId())));
            pathNode.setLastModifyUserId(lastModifyUserId);
            pathNode.setFileLength(0L);
            pathNode.setTaskId(taskId);
            nodeList.add(pathNode);
            pid = pathNode.getId();
        }
        if (!nodeList.isEmpty()) {
            int n = storageDao.insertList(nodeList);
            assert (n > 0);
        }
        return pid;
    }

    private StorageEntityUnionDTO updateStorageUnion(@NotNull StorageEntityUnionDTO srcUnion,
                                                     @NotNull UpdateNodeDTO request,
                                                     String pid,
                                                     String taskId,
                                                     String storagePath,
                                                     String lastModifyUserId,
                                                     Long fileLength,
                                                     Short typeId) {
        //修改节点属性
        BeanUtils.copyProperties(request, srcUnion);
        if (!StringUtils.isEmpty(storagePath)) {
            srcUnion.setNodeName(StringUtils.getFileName(storagePath));
            srcUnion.setPath(StringUtils.formatPath(storagePath));
        }
        srcUnion.setTaskId(taskId);
        srcUnion.setPid(pid);
        srcUnion.setLastModifyUserId(lastModifyUserId);
        srcUnion.setFileLength(fileLength);
        srcUnion.setTypeId(Short.toString(typeId));

        if (srcUnion.getHisEntity() != null) {
            StorageFileHisEntity hisEntity = srcUnion.getHisEntity();
            BeanUtils.copyProperties(request, hisEntity);
            hisEntity.setFileId(request.getMainFileId());
            if (StringUtils.isEmpty(hisEntity.getFileId())) hisEntity.setFileId(srcUnion.getId());
            hisEntity.update();
        }

        if (srcUnion.getFileEntity() != null) {
            StorageFileEntity fileEntity = srcUnion.getFileEntity();
            BeanUtils.copyProperties(request, fileEntity);
            fileEntity.update();
        }
        srcUnion.update();
        return srcUnion;
    }

    private StorageEntityUnionDTO getParentUnion(SimpleNodeDTO src, UpdateNodeDTO request){
        StorageEntityUnionDTO nodeUnion = null;
        String parentPath = StringUtils.formatPath(request.getParentPath());
        String fullName = StringUtils.formatPath(request.getFullName());
        if (!StringUtils.isEmpty(fullName) || !StringUtils.isEmpty(parentPath)) {
            String pid = request.getPid();
            String taskId = request.getTaskId();
            Short parentTypeId = request.getParentTypeId();
            //重新组合父目录和相对目录
            if (StringUtils.isEmpty(fullName)) {
                if (src != null) fullName = src.getName();
            }
            String path = fullName;
            if (!StringUtils.isSame(StringUtils.left(fullName, 1), StringUtils.SPLIT_PATH)) {
                if (StringUtils.isEmpty(parentPath)) {
                    if (src != null) parentPath = StringUtils.getDirName(src.getPath());
                }
                path = parentPath;
                if (!StringUtils.isEmpty(path)) path += StringUtils.SPLIT_PATH;
                path += fullName;
            } else {
                parentPath = "";
            }
            String srcPath = null;
            if (src != null) srcPath = src.getPath();
            if (!StringUtils.isSame(path,srcPath) && StringUtils.contains(fullName, StringUtils.SPLIT_PATH)) {
                String fullPath = parentPath;
                if (!StringUtils.isEmpty(fullPath)) fullPath += StringUtils.SPLIT_PATH;
                fullPath += fullName;
                String parentFullPath = StringUtils.getDirName(fullPath);

                SimpleNodeDTO rootNode = getNodeByFuzzyPathOld(parentFullPath);

                if (rootNode != null) {
                    QueryNodeDTO unionQuery = new QueryNodeDTO();
                    unionQuery.setId(StringUtils.left(rootNode.getId(), StringUtils.DEFAULT_ID_LENGTH));
                    nodeUnion = storageDao.quickSelectStorageEntityUnion(BeanUtils.cleanProperties(unionQuery));
                    parentPath = rootNode.getPath();
                    fullName = StringUtils.substring(fullPath, StringUtils.length(rootNode.getPath() + StringUtils.SPLIT_PATH));
                    pid = rootNode.getId();
                    taskId = rootNode.getTaskId();
                    parentTypeId = rootNode.getTypeId();
                }
            } else {
                if (src != null) pid = src.getPid();
                if (!StringUtils.isEmpty(pid)){
                    SimpleNodeDTO parentNode = getNodeByIdOld(pid);
                    if (parentNode != null){
                        QueryNodeDTO unionQuery = new QueryNodeDTO();
                        unionQuery.setId(StringUtils.left(parentNode.getId(), StringUtils.DEFAULT_ID_LENGTH));
                        nodeUnion = storageDao.quickSelectStorageEntityUnion(BeanUtils.cleanProperties(unionQuery));
                        if (!StringUtils.isEmpty(parentPath)){
                            parentPath = parentNode.getPath();
                        }
                        if ((src != null) && !StringUtils.isEmpty(fullName)){
                            fullName = StringUtils.getFileName(fullName);
                        }
                        pid = parentNode.getId();
                        taskId = parentNode.getTaskId();
                        parentTypeId = parentNode.getTypeId();
                    }
                }
            }
            request.setParentPath(parentPath);
            request.setFullName(fullName);
            request.setPid(StringUtils.left(pid,StringUtils.DEFAULT_ID_LENGTH));
            request.setTaskId(taskId);
            request.setParentTypeId(parentTypeId);
        }
        return nodeUnion;
    }

    private SimpleNodeDTO getNodeByFuzzyPathOld(String fuzzyPath){
        SimpleNodeDTO node = null;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath(fuzzyPath);
        List<SimpleNodeDTO> parentNodeList = storageDao.listNode(BeanUtils.cleanProperties(query));
        if ((parentNodeList != null) && (!parentNodeList.isEmpty())) {
            node = parentNodeList.get(0);
        }
        return node;
    }

    private SimpleNodeDTO getNodeByIdOld(String id){
        SimpleNodeDTO node = null;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> parentNodeList = storageDao.listNode(BeanUtils.cleanProperties(query));
        if ((parentNodeList != null) && (!parentNodeList.isEmpty())) {
            node = parentNodeList.get(0);
        }
        return node;
    }

    @Override
    public List<SimpleNodeDTO> listNode(@NotNull QueryNodeDTO query, Current current) {
        long t = System.currentTimeMillis();

        query.setPath(StringUtils.formatPath(query.getPath()));
        query.setParentPath(StringUtils.formatPath(query.getParentPath()));
        query.setFuzzyPath(StringUtils.formatPath(query.getFuzzyPath()));
        List<SimpleNodeDTO> list = storageDao.listNode(BeanUtils.cleanProperties(query));

        log.info("\t----> listNode花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return list;
    }

    @Override
    public List<FileNodeDTO> listFileNodeInfo(@NotNull QueryNodeDTO query, boolean withHistory, Current current) {
        if (withHistory){
            return storageDao.listFileNodeWithHistory(query);
        } else {
            return storageDao.listFileNode(query);
        }
    }

    @Override
    public FileNodeDTO getFileNodeInfo(@NotNull SimpleNodeDTO node, boolean withHistory, Current current) {
        FileNodeDTO fileNode;
        if (withHistory) {
            fileNode = storageDao.getFileNodeExtraWithHistory(StringUtils.left(node.getId(), StringUtils.DEFAULT_ID_LENGTH));
        } else {
            fileNode = storageDao.getFileNodeExtra(StringUtils.left(node.getId(), StringUtils.DEFAULT_ID_LENGTH));
        }
        if (fileNode != null){
            fileNode.setBasic(node);
        }
        return fileNode;
    }

    @Override
    @Deprecated
    public FullNodeDTO getFullNodeInfo(@NotNull SimpleNodeDTO node, Current current) {
        FullNodeDTO fullNode = storageDao.getNodeExtra(node.getTaskId());
        if (fullNode != null){
            fullNode.setBasic(node);
        }
        return fullNode;
    }

    private boolean isOwner(AccountDTO account, StorageTreeEntity nodeEntity){
        assert (nodeEntity != null);
        return isOwner(account,nodeEntity.getLastModifyUserId());
    }

    private boolean isOwner(AccountDTO account, String ownerId){
        return (StringUtils.isEmpty(ownerId) ||
                ((account != null) && StringUtils.isSame(account.getId(),ownerId)));
    }


    private SimpleNodeDTO convertToSimpleNodeDTO(StorageTreeEntity entity, String fullPath){
        if (entity == null) return null;
        SimpleNodeDTO node = BeanUtils.createCleanFrom(entity,SimpleNodeDTO.class);
        if (entity.getTypeId() != null) {
            node.setIsDirectory(ConstService.isDirectoryType(entity.getTypeId()));
            node.setIsProject(ConstService.isProjectType(entity.getTypeId()));
            node.setIsTask(ConstService.isTaskType(entity.getTypeId()));
            node.setIsDesign(ConstService.isDesignType(entity.getTypeId()));
            node.setIsCommit(ConstService.isCommitType(entity.getTypeId()));
            node.setIsHistory(ConstService.isHistoryType(entity.getTypeId()));
            Short rangeId = Short.parseShort(ConstService.getRangeId(entity.getTypeId()));
            node.setClassicId(rangeId.toString());
            node.setClassicName(ConstService.getRangeName(rangeId));
        }
        node.setName(entity.getNodeName());
        node.setStoragePath(entity.getPath());
        node.setPath(fullPath);
        node.setId(node.getId() + node.getClassicId());
        node.setPid(node.getPid() + node.getClassicId());
        if (entity.getCreateTime() != null) {
            node.setCreateTimeStamp(entity.getCreateTime().getTime());
            node.setCreateTimeText(StringUtils.getTimeStamp(entity.getCreateTime(), StringUtils.NORMAL_STAMP_FORMAT));
        }
        if (entity.getLastModifyTime() != null) {
            node.setLastModifyTimeStamp(entity.getLastModifyTime().getTime());
            node.setLastModifyTimeText(StringUtils.getTimeStamp(entity.getLastModifyTime(), StringUtils.NORMAL_STAMP_FORMAT));
        }
        if (!StringUtils.isEmpty(entity.getLastModifyUserId())){
            node.setOwnerUserId(entity.getLastModifyUserId());
        }
        node.setFileLength((entity.getFileLength() != null) ? entity.getFileLength() : 0);
        return node;
    }


    @Deprecated
    private SimpleNodeDTO createNode(@NotNull UpdateNodeDTO request, boolean force, Current current){

        long t = System.currentTimeMillis();

        request = BeanUtils.cleanProperties(request);

        StorageEntityUnionDTO parentUnion = getParentUnion(null,request);
        Short typeId = request.getTypeId();
        if ((ConstService.STORAGE_NODE_TYPE_UNKNOWN.equals(typeId)) || (ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN.equals(typeId))) {
            Short parentTypeId = (parentUnion != null) ? Short.parseShort(parentUnion.getTypeId()) : ConstService.STORAGE_NODE_TYPE_DIR_DESIGN;
            if (ConstService.STORAGE_NODE_TYPE_UNKNOWN.equals(typeId)) {
                typeId = Short.parseShort(ConstService.getFileType(parentTypeId.toString()));
            } else {
                typeId = Short.parseShort(ConstService.getPathType(parentTypeId.toString()));
            }
        }
        Long fileLength = request.getFileLength();
        String lastModifyUserId = request.getOwnerUserId();
        String pid = StringUtils.left(request.getPid(),StringUtils.DEFAULT_ID_LENGTH);
        String taskId = request.getTaskId();
        if (parentUnion != null) {
            pid = parentUnion.getId();
            taskId = parentUnion.getTaskId();
        }

        String fullName = StringUtils.formatPath(request.getFullName());
        if (!StringUtils.isEmpty(fullName) && fullName.contains(StringUtils.SPLIT_PATH)) {
            //如果fullName包含路径则创建中间路径节点
            pid = insertPathNodeList(parentUnion,request,fullName,pid,taskId,lastModifyUserId);
        }
        String storagePath = fullName;
        if ((storagePath != null) && (parentUnion != null)){
            storagePath = parentUnion.getPath() + StringUtils.SPLIT_PATH + fullName;
        }
        StorageEntityUnionDTO nodeUnion = BeanUtils.createCleanFrom(request,StorageEntityUnionDTO.class);
        if (!ConstService.isDirectoryType(typeId.toString())){
            StorageFileEntity fileEntity = BeanUtils.createCleanFrom(nodeUnion,StorageFileEntity.class);
            nodeUnion.setFileEntity(fileEntity);
            if (ConstService.isHistoryType(typeId.toString())){
                StorageFileHisEntity hisEntity = BeanUtils.createCleanFrom(nodeUnion,StorageFileHisEntity.class);
                nodeUnion.setHisEntity(hisEntity);
            }
        }
        nodeUnion = updateStorageUnion(nodeUnion,request,pid,taskId,storagePath,lastModifyUserId,fileLength,typeId);
        insertStorageUnion(nodeUnion);

        String path = StringUtils.formatPath(request.getParentPath() + StringUtils.SPLIT_PATH + nodeUnion.getPath());
        return convertToSimpleNodeDTO(nodeUnion,path);
    }

    @Override
    public SimpleNodeDTO createNode(SimpleNodeDTO parent, @NotNull UpdateNodeDTO request, Current current) throws CustomException {

        long t = System.currentTimeMillis();

        request = BeanUtils.cleanProperties(request);
        if (parent != null){
            request.setPid(parent.getId());
        }

        //取父节点信息
        StorageTreeEntity parentEntity = getParentEntity(request,current);

        //初始化新建节点参数
        StorageEntityUnionDTO nodeUnion = BeanUtils.createCleanFrom(request,StorageEntityUnionDTO.class);
        nodeUnion.reset();
        nodeUnion.setNodeName(StringUtils.getFileName(request.getPath()));
        if (StringUtils.isEmpty(nodeUnion.getTaskId()) && (parent != null)) nodeUnion.setTaskId(parent.getTaskId());
        if (parentEntity != null) {
            nodeUnion.setPid(parentEntity.getId());
            String parentPath = parentEntity.getPath();
            if (StringUtils.isNotEmpty(parentPath)) parentPath += StringUtils.SPLIT_PATH;
            nodeUnion.setPath(StringUtils.formatPath(parentPath + nodeUnion.getNodeName()));
            if (ConstService.isUnknownFileType(nodeUnion.getTypeId())) {
                nodeUnion.setTypeId(ConstService.getFileType(parentEntity.getTypeId()));
            } else if (ConstService.isUnknownDirectoryType(nodeUnion.getTypeId())) {
                nodeUnion.setTypeId(ConstService.getPathType(parentEntity.getTypeId()));
            }
        } else {
            nodeUnion.setPid(null);
            nodeUnion.setPath(nodeUnion.getNodeName());
            if (ConstService.isUnknownFileType(nodeUnion.getTypeId())) {
                if (parent != null) nodeUnion.setTypeId(ConstService.getFileType((new Short(parent.getTypeId())).toString()));
            } else if (ConstService.isUnknownDirectoryType(nodeUnion.getTypeId())) {
                if (parent != null) nodeUnion.setTypeId(ConstService.getPathType((new Short(parent.getTypeId())).toString()));
            }
        }
        nodeUnion.setLastModifyUserId(request.getAccountId());
        nodeUnion.setLastModifyRoleId(request.getAccountRoleId());

        if (!ConstService.isDirectoryType(nodeUnion.getTypeId()) && isFileInfoValid(request)){
            StorageFileEntity fileEntity = BeanUtils.createCleanFrom(nodeUnion,StorageFileEntity.class);
            BeanUtils.copyCleanProperties(request,fileEntity);
            nodeUnion.setFileEntity(fileEntity);
            if (isHisInfoValid(request)){
                StorageFileHisEntity hisEntity = BeanUtils.createCleanFrom(fileEntity,StorageFileHisEntity.class);
                BeanUtils.copyCleanProperties(request,hisEntity);
                if (StringUtils.isEmpty(hisEntity.getMainFileId())) hisEntity.setMainFileId(fileEntity.getId());
                nodeUnion.setHisEntity(hisEntity);
            }
            if (isMirrorInfoValid(request)){
                StorageFileEntity mirrorEntity = BeanUtils.createCleanFrom(fileEntity,StorageFileEntity.class);
                updateMirrorEntity(request,fileEntity.getId(),mirrorEntity);
                storageFileDao.insert(mirrorEntity);
            }
        }
        insertStorageUnion(nodeUnion);
        SimpleNodeDTO node = convertToSimpleNodeDTO(nodeUnion,null);

        log.info("\t----> createNode:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    @Override
    public SimpleNodeDTO createNodeWithRequestOnly(@NotNull UpdateNodeDTO request, Current current) throws CustomException {
        return createNode(null,request,current);
    }

    private StorageTreeEntity getParentEntity(@NotNull UpdateNodeDTO request, Current current) throws CustomException {
        SimpleNodeDTO parent = getParentNode(request,current);
        String dirName = StringUtils.getDirName(request.getPath());
        StorageTreeEntity parentEntity = insertPathNodeList(parent, dirName, request.getAccountId(), request.getAccountRoleId());
        if ((parentEntity != null) && StringUtils.isNotEmpty(request.getTaskId())){
            parentEntity.setTaskId(request.getTaskId());
        }
        return parentEntity;
    }

    private SimpleNodeDTO getParentNode(@NotNull UpdateNodeDTO request, Current current) throws CustomException {
        SimpleNodeDTO parent = null;
        if (StringUtils.isNotEmpty(request.getPid())) {
            parent = getNodeById(request.getPid(),current);
        }
        if ((parent == null) && (StringUtils.isNotEmpty(request.getPath()))) {
            String parentPath = StringUtils.getDirName(request.getPath());
            if (StringUtils.isNotEmpty(parentPath)) {
                if (!StringUtils.isStartWith(parentPath, StringUtils.SPLIT_PATH)) {
                    parentPath = StringUtils.SPLIT_PATH + parentPath;
                }
                parent = getNodeByFuzzyPath(parentPath, current);
                if (parent != null) {
                    QueryNodeInfoDTO query = new QueryNodeInfoDTO();
                    QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
                    query.setTextQuery(txtQuery);
                    FullNodeDTO parentFullInfo = getNodeInfo(parent, query, current);
                    CheckService.check((parentFullInfo != null) && (parentFullInfo.getTextInfo() != null));
                    String path = StringUtils.substring(parentPath, StringUtils.length(parentFullInfo.getTextInfo().getPath()) + StringUtils.SPLIT_PATH.length());
                    if (StringUtils.isNotEmpty(path)) {
                        path += StringUtils.SPLIT_PATH + StringUtils.getFileName(request.getPath());
                    } else {
                        path = StringUtils.getFileName(request.getPath());
                    }
                    request.setPath(path);
                }
            }
        }
        return parent;
    }

    @Override
    public SimpleNodeDTO getNodeById(String id, Current current) throws CustomException {
        if (StringUtils.isEmpty(id)) return null;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> list = listNode(query, current);
        return ObjectUtils.getFirst(list);
    }

    @Override
    public SimpleNodeDTO getNodeByPath(String path, Current current) throws CustomException {
        if (StringUtils.isEmpty(path)) return null;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(StringUtils.formatPath(path));
        List<SimpleNodeDTO> list = listNode(query, current);
        return ObjectUtils.getFirst(list);
    }

    @Override
    public SimpleNodeDTO getNodeByFuzzyPath(String fuzzyPath, Current current) throws CustomException {
        if (StringUtils.isEmpty(fuzzyPath)) return null;
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath(StringUtils.formatPath(fuzzyPath));
        List<SimpleNodeDTO> list = listNode(query, current);
        return ObjectUtils.getFirst(list);
    }
}
