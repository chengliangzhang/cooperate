package com.maoding.FileServer;

import com.maoding.Base.BaseRemoteService;
import com.maoding.FileServer.zeroc.FileDTO;
import com.maoding.FileServer.zeroc.FileService;
import com.maoding.FileServer.zeroc.FileServicePrx;
import com.maoding.Utils.SpringUtils;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/8 18:43
 * 描    述 :
 */
public class LocalFileServerPrx extends BaseRemoteService<FileServicePrx> implements FileServicePrx{
    private static FileService fileService = null;

    public static FileService getFileService(){
        if (fileService == null){
            fileService = SpringUtils.getBean(FileService.class);
        }
        return fileService;
    }

    @Override
    public FileDTO copyFile(FileDTO src, FileDTO dst) {
        return getFileService().copyFile(src,dst,null);
    }
}
