package com.maoding.common;

import com.maoding.common.remoteService.RemoteFileServerPrx;
import com.maoding.common.remoteService.RemoteNoticeServicePrx;
import com.maoding.common.remoteService.RemoteStorageServicePrx;
import com.maoding.common.remoteService.RemoteUserServicePrx;
import com.maoding.common.zeroc.CommonService;
import com.maoding.coreBase.CoreLocalService;
import com.maoding.fileServer.zeroc.FileServicePrx;
import com.maoding.notice.zeroc.NoticeServicePrx;
import com.maoding.storage.zeroc.StorageServicePrx;
import com.maoding.user.zeroc.UserServicePrx;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/5/29 11:28
 * 描    述 :
 */
@Service("commonService")
public class CommonServiceImpl extends CoreLocalService implements CommonService{
    @Override
    public FileServicePrx getFileService(String service, String config, Current current) {
        return RemoteFileServerPrx.getInstance(service,config);
    }

    @Override
    public StorageServicePrx getStorageService(String service, String config, Current current) {
        return RemoteStorageServicePrx.getInstance(service,config);
    }

    @Override
    public NoticeServicePrx getNoticeService(String service, String config, Current current) {
        return RemoteNoticeServicePrx.getInstance(service,config);
    }

    @Override
    public UserServicePrx getUserService(String service, String config, Current current) {
        return RemoteUserServicePrx.getInstance(service,config);
    }
}
