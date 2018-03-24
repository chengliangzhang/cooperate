package com.maoding.CoreFileServer;

import com.maoding.CoreUtils.StringUtils;

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
    private String key;
    @Deprecated
    private String path;
    /** 要创建的文件节点的父id */
    @Deprecated
    private String pid;
    /** 要创建的文件节点的项目id */
    @Deprecated
    private String projectId;
    /** 要创建的文件节点的任务id */
    @Deprecated
    private String taskId;
    /** 要创建的文件节点的组织id */
    @Deprecated
    private String companyId;
    /** 要创建的文件节点的拥有者id */
    @Deprecated
    private String ownerUserId;
    /** 要创建的文件节点的时间 */
    @Deprecated
    private Date lastModifyTime;
    /** 要创建的文件的长度*/
    private long fileLength;
    /** 要创建的文件的原始文件 */
    private File srcFile;

    /** 维持兼容性 */
    /** 要创建的文件节点的操作者id */
    @Deprecated
    private String accountId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Deprecated
    public String getOwnerUserId() {
        return ownerUserId;
    }

    @Deprecated
    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getPath() {
        return StringUtils.isEmpty(path) ? key : path;
    }

    public void setPath(String path) {
        this.path = path;
        this.key = path;
    }

    @Deprecated
    public String getPid() {
        return pid;
    }

    @Deprecated
    public void setPid(String pid) {
        this.pid = pid;
    }

    @Deprecated
    public String getProjectId() {
        return projectId;
    }

    @Deprecated
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Deprecated
    public String getTaskId() {
        return taskId;
    }

    @Deprecated
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Deprecated
    public String getCompanyId() {
        return companyId;
    }

    @Deprecated
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Deprecated
    public String getAccountId() {
        return accountId;
    }

    @Deprecated
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Deprecated
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    @Deprecated
    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public File getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    public CoreCreateFileRequest(String path,long fileLength,File srcFile){
        setPath(path);
        setFileLength(fileLength);
        setSrcFile(srcFile);
    }
    public CoreCreateFileRequest(String path){
        this(path,0,null);
    }
    public CoreCreateFileRequest(String path,long fileLength){
        this(path,fileLength,null);
    }
    public CoreCreateFileRequest(String path,File srcFile){
        this(path,0,srcFile);
    }
    public CoreCreateFileRequest(File srcFile){
        this(null,0,srcFile);
    }
    public CoreCreateFileRequest(long fileLength){
        this(null,fileLength,null);
    }
    public CoreCreateFileRequest(){
        this(null,0,null);
    }
}
