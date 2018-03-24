package com.maoding.Storage.Entity;

import com.maoding.Base.BaseEntity;

import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/19 20:04
 * 描    述 :
 */
@Table(name = "maoding_element")
public class ElementEntity extends BaseEntity {
    private String title;
    private byte[] dataArray;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getDataArray() {
        return dataArray;
    }

    public void setDataArray(byte[] dataArray) {
        this.dataArray = dataArray;
    }
}
