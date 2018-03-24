package com.maoding.CoreFileServer;

import com.maoding.Bean.CoreResponse;
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
    @Deprecated
    String ADDRESS_SPLIT = "|";

    /** 设定文件服务器存储空间 */
    default void coreSetBaseDir(String baseDir){}
    /** 获取文件服务器存储空间 */
    default String coreGetBaseDir(){return null;}

    /** 设置本地镜像根目录 */
    default void coreSetMirrorBaseDir(String mirrorBaseDir){}
    /** 获取本地镜像根目录 */
    default String coreGetMirrorBaseDir(){return null;}

    /** 设定文件服务器地址 */
    default void coreSetServerAddress(String serverAddress){}
    /** 获取文件服务器地址 */
    default String coreGetServerAddress(){return null;}

    /** 同时设定文件服务器地址和存储空间 */
    default void coreSetServerAddress(String serverAddress,String baseDir){
        coreSetServerAddress(serverAddress,baseDir,null);
    }
    /** 同时设定文件服务器地址,存储空间和本地镜像根目录 */
    default void coreSetServerAddress(String serverAddress,String baseDir,String mirrorBaseDir){
        coreSetServerAddress(serverAddress);
        coreSetBaseDir(baseDir);
        coreSetMirrorBaseDir(mirrorBaseDir);
    }

    /** 设置文件长度 */
    default void coreSetFileLength(String key, long fileLength) {}
    @Deprecated
    default void coreSetFileLength(CoreFileDTO src, long fileLength) {}
    /** 获取文件长度 */
    default long coreGetFileLength(String key) {
        return 0;
    }
    @Deprecated
    default long coreGetFileLength(CoreFileDTO src) {
        return 0;
    }

    /** 创建文件 */
    default String coreCreateFileNew(CoreCreateFileRequest createRequest) {return null;}
    default String coreCreateFileNew(String key,long fileLength) {return coreCreateFileNew(new CoreCreateFileRequest(key,fileLength));}
    default String coreCreateFileNew(String key,File srcFile) {return coreCreateFileNew(new CoreCreateFileRequest(key,srcFile));}
    default String coreCreateFileNew(String key) {return coreCreateFileNew(new CoreCreateFileRequest(key));}
    default String coreCreateFileNew(){return coreCreateFileNew((CoreCreateFileRequest)null);}

    @Deprecated
    default CoreFileDTO coreCreateFile(CoreCreateFileRequest createRequest) {return null;}
    @Deprecated
    default CoreFileDTO coreCreateFile(String key,long fileLength) {return coreCreateFile(new CoreCreateFileRequest(key,fileLength));}
    @Deprecated
    default CoreFileDTO coreCreateFile(String key,File srcFile) {return coreCreateFile(new CoreCreateFileRequest(key,srcFile));}
    @Deprecated
    default CoreFileDTO coreCreateFile(){return coreCreateFile(CoreFileDTO.class.cast(null));}
    @Deprecated
    default CoreFileDTO coreCreateFile(CoreFileDTO dst,CoreCreateFileRequest createRequest) {return null;}
    @Deprecated
    default CoreFileDTO coreCreateFile(CoreFileDTO dst,long fileLength) {return coreCreateFile(dst,new CoreCreateFileRequest(fileLength));}
    @Deprecated
    default CoreFileDTO coreCreateFile(CoreFileDTO dst,File srcFile) {return coreCreateFile(dst,new CoreCreateFileRequest(srcFile));}
    @Deprecated
    default CoreFileDTO coreCreateFile(CoreFileDTO dst) {return coreCreateFile(dst,CoreCreateFileRequest.class.cast(null));}

    /** 写入文件内容 */
    default int coreWriteFile(CoreFileDataDTO data, String key){return 0;}
    @Deprecated
    default int coreWriteFile(CoreFileDataDTO data, CoreFileDTO dst){return 0;}
    @Deprecated
    default int coreWriteFile(CoreFileDataDTO data) {
        if (data == null) return 0;
        CoreFileDTO dst = new CoreFileDTO();
        dst.setScope(data.getScope());
        dst.setKey(data.getKey());
        return coreWriteFile(data,dst);
    }

    /** 读取文件内容 */
    default CoreFileDataDTO coreReadFile(String key, long pos, int size) {
        return null;
    }
    default CoreFileDataDTO coreReadFile(String key) {
        return coreReadFile(key,0,(int)coreGetFileLength(key));
    }
    @Deprecated
    default CoreFileDataDTO coreReadFile(CoreFileDTO src, long pos, int size) {
        return null;
    }
    @Deprecated
    default CoreFileDataDTO coreReadFile(CoreFileDTO src) {
        return coreReadFile(src,0,(int)coreGetFileLength(src));
    }

    /** 计算文件MD5 */
    default String coreCalcChecksum(String key){return null;}
    @Deprecated
    default String coreCalcChecksum(CoreFileDTO src){return null;}

    /** 判断在文件服务器上是否存在指定文件 */
    default Boolean coreIsExist(String key){return false;}
    @Deprecated
    default Boolean coreIsExist(CoreFileDTO src){return false;}

    /** 删除指定文件 */
    default void coreDeleteFile(String key){}
    @Deprecated
    default void coreDeleteFile(CoreFileDTO src){}


    /** 获取本地文件或镜像文件 */
    default File coreGetFile(String key,String mirrorBaseDir){return null;}
    default File coreGetFile(String key){
        return coreGetFile(key,coreGetMirrorBaseDir());
    }
    @Deprecated
    default File coreGetLocalFile(CoreFileDTO src,String mirrorBaseDir){return null;}
    @Deprecated
    default File coreGetLocalFile(CoreFileDTO src){return coreGetLocalFile(src,coreGetMirrorBaseDir());}

    /** 清理本地镜像文件 */
    default void clearLocalMirrorFile(File mirrorFile){}
    default void clearLocalMirrorFile(){}

    /** 获取文件服务器上的文件标识 */
    default List<String> listKey(String key){return null;}
    default List<String> listKey(){return listKey("-");}

    /** 获取文件服务器上的所有空间 */
    @Deprecated
    default List<String> listScope(){return null;}
    /** 获取文件服务器上某一空间上的所有文件 */
    @Deprecated
    default List<CoreFileDTO> listFile(String scope){return null;}





    /** 复制文件 */
    @Deprecated
    default CoreFileCopyResult coreCopyFile(CoreFileDTO src, CoreFileDTO dst){return null;}




    /** 把文件传到服务器 */
    @Deprecated
    default CoreResponse<WebFileResponse> upload(CoreFileDTO src, CoreCreateFileRequest request){return null;}
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











    /**
     * 通告文件服务器上传完毕
     */
    @Deprecated
    default void finishUpload(BasicFileRequestDTO request) {
    }




    default CoreFileDTO coreMoveFile(CoreFileDTO src, CoreFileDTO dst) {
        return src;
    }



    default CoreFileDTO copyFile(CoreFileDTO basicSrc, CoreFileDTO basicDst) {return null;}
}
