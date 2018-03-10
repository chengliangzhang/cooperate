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
@Table(name = "maoding_storage_tree")
public class StorageTreeEntity extends BaseTreeEntity {
    /** 文件长度，如果节点是目录则固定为0 */
    @Column
    private Long fileLength;
    /** 相关联的任务id */
    @Column
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

}
