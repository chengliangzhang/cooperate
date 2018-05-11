package com.maoding.User.Dao;

import com.maoding.Base.CoreDao;
import com.maoding.User.Entity.RoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/4/15 8:57
 * 描    述 :
 */
@Repository
public interface RoleListDao extends CoreDao<RoleEntity> {
    void setWebRoleStatus(@Param("webRoleId") String webRoleId,@Param("statusId") String statusId);
}
