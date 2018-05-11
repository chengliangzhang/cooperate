package com.maoding.Common.Config;


import com.maoding.Base.CoreConfig;
import com.maoding.CoreUtils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/7 18:12
 * 描    述 :
 */
@EnableAutoConfiguration
@Component
@Configuration
@ConfigurationProperties(prefix = "ice")
public class IceConfig extends CoreConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    private String config;
    private String startCmd;
    private String cmdConfig;
    private String cmdStart;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getStartCmd() {
        return startCmd;
    }

    public void setStartCmd(String startCmd) {
        this.startCmd = startCmd;
    }

    public String getCmdConfig() {
        return cmdConfig;
    }

    public void setCmdConfig(String cmdConfig) {
        this.cmdConfig = cmdConfig;
    }

    public String getCmdStart() {
        return cmdStart;
    }

    public void setCmdStart(String cmdStart) {
        this.cmdStart = cmdStart;
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
}
