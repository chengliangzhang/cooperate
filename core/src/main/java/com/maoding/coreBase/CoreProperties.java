package com.maoding.coreBase;

import com.maoding.coreUtils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/5/6 14:14
 * 描    述 :
 */
public class CoreProperties {
    /** 日志对象 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final static String DEFAULT_CODE = "ISO-8859-1";
    private final static String DEFAULT_CHARSET = "utf-8";
    private final static String DEFAULT_CONFIG = "classpath:properties/ice-config-dev.properties";

    private Map<String, String> propertiesMap;
    private String config;

    public CoreProperties(){}

    public CoreProperties(String config){
        loadAllProperties(config);
        setConfig(config);
    }

    public String getConfig() {
        return StringUtils.isNotEmpty(config) ? config : DEFAULT_CONFIG;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    private void loadAllProperties(Properties props) throws BeansException {
        if (props != null) {
            propertiesMap = new HashMap<>();
            for (Object key : props.keySet()) {
                String keyStr = key.toString();
                try {
                    propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes(DEFAULT_CODE), DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    log.error("加载配置属性出错",e);
                }
            }
        }
    }

    public void loadAllProperties(String config) {
        try {
            ResourceLoader loader = new FileSystemResourceLoader();
            Resource resource = loader.getResource(config);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            loadAllProperties(properties);
        } catch (IOException e) {
            log.error("初始化配置文件出错",e);
        }
    }

    public void loadAllProperties() {
        loadAllProperties(getConfig());
    }

    public String getProperty(String name) {
        if (propertiesMap == null) {
            loadAllProperties();
        }
        return propertiesMap.get(name);
    }

    public Map<String, String> getPropertiesMap() {
        if (propertiesMap == null) {
            loadAllProperties();
        }
        return propertiesMap;
    }
}
