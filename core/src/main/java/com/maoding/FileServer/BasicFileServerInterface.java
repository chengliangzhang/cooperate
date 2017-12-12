package com.maoding.FileServer;

import com.maoding.Bean.*;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 9:58
 * 描    述 :
 */
public interface BasicFileServerInterface {
    /** 获取通过http方式上传文件数据库时的需要设置的部分参数 */
    BasicFileRequestDTO getUploadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting);
    default BasicFileRequestDTO getUploadRequest(BasicFileDTO src,Integer mode){
        return getUploadRequest(src,mode,null);
    }
    default BasicFileRequestDTO getUploadRequest(BasicFileDTO src){
        return getUploadRequest(src,null);
    }
    /** 获取通过http方式下载文件数据库时的需要设置的部分参数 */
    BasicFileRequestDTO getDownloadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting);
    default BasicFileRequestDTO getDownloadRequest(BasicFileDTO src, Integer mode){
        return getDownloadRequest(src,mode,null);
    }
    default BasicFileRequestDTO getDownloadRequest(BasicFileDTO src){
        return getDownloadRequest(src,null);
    }

    /** 上传文件分片内容 */
    default BasicUploadResultDTO multipartUpload(BasicUploadRequestDTO request) {return null ;}
    /** 上传文件分片内容 */
    BasicUploadResultDTO upload(BasicUploadRequestDTO request);
    /** 下载文件分片内容 */
    BasicDownloadResultDTO download(BasicDownloadRequestDTO request);
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
    /** 通告文件服务器上传完毕 */
    default void finishUpload(BasicFileRequestDTO request){}

    default int writeFile(BasicFileMultipartDTO data){return 0;};
    default BasicFileMultipartDTO readFile(BasicFileDTO file, long pos, int size){return null;}
}
