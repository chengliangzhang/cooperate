package com.maoding.Storage.Entity;

import com.maoding.Base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/21 16:19
 * 描    述 :
 */
@Table(name = "md_list_suggestion")
public class SuggestionEntity extends BaseEntity {
    @Column /** 校审意见类型 */
    private String typeId;
    @Column /** 校审意见正文 */
    private String content;
    @Column /** 校审文件编号 */
    private String mainFileId;
    @Column /** 校审状态编号 */
    private String statusTypeId;
    @Column /** 校审意见创建者用户编号 */
    private String creatorUserId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMainFileId() {
        return mainFileId;
    }

    public void setMainFileId(String mainFileId) {
        this.mainFileId = mainFileId;
    }

    public String getStatusTypeId() {
        return statusTypeId;
    }

    public void setStatusTypeId(String statusTypeId) {
        this.statusTypeId = statusTypeId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }
}
