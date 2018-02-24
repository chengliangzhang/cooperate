package com.maoding.FileServer;

import com.maoding.Base.BaseLocalService;
import com.maoding.Common.ConstService;
import com.maoding.Common.Dto.StringElementDTO;
import com.maoding.Common.zeroc.CustomException;
import com.maoding.Common.zeroc.ErrorCode;
import com.maoding.Common.zeroc.IdNameDTO;
import com.maoding.CoreFileServer.*;
import com.maoding.CoreFileServer.Disk.DiskFileServer;
import com.maoding.CoreFileServer.MaodingWeb.WebFileServer;
import com.maoding.FileServer.Config.FileServerConfig;
import com.maoding.FileServer.Dto.CopyRequestDTO;
import com.maoding.FileServer.zeroc.*;
import com.maoding.Notice.zeroc.NoticeClientPrx;
import com.maoding.Notice.zeroc.NoticeRequestDTO;
import com.maoding.Notice.zeroc.NoticeServicePrx;
import com.maoding.Project.zeroc.ProjectDTO;
import com.maoding.Storage.zeroc.*;
import com.maoding.User.zeroc.*;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 10:08
 * 描    述 :
 */
@SuppressWarnings("deprecation")
@Service("fileService")
public class FileServiceImpl extends BaseLocalService<FileServicePrx> implements FileService, FileServicePrx {

    @Autowired
    private FileServerConfig setting;
    private CoreFileServer fileServer = new DiskFileServer();

    private static Map<String,Map<String,FileDTO>> userBasicFileMap = new HashMap<>();
    private static Map<String, FileNodeDTO> fileNodeMap = new HashMap<>();

    @Override
    public List<SimpleNodeDTO> listWebArchiveDir(String projectId, Current current) {
        return listWebArchiveDirForAccount(getUserService(current).getCurrent(),
                projectId,current);
    }

    @Override
    public List<SimpleNodeDTO> listWebArchiveDirForAccount(AccountDTO account, String projectId, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setProjectId(projectId);
        query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
        List<SimpleNodeDTO> nodeList = getStorageService(current).listOldNode(BeanUtils.cleanProperties(query));
        return nodeList;
    }

    public CoreFileDTO createRealFileForAccount(AccountDTO account, Short serverTypeId, CoreFileDTO dst, FileNodeDTO src){
        String serverAddress = null;
        if (dst != null) serverAddress = dst.getServerAddress();

        CoreFileServer dstServer = setting.getFileServer(serverTypeId,serverAddress);
        if (isWebServer(dstServer)){
            if ((src != null) && (src.getBasic() != null) && StringUtils.isNotEmpty(src.getBasic().getName())){
                if (dst == null) dst = new CoreFileDTO();
                if (StringUtils.isEmpty(dst.getKey())) dst.setKey(src.getBasic().getName());
            }
        }

        CoreFileExtraDTO createRequest = new CoreFileExtraDTO();
        if (src != null){
            CoreFileDTO realSrc = getCoreFile(account,src);
            if (realSrc != null) {
                CoreFileServer srcServer = setting.getFileServer(Short.parseShort(src.getServerTypeId()),src.getServerAddress());
                createRequest.setSrcFile(srcServer.coreGetFile(realSrc));
            }

            if (src.getBasic() != null) {
                SimpleNodeDTO srcNode = src.getBasic();
                createRequest.setProjectId(srcNode.getProjectId());
                createRequest.setTaskId(srcNode.getTaskId());
                createRequest.setCompanyId(srcNode.getCompanyId());
                createRequest.setAccountId(srcNode.getOwnerUserId());
                if ((dst != null) && (StringUtils.isNotEmpty(dst.getScope()))){
                    QueryNodeDTO query = new QueryNodeDTO();
                    StringBuilder idStringBuilder = new StringBuilder(srcNode.getProjectId());
                    if (StringUtils.isNotEmpty(srcNode.getCompanyId())) idStringBuilder.append(",").append(srcNode.getCompanyId());
                    if (StringUtils.isNotEmpty(srcNode.getIssueId())) idStringBuilder.append(",").append(srcNode.getIssueId());
                    if (StringUtils.isNotEmpty(srcNode.getTaskId())) idStringBuilder.append(",").append(srcNode.getTaskId());
                    query.setFuzzyIdString(idStringBuilder.toString());
                    query.setFuzzyPath(StringUtils.formatPath(dst.getScope()));
                    query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
                    List<SimpleNodeDTO> list = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
                    if ((list != null) && !(list.isEmpty())){
                        SimpleNodeDTO parent = list.get(0);
                        createRequest.setPid(StringUtils.left(parent.getId(),StringUtils.DEFAULT_ID_LENGTH));
                    }
                }
            }
        }
        if (account != null) createRequest.setAccountId(account.getId());

        dst = dstServer.coreCreateFile(dst,BeanUtils.cleanProperties(createRequest));

        return dst;
    }
    public CoreFileDTO createRealFile(@NotNull Short serverTypeId,CoreFileDTO dst,FileNodeDTO src){
        return createRealFileForAccount(getUserServiceEx().getCurrent(),serverTypeId,dst,src);
    }
    public CoreFileDTO createRealFile(@NotNull CommitRequestDTO request,FileNodeDTO src){
        Short actionTypeId = request.getActionTypeId();

        Short serverTypeId;
        if (!ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(request.getServerTypeId())) serverTypeId = request.getServerTypeId();
        else if (!ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(ConstService.getActionFileServerTypeId(actionTypeId))) serverTypeId = ConstService.getActionFileServerTypeId(actionTypeId);
        else serverTypeId = setting.getServerTypeId();

        String serverAddress;
        if (StringUtils.isNotEmpty(request.getServerAddress())) serverAddress = request.getServerAddress();
        else if (StringUtils.isNotEmpty(ConstService.getActionFileServerAddress(actionTypeId))) serverAddress = ConstService.getActionFileServerAddress(actionTypeId);
        else serverAddress = null;
        CoreFileServer dstServer = setting.getFileServer(serverTypeId,serverAddress);

        CoreFileDTO dst = new CoreFileDTO();
        if (StringUtils.isNotEmpty(request.getPath())) {
            dst.setScope(StringUtils.getDirName(request.getPath()));
            dst.setKey(StringUtils.getFileName(request.getPath()));
        } else if ((src != null) && (src.getBasic() != null) && StringUtils.isNotEmpty(src.getBasic().getName())){
            dst.setKey(src.getBasic().getName());
        }

        CoreFileExtraDTO createRequest = new CoreFileExtraDTO();
        if (src != null){
            CoreFileDTO realSrc = getCoreFile(src,request.getUserId());
            if (realSrc != null) {
                CoreFileServer srcServer = setting.getFileServer(Short.parseShort(src.getServerTypeId()),src.getServerAddress());
                createRequest.setSrcFile(srcServer.coreGetFile(realSrc));
            }
        }


        if (isWebServer(dstServer)){
            if (StringUtils.isNotEmpty(request.getPid())) {
                QueryNodeDTO query = new QueryNodeDTO();
                query.setId(request.getPid());
                List<SimpleNodeDTO> list = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
                if ((list != null) && !(list.isEmpty())) {
                    SimpleNodeDTO parent = list.get(0);
                    createRequest = setParentInfo(createRequest,parent);
                }
            } else if ((src != null) && (src.getBasic() != null)){
                SimpleNodeDTO srcNode = src.getBasic();
                QueryNodeDTO query = new QueryNodeDTO();
                assert (StringUtils.isNotEmpty(srcNode.getProjectId()));
                StringBuilder idStringBuilder = new StringBuilder(srcNode.getProjectId());
                if (StringUtils.isNotEmpty(srcNode.getCompanyId())) idStringBuilder.append(",").append(srcNode.getCompanyId());
                if (StringUtils.isNotEmpty(srcNode.getIssueId())) idStringBuilder.append(",").append(srcNode.getIssueId());
                if (StringUtils.isNotEmpty(srcNode.getTaskId())) idStringBuilder.append(",").append(srcNode.getTaskId());
                query.setFuzzyIdString(idStringBuilder.toString());
                if (StringUtils.isNotEmpty(request.getPath())) {
                    query.setFuzzyPath(StringUtils.formatPath(request.getPath()));
                }
                query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
                List<SimpleNodeDTO> list = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
                if ((list != null) && !(list.isEmpty())){
                    SimpleNodeDTO parent = list.get(0);
                    createRequest = setParentInfo(createRequest,parent);
                }
            }
            createRequest.setAccountId(request.getUserId());
            if (StringUtils.isEmpty(createRequest.getAccountId())) {
                if ((src != null) && (src.getBasic() != null))
                createRequest.setAccountId(src.getBasic().getOwnerUserId());
            }
            if (StringUtils.isEmpty(createRequest.getAccountId())){
                createRequest.setAccountId(getUserServiceEx().getCurrent().getId());
            }
        }

        dst = dstServer.coreCreateFile(dst,BeanUtils.cleanProperties(createRequest));

        return dst;
    }
    
    private CoreFileExtraDTO setParentInfo(@NotNull CoreFileExtraDTO request, SimpleNodeDTO parent){
        if (parent != null) {
            request.setPid(StringUtils.left(parent.getId(), StringUtils.DEFAULT_ID_LENGTH));
            request.setProjectId(parent.getProjectId());
            request.setCompanyId(parent.getCompanyId());
            request.setTaskId(parent.getTaskId());
            if (StringUtils.isEmpty(request.getTaskId())) request.setTaskId(parent.getIssueId());
        }
        return request;
    }

    private boolean isSameServer(CoreFileServer srcServer,CoreFileServer dstServer){
        return (srcServer != null) && (dstServer != null) && (dstServer == srcServer);
    }

    private boolean isWebServer(CoreFileServer server){
        return (server instanceof WebFileServer);
    }

    public CoreFileDTO copyRealFile(@NotNull CoreFileDTO src, @NotNull CopyRequestDTO request, Current current){
        //补充复制申请信息
        if (request.getSrcServerTypeId() == null) request.setSrcServerTypeId(setting.getServerTypeId());
        if (request.getDstServerTypeId() == null) request.setDstServerTypeId(request.getSrcServerTypeId());
        if (request.getSrcServerAddress() == null) request.setSrcServerAddress(src.getServerAddress());
        if (request.getSrcServerAddress() == null) request.setSrcServerAddress(setting.getServerAddress());
        if (request.getDstServerAddress() == null) request.setDstServerAddress(src.getServerAddress());
        if (request.getScope() == null) request.setScope(src.getScope());
        if (request.getKey() == null) request.setKey(src.getKey());

        //复制文件
        CoreFileDTO dst = new CoreFileDTO(request.getDstServerAddress(),request.getScope(),request.getKey());
        if (isSameServer(request)){ //同服务器复制
            CoreFileServer fileServer = setting.getFileServer(request.getSrcServerTypeId(),request.getSrcServerAddress());
            dst = fileServer.coreCreateFile(dst);
            fileServer.coreCopyFile(src,dst);
        } else { //不同文件服务器复制
            Short srcServerType = request.getSrcServerTypeId();
            String srcServerAddress = src.getServerAddress();
            Short dstServerType = request.getDstServerTypeId();
            String dstServerAddress = request.getDstServerAddress();
            CoreFileServer srcFileServer = setting.getFileServer(srcServerType,srcServerAddress);
            CoreFileServer dstFileServer = setting.getFileServer(dstServerType,dstServerAddress);
            dst = dstFileServer.coreCreateFile(src,request.getExtra());
        }
        return dst;
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

    /**
     * @param src
     * @param current The Current object for the invocation.
     * @deprecated 尚未实现
     **/
    @Override
    public boolean createMirror(FileNodeDTO src, Current current) throws CustomException {
        return false;
    }

    @Override
    public boolean createMirrorForAccount(AccountDTO account, FileNodeDTO src, Current current) {
        return false;
    }

    @Override
    public CommitListResultDTO issueNodeList(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) {
        return issueNodeListForAccount(getUserService(current).getCurrent(),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO issueNodeListForAccount(AccountDTO account, List<SimpleNodeDTO> srcList, CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);


        List<SimpleNodeDTO> successList = new ArrayList<>();
        List<CommitFailDTO> failList = new ArrayList<>();
        for (SimpleNodeDTO src : srcList){
            SimpleNodeDTO result = issueNodeForAccount(account,src,request,current);
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
    public SimpleNodeDTO issueNode(SimpleNodeDTO src, CommitRequestDTO request, Current current) {
        return issueNodeForAccount(getUserService(current).getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO issueNodeForAccount(AccountDTO account, SimpleNodeDTO src, CommitRequestDTO request, Current current) {        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE);
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return issueFileForAccount(account,file,request,current);
    }

    @Override
    public SimpleNodeDTO issueFile(FileNodeDTO src, CommitRequestDTO request, Current current) {
        return issueFileForAccount(getUserService(current).getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO issueFileForAccount(AccountDTO account, FileNodeDTO src, CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE);

        if (ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(request.getServerTypeId())) request.setServerTypeId(ConstService.FILE_SERVER_TYPE_WEB);

        CoreFileDTO dst = createRealFile(request,src);

//        CoreFileExtraDTO uploadRequest = new CoreFileExtraDTO();
//        uploadRequest.setProjectId(src.getBasic().getProjectId());
//        uploadRequest.setCompanyId(src.getBasic().getCompanyId());
//        uploadRequest.setTaskId(src.getBasic().getTaskId());
//        uploadRequest.setPid(src.getBasic().getPid());
//        String userId = (account != null) ? account.getId() : src.getBasic().getOwnerUserId();
//        uploadRequest.setAccountId(userId);
//        uploadRequest.setLastModifyTime(new Date());
//        CoreFileServer webServer = new WebFileServer();
//
//        FileDTO file = getBasicFile(src,userId,current);
//        String path = DiskFileServer.getPath(file.getScope(),file.getKey());
//        CoreFileDTO basicFile = new CoreFileDTO();
//        basicFile.setScope(StringUtils.getDirName(path));
//        basicFile.setKey(StringUtils.getFileName(path));
//        webServer.upload(basicFile,uploadRequest);
        return src.getBasic();
    }

    @Override
    public SimpleNodeDTO changeNodeOwner(SimpleNodeDTO src, UserDTO dstOwner, Current current) throws CustomException {
        return changeNodeOwnerForAccount(getUserService(current).getCurrent(),
                src,dstOwner,current);
    }

    @Override
    public SimpleNodeDTO changeNodeOwnerForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull UserDTO dstOwner, Current current) throws CustomException {
        if (getIsReadOnly(account,src,current)) throw new CustomException(ErrorCode.NoPermission,"没有权限");
        UpdateNodeDTO updateRequest = new UpdateNodeDTO();
        updateRequest.setOwnerUserId(dstOwner.getId());
        return getStorageService(current).updateNodeWithParent(src,null,updateRequest);
    }

    private boolean getIsReadOnly(AccountDTO account,@NotNull SimpleNodeDTO src,Current current){
        String userId = (account != null) ? account.getId() : null;
        return src.getIsProject()
                || src.getIsTask()
                || src.getIsCommit()
                || src.getIsHistory()
                || ((StringUtils.isNotEmpty(src.getOwnerUserId()))
                && !(StringUtils.isSame(userId,src.getOwnerUserId())));
    }

    @Override
    public boolean login(LoginDTO loginInfo, Current current) {
        return getUserService(current).login(loginInfo);
    }

    @Override
    public List<String> setNoticeClient(String userId, NoticeClientPrx client, Current current) {
        getNoticeService(current).subscribeTopicForUser(userId,client);
        return getNoticeService().listSubscribedTopic(userId);
    }

    @Override
    public UserServicePrx getUserService(Current current) {
        return setting.getUserService();
    }
    private UserServicePrx getUserServiceEx() {
        return setting.getUserService();
    }

    @Override
    public NoticeServicePrx getNoticeService(Current current) {
        return setting.getNoticeService();
    }

    private StorageServicePrx getStorageService(Current current){
        return setting.getStorageService();
    }
    private StorageServicePrx getStorageService(){
        return setting.getStorageService();
    }

    @Override
    public List<ProjectRoleDTO> listProjectRoleByProjectId(String projectId, Current current) {
        return listProjectRoleByProjectIdForAccount(getUserService(current).getCurrent(),
                projectId,current);
    }

    @Override
    public List<ProjectRoleDTO> listProjectRoleByProjectIdForAccount(AccountDTO account, String projectId, Current current) {
        return getUserService(current).listProjectRoleByProjectId(projectId);
    }

    @Override
    public List<IdNameDTO> listMajor(Current current) {
        return listMajorForAccount(getUserService(current).getCurrent(),
                current);
    }

    @Override
    public List<IdNameDTO> listMajorForAccount(AccountDTO account, Current current) {
        return ConstService.listMajor();
    }

    @Override
    public List<IdNameDTO> listAction(Current current) {
        return listActionForAccount(getUserService(current).getCurrent(),
                current);
    }

    @Override
    public List<IdNameDTO> listActionForAccount(AccountDTO account, Current current) {
        return ConstService.listAction();
    }

    /**
     * @param path
     * @param current The Current object for the invocation.
     * @deprecated 尚未验证
     **/
    @Override
    public ProjectDTO getProjectInfoByPath(String path, Current current) {
        return null;
    }

    /**
     * @param account
     * @param path
     * @param current The Current object for the invocation.  @deprecated 尚未验证
     **/
    @Override
    public ProjectDTO getProjectInfoByPathForAccount(AccountDTO account, String path, Current current) {
        return null;
    }

    @Override
    public boolean deleteNode(SimpleNodeDTO src, Current current) {
        return deleteNodeForAccount(getUserService(current).getCurrent(),
                src,current);
    }

    @Override
    public boolean deleteNodeForAccount(AccountDTO account, SimpleNodeDTO src, Current current) {
        return getStorageService(current).deleteNodeById(src.getId());
    }

    @Override
    public boolean setNodeLength(SimpleNodeDTO src, long fileLength, Current current) {
        return setNodeLengthForAccount(getUserService(current).getCurrent(),
                src,fileLength,current);
    }

    @Override
    public boolean setNodeLengthForAccount(AccountDTO account, SimpleNodeDTO src, long fileLength, Current current) {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return setFileNodeLengthForAccount(getUserService(current).getCurrent(),
                file,fileLength,current);
    }

    @Override
    public boolean setFileNodeLength(FileNodeDTO src, long fileLength, Current current) {
        return setFileNodeLengthForAccount(getUserService(current).getCurrent(),
                src,fileLength,current);
    }

    @Override
    public boolean setFileNodeLengthForAccount(AccountDTO account, FileNodeDTO src, long fileLength, Current current) {
        UpdateNodeDTO updateRequest = new UpdateNodeDTO();
        CoreFileDTO basicWrite = new CoreFileDTO();
        basicWrite.setScope(src.getWriteFileScope());
        basicWrite.setKey(src.getWriteFileKey());
        if (!setting.getFileServer().isExist(basicWrite)){
            assert (src.getBasic() != null);
            basicWrite = setting.getFileServer().coreCreateFile(src.getBasic().getPath());
            updateRequest.setWriteFileScope(basicWrite.getScope());
            updateRequest.setWriteFileKey(basicWrite.getKey());
        }
        boolean isOk = setting.getFileServer().coreSetFileLength(basicWrite,fileLength);
        if (isOk) {
            updateRequest.setFileLength(fileLength);
            SimpleNodeDTO dst = updateNode(src.getBasic(), updateRequest, current);
            assert (dst != null);
        }
        return isOk;
    }

    @Override
    public boolean releaseNode(SimpleNodeDTO src, long fileLength, Current current) {
        return releaseNodeForAccount(getUserService(current).getCurrent(),
                src,fileLength,current);
    }

    @Override
    public boolean releaseNodeForAccount(AccountDTO account, SimpleNodeDTO src, long fileLength, Current current) {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return releaseFileNodeForAccount(account,file,fileLength,current);
    }

    @Override
    public boolean releaseFileNode(FileNodeDTO src, long fileLength, Current current) {
        return releaseFileNodeForAccount(getUserService(current).getCurrent(),
                src,fileLength,current);
    }

    @Override
    public boolean releaseFileNodeForAccount(AccountDTO account, FileNodeDTO src, long fileLength, Current current) {
        CoreFileDTO basicWrite = new CoreFileDTO();
        basicWrite.setScope(src.getWriteFileScope());
        basicWrite.setKey(src.getWriteFileKey());
        if (fileLength == 0) fileLength = setting.getFileServer().coreGetFileLength(basicWrite);
        if (fileLength > 0) {
            setting.getFileServer().coreSetFileLength(basicWrite,fileLength);
        }
        String fileMd5 = setting.getFileServer().coreCalcChecksum(basicWrite);
        if (!StringUtils.isSame(fileMd5,src.getFileChecksum())){
            CoreFileDTO basicRead = new CoreFileDTO();
            if (StringUtils.isEmpty(src.getReadFileKey())){
                basicRead.setScope(src.getWriteFileScope());
                basicRead.setKey(src.getWriteFileKey());
                basicRead = setting.getFileServer().coreCreateFile(src.getBasic().getPath());
            } else {
                basicRead.setScope(src.getReadFileScope());
                basicRead.setKey(src.getReadFileKey());
                if (setting.getFileServer().isExist(basicRead)){
                    basicRead = setting.getFileServer().coreCreateFile(src.getBasic().getPath());
                }
            }
            CoreFileCopyResult copyResult = setting.getFileServer().coreCopyFile(basicWrite,basicRead);
            if (copyResult != null) {
                UpdateNodeDTO updateRequest = new UpdateNodeDTO();
                updateRequest.setReadFileScope(basicRead.getScope());
                updateRequest.setReadFileKey(basicRead.getKey());
                updateRequest.setFileLength(copyResult.getFileLength());
                updateRequest.setFileChecksum(copyResult.getFileChecksum());
                fileLength = setting.getFileServer().coreGetFileLength(basicWrite);
                updateRequest.setFileLength(fileLength);
                SimpleNodeDTO dst = updateNode(src.getBasic(), updateRequest, current);
                assert (dst != null);
            }
            return (copyResult != null);
        }
        return true;
    }

    @Override
    public boolean reloadNode(SimpleNodeDTO src, Current current) {
        return reloadNodeForAccount(getUserService(current).getCurrent(),
                src,current);
    }

    @Override
    public boolean reloadNodeForAccount(AccountDTO account, SimpleNodeDTO src, Current current) {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return reloadFileNodeForAccount(account,file,current);
    }

    @Override
    public boolean reloadFileNode(FileNodeDTO src, Current current) {
        return reloadFileNodeForAccount(getUserService(current).getCurrent(),
                src,current);
    }

    @Override
    public boolean reloadFileNodeForAccount(AccountDTO account, FileNodeDTO src, Current current) {
        CoreFileDTO basicWrite = getCoreFile(account,src);
        if (setting.getFileServer().isExist(basicWrite)){
            assert (src.getBasic() != null);
            basicWrite = setting.getFileServer().coreCreateFile(src.getBasic().getPath());
        }

        CoreFileDTO basicRead = new CoreFileDTO();
        basicRead.setScope(src.getReadFileScope());
        basicRead.setKey(src.getReadFileKey());
        CoreFileCopyResult copyResult = setting.getFileServer().coreCopyFile(basicRead,basicWrite);
        if (copyResult != null) {
            UpdateNodeDTO updateRequest = new UpdateNodeDTO();
            updateRequest.setWriteFileScope(basicWrite.getScope());
            updateRequest.setWriteFileKey(basicWrite.getKey());
            updateRequest.setFileLength(copyResult.getFileLength());
            SimpleNodeDTO dst = updateNode(src.getBasic(), updateRequest, current);
            assert (dst != null);
        }
        return (copyResult != null);
    }

    @Override
    public SimpleNodeDTO getNodeById(String id, Current current) {
        return getNodeByIdForAccount(getUserService(current).getCurrent(),
                id,current);
    }

    @Override
    public SimpleNodeDTO getNodeByIdForAccount(AccountDTO account, String id, Current current) {
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
    public SimpleNodeDTO getNodeByPath(String path, Current current) {
        return getNodeByPathForAccount(getUserService(current).getCurrent(),
                path,current);
    }

    @Override
    public SimpleNodeDTO getNodeByPathForAccount(AccountDTO account, String path, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(path);
        List<SimpleNodeDTO> list = listNodeForAccount(account,query,current);

        SimpleNodeDTO node = null;
        if ((list != null) && (!list.isEmpty())){
            assert (list.size() == 1);
            node = list.get(0);
        }
        return node;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeById(String parentId, Current current) {
        return listSubNodeByIdForAccount(getUserService(current).getCurrent(),
                parentId,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByIdForAccount(AccountDTO account, String parentId, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parentId);
        return listNode(query,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPath(String parentPath, Current current) {
        return listSubNodeByPathForAccount(getUserService(current).getCurrent(),
                parentPath,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPathForAccount(AccountDTO account, String parentPath, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(parentPath);
        return listNode(query,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeById(String parentId, List<Short> typeIdList, Current current) {
        return listFilterSubNodeByIdForAccount(getUserService(current).getCurrent(),
                parentId,typeIdList,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeByIdForAccount(AccountDTO account, String parentId, List<Short> typeIdList, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parentId);
        query.setTypeId(StringUtils.getStringIdList(typeIdList));
        return listNode(query,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeByPath(String parentPath, List<Short> typeIdList, Current current) {
        return listFilterSubNodeByPathForAccount(getUserService(current).getCurrent(),
                parentPath,typeIdList,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeByPathForAccount(AccountDTO account, String parentPath, List<Short> typeIdList, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(parentPath);
        query.setTypeId(StringUtils.getStringIdList(typeIdList));
        return listNode(query,current);
    }


    @Override
    public List<SimpleNodeDTO> listAllNode(Current current) {
        return listAllNodeForAccount(getUserService(current).getCurrent(),current);
    }

    @Override
    public List<SimpleNodeDTO> listAllNodeForAccount(AccountDTO account, Current current) {
        String userId = (account != null) ? account.getId() : null;
        List<SimpleNodeDTO> list = getStorageService(current).listAllNode(userId);
        for (SimpleNodeDTO node : list){
            boolean isReadOnly = getIsReadOnly(account,node,current);
            node.setIsReadOnly(isReadOnly);
        }
        return list;
    }

    @Override
    public List<SimpleNodeDTO> listNode(QueryNodeDTO query, Current current) {
        return listNodeForAccount(getUserService(current).getCurrent(),
                query,current);
    }

    @Override
    public List<SimpleNodeDTO> listNodeForAccount(AccountDTO account, QueryNodeDTO query, Current current) {
        if (StringUtils.isEmpty(query.getUserId())) {
            if (account != null) query.setUserId(account.getId());
        }
        List<SimpleNodeDTO> list = getStorageService(current).listNode(query);
        for (SimpleNodeDTO node : list){
            node.setIsReadOnly(getIsReadOnly(account,node,current));
        }
        return list;
    }

    @Override
    public FullNodeDTO getFullNode(SimpleNodeDTO node, Current current) {
        return getFullNodeForAccount(getUserService().getCurrent(),
                node,current);
    }

    @Override
    public FullNodeDTO getFullNodeForAccount(AccountDTO account, SimpleNodeDTO node, Current current) {
        return getStorageService(current).getFullNodeInfo(node);
    }

    @Override
    public FileNodeDTO getFile(SimpleNodeDTO node, boolean withHistory, Current current) {
        return getFileForAccount(getUserService().getCurrent(),
                node,withHistory,current);
    }

    @Override
    public FileNodeDTO getFileForAccount(AccountDTO account, SimpleNodeDTO node, boolean withHistory, Current current) {
        FileNodeDTO file = fileNodeMap.get(node.getId());
        if ((file == null) || (withHistory)){
            file = getStorageService(current).getFileNodeInfo(node,withHistory);
            fileNodeMap.put(node.getId(),file);
        }
        return file;
    }

    @Override
    public List<FileNodeDTO> listAllSubFile(SimpleNodeDTO parent, List<Short> typeIdList, boolean withHistory, Current current) {
        return listAllSubFileForAccount(getUserService().getCurrent(),
                parent,typeIdList,withHistory,current);
    }

    @Override
    public List<FileNodeDTO> listAllSubFileForAccount(AccountDTO account, SimpleNodeDTO parent, List<Short> typeIdList, boolean withHistory, Current current) {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(parent.getPath());
        query.setTypeId(StringUtils.getStringIdList(typeIdList));

        return listFileForAccount(account,query,withHistory,current);
    }

    @Override
    public List<FileNodeDTO> listFile(@NotNull QueryNodeDTO query, boolean withHistory, Current current) {
        return listFileForAccount(getUserService().getCurrent(),
                query,withHistory,current);
    }

    @Override
    public List<FileNodeDTO> listFileForAccount(AccountDTO account, @NotNull QueryNodeDTO query, boolean withHistory, Current current) {
        if (StringUtils.isEmpty(query.getUserId())) {
            if (account != null) query.setUserId(account.getId());
        }
        List<FileNodeDTO> list = getStorageService(current).listFileNodeInfo(query,withHistory);
        for (FileNodeDTO file : list){
            SimpleNodeDTO node = file.getBasic();
            if (node != null) node.setIsReadOnly(getIsReadOnly(account,node,current));
        }
        return list;
    }

    @Override
    public int writeFileNode(@NotNull FileNodeDTO src, @NotNull FileDataDTO data, Current current) {
        return writeFileNodeForAccount(getUserService().getCurrent(),
                src,data,current);
    }

    @Override
    public int writeFileNodeForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull FileDataDTO data, Current current) {
        String userId = ((account != null) && !StringUtils.isEmpty(account.getId())) ?
                account.getId() : "null";
//        assert (src.getBasic() != null) && (!src.getBasic().getIsReadOnly());
        FileDTO file = getBasicFile(src,userId,current);
        assert (file != null);

        CoreFileDataDTO basicData = BeanUtils.createFrom(data,CoreFileDataDTO.class);
        basicData.setScope(file.getScope());
        basicData.setKey(file.getKey());
        return setting.getFileServer().writeFile(basicData);
    }

    private FileDTO getBasicFile(@NotNull FileNodeDTO src, String userId, Current current){
        assert (src.getBasic() != null);
        String fileId = StringUtils.left(src.getBasic().getId(),StringUtils.DEFAULT_ID_LENGTH);
        assert (!StringUtils.isEmpty(fileId));
        Map<String, FileDTO> basicFileMap = userBasicFileMap.computeIfAbsent(userId, k -> new HashMap<>());
        FileDTO basicFile = basicFileMap.get(fileId);
        if (basicFile == null){
            if (StringUtils.isEmpty(src.getBasic().getOwnerUserId()) ||
                    StringUtils.isSame(userId,src.getBasic().getOwnerUserId())){
                if (!StringUtils.isEmpty(src.getWriteFileKey())){
                    basicFile = new FileDTO(src.getWriteFileScope(),src.getWriteFileKey());
                } else {
                    CoreFileDTO f = fileServer.coreCreateFile(src.getBasic().getPath());
                    CoreFileDTO basicRead = new CoreFileDTO();
                    basicRead.setScope(src.getReadFileScope());
                    basicRead.setKey(src.getReadFileKey());
                    CoreFileCopyResult copyResult = setting.getFileServer().coreCopyFile(basicRead,f);
                    assert (copyResult != null);
                    basicFile = BeanUtils.createFrom(f,FileDTO.class);
                    UpdateNodeDTO updateRequest = new UpdateNodeDTO();
                    updateRequest.setWriteFileScope(basicFile.getScope());
                    updateRequest.setWriteFileKey(basicFile.getKey());
                    SimpleNodeDTO dst = updateNode(src.getBasic(),updateRequest,current);
                    assert (dst != null);
                }
            } else if (!StringUtils.isEmpty(src.getReadFileKey())){
                basicFile = new FileDTO(src.getReadFileScope(),src.getReadFileKey());
            }
            if (basicFile != null){
                basicFileMap.put(fileId,basicFile);
            }
        }
        return basicFile;
    }

    private SimpleNodeDTO updateNode(@NotNull SimpleNodeDTO src,@NotNull UpdateNodeDTO request,Current current){
        SimpleNodeDTO dst = getStorageService(current).updateNode(src,request);
        if (!StringUtils.isEmpty(request.getReadFileScope()) ||
                !StringUtils.isEmpty(request.getReadFileKey()) ||
                !StringUtils.isEmpty(request.getWriteFileScope()) ||
                !StringUtils.isEmpty(request.getWriteFileKey())){
            userBasicFileMap.clear();
            fileNodeMap.clear();
        }
        return dst;
    }

    @Override
    public int writeNode(@NotNull SimpleNodeDTO src, @NotNull FileDataDTO data, Current current) {
        return writeNodeForAccount(getUserService().getCurrent(),
                src,data,current);
    }

    @Override
    public int writeNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull FileDataDTO data, Current current) {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return writeFileNodeForAccount(account,file,data,current);
    }

    @Override
    public FileDataDTO readFileNode(@NotNull FileNodeDTO src, long pos, int size, Current current) {
        return readFileNodeForAccount(getUserService().getCurrent(),
                src,pos,size,current);
    }

    @Override
    public FileDataDTO readFileNodeForAccount(AccountDTO account, @NotNull FileNodeDTO src, long pos, int size, Current current) {
        String userId = ((account != null) && !StringUtils.isEmpty(account.getId())) ?
                account.getId() : "null";
        assert (src.getBasic() != null);
        FileDTO file = getBasicFile(src,userId,current);
        assert (file != null);

        CoreFileDTO basicFile = BeanUtils.createFrom(file,CoreFileDTO.class);
        CoreFileDataDTO basicData = setting.getFileServer().readFile(basicFile,pos,size);
        return BeanUtils.createFrom(basicData,FileDataDTO.class);
    }

    @Override
    public FileDataDTO readNode(@NotNull SimpleNodeDTO src, long pos, int size, Current current) {
        return readNodeForAccount(getUserService().getCurrent(),
                src,pos,size,current);
    }

    @Override
    public FileDataDTO readNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, long pos, int size, Current current) {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return readFileNodeForAccount(account,file,pos,size,current);
    }

    @Override
    public SimpleNodeDTO moveNode(@NotNull SimpleNodeDTO src, @NotNull SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) {
        return moveNodeForAccount(getUserService().getCurrent(),
                src,dstParent,request,current);
    }

    @Override
    public SimpleNodeDTO moveNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) {
        if (src.getIsDirectory()){
            String srcPath = src.getPath();
            UpdateNodeDTO updateRequest = new UpdateNodeDTO();
            updateRequest.setTypeId(ConstService.getPathType(dstParent.getTypeId()));
            updateRequest.setPid(StringUtils.left(dstParent.getId(), StringUtils.DEFAULT_ID_LENGTH));
            updateRequest.setFullName(StringUtils.formatPath(request.getFullName()));
            updateRequest.setTaskId(dstParent.getTaskId());
            SimpleNodeDTO dst = updateNode(src,updateRequest,current);
            (new Thread(){
                @Override
                public void run(){
                    QueryNodeDTO query = new QueryNodeDTO();
                    query.setParentPath(src.getPath());
                    List<FileNodeDTO> fileList = getStorageService(current).listFileNodeInfo(query,false);
                    for (FileNodeDTO file : fileList){
                        assert (file.getBasic() != null);
                        request.setFullName(StringUtils.replace(file.getBasic().getPath(),
                                (srcPath + StringUtils.SPLIT_PATH),""));
                        moveFileForAccount(account,file,dstParent,request,current);
                    }
                }
            }).start();
            return dst;
        } else {
            FileNodeDTO srcFile = getFileForAccount(account,src,false,current);
            return moveFileForAccount(account,srcFile,dstParent,request,current);
        }
    }

    public SimpleNodeDTO moveFileForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) {
        //移动文件
        UpdateNodeDTO updateRequest = BeanUtils.createCleanFrom(request,UpdateNodeDTO.class);
        if (dstParent != null) {
            updateRequest.setTypeId(ConstService.getFileType(dstParent.getTypeId()));
            updateRequest.setPid(StringUtils.left(dstParent.getId(), StringUtils.DEFAULT_ID_LENGTH));
            updateRequest.setTaskId(dstParent.getTaskId());
        }

        String fullName = StringUtils.formatPath(request.getFullName());
        FileDTO readSrcFile = new FileDTO(src.getReadFileScope(),src.getReadFileKey());
        if (isExist(readSrcFile,current)) {
            String dstPath = StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + fullName;
            if (dstParent != null) {
                dstPath = StringUtils.formatPath(dstParent.getPath() + StringUtils.SPLIT_PATH + fullName);
            }
            String dstScope = StringUtils.getDirName(dstPath);
            String dstKey = StringUtils.getFileName(dstPath);
            FileDTO readDstFile = moveFile(readSrcFile, new FileDTO(dstScope, dstKey), current);
            updateRequest.setReadFileScope(readDstFile.getScope());
            updateRequest.setReadFileKey(readDstFile.getKey());
            CoreFileDTO readDstRealFile = BeanUtils.createFrom(readDstFile,CoreFileDTO.class);
            long fileLength = setting.getFileServer().coreGetFileLength(readDstRealFile);
            updateRequest.setFileLength(fileLength);
        }
        FileDTO writeSrcFile = new FileDTO(src.getWriteFileScope(),src.getWriteFileKey());
        if (isExist(writeSrcFile,current)) {
            String dstWritePath = StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + fullName;
            if (dstParent != null) {
                dstWritePath = StringUtils.formatPath(dstParent.getPath() + StringUtils.SPLIT_PATH + fullName);
            }
            String dstWriteScope = StringUtils.getDirName(dstWritePath);
            String dstWriteKey = StringUtils.getFileName(dstWritePath);
            FileDTO writeDstFile = moveFile(writeSrcFile, new FileDTO(dstWriteScope, dstWriteKey), current);
            updateRequest.setWriteFileScope(writeDstFile.getScope());
            updateRequest.setWriteFileKey(writeDstFile.getKey());
        }

        //更改记录
        assert (src.getBasic() != null);
        return updateNode(src.getBasic(),updateRequest,current);
    }

    @Override
    public CommitListResultDTO checkNodeListRequest(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) {
        return checkNodeListRequestForAccount(getUserService().getCurrent(),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO checkNodeListRequestForAccount(AccountDTO account, @NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        return commitNodeListForAccount(account,srcList,request,current);
    }

    @Override
    public SimpleNodeDTO checkNodeRequest(@NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        return checkNodeRequestForAccount(getUserService().getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO checkNodeRequestForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        return commitNodeForAccount(account,src,request,current);
    }

    @Override
    public SimpleNodeDTO checkFileRequest(@NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        return checkFileRequestForAccount(getUserService().getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO checkFileRequestForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        return commitFileForAccount(account,src,request,current);
    }

    @Override
    public CommitListResultDTO auditNodeListRequest(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) {
        return auditNodeListRequestForAccount(getUserService().getCurrent(),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO auditNodeListRequestForAccount(AccountDTO account, @NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_AUDIT);
        return commitNodeListForAccount(account,srcList,request,current);
    }

    @Override
    public SimpleNodeDTO auditNodeRequest(@NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        return auditNodeRequestForAccount(getUserService().getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO auditNodeRequestForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_AUDIT);
        return commitNodeForAccount(account,src,request,current);
    }

    @Override
    public SimpleNodeDTO auditFileRequest(@NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        return auditFileRequestForAccount(getUserService().getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO auditFileRequestForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_AUDIT);
        return commitFileForAccount(account,src,request,current);
    }

    @Override
    public CommitListResultDTO commitNodeList(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) {
        return commitNodeListForAccount(getUserService().getCurrent(),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO commitNodeListForAccount(AccountDTO account, @NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) {
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
    public SimpleNodeDTO commitNode(@NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        return commitNodeForAccount(getUserService().getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO commitNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return commitFileForAccount(account,file,request,current);
    }

    @Override
    public SimpleNodeDTO commitFile(@NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        return commitFileForAccount(getUserService().getCurrent(),src,request,current);
    }

    @Override
    public SimpleNodeDTO commitFileForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) {
        //确定和调整文件提交动作
        Short actionTypeId = (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(request.getActionTypeId())) ?
                ConstService.STORAGE_ACTION_TYPE_COMMIT : request.getActionTypeId();
        String actionName = ConstService.getActionName(actionTypeId);

        //根据提交动作取出相关要提交的文件存放的相对位置
        Short serverTypeId = ConstService.getActionFileServerTypeId(actionTypeId);
        String serverAddress = ConstService.getActionFileServerAddress(actionTypeId);
        String path = ConstService.getActionNodePath(actionTypeId);
        if (!StringUtils.isEmpty(path)){
            FullNodeDTO node = getFullNodeForAccount(account,src.getBasic(),current);
            StringElementDTO pathElement = BeanUtils.createCleanFrom(request, StringElementDTO.class);
            path = convertPath(pathElement,node,path,actionName);
        }

        //根据相对位置和服务器类型计算要提交的文件存放的绝对位置
        String fullPath = path;
        String fullName = StringUtils.getFileName(fullPath);
        if (!ConstService.FILE_SERVER_TYPE_WEB.equals(serverTypeId)){
            if (!StringUtils.isSame(StringUtils.left(fullPath,1),StringUtils.SPLIT_PATH)){
                fullPath = StringUtils.formatPath(StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + path);
            }
            if (StringUtils.isSame(fullPath,src.getBasic().getPath())){
                fullName = StringUtils.getFileNameWithoutExt(fullName) + StringUtils.SPLIT_NAME_PART +
                        actionName + StringUtils.getFileExt(fullName);
                fullPath = StringUtils.formatPath(StringUtils.getDirName(fullPath) + StringUtils.SPLIT_PATH + fullName);
            }
        }

        //建立或更新版本节点
        SimpleNodeDTO targetNode = getNodeByPathForAccount(account,fullPath,current);
        CreateVersionRequestDTO versionRequest = BeanUtils.createFrom(request,CreateVersionRequestDTO.class);
        versionRequest.setActionTypeId(actionTypeId);
        versionRequest.setMainFileId(src.getMainFileId());
        if (StringUtils.isEmpty(versionRequest.getMainFileId())) versionRequest.setMainFileId(src.getBasic().getId());
        versionRequest.setServerTypeId(serverTypeId);
        versionRequest.setServerAddress(serverAddress);
        versionRequest.setPath(fullPath);
        if ((targetNode == null) || (StringUtils.isEmpty(targetNode.getId()))){
            targetNode = createVersionForAccount(account,src,versionRequest,current);
        } else {
            FileNodeDTO targetFile = getFileForAccount(account,targetNode,false,current);
            targetNode = updateVersionForAccount(account,src,targetFile,versionRequest,current);
        }

        //发送通知消息
        String typeIdString = ConstService.getActionNoticeTypeIdString(actionTypeId);
        if (StringUtils.isNotEmpty(typeIdString)){
            NoticeRequestDTO noticeRequest = new NoticeRequestDTO();
            noticeRequest.setTypeIdString(typeIdString);
            noticeRequest.setProjectName(src.getBasic().getProjectName());
            noticeRequest.setTaskName(src.getBasic().getTaskName());
            noticeRequest.setCompanyName(src.getBasic().getCompanyName());
            getNoticeService().sendNotice(noticeRequest);
        }

        return targetNode;
    }

    private String convertPath(@NotNull StringElementDTO pathElement, @NotNull FullNodeDTO node, String sPath, String actionName){
        String path = sPath;
        if (!StringUtils.isEmpty(path)){
            BeanUtils.copyProperties(node.getBasic(), pathElement);
            BeanUtils.copyProperties(node, pathElement);
            pathElement.setSrcPath(node.getBasic().getStoragePath());
            pathElement.setUserName(node.getBasic().getOwnerName());
            pathElement.setActionName(actionName);
            path = StringUtils.formatPath(ConstService.convertString(sPath,BeanUtils.cleanProperties(pathElement)));
        }
        return path;
    }

    @Override
    public SimpleNodeDTO createVersion(FileNodeDTO src, CreateVersionRequestDTO request, Current current) {
        return createVersionForAccount(getUserService().getCurrent(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO createVersionForAccount(AccountDTO account, FileNodeDTO src, CreateVersionRequestDTO request, Current current) {

        releaseFileNodeForAccount(account,src,0,current);

        Short actionTypeId = (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(request.getActionTypeId())) ?
                ConstService.STORAGE_ACTION_TYPE_BACKUP : request.getActionTypeId();

        //生成实际目标文件
        CoreFileDTO targetFile = null;
        String path = request.getPath();
        if (StringUtils.isNotEmpty(path)) {
            if (!StringUtils.isSame(StringUtils.left(path,1),StringUtils.SPLIT_PATH)) {
                //是相对路径
                path = StringUtils.formatPath(StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + path);
            }
            targetFile = new CoreFileDTO(request.getServerAddress(),
                    StringUtils.getDirName(path),StringUtils.getFileName(path));
        }
        targetFile = createRealFileForAccount(account,request.getServerTypeId(),targetFile,src);

        //设定创建节点申请
        Short typeId = ConstService.getActionNodeTypeId(actionTypeId);
        if (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(typeId)) typeId = src.getBasic().getTypeId();

        //查找父节点
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath(path);
        if (account != null) query.setUserId(account.getId());
        List<SimpleNodeDTO> parentList = getStorageService(current).listNode(query);
        SimpleNodeDTO parent = null;
        if ((parentList != null) && (!parentList.isEmpty())){
            parent = parentList.get(0);
        }

        UpdateNodeDTO createRequest = BeanUtils.createFrom(request,UpdateNodeDTO.class);
        BeanUtils.copyProperties(src.getBasic(),createRequest);
        BeanUtils.copyProperties(src,createRequest);

        if ((parent != null) && (!StringUtils.isEmpty(parent.getId()))) {
            createRequest.setPid(parent.getId());
            createRequest.setTaskId(parent.getTaskId());
            createRequest.setParentPath(parent.getPath());
            String fullName = path.substring(parent.getPath().length() + StringUtils.SPLIT_PATH.length());
            createRequest.setFullName(fullName);
        } else {
            createRequest.setPid(null);
            createRequest.setTaskId(null);
            createRequest.setParentPath(null);
            createRequest.setFullName(path);
        }
        createRequest.setTypeId(typeId);
        createRequest.setActionTypeId(actionTypeId);
        createRequest.setOwnerUserId(request.getUserId());
//        createRequest.setFileLength(copyResult.getFileLength());
//        createRequest.setFileChecksum(copyResult.getFileChecksum());
        createRequest.setReadFileScope(targetFile.getScope());
        createRequest.setReadFileKey(targetFile.getKey());
        createRequest.setWriteFileScope(null);
        createRequest.setWriteFileKey(null);

        return getStorageService(current).createNode(createRequest);
    }

    @Override
    public SimpleNodeDTO updateVersion(FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request, Current current) {
        return updateVersionForAccount(getUserService().getCurrent(),
                src,dst,request,current);
    }

    @Override
    public SimpleNodeDTO updateVersionForAccount(AccountDTO account, FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request, Current current) {

        releaseFileNodeForAccount(account,src,0,current);

        Short actionTypeId = (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(request.getActionTypeId())) ?
                ConstService.STORAGE_ACTION_TYPE_BACKUP : request.getActionTypeId();
        Short typeId = ConstService.getActionNodeTypeId(actionTypeId);
        if (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(typeId)) typeId = src.getBasic().getTypeId();
        String path = request.getPath();
        CoreFileDTO targetFile = setting.getFileServer().coreCreateFile(path);
        CoreFileDTO srcFile = getCoreFile(account,src);
        CoreFileCopyResult copyResult = setting.getFileServer().coreCopyFile(srcFile,targetFile);

        //建立更新版本申请
        UpdateNodeDTO updateRequest = BeanUtils.createFrom(request,UpdateNodeDTO.class);
        BeanUtils.copyProperties(src.getBasic(),updateRequest);
        BeanUtils.copyProperties(src,updateRequest);
        updateRequest.setTypeId(typeId);
        updateRequest.setOwnerUserId(request.getUserId());
        updateRequest.setFileLength(copyResult.getFileLength());
        updateRequest.setFileChecksum(copyResult.getFileChecksum());
        updateRequest.setReadFileScope(targetFile.getScope());
        updateRequest.setReadFileKey(targetFile.getKey());
        updateRequest.setParentPath(null);
        updateRequest.setFullName(null);
        assert (dst != null);
        if (!StringUtils.isEmpty(dst.getWriteFileKey())){
            Short dActionTypeId = ConstService.STORAGE_ACTION_TYPE_BACKUP;
            String dActionName = ConstService.getActionName(dActionTypeId);
            String dFullName = dst.getBasic().getName();
            String dsPath = ConstService.getActionNodePath(dActionTypeId);
            if (!StringUtils.isEmpty(dsPath)){
                FullNodeDTO dNode = getFullNodeForAccount(account,dst.getBasic(),current);
                StringElementDTO pathElement = BeanUtils.createCleanFrom(request,StringElementDTO.class);
                dFullName = convertPath(pathElement,dNode,dsPath,dActionName);
            }
            String dFullPath = dFullName;
            if (!StringUtils.isSame(StringUtils.left(dFullPath,1),StringUtils.SPLIT_PATH)) {
                dFullPath = StringUtils.getDirName(dst.getBasic().getPath()) + StringUtils.SPLIT_PATH + dFullName;
            }
            if (StringUtils.isSame(dFullPath,dst.getBasic().getPath())){
                dFullName = StringUtils.getFileNameWithoutExt(dFullName) + StringUtils.SPLIT_NAME_PART +
                        dActionName + StringUtils.getFileExt(dFullName);
                dFullPath = StringUtils.formatPath(StringUtils.getDirName(dFullPath) + StringUtils.SPLIT_PATH + dFullName);
            }

            //创建历史版本
            CreateVersionRequestDTO backupRequest = BeanUtils.createFrom(request,CreateVersionRequestDTO.class);
            backupRequest.setActionTypeId(actionTypeId);
            backupRequest.setFileVersion(dst.getFileVersion());
            backupRequest.setMainFileId(dst.getMainFileId());
            if (StringUtils.isEmpty(backupRequest.getMainFileId())) backupRequest.setMainFileId(dst.getBasic().getId());
            backupRequest.setRemark(dst.getFileRemark());
            backupRequest.setUserId(dst.getBasic().getOwnerUserId());
            backupRequest.setPath(dFullPath);
            createVersionForAccount(account,dst,backupRequest,current);

            //复制写入版本
            CoreFileDTO targetWriteFile = setting.getFileServer().coreCreateFile(path);
            CoreFileCopyResult copyWriteResult = setting.getFileServer().coreCopyFile(srcFile,targetWriteFile);
            updateRequest.setWriteFileScope(targetWriteFile.getScope());
            updateRequest.setWriteFileKey(targetWriteFile.getKey());
        } else {
            updateRequest.setWriteFileScope(null);
            updateRequest.setWriteFileKey(null);
        }

        return updateNode(dst.getBasic(),updateRequest,current);
    }

    private CoreFileDTO getCoreFile(AccountDTO account, @NotNull FileNodeDTO src){
        return getCoreFile(src,((account == null) ? null : account.getId()));
    }
    private CoreFileDTO getCoreFile(@NotNull FileNodeDTO src,String userId){
        CoreFileDTO coreFile = new CoreFileDTO();
        String ownerUserId = src.getBasic().getOwnerUserId();
        if ((ownerUserId == null) || StringUtils.isSame(userId,ownerUserId)) {
            coreFile.setScope(src.getWriteFileScope());
            if (StringUtils.isEmpty(coreFile.getScope())) coreFile.setScope(src.getReadFileScope());
            coreFile.setKey(src.getWriteFileKey());
            if (StringUtils.isEmpty(coreFile.getKey())) coreFile.setKey(src.getReadFileKey());
        } else {
            coreFile.setScope(src.getReadFileScope());
            coreFile.setKey(src.getReadFileKey());
        }
        return coreFile;
    }

    @Override
    public SimpleNodeDTO createDirectory(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) {
        return createDirectoryForAccount(getUserService().getCurrent(),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createDirectoryForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) {
        request.setIsDirectory(true);
        return createNodeForAccount(account,parent,request,current);
    }

    @Override
    public SimpleNodeDTO createFile(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) {
        return createFileForAccount(getUserService().getCurrent(),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createFileForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) {
        request.setIsDirectory(false);
        return createNodeForAccount(account,parent,request,current);
    }

    @Override
    public SimpleNodeDTO createNode(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) {
        return createNodeForAccount(getUserService(current).getCurrent(),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createNodeForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) {
        UpdateNodeDTO createRequest = BeanUtils.createCleanFrom(request,UpdateNodeDTO.class);
        if ((parent != null) && (!StringUtils.isEmpty(parent.getId()))) {
            if (request.getIsDirectory()) {
                createRequest.setTypeId(ConstService.getPathType(parent.getTypeId()));
            } else {
                createRequest.setTypeId(ConstService.getFileType(parent.getTypeId()));
            }
            createRequest.setPid(parent.getId());
            createRequest.setTaskId(parent.getTaskId());
            createRequest.setParentPath(parent.getPath());
            createRequest.setParentStoragePath(parent.getStoragePath());
        } else if (request.getIsDirectory()) {
            createRequest.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_UNKNOWN);
        } else {
            createRequest.setTypeId(ConstService.STORAGE_NODE_TYPE_UNKNOWN);
        }
        if (StringUtils.isEmpty(createRequest.getOwnerUserId())) {
            if (account != null) createRequest.setOwnerUserId(account.getId());
        }
        if (createRequest.getFileLength() > 0) {
            String path = StringUtils.formatPath(createRequest.getParentPath() + StringUtils.SPLIT_PATH + createRequest.getFullName());
            CoreFileDTO basicWrite = setting.getFileServer().coreCreateFile(path);
            assert (basicWrite != null);
            boolean isOk = setting.getFileServer().coreSetFileLength(basicWrite,createRequest.getFileLength());
            assert (isOk);
            createRequest.setWriteFileScope(basicWrite.getScope());
            createRequest.setWriteFileKey(basicWrite.getKey());
        }
        return getStorageService(current).createNode(createRequest);
    }

    /**
     * 同步方式获取业务接口代理对象
     */
    public static FileServicePrx getInstance(String adapterName) {
        FileServiceImpl prx = new FileServiceImpl();
        return prx.getServicePrx("FileService", adapterName, FileServicePrx.class, _FileServicePrxI.class);
    }

    public static FileServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public FileDTO copyFile(FileDTO src, FileDTO dst, Current current) {
        CoreFileDTO basicSrc = BeanUtils.createFrom(BeanUtils.cleanProperties(src),CoreFileDTO.class);
        CoreFileDTO basicDst = BeanUtils.createFrom(BeanUtils.cleanProperties(dst),CoreFileDTO.class);
        assert (fileServer != null);
        CoreFileDTO basicResult = fileServer.copyFile(basicSrc,basicDst);
        return BeanUtils.createFrom(basicResult, FileDTO.class);
    }

    public FileDTO moveFile(FileDTO src, FileDTO dst, Current current) {
        CoreFileDTO basicSrc = BeanUtils.createFrom(BeanUtils.cleanProperties(src),CoreFileDTO.class);
        CoreFileDTO basicDst = BeanUtils.createFrom(BeanUtils.cleanProperties(dst),CoreFileDTO.class);
        assert (fileServer != null);
        CoreFileDTO basicResult = fileServer.coreMoveFile(basicSrc,basicDst);
        return BeanUtils.createFrom(basicResult, FileDTO.class);
    }

    @Override
    public void setFileServerType(int type, Current current) {
        setting.setServerTypeId((short)type);
        fileServer = setting.getFileServer();
    }

    @Override
    public int getFileServerType(Current current) {
        return setting.getServerTypeId();
    }

    @Override
    public boolean isExist(FileDTO src, Current current) {
        CoreFileDTO fileDTO = BeanUtils.createFrom(src, CoreFileDTO.class);
        assert (fileServer != null);
        return fileServer.isExist(fileDTO);
    }


}
