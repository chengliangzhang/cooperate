package com.maoding.Storage.Entity;

import com.maoding.Base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/28 14:47
 * 描    述 :
 */
@Table(name = "md_list_storage_file_his")
public class StorageFileHisEntity extends BaseEntity {
    @Column /** 协同文件编号id */
    private String mainFileId;
    @Column /** 校审动作类型id */
    private Short actionTypeId;
    @Column /** 文件操作时的只读文件长度 */
    private long fileLength;
    @Column /** 文件操作时的只读文件md5校验值 */
    private String fileMd5;
    @Column /** 操作注解 */
    private String remark;

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getMainFileId() {
        return mainFileId;
    }

    public void setMainFileId(String mainFileId) {
        this.mainFileId = mainFileId;
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
