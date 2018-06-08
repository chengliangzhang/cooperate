package com.maoding.common.servicePrx;

import com.maoding.common.zeroc.IdNameDTO;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.SpringUtils;
import com.maoding.coreUtils.StringUtils;
import com.maoding.user.zeroc.*;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 11:54
 * 描    述 :
 */
public class UserServicePrxImpl extends CoreRemoteService<UserServicePrx> implements UserServicePrx {

    private static UserServicePrx lastPrx = null;
    private static UserService userService = null;
    private static String lastService = null;
    private static String lastConfig = null;

    private UserService getUserService(){
        if (userService == null) {
            userService = SpringUtils.getBean(UserService.class);
        }
        return userService;
    }

    /** 异步方式获取业务接口代理对象 */
    public static UserServicePrx getInstance(String service, String config) {
        if ((lastPrx == null) || (StringUtils.isNotSame(lastService,service)) || (StringUtils.isNotSame(lastConfig,config))){
            UserServicePrxImpl prx = new UserServicePrxImpl();
            lastPrx = prx.getServicePrx(service, config, UserServicePrx.class, _UserServicePrxI.class, prx);
            lastService = service;
            lastConfig = config;
        }
        return lastPrx;
    }

    @Override
    public void setWebRoleStatus(WebRoleDTO webRole, String statusId) {
        getUserService().setWebRoleStatus(webRole,statusId,null);
    }

    @Override
    public List<WebRoleDTO> listWebRole(QueryWebRoleDTO query) {
        return getUserService().listWebRole(query,null);
    }

    @Override
    public AccountDTO getCurrent() {
        AccountDTO account = new AccountDTO();
        account.setId("07649b3d23094f28bfce78930bf4d4ac");
        account.setName("卢沂");
        return account;
    }

    @Override
    public boolean login(LoginDTO loginInfo) {
        return true;
    }

    @Override
    public List<ProjectRoleDTO> listProjectRoleByProjectId(String projectId) {
        return getUserService().listProjectRoleByProjectId(projectId,null);
    }

    @Override
    public UserJoinDTO listUserJoin() {
        return getUserService().listUserJoin(null);
    }

    @Override
    public UserJoinDTO listUserJoinForAccount(AccountDTO account) {
        return getUserService().listUserJoinForAccount(account,null);
    }

    @Override
    public List<IdNameDTO> listMember(QueryMemberDTO query) {
        return getUserService().listMember(query,null);
    }
}
