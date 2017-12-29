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
    /** 使用id查找fileEntity */
    @Deprecated
    List<StorageFileEntity> listFileEntityById(List<String> idList);
    /** 使用路径查找fileEntity */
    List<StorageFileEntity> listFileEntityByPath(String path);
}
