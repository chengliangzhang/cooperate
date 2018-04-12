#pragma once
#include <Common.ice>
#include <FileServer.ice>
#include <User.ice>
#include <StorageData.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    interface StorageService {
        EmbedElementDTO createEmbedElement(UpdateElementDTO request) throws CustomException; //创建内嵌HTML元素
        EmbedElementDTO updateEmbedElement(EmbedElementDTO src,UpdateElementDTO request) throws CustomException; //创建内嵌HTML元素
        AnnotateDTO createAnnotate(SuggestionDTO src,UpdateAnnotateDTO request) throws CustomException; //创建注解
        AnnotateDTO createAnnotateWithRequestOnly(UpdateAnnotateDTO request) throws CustomException; //创建注解
        AnnotateDTO updateAnnotate(AnnotateDTO src,UpdateAnnotateDTO request) throws CustomException; //创建注解
        NodeFileDTO createNodeFile(NodeFileDTO src,UpdateNodeFileDTO request) throws CustomException; //创建文件或镜像
        NodeFileDTO createNodeFileWithRequestOnly(UpdateNodeFileDTO request) throws CustomException; //创建文件或镜像
        NodeFileDTO updateNodeFile(NodeFileDTO src,UpdateNodeFileDTO request) throws CustomException; //更新文件或镜像
        SuggestionDTO createSuggestion(SimpleNodeDTO src,UpdateSuggestionDTO request) throws CustomException; //提交校审意见
        SuggestionDTO createSuggestionWithRequestOnly(UpdateSuggestionDTO request) throws CustomException; //提交校审意见
        SuggestionDTO updateSuggestion(SuggestionDTO src,UpdateSuggestionDTO request) throws CustomException; //更新校审意见
        NodeFileList listNodeFile(QueryNodeFileDTO query) throws CustomException; //查询文件或镜像
        SuggestionList listSuggestion(QuerySuggestionDTO query) throws CustomException; //查询校审意见

        long summaryNodeLength(QuerySummaryDTO query) throws CustomException; //查询总使用空间

        void deleteNodeById(AccountDTO account,string id) throws CustomException; //删除树节点
        void deleteNodeByIdList(AccountDTO account,StringList idList) throws CustomException; //删除树节点
        void deleteNode(AccountDTO account,SimpleNodeDTO node) throws CustomException; //删除树节点
        void deleteNodeList(AccountDTO account,SimpleNodeList node) throws CustomException; //删除树节点

        SimpleNodeDTO createNode(SimpleNodeDTO parent,UpdateNodeDTO request) throws CustomException; //创建节点
        SimpleNodeDTO createNodeWithRequestOnly(UpdateNodeDTO request) throws CustomException; //创建节点
        SimpleNodeDTO updateNodeSimple(SimpleNodeDTO src,UpdateNodeDTO request) throws CustomException; //更改节点属性

        SimpleNodeList listNode(QueryNodeDTO query) throws CustomException; //查询节点列表
        SimpleNodeList listChild(SimpleNodeDTO parent) throws CustomException; //查询直接子节点
        SimpleNodeList listChildren(SimpleNodeDTO parent) throws CustomException; //查询所有子节点
        SimpleNodeList listRoot(string accountId) throws CustomException; //查询根节点
        SimpleNodeDTO getNodeById(String id) throws CustomException; //获取单个节点
        SimpleNodeDTO getNodeByPath(String path) throws CustomException; //获取单个节点
        SimpleNodeDTO getNodeByFuzzyPath(String fuzzyPath) throws CustomException; //获取单个节点
        SimpleNodeList listOldNode(QueryNodeDTO query) throws CustomException; //查询sky_driver中的节点信息

        FullNodeDTO getNodeInfo(SimpleNodeDTO node,QueryNodeInfoDTO request) throws CustomException; //查询节点详细信息

        SimpleNodeDTO updateNode(SimpleNodeDTO src,SimpleNodeDTO parent,UpdateNodeDTO request) throws CustomException; //更改节点属性
    };
};