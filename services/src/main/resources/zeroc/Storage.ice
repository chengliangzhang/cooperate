#pragma once
#include <Common.ice>
#include <FileServer.ice>
#include <User.ice>
#include <StorageData.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    interface StorageService {
        ["deprecate:尚未实现"] SimpleNodeDTO createMirror(FileNodeDTO src) throws CustomException; //建立镜像

        SimpleNodeList listOldNode(QueryNodeDTO query); //查询sky_driver中的节点信息

        SimpleNodeDTO createNodeWithParent(SimpleNodeDTO parent,UpdateNodeDTO request) throws CustomException; //创建节点
        SimpleNodeDTO updateNodeWithParent(SimpleNodeDTO src,SimpleNodeDTO parent,UpdateNodeDTO request) throws CustomException; //更改节点属性
        bool deleteNodeById(string id); //删除树节点

        SimpleNodeList listAllNode(string userId); //获取指定账号所有节点
        SimpleNodeList listNode(QueryNodeDTO query); //查询节点信息
        FileNodeList listFileNodeInfo(QueryNodeDTO query,bool withHistory); //查询文件信息

        FullNodeDTO getFullNodeInfo(SimpleNodeDTO node); //通过节点查询节点额外信息
        FileNodeDTO getFileNodeInfo(SimpleNodeDTO node,bool withHistory); //通过节点查询文件信息

        ["deprecate"] bool isDirectoryEmpty(string path); //根据路径判断目录是否为空
        ["deprecate"] bool isDirectoryEmptyForAccount(AccountDTO account,string path); //根据路径判断目录是否为空
        ["deprecate:由createNodeWithParent代替"] SimpleNodeDTO createNode(UpdateNodeDTO request); //创建节点
        ["deprecate:由updateNodeWithParent代替"] SimpleNodeDTO updateNode(SimpleNodeDTO src,UpdateNodeDTO request); //更改节点属性
    };
};