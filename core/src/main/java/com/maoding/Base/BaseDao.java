package com.maoding.Base;

import com.maoding.Mybatis.CustomMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 : 数据库访问层接口（自带通用方法）
 */
public interface BaseDao<T extends BaseEntity> extends Mapper<T>, MySqlMapper<T>, CustomMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
//    default T selectById(String id){
//        return selectByPrimaryKey(id);
//    }
}
