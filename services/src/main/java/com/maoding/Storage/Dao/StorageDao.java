package com.maoding.Storage.Dao;

import com.maoding.Base.BaseDao;
import com.maoding.Storage.Dto.QueryByPidAndNameDTO;
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
    CooperateDirNodeDTO getDirNodeInfo(CooperationQueryDTO query);
    CooperateDirNodeDTO getDirNodeInfoByNodeId(String nodeId);
    /** 获取本文件信息 */
    FileNodeDTO getFileNodeInfo(CooperationQueryDTO query);
    FileNodeDTO getFileNodeInfoByNodeId(String nodeId);
    /** 查找协同子目录 */
    List<CooperateDirNodeDTO> listSubDir(CooperationQueryDTO query);
    /** 查找目录内协同文件 */
    List<CooperateFileDTO> listMainFile(List<String> dirIdList);
    /** 查找协同文件的参考文件 */
    List<FileNodeDTO> listRelatedFile(String nodeId);
    /** 查找协同文件版本 */
    List<FileVersionDTO> listVersion(String nodeId);
    /** 使用pid和name查找树节点记录 */
    StorageEntity selectByPIdAndName(QueryByPidAndNameDTO query);
    /** 使用全路径名查找节点简单信息 */
    SimpleNodeDTO getSimpleNodeByPath(String path);
    /** 根据全路径名判断目录是否为空 */
    Short getFirstChildTypeIdByPath(String path);
    /** 根据全路径名获取树id */
    StorageEntity selectByPath(String path);
    /** 根据全路径名获取目录详细信息 */
    CooperateDirNodeDTO getDirNodeInfoByPath(String path);
    /** 根据全路径名获取文件详细信息 */
    FileNodeDTO getFileNodeInfoByPath(String path);
    /** 根据节点编号获取一层子节点简单信息 */
    List<SimpleNodeDTO> listSubNodeByNodeId(String nodeId);
    /** 根据全路径名获取一层子节点简单信息 */
    List<SimpleNodeDTO> listSubNodeByPath(String path);
    /** 根据全路径名获取所有子节点编号 */
    List<String> listAllSubNodeIdByPath(String path);
}
