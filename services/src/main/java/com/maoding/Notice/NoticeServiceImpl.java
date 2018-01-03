package com.maoding.Notice;

import com.maoding.Base.BaseLocalService;
import com.maoding.Bean.CoreMessageDTO;
import com.maoding.Bean.CoreReceiverDTO;
import com.maoding.Notice.Config.NoticeConfig;
import com.maoding.Notice.zeroc.*;
import com.maoding.Utils.BeanUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 11:31
 * 描    述 :
 */
@Service("noticeService")
public class NoticeServiceImpl extends BaseLocalService<NoticeServicePrx> implements NoticeService, NoticeServicePrx {

    @Autowired
    private NoticeConfig noticeConfig;
    private CoreNotice coreNotice;

    /**
     * 同步方式获取业务接口代理对象
     */
    public static NoticeServicePrx getInstance(String adapterName) {
        NoticeServiceImpl prx = new NoticeServiceImpl();
        return prx.getServicePrx("NoticeService", adapterName, NoticeServicePrx.class, _NoticeServicePrxI.class);
    }

    public static NoticeServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public void sendMessage(MessageDTO message, List<ReceiverDTO> receiverList, Current current) {
        CoreMessageDTO coreMessage = BeanUtils.createFrom(message,CoreMessageDTO.class);
        for (ReceiverDTO receiver : receiverList){
            CoreReceiverDTO coreReceiver = BeanUtils.createCleanFrom(receiver,CoreReceiverDTO.class);
            if (coreNotice == null) coreNotice = noticeConfig.getNoticeServer();
            assert (coreNotice != null);
            coreNotice.sendMessage(coreMessage,coreReceiver);
        }
    }

    @Override
    public void createTopic(String topic, Current current) {
        if (coreNotice == null) coreNotice = noticeConfig.getNoticeServer();
        assert (coreNotice != null);
        coreNotice.createTopic(topic);
    }

    @Override
    public void subscribeTopic(String topic, Current current) {

    }
}
