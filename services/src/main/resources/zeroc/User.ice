#pragma once
#include <Common.ice>
#include <UserData.ice>

[["java:package:com.maoding.User"]]
module zeroc {
    interface UserService {
        bool login(LoginDTO loginInfo); //登录
        AccountDTO getCurrent(); //获取当前账号信息
        ProjectRoleList listProjectRoleByProjectId(string projectId); //获取项目的参与角色列表
    };
};