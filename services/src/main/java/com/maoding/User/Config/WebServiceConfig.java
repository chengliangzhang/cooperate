package com.maoding.User.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/13 15:17
 * 描    述 :
 */
@Component
@ConfigurationProperties(prefix = "webservice")
public class WebServiceConfig {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLoginUrl() {
        String LOGIN_URL = "/iWork/sys/login";
        return url + LOGIN_URL;
    }

    public String getLoginParamsType() {
        return "application/json";
    }

    public String getGetCurrentUrl() {
        String GET_WORK_URL = "/iWork/sys/getCurrUserOfWork";
        return url + GET_WORK_URL;
    }

    public String getGetCurrentIdKey() {
        return "id";
    }

    public String getGetCurrentNameKey() {
        return "userName";
    }

    public String getGetCurrentInfoKey() {
        return "userInfo";
    }
}
