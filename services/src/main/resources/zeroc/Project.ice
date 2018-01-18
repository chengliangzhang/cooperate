#pragma once
#include <Common.ice>
#include <ProjectData.ice>

[["java:package:com.maoding.Project"]]
module zeroc {
    interface ProjectService {
        ProjectDTO getProjectInfoById(string id);
    };
};