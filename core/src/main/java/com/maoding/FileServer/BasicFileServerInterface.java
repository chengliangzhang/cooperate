package com.maoding.FileServer;

import com.maoding.Bean.*;
import com.maoding.Const.FileServerConst;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 9:58
 * 描    述 :
 */
public interface BasicFileServerInterface {
    /** 获取通过http方式上传文件数据库时的需要设置的部分参数 */
    @Deprecated
    BasicFileRequestDTO getUploadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting);
    @Deprecated
    default BasicFileRequestDTO getUploadRequest(BasicFileDTO src,Integer mode){
        return getUploadRequest(src,mode,null);
    }
    @Deprecated
    default BasicFileRequestDTO getUploadRequest(BasicFileDTO src){
        return getUploadRequest(src,null);
    }
    /** 获取通过http方式下载文件数据库时的需要设置的部分参数 */
    @Deprecated
    BasicFileRequestDTO getDownloadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting);
    @Deprecated
    default BasicFileRequestDTO getDownloadRequest(BasicFileDTO src, Integer mode){
        return getDownloadRequest(src,mode,null);
    }
    @Deprecated
    default BasicFileRequestDTO getDownloadRequest(BasicFileDTO src){
        return getDownloadRequest(src,null);
    }

    /** 上传文件分片内容 */
    @Deprecated
    default BasicUploadResultDTO multipartUpload(BasicUploadRequestDTO request) {return null ;}
    /** 上传文件分片内容 */
    @Deprecated
    BasicUploadResultDTO upload(BasicUploadRequestDTO request);
    /** 下载文件分片内容 */
    @Deprecated
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
    @Deprecated
    default void finishUpload(BasicFileRequestDTO request){}

    default int writeFile(BasicFileMultipartDTO data){return 0;};
    default BasicFileMultipartDTO readFile(BasicFileDTO file, long pos, int size){return null;}

    default BasicFileRequestDTO getFileRequest(BasicFileDTO src,short mode){
        if (FileServerConst.OPEN_MODE_READ_ONLY.equals(mode)){
            return getDownloadRequest(src);
        } else if (FileServerConst.OPEN_MODE_READ_WRITE.equals(mode)) {
            return getUploadRequest(src);
        } else {
            return null;
        }
    }

    default BasicFileDTO moveFile(BasicFileDTO src,BasicFileDTO dst){return src;}

    default long getFileLength(BasicFileDTO basicSrc){return 0;}
    default boolean setFileLength(BasicFileDTO basicSrc, long fileLength){return true;}
}
