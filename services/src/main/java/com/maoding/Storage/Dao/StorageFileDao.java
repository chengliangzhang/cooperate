package com.maoding.Storage.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Storage.Entity.StorageFileEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/16 22:25
 * 描    述 :
 */
@Repository
public interface StorageFileDao extends BaseDao<StorageFileEntity> {
    List<StorageFileEntity> listFileEntity(List<String> idList);
}
