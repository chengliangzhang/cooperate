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
    /** 对应的文件id */
    String fileId;
    /** 操作动作id */
    Short actionId;
    /** 操作说明 */
    String remark;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Short getActionId() {
        return actionId;
    }

    public void setActionId(Short actionId) {
        this.actionId = actionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
