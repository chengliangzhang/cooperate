package com.maoding.CoreFileServer;

import java.io.File;
import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/23 16:34
 * 描    述 :
 */
public class CoreCreateFileRequest {
    /** 要创建的文件的节点路径 */
    private String path;
    /** 要创建的文件节点的父id */
    private String pid;
    /** 要创建的文件节点的项目id */
    private String projectId;
    /** 要创建的文件节点的任务id */
    private String taskId;
    /** 要创建的文件节点的组织id */
    private String companyId;
    /** 要创建的文件节点的拥有者id */
    private String ownerUserId;
    /** 要创建的文件节点的时间 */
    private Date lastModifyTime;
    /** 要创建的文件的长度*/
    private Long fileLength;
    /** 要创建的文件的原始文件 */
    private File srcFile;

    /** 维持兼容性 */
    /** 要创建的文件节点的操作者id */
    private String accountId;

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public File getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    public CoreCreateFileRequest(String path,Long fileLength,File srcFile){
        setPath(path);
        setFileLength(fileLength);
        setSrcFile(srcFile);
        setLastModifyTime(new Date());
    }
    public CoreCreateFileRequest(String path){
        this(path,null,null);
    }
    public CoreCreateFileRequest(String path,Long fileLength){
        this(path,fileLength,null);
    }
    public CoreCreateFileRequest(String path,File srcFile){
        this(path,null,srcFile);
    }
    public CoreCreateFileRequest(File srcFile){
        this(null,null,srcFile);
    }
    public CoreCreateFileRequest(Long fileLength){
        this(null,fileLength,null);
    }
    public CoreCreateFileRequest(){
        this(null,null,null);
    }
}
