package com.maoding.Notice.Config;

import com.maoding.Notice.CoreNotice;
import com.maoding.Utils.SpringContextUtils;
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
    private CoreNotice stormNotice;

    private Short type;

    public void setType(Short type) {
        this.type = type;
    }

    public Short getType() {
        return type;
    }

    public CoreNotice getNoticeServer(){
        if (CoreNotice.NOTICE_SERVER_TYPE_STORM.equals(type)) {
            if (stormNotice == null) stormNotice = SpringContextUtils.getBean("stormNotice",CoreNotice.class);
            return stormNotice;
        } else {
            return null;
        }
    }
}
