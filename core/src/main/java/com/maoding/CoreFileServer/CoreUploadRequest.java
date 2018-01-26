package com.maoding.CoreFileServer;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/23 16:34
 * 描    述 :
 */
public class CoreUploadRequest {
    /** 远端文件服务器地址 */
    private String address;
    /** 要创建的文件节点的父id */
    private String pid;
    /** 要创建的文件节点的项目id */
    private String projectId;
    /** 要创建的文件节点的任务id */
    private String taskId;
    /** 要创建的文件节点的组织id */
    private String companyId;
    /** 要创建的文件节点的操作者id */
    private String accountId;
    /** 要创建的文件节点的时间 */
    private Date lastModifyTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
