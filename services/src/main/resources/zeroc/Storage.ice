#pragma once
#include <Common.ice>
#include <FileServer.ice>
#include <User.ice>
#include <StorageData.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    interface StorageService {
        EmbedElementDTO createEmbedElement(UpdateElementDTO request) throws CustomException; //创建内嵌HTML元素
        AnnotateDTO createAnnotate(UpdateAnnotateDTO request) throws CustomException; //创建注解
        NodeFileDTO createNodeFile(UpdateNodeFileDTO request) throws CustomException; //创建文件或镜像
        SuggestionDTO createSuggestion(UpdateSuggestionDTO request) throws CustomException; //提交校审意见
        NodeFileDTO updateNodeFile(NodeFileDTO src,UpdateNodeFileDTO request) throws CustomException; //更新文件属性

        long summaryNodeLength(QuerySummaryDTO query) throws CustomException; //查询总使用空间

        void deleteNodeById(AccountDTO account,string id) throws CustomException; //删除树节点
        void deleteNodeByIdList(AccountDTO account,StringList idList) throws CustomException; //删除树节点
        void deleteNode(AccountDTO account,SimpleNodeDTO node) throws CustomException; //删除树节点
        void deleteNodeList(AccountDTO account,SimpleNodeList node) throws CustomException; //删除树节点

        SimpleNodeDTO createNode(SimpleNodeDTO parent,UpdateNodeDTO request) throws CustomException; //创建节点
        SimpleNodeDTO createNodeWithRequestOnly(UpdateNodeDTO request) throws CustomException; //创建节点
        SimpleNodeDTO updateNode(SimpleNodeDTO src,UpdateNodeDTO request) throws CustomException; //更改节点属性
        ["deprecate:使用createNodeFile代替"] FullNodeDTO createMirror(FullNodeDTO src,UpdateNodeDTO request) throws CustomException; //建立镜像

        SimpleNodeList listNode(QueryNodeDTO query) throws CustomException; //查询节点列表
        SimpleNodeList listChild(SimpleNodeDTO parent) throws CustomException; //查询直接子节点
        SimpleNodeList listChildren(SimpleNodeDTO parent) throws CustomException; //查询所有子节点
        SimpleNodeList listRoot(string accountId) throws CustomException; //查询根节点
        SimpleNodeDTO getNodeById(String id) throws CustomException; //获取单个节点
        SimpleNodeDTO getNodeByPath(String path) throws CustomException; //获取单个节点
        SimpleNodeDTO getNodeByFuzzyPath(String fuzzyPath) throws CustomException; //获取单个节点
        SimpleNodeList listOldNode(QueryNodeDTO query) throws CustomException; //查询sky_driver中的节点信息

        FullNodeDTO getNodeInfo(SimpleNodeDTO node,QueryNodeInfoDTO request) throws CustomException; //查询节点详细信息

        ["deprecate:使用updateNode代替"] SimpleNodeDTO updateNodeWithParent(SimpleNodeDTO src,SimpleNodeDTO parent,UpdateNodeDTO request) throws CustomException; //更改节点属性
        ["deprecate:使用createNode代替"] SimpleNodeDTO createNodeWithParent(SimpleNodeDTO parent,UpdateNodeDTO request) throws CustomException; //创建节点

        ["deprecate:使用listNode代替"] FileNodeList listFileNodeInfo(QueryNodeDTO query,bool withHistory); //查询文件信息
        ["deprecate:使用getNodeInfo代替"] FullNodeDTO getFullNodeInfo(SimpleNodeDTO node); //通过节点查询节点额外信息
        ["deprecate:使用getNodeInfo代替"] FileNodeDTO getFileNodeInfo(SimpleNodeDTO node,bool withHistory); //通过节点查询文件信息
    };
};