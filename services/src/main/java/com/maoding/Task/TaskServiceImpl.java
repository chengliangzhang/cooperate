package com.maoding.Task;

import com.maoding.Base.BaseLocalService;
import com.maoding.Task.zeroc.QueryTaskDTO;
import com.maoding.Task.zeroc.TaskDTO;
import com.maoding.Task.zeroc.TaskService;
import com.maoding.Task.zeroc.TaskServicePrx;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/2/7 14:47
 * 描    述 :
 */
@Service("taskService")
public class TaskServiceImpl extends BaseLocalService<TaskServicePrx> implements TaskService,TaskServicePrx {
    @Override
    public List<TaskDTO> listTask(QueryTaskDTO query, Current current) {
        return null;
    }
}
