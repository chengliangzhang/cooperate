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
    /** 在文件服务器上的存储位置 */
    @Column
    private String fileScope;
    /** 在文件服务器上的存储名称 */
    @Column
    private String fileKey;
    /** 文件名 */
    @Column
    private String fileName;
    /** 协同文件创建者的用户职责id */
    @Column
    private String creatorDutyId;
    /** 文件的专业id */
    @Column
    private String specialtyId;
    /** 文件类型 */
    @Column
    private Short typeId;
    /** 校验和 */
    @Column
    private String fileChecksum;
    /** 文件大小 */
    @Column
    private Long fileLength;
    /** 最后上传的地址 */
    @Column
    private String lastModifyAddress;
    /** 用户自定义版本 */
    @Column
    private String fileVersion;
    /** 是否锁定修改，0-不锁定，1-锁定 */
    @Column
    private Boolean locking;
    /** 同步模式，0-手动同步，1-自动更新 */
    @Column
    private Short syncModeId;
    /** 当前有多少用户正在下载 */
    @Column
    private Integer downloading;
    /** 已上传到文件服务器的新scope值 */
    @Column
    private String uploadedScope;
    /** 已上传到文件服务器的新key值 */
    @Column
    private String uploadedKey;

    public String getFileScope() {
        return fileScope;
    }

    public void setFileScope(String fileScope) {
        this.fileScope = fileScope;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreatorDutyId() {
        return creatorDutyId;
    }

    public void setCreatorDutyId(String creatorDutyId) {
        this.creatorDutyId = creatorDutyId;
    }

    public String getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(String specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Short getTypeId() {
        return typeId;
    }

    public void setTypeId(Short typeId) {
        this.typeId = typeId;
    }

    public String getFileChecksum() {
        return fileChecksum;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public String getLastModifyAddress() {
        return lastModifyAddress;
    }

    public void setLastModifyAddress(String lastModifyAddress) {
        this.lastModifyAddress = lastModifyAddress;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public Boolean getLocking() {
        return locking;
    }

    public void setLocking(Boolean locking) {
        this.locking = locking;
    }

    public Short getSyncModeId() {
        return syncModeId;
    }

    public void setSyncModeId(Short syncModeId) {
        this.syncModeId = syncModeId;
    }

    public Integer getDownloading() {
        return downloading;
    }

    public void setDownloading(Integer downloading) {
        this.downloading = downloading;
    }

    public String getUploadedScope() {
        return uploadedScope;
    }

    public void setUploadedScope(String uploadedScope) {
        this.uploadedScope = uploadedScope;
    }

    public String getUploadedKey() {
        return uploadedKey;
    }

    public void setUploadedKey(String uploadedKey) {
        this.uploadedKey = uploadedKey;
    }

    public void reset(){
        super.reset();
        setUploadedScope("");
        setUploadedKey("");
        setLocking(false);
        setDownloading(0);
    }
}
