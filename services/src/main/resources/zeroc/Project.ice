#pragma once
#include <Common.ice>
#include <ProjectData.ice>

[["java:package:com.maoding.Project"]]
module zeroc {
    interface ProjectService {
        ProjectList listProject(QueryProjectDTO query); //查询任务
        ProjectDTO getProjectInfoById(string id);
    };
};