package com.maoding.common.config;

import com.maoding.coreBuffer.CoreBuffer;
import com.maoding.coreBuffer.redis.RedisBuffer;
import com.maoding.coreUtils.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/6/6 20:25
 * 描    述 :
 * @author idccapp25
 */
@EnableAutoConfiguration
@Component
@Configuration
@ConfigurationProperties(prefix = "buffer")
public class BufferConfig extends RedisBuffer {
    private static final boolean USE_REDIS = true;

    private Boolean useRedis;
    private String address;
    private String password;
    private String name;
    private Integer lockTime;

    private CoreBuffer buffer = null;

    public Boolean getUseRedis() {
        return (useRedis == null) ? USE_REDIS : useRedis;
    }

    public void setUseRedis(Boolean useRedis) {
        this.useRedis = useRedis;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getLockTime() {
        return lockTime;
    }

    @Override
    public void setLockTime(Integer lockTime) {
        this.lockTime = lockTime;
    }

    public CoreBuffer getBuffer(){
        if (buffer == null){
            if (getUseRedis()) {
                buffer = new RedisBuffer();
                BeanUtils.copyProperties(this,buffer);
            }
        }
        return buffer;
    }
}
