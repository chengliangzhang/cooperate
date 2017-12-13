package com.maoding.Utils;

import com.maoding.Const.HttpConst;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/10 10:45
 * 描    述 :
 */
public class HttpUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static <T> CloseableHttpResponse postData(CloseableHttpClient client, String url, String type, T data) {
        assert (url != null);
        final String requestBodyType = "application/json";
        final String defaultVarName = "var";

        //建立参数
        StringEntity entity = null;
        if ((type != null) && (data != null)) {
            if (requestBodyType.equals(type)) {
                entity = new StringEntity(JsonUtils.obj2Json(data), StandardCharsets.UTF_8);
            } else {
                List<NameValuePair> params = new ArrayList<>();
                if (data instanceof Map) {
                    for (Map.Entry<?, ?> item : ((Map<?, ?>) data).entrySet()) {
                        params.add(new BasicNameValuePair(item.getKey().toString(), JsonUtils.obj2Json(item.getValue())));
                    }
                } else {
                    params.add(new BasicNameValuePair(defaultVarName, JsonUtils.obj2Json(data)));
                }
                entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
            }
            entity.setContentEncoding("UTF-8");
            entity.setContentType(type);
        }

        // 发起Post请求
        HttpPost post = new HttpPost(url);
        if (type != null) post.setHeader(HTTP.CONTENT_TYPE,type);
        if (entity != null) post.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (HttpHostConnectException e) {
            log.warn("无法连接" + url);
            response = null;
        } catch (IOException e) {
            ExceptionUtils.logError(log,e);
        }
        return response;
    }
    public static <T> CloseableHttpResponse postData(CloseableHttpClient client, String url){
        return postData(client,url,null,null);
    }

    public static Boolean isResponseOK(CloseableHttpResponse response){
        return (response != null) && (response.getStatusLine() != null) && (response.getStatusLine().getStatusCode() == HttpConst.HTTP_RESULT_OK);
    }

}
