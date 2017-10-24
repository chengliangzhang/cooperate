#pragma once
#include <Common.ice>

[["java:package:com.maoding.FileServer"]]
module zeroc {
    ["java:type:java.util.ArrayList<String>"] sequence<string> FileList;

    ["java:getset"]
    struct HttpRequestDTO {
        string url; //需要设置的url
        string header; //需要设置的header
        Integer type; //文件服务器类型
    };

    interface FileServerService {
        Integer getFileServerType(); //返回空间存储类型
        HttpRequestDTO getUploadRequestForHttp(); //获取通过Multipart上传文件时的参数
        HttpRequestDTO getDownloadRequestForHttp(string src); //获取通过Multipart下载文件时的参数
        string DuplicateFile(string src); //复制文件，并返回新文件名
        void DeleteFile(string src); //删除文件
        Boolean isExist(string src); //在文件服务器内查找指定的文件
        FileList listFile(); //从文件服务器获取所有文件名
    };
};