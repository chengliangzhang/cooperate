#pragma once
#include <Common.ice>
#include <FileServer.ice>
#include <User.ice>
#include <Project.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {
    ["java:getset","clr:property"]
    struct SimpleNodeDTO { //节点信息（目录和文件通用信息）
        string id; //节点编号（树节点编号）
        string name; //节点名称（树节点名称或文件名称）
        string pid; //父节点编号
        short typeId; //节点类别编号
        ["deprecate"] string typeName; //节点类别名字
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

        //以下属性有可能被删除
        ["deprecate"] bool isValid; //节点是否有效
        ["deprecate"] string pNodeId; //父节点编号
    };
    ["java:type:java.util.ArrayList<SimpleNodeDTO>"] sequence<SimpleNodeDTO> SimpleNodeList;

    ["java:getset","clr:property"]
    struct HistoryDTO { //历史记录信息
        string id; //历史记录编号
        string fileId; //操作文件节点id
        string userId; //操作人员用户id
        string roleId; //操作人员职责id
        string userName; //操作人员名字
        short actionId; //操作动作编号
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

        //实际文件存储位置
        string fileServerTypeId; //文件服务器类型id
        string fileServerTypeName; //文件服务器类型名称
        string fileServerAddress; //文件服务器地址

        string readFileScope; //只读版本在文件服务器上的存储位置
        string readFileKey; //只读版本在文件服务器上的存储名称
        string writeFileScope; //可写版本在文件服务器上的存储位置
        string writeFileKey; //可写版本在文件服务器上的存储名称

        string mainFileId; //设计文件id

        HistoryList historyList; //文件相关历史列表

        ["deprecate"] string lastModifyAddress; //文件服务器地址
        ["deprecate"] string fileScope; //在文件服务器上的存储位置
        ["deprecate"] string fileKey; //在文件服务器上的存储名称

        ["deprecate"] string scope; //实际使用的存储位置
        ["deprecate"] string key; //实际使用的存储名称



        ["deprecate"] string ownerUserId; //节点所有者用户id
        ["deprecate"] string ownerDutyId; //节点所有者职责id
        ["deprecate"] string ownerName; //节点所有者名称
        ["deprecate"] string issueId; //所属签发任务id
        ["deprecate"] string issueName; //所属签发任务名字
        ["deprecate"] string taskId; //所属生产任务id
        ["deprecate"] string taskName; //所属生产任务名字
        ["deprecate"] string projectId; //所属项目id
        ["deprecate"] string projectName; //所属项目名字

        //以下属性有可能被删除
        ["deprecate"] short syncModeId; //同步模式
        ["deprecate"] string syncModeName; //同步模式文字说明
        ["deprecate"] string id; //节点编号（树节点编号）
        ["deprecate"] string name; //节点名称（树节点名称或文件名称）
        ["deprecate"] string pid; //父节点编号
        ["deprecate"] short typeId; //节点类别编号
        ["deprecate"] string typeName; //节点类别名字
        ["deprecate"] string path; //节点全路径
        ["deprecate"] long createTimeStamp; //节点建立时间
        ["deprecate"] string createTimeText; //节点建立时间文字
        ["deprecate"] long lastModifyTimeStamp; //节点最后修改时间
        ["deprecate"] string lastModifyTimeText; //节点最后修改时间文字
        ["deprecate"] bool isReadOnly; //节点是否只读（当前用户没有修改权限，或文件被其他用户锁定）
        ["deprecate"] long fileLength; //节点长度

        ["deprecate"] bool isValid; //节点是否有效
        ["deprecate"] bool locking; //是否锁定，false-不锁定，true-锁定
        ["deprecate"] string localFile; //本地文件路径，包含文件名
        ["deprecate"] string creatorDutyId; //协同创建者的用户职责id
        ["deprecate"] string creatorDutyName; //协同创建者名字
        ["deprecate"] string lastModifyDutyId; //最后更改协同的用户职责id
        ["deprecate"] string lastModifyDutyName; //最后更改协同的用户名字
        ["deprecate"] string organizationId; //文件所属公司id
        ["deprecate"] string organizationName; //文件所属公司名称
    };
    ["java:type:java.util.ArrayList<FileNodeDTO>"] sequence<FileNodeDTO> RelatedFileList;

    ["java:getset","clr:property","deprecate"]
    struct SimpleNodeWithSubDTO { //节点完整信息
        SimpleNodeDTO node; //本节点信息
        SimpleNodeList subNodeList; //子节点列表
    };

    ["java:getset","clr:property"]
    struct FullNodeDTO { //节点完整信息
        SimpleNodeDTO basic; //节点基本信息

        string projectId; //节点所属项目id
        string projectName; //节点所属项目名
        string classicId; //节点一级分类id
        string classicName; //节点一级分类名称
        string issueId; //节点所属签发任务id
        string issueName; //节点所属签发任务名
        string issuePath; //节点所属签发任务树路径
        string taskId; //节点所属生产任务id
        string taskName; //节点所属生产任务名
        string taskPath; //节点所属生产任务树路径
        string companyId; //节点所属组织id
        string companyName; //节点所属组织名
        string ownerDutyId; //节点所有者职责id
        string ownerUserId; //节点所有者用户id
        string ownerName; //节点所有者名称
        string storagePath; //自定义路径
    };
    ["java:type:java.util.ArrayList<FullNodeDTO>"] sequence<FullNodeDTO> FullNodeList;

    //此接口有可能被清除
    ["java:getset","clr:property","deprecate"]
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

    ["java:getset","clr:property","deprecate"]
    struct FileReviewDTO{
        string id;
    };
    ["java:type:java.util.ArrayList<FileReviewDTO>"] sequence<FileReviewDTO> FileReviewList;

    ["java:getset","clr:property","deprecate"]
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

    ["java:getset","clr:property","deprecate"]
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
        ["deprecate","protected"]
        string orgName; //所属组织名字
    };
    ["java:type:java.util.ArrayList<CooperateDirNodeDTO>"] sequence<CooperateDirNodeDTO> CooperateDirList;

    ["java:getset","clr:property","deprecate"]
    struct CooperateDirDTO { //目录内容
        CooperateDirNodeDTO node; //本目录信息
        CooperateDirList subDirList; //子目录列表
        CooperateFileList fileList; //文件列表（包含子目录内的文件）
    };

    ["java:getset","clr:property"]
    struct CreateNodeRequestDTO { //创建节点时的参数
        string pid; //父节点编号
        string fullName; //要创建的节点名，可包含相对于父节点的路径
        short typeId; //节点类型
        long fileLength; //目标文件大小

        //加速信息
        string userId; //创建者的用户id
        string parentPath; //父节点的全路径
        string parentStoragePath; //父节点Storage路径
        string taskId; //生产任务Id

        //文件节点特有信息
        short fileTypeId; //目标文件类型Id
        string fileVersion; //文件版本号
        string remark; //文件操作注解

        ["deprecate"] string path; //相对路径或绝对路径，包含节点名称
        ["deprecate"] string dutyId; //创建者的职责id
        ["deprecate"] string companyId; //创建组织id
        ["deprecate"] string projectId; //项目Id
        ["deprecate"] string classicId; //节点分类id
        ["deprecate"] string issueId; //签发任务id
        ["deprecate"] string pNodeId; //父节点编号
        ["deprecate"] string orgId; //创建组织的orgId
    };


    ["java:getset","clr:property"]
    struct NodeModifyRequestDTO { //节点更改申请
        string name; //节点名称（树节点名称或文件名称）
        string pNodeId; //父节点编号
    };

    ["java:getset","clr:property","deprecate"]
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

    ["java:getset","clr:property"]
    struct StorageQueryDTO {
        string id; //树节点id
        string pid; //父节点id
        string path; //树节点全路径
        string typeId; //节点类型
        string projectId; //节点所属项目id
        string classicId; //节点所属分类类型
        string issueId; //节点所属签发任务id
        string taskId; //节点所属生产任务id
        string companyId; //节点所属组织id
        string userId; //节点拥有者用户id
        string dutyId; //节点拥有者职责id

        ["deprecate"] string orgId; //所要查询的组织id
        ["deprecate"] string nodeId; //所要查询的协同树节点id
        ["deprecate"] string fullName; //所要查询协同树节点全路径名
    };

    ["java:getset","clr:property"]
    struct CommitRequestDTO {
        string path; //所要提资的树节点全路径名
        string major; //专业名称
        string title; //提资标题
        string remark; //提资说明
    };
    ["java:type:java.util.ArrayList<CommitRequestDTO>"] sequence<CommitRequestDTO> CommitRequestList;


    interface StorageService {
        int writeFile(SimpleNodeDTO node); //写入文件
        int writeFileForAccount(AccountDTO account,SimpleNodeDTO node); //写入文件
        ByteArray readFile(SimpleNodeDTO node,long pos,int size); //从文件读取数据
        ByteArray readFileForAccount(AccountDTO account,SimpleNodeDTO node,long pos,int size); //从文件读取数据


        FullNodeDTO getFullNodeInfoByPath(string path); //通过路径获取节点详细信息
        FullNodeDTO getFullNodeInfoByPathForAccount(AccountDTO account,string path); //通过路径获取指定账号的节点详细信息
        FullNodeDTO getFullNodeInfoById(string id); //通过id获取节点详细信息
        FullNodeDTO getFullNodeInfoByIdForAccount(AccountDTO account,string id); //通过id获取指定账号的节点详细信息
        ["deprecate:转为私有"] FullNodeDTO getFullNodeInfo(StorageQueryDTO query); //获取节点详细信息
        ["deprecate:转为私有"] FullNodeDTO getFullNodeInfoForAccount(AccountDTO account,StorageQueryDTO query); //获取指定账号节点详细信息

        FullNodeDTO getNearFullNodeInfoByPath(string path); //通过路径获取指定位置附近的节点详细信息
        FullNodeDTO getNearFullNodeInfoByPathForAccount(AccountDTO account,string path); //通过路径获取指定账号的指定位置附近的节点详细信息
        ["deprecate:转为私有"] FullNodeDTO getNearFullNodeInfo(StorageQueryDTO query); //获取指定位置附近的节点详细信息
        ["deprecate:转为私有"] FullNodeDTO getNearFullNodeInfoForAccount(AccountDTO account,StorageQueryDTO query); //获取指定账号指定位置附近的节点详细信息

        SimpleNodeList commitFileList(CommitRequestList requestList); //批量提资
        SimpleNodeList commitFileListForAccount(AccountDTO account,CommitRequestList requestList); //批量提资
        SimpleNodeList listAllSubNodeByPath(string path); //获取指定用户指定路径的所有子节点
        SimpleNodeList listAllSubNodeByPathForAccount(AccountDTO account,string path); //获取指定用户指定路径的所有子节点

        //已经实现的接口
        SimpleNodeDTO commitFile(CommitRequestDTO request); //提资
        SimpleNodeDTO commitFileForAccount(AccountDTO account, CommitRequestDTO request); //提资
        StringList listMajor(); //列出可用专业

        ["deprecate:尚未验证"]
        ProjectDTO getProjectInfoByPath(string path);
        ["deprecate:尚未验证"]
        ProjectDTO getProjectInfoByPathForAccount(AccountDTO account,string path);
        SimpleNodeDTO createStorageNode(CreateNodeRequestDTO request); //创建树节点,返回节点信息
        SimpleNodeDTO createStorageNodeForAccount(AccountDTO account,CreateNodeRequestDTO request); //创建树节点,返回节点信息
        SimpleNodeDTO createCustomerDirForAccount(AccountDTO account,CreateNodeRequestDTO request); //创建目录,返回节点信息
        SimpleNodeDTO createCustomerDir(CreateNodeRequestDTO request); //创建目录,返回节点信息
        SimpleNodeDTO createCustomerFileForAccount(AccountDTO account,CreateNodeRequestDTO request); //创建文件,返回节点信息
        SimpleNodeDTO createCustomerFile(CreateNodeRequestDTO request); //创建文件,返回节点信息
        SimpleNodeList listRootNodeForAccount(AccountDTO account); //获取指定账号的根节点
        ["deprecate:使用listRootNode代替"] SimpleNodeList listRootNodeForCurrent(); //获取当前账号的根节点
        SimpleNodeList listRootNode(); //获取当前账号的根节点
        SimpleNodeDTO getNodeByPathForAccount(AccountDTO account,string path); //根据路径和指定用户获取指定节点信息
        ["deprecate:使用getNodeByPath代替"] SimpleNodeDTO getNodeByPathForCurrent(string path); //根据路径获取当前用户指定节点信息
        SimpleNodeDTO getNodeByPath(string path); //根据路径获取当前用户指定节点信息
        SimpleNodeDTO getNodeByIdForAccount(AccountDTO account,string id); //根据id获取指定用户可看到的指定节点信息
        ["deprecate:使用getNodeById代替"] SimpleNodeDTO getNodeByIdForCurrent(string id); //根据id获取当前用户可看到的指定节点信息
        SimpleNodeDTO getNodeById(string id); //根据id获取当前用户可看到的指定节点信息
        SimpleNodeList listSubNodeByPathForAccount(AccountDTO account,string path); //获取指定用户指定路径的一层子节点
        ["deprecate:使用listSubNodeByPath代替"] SimpleNodeList listSubNodeByPathForCurrent(string path); //获取当前用户指定路径的一层子节点
        SimpleNodeList listSubNodeByPath(string path); //获取当前用户指定路径的一层子节点
        SimpleNodeList listSubNodeByPNodeIdForAccount(AccountDTO account,string pid); //获取指定用户指定节点的一层子节点
        ["deprecate:使用listSubNodeByPNodeId代替"] SimpleNodeList listSubNodeByPNodeIdForCurrent(string pid); //获取当前用户指定节点的一层子节点
        SimpleNodeList listSubNodeByPNodeId(string pid); //获取当前用户指定节点的一层子节点
        string createNode(CreateNodeRequestDTO request); //创建树节点,返回目录树节点id
        string createNodeForAccount(AccountDTO account,CreateNodeRequestDTO request); //创建树节点,返回目录树节点id
        FileRequestDTO openFile(string path); //打开文件准备上传及下载
        ["deprecate:使用openFile代替"] FileRequestDTO openFileForCurrent(string path); //打开文件准备上传及下载
        FileRequestDTO openFileForAccount(AccountDTO account,string path); //打开文件准备上传及下载
        SimpleNodeList listAllNode(); //获取指定账号所有节点
        SimpleNodeList listAllNodeForAccount(AccountDTO account); //获取指定账号所有节点
        bool isDirectoryEmpty(string path); //根据路径判断目录是否为空
        bool isDirectoryEmptyForAccount(AccountDTO account,string path); //根据路径判断目录是否为空
        ["deprecate:使用closeFile代替"] bool closeFileForCurrent(string path); //关闭文件
        bool closeFile(string path); //关闭文件
        bool closeFileForAccount(AccountDTO account,string path); //关闭文件准备上传及下载
        bool setFileLength(string path,long fileLength); //调整文件大小
        bool setFileLengthForAccount(AccountDTO account,string path,long fileLength); //调整文件大小

        ["deprecate"] bool canBeDeleted(string path); //判断节点是否可被删除
        long getUsage(StorageQueryDTO query); //获取已使用空间
        ["deprecate"] bool lockNode(string path,string userId); //通过路径锁定树节点
        ["deprecate"] bool unlockNode(string path,string userId); //通过路径解锁树节点
        ["deprecate"] bool isLocking(string path); //通过路径判断树节点是否被锁
        ["deprecate"] CooperateDirNodeDTO getDirNodeInfo(string path); //获取目录节点详细信息
        FileNodeDTO getFileNodeInfo(string path); //获取目录节点详细信息
        SimpleNodeDTO moveNode(string oldPath,string newPath); //移动或更名节点
        SimpleNodeDTO moveNodeForAccount(AccountDTO account,string oldPath,string newPath); //移动或更名节点
        bool deleteNode(string path,bool force); //删除树节点
        bool deleteNodeForAccount(AccountDTO account,string path,bool force); //删除树节点
        ["deprecate"] FileRequestDTO requestUploadByPath(string path,string userId); //申请上传文件
        ["deprecate"] FileRequestDTO requestDownloadByPath(string path,string userId); //申请下载文件
        ["deprecate"] void finishUploadById(string nodeId,string userId); //通告上传文件结束


        //有可能被删除的接口
        ["deprecate:替换为listRootNodeForAccount"]
        SimpleNodeList listSubNode(string path); //获取一层子节点简单信息
        ["deprecate:替换为getNodeByPathForAccount"]
        SimpleNodeDTO getSimpleNodeInfo(string path); //根据路径获取单节点简单信息
        ["deprecate:替换为getProjectInfoByPathForAccount、getTaskInfoByPathForAccount等"]
        CooperateDirDTO getCooperateDirInfo(CooperationQueryDTO query); //获取目录详细信息
        ["deprecate"] bool lockFile(string fileId,string address); //锁定文件
        ["deprecate"] bool changeNodeInfo(NodeModifyRequestDTO request,string nodeId); //更改节点信息
        ["deprecate"] bool initNodeInfo(CreateNodeRequestDTO request); //初始化树节点信息
        ["deprecate"] bool modifyFileInfo(CooperateFileDTO fileInfo); //更改文件信息-?
        ["deprecate"] FileRequestDTO requestUpload(CooperateFileDTO fileInfo,int mode); //申请上传文件
        ["deprecate"] FileRequestDTO requestDownload(CooperateFileDTO fileInfo,int mode); //申请下载文件
        ["deprecate"] FileRequestDTO requestDownloadFromLast(CooperateFileDTO fileInfo,int mode); //申请从最后上传者那里下载文件
        ["deprecate"] CooperateFileDTO uploadCallback(Map params); //文件服务器上传时上传完毕后的回调函数-?
        ["deprecate"] void downloadCallback(Map params); //文件服务器下载时下载完毕后的回调函数-?
        ["deprecate"] void finishUpload(FileRequestDTO request,bool succeeded); //客户端通知结束或取消上传文件
        ["deprecate"] void finishDownload(FileRequestDTO request,bool succeeded); //客户端通知结束或取消下载文件-?
        ["deprecate"] bool replaceFile(CooperateFileDTO fileInfo,FileDTO fileDTO); //替换实体文件-?
        ["deprecate"] bool deleteFile(CooperateFileDTO fileInfo); //删除文件
        ["deprecate"] string createDirectory(CreateNodeRequestDTO request); //创建目录,返回目录树节点路径
        ["deprecate"] bool deleteDirectory(string nodeId,bool force); //删除目录
        ["deprecate"] string createFile(CreateNodeRequestDTO request); //创建文件，返回文件树节点路径
        ["deprecate"] CooperateFileDTO duplicateFile(CooperateFileDTO fileInfo,string path); //复制文件-?
        ["deprecate"] CooperateFileDTO createFileLink(CooperateFileDTO fileInfo, string path); //创建文件链接-?
        ["deprecate"] bool duplicateDirectory(string path,string parent); //复制目录-?
        ["deprecate"] CooperateFileList listFileLink(FileDTO fileDTO); //查找文件记录
        ["deprecate"] bool restoreFile(CooperateFileDTO fileInfo); //恢复删除的文件-?
        ["deprecate"] bool restoreDirectory(string path); //恢复删除的目录-?
        ["deprecate"] bool unlockFile(string fileId); //解锁文件
        ["deprecate"] bool isFileLocking(string fileId); //获取文件锁定状态
        ["deprecate"] CooperateFileDTO getFileInfo(string nodeId); //获取文件信息
        ["deprecate"] int getLinkCount(FileDTO fileDTO); //获取文件使用数量-?
        ["deprecate"] string createVersion(CooperateFileDTO fileInfo,string version); //添加文件版本
    };
};