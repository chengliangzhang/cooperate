#pragma once
#include <Common.ice>
#include <FileServer.ice>
#include <User.ice>
#include <Project.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    ["java:getset"]
    struct SimpleNodeDTO { //节点信息（统一目录和文件信息）
        //节点、文件、目录通用信息
        string id; //节点编号（树节点编号）
        string name; //节点名称（树节点名称或文件名称）
        string pid; //父节点编号
        short typeId; //节点类别编号
        string typeName; //节点类别名字
        string path; //节点全路径
        long createTimeStamp; //节点建立时间
        string createTimeText; //节点建立时间文字
        long lastModifyTimeStamp; //节点最后修改时间
        string lastModifyTimeText; //节点最后修改时间文字
        bool isReadOnly; //节点是否只读（当前用户没有修改权限，或文件被其他用户锁定）
        long fileLength; //节点长度
        string owner_duty_id; //节点所有者职责id
        string owner_name; //节点所有者名称

        //节点特有信息
        bool isDirectory; //节点是否目录
        bool isProjectDirectory; //节点是否项目目录
        bool isTaskDirectory; //节点是否任务目录

        //以下属性有可能被删除
        bool isValid; //节点是否有效
        string pNodeId; //父节点编号
    };
    ["java:type:java.util.ArrayList<SimpleNodeDTO>"] sequence<SimpleNodeDTO> SimpleNodeList;

    ["java:getset"]
    struct NodeDTO { //节点完整信息
        SimpleNodeDTO node; //本节点信息
        SimpleNodeList subNodeList; //子节点列表
    };

    ["java:getset"]
    struct FileNodeDTO { //文件信息
        //节点、文件、目录通用信息
        string id; //节点编号（树节点编号）
        string name; //节点名称（树节点名称或文件名称）
        string pid; //父节点编号
        short typeId; //节点类别编号
        string typeName; //节点类别名字
        string path; //节点全路径
        long createTimeStamp; //节点建立时间
        string createTimeText; //节点建立时间文字
        long lastModifyTimeStamp; //节点最后修改时间
        string lastModifyTimeText; //节点最后修改时间文字
        bool isReadOnly; //节点是否只读（当前用户没有修改权限，或文件被其他用户锁定）
        long fileLength; //节点长度
        string owner_duty_id; //节点所有者职责id
        string owner_name; //节点所有者名称

        //文件节点特有信息
        string fileChecksum; //文件校验和
        string fileVersion; //文件版本号
        short fileTypeId; //文件类型
        string fileTypeName; //文件类型文字说明
        string organizationId; //文件所属公司id
        string organizationName; //文件所属公司名称
        short syncModeId; //同步模式
        string syncModeName; //同步模式文字说明
        string lastModifyAddress; //最后上传的地址
        string fileServerTypeId; //文件服务器类型id
        string fileServerTypeName; //文件服务器类型名称
        string fileScope; //在文件服务器上的存储位置
        string fileKey; //在文件服务器上的存储名称

        //以下属性有可能被删除
        bool isValid; //节点是否有效
        bool locking; //是否锁定，false-不锁定，true-锁定
        string localFile; //本地文件路径，包含文件名
        string creatorDutyId; //协同创建者的用户职责id
        string creatorDutyName; //协同创建者名字
        string lastModifyDutyId; //最后更改协同的用户职责id
        string lastModifyDutyName; //最后更改协同的用户名字
    };
    ["java:type:java.util.ArrayList<FileNodeDTO>"] sequence<FileNodeDTO> RelatedFileList;

    struct FileReviewNodeDTO { //文件校审信息
        string id; //文件校审记录编号
        string fileId; //文件节点编号
        string dutyId; //校审人员职责编号
        string dutyName; //校审人员名字
        short actionId; //校审动作编号
        string actionName; //校审动作名称
        long actionTimeStamp; //校审时间
        string actionTimeText; //校审时间文字
    };
    ["java:type:java.util.ArrayList<FileReviewNodeDTO>"] sequence<FileReviewNodeDTO> FileReviewList;

    //此接口有可能被清除
    struct FileVersionDTO { //版本信息
        string id; //协同文件编号
        string nodeId; //协同文件树节点编号
        string fileVersion; //协同文件版本号
        string lastModifyAddress; //最后上传的地址
        bool locking; //是否锁定，false-不锁定，true-锁定
        long createTimeStamp; //版本建立时间
        string createTimeText; //版本建立时间文字
        string lastModifyDutyId; //最近更改此版本的用户职责id
        string lastModifyDutyName; //最近更改此版本的用户名字
        long lastModifyTimeStamp; //最近更改此版本的时间
        string lastModifyTimeText; //最近更改此版本的时间文字
    };
    ["java:type:java.util.ArrayList<FileVersionDTO>"] sequence<FileVersionDTO> FileVersionList;


    ["java:getset"]
    struct CooperateFileDTO { //文件完整信息
        FileNodeDTO node; //本文件信息
        RelatedFileList referenceFileList; //参考文件列表
        FileReviewList reviewList; //文件校审记录列表


        //以下属性有可能被删除
        FileVersionList versionList; //历史版本列表
        string id; //协同文件编号
        string name; //协同文件名
        string nodeId; //协同文件树节点编号
        string pNodeId; //协同文件父节点编号
        string pathName; //协同文件所在路径文字说明
        long fileLength; //文件长度
        string fileChecksum; //协同文件校验和
        string fileVersion; //协同文件版本号
        string specialtyId; //专业id
        string specialtyName; //专业文字说明
        string lastModifyAddress; //最后上传的地址
        short syncModeId; //同步模式，0-手动同步，1-自动更新
        string syncModeName; //同步模式文字说明
        short typeId; //文件类型
        string typeName; //文件类型文字说明
        bool locking; //是否锁定，false-不锁定，true-锁定
        string localFile; //本地文件路径，包含文件名
        string creatorDutyId; //协同创建者的用户职责id
        string creatorDutyName; //协同创建者名字
        long createTimeStamp; //协同建立时间
        string createTimeText; //协同建立时间文字
        string lastModifyDutyId; //最近更改协同的用户职责id
        string lastModifyDutyName; //最近更改协同的用户名字
        long lastModifyTimeStamp; //最近更改协同的时间
        string lastModifyTimeText; //最近更改协同的时间文字
    };
    ["java:type:java.util.ArrayList<CooperateFileDTO>"] sequence<CooperateFileDTO> CooperateFileList;

    ["java:getset"]
    struct CooperateDirNodeDTO {
        //节点、文件、目录通用信息
        string id; //节点编号（树节点编号）
        string name; //节点名称（树节点名称或文件名称）
        string pNodeId; //父节点编号
        short typeId; //节点类别编号
        string typeName; //节点类别名字
        long createTimeStamp; //节点建立时间
        string createTimeText; //节点建立时间文字
        long lastModifyTimeStamp; //节点最后修改时间
        string lastModifyTimeText; //节点最后修改时间文字
        bool isReadOnly; //节点是否只读（当前用户没有修改权限，或文件被其他用户锁定）
        long fileLength; //节点长度

        //目录节点特有信息
        bool isSystem; //是否系统目录
        bool isBackup; //是否备份目录
        string fullName; //目录全路径名
        string projectId; //所属项目id
        string projectName; //所属项目名字
        string taskId; //所属任务id
        string taskName; //所属任务名字

        //以下属性有可能被删除
        bool isValid; //节点是否有效
        string userId; //协同目录所属用户id
        string dutyId; //协同目录所属用户的职责id
        string userName; //所属用户名字
        string aliasName; //所属用户别名
        string orgId; //所属组织id
        string orgName; //所属组织名字
    };
    ["java:type:java.util.ArrayList<CooperateDirNodeDTO>"] sequence<CooperateDirNodeDTO> CooperateDirList;

    ["java:getset"]
    struct CooperateDirDTO { //目录内容
        CooperateDirNodeDTO node; //本目录信息
        CooperateDirList subDirList; //子目录列表
        CooperateFileList fileList; //文件列表（包含子目录内的文件）
    };

    ["java:getset"]
    struct CreateNodeRequestDTO { //创建节点时的参数
        string pNodeId; //父节点编号
        short typeId; //目标节点类型Id
        short fileTypeId; //目标文件类型Id
        string userId; //创建者的userId
        string dutyId; //创建者的dutyId
        string orgId; //创建组织的orgId
        string projectId; //项目Id
        string taskId; //任务Id
        string fullName; //创建节点相对于父节点的全路径
    };


    ["java:getset"]
    struct NodeModifyRequestDTO { //节点更改申请
        string name; //节点名称（树节点名称或文件名称）
        string pNodeId; //父节点编号
    };

    ["java:getset"]
    struct CooperationQueryDTO {
        string nodeId; //协同树节点id
        string pNodeId; //协同父节点id
        string nodeName; //协同节点名称
        string userId; //协同目录所属用户id
        string dutyId; //协同目录所属用户的职责id
        string orgId; //所属组织id
        string projectId; //所属项目id
        string taskId; //所属任务id
        string scope; //要查找的文件在文件服务器上的域
        string key; //要查找的文件在文件服务器上的标识
        int level; //要从树节点向下查找多少层
    };

    ["java:getset"]
    struct StorageQueryDTO {
        string nodeId; //所要查询的协同树节点id
        string fullName; //所要查询协同树节点全路径名
        string userId; //所要查询的用户id
        string dutyId; //所要查询的的用户职责id
        string orgId; //所要查询的组织id
        string projectId; //所要查询的项目id
        string taskId; //所要查询的任务id
    };

    interface StorageService {
        //准备实现的接口
        ProjectDTO getProjectInfoByPath(string path);
        ProjectDTO getProjectInfoByPathForAccount(AccountDTO account,string path);

        //已经实现的接口
        SimpleNodeList listRootNodeForAccount(AccountDTO account); //获取指定账号的根节点
        SimpleNodeList listRootNodeForCurrent(); //获取当前账号的根节点
        SimpleNodeDTO getNodeByPathForAccount(AccountDTO account,string path); //根据路径和指定用户获取指定节点信息
        SimpleNodeDTO getNodeByPathForCurrent(string path); //根据路径获取当前用户指定节点信息
        SimpleNodeDTO getNodeByIdForAccount(AccountDTO account,string id); //根据id获取指定用户可看到的指定节点信息
        SimpleNodeDTO getNodeByIdForCurrent(string id); //根据id获取当前用户可看到的指定节点信息
        SimpleNodeList listSubNodeByPathForAccount(AccountDTO account,string path); //获取指定用户指定路径的一层子节点
        SimpleNodeList listSubNodeByPathForCurrent(string path); //获取当前用户指定路径的一层子节点
        SimpleNodeList listSubNodeByPNodeIdForAccount(AccountDTO account,string pid); //获取指定用户指定节点的一层子节点
        SimpleNodeList listSubNodeByPNodeIdForCurrent(string pid); //获取当前用户指定节点的一层子节点
        string createNode(CreateNodeRequestDTO request); //创建树节点,返回目录树节点id
        FileRequestDTO openFileForCurrent(string path); //打开文件准备上传及下载
        FileRequestDTO openFileForAccount(AccountDTO account,string path); //打开文件准备上传及下载
        SimpleNodeList listAllNodeForAccount(AccountDTO account); //获取指定账号所有节点
        bool isDirectoryEmpty(string path); //根据路径判断目录是否为空
        bool closeFileForCurrent(string path); //关闭文件
        bool closeFileForAccount(AccountDTO account,string path); //关闭文件准备上传及下载
        bool setFileLength(string path,long fileLength); //调整文件大小

        bool canBeDeleted(string path); //判断节点是否可被删除
        long getUsage(StorageQueryDTO query); //获取已使用空间
        bool lockNode(string path,string userId); //通过路径锁定树节点
        bool unlockNode(string path,string userId); //通过路径解锁树节点
        bool isLocking(string path); //通过路径判断树节点是否被锁
        CooperateDirNodeDTO getDirNodeInfo(string path); //获取目录节点详细信息
        FileNodeDTO getFileNodeInfo(string path); //获取目录节点详细信息
        bool moveNode(string oldPath,string newPath); //移动或更名节点
        bool deleteNode(string path,bool force); //删除树节点
        FileRequestDTO requestUploadByPath(string path,string userId); //申请上传文件
        FileRequestDTO requestDownloadByPath(string path,string userId); //申请下载文件
        void finishUploadById(string nodeId,string userId); //通告上传文件结束


        //有可能被删除的接口
        SimpleNodeList listSubNode(string path); //获取一层子节点简单信息
        SimpleNodeDTO getSimpleNodeInfo(string path); //根据路径获取单节点简单信息

        CooperateDirDTO getCooperateDirInfo(CooperationQueryDTO query); //获取目录详细信息
        bool lockFile(string fileId,string address); //锁定文件
        NodeDTO getNodeInfo(CooperationQueryDTO query); //获取节点简单信息
        bool changeNodeInfo(NodeModifyRequestDTO request,string nodeId); //更改节点信息
        bool initNodeInfo(CreateNodeRequestDTO request); //初始化树节点信息
        bool modifyFileInfo(CooperateFileDTO fileInfo); //更改文件信息-?
        FileRequestDTO requestUpload(CooperateFileDTO fileInfo,int mode); //申请上传文件
        FileRequestDTO requestDownload(CooperateFileDTO fileInfo,int mode); //申请下载文件
        FileRequestDTO requestDownloadFromLast(CooperateFileDTO fileInfo,int mode); //申请从最后上传者那里下载文件
        CooperateFileDTO uploadCallback(Map params); //文件服务器上传时上传完毕后的回调函数-?
        void downloadCallback(Map params); //文件服务器下载时下载完毕后的回调函数-?
        void finishUpload(FileRequestDTO request,bool succeeded); //客户端通知结束或取消上传文件
        void finishDownload(FileRequestDTO request,bool succeeded); //客户端通知结束或取消下载文件-?
        bool replaceFile(CooperateFileDTO fileInfo,FileDTO fileDTO); //替换实体文件-?
        bool deleteFile(CooperateFileDTO fileInfo); //删除文件
        string createDirectory(CreateNodeRequestDTO request); //创建目录,返回目录树节点路径
        bool deleteDirectory(string nodeId,bool force); //删除目录
        string createFile(CreateNodeRequestDTO request); //创建文件，返回文件树节点路径
        CooperateFileDTO duplicateFile(CooperateFileDTO fileInfo,string path); //复制文件-?
        CooperateFileDTO createFileLink(CooperateFileDTO fileInfo, string path); //创建文件链接-?
        bool duplicateDirectory(string path,string parent); //复制目录-?
        CooperateFileList listFileLink(FileDTO fileDTO); //查找文件记录
        bool restoreFile(CooperateFileDTO fileInfo); //恢复删除的文件-?
        bool restoreDirectory(string path); //恢复删除的目录-?
        bool unlockFile(string fileId); //解锁文件
        bool isFileLocking(string fileId); //获取文件锁定状态
        CooperateFileDTO getFileInfo(string nodeId); //获取文件信息
        int getLinkCount(FileDTO fileDTO); //获取文件使用数量-?
        string createVersion(CooperateFileDTO fileInfo,string version); //添加文件版本
    };
};