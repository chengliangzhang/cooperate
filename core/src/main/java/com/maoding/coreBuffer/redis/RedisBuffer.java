package com.maoding.coreBuffer.redis;

import com.maoding.coreBuffer.CoreBuffer;
import com.maoding.coreUtils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/6/1 11:25
 * 描    述 :
 */
public class RedisBuffer implements CoreBuffer {

    @Autowired
    private RedissonClient redissonClient;

    private String address;
    private String password;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean(name = "redisClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        Config config = new Config();
        SingleServerConfig ssc = config.useSingleServer();
        ssc.setAddress(address);
        if (!StringUtils.isEmpty(password)) {
            ssc.setPassword(password);
        }
        return Redisson.create(config);
    }

    @Override
    public <T> void replaceInList(T element, long aliveTime) {
        RReadWriteLock lock = redissonClient.getReadWriteLock("RedisBuffer");
        RLock r = lock.writeLock();
        r.lock(5, TimeUnit.SECONDS);

        RMap<String,Object> map = redissonClient.getMap("RedisBuffer");
        Object a = map.get("aa");

        r.unlock();

    }

    @Override
    public <T> void removeFormList(T element) {

    }

    @Override
    public <T> void setList(String key, List<? extends T> list, long aliveTime) {

    }

    @Override
    public <T> T getList(String key, long aliveTime) {
        return null;
    }
}
