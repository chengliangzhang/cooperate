package com.maoding.User.Dao;

import com.maoding.Common.zeroc.IdNameDTO;
import com.maoding.User.zeroc.ProjectRoleDTO;
import com.maoding.User.zeroc.QueryMemberDTO;
import com.maoding.User.zeroc.QueryWebRoleDTO;
import com.maoding.User.zeroc.WebRoleDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/16 14:17
 * 描    述 :
 */
@Repository
public interface RoleDao {
    List<WebRoleDTO> listWebRole(@NotNull QueryWebRoleDTO query);

    List<ProjectRoleDTO> listProjectRoleByProjectId(@Param("projectId")  String projectId);
    List<IdNameDTO> listProject(@Param("userId") String userId);
    List<IdNameDTO> listTask(@Param("userId") String userId);
    List<IdNameDTO> listCompany(@Param("userId") String userId);
    List<IdNameDTO> listMember(QueryMemberDTO query);
}
