package com.maoding.Common.Entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/12 19:14
 * 描    述 :
 */
@Table(name = "maoding_const")
public class ConstEntity {
    /** 分类id */
    @Column
    private Short classicId;
    /** 值id */
    @Column
    private Short valueId;
    /** 基本定义 */
    private String content;
    /** 扩展定义 */
    private String contentExtra;

    public Short getClassicId() {
        return classicId;
    }

    public void setClassicId(Short classicId) {
        this.classicId = classicId;
    }

    public Short getValueId() {
        return valueId;
    }

    public void setValueId(Short valueId) {
        this.valueId = valueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentExtra() {
        return contentExtra;
    }

    public void setContentExtra(String contentExtra) {
        this.contentExtra = contentExtra;
    }
}
