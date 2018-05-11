package com.maoding.FileServer;

import com.maoding.Base.CoreLocalService;
import com.maoding.Common.CheckService;
import com.maoding.Common.Config.WebServiceConfig;
import com.maoding.Common.ConstService;
import com.maoding.Common.zeroc.*;
import com.maoding.CoreFileServer.CoreCreateFileRequest;
import com.maoding.CoreFileServer.CoreFileDataDTO;
import com.maoding.CoreFileServer.CoreFileServer;
import com.maoding.CoreFileServer.Disk.DiskFileServer;
import com.maoding.CoreFileServer.MaodingWeb.WebFileServer;
import com.maoding.CoreUtils.BeanUtils;
import com.maoding.CoreUtils.DigitUtils;
import com.maoding.CoreUtils.ObjectUtils;
import com.maoding.CoreUtils.StringUtils;
import com.maoding.FileServer.Config.FileServerConfig;
import com.maoding.FileServer.Dto.CopyRequestDTO;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Notice.zeroc.NoticeClientPrx;
import com.maoding.Notice.zeroc.NoticeRequestDTO;
import com.maoding.Notice.zeroc.NoticeServicePrx;
import com.maoding.Storage.zeroc.*;
import com.maoding.User.zeroc.*;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.*;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 10:08
 * 描    述 :
 */
@SuppressWarnings("deprecation")
@Service("fileService")
public class FileServiceImpl extends CoreLocalService implements FileService {

    @Autowired
    private FileServerConfig setting;

    @Autowired
    private WebServiceConfig webServiceConfig;

    private CoreFileServer fileServer = new DiskFileServer();

    private Map<String,NodeFileDTO> fileMap = new HashMap<>();
    private Map<String,String> pathMap = new HashMap<>();

    @Override
    public void clearAll(AccountDTO account, Current current) throws CustomException {
        CoreFileServer localServer = getCoreFileServer();
        List<String> keyList = localServer.coreListKey();
        for (String key : keyList) {
            clearKey(account,key,current);
        }
    }

    @Override
    public void clearKey(AccountDTO account, @NotNull String key, Current current) throws CustomException {
        CoreFileServer localServer = getCoreFileServer();
        List<String> keyList = localServer.coreListKey(key);
        if (ObjectUtils.isNotEmpty(keyList)) {
            for (String k : keyList) {
                clearKey(account, k, current);
            }
            //根据实体文件查找文件节点
            QueryFullNodeDTO query = new QueryFullNodeDTO();
            query.setKey(key);
            List<FullNodeDTO> fileList = getStorageService().listFullNode(query);
            if (ObjectUtils.isNotEmpty(fileList)){
                //如果找到，根据文件节点路径更新实体文件路径
                FullNodeDTO fullNode = fileList.get(0);
                renameKey(fullNode,key);
            } else {
                //如果没找到，删除实体文件
                localServer.coreDeleteFile(key);
            }
        }
    }

    private void renameKey(FullNodeDTO fullNode,String key) throws CustomException {

    }

    @Override
    public List<CANodeDTO> listDesignNode(AccountDTO account, Current current) throws CustomException {
        QueryCANodeDTO query = new QueryCANodeDTO();
        query.setUserId(getAccountId(account));
        query.setIsTaskDesigner(ConstService.MODE_TRUE);
        query.setIsDesign(ConstService.MODE_TRUE);
        query.setIsHistory(ConstService.MODE_FALSE);
        return getStorageService().listCANode(query);
    }

    @Override
    public List<CANodeDTO> listCANode(AccountDTO account, Current current) throws CustomException {
        final String ROLE_CHECK_AUDIT = "000011";
        QueryCANodeDTO query = new QueryCANodeDTO();
        query.setUserId(getAccountId(account));
        query.setIsCA(ConstService.MODE_TRUE);
        query.setRoleAttr(ROLE_CHECK_AUDIT);
        query.setIsHistory(ConstService.MODE_FALSE);
        return getStorageService().listCANode(query);
    }


    @Override
    public long getTime(Current current) throws CustomException {
        return (new Date()).getTime();
    }

    @Override
    public void restartWebRole(WebRoleDTO webRole, Current current) throws CustomException {
        setWebRoleStatus(webRole,"0",current);
    }

    @Override
    public void finishWebRole(WebRoleDTO webRole, Current current) throws CustomException {
        setWebRoleStatus(webRole,"1",current);
    }

    @Override
    public void setWebRoleStatus(WebRoleDTO webRole, String statusId, Current current) throws CustomException {
        getUserService().setWebRoleStatus(webRole,statusId);
    }

    @Override
    public WebRoleDTO getWebRole(@NotNull AccountDTO account, @NotNull SimpleNodeDTO node, Current current) {
        QueryWebRoleDTO query = new QueryWebRoleDTO();
        query.setUserId(getAccountId(account));
        query.setTaskId(node.getTaskId());
        List<WebRoleDTO> list = getUserService().listWebRole(query);
        return (ObjectUtils.isNotEmpty(list)) ? list.get(0) : null;
    }

    @Override
    public List<WebRoleDTO> listWebRoleTask(AccountDTO account, Current current) {
        final String ROLE_TYPE_ID_FILETER = "40,41,42,43";
        QueryWebRoleDTO query = new QueryWebRoleDTO();
        query.setRoleId(ROLE_TYPE_ID_FILETER);
        query.setUserId(getAccountId(account));
        return getUserService().listWebRole(query);
    }

    @Override
    public List<WebRoleDTO> listAllWebRole(AccountDTO account, Current current) throws CustomException {
        final String ROLE_TYPE_ID_FILETER = "40,41,42,43";
        QueryWebRoleDTO query = new QueryWebRoleDTO();
        query.setRoleId(ROLE_TYPE_ID_FILETER);
        query.setAccountId(getAccountId(account));
        return getUserService().listWebRole(query);
    }

    @Override
    public boolean isExist(String path, Current current) throws CustomException {
        return isExistForAccount(null,path,current);
    }

    @Override
    public boolean isExistForAccount(AccountDTO account, String path, Current current) throws CustomException {
        SimpleNodeDTO node = getNodeByPathForAccount(account,path,current);
        return (node != null);
    }

    private String getNodeId(SimpleNodeDTO node) {
        return (node != null) ? node.getId() : null;
    }

    private String getId(SuggestionDTO suggestion){
        return (suggestion != null) ? suggestion.getId() : null;
    }

    private String getNameByContent(String content) {
        String name = StringUtils.left(content,"\n");
        if (StringUtils.isNotEmpty(name)){
            name = StringUtils.left(name,255);
        }
        return name;
    }

    private String getNotNullString(String s) {
        return (s != null) ? s : "";
    }

    @Override
    public AnnotateDTO createAnnotateCheck(AccountDTO account, @NotNull SimpleNodeDTO node, @NotNull AnnotateRequestDTO request, Current current) throws CustomException {
        request.setTypeId(ConstService.ANNOTATE_TYPE_CHECK.toString());
        return createAnnotate(account,node,request,current);
    }

    @Override
    public AnnotateDTO createAnnotateAudit(AccountDTO account, @NotNull SimpleNodeDTO node, @NotNull AnnotateRequestDTO request, Current current) throws CustomException {
        request.setTypeId(ConstService.ANNOTATE_TYPE_AUDIT.toString());
        return createAnnotate(account,node,request,current);
    }

    @Override
    public AnnotateDTO createAnnotate(AccountDTO account, @NotNull SimpleNodeDTO node, AnnotateRequestDTO request, Current current) throws CustomException {
        //获取文件
        CheckService.check(!node.getIsDirectory(),ErrorCode.InvalidParameter,"createAnnotate");
        NodeFileDTO file = getFileInfoForAccount(account,node,current);
        CheckService.check(file != null,ErrorCode.DataIsInvalid,"createAnnotate");

        //建立创建申请
        UpdateAnnotateDTO updateRequest = BeanUtils.createCleanFrom(request,UpdateAnnotateDTO.class);
        updateRequest.setStatusId(request.getIsPassed() ? ConstService.ANNOTATE_STATUS_TYPE_PASS.toString() : ConstService.ANNOTATE_STATUS_TYPE_REFUSE.toString());
        updateRequest.setLastModifyUserId(getAccountId(account));

        EmbedElementDTO element = null;
        if (ObjectUtils.isNotEmpty(request.getData())){
            UpdateElementDTO elementRequest = BeanUtils.createCleanFrom(request,UpdateElementDTO.class);
            elementRequest.setDataArray(request.getData());
            element = getStorageService().createEmbedElement(elementRequest);
            if (element != null) {
                List<String> elementIdList = new ArrayList<>();
                elementIdList.add(element.getId());
                updateRequest.setAddElementIdList(elementIdList);
            }
        }
        if (ObjectUtils.isNotEmpty(request.getAddAccessoryList())) {
            List<String> fileIdList = new ArrayList<>();
            for (NodeFileDTO accessory : request.getAddAccessoryList()){
                fileIdList.add(accessory.getId());
            }
            updateRequest.setAddFileIdList(fileIdList);
        }
        AnnotateDTO annotate = getStorageService().createAnnotate(file,updateRequest);
        annotate.setElement(element);
        annotate.setAccessoryList(request.getAddAccessoryList());
        return annotate;
    }

    @Override
    public AnnotateDTO updateAnnotate(AccountDTO account, @NotNull AnnotateDTO annotate, @NotNull AnnotateRequestDTO request, Current current) throws CustomException {
        //建立更新申请
        UpdateAnnotateDTO updateRequest = BeanUtils.createCleanFrom(request,UpdateAnnotateDTO.class);
        updateRequest.setStatusId(request.getIsPassed() ? ConstService.ANNOTATE_STATUS_TYPE_PASS.toString() : ConstService.ANNOTATE_STATUS_TYPE_REFUSE.toString());
        updateRequest.setLastModifyUserId(getAccountId(account));

        EmbedElementDTO element = annotate.getElement();
        if (ObjectUtils.isNotEmpty(request.getData())){
            UpdateElementDTO elementRequest = BeanUtils.createCleanFrom(updateRequest,UpdateElementDTO.class);
            elementRequest.setDataArray(request.getData());

            if ((element != null) && (StringUtils.isNotEmpty(element.getId()))) {
                element = getStorageService().updateEmbedElement(element,elementRequest);
            } else {
                element = getStorageService().createEmbedElement(elementRequest);
                if (element != null) {
                    List<String> elementIdList = new ArrayList<>();
                    elementIdList.add(element.getId());
                    updateRequest.setAddElementIdList(elementIdList);
                }
            }
        }

        List<NodeFileDTO> fileList = annotate.getAccessoryList();
        if (ObjectUtils.isNotEmpty(request.getDelAccessoryList())) {
            List<String> fileIdList = new ArrayList<>();
            for (NodeFileDTO accessory : request.getDelAccessoryList()){
                fileIdList.add(accessory.getId());
                if (fileList != null) {
                    for (NodeFileDTO annotateAccessory : fileList) {
                        if (StringUtils.isSame(annotateAccessory.getId(), accessory.getId())) {
                            fileList.remove(annotateAccessory);
                            break;
                        }
                    }
                }
            }
            updateRequest.setDelAttachmentIdList(fileIdList);
        }
        if (ObjectUtils.isNotEmpty(request.getAddAccessoryList())) {
            List<String> fileIdList = new ArrayList<>();
            if (fileList == null) {
                fileList = new ArrayList<>();
            }
            for (NodeFileDTO accessory : request.getAddAccessoryList()){
                fileIdList.add(accessory.getId());
                fileList.add(accessory);
            }
            updateRequest.setAddFileIdList(fileIdList);
        }
        annotate = getStorageService().updateAnnotate(annotate,updateRequest);
        annotate.setElement(element);
        annotate.setAccessoryList(fileList);
        return annotate;
    }

    @Override
    public List<AnnotateDTO> listAnnotate(AccountDTO account, @NotNull QueryAnnotateDTO query, Current current) throws CustomException {
        return getStorageService().listAnnotate(query);
    }

    @Override
    public NodeFileDTO addAccessory(AccountDTO account, @NotNull AnnotateDTO annotate, @NotNull AccessoryRequestDTO request, Current current) throws CustomException {
        //创建文件
        UpdateNodeFileDTO storageRequest = new UpdateNodeFileDTO();
        storageRequest.setServerTypeId(setting.getServerTypeId());
        storageRequest.setServerAddress(setting.getServerAddress());
        storageRequest.setBaseDir(setting.getBaseDir());
        if (ObjectUtils.isNotEmpty(request.getData())){
            CoreCreateFileRequest coreRequest = null;
            if (StringUtils.isNotEmpty(request.getPath())){
                coreRequest = new CoreCreateFileRequest();
                coreRequest.setPath(request.getPath());
            }
            String key = createLocalKey(coreRequest);
            CoreFileDataDTO data = new CoreFileDataDTO();
            data.setData(request.getData());
            getCoreFileServer().coreWriteFile(data,key);
            storageRequest.setReadOnlyKey(key);
        }
        storageRequest.setLastModifyUserId(getAccountId(account));
        NodeFileDTO file = getStorageService().createNodeFileWithRequestOnly(storageRequest);

        //添加附件
        if ((file != null) && (StringUtils.isNotEmpty(file.getId()))) {
            List<String> fileIdList = new ArrayList<>();
            fileIdList.add(file.getId());
            UpdateAnnotateDTO addRequest = new UpdateAnnotateDTO();
            addRequest.setAddFileIdList(fileIdList);
            getStorageService().updateAnnotate(annotate,addRequest);
        }
        return file;
    }

    @Override
    public void deleteAccessory(AccountDTO account, @NotNull AnnotateDTO annotate, @NotNull NodeFileDTO accessory, Current current) throws CustomException {
        List<NodeFileDTO> fileList = annotate.getAccessoryList();
        if (fileList != null){
            for (NodeFileDTO file : fileList) {
                if (StringUtils.isSame(file.getId(),accessory.getId())){
                    List<String> fileIdList = new ArrayList<>();
                    fileIdList.add(file.getId());
                    UpdateAnnotateDTO delRequest = new UpdateAnnotateDTO();
                    delRequest.setDelAttachmentIdList(fileIdList);
                    getStorageService().updateAnnotate(annotate,delRequest);
                    fileList.remove(file);
                    break;
                }
            }
        }
    }


    private int writeRWFile(AccountDTO account, @NotNull NodeFileDTO file, @NotNull FileDataDTO data, String path, boolean isReadOnly, Current current) throws CustomException {
        String key = getFileKey(file,isReadOnly);
        if (StringUtils.isEmpty(key)){
            UpdateNodeFileDTO updateRequest = createUpdateRequestForLocalKey(file,path,isReadOnly);
            CheckService.check(updateRequest != null,ErrorCode.DataIsInvalid,"writeRWFile");
            updateRequest.setLastModifyUserId(getAccountId(account));
            file = getStorageService().updateNodeFile(file,updateRequest);
            CheckService.check((file != null) && (StringUtils.isNotEmpty(file.getId())),ErrorCode.DataIsInvalid,"writeRWFile");
            fileMap.put(file.getId(),file);
            key = getFileKey(file,isReadOnly);
        }
        CoreFileDataDTO coreData = BeanUtils.createFrom(data,CoreFileDataDTO.class);
        return getCoreFileServer().coreWriteFile(coreData,key);
    }

    @Override
    public int writeAccessory(AccountDTO account, @NotNull NodeFileDTO file, FileDataDTO data, Current current) throws CustomException {
        return writeRWFile(account,file,data,null,true,current);
    }

    @Override
    public int writeFile(AccountDTO account, @NotNull NodeFileDTO file, @NotNull FileDataDTO data, String path, Current current) throws CustomException {
        return writeRWFile(account,file,data,path,false,current);
    }

    @Override
    public int writeFileAndRelease(AccountDTO account, @NotNull NodeFileDTO file, FileDataDTO data, String path, long fileLength, Current current) throws CustomException {
        int n = writeFile(account,file,data,path,current);
        releaseFile(account,file,path,current);
        return n;
    }

    @Override
    public void releaseFile(AccountDTO account, @NotNull NodeFileDTO file, String path, Current current) throws CustomException {
        String key = getFileKey(file,false);
        if (StringUtils.isNotEmpty(key)) {
            CoreFileServer localServer = getCoreFileServer();
            String md5 = localServer.coreCalcChecksum(key);
            if (StringUtils.isNotSame(file.getFileChecksum(),md5)){
                File localFile = localServer.coreGetFile(key);
                String readOnlyKey = fileServer.coreCreateFile(path, localFile);
                UpdateNodeFileDTO updateRequest = new UpdateNodeFileDTO();
                updateRequest.setReadOnlyKey(readOnlyKey);
                updateRequest.setFileLength(localFile.length());
                updateRequest.setFileMd5(md5);
                file = getStorageService().updateNodeFile(file, updateRequest);
                String id = file.getId();
                fileMap.put(id, file);
                CoreFileServer fileServer = getCoreFileServer(file.getServerTypeId(), file.getServerAddress(), file.getBaseDir());
                //*// 本行以双斜杠开始根据是否本地文件打开上传线程，单斜杠不打开上传线程
                if (!isLocalFile(file))
                //*/
                {
                    new Thread() {
                        @Override
                        public void run() {
                            String writableKey = fileServer.coreCreateFile(path, localFile);
                            String readOnlyMirrorKey = localServer.coreCreateFile(path, localFile);
                            updateRequest.setWritableKey(writableKey);
                            updateRequest.setReadOnlyMirrorKey(readOnlyMirrorKey);
                            updateRequest.setMirrorTypeId(setting.getServerTypeId());
                            updateRequest.setMirrorAddress(setting.getServerAddress());
                            updateRequest.setMirrorBaseDir(setting.getBaseDir());
                            NodeFileDTO updatedFile = fileMap.get(id);
                            try {
                                updatedFile = getStorageService().updateNodeFile(updatedFile, updateRequest);
                            } catch (CustomException e) {
                                log.error("发布文件时出错", e);
                            }
                            fileMap.put(id, updatedFile);
                        }
                    }.start();
                }
            }
        }
    }

    @Override
    public void reloadFile(AccountDTO account, @NotNull NodeFileDTO file, String path, Current current) throws CustomException {
        String key = getFileKey(file,true);
        CheckService.check(StringUtils.isNotEmpty(key),ErrorCode.InvalidParameter,"reloadFile");

        CoreFileServer fileServer = getCoreFileServer(file.getServerTypeId(),file.getServerAddress(),file.getBaseDir());
        CoreFileServer localServer = getCoreFileServer();
        File localFile = fileServer.coreGetFile(key);
        String localKey = localServer.coreCreateFile(path,localFile);
        UpdateNodeFileDTO updateRequest = new UpdateNodeFileDTO();
        if (isLocalFile(file)) {
            updateRequest.setWritableKey(localKey);
        } else {
            updateRequest.setMirrorTypeId(setting.getServerTypeId());
            updateRequest.setMirrorAddress(setting.getServerAddress());
            updateRequest.setMirrorBaseDir(setting.getBaseDir());
            updateRequest.setWritableMirrorKey(localKey);
        }
        file = getStorageService().updateNodeFile(file,updateRequest);
        String id = file.getId();
        fileMap.put(id,file);
        if (!isLocalFile(file)){
            new Thread() {
                @Override
                public void run() {
                    String writableKey = fileServer.coreCreateFile(path,localFile);
                    String readOnlyMirrorKey = localServer.coreCreateFile(path,localFile);
                    updateRequest.setWritableKey(writableKey);
                    updateRequest.setReadOnlyMirrorKey(readOnlyMirrorKey);
                    NodeFileDTO updatedFile = fileMap.get(id);
                    try {
                        updatedFile = getStorageService().updateNodeFile(updatedFile, updateRequest);
                    } catch (CustomException e) {
                        log.error("还原文件时出错",e);
                    }
                    fileMap.put(id,updatedFile);
                }
            }.start();
        }
    }


    private boolean isLocalFile(@NotNull NodeFileDTO file){
        return setting.isLocalServer(file.getServerTypeId(),file.getServerAddress(),file.getBaseDir());
    }
    private String getFileKey(NodeFileDTO file, boolean isReadOnly){
        if (file == null) {
            return null;
        } else if (isReadOnly){
            return file.getReadOnlyKey();
//            return (isLocalFile(file)) ? file.getReadOnlyKey() : file.getReadOnlyMirrorKey();
        } else {
            return file.getWritableKey();
//            return (isLocalFile(file)) ? file.getWritableKey() : file.getWritableMirrorKey();
        }
    }
    private String getFileKey(@NotNull FullNodeDTO file, boolean isReadOnly){
        return getFileKey(file.getFileInfo(),isReadOnly);
    }
    private String getFileKey(@NotNull FullNodeDTO file){
        SimpleNodeDTO nodeInfo = file.getBasic();
        assert (nodeInfo != null) && !(nodeInfo.getIsDirectory());
        return getFileKey(file,nodeInfo.getIsReadOnly());
    }
    private String createLocalKey(@NotNull CoreCreateFileRequest request){
        CoreFileServer server = getCoreFileServer();
        assert (server != null);
        return server.coreCreateFile(request);
    }
    private File getSrcFile(String serverTypeId,String serverAddress,String baseDir,String key){
        File file = null;
        if (StringUtils.isNotEmpty(key)){
            CoreFileServer server = getCoreFileServer(serverTypeId, serverAddress, baseDir);
            assert (server != null);
            file = server.coreGetFile(key);
        }
        return file;
    }
    private UpdateNodeDTO updateKey(@NotNull UpdateNodeDTO updateRequest, String key, boolean isReadOnly, boolean isMirror){
        if (isReadOnly && !isMirror) updateRequest.setReadOnlyKey(key);
        else if (!isReadOnly && !isMirror) updateRequest.setWritableKey(key);
        else if (isReadOnly) updateRequest.setReadOnlyMirrorKey(key);
        else updateRequest.setWritableMirrorKey(key);
        return updateRequest;
    }

    /**
     * 根据文件信息创建本地文件或本地镜像文件，并返回文件信息更改申请
     * @param fileInfo 当前的文件节点信息
     * @param path 要创建的本地文件的路径，如果存在同名文件，需要为新建文件添加时间戳，如果中间路径不存在，需要创建中间路径
     * @param fileLength 要创建的本地文件的长度，可以为0
     * @param isReadOnly 需要创建用于只读还是读写的文件，根据这个输入决定更改只读文件属性还是可写文件属性
     * @return 用于更改节点属性的对象
     */
    private UpdateNodeDTO createUpdateRequestForLocalKey(NodeFileDTO fileInfo,String path,long fileLength,boolean isReadOnly){
        CoreCreateFileRequest coreCreateRequest = new CoreCreateFileRequest();
        coreCreateRequest.setPath(path);
        if (fileLength > 0) {
            coreCreateRequest.setFileLength(fileLength);
        }
        UpdateNodeDTO updateRequest = new UpdateNodeDTO();
        if (fileInfo == null){ //如果文件信息为空，则创建本地文件
            String key = createLocalKey(coreCreateRequest);
            updateRequest = updateKey(updateRequest,key,isReadOnly,false);
            updateRequest.setServerTypeId(setting.getServerTypeId());
            updateRequest.setServerAddress(setting.getServerAddress());
            updateRequest.setBaseDir(setting.getBaseDir());
        } else if (isLocalFile(fileInfo)) { //如果是本地文件，则从另一侧复制,或创建本地文件
            String otherKey = (!isReadOnly) ? fileInfo.getReadOnlyKey() : fileInfo.getWritableKey();
            if (StringUtils.isNotEmpty(otherKey)) {
                File srcFile = getSrcFile(fileInfo.getServerTypeId(), fileInfo.getServerAddress(), fileInfo.getBaseDir(), otherKey);
                if (srcFile != null) coreCreateRequest.setSrcFile(srcFile);
            }
            String key = createLocalKey(coreCreateRequest);
            updateRequest = updateKey(updateRequest,key,isReadOnly,false);
            BeanUtils.copyCleanProperties(updateRequest,fileInfo);
            updateRequest.setServerTypeId(setting.getServerTypeId());
            updateRequest.setServerAddress(setting.getServerAddress());
            updateRequest.setBaseDir(setting.getBaseDir());
        } else { //如果文件不是本地文件，则创建本地镜像文件
            String remoteKey = (isReadOnly) ? fileInfo.getReadOnlyKey() : fileInfo.getWritableKey();
            if (StringUtils.isNotEmpty(remoteKey)) {
                File srcFile = getSrcFile(fileInfo.getServerTypeId(), fileInfo.getServerAddress(), fileInfo.getBaseDir(), remoteKey);
                if (srcFile != null) coreCreateRequest.setSrcFile(srcFile);
            }
            String key = createLocalKey(coreCreateRequest);
            updateRequest = updateKey(updateRequest,key,isReadOnly,true);
            BeanUtils.copyCleanProperties(updateRequest,fileInfo);
            updateRequest.setMirrorTypeId(setting.getServerTypeId());
            updateRequest.setMirrorAddress(setting.getServerAddress());
            updateRequest.setMirrorBaseDir(setting.getBaseDir());
        }
        return updateRequest;
    }
    private UpdateNodeFileDTO createUpdateRequestForLocalKey(@NotNull NodeFileDTO nodeFile,String path,boolean isReadOnly){
        UpdateNodeDTO request = createUpdateRequestForLocalKey(nodeFile,path,0,isReadOnly);
        return BeanUtils.createCleanFrom(request,UpdateNodeFileDTO.class);
    }
    private UpdateNodeFileDTO createUpdateRequestForLocalKey(@NotNull NodeFileDTO nodeFile,boolean isReadOnly){
        return createUpdateRequestForLocalKey(nodeFile,null,isReadOnly);
    }
    private UpdateNodeDTO createUpdateRequestForLocalKey(@NotNull FullNodeDTO fileNode,long fileLength,boolean isReadOnly){
        String path = (fileNode.getTextInfo() != null) ? fileNode.getTextInfo().getPath() : null;
        return createUpdateRequestForLocalKey(fileNode.getFileInfo(),path,fileLength,isReadOnly);
    }
    private UpdateNodeDTO createUpdateRequestForLocalKey(@NotNull FullNodeDTO fileNode,boolean isReadOnly){
        return createUpdateRequestForLocalKey(fileNode,0,isReadOnly);
    }
    private UpdateNodeDTO createUpdateRequestForLocalKey(@NotNull FullNodeDTO fileNode){
        SimpleNodeDTO nodeInfo = fileNode.getBasic();
        assert(nodeInfo != null);
        return createUpdateRequestForLocalKey(fileNode,nodeInfo.getFileLength(),nodeInfo.getIsReadOnly());
    }

    @Override
    public String getNodePath(SimpleNodeDTO node, Current current) throws CustomException {
        return getNodePathForAccount(getCurrentAccount(current),node,current);
    }

    @Override
    public String getNodePathForAccount(AccountDTO account, SimpleNodeDTO node, Current current) throws CustomException {
        String path = null;
        if (isValid(node)) {
            String id = node.getId();
            if (StringUtils.isNotEmpty(id)) {
                path = pathMap.get(id);
                //*/// 本行如果以双斜杠开始则使用缓存，单斜杠开始为不使用缓存
                if (StringUtils.isEmpty(path))
                //*/
                {
                    path = node.getPath();
                    if (StringUtils.isNotEmpty(path)) {
                        pathMap.put(id, path);
                    }
                }
            }
        } else {
            path = StringUtils.SPLIT_PATH;
        }
        return path;
    }

    @Override
    public boolean isEmpty(@NotNull SimpleNodeDTO node, Current current) throws CustomException {
        List<SimpleNodeDTO> list = getStorageService().listChild(node);
        return ObjectUtils.isEmpty(list);
    }

    @Override
    public FullNodeDTO getFullNodeWithHis(@NotNull SimpleNodeDTO node, Current current) throws CustomException {
        return getFullNodeWithHisForAccount(getCurrentAccount(current),node,current);
    }

    @Override
    public FullNodeDTO getFullNodeWithHisForAccount(AccountDTO account, @NotNull SimpleNodeDTO node, Current current) throws CustomException {
        QueryNodeInfoDTO infoQuery = new QueryNodeInfoDTO();
        infoQuery.setFileQuery(getFileQuery());
        QueryNodeInfoHistoryDTO hisQuery = new QueryNodeInfoHistoryDTO();
        hisQuery.setHistoryEndTimeStamp(node.getLastModifyTimeStamp());
        infoQuery.setHistoryQuery(hisQuery);
        return getNodeInfoForAccount(account,node,infoQuery,current);
    }

    @Override
    public NodeFileDTO getFileInfo(@NotNull SimpleNodeDTO node, Current current) throws CustomException {
        return getFileInfoForAccount(getCurrentAccount(current),node,current);
    }

    @Override
    public NodeFileDTO getFileInfoForAccount(AccountDTO account,  @NotNull SimpleNodeDTO node, Current current) throws CustomException {
        CheckService.check(!node.getIsDirectory(),ErrorCode.InvalidParameter,"getFileInfoForAccount");
        String id = StringUtils.left(node.getId(),StringUtils.DEFAULT_ID_LENGTH);
        NodeFileDTO fileInfo = fileMap.get(id);
        //*// 本行如果以双斜杠开始则使用缓存，单斜杠开始为不使用缓存
        if (fileInfo == null)
        //*/
        {
            QueryNodeInfoDTO infoQuery = new QueryNodeInfoDTO();
            infoQuery.setFileQuery(getFileQuery());
            FullNodeDTO file = getNodeInfoForAccount(account, node, infoQuery, current);
            if ((file != null) && (file.getFileInfo() != null) && (StringUtils.isNotEmpty(file.getFileInfo().getId()))) {
                fileInfo = file.getFileInfo();
                fileMap.put(id, fileInfo);
            }
        }
        return fileInfo;
    }

    private QueryNodeInfoFileDTO getFileQuery(){
        QueryNodeInfoFileDTO fileQuery = new QueryNodeInfoFileDTO();
        fileQuery.setMirrorServerTypeId(setting.getServerTypeId());
        fileQuery.setMirrorServerAddress(setting.getServerAddress());
        fileQuery.setMirrorBaseDir(setting.getBaseDir());
        return fileQuery;
    }

    @Override
    public FullNodeDTO getNodeInfo(@NotNull SimpleNodeDTO node, QueryNodeInfoDTO request, Current current) throws CustomException {
        return getNodeInfoForAccount(getCurrentAccount(current),node,request,current);
    }

    @Override
    public FullNodeDTO getNodeInfoForAccount(AccountDTO account, @NotNull SimpleNodeDTO node, QueryNodeInfoDTO request, Current current) throws CustomException {
        return getStorageService().getNodeInfo(node,request);
    }

    @Override
    public List<SimpleNodeDTO> listChildNode(@NotNull SimpleNodeDTO parent, Current current) throws CustomException {
        return listChildNodeForAccount(getCurrentAccount(current),parent,current);
    }

    @Override
    public List<SimpleNodeDTO> listChildNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO parent, Current current) throws CustomException {
        if (isRootNode(parent)) return listRootNodeForAccount(account,current);
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parent.getId());
        return listNodeForAccount(account,query,current);
    }

    @Override
    public List<SimpleNodeDTO> listChildrenNode(SimpleNodeDTO parent, Current current) throws CustomException {
        return listChildrenNodeForAccount(getCurrentAccount(current),parent,current);
    }

    @Override
    public List<SimpleNodeDTO> listChildrenNodeForAccount(AccountDTO account, SimpleNodeDTO parent, Current current) throws CustomException {
        if (isRootNode(parent)) return listAllNodeForAccount(account,current);
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(parent.getPath());
        return listNodeForAccount(account,query,current);
    }

    private boolean isRootNode(@NotNull SimpleNodeDTO parent){
        return StringUtils.isEmpty(parent.getId());
    }

    @Override
    public List<SimpleNodeDTO> listRootNode(Current current) throws CustomException {
        return listRootNodeForAccount(getCurrentAccount(current),current);
    }

    @Override
    public List<SimpleNodeDTO> listRootNodeForAccount(AccountDTO account, Current current) throws CustomException {
        CheckService.check(StringUtils.isNotEmpty(getAccountId(account)),ErrorCode.NoPermission,"listRootNodeForAccount");
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid("-");
        return listNodeForAccount(account,query,current);
    }

    @Override
    public SimpleNodeDTO getNodeByFuzzyPath(String fuzzyPath, Current current) throws CustomException {
        return getNodeByFuzzyPathForAccount(getCurrentAccount(current),fuzzyPath,current);
    }

    @Override
    public List<SimpleNodeDTO> listWebArchiveDir(String projectId, Current current) throws CustomException {
        return listWebArchiveDirForAccount(getCurrentAccount(current),
                projectId,current);
    }

    @Override
    public List<SimpleNodeDTO> listWebArchiveDirForAccount(AccountDTO account, String projectId, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setAccountId(getAccountId(account));
        query.setProjectId(projectId);
        query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
        List<SimpleNodeDTO> nodeList = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
        for (SimpleNodeDTO node : nodeList) {
            setAttr(node,getAccountId(account));
        }
        return nodeList;
    }


    private boolean isSameServer(CoreFileServer srcServer,CoreFileServer dstServer){
        return (srcServer != null) && (dstServer != null) && (dstServer == srcServer);
    }

    private boolean isWebServer(CoreFileServer server){
        return (server instanceof WebFileServer);
    }

    private boolean isDstWebServer(@NotNull CopyRequestDTO request){
        return (ConstService.FILE_SERVER_TYPE_WEB.equals(request.getDstServerTypeId()));
    }

    private boolean isSameServer(@NotNull CopyRequestDTO request){
        boolean isSame = true;
        Short srcTypeId = ConstService.FILE_SERVER_TYPE_DISK;
        if ((request.getSrcServerTypeId() != null) && (ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(request.getSrcServerTypeId()))) {
            srcTypeId = request.getSrcServerTypeId();
        }
        Short dstTypeId = ConstService.FILE_SERVER_TYPE_DISK;
        if ((request.getDstServerTypeId() != null) && (ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(request.getDstServerTypeId()))) {
            dstTypeId = request.getDstServerTypeId();
        }

        if (!(srcTypeId.equals(dstTypeId))) isSame = false;
        else if (!isSameAddress(request.getSrcServerAddress(),request.getDstServerAddress())) isSame = false;

        return isSame;
    }

    private boolean isSameAddress(String srcAddress,String dstAddress){
        return (srcAddress == null) || (dstAddress == null)
                || StringUtils.isSame(StringUtils.getFileServerAddress(srcAddress),StringUtils.getFileServerAddress(dstAddress));
    }

    @Override
    public CommitListResultDTO issueNodeList(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return issueNodeListForAccount(getCurrentAccount(current),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO issueNodeListForAccount(AccountDTO account, @NotNull List<SimpleNodeDTO> srcList, CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE.toString());
        return commitNodeListForAccount(account,srcList,request,current);
    }

    @Override
    public SimpleNodeDTO issueNode(@NotNull SimpleNodeDTO src, CommitRequestDTO request, Current current) throws CustomException {
        return issueNodeForAccount(getCurrentAccount(current),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO issueNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, CommitRequestDTO request, Current current) throws CustomException {        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK.toString());
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE.toString());
        return commitNodeForAccount(account,src,request,current);
    }


    private void updateStringElement(@NotNull StringElementDTO stringElement, SimpleNodeDTO node, NodeTextDTO txtInfo, CommitRequestDTO request, String actionTypeId){
        BeanUtils.copyCleanProperties(node,stringElement);
        BeanUtils.copyCleanProperties(txtInfo,stringElement);
        stringElement.setSrcPath(txtInfo.getPath());
        BeanUtils.copyCleanProperties(request,stringElement);
        stringElement.setSkyPid(request.getPid());
        stringElement.setActionId(actionTypeId);
        stringElement.setActionName(ConstService.getActionName(DigitUtils.parseShort(actionTypeId)));
    }

    @Override
    public SimpleNodeDTO changeNodeOwner(SimpleNodeDTO src, UserDTO dstOwner, Current current) throws CustomException {
        return changeNodeOwnerForAccount(getCurrentAccount(current),
                src,dstOwner,current);
    }

    @Override
    public SimpleNodeDTO changeNodeOwnerForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull UserDTO dstOwner, Current current) throws CustomException {
        CheckService.check(!isReadOnly(src,account),ErrorCode.NoPermission);
        UpdateNodeDTO updateRequest = new UpdateNodeDTO();
        updateRequest.setOwnerUserId(dstOwner.getId());
        return getStorageService().updateNode(src,null,updateRequest);
    }

    private String getAccountId(AccountDTO account){
        return (account != null) ? account.getId() : null;
    }

    private boolean isProject(short nodeType) {
        return ConstService.isAttrTrue(ConstService.CLASSIC_TYPE_STORAGE_NODE,nodeType,ConstService.POS_IS_PROJECT);
    }

    private boolean isIssue(short nodeType) {
        return ConstService.isAttrTrue(ConstService.CLASSIC_TYPE_STORAGE_NODE,nodeType,ConstService.POS_IS_ISSUE);
    }

    private boolean isTask(short nodeType) {
        return ConstService.isAttrTrue(ConstService.CLASSIC_TYPE_STORAGE_NODE,nodeType,ConstService.POS_IS_ISSUE);
    }

    private boolean isCA(short nodeType) {
        return ConstService.isAttrTrue(ConstService.CLASSIC_TYPE_STORAGE_NODE,nodeType,ConstService.POS_IS_CA);
    }

    private boolean isCommit(short nodeType) {
        return ConstService.isAttrTrue(ConstService.CLASSIC_TYPE_STORAGE_NODE,nodeType,ConstService.POS_IS_COMMIT);
    }

    private boolean isWeb(short nodeType) {
        return ConstService.isAttrTrue(ConstService.CLASSIC_TYPE_STORAGE_NODE,nodeType,ConstService.POS_IS_WEB);
    }

    private boolean isHistory(short nodeType) {
        return ConstService.isAttrTrue(ConstService.CLASSIC_TYPE_STORAGE_NODE,nodeType,ConstService.POS_IS_HISTORY);
    }

    private boolean isReadOnlyType(short nodeType) {
        return isProject(nodeType)
            || isIssue(nodeType)
            || isTask(nodeType)
            || isCA(nodeType)
            || isCommit(nodeType)
            || isWeb(nodeType)
            || isHistory(nodeType);
    }

    private boolean isReadOnly(@NotNull SimpleNodeDTO src,AccountDTO account){
        return isReadOnly(DigitUtils.parseShort(src.getTypeId()),src.getOwnerUserId(),getAccountId(account));
    }

    private boolean isReadOnly(short nodeType,String ownerUserId, String accountId){
        return isReadOnlyType(nodeType)
                || (StringUtils.isNotEmpty(ownerUserId)
                    && (StringUtils.isNotSame(ownerUserId,accountId)));
    }

    private boolean isCanCreateChildType(short nodeType) {
        return !isProject(nodeType)
                && !isCommit(nodeType)
                && !isWeb(nodeType)
                && !isHistory(nodeType);
    }

    private SimpleNodeDTO setAttr(@NotNull SimpleNodeDTO node, String accountId) {
        short nodeType = DigitUtils.parseShort(node.getTypeId());
        node.setIsReadOnly(isReadOnly(nodeType,node.getOwnerUserId(),accountId));
        node.setCanCreateChild(isCanCreateChildType(nodeType));
        return node;
    }

    @Override
    public boolean login(LoginDTO loginInfo, Current current) throws CustomException {
        return getUserService().login(loginInfo);
    }

    @Override
    public List<String> setNoticeClient(String userId, NoticeClientPrx client, Current current) throws CustomException {
        getNoticeServicePrx().subscribeTopicForUser(userId,client);
        return getNoticeServicePrx().listSubscribedTopic(userId);
    }

    private UserServicePrx getUserService() {
        return setting.getUserService();
    }


    private AccountDTO getCurrentAccount(Current current){
        return getUserService().getCurrent();
    }


    private NoticeServicePrx getNoticeServicePrx(){
        return setting.getNoticeService();
    }

    private StorageServicePrx getStorageService(){
        return setting.getStorageService();
    }
    private CoreFileServer getCoreFileServer(String serverTypeId, String serverAddress,String baseDir) {
        return setting.getCoreFileServer(serverTypeId,serverAddress,baseDir);
    }
    private CoreFileServer getCoreFileServer(Short serverTypeId, String serverAddress) {
        return getCoreFileServer(serverTypeId.toString(),serverAddress,null);
    }
    private CoreFileServer getCoreFileServer() {
        return getCoreFileServer(null,null,null);
    }
    private FileServicePrx getRemoteFileService(String serverAddress) {
        return setting.getRemoteFileService(serverAddress);
    }

    @Override
    public List<ProjectRoleDTO> listProjectRoleByProjectId(String projectId, Current current) throws CustomException {
        return listProjectRoleByProjectIdForAccount(getCurrentAccount(current),
                projectId,current);
    }

    @Override
    public List<ProjectRoleDTO> listProjectRoleByProjectIdForAccount(AccountDTO account, String projectId, Current current) throws CustomException {
        return getUserService().listProjectRoleByProjectId(projectId);
    }

    @Override
    public List<IdNameDTO> listMajor(Current current) throws CustomException {
        return listMajorForAccount(getCurrentAccount(current),
                current);
    }

    @Override
    public List<IdNameDTO> listMajorForAccount(AccountDTO account, Current current) throws CustomException {
        return ConstService.listMajor();
    }

    @Override
    public List<IdNameDTO> listAction(Current current) throws CustomException {
        return listActionForAccount(getCurrentAccount(current),
                current);
    }

    @Override
    public List<IdNameDTO> listActionForAccount(AccountDTO account, Current current) throws CustomException {
        return ConstService.listAction();
    }

    @Override
    public boolean deleteNode(SimpleNodeDTO src, Current current) throws CustomException {
        return deleteNodeForAccount(getCurrentAccount(current),
                src,current);
    }

    @Override
    public boolean deleteNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, Current current) throws CustomException {
        if (isValid(src)) {
            DeleteAskDTO deleteAsk = new DeleteAskDTO();
            deleteAsk.setLastModifyUserId(getAccountId(account));
            getStorageService().deleteNode(src,deleteAsk);
        }
        return true;
    }

    @Override
    public boolean setFullNodeLength(FullNodeDTO src, long fileLength, Current current) throws CustomException {
        return false;
    }

    @Override
    public boolean setFullNodeLengthForAccount(AccountDTO account, FullNodeDTO src, long fileLength, Current current) throws CustomException {
        return false;
    }

    @Override
    public boolean setNodeLength(SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        return setNodeLengthForAccount(getCurrentAccount(current),
                src,fileLength,current);
    }

    @Override
    public void setFileLength(AccountDTO account, @NotNull NodeFileDTO file, long fileLength, Current current) throws CustomException {
        String updatedKey = null;
        String updatedMirrorKey = null;
        String key = file.getWritableKey();
        CoreFileServer coreServer = getCoreFileServer(file.getServerTypeId(), file.getServerAddress(), file.getBaseDir());
        if (StringUtils.isNotEmpty(key)) {
            if (fileLength <= 0) {
                fileLength = coreServer.coreGetFileLength(key);
            }
            coreServer.coreSetFileLength(key, fileLength);
        } else {
            updatedKey = coreServer.coreCreateFile(fileLength);
        }
        if (!isLocalFile(file)) {
            String mirrorKey = file.getWritableMirrorKey();
            CoreFileServer localServer = getCoreFileServer();
            if (StringUtils.isNotEmpty(mirrorKey)) {
                localServer.coreSetFileLength(mirrorKey, fileLength);
            } else {
                updatedMirrorKey = localServer.coreCreateFile(fileLength);
            }
        }
        UpdateNodeFileDTO request = new UpdateNodeFileDTO();
        request.setFileLength(fileLength);
        request.setWritableKey(updatedKey);
        request.setWritableMirrorKey(updatedMirrorKey);
        NodeFileDTO updatedFile = getStorageService().updateNodeFile(file,request);
        updateFileMap(file.getId(),updatedFile);
    }

    private void updateFileMap(@NotNull String id, NodeFileDTO updatedFile) {
        if (StringUtils.isNotEmpty(id)) {
            if (updatedFile != null) {
                fileMap.put(id, updatedFile);
            } else {
                fileMap.remove(id);
            }
        }
    }

    @Override
    public boolean setNodeLengthForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        CheckService.check(!src.getIsDirectory(),ErrorCode.InvalidParameter,"setNodeLengthForAccount");
        NodeFileDTO file = getFileInfoForAccount(account,src,current);
        if ((file != null) && (StringUtils.isNotEmpty(file.getWritableKey()))) {
            if (StringUtils.isEmpty(file.getWritableKey())){
            }
            setFileLength(account,file,fileLength,current);
            return true;
        } else {
            CoreFileServer localServer = getCoreFileServer();
            String key = localServer.coreCreateFile(src.getPath(),fileLength);

            UpdateNodeDTO updateRequest = new UpdateNodeDTO();
            updateRequest.setServerTypeId(setting.getServerTypeId());
            updateRequest.setServerAddress(setting.getServerAddress());
            updateRequest.setBaseDir(setting.getBaseDir());
            updateRequest.setFileLength((new Long(fileLength)).toString());
            updateRequest.setWritableKey(key);
            getStorageService().updateNodeSimple(src,updateRequest);
            return true;
        }
    }


    @Override
    public boolean releaseNode(SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        return releaseNodeForAccount(getCurrentAccount(current),
                src,fileLength,current);
    }

    @Override
    public boolean releaseNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        CheckService.check(!src.getIsDirectory(),ErrorCode.InvalidParameter,"releaseNodeForAccount");
        boolean result = false;
        NodeFileDTO file = getFileInfoForAccount(account,src,current);
        if (file != null) {
            String path = getNodePathForAccount(account, src, current);
            releaseFile(account, file, path, current);
            result = true;
        }
        return result;
    }

    @Override
    public boolean reloadNode(SimpleNodeDTO src, Current current) throws CustomException {
        return reloadNodeForAccount(getCurrentAccount(current),
                src,current);
    }

    @Override
    public boolean reloadNodeForAccount(AccountDTO account, SimpleNodeDTO src, Current current) throws CustomException {
        NodeFileDTO file = getFileInfoForAccount(account,src,current);
        if (file != null) {
            String path = getNodePathForAccount(account, src, current);
            reloadFile(account, file, path, current);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SimpleNodeDTO getNodeById(String id, Current current) throws CustomException {
        return getNodeByIdForAccount(getCurrentAccount(current),
                id,current);
    }

    @Override
    public SimpleNodeDTO getNodeByIdForAccount(AccountDTO account, String id, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setId(id);
        List<SimpleNodeDTO> list = listNodeForAccount(account,query,current);

        SimpleNodeDTO node = null;
        if ((list != null) && (!list.isEmpty())){
            assert (list.size() == 1);
            node = list.get(0);
        }
        return node;
    }

    @Override
    public SimpleNodeDTO getNodeByPath(String path, Current current) throws CustomException {
        return getNodeByPathForAccount(getCurrentAccount(current),
                path,current);
    }

    @Override
    public SimpleNodeDTO getNodeByPathForAccount(AccountDTO account, String path, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(path);
        query.setAccountId(getAccountId(account));
        List<SimpleNodeDTO> list = listNodeForAccount(account,query,current);

        SimpleNodeDTO node = null;
        if (ObjectUtils.isNotEmpty(list)){
            node = list.get(0);
        }
        return node;
    }

    @Override
    public List<SimpleNodeDTO> listAllNode(Current current) throws CustomException {
        return listAllNodeForAccount(getCurrentAccount(current),current);
    }

    @Override
    public List<SimpleNodeDTO> listAllNodeForAccount(AccountDTO account, Current current) throws CustomException {
        CheckService.check(StringUtils.isNotEmpty(getAccountId(account)),ErrorCode.NoPermission,"listAllNodeForAccount");
        QueryNodeDTO query = new QueryNodeDTO();
        query.setAccountId(getAccountId(account));
        return listNodeForAccount(account,query,current);
    }

    @Override
    public List<SimpleNodeDTO> listNode(QueryNodeDTO query, Current current) throws CustomException {
        return listNodeForAccount(getCurrentAccount(current),
                query,current);
    }

    @Override
    public List<SimpleNodeDTO> listNodeForAccount(AccountDTO account, @NotNull QueryNodeDTO query, Current current) throws CustomException {
        List<SimpleNodeDTO> list = getStorageService().listNode(query);
        for (SimpleNodeDTO node : list) {
            setAttr(node,getAccountId(account));
        }
        return list;
    }

    @Override
    public List<HistoryDTO> listHistory(@NotNull SimpleNodeDTO node, long startTime, long endTime, Current current) throws CustomException {
        return listHistoryForAccount(getCurrentAccount(current),node,startTime,endTime,current);
    }

    @Override
    public List<HistoryDTO> listHistoryForAccount(AccountDTO account, @NotNull SimpleNodeDTO node, long startTime, long endTime, Current current) throws CustomException {
        if (endTime == 0) {
            endTime = node.getLastModifyTimeStamp();
        }
        QueryNodeInfoHistoryDTO hisQuery = new QueryNodeInfoHistoryDTO();
        hisQuery.setHistoryStartTimeStamp(startTime);
        hisQuery.setHistoryEndTimeStamp(endTime);
        QueryNodeInfoDTO query = new QueryNodeInfoDTO();
        query.setHistoryQuery(hisQuery);
        FullNodeDTO fullNode = getNodeInfoForAccount(account,node,query,current);
        return (fullNode != null) ? fullNode.getHistoryList() : null;
    }

    @Override
    public NodeTextDTO getTextInfo(@NotNull SimpleNodeDTO node, Current current) throws CustomException {
        return getTextInfoForAccount(getCurrentAccount(current),node,current);
    }

    @Override
    public NodeTextDTO getTextInfoForAccount(AccountDTO account, @NotNull SimpleNodeDTO node, Current current) throws CustomException {
        QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
        txtQuery.setIsQueryTypeName(true);
        QueryNodeInfoDTO query = new QueryNodeInfoDTO();
        query.setTextQuery(txtQuery);
        FullNodeDTO fullNode = getNodeInfoForAccount(account,node,query,current);
        return (fullNode != null) ? fullNode.getTextInfo() : null;
    }

    @Override
    public FullNodeDTO getFullNode(SimpleNodeDTO node, Current current) throws CustomException {
        return getFullNodeForAccount(getCurrentAccount(current),
                node,current);
    }

    @Override
    public FullNodeDTO getFullNodeForAccount(AccountDTO account, SimpleNodeDTO node, Current current) throws CustomException {
        QueryNodeInfoTextDTO txtQuery = new QueryNodeInfoTextDTO();
        txtQuery.setIsQueryTypeName(true);
        QueryNodeInfoDTO query = new QueryNodeInfoDTO();
        query.setTextQuery(txtQuery);
        query.setFileQuery(getFileQuery());
        return getNodeInfoForAccount(account, node, query, current);
    }

    @Override
    public List<NodeFileDTO> listFile(AccountDTO account, QueryNodeFileDTO query, Current current) throws CustomException {
        return getStorageService().listNodeFile(query);
    }

    private boolean isValid(NodeFileDTO file,boolean isReadOnly){
        return (file != null) && (StringUtils.isNotEmpty(file.getId())) && StringUtils.isNotEmpty(getFileKey(file,isReadOnly));
    }

    private boolean isValid(SimpleNodeDTO node){
        return (node != null) && (StringUtils.isNotEmpty(node.getId()));
    }

    @Override
    public int writeNode(@NotNull SimpleNodeDTO src, @NotNull FileDataDTO data, Current current) throws CustomException {
        return writeNodeForAccount(getCurrentAccount(current),src,data,current);
    }

    @Override
    public int writeNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull FileDataDTO data, Current current) throws CustomException {
        NodeFileDTO file = getFileInfoForAccount(account,src,current);
        String path = getNodePathForAccount(account,src,current);
        boolean rw = isReadOnly(src,account);
        return writeRWFile(account,file,data,path,rw,current);
    }

    @Override
    public FileDataDTO readFile(AccountDTO account, NodeFileDTO file, long pos, int size, Current current) throws CustomException {
        return readFile(account,file,pos,size,true,current);
    }

    public FileDataDTO readFile(AccountDTO account, NodeFileDTO file, long pos, int size, boolean isReadOnly, Current current) throws CustomException {
        String key = getFileKey(file,isReadOnly);
        FileDataDTO data = null;
        if (StringUtils.isNotEmpty(key)) {
            CoreFileServer localServer = getCoreFileServer();
            CoreFileDataDTO coreData = localServer.coreReadFile(key,pos,size);
            data = BeanUtils.createFrom(coreData,FileDataDTO.class);
        }
        return data;
    }

    @Override
    public FileDataDTO readNode(@NotNull SimpleNodeDTO src, long pos, int size, Current current) throws CustomException {
        return readNodeForAccount(getCurrentAccount(current),
                src,pos,size,current);
    }

    private boolean isFile(@NotNull SimpleNodeDTO src){
        return !src.getIsDirectory();
    }
    @Override
    public FileDataDTO readNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, long pos, int size, Current current) throws CustomException {
        CheckService.check(isFile(src),ErrorCode.DataIsInvalid);
        NodeFileDTO file = getFileInfoForAccount(account,src,current);
        if (file == null) {
            return null;
        }
        boolean rw = isReadOnly(src,account);
        String key = getFileKey(file,rw);
        if (StringUtils.isEmpty(key)) {
            if (rw) {
                releaseNodeForAccount(account, src, 0, current);
            } else {
                reloadNodeForAccount(account, src, current);
            }
            file = getFileInfoForAccount(account,src,current);
        }
        return readFile(account,file,pos,size,rw,current);
    }

    @Override
    public SimpleNodeDTO moveNode(@NotNull SimpleNodeDTO src, @NotNull SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) throws CustomException {
        return moveNodeForAccount(getCurrentAccount(current),
                src,dstParent,request,current);
    }

    @Override
    public SimpleNodeDTO moveNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) throws CustomException {
        UpdateNodeDTO updateRequest = BeanUtils.createCleanFrom(request,UpdateNodeDTO.class);
        updateRequest.setPath(StringUtils.formatPath(request.getFullName()));
        updateRequest.setLastModifyUserId(getAccountId(account));
        log.info("改变节点" + src.getId() + "名称为：" + request.getFullName());
        return getStorageService().updateNode(src,dstParent,updateRequest);
    }


    private CommitListResultDTO updateCANodeList(AccountDTO account, List<CANodeDTO> srcList, Current current) throws CustomException {
        UpdateNodeDTO updateRequest = new UpdateNodeDTO();
        updateRequest.setLastModifyUserId(getAccountId(account));
        List<SimpleNodeDTO> successList = new ArrayList<>();
        List<CommitFailDTO> failList = new ArrayList<>();
        for (CANodeDTO src : srcList){
            try {
                updateRequest.setIsPassCheck(src.getIsPassCheck() ? ConstService.MODE_TRUE : ConstService.MODE_FALSE);
                updateRequest.setIsPassAudit(src.getIsPassAudit() ? ConstService.MODE_TRUE : ConstService.MODE_FALSE);
                SimpleNodeDTO dst = getStorageService().updateNodeSimple(getNodeByCANode(src),updateRequest);
                successList.add(dst);
            } catch (CustomException e) {
                CommitFailDTO failDTO = BeanUtils.createCleanFrom(src,CommitFailDTO.class);
                failList.add(failDTO);
            }
        }
        CommitListResultDTO result = new CommitListResultDTO();
        result.setSuccessList(successList);
        result.setFailList(failList);
        return result;
    }

    @Override
    public CommitListResultDTO updateNodeList(AccountDTO account, List<SimpleNodeDTO> srcList, CommitRequestDTO request, Current current) throws CustomException {
        UpdateNodeDTO updateRequest = BeanUtils.createCleanFrom(request,UpdateNodeDTO.class);
        updateRequest.setLastModifyUserId(getAccountId(account));
        List<SimpleNodeDTO> successList = new ArrayList<>();
        List<CommitFailDTO> failList = new ArrayList<>();
        for (SimpleNodeDTO src : srcList){
            try {
                SimpleNodeDTO dst = getStorageService().updateNodeSimple(src,updateRequest);
                successList.add(dst);
            } catch (CustomException e) {
                CommitFailDTO failDTO = BeanUtils.createCleanFrom(src,CommitFailDTO.class);
                failList.add(failDTO);
            }
        }
        CommitListResultDTO result = new CommitListResultDTO();
        result.setSuccessList(successList);
        result.setFailList(failList);
        return result;
    }

    @Override
    public CommitListResultDTO checkNodeListRequest(List<CANodeDTO> srcList, Current current) throws CustomException {
        return checkNodeListRequestForAccount(getCurrentAccount(current),
                srcList,current);
    }

    @Override
    public CommitListResultDTO checkNodeListRequestForAccount(AccountDTO account, List<CANodeDTO> srcList, Current current) throws CustomException {
        return updateCANodeList(account,srcList,current);
    }

    @Override
    public SimpleNodeDTO checkNodeRequest(CANodeDTO src, Current current) throws CustomException {
        return checkNodeRequestForAccount(getCurrentAccount(current),
                src,current);
    }

    @Override
    public SimpleNodeDTO checkNodeRequestForAccount(AccountDTO account, CANodeDTO src, Current current) throws CustomException {
        UpdateNodeDTO updateAsk = new UpdateNodeDTO();
        updateAsk.setIsPassCheck(src.getIsPassCheck() ? ConstService.MODE_TRUE : ConstService.MODE_FALSE);
        return getStorageService().updateNodeSimple(getNodeByCANode(src),updateAsk);
    }

    @Override
    public CommitListResultDTO auditNodeListRequest(List<CANodeDTO> srcList, Current current) throws CustomException {
        return auditNodeListRequestForAccount(getCurrentAccount(current),
                srcList,current);
    }

    @Override
    public CommitListResultDTO auditNodeListRequestForAccount(AccountDTO account, List<CANodeDTO> srcList, Current current) throws CustomException {
        return updateCANodeList(account,srcList,current);
    }

    @Override
    public SimpleNodeDTO auditNodeRequest(CANodeDTO src, Current current) throws CustomException {
        return auditNodeRequestForAccount(getCurrentAccount(current),
                src,current);
    }

    @Override
    public SimpleNodeDTO auditNodeRequestForAccount(AccountDTO account, CANodeDTO src, Current current) throws CustomException {
        UpdateNodeDTO updateAsk = new UpdateNodeDTO();
        updateAsk.setIsPassAudit(src.getIsPassAudit() ? ConstService.MODE_TRUE : ConstService.MODE_FALSE);
        return getStorageService().updateNodeSimple(getNodeByCANode(src),updateAsk);
    }

    private SimpleNodeDTO getNodeByCANode(CANodeDTO caNode) throws CustomException {
        return BeanUtils.createFrom(caNode,SimpleNodeDTO.class);
    }

    private List<SimpleNodeDTO> getNodeByCANode(List<CANodeDTO> caNode) throws CustomException {
        return BeanUtils.createListFrom(caNode,SimpleNodeDTO.class);
    }

    @Override
    public CommitListResultDTO requestIssueListForAccount(AccountDTO account, List<CANodeDTO> srcList, SimpleNodeDTO parent, Current current) throws CustomException {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE.toString());
        request.setPid(parent.getId());

        return commitNodeListForAccount(account,getNodeByCANode(srcList),request,current);
    }

    @Override
    public SimpleNodeDTO requestIssueForAccount(AccountDTO account, CANodeDTO src, SimpleNodeDTO parent, Current current) throws CustomException {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE.toString());
        request.setPid(parent.getId());

        return commitNodeForAccount(account,getNodeByCANode(src),request,current);
    }

    @Override
    public CommitListResultDTO requestCommitListForAccount(AccountDTO account, @NotNull List<CANodeDTO> srcList, Current current) throws CustomException {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_COMMIT.toString());
        return commitNodeListForAccount(account,BeanUtils.createListFrom(srcList,SimpleNodeDTO.class),request,current);
    }

    @Override
    public SimpleNodeDTO requestCommitForAccount(AccountDTO account, CANodeDTO src, Current current) throws CustomException {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_COMMIT.toString());
        return commitNodeForAccount(account,BeanUtils.createCleanFrom(src,SimpleNodeDTO.class),request,current);
    }

    @Override
    public CommitListResultDTO askCANodeListRequest(List<CANodeDTO> srcList, Current current) throws CustomException {
        return askCANodeListRequestForAccount(getCurrentAccount(current),srcList,current);
    }

    @Override
    public CommitListResultDTO askCANodeListRequestForAccount(AccountDTO account, List<CANodeDTO> srcList, Current current) throws CustomException {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ASK_CA.toString());
        request.setIsPassDesign(true);
        return commitNodeListForAccount(account,BeanUtils.createListFrom(srcList,SimpleNodeDTO.class),request,current);
    }

    @Override
    public SimpleNodeDTO askCANodeRequest(CANodeDTO src, Current current) throws CustomException {
        return askCANodeRequestForAccount(getCurrentAccount(current),src,current);
    }

    @Override
    public SimpleNodeDTO askCANodeRequestForAccount(AccountDTO account, CANodeDTO src, Current current) throws CustomException {
        CommitRequestDTO request = new CommitRequestDTO();
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ASK_CA.toString());
        request.setIsPassDesign(true);
        return commitNodeForAccount(account,BeanUtils.createCleanFrom(src,SimpleNodeDTO.class),request,current);
    }

    @Override
    public CommitListResultDTO commitNodeList(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return commitNodeListForAccount(getCurrentAccount(current),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO commitNodeListForAccount(AccountDTO account, @NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        List<SimpleNodeDTO> successList = new ArrayList<>();
        List<CommitFailDTO> failList = new ArrayList<>();
        for (SimpleNodeDTO src : srcList){
            SimpleNodeDTO result = commitNodeForAccount(account,src,request,current);
            if ((result != null) && (!StringUtils.isEmpty(result.getId()))) {
                successList.add(result);
            } else {
                CommitFailDTO failDTO = new CommitFailDTO();
                failDTO.setId(src.getId());
            }
        }
        CommitListResultDTO result = new CommitListResultDTO();
        result.setSuccessList(successList);
        result.setFailList(failList);
        return result;
    }

    @Override
    public SimpleNodeDTO commitNode(@NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return commitNodeForAccount(getCurrentAccount(current),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO commitNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        //确定和调整文件提交动作
        String actionTypeId = request.getActionTypeId();
        if (StringUtils.isEmpty(actionTypeId)){
            actionTypeId = ConstService.STORAGE_ACTION_TYPE_COMMIT.toString();
        }

        //获取目标文件的类型
        Short nodeTypeId = ConstService.getActionNodeTypeId(DigitUtils.parseShort(actionTypeId));
        if (ConstService.STORAGE_NODE_TYPE_UNKNOWN.equals(nodeTypeId)) {
            nodeTypeId = ConstService.STORAGE_NODE_TYPE_FILE_COMMIT_HIS;
        }

        //获取目标文件的地址
        NodeTextDTO txt = getTextInfoForAccount(account, src, current);
        StringElementDTO stringElement = new StringElementDTO();
        updateStringElement(stringElement, src, txt, request, actionTypeId);
        String path = request.getPath();
        if (StringUtils.isEmpty(path)) { //如果没有指定路径，使用默认配置
            path = ConstService.getActionNodePath(DigitUtils.parseShort(actionTypeId), stringElement);

            //如果未定义，使用当前路径
            if (StringUtils.isEmpty(path)) {
                path = txt.getPath();
            }
        }

        //如果是相对路径，添加当前路径
        if (!StringUtils.isAbsolutePath(path)){
            path = StringUtils.formatPath(StringUtils.getDirName(txt.getPath()) + StringUtils.SPLIT_PATH + path);
        }
        //如果与源文件文件名相同，添加动作名称
        if (StringUtils.isSame(path,txt.getPath())){
            String actionName = ConstService.getActionName(DigitUtils.parseShort(actionTypeId));
            String fileName = StringUtils.getFileNameWithoutExt(path) + StringUtils.SPLIT_NAME_PART +
                    actionName + StringUtils.getFileExt(path);
            path = StringUtils.formatPath(StringUtils.getDirName(path) + StringUtils.SPLIT_PATH + fileName);
        }

        //根据提交动作取出要上传的文件服务器类型和地址
        String serverTypeId = ConstService.getActionFileServerTypeId(DigitUtils.parseShort(actionTypeId)).toString();
        String serverAddress = ConstService.getActionFileServerAddress(DigitUtils.parseShort(actionTypeId),stringElement);
        String baseDir = ConstService.getActionFileServerBaseDir(DigitUtils.parseShort(actionTypeId),stringElement);;

        //建立或更新版本节点
        request.setActionTypeId(actionTypeId);
        request.setServerTypeId(serverTypeId);
        request.setServerAddress(serverAddress);
        request.setBaseDir(baseDir);
        if (StringUtils.isEmpty(request.getOwnerUserId())){
            request.setOwnerUserId(request.getUserId());
        }

        //更新源节点状态
        if (request.getIsPassDesign() || request.getIsPassCheck() || request.getIsPassAudit()) {
            UpdateNodeDTO updateRequest = new UpdateNodeDTO();
            getStorageService().updateNodeSimple(src,updateRequest);
        }

        SimpleNodeDTO targetNode = getNodeByPathForAccount(account,path,current);
        if ((targetNode == null) || (StringUtils.isEmpty(targetNode.getId()))){
            targetNode = createVersion(account,src,path,request,current);
        } else {
            targetNode = updateVersion(account,src,targetNode,request,current);
        }



        //发送通知消息
        String typeIdString = ConstService.getActionNoticeTypeIdString(DigitUtils.parseShort(actionTypeId));
        if (StringUtils.isNotEmpty(typeIdString)){
            NoticeRequestDTO noticeRequest = new NoticeRequestDTO();
            noticeRequest.setTypeIdString(typeIdString);
            noticeRequest.setStringElement(stringElement);
            getNoticeServicePrx().sendNoticeForAccountAsync(account,noticeRequest);
        }

        return targetNode;
    }

    public SimpleNodeDTO getNodeByFuzzyPathForAccount(AccountDTO account,String fuzzyPath,Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath(fuzzyPath);
        query.setAccountId(getAccountId(account));
        List<SimpleNodeDTO> list = getStorageService().listNode(query);
        return ((list != null) && (!list.isEmpty())) ? list.get(0) : null;
    }

    private boolean isCoreFileServer(Short serverTypeId, String serverAddress){
        boolean isCore = !ConstService.FILE_SERVER_TYPE_DISK.equals(serverTypeId);
        if (!isCore) isCore = StringUtils.isSame(serverAddress,setting.getServerAddress());
        return isCore;
    }

    private String getUniquePath(AccountDTO account, @NotNull String path, Current current) throws CustomException {
        String dir = StringUtils.getDirName(path);
        String fn = StringUtils.getFileNameWithoutExt(path);
        String ext = StringUtils.getFileExt(path);
        assert (fn != null);
        fn = StringUtils.addTimeStamp(fn);
        StringBuilder keyBuilder = new StringBuilder(dir);
        if (keyBuilder.length() > 0) {
            keyBuilder.append(StringUtils.SPLIT_PATH);
        }
        keyBuilder.append(fn);
        final int MAX_UNIQUE_NUM = 1000;
        int i = 0;
        String tmpKey;
        do {
            tmpKey = keyBuilder.toString();
            if (i > 0) {
                tmpKey += StringUtils.SPLIT_NAME_PART + i;
            }
            if (StringUtils.isNotEmpty(ext)) {
                tmpKey += ext;
            }
        } while (i++<MAX_UNIQUE_NUM && (getNodeByPathForAccount(account,tmpKey,current) != null));
        path = tmpKey;

        return path;
    }
    
    @Override
    public SimpleNodeDTO createVersion(AccountDTO account, @NotNull SimpleNodeDTO src, String path, CommitRequestDTO request, Current current) throws CustomException {
        //发布改写内容
        boolean b = releaseNodeForAccount(account,src,0,current);

        //获取父节点
        SimpleNodeDTO parent = null;
        path = StringUtils.formatPath(path);
        if (StringUtils.isNotSame(StringUtils.left(path,1),StringUtils.SPLIT_PATH)){
            parent = getNodeByIdForAccount(account,src.getPid(),current); 
        }
        //复制文件
        NodeFileDTO srcFile = getFileInfoForAccount(account,src,current);
        String srcKey = getFileKey(srcFile,true);
        if (StringUtils.isEmpty(srcKey)) {
            srcKey = getFileKey(srcFile,false);
        }
        CheckService.check(StringUtils.isNotEmpty(srcKey), ErrorCode.DataIsInvalid,"createVersion");
        File localFile = getCoreFileServer().coreGetFile(srcKey);
        String dstPath = (parent != null) ? getNodePathForAccount(account,parent,current) + StringUtils.SPLIT_PATH + path : path;
        SimpleNodeDTO node = getNodeByPathForAccount(account,dstPath,current);
        if (node != null) {
            dstPath = getUniquePath(account,dstPath,current);
            path = StringUtils.getDirName(path);
            if (StringUtils.isNotEmpty(path)) {
                path += StringUtils.SPLIT_PATH;
            }
            path += StringUtils.getFileName(dstPath);
        }
        CoreFileServer fileServer = getCoreFileServer(request.getServerTypeId(),request.getServerAddress(),request.getBaseDir());
        String dstKey = fileServer.coreCreateFile(dstPath,localFile);

        //获取目标文件的类型
        Short nodeTypeId = ConstService.getActionNodeTypeId(DigitUtils.parseShort(request.getActionTypeId()));

        UpdateNodeDTO createRequest = BeanUtils.createCleanFrom(request,UpdateNodeDTO.class);
        BeanUtils.copyCleanProperties(src,createRequest);
        createRequest.setServerTypeId(setting.getServerTypeId());
        createRequest.setServerAddress(setting.getServerAddress());
        createRequest.setBaseDir(setting.getBaseDir());
        createRequest.setReadOnlyKey(dstKey);
        createRequest.setLastModifyUserId(getAccountId(account));
        createRequest.setPath(path);
        createRequest.setMainFileId(StringUtils.left(src.getId(),StringUtils.DEFAULT_ID_LENGTH));
        createRequest.setTypeId(nodeTypeId.toString());
        createRequest.setOwnerUserId(getAccountId(account));
        return getStorageService().createNode(parent,createRequest);
    }

    private String getBackupNodeTypeId(String actionTypeId) {
        return ConstService.getExtra(ConstService.CLASSIC_TYPE_ACTION,DigitUtils.parseShort(actionTypeId),8);
    }

    private String getNodeTypeName(String nodeTypeId) {
        return ConstService.getTitle(ConstService.CLASSIC_TYPE_STORAGE_NODE,DigitUtils.parseShort(nodeTypeId));
    }

    @Override
    public SimpleNodeDTO updateVersion(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull SimpleNodeDTO dst, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        final String DEFAULT_BACKUP_DIR = "历史版本";

        //备份原有节点
        String dstPath = getNodePathForAccount(account,dst,current);
        String backupDirTypeId = getBackupNodeTypeId(request.getActionTypeId());
        String backupDirName = getNodeTypeName(backupDirTypeId);

        MoveNodeRequestDTO moveRequest = new MoveNodeRequestDTO();
        String backupDir = StringUtils.getDirName(dst.getPath());
        backupDir += StringUtils.SPLIT_PATH + backupDirName;
        SimpleNodeDTO parent = getNodeByPathForAccount(account,backupDir,current);
        if ((parent == null) && (StringUtils.isNotEmpty(StringUtils.getDirName(dst.getPath())))){
            parent = getNodeByPathForAccount(account,StringUtils.getDirName(dst.getPath()),current);
            if (parent != null) {
                UpdateNodeDTO createBackupRequest = new UpdateNodeDTO();
                createBackupRequest.setPath(backupDirName);
                createBackupRequest.setTypeId(backupDirTypeId);
                parent = getStorageService().createNode(parent, createBackupRequest);
            }
        }
        String backupFileName = StringUtils.getFileNameWithoutExt(dst.getName());
        backupFileName += StringUtils.SPLIT_NAME_PART + StringUtils.getTimeStamp();
        backupFileName += StringUtils.getFileExt(dst.getName());
        moveRequest.setFullName(backupFileName);
        moveNodeForAccount(account, dst, parent, moveRequest, current);

        //发布源节点
        return createVersion(account,src,dstPath,request,current);
    }

    @Override
    public SimpleNodeDTO createDirectory(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        return createDirectoryForAccount(getCurrentAccount(current),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createDirectoryForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        request.setIsDirectory(true);
        return createNodeForAccount(account,parent,request,current);
    }

    @Override
    public SimpleNodeDTO createFile(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        return createFileForAccount(getCurrentAccount(current),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createFileForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        request.setIsDirectory(false);
        return createNodeForAccount(account,parent,request,current);
    }

    @Override
    public SimpleNodeDTO createNode(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        return createNodeForAccount(getCurrentAccount(current),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createNodeForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        UpdateNodeDTO createRequest = BeanUtils.createCleanFrom(request,UpdateNodeDTO.class);
        String accountId = getAccountId(account);
        createRequest.setLastModifyUserId(accountId);
        createRequest.setOwnerUserId(accountId);
        createRequest.setPath(request.getFullName());
        if (request.getIsDirectory()) {
            if (!isValid(parent)) {
                createRequest.setTypeId(ConstService.getPathType(parent.getTypeId()));
                createRequest.setTaskId(parent.getTaskId());
            } else {
                createRequest.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN.toString());
                createRequest.setTaskId(null);
            }
        } else {
            createRequest.setServerTypeId(setting.getServerTypeId());
            createRequest.setServerAddress(setting.getServerAddress());
            createRequest.setBaseDir(setting.getBaseDir());
            if (request.getFileLength() > 0) {
                String path = getNodePathForAccount(account,parent,current);
                path = StringUtils.formatPath(path + StringUtils.SPLIT_PATH + createRequest.getPath());
                long fileLength = DigitUtils.parseLong(createRequest.getFileLength());
                String key = getCoreFileServer().coreCreateFile(path,fileLength);
                createRequest.setWritableKey(key);
            } else {
                createRequest.setFileLength(null);
            }
            if (isValid(parent)) {
                createRequest.setTypeId(ConstService.getFileType(parent.getTypeId()));
                createRequest.setTaskId(parent.getTaskId());
            } else {
                createRequest.setTypeId(ConstService.STORAGE_NODE_TYPE_UNKNOWN.toString());
                createRequest.setTaskId(null);
            }
        }
        return getStorageService().createNode(parent,createRequest);
    }
}
