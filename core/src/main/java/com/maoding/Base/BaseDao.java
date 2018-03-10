package com.maoding.Base;

import com.maoding.CoreMybatis.CustomMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.Date;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 : 数据库访问层接口（自带通用方法）
 */
public interface BaseDao<T extends BaseEntity> extends Mapper<T>, MySqlMapper<T>, CustomMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
    default T selectById(String id){
        return selectByPrimaryKey(id);
    }

    default int insert(T entity){
        return insertSelective(entity);
    }

    default int insertList(List<T> entityList){
        int n = 0;
        for (T entity : entityList){
            n += insertSelective(entity);
        }
        return n;
    }

    default int update(T entity) {
        return updateByPrimaryKeySelective(entity);
    }
    
    default int updateExact(T entity) {
        return updateByPrimaryKey(entity); 
    }

    default int updateById(T entity){
        return updateById(entity, entity.getId(), null, null);
    }

    default int updateById(T entity, String id){
        return updateById(entity, id, null, null);
    }

    default int updateById(T entity, String id, String lastModifyUserId){
        return updateById(entity, id, lastModifyUserId, null);
    }

    default int updateById(T entity, List<String> idList){
        return updateByIdList(entity, idList, null, null);
    }

    default int updateById(T entity, List<String> idList, String lastModifyUserId){
        return updateByIdList(entity, idList, lastModifyUserId, null);
    }

    default int updateById(T entity, List<String> idList, String lastModifyUserId, Date lastModifyTime){
        return updateByIdList(entity, idList, lastModifyUserId, lastModifyTime);
    }

    default int updateExactById(T entity){
        return updateExactById(entity, entity.getId(), null, null);
    }

    default int updateExactById(T entity, String id){
        return updateExactById(entity, id, null, null);
    }

    default int updateExactById(T entity, String id, String lastModifyUserId){
        return updateExactById(entity, id, lastModifyUserId, null);
    }

    default int updateExactById(T entity, List<String> idList){
        return updateExactByIdList(entity, idList, null, null);
    }

    default int updateExactById(T entity, List<String> idList, String lastModifyUserId){
        return updateExactByIdList(entity, idList, lastModifyUserId, null);
    }

    default int updateExactById(T entity, List<String> idList, String lastModifyUserId, Date lastModifyTime){
        return updateExactByIdList(entity, idList, lastModifyUserId, lastModifyTime);
    }

    default int fakeDeleteById(String id){
        return fakeDeleteById(id,null,null);
    }
    
    default int fakeDeleteById(String id, String lastModifyUserId){
        return fakeDeleteById(id,lastModifyUserId,null);
    }
    
    default int fakeDeleteById(List<String> idList){
        return fakeDeleteByIdList(idList,null,null);
    }
    
    default int fakeDeleteById(List<String> idList, String lastModifyUserId){
        return fakeDeleteByIdList(idList,lastModifyUserId,null);
    }

    default int fakeDeleteById(List<String> idList, String lastModifyUserId, Date lastModifyTime){
        return fakeDeleteByIdList(idList,lastModifyUserId,lastModifyTime);
    }
}
