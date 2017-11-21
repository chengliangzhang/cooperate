package com.maoding.Storage.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Storage.Entity.StorageEntity;
import com.maoding.Storage.zeroc.CooperateFileDTO;
import com.maoding.Storage.zeroc.CooperationQueryDTO;
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
    List<CooperateFileDTO> listFileByScopeAndKey(CooperationQueryDTO query);
}
