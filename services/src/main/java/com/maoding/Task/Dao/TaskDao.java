package com.maoding.Task.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Task.Dto.TaskDTO;
import com.maoding.Task.Entity.TaskEntity;
import org.springframework.stereotype.Repository;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/3 15:11
 * 描    述 :
 */
@Repository
public interface TaskDao extends BaseDao<TaskEntity> {
    TaskDTO getFullInfoById(String id);
}
