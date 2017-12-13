package com.maoding;

import com.maoding.Config.IceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/13 12:06
 * 描    述 :
 */
@Component
public class IceRunner {
    /** 日志对象 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IceConfig iceConfig;

    public void run(String[] args) {
        String configName = iceConfig.getConfigFileName();

        if (configName != null) {
            List<String> params = new ArrayList<>();
            params.addAll(Arrays.asList(args));
            params.add("--Ice.Config=" + configName);
            log.info("启动IceBox服务，配置文件：" + configName);
            com.zeroc.IceBox.Server.main(params.toArray(new String[params.size()]));
        }
    }
}
