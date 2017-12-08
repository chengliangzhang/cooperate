#pragma once
#include <Common.ice>
#include <User.ice>

[["java:package:com.maoding.Project"]]
module zeroc {
    ["java:getset"]
    struct ProjectDTO {
        string id; //唯一标识
        string name; //项目名称
    };
    ["java:type:java.util.ArrayList<ProjectDTO>"] sequence<ProjectDTO> ProjectList;

    interface ProjectService {
        ProjectList listProjectByAccount(AccountDTO account); //获取指定账号所参与项目
        ProjectList listProjectForCurrent(); //获取当前账号所参与项目
    };
};