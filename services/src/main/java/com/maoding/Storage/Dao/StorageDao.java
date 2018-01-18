package com.maoding.Storage.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Storage.Dto.StorageEntityUnionDTO;
import com.maoding.Storage.Entity.StorageEntity;
import com.maoding.Storage.zeroc.FileNodeDTO;
import com.maoding.Storage.zeroc.FullNodeDTO;
import com.maoding.Storage.zeroc.QueryNodeDTO;
import com.maoding.Storage.zeroc.SimpleNodeDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/20 11:37
 * 描    述 :
 */
@Repository
public interface StorageDao extends BaseDao<StorageEntity> {

    List<SimpleNodeDTO> listRootNode(@Param("userId") String userId);
    List<SimpleNodeDTO> listNode(QueryNodeDTO query);

    List<SimpleNodeDTO> listAllNode(@Param("userId") String userId);
    List<FileNodeDTO> listFileNode(QueryNodeDTO query);
    List<FileNodeDTO> listFileNodeWithHistory(QueryNodeDTO query);

    FullNodeDTO getNodeExtra(@Param("taskId") String taskId);
    FileNodeDTO getFileNodeExtra(@Param("id") String id);
    FileNodeDTO getFileNodeExtraWithHistory(@Param("id") String id);

//    FileNodeDTO getFileNodeInfo(QueryNodeDTO query);

    List<String> listMajor();
//    SimpleNodeDTO getCommitRoot(QueryNodeDTO query);
//    DesignTaskDTO getDesignTaskInfo(QueryNodeDTO query);


    SimpleNodeDTO getNodeInfo(QueryNodeDTO query);
    Boolean hasChild(QueryNodeDTO query);
    Boolean hasRootChild(QueryNodeDTO query);
//    FileDTO getRealFile(QueryNodeDTO query);

//    StorageEntityUnionDTO selectFileEntityUnion(QueryNodeDTO query);

    StorageEntityUnionDTO selectStorageEntityUnion(QueryNodeDTO query);
    StorageEntityUnionDTO quickSelectStorageEntityUnion(QueryNodeDTO query);

//    /** 准备实现的接口 */
//    FullNodeDTO getFullNodeInfo(QueryNodeDTO query);
//    FullNodeDTO getNearFullNodeInfo(QueryNodeDTO query);
//    SimpleNodeDTO getNearSimpleNodeInfo(QueryNodeDTO query);

//    List<SimpleNodeDTO> listCompanyRootNode(QueryNodeDTO query);
//    List<SimpleNodeDTO> listCompanySubNode(QueryNodeDTO query);

    /** 已经实现的接口 */

//    List<SimpleNodeDTO> listSubNode(QueryNodeDTO query);
//    List<SimpleNodeDTO> listAllSubNode(QueryNodeDTO query);
    List<String> listAllSubNodeIdByPath(String path);
//    List<SimpleNodeDTO> listStorageRootNode(QueryNodeDTO query);
//    List<SimpleNodeDTO> listStorageSubNode(QueryNodeDTO query);
//    SimpleNodeDTO getStorageNode(QueryNodeDTO query);
//    SimpleNodeDTO getStorageNodeByRedundancyPath(QueryNodeDTO query);
//    SimpleNodeDTO getTaskNodeByRedundancyPath(QueryNodeDTO query);
//    SimpleNodeDTO getProjectNodeByRedundancyPath(QueryNodeDTO query);
//    List<SimpleNodeDTO> listProjectRootNode(QueryNodeDTO query);
//    SimpleNodeDTO getProjectNode(QueryNodeDTO query);
//    SimpleNodeDTO getTaskNode(QueryNodeDTO query);
//    List<SimpleNodeDTO> listTaskSubNode(QueryNodeDTO query);
//    Integer countProjectRootNode(QueryNodeDTO query);
//    Integer countTaskSubNode(QueryNodeDTO query);
//    Integer countStorageRootNode(QueryNodeDTO query);
//    Integer countStorageSubNode(QueryNodeDTO query);


    FileNodeDTO getFileNodeInfoByNodeId(String nodeId);
//    List<FileNodeDTO> listRelatedFile(String nodeId);
    /** 使用pid和name查找树节点记录 */
//    StorageEntity selectByPIdAndName(QueryByPidAndNameDTO query);
    /** 使用全路径名查找节点简单信息 */
//    SimpleNodeDTO getSimpleNodeByPath(String path);
    /** 根据全路径名获取第一个子节点类型 */
//    Short getFirstChildTypeIdByPath(String path);
    /** 根据全路径名获取第一个子节点 */
//    StorageEntity getFirstChildNodeByPath(String path);
    /** 根据全路径名获取树id */
//    StorageEntity selectByPath(String path);
    /** 根据全路径名获取文件详细信息 */
//    FileNodeDTO getFileNodeInfoByPath(String path);
    /** 根据节点编号获取一层子节点简单信息 */
//    List<SimpleNodeDTO> listSubNodeByNodeId(String nodeId);
    /** 更改子节点全路径名 */
//    int updateParentPath(@Param("oldPath") String oldPath,@Param("newPath") String newPath);
    /** 根据全路径名获取最靠近的节点 */
//    StorageEntity selectByRedundancyPath(String path);

    /** 有可能被删除的接口 */
//    @Deprecated
//    SimpleNodeDTO getTaskNodeByRedundancyPath2(QueryNodeDTO query);
//    @Deprecated
//    SimpleNodeDTO getTaskNode2(QueryNodeDTO query);
//    @Deprecated
//    List<SimpleNodeDTO> listTaskSubNode2(QueryNodeDTO query);
//    @Deprecated
//    Integer countTaskSubNode2(QueryNodeDTO query);
}
