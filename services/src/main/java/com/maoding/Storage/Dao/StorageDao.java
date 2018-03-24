package com.maoding.Storage.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Storage.Dto.StorageEntityUnionDTO;
import com.maoding.Storage.Entity.StorageTreeEntity;
import com.maoding.Storage.zeroc.*;
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
public interface StorageDao extends BaseDao<StorageTreeEntity> {
    List<SimpleNodeDTO> listWebArchiveDir(QueryNodeDTO query);
    List<SimpleNodeDTO> listOldNode(QueryNodeDTO query);

    @Deprecated
    List<SimpleNodeDTO> listRootNode(@Param("userId") String userId);
    List<SimpleNodeDTO> listNode(QueryNodeDTO query);

    @Deprecated
    List<SimpleNodeDTO> listAllNode(@Param("userId") String userId);
    List<FileNodeDTO> listFileNode(QueryNodeDTO query);
    List<FileNodeDTO> listFileNodeWithHistory(QueryNodeDTO query);

    @Deprecated
    FullNodeDTO getNodeExtra(@Param("taskId") String taskId);
    FileNodeDTO getFileNodeExtra(@Param("id") String id);
    FileNodeDTO getFileNodeExtraWithHistory(@Param("id") String id);

    @Deprecated
    List<String> listMajor();

    SimpleNodeDTO getNodeInfo(QueryNodeDTO query);
    Boolean hasChild(QueryNodeDTO query);
    Boolean hasRootChild(QueryNodeDTO query);

    StorageEntityUnionDTO selectStorageEntityUnion(QueryNodeDTO query);
    @Deprecated
    StorageEntityUnionDTO quickSelectStorageEntityUnion(QueryNodeDTO query);

    List<String> listAllSubNodeIdByPath(String path);
    FileNodeDTO getFileNodeInfoByNodeId(String nodeId);

    FullNodeDTO getNodeDetailByNodeId(@Param("id") String nodeId, @Param("request") QueryNodeInfoDTO request);
    StorageEntityUnionDTO selectUnionById(@Param("id") String id);

    Long summaryNodeLength(QuerySummaryDTO query);
}
