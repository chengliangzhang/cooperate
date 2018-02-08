package com.maoding.Task.Dao;

import com.maoding.Task.zeroc.QueryTaskDTO;
import com.maoding.Task.zeroc.TaskDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 15:11
 * 描    述 :
 */
@Repository
public interface TaskDao {
    List<TaskDTO> listTask(QueryTaskDTO query);
}
