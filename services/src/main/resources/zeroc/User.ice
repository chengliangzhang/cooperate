#pragma once
#include <data/UserData.ice>

[["java:package:com.maoding.user"]]
module zeroc {
    interface UserService {
        void setWebRoleStatus(WebRoleDTO webRole,string statusId); //设置角色的工作状态
        WebRoleList listWebRole(QueryWebRoleDTO query); //列出用户在生产任务中担任的角色
        RoleList listRole(QueryRoleDTO query); //列出用户担任的角色

        bool login(LoginDTO loginInfo); //登录
        AccountDTO getCurrent(); //获取当前账号信息
        ProjectRoleList listProjectRoleByProjectId(string projectId); //获取项目的参与角色列表
        UserJoinDTO listUserJoin(); //获取当前用户参与的项目、任务和所属公司
        UserJoinDTO listUserJoinForAccount(AccountDTO account); //获取指定用户参与的项目、任务和所属公司
        IdNameList listMember(QueryMemberDTO query); //获取指定项目、任务或组织的所有成员编号姓名
    };
};