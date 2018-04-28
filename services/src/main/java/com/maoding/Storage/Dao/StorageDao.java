package com.maoding.Storage.Dao;

import com.maoding.Storage.Dto.StorageEntityUnionDTO;
import com.maoding.Storage.Entity.StorageFileEntity;
import com.maoding.Storage.zeroc.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/20 11:37
 * 描    述 :
 */
@Repository
public interface StorageDao {
    List<CANodeDTO> listCANode(QueryCANodeDTO query);
    List<SimpleNodeDTO> listWebArchiveDir(QueryNodeDTO query);

    List<SimpleNodeDTO> listNode(QueryNodeDTO query);
    List<NodeFileDTO> listNodeFile(QueryNodeFileDTO query);

    StorageEntityUnionDTO selectStorageEntityUnion(QueryNodeDTO query);
    StorageFileEntity selectFileEntity(Map<String,Object> query);

    FullNodeDTO getNodeDetailByNodeId(@Param("id") String nodeId, @Param("request") QueryNodeInfoDTO request);

    Long summaryNodeLength(QuerySummaryDTO query);
}
