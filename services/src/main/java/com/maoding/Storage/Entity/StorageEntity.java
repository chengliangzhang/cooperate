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
    /** 锁定树节点的用户id */
    @Column
    private String lockUserId;

    public String getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(String lockUserId) {
        this.lockUserId = lockUserId;
    }

    public StorageEntity reset(){
        super.reset();
        setLockUserId(null);
        return this;
    }

    public StorageEntity clear(){
        super.clear();
        setLockUserId(null);
        return this;
    }
}
