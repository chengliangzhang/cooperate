package com.maoding.Storage;

import com.maoding.Base.BaseLocalService;
import com.maoding.Common.CheckService;
import com.maoding.Common.ConstService;
import com.maoding.Common.zeroc.CustomException;
import com.maoding.Common.zeroc.DeleteAskDTO;
import com.maoding.Common.zeroc.ErrorCode;
import com.maoding.Common.zeroc.QueryAskDTO;
import com.maoding.CoreUtils.BeanUtils;
import com.maoding.CoreUtils.ObjectUtils;
import com.maoding.CoreUtils.StringUtils;
import com.maoding.Storage.Dao.*;
import com.maoding.Storage.Dto.StorageEntityUnionDTO;
import com.maoding.Storage.Entity.*;
import com.maoding.Storage.zeroc.*;
import com.maoding.User.zeroc.AccountDTO;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
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
    private StorageDao storageDao;

    @Autowired
    private StorageTreeDao storageTreeDao;

    @Autowired
    private StorageFileDao storageFileDao;

    @Autowired
    private StorageFileHisDao storageFileHisDao;

    @Autowired
    private ElementListDao elementListDao;

    @Autowired
    private AnnotateTreeDao annotateTreeDao;

    @Autowired
    private AttachmentListDao attachmentListDao;

    @Autowired
    private AnnotateDao annotateDao;

    @Autowired
    private ElementDao elementDao;

    private Map<String,List<SimpleNodeDTO>> simpleNodeMap = new HashMap<>();
    private Map<String,Integer> queryTimesMap = new HashMap<>();

    @Override
    public List<CANodeDTO> listCANode(@NotNull QueryCANodeDTO query, Current current) throws CustomException {
        return storageDao.listCANode(BeanUtils.cleanProperties(query));
    }

    @Override
    public List<EmbedElementDTO> listEmbedElement(@NotNull QueryAskDTO query, Current current) throws CustomException {
        return elementDao.listElement(query);
    }

    @Override
    public List<NodeFileDTO> listNodeFile(QueryNodeFileDTO query, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        List<NodeFileDTO> fileList = storageDao.listNodeFile(BeanUtils.cleanProperties(query));

        log.info("\t----> listNodeFile花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return fileList;
    }

    @Override
    public EmbedElementDTO createEmbedElement(UpdateElementDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        ElementEntity entity = BeanUtils.createCleanFrom(request,ElementEntity.class);
        String id = entity.getId();
        elementListDao.insert(entity);
        EmbedElementDTO result = BeanUtils.createCleanFrom(entity,EmbedElementDTO.class);

        log.info("\t----> createEmbedElement花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return result;
    }

    @Override
    public NodeFileDTO updateNodeFile(@NotNull NodeFileDTO file, @NotNull UpdateNodeFileDTO request, Current current) throws CustomException {
        final String QUERY_FIELD_ID = "id";
        final String QUERY_MAIN_FIELD_ID = "mainFileId";
        final String QUERY_MIRROR_SERVER_TYPE_ID = "mirrorServerTypeId";
        final String QUERY_MIRROR_SERVER_ADDRESS = "mirrorServerAddress";
        final String QUERY_MIRROR_BASE_DIR = "mirrorBaseDir";
        long t = System.currentTimeMillis();

        CheckService.check(StringUtils.isNotEmpty(file.getId()), ErrorCode.InvalidParameter,"updateNodeFile");
        //更新节点信息
        Map<String,Object> queryFile = new HashMap<>();
        queryFile.put(QUERY_FIELD_ID,file.getId());
        StorageFileEntity fileEntity = storageDao.selectFileEntity(queryFile);
        CheckService.check(fileEntity != null,ErrorCode.DataNotFound,"updateNodeFile");
        BeanUtils.copyCleanProperties(request, fileEntity);
        fileEntity.update();
        storageFileDao.update(fileEntity);

        //更新镜像信息
        if (isMirrorInfoValid(request)){
            Map<String,Object> queryMirror = new HashMap<>();
            if (StringUtils.isNotEmpty(file.getId())) {
                queryMirror.put(QUERY_MAIN_FIELD_ID,file.getId());
            }
            if (StringUtils.isNotEmpty(request.getMirrorTypeId())) {
                queryMirror.put(QUERY_MIRROR_SERVER_TYPE_ID, request.getMirrorTypeId());
            }
            if (StringUtils.isNotEmpty(request.getMirrorAddress())) {
                queryMirror.put(QUERY_MIRROR_SERVER_ADDRESS, request.getMirrorAddress());
            }
            if (StringUtils.isNotEmpty(request.getMirrorBaseDir())) {
                queryMirror.put(QUERY_MIRROR_BASE_DIR, request.getMirrorBaseDir());
            }
            StorageFileEntity mirrorEntity = storageDao.selectFileEntity(queryMirror);
            if (mirrorEntity == null){
                mirrorEntity = new StorageFileEntity();
                updateMirrorEntity(request,file.getId(),mirrorEntity);
                storageFileDao.insert(mirrorEntity);
            } else {
                updateMirrorEntity(request,file.getId(),mirrorEntity);
                storageFileDao.update(mirrorEntity);
            }
        }
        BeanUtils.copyCleanProperties(request,file);

        //更新节点长度和md5
        if ((request.getFileLength() > 0) || (StringUtils.isNotEmpty(request.getFileMd5()))) {
            StorageTreeEntity nodeEntity = storageTreeDao.selectById(StringUtils.left(file.getId(), StringUtils.DEFAULT_ID_LENGTH));
            CheckService.check(nodeEntity != null, ErrorCode.DataNotFound);
            nodeEntity.setFileLength(fileEntity.getFileLength());
            nodeEntity.setFileMd5(fileEntity.getFileMd5());
            nodeEntity.update();
            storageTreeDao.update(nodeEntity);
        }

        log.info("\t----> updateNodeFile花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return file;
    }

    @Override
    public NodeFileDTO createNodeFileWithRequestOnly(UpdateNodeFileDTO request, Current current) throws CustomException {
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
    public long summaryNodeLength(QuerySummaryDTO query, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        Long summaryLength = storageDao.summaryNodeLength(BeanUtils.cleanProperties(query));

        log.info("\t----> summaryNodeLength花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return summaryLength;
    }

    @Override
    public EmbedElementDTO updateEmbedElement(@NotNull EmbedElementDTO src, @NotNull UpdateElementDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        ElementEntity entity = elementListDao.selectById(src.getId());
        CheckService.check(entity != null,ErrorCode.DataNotFound,"updateEmbedElement");
        BeanUtils.copyCleanProperties(request,entity);
        entity.update();
        int n = elementListDao.update(entity);
        CheckService.check (n == 1,ErrorCode.DataIsInvalid,"updateEmbedElement");
        BeanUtils.copyCleanProperties(entity,src);

        log.info("\t----> updateEmbedElement:" + (System.currentTimeMillis()-t) + "ms");
        return src;
    }

    private void addAttachmentList(@NotNull AnnotateEntity annotateEntity, List<String> elementIdList, List<String> fileIdList){
        List<AttachmentEntity> attachmentList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(elementIdList)) {
            for (String id : elementIdList) {
                AttachmentEntity attachmentEntity = BeanUtils.createCleanFrom(annotateEntity, AttachmentEntity.class);
                attachmentEntity.resetId();
                attachmentEntity.setAnnotateId(annotateEntity.getId());
                attachmentEntity.setAttachmentElementId(id);
                attachmentList.add(attachmentEntity);
            }
        }
        if (ObjectUtils.isNotEmpty(fileIdList)) {
            for (String id : fileIdList) {
                AttachmentEntity attachmentEntity = BeanUtils.createCleanFrom(annotateEntity, AttachmentEntity.class);
                attachmentEntity.resetId();
                attachmentEntity.setAnnotateId(annotateEntity.getId());
                attachmentEntity.setAttachmentFileId(id);
                attachmentList.add(attachmentEntity);
            }
        }
        if (ObjectUtils.isNotEmpty(attachmentList)){
            int n = attachmentListDao.insertList(attachmentList);
            assert (n == attachmentList.size());
        }

    }

    @Override
    public AnnotateDTO createAnnotate(NodeFileDTO file, @NotNull UpdateAnnotateDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        //建立文件注解记录
        AnnotateEntity annotateEntity = BeanUtils.createCleanFrom(request,AnnotateEntity.class);
        if (file != null) {
            annotateEntity.setFileId(file.getId());
            if (StringUtils.isNotEmpty(file.getMainFileId())) {
                annotateEntity.setMainFileId(file.getMainFileId());
            } else {
                annotateEntity.setMainFileId(file.getId());
            }
        }

        //添加文件注解附件
        addAttachmentList(annotateEntity,request.getAddElementIdList(),request.getAddFileIdList());

        int n = annotateTreeDao.insert(annotateEntity);
        assert (n == 1);
        AnnotateDTO annotate = BeanUtils.createCleanFrom(annotateEntity,AnnotateDTO.class);

        log.info("\t----> createAnnotate:" + (System.currentTimeMillis()-t) + "ms");
        return annotate;
    }

    @Override
    public List<AnnotateDTO> listAnnotate(@NotNull QueryAnnotateDTO query, Current current) throws CustomException {
        return annotateDao.listAnnotate(BeanUtils.cleanProperties(query));
    }

    @Override
    public AnnotateDTO updateAnnotate(@NotNull AnnotateDTO src, @NotNull UpdateAnnotateDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        //更新文件注解记录
        AnnotateEntity annotateEntity = annotateTreeDao.selectById(StringUtils.left(src.getId(),StringUtils.DEFAULT_ID_LENGTH));
        BeanUtils.copyCleanProperties(request,annotateEntity);
        annotateEntity.update();

        //添加附件
        addAttachmentList(annotateEntity,request.getAddElementIdList(),request.getAddFileIdList());

        //删除附件
        if (ObjectUtils.isNotEmpty(request.getDelAttachmentIdList())) {
            List<String> delElementIdList = new ArrayList<>();
            delElementIdList.addAll(request.getDelAttachmentIdList());
            attachmentListDao.deleteAttachment(annotateEntity.getId(),delElementIdList,request.getLastModifyUserId());
        }

        int n = annotateTreeDao.update(annotateEntity);
        assert (n == 1);
        BeanUtils.copyCleanProperties(annotateEntity,src);

        log.info("\t----> updateAnnotate:" + (System.currentTimeMillis()-t) + "ms");
        return src;
    }

    @Override
    public NodeFileDTO createNodeFile(NodeFileDTO src, @NotNull UpdateNodeFileDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        //创建文件或镜像并更改返回对象
        StorageFileEntity entity;
        if ((src != null) && (StringUtils.isNotEmpty(src.getId()))) {
            entity = storageFileDao.selectById(src.getId());
            CheckService.check(entity != null,ErrorCode.DataNotFound,"createNodeFile");
            updateMirrorEntity(request, src.getId(), entity);
            src.setReadOnlyMirrorKey(StringUtils.formatPath(entity.getReadOnlyKey()));
            src.setWritableMirrorKey(StringUtils.formatPath(entity.getWritableKey()));
        } else {
            entity = BeanUtils.createCleanFrom(request,StorageFileEntity.class);
            entity.reset();
            src = BeanUtils.createCleanFrom(entity,NodeFileDTO.class);
        }
        storageFileDao.insert(entity);

        //如果request内有创建镜像内容，创建镜像
        if (isMirrorInfoValid(request)){
            String fileId = src.getId();
            StorageFileEntity mirrorEntity = selectMirrorFileEntity(fileId, Short.parseShort(request.getMirrorTypeId()), request.getMirrorAddress(), request.getMirrorBaseDir());
            if (mirrorEntity == null) {
                mirrorEntity = new StorageFileEntity();
                BeanUtils.copyCleanProperties(entity,mirrorEntity);
                mirrorEntity.reset();
                updateMirrorEntity(request,fileId,mirrorEntity);
                storageFileDao.insert(mirrorEntity);
            } else {
                updateMirrorEntity(request,fileId,mirrorEntity);
                mirrorEntity.update();
                storageFileDao.update(mirrorEntity);
            }
            src.setReadOnlyMirrorKey(StringUtils.formatPath(mirrorEntity.getReadOnlyKey()));
            src.setWritableMirrorKey(StringUtils.formatPath(mirrorEntity.getWritableKey()));
        }

        log.info("\t----> createNodeFile:" + (System.currentTimeMillis()-t) + "ms");
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
        return storageDao.listWebArchiveDir(BeanUtils.cleanProperties(query));
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
        if (request.getFileLength() > 0) {
            mirrorEntity.setFileLength(request.getFileLength());
        }
        if (StringUtils.isNotEmpty(request.getFileMd5())) {
            mirrorEntity.setFileMd5(request.getFileMd5());
        }
        if (StringUtils.isNotEmpty(request.getLastModifyUserId())){
            mirrorEntity.setLastModifyUserId(request.getLastModifyUserId());
        }
        if (StringUtils.isNotEmpty(request.getLastModifyRoleId())){
            mirrorEntity.setLastModifyRoleId(request.getLastModifyRoleId());
        }
        return mirrorEntity;
    }

    private StorageFileEntity updateMirrorEntity(@NotNull UpdateNodeDTO request, @NotNull String fileId, @NotNull StorageFileEntity mirrorEntity){
        UpdateNodeFileDTO updateFileRequest = BeanUtils.createCleanFrom(request,UpdateNodeFileDTO.class);
        updateMirrorEntity(updateFileRequest,fileId,mirrorEntity);
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
                || (ObjectUtils.isNotEmpty(request.getWritableKey()))
                || (request.getFileLength() > 0)
                || (StringUtils.isNotEmpty(request.getFileMd5()))
                || (request.getIsPassDesign())
                || (request.getIsPassCheck())
                || (request.getIsPassAudit())
                || isHisInfoValid(request)
                || isMirrorInfoValid(request);
    }

    private boolean isMirrorInfoValid(@NotNull UpdateNodeDTO request) {
        return (ObjectUtils.isNotEmpty(request.getReadOnlyMirrorKey()))
                || (ObjectUtils.isNotEmpty(request.getWritableMirrorKey()));
    }

    private boolean isMirrorInfoValid(@NotNull UpdateNodeFileDTO request) {
        return (ObjectUtils.isNotEmpty(request.getReadOnlyMirrorKey()))
                || (ObjectUtils.isNotEmpty(request.getWritableMirrorKey()));
    }

    private boolean isValid(SimpleNodeDTO node) {
        return (node != null) && (StringUtils.isNotEmpty(node.getId()));
    }

    private StorageTreeEntity insertPathNodeList(SimpleNodeDTO parent, String path, String accountId, String accountRoleId) {
        //获取父节点
        StorageTreeEntity lastEntity = null;
        if (isValid(parent)) {
            lastEntity = storageTreeDao.selectById(StringUtils.left(parent.getId(),StringUtils.DEFAULT_ID_LENGTH));
            while (StringUtils.isStartWith(path,StringUtils.SPLIT_PATH)) {
                path = StringUtils.substring(path,StringUtils.SPLIT_PATH.length());
            }
        }
        if (lastEntity != null) {
            path = StringUtils.formatPath(lastEntity.getPath() + StringUtils.SPLIT_PATH + path);
        }
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
                if (StringUtils.isNotEmpty(lastEntity.getTypeId())) {
                    entity.setTypeId(ConstService.getPathType((new Short(lastEntity.getTypeId())).toString()));
                } else {
                    entity.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_USER.toString());
                }
                entity.setTaskId(lastEntity.getTaskId());
                entity.setProjectId(lastEntity.getProjectId());
            } else if (isValid(parent)){
                if (StringUtils.isNotEmpty(parent.getTypeId())) {
                    entity.setTypeId(ConstService.getPathType((new Short(parent.getTypeId())).toString()));
                } else {
                    entity.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_USER.toString());
                }
                entity.setTaskId(parent.getTaskId());
                entity.setProjectId(parent.getProjectId());
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
//            assert (n > 0);
        }

        return lastEntity;
    }


    @Override
    public SimpleNodeDTO updateNode(@NotNull SimpleNodeDTO src, SimpleNodeDTO parent, UpdateNodeDTO request, Current current) throws CustomException {
        long t = System.currentTimeMillis();

        request = BeanUtils.cleanProperties(request);

        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(StringUtils.left(src.getId(),StringUtils.DEFAULT_ID_LENGTH));
        StorageEntityUnionDTO srcUnion = storageDao.selectStorageEntityUnion(BeanUtils.cleanProperties(query));
        CheckService.check(srcUnion != null,ErrorCode.DataNotFound,"updateNode");
        BeanUtils.copyCleanProperties(request,srcUnion);

        //更改属性
        srcUnion.setNodeName(StringUtils.getFileName(request.getPath()));
        if (isValid(parent)) {
            StorageTreeEntity parentEntity = getParentEntity(parent, request, current);
            CheckService.check(parent != null,ErrorCode.DataNotFound);
            srcUnion.setPid(parentEntity.getId());
            String parentPath = parentEntity.getPath();
            if (StringUtils.isNotEmpty(parentPath)) {
                parentPath += StringUtils.SPLIT_PATH;
            }
            srcUnion.setPath(StringUtils.formatPath(parentPath + srcUnion.getNodeName()));
            srcUnion.setTaskId(parentEntity.getTaskId());
            srcUnion.setProjectId(parentEntity.getProjectId());
        }

        if (!ConstService.isDirectoryType(srcUnion.getTypeId()) && isFileInfoValid(request)){
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
        storageTreeDao.update(srcUnion);

        //获取新节点全路径
        StringBuilder pathBuilder = new StringBuilder();
        if (isValid(parent)) {
            pathBuilder.append(parent.getPath());
            if (pathBuilder.length() > 0) {
                pathBuilder.append(StringUtils.SPLIT_PATH);
            }
        }
        pathBuilder.append(srcUnion.getPath());
        SimpleNodeDTO node = convertToSimpleNodeDTO(srcUnion,pathBuilder.toString());

        log.info("\t----> updateNode:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    private boolean isSameParent(@NotNull SimpleNodeDTO src, SimpleNodeDTO parent, UpdateNodeDTO request){
        StringBuilder pathBuilder = new StringBuilder();
        if (parent != null){
            pathBuilder.append(parent.getPath());
        } else {
            pathBuilder.append(StringUtils.getDirName(src.getPath()));
        }
        if ((request != null) && (!StringUtils.isEmpty(request.getPath()))){
            String dirName = StringUtils.getDirName(request.getPath());
            if (StringUtils.isNotEmpty(dirName)) {
                if (pathBuilder.length() > 0) pathBuilder.append(StringUtils.SPLIT_PATH);
                pathBuilder.append(dirName);
            }
        }
        return StringUtils.isSame(StringUtils.getDirName(src.getPath()),StringUtils.formatPath(pathBuilder.toString()));
    }

    @Override
    public void deleteNodeByIdList(@NotNull List<String> idList, DeleteAskDTO request, Current current) throws CustomException {
        if (ObjectUtils.isNotEmpty(idList)) {
            int n = 0;
            String userId = (request != null) ? request.getLastModifyUserId() : null;
            n += storageTreeDao.fakeDeleteById(idList, userId);
            n += storageFileDao.fakeDeleteById(idList, userId);
            n += storageFileHisDao.fakeDeleteById(idList, userId);
            log.debug("\t----> deleteNodeByIdList：删除了" + n + "条记录");
        }
    }

    @Override
    public void deleteNodeById(@NotNull String id, DeleteAskDTO request, Current current) throws CustomException {
        List<String> idList = new ArrayList<>();
        id = StringUtils.left(id, StringUtils.DEFAULT_ID_LENGTH);
        if (StringUtils.isNotEmpty(id)) {
            idList.add(id);
        }
        deleteNodeByIdList(idList,request,current);
    }

    @Override
    public void deleteNodeList(@NotNull List<SimpleNodeDTO> nodeList, DeleteAskDTO request, Current current) throws CustomException {
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
        deleteNodeByIdList(idList,request,current);
    }

    @Override
    public void deleteNode(@NotNull SimpleNodeDTO node, DeleteAskDTO request, Current current) throws CustomException {
        if (StringUtils.isNotEmpty(node.getId())) {
            List<SimpleNodeDTO> list = new ArrayList<>();
            list.add(node);
            deleteNodeList(list, request, current);
        }
    }

    private boolean isRoot(@NotNull SimpleNodeDTO parent){
        return StringUtils.isEmpty(parent.getId());
    }

    @Override
    public List<SimpleNodeDTO> listChild(@NotNull SimpleNodeDTO parent, Current current) throws CustomException {
        if (isRoot(parent)) return listRoot(null,current);

        CheckService.check(parent.getIsDirectory(),ErrorCode.InvalidParameter,"listChild");
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parent.getId());
        return listNode(query,current);

    }

    @Override
    public List<SimpleNodeDTO> listChildren(@NotNull SimpleNodeDTO parent, Current current) throws CustomException {
        QueryNodeDTO nodeQuery = new QueryNodeDTO();
        if (!isRoot(parent)) {
            CheckService.check(parent.getIsDirectory(),ErrorCode.InvalidParameter,"listChildren");
            QueryNodeInfoDTO infoQuery = new QueryNodeInfoDTO();
            QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
            infoQuery.setTextQuery(txtQuery);
            FullNodeDTO parentInfo = getNodeInfo(parent, infoQuery, current);
            CheckService.check((parentInfo != null) && (parentInfo.getTextInfo() != null) && (StringUtils.isNotEmpty(parentInfo.getTextInfo().getPath())),ErrorCode.DataIsInvalid,"listChildren");
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
    public SimpleNodeDTO updateNodeSimple(@NotNull SimpleNodeDTO src, @NotNull UpdateNodeDTO request, Current current) throws CustomException {
        return updateNode(src,null,request,current);
    }

    private void updateStorageUnion(@NotNull StorageEntityUnionDTO srcUnion){
        int n = 0;
        if (srcUnion.getHisEntity() != null) {
            StorageFileHisEntity hisEntity = srcUnion.getHisEntity();
            n += storageFileHisDao.updateById(hisEntity);
            assert (n > 0);
        }
        if (srcUnion.getFileEntity() != null) {
            StorageFileEntity fileEntity = srcUnion.getFileEntity();
            fileEntity.setMainFileId(StringUtils.left(fileEntity.getMainFileId(),StringUtils.DEFAULT_ID_LENGTH));
            n += storageFileDao.updateById(fileEntity);
            assert (n > 0);
        }
        n += storageTreeDao.updateById(srcUnion);
        assert (n > 0);
    }

    private void insertStorageUnion(@NotNull StorageEntityUnionDTO srcUnion){
        int n = 0;
        if (srcUnion.getHisEntity() != null) {
            StorageFileHisEntity hisEntity = srcUnion.getHisEntity();
            n += storageFileHisDao.insert(BeanUtils.cleanProperties(hisEntity));
//            assert (n > 0);
        }
        if (srcUnion.getFileEntity() != null) {
            StorageFileEntity fileEntity = srcUnion.getFileEntity();
            fileEntity.setMainFileId(StringUtils.left(fileEntity.getMainFileId(),StringUtils.DEFAULT_ID_LENGTH));
            n += storageFileDao.insert(BeanUtils.cleanProperties(fileEntity));
//            assert (n > 0);
        }
        n += storageTreeDao.insert(BeanUtils.cleanProperties(srcUnion));
//        assert (n > 0);
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

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(query.getId()).append(query.getPath()).append(query.getParentPath())
                .append(query.getTypeId()).append(query.getAccountId()).append(query.getPid());
        String key = keyBuilder.toString();
        List<SimpleNodeDTO> list = simpleNodeMap.get(key);
        Integer times = queryTimesMap.get(key);
        final Integer MAX_QUERY_USE_TIMES = 20;
        if ((list == null) || (times > MAX_QUERY_USE_TIMES)) {
            log.info("\t----> listNode_1:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
            list = storageDao.listNode(BeanUtils.cleanProperties(query));
            log.info("\t----> listNode_2:" + (System.currentTimeMillis()-t) + "ms");
            t = System.currentTimeMillis();
            simpleNodeMap.put(key, list);
            queryTimesMap.put(key,0);
        } else {
            times++;
            queryTimesMap.put(key,times);
        }

        log.info("\t----> listNode花费时间:" + (System.currentTimeMillis()-t) + "ms");
        return list;
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
        if (StringUtils.isNotEmpty(entity.getTypeId())) {
            node.setIsDirectory(ConstService.isDirectoryType(entity.getTypeId()));
        } else {
            entity.setTypeId(ConstService.STORAGE_NODE_TYPE_UNKNOWN.toString());
            node.setIsDirectory(false);
        }
        node.setName(entity.getNodeName());
        node.setPath(fullPath);
        Short rangeId = Short.parseShort(ConstService.getRangeId(entity.getTypeId()));
        node.setId(node.getId() + "-" + rangeId);
        String pid = node.getPid();
        if (StringUtils.isEmpty(pid)) {
            pid = StringUtils.isNotEmpty(node.getTaskId()) ? node.getTaskId() : node.getProjectId()
                    + "-" + rangeId;
        }
        node.setPid(pid);
        if (entity.getCreateTime() != null) {
            node.setCreateTimeStamp(entity.getCreateTime().getTime());
        }
        if (entity.getLastModifyTime() != null) {
            node.setLastModifyTimeStamp(entity.getLastModifyTime().getTime());
        }
        if (!StringUtils.isEmpty(entity.getLastModifyUserId())){
            node.setOwnerUserId(entity.getLastModifyUserId());
        }
        return node;
    }


    @Override
    public SimpleNodeDTO createNode(SimpleNodeDTO parent, @NotNull UpdateNodeDTO request, Current current) throws CustomException {

        long t = System.currentTimeMillis();

        request = BeanUtils.cleanProperties(request);

        //取父节点信息
        StorageTreeEntity parentEntity = getParentEntity(parent,request,current);

        //初始化新建节点参数
        StorageEntityUnionDTO nodeUnion = BeanUtils.createCleanFrom(request,StorageEntityUnionDTO.class);
        nodeUnion.reset();
        nodeUnion.setNodeName(StringUtils.getFileName(request.getPath()));
        if (parentEntity != null) {
            nodeUnion.setPid(parentEntity.getId());
            String parentPath = parentEntity.getPath();
            if (StringUtils.isNotEmpty(parentPath)) {
                parentPath += StringUtils.SPLIT_PATH;
            }
            nodeUnion.setPath(StringUtils.formatPath(parentPath + nodeUnion.getNodeName()));
            if (ConstService.isUnknownFileType(nodeUnion.getTypeId())) {
                nodeUnion.setTypeId(ConstService.getFileType(parentEntity.getTypeId()));
            } else if (ConstService.isUnknownDirectoryType(nodeUnion.getTypeId())) {
                nodeUnion.setTypeId(ConstService.getPathType(parentEntity.getTypeId()));
            }
            nodeUnion.setTaskId(parentEntity.getTaskId());
            nodeUnion.setProjectId(parentEntity.getProjectId());
        } else {
            nodeUnion.setPid(null);
            nodeUnion.setPath(nodeUnion.getNodeName());
            if (isValid(parent)) {
                if (ConstService.isUnknownFileType(nodeUnion.getTypeId())) {
                    nodeUnion.setTypeId(ConstService.getFileType((new Short(parent.getTypeId())).toString()));
                } else if (ConstService.isUnknownDirectoryType(nodeUnion.getTypeId())) {
                    nodeUnion.setTypeId(ConstService.getPathType((new Short(parent.getTypeId())).toString()));
                }
                if (StringUtils.isEmpty(nodeUnion.getTaskId())) {
                    nodeUnion.setTaskId(parent.getTaskId());
                }
                if (StringUtils.isEmpty(nodeUnion.getProjectId())) {
                    nodeUnion.setProjectId(parent.getProjectId());
                }
            }
        }

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

        //获取新节点全路径
        StringBuilder pathBuilder = new StringBuilder();
        if (isValid(parent)) {
            pathBuilder.append(parent.getPath());
            if (pathBuilder.length() > 0) {
                pathBuilder.append(StringUtils.SPLIT_PATH);
            }
        }
        pathBuilder.append(nodeUnion.getPath());
        SimpleNodeDTO node = convertToSimpleNodeDTO(nodeUnion,pathBuilder.toString());

        log.info("\t----> createNode:" + (System.currentTimeMillis()-t) + "ms");
        return node;
    }

    @Override
    public SimpleNodeDTO createNodeWithRequestOnly(@NotNull UpdateNodeDTO request, Current current) throws CustomException {
        return createNode(null,request,current);
    }

    private StorageTreeEntity getParentEntity(SimpleNodeDTO parent, @NotNull UpdateNodeDTO request, Current current) throws CustomException {
        StorageTreeEntity parentEntity = null;
        if (StringUtils.isNotEmpty(request.getPath())) {
            String parentPath = StringUtils.getDirName(request.getPath());
            if (StringUtils.isNotEmpty(parentPath)) {
                if (!StringUtils.isStartWith(parentPath, StringUtils.SPLIT_PATH)) {
                    parentPath = StringUtils.SPLIT_PATH + parentPath;
                }
            }
            if (isValid(parent)) {
                parentPath = parent.getPath() + parentPath;
            }
            parent = getNodeByFuzzyPath(parentPath,current);
            if (isValid(parent)) {
                QueryNodeInfoDTO query = new QueryNodeInfoDTO();
                QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
                query.setTextQuery(txtQuery);
                FullNodeDTO parentFullInfo = getNodeInfo(parent, query, current);
                CheckService.check((parentFullInfo != null) && (parentFullInfo.getTextInfo() != null),ErrorCode.DataIsInvalid,"getParentEntity");
                String path = StringUtils.substring(parentPath, StringUtils.length(parentFullInfo.getTextInfo().getPath()) + StringUtils.SPLIT_PATH.length());
                if (StringUtils.isNotEmpty(path)) {
                    path += StringUtils.SPLIT_PATH + StringUtils.getFileName(request.getPath());
                } else {
                    path = StringUtils.getFileName(request.getPath());
                }
                request.setPath(path);
                if (StringUtils.isEmpty(parent.getTaskId())){
                    parent.setTaskId(request.getTaskId());
                }
            }
            String dirName = StringUtils.getDirName(request.getPath());
            if (StringUtils.isNotEmpty(dirName)) {
                parentEntity = insertPathNodeList(parent, dirName, request.getLastModifyUserId(), request.getLastModifyRoleId());
            } else if (isValid(parent)){
                parentEntity = storageTreeDao.selectById(StringUtils.left(parent.getId(),StringUtils.DEFAULT_ID_LENGTH));
            }
        }
        return parentEntity;
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
        if (!StringUtils.isStartWith(fuzzyPath, StringUtils.SPLIT_PATH)) {
            fuzzyPath = StringUtils.SPLIT_PATH + fuzzyPath;
        }
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath(StringUtils.formatPath(fuzzyPath));
        List<SimpleNodeDTO> list = listNode(query, current);
        return ObjectUtils.getFirst(list);
    }
}
