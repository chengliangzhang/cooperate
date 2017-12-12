package com.maoding.Storage.Entity;

import com.maoding.Base.BaseTreeEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/17 10:55
 * 描    述 :
 */
@Table(name = "maoding_storage")
public class StorageEntity extends BaseTreeEntity {
    /** 文件长度，如果节点是目录则固定为0 */
    @Column
    private Long fileLength;

    /** 上级节点类型 */
    @Column
    private Short pidTypeId;

    /** （取消，因只需锁定文件）锁定树节点的用户id */
    @Column
    private String lockUserId;

    /** （取消，因唯一编号同id）对应file/dir内的唯一编号 */
    @Column
    private String detailId;

    public String getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(String lockUserId) {
        this.lockUserId = lockUserId;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public Short getPidTypeId() {
        return pidTypeId;
    }

    public void setPidTypeId(Short pidTypeId) {
        this.pidTypeId = pidTypeId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
