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
    };

    ["java:getset","clr:property"]
    struct RoleDTO {
        string id; //唯一编号
        string name; //职责角色名
        string companyId; //职责所属组织id
        string companyName; //职责所属组织名称
        string userId; //用户id
        string userName; //用户名称
    };
    ["java:type:java.util.ArrayList<RoleDTO>"] sequence<RoleDTO> RoleList;

    ["java:getset","clr:property"]
    struct UserDTO {
        string id; //唯一编号
        string name; //用户名
    };
    ["java:type:java.util.ArrayList<UserDTO>"] sequence<UserDTO> UserList;

    ["java:getset","clr:property"]
    struct TaskRoleDTO { //角色定义
        string id; //任务id
        string name; //任务名称
        IdNameList taskRoleList; //用户在任务中承担的角色列表
    };
    ["java:type:java.util.ArrayList<TaskRoleDTO>"] sequence<TaskRoleDTO> TaskRoleList;

    ["java:getset","clr:property"]
    struct ProjectRoleDTO {
        string userId; //用户编号
        string userName; //用户名称
        IdNameList projectRoleList; //用户在项目中承担的角色列表
        TaskRoleList taskList; //用户所参与的任务
    };
    ["java:type:java.util.ArrayList<ProjectRoleDTO>"] sequence<ProjectRoleDTO> ProjectRoleList;

    ["java:getset","clr:property"]
    struct UserJoinDTO {
        IdNameList projectList; //用户参加的项目
        IdNameList taskList; //用户参加的任务
        IdNameList companyList; //用户所属的组织
    };
    ["java:type:java.util.ArrayList<UserJoinDTO>"] sequence<UserJoinDTO> UserJoinList;

    ["java:getset","clr:property"]
    struct QueryMemberDTO {
        string userTypeIdString; //参与者角色类型id，可以是用","分隔的多个id
        string projectId; //参与项目id
        string taskId; //参与任务id
        string companyId; //参与组织id
    };
};