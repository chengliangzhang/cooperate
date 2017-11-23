package com.maoding.Storage.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Storage.Entity.StorageEntity;
import com.maoding.Storage.zeroc.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/20 11:37
 * 描    述 :
 */
@Repository
public interface StorageDao extends BaseDao<StorageEntity> {
    /** 查找文件服务器上的文件使用记录 */
    List<CooperateFileDTO> listFileByScopeAndKey(CooperationQueryDTO query);
    /** 获取本目录信息 */
    CooperateDirNodeDTO getDirNodeInfo(String nodeId);
    /** 获取本文件信息 */
    FileNodeDTO getFileNodeInfo(String nodeId);
    /** 查找协同子目录 */
    List<CooperateDirNodeDTO> listSubDir(CooperationQueryDTO query);
    /** 查找目录内协同文件 */
    List<CooperateFileDTO> listMainFile(List<String> dirIdList);
    /** 查找协同文件的参考文件 */
    List<FileNodeDTO> listRelatedFile(String nodeId);
    /** 查找协同文件版本 */
    List<FileVersionDTO> listVersion(String nodeId);
}
