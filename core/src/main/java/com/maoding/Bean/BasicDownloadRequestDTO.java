package com.maoding.Bean;

import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/31 10:21
 * 描    述 :
 */
public class BasicDownloadRequestDTO {
    /** 下载申请的唯一编号 */
    private Integer requestId;
    /** 下载任务ID（没有作用，将被取消） */
    private String downloadId; //没有作用，将被取消
    /** 空间(bucket或group) */
    private String scope;
    /** 文件id(key或path) */
    private String key;
    /** 申请下载的分片序号，与分片大小相乘计算出文件起始位置 */
    private Integer chunkId;
    /** 申请下载的分片大小，如果为0则chunkId失效，下载文件所有内容 */
    private Integer chunkSize;
    /** 分片的约定大小，如果为0则下载后续所有内容（与chunkSize重复，将被取消） */
    private Integer chunkPerSize; //与chunkSize重复，将被取消
    /** 下载的起始位置 */
    private Long pos;
    /** 下载的大小 */
    private Integer size;
    /** 其他下载参数 */
    private Map<String,String> params;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getChunkId() {
        return chunkId;
    }

    public void setChunkId(Integer chunkId) {
        this.chunkId = chunkId;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getChunkPerSize() {
        return chunkPerSize;
    }

    public void setChunkPerSize(Integer chunkPerSize) {
        this.chunkPerSize = chunkPerSize;
    }

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
