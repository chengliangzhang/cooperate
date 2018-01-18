package com.maoding.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 深圳市卯丁技术有限公司
 * @author 张成亮
 * @date 2017/10/31
 * 描    述 :
 */
public class JsonUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    
    /** 转换器 */
    private final static ObjectMapper objectMapper = new ObjectMapper();

    protected static ObjectMapper getMapper() {
        return objectMapper;
    }

    /** 把java对象转换为json字符串 */
    public static String obj2Json(Object obj){
        String json = null;
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
            json = null;
        }
        return json;
    }

    /** 把json字符串转换为java对象 */
    public static <T> T json2Obj(String json, Class<T> clazz) {
        T obj = null;
        try {
            obj = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            obj = null;
        }
        return obj;
    }
}
