package com.maoding.User;

import com.maoding.Base.BaseLocalService;
import com.maoding.Bean.ApiResponse;
import com.maoding.User.Config.WebServiceConfig;
import com.maoding.User.zeroc.*;
import com.maoding.Utils.*;
import com.zeroc.Ice.Current;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/19 18:34
 * 描    述 :
 */
@Service("userService")
public class UserServiceImpl extends BaseLocalService<UserServicePrx> implements UserService,UserServicePrx {

    private CloseableHttpClient client = null;

    @Autowired
    private WebServiceConfig webServiceConfig;

    /** 同步方式获取业务接口代理对象 */
    public static UserServicePrx getInstance(String adapterName) {
        UserServiceImpl prx = new UserServiceImpl();
        return prx.getServicePrx("UserService",adapterName, UserServicePrx.class,_UserServicePrxI.class);
    }
    public static UserServicePrx getInstance(){
        return getInstance(null);
    }

    @Override
    public boolean login(LoginDTO loginInfo, Current current) {
        assert (loginInfo != null);
        final String LOGIN_URL = "http://172.16.6.73/maoding/iWork/sys/login";
        final String PARAMS_TYPE = "application/json";

        if (StringUtils.isEmpty(loginInfo.getCellphone())) loginInfo.setCellphone(loginInfo.getAccountId());
        assert (!StringUtils.isEmpty(loginInfo.getCellphone()));
        assert (!StringUtils.isEmpty(loginInfo.getPassword()));

        if (client == null) client = HttpClients.createDefault();
        CloseableHttpResponse response = HttpUtils.postData(client, webServiceConfig.getLoginUrl(), webServiceConfig.getLoginParamsType(), loginInfo);
        if (!HttpUtils.isResponseOK(response)) return false;
        ApiResponse result = getResult(response);
        FileUtils.close(response);
        assert (result != null);
        return (result.isSuccessful());
    }

    @Override
    public AccountDTO getCurrent(Current current) {
        final String LOGIN_URL = "http://172.16.6.73/maoding/iWork/sys/getCurrUserOfWork";
        final String USER_INFO_KEY = "userInfo";
        final String USER_ID_KEY = "id";
        final String USER_NAME_KEY = "userName";
        if (client == null) client = HttpClients.createDefault();
        CloseableHttpResponse response = HttpUtils.postData(client, webServiceConfig.getGetCurrentUrl());
        if (!HttpUtils.isResponseOK(response)) return null;
        ApiResponse result = getResult(response);
        FileUtils.close(response);
        assert (result != null);
        Map<String,Object> data = (Map<String,Object>)result.getData();
        if (data == null) return null;
        AccountDTO dto = new AccountDTO();
        if (data.containsKey(webServiceConfig.getGetCurrentInfoKey())) data = (Map<String,Object>)data.get(webServiceConfig.getGetCurrentInfoKey());
        if (data.containsKey(webServiceConfig.getGetCurrentIdKey())) dto.setId((String)data.get(webServiceConfig.getGetCurrentIdKey()));
        if (data.containsKey(webServiceConfig.getGetCurrentNameKey())) dto.setName((String)data.get(webServiceConfig.getGetCurrentNameKey()));
        return dto;
    }

    private ApiResponse getResult(CloseableHttpResponse response){
        ApiResponse result = null;
        try {
            result = JsonUtils.json2Obj(EntityUtils.toString(response.getEntity()),ApiResponse.class);
        } catch (IOException e) {
            ExceptionUtils.logError(log,e);
        }
        return result;
    }

    @Override
    public boolean setOrganization(String organizationId, Current current) {
        return false;
    }

    @Override
    public boolean setDuty(String dutyId, Current current) {
        return false;
    }

    @Override
    public List<DutyDTO> listDutyByUserId(String userId, Current current) {
        return null;
    }

    @Override
    public List<DutyDTO> listDutyForCurrent(Current current) {
        return null;
    }

}
