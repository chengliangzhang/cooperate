#pragma once
#include <Common.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    ["java:getset","clr:property"]
    struct SimpleNodeDTO { //节点信息（目录和文件通用信息）
        string id; //节点编号（树节点编号）
        string pid; //父节点编号
        short typeId; //节点类别编号
        string name; //节点名称（树节点名称或文件名称）
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
    ["java:type:java.util.ArrayList<SimpleNodeDTO>"] sequence<SimpleNodeDTO> SimpleNodeList;

    ["java:getset","clr:property"]
    struct FullNodeDTO { //节点完整信息
        SimpleNodeDTO basic; //节点基本信息

        //补充节点信息
        string issuePath; //节点所属签发任务路径
        string taskPath; //节点所属生产任务路径
    };
    ["java:type:java.util.ArrayList<FullNodeDTO>"] sequence<FullNodeDTO> FullNodeList;

    ["java:getset","clr:property"]
    struct HistoryDTO { //历史记录信息
        string id; //历史记录编号
        string fileId; //操作文件节点id
        string userId; //操作人员用户id
        string roleId; //操作人员职责id
        string userName; //操作人员名字
        short actionTypeId; //操作动作编号
        string actionName; //操作动作名称
        long actionTimeStamp; //操作时间
        string actionTimeText; //操作时间文字
        string remark; //操作说明
    };
    ["java:type:java.util.ArrayList<HistoryDTO>"] sequence<HistoryDTO> HistoryList;

    ["java:getset","clr:property"]
    struct FileNodeDTO { //文件信息
        SimpleNodeDTO basic; //节点基本信息

        //文件节点特有信息
        short fileTypeId; //文件类型
        string fileTypeName; //文件类型文字说明
        string fileVersion; //文件版本号
        string fileChecksum; //文件校验和
        string majorId; //文件所属专业id
        string majorName; //文件所属专业名称
        string mainFileId; //设计文件id
        string fileRemark; //文件操作注解

        //实际文件存储位置
        string serverTypeId; //文件服务器类型id
        string serverTypeName; //文件服务器类型名称
        string serverAddress; //文件服务器地址

        string readFileScope; //只读版本在文件服务器上的存储位置
        string readFileKey; //只读版本在文件服务器上的存储名称
        string writeFileScope; //可写版本在文件服务器上的存储位置
        string writeFileKey; //可写版本在文件服务器上的存储名称

        HistoryList historyList; //文件相关历史列表
    };
    ["java:type:java.util.ArrayList<FileNodeDTO>"] sequence<FileNodeDTO> FileNodeList;

    ["java:getset","clr:property"]
    struct QueryNodeDTO {
        string id; //树节点id
        string pid; //父节点id
        string path; //树节点全路径
        string typeId; //节点类型
        string projectId; //节点所属项目id
        string classicId; //节点所属分类类型
        string issueId; //节点所属签发任务id
        string taskId; //节点所属生产任务id
        string companyId; //节点所属组织id
        string ownerUserId; //节点拥有者用户id
        string ownerRoleId; //节点拥有者职责id
        string fuzzyPath; //模糊匹配路径
        string storagePath; //相对路径
        string parentPath; //父路径
        string parentStoragePath; //父路径，针对树节点存储的相对路径
        string fuzzyStoragePath; //模糊匹配路径，针对树节点存储的相对路径
        string userId; //查询者用户id
    };

    ["java:getset","clr:property"]
    struct UpdateNodeDTO { //节点更改申请
        //节点信息
        short typeId; //节点类型
        string pid; //父节点编号
        string fullName; //要创建的节点名，可包含相对于父节点的路径
        long fileLength; //目标文件大小
        string ownerUserId; //拥有者用户id
        string taskId; //生产任务Id

        //目标位置信息
        string parentPath; //父节点的全路径
        short parentTypeId; //父节点的类型
        string parentStoragePath; //父节点相对路径

        //文件节点信息
        string mainFileId; //原始节点id
        short fileTypeId; //目标文件类型Id
        string fileVersion; //文件版本号
        string fileChecksum; //文件校验和
        string majorId; //文件所属专业id
        string readFileScope; //只读版本在文件服务器上的存储位置
        string readFileKey; //只读版本在文件服务器上的存储名称
        string writeFileScope; //可写版本在文件服务器上的存储位置
        string writeFileKey; //可写版本在文件服务器上的存储名称

        //历史节点信息
        short actionTypeId; //文件操作类型
        string remark; //文件操作注解

        //文件服务器信息
        short serverTypeId; //文件服务器类型
        string serverAddress; //文件服务器地址
    };
};