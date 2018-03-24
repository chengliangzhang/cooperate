package com.maoding.Storage.Entity;

import com.maoding.Base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/21 10:08
 * 描    述 :
 */
@Table(name = "md_list_annotate")
public class AnnotateEntity extends BaseEntity {
    @Column /** 批注评论标题 */
    private String title;
    @Column /** 批注评论正文 */
    private String content;
    @Column /** 校审意见编号 */
    private String relatedId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }
}
