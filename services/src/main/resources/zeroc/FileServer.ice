#pragma once
#include <Common.ice>

[["java:package:com.maoding.FileServer"]]
module zeroc {
    ["java:type:java.util.ArrayList<String>"] sequence<string> FileList;
    ["java:type:java.util.ArrayList<String>"] sequence<string> ScopeList;

    ["java:getset","clr:property"]
    struct NodeDTO { //节点信息（目录和文件通用信息）
        string id; //节点编号（树节点编号）
        string name; //节点名称（树节点名称或文件名称）
        string pid; //父节点编号
        short typeId; //节点类别编号
        string path; //节点全路径
        long createTimeStamp; //节点建立时间
        string createTimeText; //节点建立时间文字
        long lastModifyTimeStamp; //节点最后修改时间
        string lastModifyTimeText; //节点最后修改时间文字
        bool isReadOnly; //节点是否只读
        long fileLength; //节点长度

        bool isDirectory; //节点是否目录
        bool isProject; //节点是否项目目录
        bool isTask; //节点是否任务目录
        bool isDesign; //节点是否设计资料
        bool isCommit; //节点是否提资资料
        bool isHistory; //节点是否历史版本

        //加速所需属性
        string projectId; //节点所属项目id
        string projectName; //节点所属项目名称
        string issueId; //节点所属签发任务id
        string issueName; //节点所属签发任务名称
        string taskId; //节点所属生产任务id
        string taskName; //节点所属生产任务名称
        string companyId; //节点生产组织id
        string companyName; //节点生产组织名称
        string classicId; //节点所属分类id
        string classicName; //节点所属分类名称
        string storagePath; //节点相对路径
        string ownerUserId; //节点所有者用户id
        string ownerRoleId; //节点所有者职责id
        string ownerName; //节点所有者名称
    };
    ["java:type:java.util.ArrayList<NodeDTO>"] sequence<NodeDTO> NodeList;


    ["java:getset","clr:property"]
    struct CallbackDTO {
        string url; //回调地址
        string name; //回调服务器名称
        Map params; //回调参数
    };

    ["java:getset","clr:property"]
    struct FileRequestDTO {
        string id; //协同文件编号
        string nodeId; //协同文件树节点编号
        string url; //文件服务的连接地址
        string scope; //文件在文件服务器内的存储空间
        string key; //文件在文件服务器内的存储名称
        int mode; //文件服务器连接方式，2-Http Get,3-Http Post,4-阿里云OSS,...
        Map params; //需要设置的参数（如OSSAccessKeyId等)
    };

    ["java:getset","clr:property"]
    struct FileDTO {
        string scope; //空间(bucket或group)
        string key; //文件id(key或path)
    };

    ["java:getset","clr:property"]
    struct FileMultipartDTO {
        string scope; //空间(bucket或group)
        string key; //文件id(key或path)
        long pos; //数据所在起始位置，为0则为从文件头开始
        int size; //数据有效字节数，为0则所有字节都有效
        ByteArray data; //当前分片数据
    };

    ["java:getset","clr:property"]
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

    ["java:getset","clr:property"]
    struct UploadResultDTO {
        int status; //返回状态，等于0-正常，小于0-发生异常，大于0-存在警告
        string msg; //返回状态的文字说明
        FileDTO data; //上传文件在文件服务器内的标识
        int requestId; //上传申请的唯一编号
        string uploadId; //上传任务ID
        int chunkId; //当前上传的分片序号
        int chunkSize; //实际上传的分片大小
    };

    ["java:getset","clr:property"]
    struct DownloadRequestDTO {
        int requestId; //下载申请的唯一编号
        string scope; //空间(bucket或group)
        string key; //文件id(key或path)
        int chunkId; //申请下载的分片序号，与分片大小相乘计算出文件起始位置
        int chunkSize; //申请下载的分片大小，如果为0则chunkId失效，下载文件所有内容
        long pos; //申请下载的文件内起始地址
        int size; //申请下载的大小
        Map params; //其他下载参数
    };

    ["java:getset","clr:property"]
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
        //准备实现的接口
        FileRequestDTO getFileRequest(FileDTO src,short mode); //使用只读或读写方式申请文件实际地址
        FileDTO moveFile(FileDTO src,FileDTO dst); //移动文件
        long getFileLength(FileDTO src); //取文件长度
        bool setFileLength(FileDTO src,long fileLength); //设置文件长度
        FileDTO copyFile(FileDTO src,FileDTO dst); //复制文件

        //已经实现接口
        int writeFile(FileMultipartDTO data); //写入文件
        FileMultipartDTO readFile(FileDTO file,long pos,int size); //从文件读取数据
        void setFileServerType(int type); //设置文件服务器类型，1-FastDFS服务器，2-阿里云服务器
        int getFileServerType(); //读取文件服务器类型，1-FastDFS服务器，2-阿里云服务器
        string duplicateFile(FileDTO src); //复制文件，并返回新文件名
        void deleteFile(FileDTO src); //删除文件
        bool isExist(FileDTO src); //在文件服务器内查找指定的文件
        FileList listFile(string scope); //从文件服务器获取某空间所有文件名
        ScopeList listScope(); //从文件服务器获取某空间所有文件名
        void finishUpload(FileRequestDTO request); //结束上传过程

        //准备删除接口
        FileRequestDTO getUploadRequest(FileDTO src,int mode,CallbackDTO callback); //获取通过Multipart上传文件时的参数
        FileRequestDTO getDownloadRequest(FileDTO src,int mode,CallbackDTO callback); //获取通过Multipart下载文件时的参数
        UploadResultDTO upload(UploadRequestDTO request); //上传文件分片内容
        DownloadResultDTO download(DownloadRequestDTO request); //下载文件分片内容
    };
};