package com.maoding.Storage.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Storage.Entity.AttachmentEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/4/12 21:30
 * 描    述 :
 */
@Repository
public interface AttachmentListDao extends BaseDao<AttachmentEntity> {
    void deleteAttachment(@Param("annotateId") String annotateId,
                          @Param("attachmentIdList") List<String> attachmentIdList,
                          @Param("lastModifyUserId") String lastModifyUserId);
}
