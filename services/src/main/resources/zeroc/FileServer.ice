#pragma once
#include <Common.ice>
#include <FileServerData.ice>
#include <StorageData.ice>

#include <Notice.ice>
#include <User.ice>

[["java:package:com.maoding.FileServer"]]
module zeroc {
    interface FileService {
        UserService* getUserService(); //获取UserService代理
        NoticeService* getNoticeService(); //获取NoticeService代理

        bool login(LoginDTO loginInfo); //登录
        StringList setNoticeClient(string userId,NoticeClient* client); //登录

        ["deprecate:尚未验证"] ProjectDTO getProjectInfoByPath(string path);
        ["deprecate:尚未验证"] ProjectDTO getProjectInfoByPathForAccount(AccountDTO account,string path);

        IdNameList listMajor(); //列出可用专业
        IdNameList listMajorForAccount(AccountDTO account); //列出可用专业
        IdNameList listAction(); //列出可用操作
        IdNameList listActionForAccount(AccountDTO account); //列出可用操作
        ProjectRoleList listProjectRoleByProjectId(string projectId); //获取项目的参与角色列表
        ProjectRoleList listProjectRoleByProjectIdForAccount(AccountDTO account,string projectId); //获取项目的参与角色列表

        CommitListResultDTO checkNodeListRequest(SimpleNodeList srcList,CommitRequestDTO request); //提交文件
        CommitListResultDTO checkNodeListRequestForAccount(AccountDTO account,SimpleNodeList srcList,CommitRequestDTO request); //提交文件
        SimpleNodeDTO checkNodeRequest(SimpleNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO checkNodeRequestForAccount(AccountDTO account,SimpleNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO checkFileRequest(FileNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO checkFileRequestForAccount(AccountDTO account,FileNodeDTO src,CommitRequestDTO request); //提交文件

        CommitListResultDTO auditNodeListRequest(SimpleNodeList srcList,CommitRequestDTO request); //提交文件
        CommitListResultDTO auditNodeListRequestForAccount(AccountDTO account,SimpleNodeList srcList,CommitRequestDTO request); //提交文件
        SimpleNodeDTO auditNodeRequest(SimpleNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO auditNodeRequestForAccount(AccountDTO account,SimpleNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO auditFileRequest(FileNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO auditFileRequestForAccount(AccountDTO account,FileNodeDTO src,CommitRequestDTO request); //提交文件

        CommitListResultDTO commitNodeList(SimpleNodeList srcList,CommitRequestDTO request); //提交文件
        CommitListResultDTO commitNodeListForAccount(AccountDTO account,SimpleNodeList srcList,CommitRequestDTO request); //提交文件
        SimpleNodeDTO commitNode(SimpleNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO commitNodeForAccount(AccountDTO account,SimpleNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO commitFile(FileNodeDTO src,CommitRequestDTO request); //提交文件
        SimpleNodeDTO commitFileForAccount(AccountDTO account,FileNodeDTO src,CommitRequestDTO request); //提交文件

        SimpleNodeDTO createVersion(FileNodeDTO src, CreateVersionRequestDTO request); //创建版本
        SimpleNodeDTO createVersionForAccount(AccountDTO account,FileNodeDTO src, CreateVersionRequestDTO request); //创建版本
        SimpleNodeDTO updateVersion(FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request); //创建版本
        SimpleNodeDTO updateVersionForAccount(AccountDTO account,FileNodeDTO src, FileNodeDTO dst, CreateVersionRequestDTO request); //创建版本

        bool deleteNode(SimpleNodeDTO src); //删除节点
        bool deleteNodeForAccount(AccountDTO account,SimpleNodeDTO src); //删除节点
        bool setNodeLength(SimpleNodeDTO src,long fileLength); //设置文件长度
        bool setNodeLengthForAccount(AccountDTO account,SimpleNodeDTO src,long fileLength); //设置文件长度
        bool setFileNodeLength(FileNodeDTO src,long fileLength); //设置文件长度
        bool setFileNodeLengthForAccount(AccountDTO account,FileNodeDTO src,long fileLength); //设置文件长度
        bool releaseNode(SimpleNodeDTO src,long fileLength); //用可写版本覆盖只读版本
        bool releaseNodeForAccount(AccountDTO account,SimpleNodeDTO  src,long fileLength); //用可写版本覆盖只读版本
        bool releaseFileNode(FileNodeDTO src,long fileLength); //用可写版本覆盖只读版本
        bool releaseFileNodeForAccount(AccountDTO account,FileNodeDTO src,long fileLength); //用可写版本覆盖只读版本
        bool reloadNode(SimpleNodeDTO src); //用只读版本覆盖可写版本
        bool reloadNodeForAccount(AccountDTO account,SimpleNodeDTO  src); //用只读版本覆盖可写版本
        bool reloadFileNode(FileNodeDTO src); //用只读版本覆盖可写版本
        bool reloadFileNodeForAccount(AccountDTO account,FileNodeDTO src); //用只读版本覆盖可写版本
        int writeFileNode(FileNodeDTO src,FileDataDTO data); //写入文件
        int writeFileNodeForAccount(AccountDTO account,FileNodeDTO src,FileDataDTO data); //写入文件
        int writeNode(SimpleNodeDTO src,FileDataDTO data); //写入文件节点
        int writeNodeForAccount(AccountDTO account,SimpleNodeDTO src,FileDataDTO data); //写入文件节点
        FileDataDTO readFileNode(FileNodeDTO src,long pos,int size); //读出文件
        FileDataDTO readFileNodeForAccount(AccountDTO account,FileNodeDTO src,long pos,int size); //读出文件
        FileDataDTO readNode(SimpleNodeDTO src,long pos,int size); //读出文件节点
        FileDataDTO readNodeForAccount(AccountDTO account,SimpleNodeDTO src,long pos,int size); //读出文件节点
        SimpleNodeDTO moveNode(SimpleNodeDTO src,SimpleNodeDTO dstParent,MoveNodeRequestDTO request); //移动节点
        SimpleNodeDTO moveNodeForAccount(AccountDTO account,SimpleNodeDTO src,SimpleNodeDTO dstParent,MoveNodeRequestDTO request); //移动节点

        SimpleNodeDTO createDirectory(SimpleNodeDTO parent,CreateNodeRequestDTO request); //创建目录,返回节点信息
        SimpleNodeDTO createDirectoryForAccount(AccountDTO account,SimpleNodeDTO parent,CreateNodeRequestDTO request); //创建目录,返回节点信息
        SimpleNodeDTO createFile(SimpleNodeDTO parent,CreateNodeRequestDTO request); //创建目录,返回节点信息
        SimpleNodeDTO createFileForAccount(AccountDTO account,SimpleNodeDTO parent,CreateNodeRequestDTO request); //创建目录,返回节点信息
        SimpleNodeDTO createNode(SimpleNodeDTO parent,CreateNodeRequestDTO request); //创建树节点,返回节点信息
        SimpleNodeDTO createNodeForAccount(AccountDTO account,SimpleNodeDTO parent,CreateNodeRequestDTO request); //创建树节点,返回节点信息

        SimpleNodeDTO getNodeById(string id); //查询节点信息
        SimpleNodeDTO getNodeByIdForAccount(AccountDTO account,string id); //查询节点信息
        SimpleNodeDTO getNodeByPath(string path); //查询节点信息
        SimpleNodeDTO getNodeByPathForAccount(AccountDTO account,string path); //查询节点信息
        SimpleNodeList listSubNodeById(string parentId); //查询节点信息
        SimpleNodeList listSubNodeByIdForAccount(AccountDTO account,string parentId); //查询节点信息
        SimpleNodeList listSubNodeByPath(string parentPath); //查询节点信息
        SimpleNodeList listSubNodeByPathForAccount(AccountDTO account,string parentPath); //查询节点信息
        SimpleNodeList listFilterSubNodeById(string parentId,ShortList typeIdList); //查询节点信息
        SimpleNodeList listFilterSubNodeByIdForAccount(AccountDTO account,string parentId,ShortList typeIdList); //查询节点信息
        SimpleNodeList listFilterSubNodeByPath(string parentPath, ShortList typeIdList); //查询节点信息
        SimpleNodeList listFilterSubNodeByPathForAccount(AccountDTO account,string parentPath,ShortList typeIdList); //查询节点信息
        SimpleNodeList listAllNode(); //查询所有节点信息
        SimpleNodeList listAllNodeForAccount(AccountDTO account); //查询所有节点信息
        SimpleNodeList listNode(QueryNodeDTO query); //查询节点信息
        SimpleNodeList listNodeForAccount(AccountDTO account,QueryNodeDTO query); //查询节点信息

        FileNodeDTO getFile(SimpleNodeDTO node,bool withHistory); //通过节点查询文件信息
        FileNodeDTO getFileForAccount(AccountDTO account,SimpleNodeDTO node,bool withHistory); //通过节点查询文件信息
        FileNodeList listAllSubFile(SimpleNodeDTO parent,ShortList typeIdList,bool withHistory); //查询文件信息
        FileNodeList listAllSubFileForAccount(AccountDTO account,SimpleNodeDTO parent,ShortList typeIdList,bool withHistory); //查询文件信息
        FileNodeList listFile(QueryNodeDTO query,bool withHistory); //查询文件信息
        FileNodeList listFileForAccount(AccountDTO account,QueryNodeDTO query,bool withHistory); //查询文件信息

        FullNodeDTO getFullNode(SimpleNodeDTO node); //通过节点查询文件完整信息
        FullNodeDTO getFullNodeForAccount(AccountDTO account,SimpleNodeDTO node); //通过节点查询文件完整信息

        ["deprecate"] void setFileServerType(int type);
        ["deprecate"] int getFileServerType();
        ["deprecate"] bool isExist(FileDTO src);
        ["deprecate"] FileDTO copyFile(FileDTO src,FileDTO dst);
    };
};