#pragma once
#include <Common.ice>
#include <FileServerData.ice>
#include <StorageData.ice>

#include <Notice.ice>
#include <User.ice>

[["java:package:com.maoding.FileServer"]]
module zeroc {
    interface FileService {
        SimpleNodeList listWebArchiveDir(string projectId) throws CustomException; // 获取网站空间的归档目录树
        SimpleNodeList listWebArchiveDirForAccount(AccountDTO account,string projectId) throws CustomException; // 获取网站空间的归档目录树

        ["deprecate:尚未实现"] bool createMirror(FileNodeDTO src) throws CustomException; //在本地建立节点镜像文件
        ["deprecate:尚未实现"] bool createMirrorForAccount(AccountDTO account,FileNodeDTO src) throws CustomException; //在本地建立节点镜像文件

        SimpleNodeDTO changeNodeOwner(SimpleNodeDTO src,UserDTO dstOwner) throws CustomException; //更改文件所有者
        SimpleNodeDTO changeNodeOwnerForAccount(AccountDTO account,SimpleNodeDTO src,UserDTO dstOwner) throws CustomException; //更改文件所有者

        ["deprecate:尚未实现"] ProjectDTO getProjectInfoByPath(string path) throws CustomException;
        ["deprecate:尚未实现"] ProjectDTO getProjectInfoByPathForAccount(AccountDTO account,string path) throws CustomException;

        ["deprecate"] UserService* getUserService() throws CustomException; //获取UserService代理
        ["deprecate"] NoticeService* getNoticeService() throws CustomException; //获取NoticeService代理

        bool login(LoginDTO loginInfo) throws CustomException; //登录
        StringList setNoticeClient(string userId,NoticeClient* client) throws CustomException; //登录

        IdNameList listMajor() throws CustomException; //列出可用专业
        IdNameList listMajorForAccount(AccountDTO account) throws CustomException; //列出可用专业
        IdNameList listAction() throws CustomException; //列出可用操作
        IdNameList listActionForAccount(AccountDTO account) throws CustomException; //列出可用操作
        ProjectRoleList listProjectRoleByProjectId(string projectId) throws CustomException; //获取项目的参与角色列表
        ProjectRoleList listProjectRoleByProjectIdForAccount(AccountDTO account,string projectId) throws CustomException; //获取项目的参与角色列表

        CommitListResultDTO issueNodeList(SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件到web
        CommitListResultDTO issueNodeListForAccount(AccountDTO account,SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件到web
        SimpleNodeDTO issueNode(SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件到web
        SimpleNodeDTO issueNodeForAccount(AccountDTO account,SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件到web
        SimpleNodeDTO issueFile(FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件到web
        SimpleNodeDTO issueFileForAccount(AccountDTO account,FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件到web

        CommitListResultDTO checkNodeListRequest(SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件
        CommitListResultDTO checkNodeListRequestForAccount(AccountDTO account,SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO checkNodeRequest(SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO checkNodeRequestForAccount(AccountDTO account,SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO checkFileRequest(FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO checkFileRequestForAccount(AccountDTO account,FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件

        CommitListResultDTO auditNodeListRequest(SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件
        CommitListResultDTO auditNodeListRequestForAccount(AccountDTO account,SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO auditNodeRequest(SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO auditNodeRequestForAccount(AccountDTO account,SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO auditFileRequest(FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO auditFileRequestForAccount(AccountDTO account,FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件

        CommitListResultDTO commitNodeList(SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件
        CommitListResultDTO commitNodeListForAccount(AccountDTO account,SimpleNodeList srcList,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO commitNode(SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO commitNodeForAccount(AccountDTO account,SimpleNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO commitFile(FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件
        SimpleNodeDTO commitFileForAccount(AccountDTO account,FileNodeDTO src,CommitRequestDTO request) throws CustomException; //提交文件

        SimpleNodeDTO createVersion(FileNodeDTO src, CreateVersionRequestDTO request) throws CustomException; //创建版本
        SimpleNodeDTO createVersionForAccount(AccountDTO account,FileNodeDTO src, CreateVersionRequestDTO request) throws CustomException; //创建版本
        SimpleNodeDTO updateVersion(FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request) throws CustomException; //创建版本
        SimpleNodeDTO updateVersionForAccount(AccountDTO account,FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request) throws CustomException; //创建版本

        bool deleteNode(SimpleNodeDTO src) throws CustomException; //删除节点
        bool deleteNodeForAccount(AccountDTO account,SimpleNodeDTO src) throws CustomException; //删除节点

        bool setNodeLength(SimpleNodeDTO src,long fileLength) throws CustomException; //设置文件长度
        bool setNodeLengthForAccount(AccountDTO account,SimpleNodeDTO src,long fileLength) throws CustomException; //设置文件长度
        bool setFileNodeLength(FileNodeDTO src,long fileLength) throws CustomException; //设置文件长度
        bool setFileNodeLengthForAccount(AccountDTO account,FileNodeDTO src,long fileLength) throws CustomException; //设置文件长度

        bool releaseNode(SimpleNodeDTO src,long fileLength) throws CustomException; //用可写版本覆盖只读版本
        bool releaseNodeForAccount(AccountDTO account,SimpleNodeDTO  src,long fileLength) throws CustomException; //用可写版本覆盖只读版本
        bool releaseFileNode(FileNodeDTO src,long fileLength) throws CustomException; //用可写版本覆盖只读版本
        bool releaseFileNodeForAccount(AccountDTO account,FileNodeDTO src,long fileLength) throws CustomException; //用可写版本覆盖只读版本

        bool reloadNode(SimpleNodeDTO src) throws CustomException; //用只读版本覆盖可写版本
        bool reloadNodeForAccount(AccountDTO account,SimpleNodeDTO  src) throws CustomException; //用只读版本覆盖可写版本
        bool reloadFileNode(FileNodeDTO src) throws CustomException; //用只读版本覆盖可写版本
        bool reloadFileNodeForAccount(AccountDTO account,FileNodeDTO src) throws CustomException; //用只读版本覆盖可写版本

        int writeFileNode(FileNodeDTO src,FileDataDTO data) throws CustomException; //写入文件
        int writeFileNodeForAccount(AccountDTO account,FileNodeDTO src,FileDataDTO data) throws CustomException; //写入文件
        int writeNode(SimpleNodeDTO src,FileDataDTO data) throws CustomException; //写入文件节点
        int writeNodeForAccount(AccountDTO account,SimpleNodeDTO src,FileDataDTO data) throws CustomException; //写入文件节点

        FileDataDTO readFileNode(FileNodeDTO src,long pos,int size) throws CustomException; //读出文件
        FileDataDTO readFileNodeForAccount(AccountDTO account,FileNodeDTO src,long pos,int size) throws CustomException; //读出文件
        FileDataDTO readNode(SimpleNodeDTO src,long pos,int size) throws CustomException; //读出文件节点
        FileDataDTO readNodeForAccount(AccountDTO account,SimpleNodeDTO src,long pos,int size) throws CustomException; //读出文件节点

        SimpleNodeDTO moveNode(SimpleNodeDTO src,SimpleNodeDTO dstParent,MoveNodeRequestDTO request) throws CustomException; //移动节点
        SimpleNodeDTO moveNodeForAccount(AccountDTO account,SimpleNodeDTO src,SimpleNodeDTO dstParent,MoveNodeRequestDTO request) throws CustomException; //移动节点

        SimpleNodeDTO createDirectory(SimpleNodeDTO parent,CreateNodeRequestDTO request) throws CustomException; //创建目录,返回节点信息
        SimpleNodeDTO createDirectoryForAccount(AccountDTO account,SimpleNodeDTO parent,CreateNodeRequestDTO request) throws CustomException; //创建目录,返回节点信息
        SimpleNodeDTO createFile(SimpleNodeDTO parent,CreateNodeRequestDTO request) throws CustomException; //创建目录,返回节点信息
        SimpleNodeDTO createFileForAccount(AccountDTO account,SimpleNodeDTO parent,CreateNodeRequestDTO request) throws CustomException; //创建目录,返回节点信息
        SimpleNodeDTO createNode(SimpleNodeDTO parent,CreateNodeRequestDTO request) throws CustomException; //创建树节点,返回节点信息
        SimpleNodeDTO createNodeForAccount(AccountDTO account,SimpleNodeDTO parent,CreateNodeRequestDTO request) throws CustomException; //创建树节点,返回节点信息

        SimpleNodeDTO getNodeById(string id) throws CustomException; //查询节点信息
        SimpleNodeDTO getNodeByIdForAccount(AccountDTO account,string id) throws CustomException; //查询节点信息
        SimpleNodeDTO getNodeByPath(string path) throws CustomException; //查询节点信息
        SimpleNodeDTO getNodeByPathForAccount(AccountDTO account,string path) throws CustomException; //查询节点信息
        SimpleNodeDTO getNodeByFuzzyPath(string fuzzyPath) throws CustomException; //查询节点信息
        SimpleNodeDTO getNodeByFuzzyPathForAccount(AccountDTO account,string fuzzyPath) throws CustomException; //查询节点信息

        SimpleNodeList listSubNodeById(string parentId) throws CustomException; //查询节点信息
        SimpleNodeList listSubNodeByIdForAccount(AccountDTO account,string parentId) throws CustomException; //查询节点信息
        SimpleNodeList listSubNodeByPath(string parentPath) throws CustomException; //查询节点信息
        SimpleNodeList listSubNodeByPathForAccount(AccountDTO account,string parentPath) throws CustomException; //查询节点信息

        SimpleNodeList listFilterSubNodeById(string parentId,ShortList typeIdList) throws CustomException; //查询节点信息
        SimpleNodeList listFilterSubNodeByIdForAccount(AccountDTO account,string parentId,ShortList typeIdList) throws CustomException; //查询节点信息
        SimpleNodeList listFilterSubNodeByPath(string parentPath, ShortList typeIdList) throws CustomException; //查询节点信息
        SimpleNodeList listFilterSubNodeByPathForAccount(AccountDTO account,string parentPath,ShortList typeIdList) throws CustomException; //查询节点信息

        SimpleNodeList listAllNode() throws CustomException; //查询所有节点信息
        SimpleNodeList listAllNodeForAccount(AccountDTO account) throws CustomException; //查询所有节点信息

        SimpleNodeList listNode(QueryNodeDTO query) throws CustomException; //查询节点信息
        SimpleNodeList listNodeForAccount(AccountDTO account,QueryNodeDTO query) throws CustomException; //查询节点信息

        FileNodeDTO getFile(SimpleNodeDTO node,bool withHistory) throws CustomException; //通过节点查询文件信息
        FileNodeDTO getFileForAccount(AccountDTO account,SimpleNodeDTO node,bool withHistory) throws CustomException; //通过节点查询文件信息

        FileNodeList listAllSubFile(SimpleNodeDTO parent,ShortList typeIdList,bool withHistory) throws CustomException; //查询文件信息
        FileNodeList listAllSubFileForAccount(AccountDTO account,SimpleNodeDTO parent,ShortList typeIdList,bool withHistory) throws CustomException; //查询文件信息
        FileNodeList listFile(QueryNodeDTO query,bool withHistory) throws CustomException; //查询文件信息
        FileNodeList listFileForAccount(AccountDTO account,QueryNodeDTO query,bool withHistory) throws CustomException; //查询文件信息

        FullNodeDTO getFullNode(SimpleNodeDTO node) throws CustomException; //通过节点查询文件完整信息
        FullNodeDTO getFullNodeForAccount(AccountDTO account,SimpleNodeDTO node) throws CustomException; //通过节点查询文件完整信息

        ["deprecate"] void setFileServerType(int type);
        ["deprecate"] int getFileServerType();
        ["deprecate"] bool isExist(FileDTO src);
        ["deprecate"] FileDTO copyFile(FileDTO src,FileDTO dst);
    };
};