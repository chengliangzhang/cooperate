package com.maoding.Storage.Entity;

import com.maoding.Base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/16 22:12
 * 描    述 :
 */
@Table(name = "maoding_storage_file")
public class StorageFileEntity extends BaseEntity {
    /** 文件服务器类型 */
    @Column
    private Short serverTypeId;
    /** 文件服务器地址 */
    @Column
    private String serverAddress;
    /** 文件类型 */
    @Column
    private Short fileTypeId;
    /** 文件版本 */
    @Column
    private String fileVersion;
    /** 文件校验和 */
    @Column
    private String fileChecksum;
    /** 文件所属专业id */
    @Column
    private String majorId;
    /** 所对应的原始文件id */
    @Column
    private String mainFileId;
    /** 只读文件在文件服务器上的存储位置 */
    @Column
    private String readFileScope;
    /** 只读文件在文件服务器上的存储名称 */
    @Column
    private String readFileKey;
    /** 可写文件在文件服务器上的存储位置 */
    @Column
    private String writeFileScope;
    /** 可写文件在文件服务器上的存储名称 */
    @Column
    private String writeFileKey;

    public Short getServerTypeId() {
        return serverTypeId;
    }

    public void setServerTypeId(Short serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Short getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(Short fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getFileChecksum() {
        return fileChecksum;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getMainFileId() {
        return mainFileId;
    }

    public void setMainFileId(String mainFileId) {
        this.mainFileId = mainFileId;
    }

    public String getReadFileScope() {
        return readFileScope;
    }

    public void setReadFileScope(String readFileScope) {
        this.readFileScope = readFileScope;
    }

    public String getReadFileKey() {
        return readFileKey;
    }

    public void setReadFileKey(String readFileKey) {
        this.readFileKey = readFileKey;
    }

    public String getWriteFileScope() {
        return writeFileScope;
    }

    public void setWriteFileScope(String writeFileScope) {
        this.writeFileScope = writeFileScope;
    }

    public String getWriteFileKey() {
        return writeFileKey;
    }

    public void setWriteFileKey(String writeFileKey) {
        this.writeFileKey = writeFileKey;
    }
}
