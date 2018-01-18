#pragma once
#include <Common.ice>
#include <FileServer.ice>
#include <User.ice>
#include <StorageData.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    interface StorageService {
        SimpleNodeDTO createNode(UpdateNodeDTO request); //创建节点
        SimpleNodeDTO updateNode(SimpleNodeDTO src,UpdateNodeDTO request); //更改节点属性
        bool deleteNodeById(string id); //删除树节点

        SimpleNodeList listAllNode(string userId); //获取指定账号所有节点
        SimpleNodeList listNode(QueryNodeDTO query); //查询节点信息
        FileNodeList listFileNodeInfo(QueryNodeDTO query,bool withHistory); //查询文件信息

        FullNodeDTO getFullNodeInfo(SimpleNodeDTO node); //通过节点查询节点额外信息
        FileNodeDTO getFileNodeInfo(SimpleNodeDTO node,bool withHistory); //通过节点查询文件信息

        ["deprecate"] bool isDirectoryEmpty(string path); //根据路径判断目录是否为空
        ["deprecate"] bool isDirectoryEmptyForAccount(AccountDTO account,string path); //根据路径判断目录是否为空
    };
};