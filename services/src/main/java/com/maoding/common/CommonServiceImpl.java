package com.maoding.common;

import com.maoding.common.config.IceConfig;
import com.maoding.common.servicePrx.*;
import com.maoding.common.zeroc.*;
import com.maoding.coreBase.CoreLocalService;
import com.maoding.coreUtils.ObjectUtils;
import com.maoding.coreUtils.StringUtils;
import com.maoding.coreUtils.TraceUtils;
import com.maoding.fileServer.zeroc.FileServicePrx;
import com.maoding.notice.zeroc.NoticeServicePrx;
import com.maoding.storage.zeroc.StorageServicePrx;
import com.maoding.user.zeroc.UserServicePrx;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/5/29 11:28
 * 描    述 :
 */
@Service("commonService")
public class CommonServiceImpl extends CoreLocalService implements CommonService{

    @Autowired
    private IceConfig iceConfig;

    @Override
    public FileServicePrx getDefaultFileService(Current current) {
        String config = getIceServiceConfig("file",current);
        String s = StringUtils.left(config,StringUtils.SPLIT_CONTENT);
        String c = StringUtils.right(config,StringUtils.SPLIT_CONTENT);
        return getFileService(s,c,current);
    }

    @Override
    public UserServicePrx getDefaultUserService(Current current) {
        return null;
    }

    @Override
    public ConstServicePrx getDefaultConstService(Current current) {
        return ConstServicePrxImpl.getInstance();
    }

    @Override
    public void updateService(Current current) {
        //获取当前版本
        //获取最新版本
        VersionDTO serviceVersion = getNewestService(current);
        //如果最新版本比当前版本高
            String url = serviceVersion.getUpdateUrl();
            //获取文件服务器链接
    //        FileServicePrx fileService = getFileService(url,iceConfig.getConfig(),current);
            //获取文件key
            //从文件服务器读取文件并保存到本地
            //调用升级脚本
            //退出应用
    }

    private String getCurrentVersion(Current current) {
        return "V1.0";
    }

    private VersionDTO getNewestService(Current current) {
        VersionQuery serviceQuery = new VersionQuery();
        serviceQuery.setSvnRepo("maoding-services");
        serviceQuery.setLimitRows("1");
        List<VersionDTO> serviceList = getConstService().listVersion(serviceQuery);
        return ObjectUtils.getFirst(serviceList);
    }

    @Override
    public String getNewestClient(Current current) {
        long t = TraceUtils.enter(log,"getNewestClient");

        //获取当前服务器版本
        VersionQuery serviceQuery = new VersionQuery();
        serviceQuery.setSvnRepo("maoding-services");
        serviceQuery.setLimitRows("1");
        List<VersionDTO> serviceList = getConstService().listVersion(serviceQuery);
        VersionDTO service = ObjectUtils.getFirst(serviceList);

        //获取客户端与当前服务器版本兼容的最新版本
        VersionQuery clientQuery = new VersionQuery();
        clientQuery.setSvnRepo("app");
        clientQuery.setLimitRows("1");
        if (service != null){
            clientQuery.setServiceSvnVersion(service.getSvnVersion());
        }
        List<VersionDTO> clientList = getConstService().listVersion(clientQuery);
        VersionDTO client = ObjectUtils.getFirst(clientList);

        TraceUtils.exit(log,"getNewestVersion",t,client);
        return (client != null) ? client.getVersionName() : "";
    }

    @Override
    public FileServicePrx getFileService(String service, String config, Current current) {
        return FileServerPrxImpl.getInstance(service,config);
    }

    @Override
    public StorageServicePrx getStorageService(String service, String config, Current current) {
        return StorageServicePrxImpl.getInstance(service,config);
    }

    @Override
    public NoticeServicePrx getNoticeService(String service, String config, Current current) {
        return NoticeServicePrxImpl.getInstance(service,config);
    }

    @Override
    public UserServicePrx getUserService(String service, String config, Current current) {
        return UserServicePrxImpl.getInstance(service,config);
    }

    private String getIceServiceConfig(@NotNull String service,Current current) {
        String title = service;
        String identify = iceConfig.getIdentify();
        if (StringUtils.isNotEmpty(identify)){
            title += "-" + identify;
        }
        ConstQuery query = new ConstQuery();
        query.setClassicName("ice");
        query.setTitle(title);
        String config = getConstService().getExtra(query);
        if (StringUtils.isEmpty(config)){
            config = iceConfig.getProperty(service);
        }
        return config;
    }

    private ConstServicePrx getConstService() {
        return getDefaultConstService(null);
    }

    private FileServicePrx getFileService() {
        return getDefaultFileService(null);
    }
}
