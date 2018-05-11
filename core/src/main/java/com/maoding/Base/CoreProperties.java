package com.maoding.Base;

import com.maoding.CoreUtils.StringUtils;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
    protected static final Logger log = LoggerFactory.getLogger(CoreProperties.class);

    private final static String GRID_LOCATION = "Ice.Default.Locator";
    private final static String DEFAULT_CODE = "ISO-8859-1";
    private final static String DEFAULT_CHARSET = "utf-8";
    private final static String DEFAULT_CONFIG = "file:///c:/work/maoding-services/services/src/main/resources/properties/ice-config-dev.properties";

    @Autowired
    private ResourceLoader resourceLoader;

    private String config;
    private static Communicator communicator;
    private static Communicator lastCommunicator;
    private static Map<String, String> propertiesMap;

    public String getConfig() {
        return StringUtils.isNotEmpty(config) ? config : DEFAULT_CONFIG;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    private Resource getResource() {
        return resourceLoader.getResource(getConfig());
    }

    private void processProperties(Properties props) throws BeansException {
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

    public void loadAllProperties() {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(getResource());
            processProperties(properties);
        } catch (IOException e) {
            log.error("初始化配置文件出错",e);
        }
    }

    public String getProperty(String name) {
        if (propertiesMap == null) {
            loadAllProperties();
        }
        return propertiesMap.get(name);
    }

    public Map<String, String> getAllProperty() {
        if (propertiesMap == null) {
            loadAllProperties();
        }
        return propertiesMap;
    }

    private static boolean isSameLocator(Communicator c, String ip){
        if (c == null) return false;
        String cip = StringUtils.getParam(c.getDefaultLocator().toString(),"-h");
        return StringUtils.isSame(cip,ip);
    }

    public static Communicator getDirectCommunicator(String locatorConfig,String ip){
        final String DEFAULT_LOCATOR_CONFIG = "IceGrid/Locator:tcp -h 127.0.0.1 -p 4061";
        if (StringUtils.isNotEmpty(ip)) {
            if (!isSameLocator(lastCommunicator,ip)) {
                String x = (lastCommunicator != null) ? lastCommunicator.getDefaultLocator().toString() : "";
                if (StringUtils.isEmpty(locatorConfig)){
                    locatorConfig = DEFAULT_LOCATOR_CONFIG;
                }
                locatorConfig = StringUtils.replaceParam(locatorConfig,"-h",ip);
                lastCommunicator = Util.initialize(new String[]{"--" + GRID_LOCATION + "=" + locatorConfig});
                log.info("使用" + locatorConfig + "的IceGrid服务器");
            }
        } else if (lastCommunicator == null){
            if (StringUtils.isNotEmpty(locatorConfig)){
                lastCommunicator = Util.initialize(new String[]{"--" + GRID_LOCATION + "=" + locatorConfig});
                log.info("使用" + locatorConfig + "的IceGrid服务器");
            } else {
                lastCommunicator = Util.initialize();
                log.info("使用本地的IceGrid服务器");
            }
        }
        return lastCommunicator;
    }

    public static Communicator getDirectCommunicator(String ip){
        return getDirectCommunicator(null,ip);
    }

    public Communicator getCommunicator(String ip){
        communicator = getDirectCommunicator(getProperty(GRID_LOCATION),ip);
        return communicator;

    }

    public Communicator getCommunicator(){
        if (communicator == null){
            communicator = getCommunicator(null);
        }
        return communicator;
    }
}
