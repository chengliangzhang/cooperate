package com.maoding.CoreFileServer;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/12 14:53
 * 描    述 :
 */
public class CoreFileCopyResult {
    /** 文件的md5 */
    String fileChecksum;
    /** 文件长度 */
    long fileLength;

    public String getFileChecksum() {
        return fileChecksum;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
}
