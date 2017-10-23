package com.maoding.Base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 :
 */
public class BaseEntity implements Serializable,Cloneable {
    /** 实体ID */
    @GeneratedValue(generator = "UUID")
    @Id
    private String id;

//    /** 删除标志 */
//    @Column
//    private Integer deleted;
//
//    /** 创建时间 */
//    @Column
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//
//    /** 最后修改时间 */
//    @Column
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime lastModifyTime;

//    /** 最后修改者ID */
//    @Column
//    private String lastModifyUserId;
//
//    /** 最后修改者职责ID */
//    @Column
//    private String lastModifyDutyId;

    /** 维持兼容 */
    @Column
    private String 	createBy;
    @Column
    private String 	updateBy;
    @Column
    private Date createDate;
    @Column
    private Date updateDate;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /************维持兼容*********************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Integer getDeleted() {
//        return deleted;
//    }
//
//    public void setDeleted(Integer deleted) {
//        this.deleted = deleted;
//    }
//
//    public LocalDateTime getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(LocalDateTime createTime) {
//        this.createTime = createTime;
//    }
//
//    public LocalDateTime getLastModifyTime() {
//        return lastModifyTime;
//    }
//
//    public void setLastModifyTime(LocalDateTime lastModifyTime) {
//        this.lastModifyTime = lastModifyTime;
//    }
//
//    public String getLastModifyUserId() {
//        return lastModifyUserId;
//    }
//
//    public void setLastModifyUserId(String lastModifyUserId) {
//        this.lastModifyUserId = lastModifyUserId;
//    }
//
//    public String getLastModifyDutyId() {
//        return lastModifyDutyId;
//    }
//
//    public void setLastModifyDutyId(String lastModifyDutyId) {
//        this.lastModifyDutyId = lastModifyDutyId;
//    }

//    /** 初始化实体 */
//    public BaseEntity(){}
//    public BaseEntity(Object obj){
//        BeanUtils.copyProperties(obj,this);
//    }

//    /** 重新初始化 */
//    public void reset() {
//        resetId();
//        resetTime();
//    }
//
//    /** 重新初始化状态 */
//    public void resetTime(){
//        resetDeleted();
//        resetCreateTime();
//        resetLastModifyTime();
//    }
//
//    /** 重置主键Id为新的UUID */
//    public void resetId() {
//        id = UUID.randomUUID().toString().replaceAll("-", "");
//    }
//
//    /** 重置删除标志 */
//    public void resetDeleted(){deleted = 0;}
//
//    /** 重置创建时间为当前时间 */
//    public void resetCreateTime() {
//        createTime = LocalDateTime.now();
//    }
//
//    /** 重置最后更改时间为当前时间 */
//    public void resetLastModifyTime() {
//        lastModifyTime = LocalDateTime.now();
//    }
//
//    /** 维持兼容性 */
//    @JsonIgnore
//    public void resetCreateDate(){resetCreateTime();}
//    @JsonIgnore
//    public void setCreateDate(LocalDateTime createDate){setCreateTime(createDate);}
//    @JsonIgnore
//    public LocalDateTime getCreateDate(){return getCreateTime();}
//    @JsonIgnore
//    public void resetUpdateDate(){resetLastModifyTime();}
//    @JsonIgnore
//    public void setUpdateDate(LocalDateTime updateDate){setLastModifyTime(updateDate);}
//    @JsonIgnore
//    public void setCreateBy(String createBy){setLastModifyUserId(createBy);}
//    @JsonIgnore
//    public String getCreateBy(){return getLastModifyUserId();}
//    @JsonIgnore
//    public void setUpdateBy(String updateBy){setLastModifyUserId(updateBy);}
//    @JsonIgnore
//    public void initEntity(){reset();}
}

