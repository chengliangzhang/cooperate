#pragma once
#include <Common.ice>

[["java:package:com.maoding.FileServer"]]
module zeroc {
    ["java:type:java.util.ArrayList<String>"] sequence<string> FileList;
    ["java:type:java.util.ArrayList<String>"] sequence<string> ScopeList;


    ["java:getset"]
    struct CallbackDTO {
        string url; //回调地址
        string name; //回调服务器名称
        Map params; //回调参数
    };

    ["java:getset"]
    struct FileRequestDTO {
        string url; //文件服务的连接地址
        int mode; //文件服务器连接方式，2-Http Get,3-Http Post,4-阿里云OSS,...
        Map params; //需要设置的参数（如OSSAccessKeyId等)
    };

    ["java:getset"]
    struct FileDTO {
        string scope; //空间(bucket或group)
        string key; //文件id(key或path)
    };

    ["java:getset"]
    struct FileMultipartDTO {
        string scope; //空间(bucket或group)
        string key; //文件id(key或path)
        long pos; //数据所在起始位置，为0则为从文件头开始
        int size; //数据有效字节数，为0则所有字节都有效
        ByteArray data; //当前分片数据
    };

    ["java:getset"]
    struct UploadRequestDTO {
        int requestId; //上传申请的唯一编号
        string uploadId; //上传任务ID
        int chunkCount; //总分片数量
        int chunkPerSize; //每个分片的约定大小
        int chunkId; //要上传的分片序号
        int chunkSize; //要上传的分片大小
        FileMultipartDTO multipart; //要上传的文件数据
        Map params; //其他上传参数
    };

    ["java:getset"]
    struct UploadResultDTO {
        int status; //返回状态，等于0-正常，小于0-发生异常，大于0-存在警告
        string msg; //返回状态的文字说明
        FileMultipartDTO data; //上传文件在文件服务器内的标识
        int requestId; //上传申请的唯一编号
        string uploadId; //上传任务ID
        int chunkId; //当前上传的分片序号
        int chunkSize; //实际上传的分片大小
    };

    ["java:getset"]
    struct DownloadRequestDTO {
        int requestId; //下载申请的唯一编号
        string scope; //空间(bucket或group)
        string key; //文件id(key或path)
        int chunkId; //申请下载的分片序号，与分片大小相乘计算出文件起始位置
        int chunkSize; //申请下载的分片大小，如果为0则chunkId失效，下载文件所有内容
        Map params; //其他下载参数
    };

    ["java:getset"]
    struct DownloadResultDTO {
        int status; //返回状态，等于0-正常，小于0-发生异常，大于0-存在警告
        string msg; //返回状态的文字说明
        FileMultipartDTO data; //已下载的文件数据
        int requestId; //下载申请的唯一编号
        Integer chunkId; //当前下载的分片序号
        int chunkSize; //当前下载的分片大小
        int chunkCount; //后续内容分片数量，为0则已经下载完毕
    };

    interface FileService {
        void setFileServerType(int type); //设置文件服务器类型，1-FastDFS服务器，2-阿里云服务器
        int getFileServerType(); //读取文件服务器类型，1-FastDFS服务器，2-阿里云服务器
        FileRequestDTO getUploadRequest(FileDTO src,int mode,CallbackDTO callback); //获取通过Multipart上传文件时的参数
        FileRequestDTO getDownloadRequest(FileDTO src,int mode,CallbackDTO callback); //获取通过Multipart下载文件时的参数
        UploadResultDTO upload(UploadRequestDTO request); //上传文件分片内容
        DownloadResultDTO download(DownloadRequestDTO request); //下载文件分片内容
        string duplicateFile(FileDTO src); //复制文件，并返回新文件名
        void deleteFile(FileDTO src); //删除文件
        bool isExist(FileDTO src); //在文件服务器内查找指定的文件
        FileList listFile(string scope); //从文件服务器获取某空间所有文件名
        ScopeList listScope(); //从文件服务器获取某空间所有文件名
    };
};