package com.maoding.Config;


import com.maoding.Utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/7 18:12
 * 描    述 :
 */
@Configuration
public class IceConfig {
    /** 日志对象 */
    protected static final Logger log = LoggerFactory.getLogger(IceConfig.class);

    protected static final String DEFAULT_CONFIG_FILE = "properties/ice-config.properties";

    public static Map<String, String> propertiesMap = null;

    private static void processProperties(Properties props) throws BeansException {
        if (props != null) {
            propertiesMap = new HashMap<>();
            for (Object key : props.keySet()) {
                String keyStr = key.toString();
                try {
                    propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    ExceptionUtils.logError(log,e);
                }
            }
        }
    }

    public static void loadAllProperties(String propertyFileName) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(propertyFileName);
            processProperties(properties);
        } catch (IOException e) {
            ExceptionUtils.logError(log,e);
        }
    }

    public static void loadAllProperties() {
        loadAllProperties(DEFAULT_CONFIG_FILE);
    }

    public static String getProperty(String name) {
        if (propertiesMap == null) loadAllProperties();
        return propertiesMap.get(name);
    }

    public static Map<String, String> getAllProperty() {
        if (propertiesMap == null) loadAllProperties();
        return propertiesMap;
    }
}
