package com.maoding.Storage.Entity;

import com.maoding.Base.BaseEntity;

import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/28 14:47
 * 描    述 :
 */
@Table(name = "maoding_storage_file_his")
public class StorageFileHisEntity extends BaseEntity {
    /** 协同文件编号id */
    private String mainFileId;
    @Deprecated
    private String fileId;
    /** 校审动作类型id */
    private Short actionTypeId;
    /** 操作注解 */
    private String remark;

    public String getMainFileId() {
        return mainFileId;
    }

    public void setMainFileId(String mainFileId) {
        this.mainFileId = mainFileId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Short getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(Short actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
