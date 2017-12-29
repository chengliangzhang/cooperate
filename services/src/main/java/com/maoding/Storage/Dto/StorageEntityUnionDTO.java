package com.maoding.Storage.Dto;

import com.maoding.Storage.Entity.StorageEntity;
import com.maoding.Storage.Entity.StorageFileEntity;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/18 20:09
 * 描    述 :
 */
public class StorageEntityUnionDTO extends StorageEntity {
    /** 节点对应文件信息 */
    private StorageFileEntity fileEntity;

    public StorageFileEntity getFileEntity() {
        return fileEntity;
    }

    public void setFileEntity(StorageFileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }
}
