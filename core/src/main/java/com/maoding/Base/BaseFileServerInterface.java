package com.maoding.Base;

import com.maoding.Bean.BasicFileDTO;
import com.maoding.Bean.BasicHttpRequestDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 9:58
 * 描    述 :
 */
public interface BaseFileServerInterface {
    /** 获取通过http方式上传文件数据库时的需要设置的部分参数 */
    BasicHttpRequestDTO getUploadRequestForHttp(BasicFileDTO src);
    /** 获取通过http方式下载文件数据库时的需要设置的部分参数 */
    BasicHttpRequestDTO getDownloadRequestForHttp(BasicFileDTO src);
    /** 在文件服务器上复制文件，复制到同一空间，返回复制后的文件标识 */
    String duplicateFile(BasicFileDTO src);
    /** 判断在文件服务器上是否存在指定文件 */
    Boolean isExist(BasicFileDTO src);
    /** 获取文件服务器上某一空间上的所有文件 */
    List<String> listFile(String scope);
    /** 获取文件服务器上的所有空间 */
    List<String> listScope();
    /** 在文件服务器上删除指定文件 */
    void deleteFile(BasicFileDTO src);
}
