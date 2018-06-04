package com.maoding.common.config;

import com.maoding.common.ConstService;
import com.maoding.coreBase.CoreProperties;
import com.maoding.coreUtils.ObjectUtils;
import com.maoding.coreUtils.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/5/17 17:41
 * 描    述 :
 */
public class BaseConfig {

    private String config = null; //配置文件参数
    private String identify = null; //特殊标志

    private CoreProperties properties = null; //配置文件实体

    public String getIdentify() {
        return (StringUtils.isEmpty(identify)) ? "" : identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public CoreProperties getProperties(){
        if (properties == null){
            String fn = getConfig();
            if (StringUtils.isNotEmpty(fn)) {
                properties = new CoreProperties(fn);
            }
        }
        return properties;
    }

    public String getProperty(short classicId, @NotNull String name){
        //从数据库读取配置信息
        String p = ConstService.getExtraByTitle(classicId,name + getIdentify());
        //如果没有从数据库内读取到配置信息，则从配置文件读取配置信息
        if (StringUtils.isEmpty(p) && ObjectUtils.isNotEmpty(getProperties())) {
            p = getProperties().getProperty(name);
        }
        return p;
    }

    public String getProperty(@NotNull String name){
        return getProperty(ConstService.CLASSIC_TYPE_CONFIG,name);
    }

    public String getProperty(String name,String defaultConfig,String defaultConst){
        String s = getProperty(name);
        if (StringUtils.isEmpty(s)) {
            s = (StringUtils.isEmpty(defaultConfig) ? defaultConst : defaultConfig);
        }
        return s;
    }
}
