package com.maoding.Storage.Dto;

import com.maoding.Storage.zeroc.CooperateFileDTO;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/21 21:29
 * 描    述 :
 */
public class RequestUploadParamsDTO {
    private CooperateFileDTO fileInfo;
    private Integer mode;

    public CooperateFileDTO getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(CooperateFileDTO fileInfo) {
        this.fileInfo = fileInfo;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
