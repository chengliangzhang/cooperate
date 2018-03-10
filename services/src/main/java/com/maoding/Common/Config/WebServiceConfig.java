package com.maoding.Common.Config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
    private CloseableHttpClient client = null;

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

    public String getLoadProjectDetailsUrl(){
        return url + "/iWork/project/loadProjectDetails";
    }

    public String getUploadUrl(){
        return url + "/fileCenter/netFile/uploadFile";
    }

    public CloseableHttpClient getClient() {
        if (client == null){
            client = HttpClients.createDefault();
        }
        return client;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
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
