package com.maoding.Mybatis;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 : 通用SQL语句实现
 */
public class CustomProvider extends MapperTemplate {

    public CustomProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /** 按id更新，entity字段为null不更新 */
    public String updateById(MappedStatement ms){
        return getUpdateString(ms,true);
    }

    /** 按id严格更新，entity字段为null也更新到null */
    public String updateExactById(MappedStatement ms){
        return getUpdateString(ms,false);
    }

    private String getUpdateString(MappedStatement ms, Boolean isIgnoreNull){
        final Class<?> entityClass = getEntityClass(ms); //获得列名称转换对象

        StringBuilder sql = new StringBuilder(); //sql语句
        StringBuilder sqlWhere = new StringBuilder(); //sql语句内where部分
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        //update <table>
        sql.append(SqlHelper.updateTable(entityClass,tableName(entityClass)));
        //根据各字段进行更新，id字段和输入为null的字段不更新
        sql.append("<set>");
        for (EntityColumn column : columns) {
            if (!column.isUpdatable()) continue;
            if (column.isId()) {
                sqlWhere.append("`" + column.getColumn() + "`").append("=#{id}"); //#{id}是从@Param中得到的字符串,id不会被更新
            } else {
                if (isIgnoreNull) {
                    sql.append("<if test=\"entity.").append(column.getProperty()).append("!=null\">");//entity是从@Param中得到的字符串
                }
                sql.append("`" + column.getColumn() + "`").append("=#{entity.").append(column.getProperty()).append("}").append(",");
                if (isIgnoreNull) {
                    sql.append("</if>");
                }
            }
        }
        sql.append("</set>");
        //where id=#{id}
        sql.append("<where>").append(sqlWhere.toString()).append("</where>");

        return sql.toString();
    }

    /** 按id删除 */
    public String fakeDeleteById(MappedStatement ms){
        final Class<?> entityClass = getEntityClass(ms); //获得列名称转换对象

        StringBuilder sql = new StringBuilder(); //sql语句
        StringBuilder sqlWhere = new StringBuilder(); //sql语句内where部分
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        //update <table>
        sql.append(SqlHelper.updateTable(entityClass,tableName(entityClass)));
        //set deleted=1,last_modify_user_id=#{lastModifyUserId},lastModifyTime=时间
        sql.append("<set>");
        for (EntityColumn column : columns) {
            if (!column.isUpdatable()) continue;
            if (column.isId()) {
                sqlWhere.append("`" + column.getColumn() + "`").append("=#{id}"); //#{id}是从@Param中得到的字符串
            }
            if ("deleted".equals(column.getProperty())) {
                sql.append("`" + column.getColumn() + "`").append("=1").append(",");
            }
            if ("lastModifyUserId".equals(column.getProperty())) {
                sql.append("`" + column.getColumn() + "`").append("=#{lastModifyUserId}").append(","); //#{lastModifyUserId}是从@Param中得到的字符串
            }
            if ("lastModifyTime".equals(column.getProperty())) {
                sql.append("<choose>");
                    sql.append("<when test=\"lastModifyTime!=null\">");
                        sql.append("`" + column.getColumn() + "`").append("=#{lastModifyTime}").append(",");
                    sql.append("</when>");
                    sql.append("<otherwise>");
                        sql.append("`" + column.getColumn() + "`").append("=now()").append(",");
                    sql.append("</otherwise>");
                sql.append("</choose>");
            }
        }
        sql.append("</set>");
        //where id=#{id}
        sql.append("<where>").append(sqlWhere.toString()).append("</where>");

        return sql.toString();
    }
}
