package com.maoding.Notice.Config;

import com.maoding.CoreNotice.ActiveMQ.ActiveMQClient;
import com.maoding.CoreNotice.CoreNoticeService;
import com.maoding.Utils.SpringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 12:09
 * 描    述 :
 */
@Component
@ConfigurationProperties(prefix = "notice")
public class NoticeConfig {
    private static CoreNoticeService activeMQ = null;

    private Short type;

    public void setType(Short type) {
        this.type = type;
    }

    public Short getType() {
        return type;
    }

    public CoreNoticeService getActiveMQ(){
        if (activeMQ == null){
            activeMQ = SpringUtils.getBean(ActiveMQClient.class);
        }
        return activeMQ;
    }
}
