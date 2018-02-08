package com.maoding.CoreFileServer;

import com.maoding.Bean.CoreResponse;
import com.maoding.Const.FileServerConst;
import com.maoding.CoreFileServer.MaodingWeb.WebFileResponse;

import java.io.File;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 9:58
 * 描    述 :
 */
public interface CoreFileServer {
    /** 设定文件服务器地址 */
    default void coreSetServerAddress(String serverAddress){}
    /** 获取文件服务器地址 */
    default String coreGetServerAddress(){return null;}
    /** 计算文件MD5 */
    default String coreCalcChecksum(CoreFileDTO src){return null;}
    /** 复制文件 */
    default CoreFileCopyResult coreCopyFile(CoreFileDTO src, CoreFileDTO dst){return null;}
    /** 创建文件 */
    default CoreFileDTO coreCreateFile(){return coreCreateFile(CoreFileDTO.class.cast(null));}
    default CoreFileDTO coreCreateFile(CoreFileDTO dst) {return coreCreateFile(dst,CoreFileExtraDTO.class.cast(null));}
    default CoreFileDTO coreCreateFile(CoreFileDTO dst,long fileLength) {return coreCreateFile(dst,new CoreFileExtraDTO(fileLength));}
    default CoreFileDTO coreCreateFile(CoreFileDTO dst,CoreFileExtraDTO createRequest) {return null;}
    /** 获取文件句柄 */
    default File coreGetFile(CoreFileDTO src){return null;}


    /** 把文件传到服务器 */
    @Deprecated
    default CoreResponse<WebFileResponse> upload(CoreFileDTO src, CoreFileExtraDTO request){return null;}
    /** 把文件传从服务器下载到本地 */
    @Deprecated
    default CoreFileDTO download(CoreFileDTO src, CoreDownloadRequest request){return null;}
    /** 根据路径创建文件 */
    @Deprecated
    default CoreFileDTO coreCreateFile(String path) {return null;}
    /**
     * 获取通过http方式上传文件数据库时的需要设置的部分参数
     */
    @Deprecated
    default BasicFileRequestDTO getUploadRequest(CoreFileDTO src, Integer mode, BasicCallbackDTO callbackSetting){return null;}

    @Deprecated
    default BasicFileRequestDTO getUploadRequest(CoreFileDTO src, Integer mode) {
        return getUploadRequest(src, mode, null);
    }

    @Deprecated
    default BasicFileRequestDTO getUploadRequest(CoreFileDTO src) {
        return getUploadRequest(src, null);
    }

    /**
     * 获取通过http方式下载文件数据库时的需要设置的部分参数
     */
    @Deprecated
    default BasicFileRequestDTO getDownloadRequest(CoreFileDTO src, Integer mode, BasicCallbackDTO callbackSetting){return null;}

    @Deprecated
    default BasicFileRequestDTO getDownloadRequest(CoreFileDTO src, Integer mode) {
        return getDownloadRequest(src, mode, null);
    }

    @Deprecated
    default BasicFileRequestDTO getDownloadRequest(CoreFileDTO src) {
        return getDownloadRequest(src, null);
    }

    /**
     * 上传文件分片内容
     */
    @Deprecated
    default BasicUploadResultDTO multipartUpload(BasicUploadRequestDTO request) {
        return null;
    }

    /**
     * 上传文件分片内容
     */
    @Deprecated
    default BasicUploadResultDTO upload(BasicUploadRequestDTO request){return null;}

    /**
     * 下载文件分片内容
     */
    @Deprecated
    default BasicDownloadResultDTO download(BasicDownloadRequestDTO request){return null;}

    /**
     * 在文件服务器上复制文件，复制到同一空间，返回复制后的文件标识
     */
    default String duplicateFile(CoreFileDTO src){return null;}

    /**
     * 判断在文件服务器上是否存在指定文件
     */
    default Boolean isExist(CoreFileDTO src){return false;}

    /**
     * 获取文件服务器上某一空间上的所有文件
     */
    default List listFile(String scope){return null;}

    /**
     * 获取文件服务器上的所有空间
     */
    default List<String> listScope(){return null;}

    /**
     * 在文件服务器上删除指定文件
     */
    default void deleteFile(CoreFileDTO src){}

    /**
     * 通告文件服务器上传完毕
     */
    @Deprecated
    default void finishUpload(BasicFileRequestDTO request) {
    }

    default int writeFile(CoreFileDataDTO data) {
        return 0;
    }

    default CoreFileDataDTO readFile(CoreFileDTO file, long pos, int size) {
        return null;
    }

    @Deprecated
    default BasicFileRequestDTO getFileRequest(CoreFileDTO src, short mode) {
        if (FileServerConst.OPEN_MODE_READ_ONLY.equals(mode)) {
            return getDownloadRequest(src);
        } else if (FileServerConst.OPEN_MODE_READ_WRITE.equals(mode)) {
            return getUploadRequest(src);
        } else {
            return null;
        }
    }

    default CoreFileDTO coreMoveFile(CoreFileDTO src, CoreFileDTO dst) {
        return src;
    }

    default long coreGetFileLength(CoreFileDTO basicSrc) {
        return 0;
    }

    /** 设置文件长度 */
    default boolean coreSetFileLength(CoreFileDTO src, long fileLength) {
        return true;
    }

    default CoreFileDTO copyFile(CoreFileDTO basicSrc, CoreFileDTO basicDst) {return null;}
}
