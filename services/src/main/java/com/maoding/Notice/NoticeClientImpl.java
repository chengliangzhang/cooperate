package com.maoding.Notice;

import com.maoding.Base.BaseLocalService;
import com.maoding.Notice.zeroc.MessageDTO;
import com.maoding.Notice.zeroc.NoticeClient;
import com.maoding.Notice.zeroc.NoticeClientPrx;
import com.zeroc.Ice.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/5 23:31
 * 描    述 :
 */
@Service("noticeClient")
public class NoticeClientImpl extends BaseLocalService<NoticeClientPrx> implements NoticeClientPrx,NoticeClient{

    private String userId;

    public NoticeClientImpl(String userId){
        this.userId = userId;
    }
    public NoticeClientImpl(){
        this("null");
    }

    @Override
    public void notice(MessageDTO msg, Current current) {
        log.info(userId + " got message:\"" + msg.getTitle() + ":" + msg.getContent() + "\" from " + msg.getUserId());
    }

//    @Override
//    public void notice(MessageDTO msg){
//        notice(msg,(Current) null);
//    }
//
//    @Override
//    public void notice(MessageDTO msg, Map<String, String> context) {
//        notice(msg,(Current) null);
//    }
//
//    @Override
//    public CompletableFuture<Void> noticeAsync(MessageDTO msg) {
//        notice(msg,(Current) null);
//        return null;
//    }

    @Override
    public CompletableFuture<Void> noticeAsync(MessageDTO msg, Map<String, String> context) {
        return null;
    }

    public static NoticeClientPrx createNewClient(String locatorIp, String userId){
        String locatorConfig = "IceGrid/Locator:tcp -h " + locatorIp + " -p 4061";
        Communicator communicator = Util.initialize(new String[]{"--Ice.Default.Locator=" + locatorConfig});
        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("ClientDemo","tcp:udp");
        assert (adapter != null);
        Identity uid = new Identity("userId",userId);
        NoticeClientImpl client = new NoticeClientImpl(userId);
        ObjectPrx proxy = adapter.add(client, uid);
        adapter.activate();
        return NoticeClientPrx.uncheckedCast(proxy);
    }
}
