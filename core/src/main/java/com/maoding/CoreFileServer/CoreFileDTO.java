package com.maoding.CoreFileServer;

import java.io.Serializable;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 9:53
 * 描    述 :
 */
public class CoreFileDTO implements Serializable {
    /** 文件空间，即阿里云的buchet或FastFDS的group */
    private String scope;
    /** 文件标志，即阿里云的key或FastFDS的key */
    private String key;

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
}
