package com.maoding.Config;


import com.maoding.Utils.StringUtils;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

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
@Component
@ConfigurationProperties(prefix = "ice")
public class IceConfig {
    /** 日志对象 */
    protected static final Logger log = LoggerFactory.getLogger(IceConfig.class);

    private final static String GRID_LOCATION = "Ice.Default.Locator";

    @Autowired
    private ResourceLoader resourceLoader;

    private String config;
    private String start;

    public static Map<String, String> propertiesMap = null;
    public static Communicator communicator = null;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getConfigFileName(){
        String fileName = null;
        try {
            if (getConfig() != null) {
                if (!getConfig().startsWith("classpath:")){
                    fileName = getConfig();
                } else {
                    fileName = resourceLoader.getResource(getConfig()).getURI().toString();
                    fileName = StringUtils.getLastSplit(fileName, "file:/");
                }
            }
        } catch (IOException e) {
            log.error("初始化ice配置文件出错：" + config);
        }
        return fileName;
    }

    private void processProperties(Properties props) throws BeansException {
        if (props != null) {
            propertiesMap = new HashMap<>();
            for (Object key : props.keySet()) {
                String keyStr = key.toString();
                try {
                    propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    public void loadAllProperties(String propertyFileName) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(propertyFileName);
            processProperties(properties);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    public void loadAllProperties() {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resourceLoader.getResource(config));
            processProperties(properties);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    public String getProperty(String name) {
        if (propertiesMap == null) loadAllProperties();
        return propertiesMap.get(name);
    }

    public Map<String, String> getAllProperty() {
        if (propertiesMap == null) loadAllProperties();
        return propertiesMap;
    }

    public Communicator getCommunicator(){
        if (communicator == null) {
            String gridLocation = getProperty(GRID_LOCATION);
            if (!StringUtils.isEmpty(gridLocation)) {
                communicator = Util.initialize(new String[]{"--" + GRID_LOCATION + "=" + gridLocation});
                log.info("使用" + gridLocation + "的IceGrid服务器");
            } else {
                Util.initialize();
            }
        }
        return communicator;
    }
}
