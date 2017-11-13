#pragma once
#include <Common.ice>
#include <FileServer.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    ["java:getset"]
    struct CooperateFileDTO {
        string id; //协同文件id（针对每一个版本）
        string fileName; //协同文件名
        string dirName; //协同目录名
        long fileLength; //文件长度
        string checksum; //协同文件md5
        string version; //协同文件版本号
        string specialtyId; //专业id
        string lastAdapterId; //最近更改协同的下载服务的AdapterId
        string lastComputerIp; //最近更改协同的计算机的IP地址
        short syncMode; //同步模式，0-手动同步，1-自动下载最新版本，2-自动更新
        bool canBeModify; //能否修改（服务器是否锁定）
        string localFile; //本地文件路径
        Map referenceIdList; //参考协同文件id

        string creatorId; //协同创建者的用户id
        Date createTime; //协同建立时间
        string lastId; //最近更改协同的用户id
        Date lastTime; //最近更改协同的时间
    };

    interface StorageService {
        FileRequestDTO requestUpload(CooperateFileDTO fileInfo,int mode); //申请上传文件
        FileRequestDTO requestDownload(CooperateFileDTO fileInfo,int mode); //申请下载文件
    };
};