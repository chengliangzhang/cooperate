package com.maoding.Storage.Entity;

import com.maoding.Base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/20 11:33
 * 描    述 :
 */
@Table(name = "maoding_storage_dir")
public class StorageDirEntity extends BaseEntity {
    /** 目录名 */
    @Column
    private String fullName;
    /** 协同目录所属用户id */
    @Column
    private String userId;
    /** 协同目录所属用户的职责id */
    @Column
    private String dutyId;
    /** 所属组织id,项目目录在立项时创建，创建组织为立项组织 */
    @Column
    private String orgId;
    /** 所属项目id,目录如果不属于任何一个项目，项目id为空 */
    @Column
    private String projectId;
    /** 所属任务id，目录如果不属于任何一个任务，任务id为空 */
    @Column
    private String taskId;
    /** 目录类别，如：系统默认目录、用户添加目录 */
    @Column
    private Short typeId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Short getTypeId() {
        return typeId;
    }

    public void setTypeId(Short typeId) {
        this.typeId = typeId;
    }

    public StorageDirEntity reset(){
        super.reset();
        setFullName(null);
        return this;
    }

    public StorageDirEntity clear(){
        super.clear();
        setFullName(null);
        return this;
    }
}
