package com.maoding.FileServer.Dto;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/26 15:27
 * 描    述 :
 */
public class CopyRequestDTO {
    private Short fileServerType;
    private String fileServerAddress;
    private String dstScope;
    private String dstKey;

    public Short getFileServerType() {
        return fileServerType;
    }

    public void setFileServerType(Short fileServerType) {
        this.fileServerType = fileServerType;
    }

    public String getFileServerAddress() {
        return fileServerAddress;
    }

    public void setFileServerAddress(String fileServerAddress) {
        this.fileServerAddress = fileServerAddress;
    }

    public String getDstScope() {
        return dstScope;
    }

    public void setDstScope(String dstScope) {
        this.dstScope = dstScope;
    }

    public String getDstKey() {
        return dstKey;
    }

    public void setDstKey(String dstKey) {
        this.dstKey = dstKey;
    }
}
