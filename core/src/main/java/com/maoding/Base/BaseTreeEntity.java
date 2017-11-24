package com.maoding.Base;

import javax.persistence.Column;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/20 11:27
 * 描    述 :
 */
public class BaseTreeEntity extends BaseEntity{
    /** 父节点唯一编号 */
    @Column
    private String pid;

    /** 从根节点到本节点的id路径 */
    @Column
    private String path;

    /** 对应节点实体的唯一编号 */
    @Column
    private String detailId;

    /** 对应节点实体的类型 */
    @Column
    private Short typeId;

    /** 节点名称 */
    @Column
    private String nodeName;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public Short getTypeId() {
        return typeId;
    }

    public void setTypeId(Short typeId) {
        this.typeId = typeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
