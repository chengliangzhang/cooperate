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
        long createTimeStamp; //节点建立时间
        string createTimeText; //节点建立时间文字
        long lastModifyTimeStamp; //节点最后修改时间
        string lastModifyTimeText; //节点最后修改时间文字
        long readOnlyFileLength; //只读节点长度
        long writableFileLength; //可写节点长度
        string readOnlyFileMd5; //只读节点MD5
        string writableFileMd5; //可写节点MD5

        bool isDirectory; //节点是否目录
        bool isProject; //节点是否项目目录
        bool isTask; //节点是否任务目录
        bool isDesign; //节点是否设计资料
        bool isCommit; //节点是否提资资料
        bool isHistory; //节点是否历史版本

        string projectId; //节点所属项目id
        string rangeId; //节点所属分类id
        string issueId; //节点所属签发任务id
        string taskId; //节点所属生产任务id
        string companyId; //节点所属组织id
        string ownerUserId; //节点所有者用户id
        string ownerRoleId; //节点所有者职责id
        string lastModifyRoleId; //最后操作者职责id

        //即时属性
        bool isReadOnly; //节点是否只读
        string fileMd5; //MD5
        long fileLength; //节点长度

        //兼容属性
        ["deprecate:移入FullNodeDTO"] string path; //节点全路径
        ["deprecate:移入FullNodeDTO"] string projectName; //节点所属项目名称
        ["deprecate:移入FullNodeDTO"] string issueName; //节点所属签发任务名称
        ["deprecate:移入FullNodeDTO"] string taskName; //节点所属生产任务名称
        ["deprecate:移入FullNodeDTO"] string companyName; //节点生产组织名称
        ["deprecate:移入FullNodeDTO"] string classicName; //节点所属分类名称
        ["deprecate:移入FullNodeDTO"] string storagePath; //节点相对路径
        ["deprecate:移入FullNodeDTO"] string ownerName; //节点所有者名称
		["deprecate:更换为rangeId"] string classicId; //节点所属分类id
    };
    ["java:type:java.util.ArrayList<SimpleNodeDTO>"] sequence<SimpleNodeDTO> SimpleNodeList;

    ["java:getset","clr:property"]
    struct HistoryDTO { //历史记录信息
        string id; //历史记录编号
        string userId; //操作人员用户id
        string userName; //操作人员名字
        string roleId; //操作人员职责id
        string roleName; //操作人员职责名称
        short actionTypeId; //操作动作编号
        string actionName; //操作动作名称
        long actionTimeStamp; //操作时间
        string actionTimeText; //操作时间文字
        string remark; //操作说明

        ["deprecate"] string fileId; //操作文件节点id
    };
    ["java:type:java.util.ArrayList<HistoryDTO>"] sequence<HistoryDTO> HistoryList;

    ["java:getset","clr:property","deprecate"]
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
    struct NodeTextDTO { //节点文字描述信息
        string path; //节点全路径
        string rangeName; //节点所属分类名称
        string companyName; //节点所属组织名称
        string companyPath; //节点所属组织路径
        string projectName; //节点所属项目名称
        string issueName; //节点所属签发任务名称
        string issuePath; //节点所属签发任务路径
        string taskName; //节点所属生产任务名称
        string taskPath; //节点所属生产任务路径
        string storagePath; //节点相对路径
        string ownerName; //节点所有者名称
        string lastModifyRoleName; //节点最后操作者角色
        string serverTypeName; //文件服务器类型名称
        string fileTypeName; //文件类型文字说明
        string majorName; //文件所属专业名称
        string mainFilePath; //主文件全路径
    };
    ["java:type:java.util.ArrayList<NodeTextDTO>"] sequence<NodeTextDTO> NodeTextList;

    ["java:getset","clr:property"]
    struct NodeFileDTO { //节点文件信息
        //文件节点特有信息
        string id; //文件节点编号
        string fileTypeId; //文件类型
        string fileVersion; //文件版本号
        string fileChecksum; //文件校验和
        string majorTypeId; //文件所属专业id
        string mainFileId; //主文件id

        //实际文件存储位置
        string serverTypeId; //文件服务器类型id
        string serverAddress; //文件服务器地址
        string baseDir; //文件在文件服务器上的存储位置

        string readOnlyKey; //只读版本在文件服务器上的存储名称
        string writableKey; //可写版本在文件服务器上的存储名称

        string readOnlyMirrorKey; //只读版本在本地的镜像文件相对路径
        string writableMirrorKey; //可写版本在本地的镜像文件相对路径
    };
    ["java:type:java.util.ArrayList<NodeFileDTO>"] sequence<NodeFileDTO> NodeFileList;

    ["java:getset","clr:property"]
    struct FullNodeDTO { //节点完整信息
        SimpleNodeDTO basic; //节点基本信息

        NodeTextDTO textInfo; //文字描述信息
        NodeFileDTO fileInfo; //文件信息

        HistoryList historyList; //相关历史列表

        ["deprecate"] string issuePath; //节点所属签发任务路径
        ["deprecate"] string taskPath; //节点所属生产任务路径
    };
    ["java:type:java.util.ArrayList<FullNodeDTO>"] sequence<FullNodeDTO> FullNodeList;

    ["java:getset","clr:property"]
    struct EmbedElementDTO { //嵌入的HTML元素，如小型位图等
        //通用属性
        string id; //元素id

        //嵌入元素属性
        string title; //元素占位文字
        ByteArray dataArray; //元素内容

        //通用属性
        long createTimeStamp; //注解建立时间
        string createTimeText; //注解建立时间文字
        long lastModifyTimeStamp; //注解最后修改时间
        string lastModifyTimeText; //注解最后修改时间文字
        string lastModifyUserId; //注解最后编辑用户id
        string lastModifyRoleId; //注解最后编辑角色id
    };
    ["java:type:java.util.ArrayList<EmbedElementDTO>"] sequence<EmbedElementDTO> EmbedElementList;

    ["java:getset","clr:property"]
    struct UpdateElementDTO { //嵌入的HTML元素更新申请
        string title; //元素占位文字
        ByteArray dataArray; //元素内容

        //通用更改申请
        ["deprecate"] long lastModifyTimeStamp; //最后修改时间
        string lastModifyUserId; //最后编辑用户id
        string lastModifyRoleId; //最后编辑角色id
    };

    ["java:getset","clr:property"]
    struct AnnotateDTO { //文件注解
        //通用属性
        string id; //文件注解编号
        string name; //文件注解标题

        //通用树节点属性
        string pid; //文件注解父节点编号
        string typeId; //文件注解类型

        //文件注解属性
        string statusId; //文件注解状态
        string content; //文件注解正文内容
        string fileId; //被批注的文件编号
        string mainFileId; //原始文件编号

        //文件注解附件信息
        EmbedElementDTO element; //首个嵌入元素
        NodeFileList accessoryList; //附件列表

        //通用属性
        long createTimeStamp; //注解建立时间
        string createTimeText; //注解建立时间文字
        long lastModifyTimeStamp; //注解最后修改时间
        string lastModifyTimeText; //注解最后修改时间文字
        string lastModifyUserId; //注解最后编辑用户id
        string lastModifyRoleId; //注解最后编辑角色id
    };
    ["java:type:java.util.ArrayList<AnnotateDTO>"] sequence<AnnotateDTO> AnnotateList;

    ["java:getset","clr:property"]
    struct UpdateAnnotateDTO { //注解更新申请
        //文件注解属性
        string statusId; //文件注解状态
        string content; //文件注解正文内容
        string fileId; //被批注的文件编号
        string mainFileId; //原始文件编号
        StringList addElementIdList; //需要添加的嵌入元素类附件编号
        StringList addFileIdList; //需要添加的文件类附件编号
        StringList delAttachmentIdList; //需要删除的附件编号

        //通用更改申请
        string typeId; //修改后的节点类型
        string pid; //修改后的父节点编号
        string path; //修改后的节点路径
        string lastModifyUserId; //申请修改的用户编号
        string lastModifyRoleId; //申请修改的角色编号
    };


    ["java:getset","clr:property"]
    struct QueryAnnotateDTO { //文件注解查询申请，每个属性都可以是逗号分隔的多个数据
        string statusId; //文件注解状态
        string fileId; //被注解的文件编号
        string mainFileId; //被注解的原始文件的编号
        string anyFileId; //被注解的文件或原始文件编号

        //通用查询属性
        string id; //记录编号
        string lastModifyUserId; //最后更改者用户编号
        string lastModifyRoleId; //最后更改者职责编号
        long startTimeStamp; //起始时间
        long endTimeStamp; //终止时间
        string accountId; //查询者用户编号

        string nodeName; //记录节点名称
        string pid; //记录父节点编号
        string path; //记录路径
        string parentPath; //记录父节点路径
        string typeId; //记录节点类型

        string fuzzyPath; //模糊匹配路径
    };

    ["java:getset","clr:property"]
    struct UpdateNodeFileDTO { //附件或文件更新申请
        //文件服务器信息
        string serverTypeId; //文件服务器类型
        string serverAddress; //文件服务器地址
        string baseDir; //文件在文件服务器上的存储位置

        //文件节点信息
        string fileTypeId; //目标文件类型Id
        string fileVersion; //文件版本号
        ["deprecate"] string fileChecksum; //文件校验和
        string majorTypeId; //文件所属专业编号
        string readOnlyKey; //只读版本在文件服务器上的存储名称
        long readOnlyFileLength; //只读版本文件长度
        string readOnlyFileMd5; //只读版本校验和
        string writableKey; //可写版本在文件服务器上的存储名称
        long writableFileLength; //可写版本文件长度
        string writableFileMd5; //可写版本校验和

        //镜像信息
        string mainFileId; //源文件编号
        string mirrorTypeId; //镜像文件服务器类型
        string mirrorAddress; //镜像文件服务器地址
        string mirrorBaseDir; //文件在镜像文件服务器上的存储位置
        string readOnlyMirrorKey; //只读版本在本地的镜像
        string writableMirrorKey; //可写版本在本地的镜像

        //通用更改申请
        string lastModifyUserId; //最后编辑用户id
        string lastModifyRoleId; //最后编辑角色id
    };

    ["java:getset","clr:property"]
    struct QueryNodeDTO { //节点查询申请，每个属性都可以是逗号分隔的多个数据
        string id; //树节点id
        string pid; //父节点id
        string name; //节点名称
        string path; //树节点全路径
        string typeId; //节点类型
        string projectId; //节点所属项目id
        string rangeId; //节点所属分类类型
        string issueId; //节点所属签发任务id
        string taskId; //节点所属生产任务id
        string companyId; //节点所属组织id
        string ownerUserId; //节点拥有者用户id
        string lastModifyRoleId; //最后更改者职责id
        string accountId; //查询者用户id
        string parentPath; //父路径

        string fuzzyId; //模糊匹配id字符串
        string fuzzyPath; //模糊匹配路径

        ["deprecate:使用rangeId代替"] string classicId; //节点所属分类类型
        ["deprecate"] string storagePath; //相对路径
        ["deprecate"] string parentStoragePath; //父路径，针对树节点存储的相对路径
        ["deprecate"] string fuzzyStoragePath; //模糊匹配路径，针对树节点存储的相对路径
        ["deprecate:使用lastModifyRoleId代替"] string ownerRoleId; //节点拥有者职责id

        ["deprecate"] string userId; //查询者用户id
    };

    ["java:getset","clr:property"]
    struct QueryNodeInfoTextDTO { //文字详细信息查询申请
        bool isQueryTypeName; //查询类型名称
    };

    ["java:getset","clr:property"]
    struct QueryNodeInfoFileDTO { //文件详细信息查询申请
        string mirrorServerTypeId; //镜像服务器类型
        string mirrorServerAddress; //镜像服务器地址
        string mirrorBaseDir; //镜像根目录
    };

    ["java:getset","clr:property"]
    struct QueryNodeInfoHistoryDTO { //历史详细信息查询申请
        long historyStartTimeStamp; //历史信息的起始时间
        long historyEndTimeStamp; //历史信息的终止时间
    };

    ["java:getset","clr:property"]
    struct QueryNodeInfoDTO { //节点详细资料查询申请
        QueryNodeInfoTextDTO textQuery; //文字信息查询申请
        QueryNodeInfoFileDTO fileQuery; //文件信息查询申请
        QueryNodeInfoHistoryDTO historyQuery; //历史信息查询申请
    };

    ["java:getset","clr:property"]
    struct QueryNodeFileDTO { //文件信息查询申请
        string id; //树节点id
        string serverTypeId; //文件或镜像服务器类型
        string serverAddress; //文件或镜像服务器地址
        string baseDir; //文件或镜像根目录
        string key; //文件标识符
    };


    ["java:getset","clr:property"]
    struct UpdateNodeDTO { //节点更改申请
        //节点信息
        short typeId; //节点类型
        string path; //绝对或相对路径，包含文件名
        string ownerUserId; //拥有者用户id

        //节点关联属性
        string taskId; //所属任务Id
        string mainFileId; //主文件id

        //文件服务器信息
        short serverTypeId; //文件服务器类型
        string serverAddress; //文件服务器地址
        string baseDir; //文件在文件服务器上的存储位置

        //文件节点信息
        short fileTypeId; //目标文件类型Id
        ["deprecate"] long fileLength; //目标文件大小
        string fileVersion; //文件版本号
        ["deprecate"] string fileChecksum; //文件校验和
        string majorTypeId; //文件所属专业编号
        string readOnlyKey; //只读版本在文件服务器上的存储名称
        long readOnlyFileLength; //只读版本文件长度
        string readOnlyFileMd5; //只读版本校验和
        string writableKey; //可写版本在文件服务器上的存储名称
        long writableFileLength; //可写版本文件长度
        string writableFileMd5; //可写版本校验和

        //镜像信息
        short mirrorTypeId; //镜像文件服务器类型
        string mirrorAddress; //镜像文件服务器地址
        string mirrorBaseDir; //文件在镜像文件服务器上的存储位置
        string readOnlyMirrorKey; //只读版本在本地的镜像
        string writableMirrorKey; //可写版本在本地的镜像

        //历史节点信息
        short actionTypeId; //文件操作类型
        string remark; //文件操作注解

        //操作者信息
        string lastModifyUserId; //操作者用户id
        string lastModifyRoleId; //操作者职责id

        ["deprecate"] string pid; //父节点编号
        ["deprecate"] string readFileScope; //只读版本在文件服务器上的存储位置
        ["deprecate:使用readOnlyKey代替"] string readFileKey; //只读版本在文件服务器上的存储名称
        ["deprecate:使用writableScope代替"] string writeFileScope; //可写版本在文件服务器上的存储位置
        ["deprecate:使用writableKey代替"] string writeFileKey; //可写版本在文件服务器上的存储名称
        ["deprecate:使用path代替"] string fullName; //要创建的节点名，可包含相对于父节点的路径
        ["deprecate"] string parentPath; //父节点的全路径
        ["deprecate"] short parentTypeId; //父节点的类型
        ["deprecate"] string parentStoragePath; //父节点相对路径
    };

    ["java:getset","clr:property"]
    struct QuerySummaryDTO { //汇总查询申请
        string companyId; //组织id
        string projectId; //项目id
        string ownerUserId; //拥有者用户id
    };
};