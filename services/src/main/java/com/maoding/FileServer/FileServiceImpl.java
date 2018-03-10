package com.maoding.FileServer;

import com.maoding.Base.BaseLocalService;
import com.maoding.Common.Config.WebServiceConfig;
import com.maoding.Common.ConstService;
import com.maoding.Common.zeroc.CustomException;
import com.maoding.Common.zeroc.ErrorCode;
import com.maoding.Common.zeroc.IdNameDTO;
import com.maoding.Common.zeroc.StringElementDTO;
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
import com.maoding.Utils.FileUtils;
import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    @Autowired
    private WebServiceConfig webServiceConfig;

    private CoreFileServer fileServer = new DiskFileServer();

    private static Map<String,Map<String,FileDTO>> userBasicFileMap = new HashMap<>();
    private static Map<String, FileNodeDTO> fileNodeMap = new HashMap<>();

    @Override
    public SimpleNodeDTO getNodeByFuzzyPath(String fuzzyPath, Current current) throws CustomException {
        return null;
    }

    @Override
    public List<SimpleNodeDTO> listWebArchiveDir(String projectId, Current current) throws CustomException {
        return listWebArchiveDirForAccount(getCurrentAccount(),
                projectId,current);
    }

    @Override
    public List<SimpleNodeDTO> listWebArchiveDirForAccount(AccountDTO account, String projectId, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setProjectId(projectId);
        query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
        List<SimpleNodeDTO> nodeList = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
        return nodeList;
    }

    private File getLocalMirror(CoreFileServer coreFileServer, CoreFileDTO coreSrc, String mirrorPath){
        final Integer MAX_BLOCK_SIZE = (8192 * 1024);
        final String DEFAULT_MIRROR_PATH = "c:/work/file_server/mirror";

        if (StringUtils.isEmpty(mirrorPath)) mirrorPath = DEFAULT_MIRROR_PATH;
        String path = StringUtils.formatPath(mirrorPath + StringUtils.SPLIT_PATH
                + UUID.randomUUID().toString() + StringUtils.getFileExt(coreSrc.getKey()));
        FileUtils.ensureDirExist(StringUtils.getDirName(path));
        File mirror = new File(path);

        RandomAccessFile out = null;
        int size = MAX_BLOCK_SIZE;
        try {
            out = new RandomAccessFile(mirror, "rw");
            long pos = 0;
            while (size > 0){
                CoreFileDataDTO fileData = coreFileServer.coreReadFile(coreSrc,pos,size);
                if (fileData == null) break;
                pos = fileData.getPos();
                size = fileData.getSize();
                if (out.length() < (pos + size)) out.setLength(pos + size);
                out.seek(pos);
                out.write(fileData.getData(),0,size);
                pos += size;
            }
        } catch (IOException e) {
            log.error("无法创建本地镜像文件" + path);
        } finally {
            FileUtils.close(out);
        }
        return mirror;
    }
    private File getLocalMirror(CoreFileServer coreFileServer, CoreFileDTO coreSrc){
        return getLocalMirror(coreFileServer,coreSrc,null);
    }

    public CoreFileDTO createRealFileForAccount(AccountDTO account, Short serverTypeId, CoreFileDTO dst, FileNodeDTO src){
        String serverAddress = null;
        if (dst != null) serverAddress = dst.getServerAddress();

        CoreFileServer dstServer = setting.getCoreFileServer(serverTypeId,serverAddress);
        if (isWebServer(dstServer)){
            if ((src != null) && (src.getBasic() != null) && StringUtils.isNotEmpty(src.getBasic().getName())){
                if (dst == null) dst = new CoreFileDTO();
                if (StringUtils.isEmpty(dst.getKey())) dst.setKey(src.getBasic().getName());
            }
        }

        CoreCreateFileRequest createRequest = new CoreCreateFileRequest();
        if (src != null){
            CoreFileDTO realSrc = getCoreFile(account,src);
            if (realSrc != null) {
                CoreFileServer srcServer = setting.getCoreFileServer(Short.parseShort(src.getServerTypeId()),src.getServerAddress());
                createRequest.setSrcFile(getLocalMirror(srcServer,realSrc));
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
                    query.setFuzzyId(idStringBuilder.toString());
                    query.setFuzzyPath(StringUtils.formatPath(dst.getScope()));
                    query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
                    List<SimpleNodeDTO> list = null;
                    try {
                        list = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
                    } catch (CustomException e) {
                        e.printStackTrace();
                    }
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
        return createRealFileForAccount(getCurrentAccount(),serverTypeId,dst,src);
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
        CoreFileServer dstServer = setting.getCoreFileServer(serverTypeId,serverAddress);

        CoreFileDTO dst = new CoreFileDTO();
        if (StringUtils.isNotEmpty(request.getPath())) {
            dst.setScope(StringUtils.getDirName(request.getPath()));
            dst.setKey(StringUtils.getFileName(request.getPath()));
        } else if ((src != null) && (src.getBasic() != null) && StringUtils.isNotEmpty(src.getBasic().getName())){
            dst.setKey(src.getBasic().getName());
        }

        CoreCreateFileRequest createRequest = new CoreCreateFileRequest();
        if (src != null){
            AccountDTO account = new AccountDTO();
            account.setId(request.getUserId());
            CoreFileDTO realSrc = getCoreFile(account,src);
            if (realSrc != null) {
                CoreFileServer srcServer = setting.getCoreFileServer(Short.parseShort(src.getServerTypeId()),src.getServerAddress());
                createRequest.setSrcFile(getLocalMirror(srcServer,realSrc));
            }
        }


        if (isWebServer(dstServer)){
            dstServer.coreSetServerAddress(webServiceConfig.getUploadUrl());
            if (StringUtils.isNotEmpty(request.getPid())) {
                QueryNodeDTO query = new QueryNodeDTO();
                query.setId(request.getPid());
                List<SimpleNodeDTO> list = null;
                try {
                    list = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
                } catch (CustomException e) {
                    e.printStackTrace();
                }
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
                query.setFuzzyId(idStringBuilder.toString());
                if (StringUtils.isNotEmpty(request.getPath())) {
                    query.setFuzzyPath(StringUtils.formatPath(request.getPath()));
                }
                query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
                List<SimpleNodeDTO> list = null;
                try {
                    list = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
                } catch (CustomException e) {
                    e.printStackTrace();
                }
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
                createRequest.setAccountId(getCurrentAccount().getId());
            }
        }

        dst = dstServer.coreCreateFile(dst,BeanUtils.cleanProperties(createRequest));

        return dst;
    }
    
    private CoreCreateFileRequest setParentInfo(@NotNull CoreCreateFileRequest request, SimpleNodeDTO parent){
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
            CoreFileServer fileServer = setting.getCoreFileServer(request.getSrcServerTypeId(),request.getSrcServerAddress());
            dst = fileServer.coreCreateFile(dst);
            fileServer.coreCopyFile(src,dst);
        } else { //不同文件服务器复制
            Short srcServerType = request.getSrcServerTypeId();
            String srcServerAddress = src.getServerAddress();
            Short dstServerType = request.getDstServerTypeId();
            String dstServerAddress = request.getDstServerAddress();
            CoreFileServer srcFileServer = setting.getCoreFileServer(srcServerType,srcServerAddress);
            CoreFileServer dstFileServer = setting.getCoreFileServer(dstServerType,dstServerAddress);
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
    public boolean createMirrorForAccount(AccountDTO account, FileNodeDTO src, Current current) throws CustomException {
        return false;
    }

    @Override
    public CommitListResultDTO issueNodeList(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return issueNodeListForAccount(getCurrentAccount(),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO issueNodeListForAccount(AccountDTO account, List<SimpleNodeDTO> srcList, CommitRequestDTO request, Current current) throws CustomException {
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
    public SimpleNodeDTO issueNode(SimpleNodeDTO src, CommitRequestDTO request, Current current) throws CustomException {
        return issueNodeForAccount(getCurrentAccount(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO issueNodeForAccount(AccountDTO account, SimpleNodeDTO src, CommitRequestDTO request, Current current) throws CustomException {        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE);
        return commitNodeForAccount(account,src,request,current);
    }

    @Override
    public SimpleNodeDTO issueFile(FileNodeDTO src, CommitRequestDTO request, Current current) throws CustomException {
        return issueFileForAccount(getCurrentAccount(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO issueFileForAccount(AccountDTO account, FileNodeDTO src, CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_ISSUE);

        if (ConstService.FILE_SERVER_TYPE_UNKNOWN.equals(request.getServerTypeId())) request.setServerTypeId(ConstService.FILE_SERVER_TYPE_WEB);

        return commitFileForAccount(account,src,request,current);
    }

    private void updateStringElement(@NotNull StringElementDTO stringElement, @NotNull FileNodeDTO src){
        SimpleNodeDTO node = src.getBasic();
        assert (node != null);
        BeanUtils.copyCleanProperties(node,stringElement);
        BeanUtils.copyCleanProperties(src,stringElement);
        stringElement.setSrcPath(node.getStoragePath());
        stringElement.setUserName(node.getOwnerName());
        stringElement.setUserId(node.getOwnerUserId());
    }

    private void updateStringElement(@NotNull StringElementDTO stringElement, @NotNull CommitRequestDTO request){
        BeanUtils.copyCleanProperties(request,stringElement);
    }

    private void updateStringElement(@NotNull StringElementDTO stringElement, @NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, String actionName){
        updateStringElement(stringElement,src);
        updateStringElement(stringElement,request);
        stringElement.setActionName(actionName);
    }

    @Override
    public SimpleNodeDTO changeNodeOwner(SimpleNodeDTO src, UserDTO dstOwner, Current current) throws CustomException {
        return changeNodeOwnerForAccount(getCurrentAccount(),
                src,dstOwner,current);
    }

    @Override
    public SimpleNodeDTO changeNodeOwnerForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull UserDTO dstOwner, Current current) throws CustomException {
        if (getIsReadOnly(account,src,current)) throw new CustomException(ErrorCode.NoPermission,"没有权限");
        UpdateNodeDTO updateRequest = new UpdateNodeDTO();
        updateRequest.setOwnerUserId(dstOwner.getId());
        return getStorageService().updateNodeWithParent(src,null,updateRequest);
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
    public boolean login(LoginDTO loginInfo, Current current) throws CustomException {
        return getUserServicePrx().login(loginInfo);
    }

    @Override
    public List<String> setNoticeClient(String userId, NoticeClientPrx client, Current current) throws CustomException {
        getNoticeServicePrx().subscribeTopicForUser(userId,client);
        return getNoticeServicePrx().listSubscribedTopic(userId);
    }

    @Override
    public UserServicePrx getUserService(Current current) throws CustomException {
        return getUserServicePrx();
    }

    private UserServicePrx getUserServicePrx() {
        return setting.getUserService();
    }


    private AccountDTO getCurrentAccount(){
        return getUserServicePrx().getCurrent();
    }


    @Override
    public NoticeServicePrx getNoticeService(Current current) throws CustomException {
        return getNoticeServicePrx();
    }
    private NoticeServicePrx getNoticeServicePrx(){
        return setting.getNoticeService();
    }

    private StorageServicePrx getStorageService(){
        return setting.getStorageService();
    }

    private CoreFileServer getCoreFileServer(Short serverTypeId, String serverAddress) {
        return setting.getCoreFileServer(serverTypeId,serverAddress);
    }
    private CoreFileServer getCoreFileServer() {
        return setting.getCoreFileServer();
    }
    private FileServicePrx getRemoteFileService(String serverAddress) {
        return setting.getRemoteFileService(serverAddress);
    }

    @Override
    public List<ProjectRoleDTO> listProjectRoleByProjectId(String projectId, Current current) throws CustomException {
        return listProjectRoleByProjectIdForAccount(getCurrentAccount(),
                projectId,current);
    }

    @Override
    public List<ProjectRoleDTO> listProjectRoleByProjectIdForAccount(AccountDTO account, String projectId, Current current) throws CustomException {
        return getUserServicePrx().listProjectRoleByProjectId(projectId);
    }

    @Override
    public List<IdNameDTO> listMajor(Current current) throws CustomException {
        return listMajorForAccount(getCurrentAccount(),
                current);
    }

    @Override
    public List<IdNameDTO> listMajorForAccount(AccountDTO account, Current current) throws CustomException {
        return ConstService.listMajor();
    }

    @Override
    public List<IdNameDTO> listAction(Current current) throws CustomException {
        return listActionForAccount(getCurrentAccount(),
                current);
    }

    @Override
    public List<IdNameDTO> listActionForAccount(AccountDTO account, Current current) throws CustomException {
        return ConstService.listAction();
    }

    /**
     * @param path
     * @param current The Current object for the invocation.
     * @deprecated 尚未验证
     **/
    @Override
    public ProjectDTO getProjectInfoByPath(String path, Current current) throws CustomException {
        return null;
    }

    /**
     * @param account
     * @param path
     * @param current The Current object for the invocation.  @deprecated 尚未验证
     **/
    @Override
    public ProjectDTO getProjectInfoByPathForAccount(AccountDTO account, String path, Current current) throws CustomException {
        return null;
    }

    @Override
    public boolean deleteNode(SimpleNodeDTO src, Current current) throws CustomException {
        return deleteNodeForAccount(getCurrentAccount(),
                src,current);
    }

    @Override
    public boolean deleteNodeForAccount(AccountDTO account, SimpleNodeDTO src, Current current) throws CustomException {
        return getStorageService().deleteNodeById(src.getId());
    }

    @Override
    public boolean setNodeLength(SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        return setNodeLengthForAccount(getCurrentAccount(),
                src,fileLength,current);
    }

    @Override
    public boolean setNodeLengthForAccount(AccountDTO account, SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return setFileNodeLengthForAccount(getCurrentAccount(),
                file,fileLength,current);
    }

    @Override
    public boolean setFileNodeLength(FileNodeDTO src, long fileLength, Current current) throws CustomException {
        return setFileNodeLengthForAccount(getCurrentAccount(),
                src,fileLength,current);
    }

    @Override
    public boolean setFileNodeLengthForAccount(AccountDTO account, FileNodeDTO src, long fileLength, Current current) throws CustomException {
        UpdateNodeDTO updateRequest = new UpdateNodeDTO();
        CoreFileDTO basicWrite = new CoreFileDTO();
        basicWrite.setScope(src.getWriteFileScope());
        basicWrite.setKey(src.getWriteFileKey());
        if (!getCoreFileServer().coreIsExist(basicWrite)){
            assert (src.getBasic() != null);
            basicWrite = getCoreFileServer().coreCreateFile(src.getBasic().getPath());
            updateRequest.setWriteFileScope(basicWrite.getScope());
            updateRequest.setWriteFileKey(basicWrite.getKey());
        }
        getCoreFileServer().coreSetFileLength(basicWrite,fileLength);
        updateRequest.setFileLength(fileLength);
        SimpleNodeDTO dst = updateNode(src.getBasic(), updateRequest, current);
        assert (dst != null);
        return true;
    }

    @Override
    public boolean releaseNode(SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        return releaseNodeForAccount(getCurrentAccount(),
                src,fileLength,current);
    }

    @Override
    public boolean releaseNodeForAccount(AccountDTO account, SimpleNodeDTO src, long fileLength, Current current) throws CustomException {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return releaseFileNodeForAccount(account,file,fileLength,current);
    }

    @Override
    public boolean releaseFileNode(FileNodeDTO src, long fileLength, Current current) throws CustomException {
        return releaseFileNodeForAccount(getCurrentAccount(),
                src,fileLength,current);
    }

    @Override
    public boolean releaseFileNodeForAccount(AccountDTO account, FileNodeDTO src, long fileLength, Current current) throws CustomException {
        CoreFileDTO basicWrite = new CoreFileDTO();
        basicWrite.setScope(src.getWriteFileScope());
        basicWrite.setKey(src.getWriteFileKey());
        if (fileLength == 0) fileLength = getCoreFileServer().coreGetFileLength(basicWrite);
        if (fileLength > 0) {
            getCoreFileServer().coreSetFileLength(basicWrite,fileLength);
        }
        String fileMd5 = getCoreFileServer().coreCalcChecksum(basicWrite);
        if (!StringUtils.isSame(fileMd5,src.getFileChecksum())){
            CoreFileDTO basicRead = new CoreFileDTO();
            if (StringUtils.isEmpty(src.getReadFileKey())){
                basicRead.setScope(src.getWriteFileScope());
                basicRead.setKey(src.getWriteFileKey());
                basicRead = getCoreFileServer().coreCreateFile(src.getBasic().getPath());
            } else {
                basicRead.setScope(src.getReadFileScope());
                basicRead.setKey(src.getReadFileKey());
                if (getCoreFileServer().coreIsExist(basicRead)){
                    basicRead = getCoreFileServer().coreCreateFile(src.getBasic().getPath());
                }
            }
            CoreFileCopyResult copyResult = getCoreFileServer().coreCopyFile(basicWrite,basicRead);
            if (copyResult != null) {
                UpdateNodeDTO updateRequest = new UpdateNodeDTO();
                updateRequest.setReadFileScope(basicRead.getScope());
                updateRequest.setReadFileKey(basicRead.getKey());
                updateRequest.setFileLength(copyResult.getFileLength());
                updateRequest.setFileChecksum(copyResult.getFileChecksum());
                fileLength = getCoreFileServer().coreGetFileLength(basicWrite);
                updateRequest.setFileLength(fileLength);
                SimpleNodeDTO dst = updateNode(src.getBasic(), updateRequest, current);
                assert (dst != null);
            }
            return (copyResult != null);
        }
        return true;
    }

    @Override
    public boolean reloadNode(SimpleNodeDTO src, Current current) throws CustomException {
        return reloadNodeForAccount(getCurrentAccount(),
                src,current);
    }

    @Override
    public boolean reloadNodeForAccount(AccountDTO account, SimpleNodeDTO src, Current current) throws CustomException {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return reloadFileNodeForAccount(account,file,current);
    }

    @Override
    public boolean reloadFileNode(FileNodeDTO src, Current current) throws CustomException {
        return reloadFileNodeForAccount(getCurrentAccount(),
                src,current);
    }

    @Override
    public boolean reloadFileNodeForAccount(AccountDTO account, FileNodeDTO src, Current current) throws CustomException {
        CoreFileDTO basicWrite = getCoreFile(account,src);
        if (getCoreFileServer().coreIsExist(basicWrite)){
            assert (src.getBasic() != null);
            basicWrite = getCoreFileServer().coreCreateFile(src.getBasic().getPath());
        }

        CoreFileDTO basicRead = new CoreFileDTO();
        basicRead.setScope(src.getReadFileScope());
        basicRead.setKey(src.getReadFileKey());
        CoreFileCopyResult copyResult = getCoreFileServer().coreCopyFile(basicRead,basicWrite);
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
    public SimpleNodeDTO getNodeById(String id, Current current) throws CustomException {
        return getNodeByIdForAccount(getCurrentAccount(),
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
        return getNodeByPathForAccount(getCurrentAccount(),
                path,current);
    }

    @Override
    public SimpleNodeDTO getNodeByPathForAccount(AccountDTO account, String path, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPath(path);
        List<SimpleNodeDTO> list = listNodeForAccount(account,query,current);

        SimpleNodeDTO node = null;
        if ((list != null) && (!list.isEmpty())){
//            assert (list.size() == 1);
            node = list.get(0);
        }
        return node;
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeById(String parentId, Current current) throws CustomException {
        return listSubNodeByIdForAccount(getCurrentAccount(),
                parentId,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByIdForAccount(AccountDTO account, String parentId, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parentId);
        return listNode(query,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPath(String parentPath, Current current) throws CustomException {
        return listSubNodeByPathForAccount(getCurrentAccount(),
                parentPath,current);
    }

    @Override
    public List<SimpleNodeDTO> listSubNodeByPathForAccount(AccountDTO account, String parentPath, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(parentPath);
        return listNode(query,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeById(String parentId, List<Short> typeIdList, Current current) throws CustomException {
        return listFilterSubNodeByIdForAccount(getCurrentAccount(),
                parentId,typeIdList,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeByIdForAccount(AccountDTO account, String parentId, List<Short> typeIdList, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setPid(parentId);
        query.setTypeId(StringUtils.getStringIdList(typeIdList));
        return listNode(query,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeByPath(String parentPath, List<Short> typeIdList, Current current) throws CustomException {
        return listFilterSubNodeByPathForAccount(getCurrentAccount(),
                parentPath,typeIdList,current);
    }

    @Override
    public List<SimpleNodeDTO> listFilterSubNodeByPathForAccount(AccountDTO account, String parentPath, List<Short> typeIdList, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(parentPath);
        query.setTypeId(StringUtils.getStringIdList(typeIdList));
        return listNode(query,current);
    }


    @Override
    public List<SimpleNodeDTO> listAllNode(Current current) throws CustomException {
        return listAllNodeForAccount(getCurrentAccount(),current);
    }

    @Override
    public List<SimpleNodeDTO> listAllNodeForAccount(AccountDTO account, Current current) throws CustomException {
        String userId = (account != null) ? account.getId() : null;
        List<SimpleNodeDTO> list = getStorageService().listAllNode(userId);
        for (SimpleNodeDTO node : list){
            boolean isReadOnly = getIsReadOnly(account,node,current);
            node.setIsReadOnly(isReadOnly);
        }
        return list;
    }

    @Override
    public List<SimpleNodeDTO> listNode(QueryNodeDTO query, Current current) throws CustomException {
        return listNodeForAccount(getCurrentAccount(),
                query,current);
    }

    @Override
    public List<SimpleNodeDTO> listNodeForAccount(AccountDTO account, QueryNodeDTO query, Current current) throws CustomException {
        if (StringUtils.isEmpty(query.getUserId())) {
            if (account != null) query.setUserId(account.getId());
        }
        List<SimpleNodeDTO> list = getStorageService().listNode(query);
        for (SimpleNodeDTO node : list){
            node.setIsReadOnly(getIsReadOnly(account,node,current));
        }
        return list;
    }

    @Override
    public FullNodeDTO getFullNode(SimpleNodeDTO node, Current current) throws CustomException {
        return getFullNodeForAccount(getCurrentAccount(),
                node,current);
    }

    @Override
    public FullNodeDTO getFullNodeForAccount(AccountDTO account, SimpleNodeDTO node, Current current) throws CustomException {
        return getStorageService().getFullNodeInfo(node);
    }

    @Override
    public FileNodeDTO getFile(SimpleNodeDTO node, boolean withHistory, Current current) throws CustomException {
        return getFileForAccount(getCurrentAccount(),
                node,withHistory,current);
    }

    @Override
    public FileNodeDTO getFileForAccount(AccountDTO account, SimpleNodeDTO node, boolean withHistory, Current current) throws CustomException {
        FileNodeDTO file = fileNodeMap.get(node.getId());
        if ((file == null) || (withHistory)){
            file = getStorageService().getFileNodeInfo(node,withHistory);
            fileNodeMap.put(node.getId(),file);
        }
        return file;
    }

    @Override
    public List<FileNodeDTO> listAllSubFile(SimpleNodeDTO parent, List<Short> typeIdList, boolean withHistory, Current current) throws CustomException {
        return listAllSubFileForAccount(getCurrentAccount(),
                parent,typeIdList,withHistory,current);
    }

    @Override
    public List<FileNodeDTO> listAllSubFileForAccount(AccountDTO account, SimpleNodeDTO parent, List<Short> typeIdList, boolean withHistory, Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setParentPath(parent.getPath());
        query.setTypeId(StringUtils.getStringIdList(typeIdList));

        return listFileForAccount(account,query,withHistory,current);
    }

    @Override
    public List<FileNodeDTO> listFile(@NotNull QueryNodeDTO query, boolean withHistory, Current current) throws CustomException {
        return listFileForAccount(getCurrentAccount(),
                query,withHistory,current);
    }

    @Override
    public List<FileNodeDTO> listFileForAccount(AccountDTO account, @NotNull QueryNodeDTO query, boolean withHistory, Current current) throws CustomException {
        if (StringUtils.isEmpty(query.getUserId())) {
            if (account != null) query.setUserId(account.getId());
        }
        List<FileNodeDTO> list = getStorageService().listFileNodeInfo(query,withHistory);
        for (FileNodeDTO file : list){
            SimpleNodeDTO node = file.getBasic();
            if (node != null) node.setIsReadOnly(getIsReadOnly(account,node,current));
        }
        return list;
    }

    @Override
    public int writeFileNode(@NotNull FileNodeDTO src, @NotNull FileDataDTO data, Current current) throws CustomException {
        return writeFileNodeForAccount(getCurrentAccount(),
                src,data,current);
    }

    @Override
    public int writeFileNodeForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull FileDataDTO data, Current current) throws CustomException {
        String userId = ((account != null) && !StringUtils.isEmpty(account.getId())) ?
                account.getId() : "null";
//        assert (src.getBasic() != null) && (!src.getBasic().getIsReadOnly());
        FileDTO file = getBasicFile(src,userId,current);
        assert (file != null);

        CoreFileDataDTO basicData = BeanUtils.createFrom(data,CoreFileDataDTO.class);
        basicData.setScope(file.getScope());
        basicData.setKey(file.getKey());
        return getCoreFileServer().coreWriteFile(basicData);
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
                    CoreFileDTO f = getCoreFileServer().coreCreateFile(src.getBasic().getPath());
                    CoreFileDTO basicRead = new CoreFileDTO();
                    basicRead.setScope(src.getReadFileScope());
                    basicRead.setKey(src.getReadFileKey());
                    CoreFileCopyResult copyResult = getCoreFileServer().coreCopyFile(basicRead,f);
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
        SimpleNodeDTO dst = getStorageService().updateNode(src,request);
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
    public int writeNode(@NotNull SimpleNodeDTO src, @NotNull FileDataDTO data, Current current) throws CustomException {
        return writeNodeForAccount(getCurrentAccount(),
                src,data,current);
    }

    @Override
    public int writeNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull FileDataDTO data, Current current) throws CustomException {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return writeFileNodeForAccount(account,file,data,current);
    }

    @Override
    public FileDataDTO readFileNode(@NotNull FileNodeDTO src, long pos, int size, Current current) throws CustomException {
        return readFileNodeForAccount(getCurrentAccount(),
                src,pos,size,current);
    }

    @Override
    public FileDataDTO readFileNodeForAccount(AccountDTO account, @NotNull FileNodeDTO src, long pos, int size, Current current) throws CustomException {
        String userId = ((account != null) && !StringUtils.isEmpty(account.getId())) ?
                account.getId() : "null";
        assert (src.getBasic() != null);
        FileDTO file = getBasicFile(src,userId,current);
        assert (file != null);

        CoreFileDTO basicFile = BeanUtils.createFrom(file,CoreFileDTO.class);
        CoreFileDataDTO basicData = getCoreFileServer().coreReadFile(basicFile,pos,size);
        return BeanUtils.createFrom(basicData,FileDataDTO.class);
    }

    @Override
    public FileDataDTO readNode(@NotNull SimpleNodeDTO src, long pos, int size, Current current) throws CustomException {
        return readNodeForAccount(getCurrentAccount(),
                src,pos,size,current);
    }

    @Override
    public FileDataDTO readNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, long pos, int size, Current current) throws CustomException {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return readFileNodeForAccount(account,file,pos,size,current);
    }

    @Override
    public SimpleNodeDTO moveNode(@NotNull SimpleNodeDTO src, @NotNull SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) throws CustomException {
        return moveNodeForAccount(getCurrentAccount(),
                src,dstParent,request,current);
    }

    @Override
    public SimpleNodeDTO moveNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) throws CustomException {
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
                    List<FileNodeDTO> fileList = getStorageService().listFileNodeInfo(query,false);
                    for (FileNodeDTO file : fileList){
                        assert (file.getBasic() != null);
                        request.setFullName(StringUtils.replace(file.getBasic().getPath(),
                                (srcPath + StringUtils.SPLIT_PATH),""));
                        try {
                            moveFileForAccount(account,file,dstParent,request,current);
                        } catch (CustomException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            return dst;
        } else {
            FileNodeDTO srcFile = getFileForAccount(account,src,false,current);
            return moveFileForAccount(account,srcFile,dstParent,request,current);
        }
    }

    public SimpleNodeDTO moveFileForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull SimpleNodeDTO dstParent, @NotNull MoveNodeRequestDTO request, Current current) throws CustomException {
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
//            String dstPath = StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + fullName;
//            if (dstParent != null) {
//                dstPath = StringUtils.formatPath(dstParent.getPath() + StringUtils.SPLIT_PATH + fullName);
//            }
//            String dstScope = StringUtils.getDirName(dstPath);
//            String dstKey = StringUtils.getFileName(dstPath);
//            FileDTO readDstFile = moveFile(readSrcFile, new FileDTO(dstScope, dstKey), current);
//            updateRequest.setReadFileScope(readDstFile.getScope());
//            updateRequest.setReadFileKey(readDstFile.getKey());
//            CoreFileDTO readDstRealFile = BeanUtils.createFrom(readDstFile,CoreFileDTO.class);
            CoreFileDTO coreReadSrcFile = BeanUtils.createFrom(readSrcFile,CoreFileDTO.class);
            long fileLength = getCoreFileServer().coreGetFileLength(coreReadSrcFile);
            updateRequest.setFileLength(fileLength);
        }
//        FileDTO writeSrcFile = new FileDTO(src.getWriteFileScope(),src.getWriteFileKey());
//        if (isExist(writeSrcFile,current)) {
//            String dstWritePath = StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + fullName;
//            if (dstParent != null) {
//                dstWritePath = StringUtils.formatPath(dstParent.getPath() + StringUtils.SPLIT_PATH + fullName);
//            }
//            String dstWriteScope = StringUtils.getDirName(dstWritePath);
//            String dstWriteKey = StringUtils.getFileName(dstWritePath);
//            FileDTO writeDstFile = moveFile(writeSrcFile, new FileDTO(dstWriteScope, dstWriteKey), current);
//            updateRequest.setWriteFileScope(writeDstFile.getScope());
//            updateRequest.setWriteFileKey(writeDstFile.getKey());
//        }

        //更改记录
        assert (src.getBasic() != null);
        return updateNode(src.getBasic(),updateRequest,current);
    }

    @Override
    public CommitListResultDTO checkNodeListRequest(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return checkNodeListRequestForAccount(getCurrentAccount(),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO checkNodeListRequestForAccount(AccountDTO account, @NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        return commitNodeListForAccount(account,srcList,request,current);
    }

    @Override
    public SimpleNodeDTO checkNodeRequest(@NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return checkNodeRequestForAccount(getCurrentAccount(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO checkNodeRequestForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        return commitNodeForAccount(account,src,request,current);
    }

    @Override
    public SimpleNodeDTO checkFileRequest(@NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return checkFileRequestForAccount(getCurrentAccount(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO checkFileRequestForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_CHECK);
        return commitFileForAccount(account,src,request,current);
    }

    @Override
    public CommitListResultDTO auditNodeListRequest(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return auditNodeListRequestForAccount(getCurrentAccount(),
                srcList,request,current);
    }

    @Override
    public CommitListResultDTO auditNodeListRequestForAccount(AccountDTO account, @NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_AUDIT);
        return commitNodeListForAccount(account,srcList,request,current);
    }

    @Override
    public SimpleNodeDTO auditNodeRequest(@NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return auditNodeRequestForAccount(getCurrentAccount(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO auditNodeRequestForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_AUDIT);
        return commitNodeForAccount(account,src,request,current);
    }

    @Override
    public SimpleNodeDTO auditFileRequest(@NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return auditFileRequestForAccount(getCurrentAccount(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO auditFileRequestForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        request.setActionTypeId(ConstService.STORAGE_ACTION_TYPE_AUDIT);
        return commitFileForAccount(account,src,request,current);
    }

    @Override
    public CommitListResultDTO commitNodeList(@NotNull List<SimpleNodeDTO> srcList, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return commitNodeListForAccount(getCurrentAccount(),
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
        return commitNodeForAccount(getCurrentAccount(),
                src,request,current);
    }

    @Override
    public SimpleNodeDTO commitNodeForAccount(AccountDTO account, @NotNull SimpleNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        FileNodeDTO file = getFileForAccount(account,src,false,current);
        return commitFileForAccount(account,file,request,current);
    }

    @Override
    public SimpleNodeDTO commitFile(@NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        return commitFileForAccount(getCurrentAccount(),src,request,current);
    }

    @Override
    public SimpleNodeDTO commitFileForAccount(AccountDTO account, @NotNull FileNodeDTO src, @NotNull CommitRequestDTO request, Current current) throws CustomException {
        //确定和调整文件提交动作
        Short actionTypeId = (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(request.getActionTypeId())) ?
                ConstService.STORAGE_ACTION_TYPE_COMMIT : request.getActionTypeId();
        String actionName = ConstService.getActionName(actionTypeId);

        //根据提交动作取出相关要提交的文件节点位置
        StringElementDTO stringElement = new StringElementDTO();
        updateStringElement(stringElement,src,request,actionName);
        FullNodeDTO fullNode = getStorageService().getFullNodeInfo(src.getBasic());
        stringElement.setIssuePath(fullNode.getIssuePath());

        Short nodeTypeId = ConstService.getActionNodeTypeId(actionTypeId);
        if (ConstService.STORAGE_NODE_TYPE_UNKNOWN.equals(nodeTypeId)) nodeTypeId = ConstService.STORAGE_NODE_TYPE_FILE_COMMIT_HIS;
        String nodePath = ConstService.getActionNodePath(actionTypeId,stringElement);
        if (StringUtils.isEmpty(nodePath)) nodePath = src.getBasic().getPath();
        if (!StringUtils.isSame(StringUtils.left(nodePath,1),StringUtils.SPLIT_PATH)){
            nodePath = StringUtils.formatPath(StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + nodePath);
        }
        if (StringUtils.isSame(nodePath,src.getBasic().getPath())){
            String fileName = StringUtils.getFileNameWithoutExt(nodePath) + StringUtils.SPLIT_NAME_PART +
                    actionName + StringUtils.getFileExt(nodePath);
            nodePath = StringUtils.formatPath(StringUtils.getDirName(nodePath) + StringUtils.SPLIT_PATH + fileName);
        }

        //根据提交动作取出要上传的文件服务器类型和地址
        Short serverTypeId = ConstService.getActionFileServerTypeId(actionTypeId);
        String serverAddress = ConstService.getActionFileServerAddress(actionTypeId,stringElement);

        //建立或更新版本节点
        SimpleNodeDTO targetNode = getNodeByPathForAccount(account,nodePath,current);
        CreateVersionRequestDTO versionRequest = BeanUtils.createFrom(request,CreateVersionRequestDTO.class);
        versionRequest.setActionTypeId(actionTypeId);
        versionRequest.setMainFileId(src.getMainFileId());
        if (StringUtils.isEmpty(versionRequest.getMainFileId())) versionRequest.setMainFileId(src.getBasic().getId());
        versionRequest.setServerTypeId(serverTypeId);
        versionRequest.setServerAddress(serverAddress);
        versionRequest.setPath(nodePath);
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
            noticeRequest.setStringElement(stringElement);
            getNoticeServicePrx().sendNoticeForAccountAsync(account,noticeRequest);
        }

        return targetNode;
    }

    private void updateCoreCreateFileRequest(CoreCreateFileRequest createRequest,FileNodeDTO src,CreateVersionRequestDTO versionRequest,String path,File mirror){
        SimpleNodeDTO node = src.getBasic();
        BeanUtils.copyCleanProperties(node,createRequest);
        BeanUtils.copyCleanProperties(src,createRequest);
        BeanUtils.copyCleanProperties(versionRequest,createRequest);
        if (StringUtils.isNotEmpty(versionRequest.getUserId())) createRequest.setOwnerUserId(versionRequest.getUserId());
        createRequest.setPath(path);
        createRequest.setSrcFile(mirror);
        createRequest.setAccountId(createRequest.getOwnerUserId());
    }

    public SimpleNodeDTO getNodeByFuzzyPathForAccount(AccountDTO account,String fuzzyPath,Current current) throws CustomException {
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath(fuzzyPath);
        List<SimpleNodeDTO> list = getStorageService().listNode(query);
        return ((list != null) && (!list.isEmpty())) ? list.get(0) : null;
    }
    private void updateCoreCreateFileRequestForParentInfo(@NotNull CoreCreateFileRequest request,@NotNull FileNodeDTO src){
        SimpleNodeDTO srcNode = src.getBasic();
        QueryNodeDTO query = new QueryNodeDTO();
//        assert (StringUtils.isNotEmpty(srcNode.getProjectId()));
//        StringBuilder idStringBuilder = new StringBuilder(srcNode.getProjectId());
//        if (StringUtils.isNotEmpty(srcNode.getCompanyId())) idStringBuilder.append(",").append(srcNode.getCompanyId());
//        if (StringUtils.isNotEmpty(srcNode.getIssueId())) idStringBuilder.append(",").append(srcNode.getIssueId());
//        if (StringUtils.isNotEmpty(srcNode.getTaskId())) idStringBuilder.append(",").append(srcNode.getTaskId());
//        query.setFuzzyIdString(idStringBuilder.toString());
        query.setTypeId(ConstService.STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE.toString());
        if (StringUtils.isNotEmpty(request.getPath())) query.setFuzzyPath(StringUtils.getDirName(request.getPath()));
        List<SimpleNodeDTO> list = null;
        try {
            list = getStorageService().listOldNode(BeanUtils.cleanProperties(query));
        } catch (CustomException e) {
            e.printStackTrace();
        }
        if ((list != null) && !(list.isEmpty())){
            SimpleNodeDTO parent = list.get(0);
            setParentInfo(request,parent);
        }
    }

    private boolean isCoreFileServer(Short serverTypeId, String serverAddress){
        boolean isCore = !ConstService.FILE_SERVER_TYPE_DISK.equals(serverTypeId);
        if (!isCore) isCore = StringUtils.isSame(serverAddress,setting.getServerAddress());
        return isCore;
    }

    @Deprecated
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
    public SimpleNodeDTO createVersion(FileNodeDTO src, CreateVersionRequestDTO request, Current current) throws CustomException {
        return createVersionForAccount(getCurrentAccount(),
                src,request,current);
    }

    private CoreFileDTO createCoreFile(@NotNull FileNodeDTO src,String path, Short serverTypeId, String serverAddress, Current current) throws CustomException {
        final String DEFAULT_MIRROR_BASE_DIR = "c:/work/file_server/mirror";

        //生成实际目标文件
        CoreFileServer srcFileServer = getCoreFileServer();
        CoreFileDTO srcCoreFile = getCoreFile(null,src,current);
        File srcFile = srcFileServer.coreGetLocalFile(srcCoreFile,DEFAULT_MIRROR_BASE_DIR);
        CoreCreateFileRequest coreCreateFileRequest = updateCoreCreateFileRequest(new CoreCreateFileRequest(),src,path,srcFile);
        CoreFileServer dstFileServer = getCoreFileServer(serverTypeId,serverAddress);
        if (isWebServer(dstFileServer)){
            String tmpPath = coreCreateFileRequest.getPath();
            updateCoreCreateFileRequestForParentInfo(coreCreateFileRequest,src);
        }
        CoreFileDTO targetFile = dstFileServer.coreCreateFile(coreCreateFileRequest);
        return targetFile;
    }

    @Override
    public SimpleNodeDTO createVersionForAccount(AccountDTO account, FileNodeDTO src, CreateVersionRequestDTO request, Current current) throws CustomException {

        releaseFileNodeForAccount(account,src,0,current);

        Short actionTypeId = (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(request.getActionTypeId())) ?
                ConstService.STORAGE_ACTION_TYPE_BACKUP : request.getActionTypeId();

        //生成实际目标文件
        String path = request.getPath();
        if (StringUtils.isNotEmpty(path)) {
            if (!StringUtils.isSame(StringUtils.left(path,1),StringUtils.SPLIT_PATH)) {
                //是相对路径
                path = StringUtils.formatPath(StringUtils.getDirName(src.getBasic().getPath()) + StringUtils.SPLIT_PATH + path);
            }
        }
        CoreFileDTO targetFile = createCoreFile(src,path,request.getServerTypeId(),request.getServerAddress(),current);

        //设定创建节点申请
        Short typeId = ConstService.getActionNodeTypeId(actionTypeId);
        if (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(typeId)) typeId = src.getBasic().getTypeId();

        //查找父节点
        QueryNodeDTO query = new QueryNodeDTO();
        query.setFuzzyPath(StringUtils.getDirName(path));
        if (account != null) query.setUserId(account.getId());
        List<SimpleNodeDTO> parentList = getStorageService().listNode(query);
        SimpleNodeDTO parent = null;
        if ((parentList != null) && (!parentList.isEmpty())){
            parent = parentList.get(0);
        }

        UpdateNodeDTO createRequest = BeanUtils.createFrom(request,UpdateNodeDTO.class);
        BeanUtils.copyProperties(src.getBasic(),createRequest);
        BeanUtils.copyProperties(src,createRequest);

        if ((parent != null) && (StringUtils.isNotEmpty(parent.getId()))) {
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
        if (targetFile != null) {
            createRequest.setReadFileScope(targetFile.getScope());
            createRequest.setReadFileKey(targetFile.getKey());
        } else {
            createRequest.setReadFileScope(null);
            createRequest.setReadFileKey(null);
        }
        createRequest.setWriteFileScope(null);
        createRequest.setWriteFileKey(null);


        return getStorageService().createNode(createRequest);
    }

    private CoreCreateFileRequest updateCoreCreateFileRequest(@NotNull CoreCreateFileRequest request,FileNodeDTO src,String path,File srcFile){
        if (src != null){
            BeanUtils.copyProperties(src.getBasic(),request);
            BeanUtils.copyProperties(src,request);
        }
        if (StringUtils.isNotEmpty(path)){
            request.setPath(path);
        }
        if (null != srcFile){
            request.setSrcFile(srcFile);
        }
        return request;
    }

    @Override
    public SimpleNodeDTO updateVersion(FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request, Current current) throws CustomException {
        return updateVersionForAccount(getCurrentAccount(),
                src,dst,request,current);
    }

    @Override
    public SimpleNodeDTO updateVersionForAccount(AccountDTO account, FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request, Current current) throws CustomException {
        //备份目标版本
        Short dActionTypeId = ConstService.STORAGE_ACTION_TYPE_BACKUP;
        String dActionName = ConstService.getActionName(dActionTypeId);
        String dFullName = dst.getBasic().getName();
        String dsPath = ConstService.getActionNodePath(dActionTypeId);
        if (!StringUtils.isEmpty(dsPath)) {
            FullNodeDTO dNode = getFullNodeForAccount(account, dst.getBasic(), current);
            StringElementDTO pathElement = BeanUtils.createCleanFrom(request, StringElementDTO.class);
            dFullName = convertPath(pathElement, dNode, dsPath, dActionName);
        }
        String dFullPath = dFullName;
        if (!StringUtils.isSame(StringUtils.left(dFullPath, 1), StringUtils.SPLIT_PATH)) {
            dFullPath = StringUtils.getDirName(dst.getBasic().getPath()) + StringUtils.SPLIT_PATH + dFullName;
        }
        if (StringUtils.isSame(dFullPath, dst.getBasic().getPath())) {
            dFullName = StringUtils.getFileNameWithoutExt(dFullName) + StringUtils.SPLIT_NAME_PART +
                    dActionName + StringUtils.getFileExt(dFullName);
            dFullPath = StringUtils.formatPath(StringUtils.getDirName(dFullPath) + StringUtils.SPLIT_PATH + dFullName);
        }

        //创建历史版本
        UpdateNodeDTO backupRequest = BeanUtils.createFrom(dst.getBasic(), UpdateNodeDTO.class);
        BeanUtils.copyProperties(dst,backupRequest);
        backupRequest.setActionTypeId(dActionTypeId);
        backupRequest.setFileVersion(dst.getFileVersion());
        backupRequest.setMainFileId(dst.getMainFileId());
        if (StringUtils.isEmpty(backupRequest.getMainFileId())) backupRequest.setMainFileId(dst.getBasic().getId());
        backupRequest.setRemark(dst.getFileRemark());
        backupRequest.setFullName(dFullPath);
        getStorageService().createNode(backupRequest);

        releaseFileNodeForAccount(account,src,0,current);

        Short actionTypeId = (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(request.getActionTypeId())) ?
                ConstService.STORAGE_ACTION_TYPE_BACKUP : request.getActionTypeId();


        CoreFileDTO targetFile = createCoreFile(src,dst.getBasic().getPath(),request.getServerTypeId(),request.getServerAddress(),current);

        Short typeId = ConstService.getActionNodeTypeId(actionTypeId);
        if (ConstService.STORAGE_ACTION_TYPE_UNKOWN.equals(typeId)) typeId = src.getBasic().getTypeId();

        //建立更新版本申请
        UpdateNodeDTO updateRequest = BeanUtils.createFrom(request,UpdateNodeDTO.class);
        BeanUtils.copyProperties(src.getBasic(),updateRequest);
        BeanUtils.copyProperties(src,updateRequest);
        updateRequest.setTypeId(typeId);
        updateRequest.setOwnerUserId(request.getUserId());
        updateRequest.setReadFileScope(targetFile.getScope());
        updateRequest.setReadFileKey(targetFile.getKey());
        updateRequest.setParentPath(null);
        updateRequest.setFullName(null);
        updateRequest.setWriteFileScope(null);
        updateRequest.setWriteFileKey(null);

        return updateNode(dst.getBasic(),updateRequest,current);
    }

    private CoreFileDTO getCoreFile(AccountDTO account, @NotNull FileNodeDTO src,Current current){
        CoreFileDTO coreFile = new CoreFileDTO();
        if (getIsReadOnly(account,src.getBasic(),current)){
            coreFile.setScope(src.getReadFileScope());
            coreFile.setKey(src.getReadFileKey());
        } else {
            coreFile.setScope(src.getWriteFileScope());
            if (StringUtils.isEmpty(coreFile.getScope())) coreFile.setScope(src.getReadFileScope());
            coreFile.setKey(src.getWriteFileKey());
            if (StringUtils.isEmpty(coreFile.getKey())) coreFile.setKey(src.getReadFileKey());
        }
        return coreFile;
    }
    private CoreFileDTO getCoreFile(AccountDTO account, @NotNull FileNodeDTO src){
        return getCoreFile(account,src,null);
    }

    @Deprecated
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
    public SimpleNodeDTO createDirectory(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        return createDirectoryForAccount(getCurrentAccount(),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createDirectoryForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        request.setIsDirectory(true);
        return createNodeForAccount(account,parent,request,current);
    }

    @Override
    public SimpleNodeDTO createFile(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        return createFileForAccount(getCurrentAccount(),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createFileForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        request.setIsDirectory(false);
        return createNodeForAccount(account,parent,request,current);
    }

    @Override
    public SimpleNodeDTO createNode(SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
        return createNodeForAccount(getCurrentAccount(),parent,request,current);
    }

    @Override
    public SimpleNodeDTO createNodeForAccount(AccountDTO account, SimpleNodeDTO parent, @NotNull CreateNodeRequestDTO request, Current current) throws CustomException {
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
            CoreFileDTO basicWrite = getCoreFileServer().coreCreateFile(path);
            assert (basicWrite != null);
            getCoreFileServer().coreSetFileLength(basicWrite,createRequest.getFileLength());
            createRequest.setWriteFileScope(basicWrite.getScope());
            createRequest.setWriteFileKey(basicWrite.getKey());
        }
        return getStorageService().createNode(createRequest);
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
    public FileDTO copyFile(FileDTO src, FileDTO dst, Current current){
        CoreFileDTO basicSrc = BeanUtils.createFrom(BeanUtils.cleanProperties(src),CoreFileDTO.class);
        CoreFileDTO basicDst = BeanUtils.createFrom(BeanUtils.cleanProperties(dst),CoreFileDTO.class);
        CoreFileDTO basicResult = getCoreFileServer().copyFile(basicSrc,basicDst);
        return BeanUtils.createFrom(basicResult, FileDTO.class);
    }

    public FileDTO moveFile(FileDTO src, FileDTO dst, Current current) throws CustomException {
        CoreFileDTO basicSrc = BeanUtils.createFrom(BeanUtils.cleanProperties(src),CoreFileDTO.class);
        CoreFileDTO basicDst = BeanUtils.createFrom(BeanUtils.cleanProperties(dst),CoreFileDTO.class);
        CoreFileDTO basicResult = getCoreFileServer().coreMoveFile(basicSrc,basicDst);
        return BeanUtils.createFrom(basicResult, FileDTO.class);
    }

    @Override
    public void setFileServerType(int type, Current current){
        setting.setServerTypeId((short)type);
    }

    @Override
    public int getFileServerType(Current current){
        return setting.getServerTypeId();
    }

    @Override
    public boolean isExist(FileDTO src, Current current){
        CoreFileDTO fileDTO = BeanUtils.createFrom(src, CoreFileDTO.class);
        return getCoreFileServer().coreIsExist(fileDTO);
    }


}
