#pragma once
#include <Common.ice>

[["java:package:com.maoding.User"]]
module zeroc {
    sequence<string> OrganizationList;
    sequence<string> ProjectList;
    sequence<string> TaskList;

    ["java:getset"]
    struct UserRelatedDTO {
        OrganizationList organizationList;
        ProjectList projectList;
        TaskList taskList;
    };

    interface UserService {
        UserRelatedDTO getUserRelatedInfo(string userId);
    };
};