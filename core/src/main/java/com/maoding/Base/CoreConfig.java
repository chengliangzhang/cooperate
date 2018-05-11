package com.maoding.Base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/5/6 14:14
 * 描    述 :
 */
public class CoreConfig {
    /** 日志对象 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "iceProperties")
    @Primary
    public CoreProperties iceProperties() {
        CoreProperties properties = new CoreProperties();
        try {
            BeanUtils.copyProperties(this, properties);
        } catch (Exception e) {
            throw new RuntimeException("failed to init ice properties", e);
        }
        return properties;
    }
}
