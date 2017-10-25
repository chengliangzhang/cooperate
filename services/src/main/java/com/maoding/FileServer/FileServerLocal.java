package com.maoding.FileServer;

import com.maoding.FileServer.zeroc.FileServerService;
import com.maoding.FileServer.zeroc.HttpRequestDTO;
import com.zeroc.Ice.Current;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 10:08
 * 描    述 :
 */
public class FileServerLocal implements FileServerService{
    @Override
    public HttpRequestDTO getUploadRequestForHttp(Current current) {
        return null;
    }

    @Override
    public HttpRequestDTO getDownloadRequestForHttp(String src, Current current) {
        return null;
    }

    @Override
    public Boolean isExist(String src, Current current) {
        return null;
    }

    @Override
    public String duplicateFile(String src, Current current) {
        return null;
    }

    @Override
    public void deleteFile(String src, Current current) {

    }

    @Override
    public List<String> listFile(String scope, Current current) {
        return null;
    }

    @Override
    public List<String> listScope(Current current) {
        return null;
    }
}
