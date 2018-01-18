package com.maoding.User.Dao;

import com.maoding.User.zeroc.ProjectRoleDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/16 14:17
 * 描    述 :
 */
@Repository
public interface RoleDao {
    List<ProjectRoleDTO> listProjectRoleByProjectId(@Param("projectId")  String projectId);
}
