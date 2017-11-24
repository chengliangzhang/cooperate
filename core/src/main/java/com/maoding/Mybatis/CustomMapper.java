package com.maoding.Mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.Date;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 : 通用SQL语句接口
 */
public interface CustomMapper<T> {
    /** 按id更新，entity字段为null不更新 */
    @UpdateProvider(type = CustomProvider.class, method = "dynamicSQL")
    int updateById(@Param("entity") T entity, @Param("id") String id);

    /** 按id严格更新，entity字段为null也更新到null */
    @UpdateProvider(type = CustomProvider.class, method = "dynamicSQL")
    int updateExactById(@Param("entity") T entity, @Param("id") String id);

    /** 按id删除 */
    @UpdateProvider(type = CustomProvider.class, method = "dynamicSQL")
    int fakeDeleteById(@Param("idList") List<String> idList, @Param("lastModifyUserId") String lastModifyUserId, @Param("lastModifyTime") Date lastModifyTime);
}
