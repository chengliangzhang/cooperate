package com.maoding.common.servicePrx;

import com.maoding.common.zeroc.*;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.SpringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/5/29 15:19
 * 描    述 :
 */
public class ConstServicePrxImpl extends CoreRemoteService<ConstServicePrx> implements ConstServicePrx {
    private static ConstServicePrx lastPrx = null;
    private static ConstService localService = null;

    private ConstService getLocalService(){
        if (localService == null) {
            localService = SpringUtils.getBean(ConstService.class);
        }
        return localService;
    }

    /** 同步方式获取业务接口代理对象 */
    public static ConstServicePrx getInstance() {
        if (lastPrx == null){
            lastPrx = new ConstServicePrxImpl();
        }
        return lastPrx;
    }

    @Override
    public String getTitle(@NotNull ConstQuery query) {
        return getLocalService().getTitle(query,null);
    }

    @Override
    public String getExtra(@NotNull ConstQuery query) {
        return getLocalService().getExtra(query,null);
    }

    @Override
    public VersionSimpleDTO getNewestVersion(@NotNull VersionSimpleQuery query) {
        return getLocalService().getNewestVersion(query,null);
    }

    @Override
    public List<VersionDTO> listVersion(VersionQuery query) {
        return getLocalService().listVersion(query,null);
    }
}
