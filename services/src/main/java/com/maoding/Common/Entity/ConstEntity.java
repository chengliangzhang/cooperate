package com.maoding.common.entity;

import com.maoding.coreUtils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/12 19:14
 * 描    述 :
 */
@Table(name = "md_const")
public class ConstEntity {
    /** 分类id */
    @Column
    private Short classicId;
    /** 值id */
    @Column
    private String codeId;
    /** 显示信息 */
    private String title;
    /** 控制定义 */
    private String extra;

    public Short getClassicId() {
        return classicId;
    }

    public void setClassicId(Short classicId) {
        this.classicId = classicId;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(int n) {
        return StringUtils.getContent(title,n);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtra() {
        return extra;
    }

    public String getExtra(int n) {
        return StringUtils.getContent(extra,n);
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
