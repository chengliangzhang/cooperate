package com.maoding.Bean;

import java.io.Serializable;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 9:55
 * 描    述 :
 */
public class BasicHttpRequestDTO implements Serializable {
    /** 申请文件服务器操作时的访问地址 */
    String url;
    /** 申请文件服务器操作时的包头 */
    String header;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
