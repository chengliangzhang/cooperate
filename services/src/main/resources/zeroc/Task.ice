#pragma once
#include <Common.ice>
#include <TaskData.ice>

[["java:package:com.maoding.Task"]]
module zeroc {
    interface TaskService {
        TaskList listTask(QueryTaskDTO query); //查询任务
    };
};