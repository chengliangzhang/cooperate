package com.maoding.Storage.Dto;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/10 11:12
 * 描    述 :
 */
public class QueryNodeDTO {
    /** 节点id */
    private String nodeId;
    /** 用户id */
    private String userId;
    /** 绝对路径 */
    private String path;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
