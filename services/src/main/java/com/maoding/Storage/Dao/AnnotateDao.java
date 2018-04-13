package com.maoding.Storage.Dao;

import com.maoding.Storage.zeroc.AnnotateDTO;
import com.maoding.Storage.zeroc.QueryAnnotateDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/4/12 18:09
 * 描    述 :
 */
@Repository
public interface AnnotateDao {
    List<AnnotateDTO> listAnnotate(QueryAnnotateDTO query);
}
