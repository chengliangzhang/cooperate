package com.maoding.CoreNotice.ActiveMQ;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/18 21:19
 * 描    述 :
 */
public class imDTO {
    String id;
    Integer queueNo;
    String targetId;
    String operation;
    imDetailDTO content;
    Integer retry;
    Integer ignoreFix;
    Boolean fixMode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(Integer queueNo) {
        this.queueNo = queueNo;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public imDetailDTO getContent() {
        return content;
    }

    public void setContent(imDetailDTO content) {
        this.content = content;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Integer getIgnoreFix() {
        return ignoreFix;
    }

    public void setIgnoreFix(Integer ignoreFix) {
        this.ignoreFix = ignoreFix;
    }

    public Boolean getFixMode() {
        return fixMode;
    }

    public void setFixMode(Boolean fixMode) {
        this.fixMode = fixMode;
    }
}
