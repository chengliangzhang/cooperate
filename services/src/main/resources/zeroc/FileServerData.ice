#pragma once
#include <Common.ice>
#include <StorageData.ice>
#include <ProjectData.ice>

[["java:package:com.maoding.FileServer"]]
module zeroc {
    ["java:type:java.util.ArrayList<String>","deprecate"] sequence<string> KeyList;
    ["java:type:java.util.ArrayList<String>","deprecate"] sequence<string> ScopeList;

    ["java:getset","clr:property"]
    struct CommitRequestDTO {
        string actionTypeId; //提交操作id

        ["deprecate"] string pid; //提交到的父节点id
        ["deprecate"] string path; //要产生的文件路径

        ["deprecate"] string userId; //提交目标用户id
        string ownerUserId; //提交到的目标用户id
        string fileVersion; //提交的版本号
        string majorId; //提交的专业编号
        string majorName; //提交的专业名称
        string remark; //版本提交说明

        string serverTypeId; //提交到的文件服务器类型
        string serverAddress; //提交到的文件服务器地址
        string baseDir; //提交到的版本存放的服务器空间
    };

    ["java:getset","clr:property"]
    struct CreateNodeRequestDTO { //创建节点时的参数
        bool isDirectory; //是否创建目录
        string fullName; //要创建的节点名，可包含相对于父节点的路径
        long fileLength; //目标文件大小
    };

    ["java:getset","clr:property"]
    struct CommitFailDTO { //批量提交失败返回
        string id; //提交失败的节点id
    };
    ["java:type:java.util.ArrayList<CommitFailDTO>"] sequence<CommitFailDTO> CommitFailList;

    ["java:getset","clr:property"]
    struct CommitListResultDTO { //批量提交结果
        SimpleNodeList successList;
        CommitFailList failList;
    };

    ["java:getset","clr:property","deprecate"]
    struct CallbackDTO {
        string url; //回调地址
        string name; //回调服务器名称
        Map params; //回调参数
    };

    ["java:getset","clr:property","deprecate"]
    struct FileDTO {
        string scope; //空间(bucket或group)
        string key; //文件id(key或path)
    };

    ["java:getset","clr:property"]
    struct MoveNodeRequestDTO { //复制节点时的参数
        string fullName; //目标相对于目标父节点的路径
    };

    ["java:getset","clr:property"]
    struct FileDataDTO {
        long pos; //数据所在起始位置，为0则为从文件头开始
        int size; //数据有效字节数，为0则所有字节都有效
        ByteArray data; //当前分片数据
    };

    ["java:getset","clr:property"]
    struct SuggestionRequestDTO { //校审意见提交申请
        string typeId; //校审意见类型
        bool isPassed; //是否通过
        string content; //校审意见正文
        ByteArray data; //意见截图数据
        NodeFileList accessoryList; //附件文件列表
    };

    ["java:getset","clr:property"]
    struct AccessoryRequestDTO { //附件提交申请
        string path; //附件路径
        ByteArray data; //附件文件内容
    };

};