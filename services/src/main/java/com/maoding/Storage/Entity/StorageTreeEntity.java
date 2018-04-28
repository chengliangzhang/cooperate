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
@Table(name = "md_tree_storage")
public class StorageTreeEntity extends BaseTreeEntity {
    @Column /** 相关联的任务id */
    private String taskId;

    @Column /** 相关联的项目id */
    private String projectId;

    @Column /** 文件所属用户id */
    private String ownerUserId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

}
