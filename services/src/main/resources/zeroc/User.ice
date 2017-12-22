#pragma once
#include <Common.ice>

[["java:package:com.maoding.User"]]
module zeroc {
    ["java:getset","clr:property"]
    struct LoginDTO { //登录信息
        string accountId; //用户名
        string encryptPassword; //密码（已进行过加密算法）
        bool isRemember; //是否记住

        //保存兼容性
        string password; //密码（未加密）
        string cellphone; //登录账号
    };

    ["java:getset","clr:property"]
    struct AccountDTO {
        string id; //唯一编号
        string name; //用户名
        string defaultOrganizationId; //默认所在组织id
        string organizationId; //当前所在组织id
        string dutyId; //当前所用职责id
    };

    ["java:getset","clr:property"]
    struct DutyDTO {
        string id; //唯一编号
        string name; //职责名
        string organizationId; //职责所属组织id
        string organizationName; //职责所属组织名称
        string userId; //用户id
    };
    ["java:type:java.util.ArrayList<DutyDTO>"] sequence<DutyDTO> DutyList;

    ["java:getset","clr:property"]
    struct UserDTO {
        string id; //唯一编号
        string userName; //用户名
    };
    ["java:type:java.util.ArrayList<UserDTO>"] sequence<UserDTO> UserList;

    interface UserService {
        bool login(LoginDTO loginInfo); //登录
        AccountDTO getCurrent(); //获取当前账号信息

        bool setOrganization(string organizationId); //设置用户当前所在组织
        bool setDuty(string dutyId); //设置用户当前所用职责
        DutyList listDutyByUserId(string userId); //列出指定用户可用的职责信息
        DutyList listDutyForCurrent(); //列出当前用户可用的职责信息
    };
};