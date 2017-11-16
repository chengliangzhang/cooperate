#pragma once
#include <Common.ice>
#include <FileServer.ice>

[["java:package:com.maoding.Storage"]]
module zeroc {

    ["java:getset"]
    class CooperateFileNodeDTO {
        string id; //协同文件树节点编号id
        string pid; //协同文件父节点树编号
        string storageId; //协同文件编号
        string name; //协同文件名
        long fileLength; //文件长度
        string checksum; //协同文件md5
        string version; //协同文件版本号
        string specialtyId; //专业id
        string specialtyName; //专业文字说明
        string lastModifyAddress; //最后上传的地址
        short syncModeId; //同步模式，0-手动同步，1-自动更新
        string syncModeName; //同步模式文字说明
        int typeId; //文件类型
        string typeName; //文件类型文字说明
        bool locking; //是否锁定，false-不锁定，true-锁定
        string localFile; //本地文件路径，包含文件名
        string creatorPostId; //协同创建者的用户职责id
        string creatorPostName; //协同创建者名字
        Date createTime; //协同建立时间
        string lastModifyPostId; //最近更改协同的用户职责id
        string lastModifyPostName; //最近更改协同的用户名字
        Date lastModifyTime; //最近更改协同的时间
    };
    ["java:type:java.util.ArrayList<CooperateFileNodeDTO>"] sequence<CooperateFileNodeDTO> CooperateRelatedFileList;

    ["java:getset"]
    struct CooperateFileDTO {
        //本节点信息
        string id; //协同文件树节点编号id
        string storageId; //协同文件编号
        string name; //协同文件名
        long fileLength; //文件长度
        string checksum; //协同文件md5
        string version; //协同文件版本号
        string specialtyId; //专业id
        string specialtyName; //专业文字说明
        string lastModifyAddress; //最后上传的地址
        short syncModeId; //同步模式，0-手动同步，1-自动更新
        string syncModeName; //同步模式文字说明
        int typeId; //文件类型
        string typeName; //文件类型文字说明
        bool locking; //是否锁定，false-不锁定，true-锁定
        string localFile; //本地文件路径，包含文件名
        string creatorPostId; //协同创建者的用户职责id
        string creatorPostName; //协同创建者名字
        Date createTime; //协同建立时间
        string lastModifyPostId; //最近更改协同的用户职责id
        string lastModifyPostName; //最近更改协同的用户名字
        Date lastModifyTime; //最近更改协同的时间

        //子节点信息
        int referenceFileCount; //参考文件数量
        CooperateRelatedFileList referenceFileList; //参考文件列表
    };
    ["java:type:java.util.ArrayList<CooperateFileDTO>"] sequence<CooperateFileDTO> CooperateFileList;

    ["java:getset"]
    class CooperateDirNodeDTO {
        string id; //协同目录树节点编号id
        string pid; //协同目录父节点树编号
        string storageId; //协同目录id
        string name; //协同目录名
        string userId; //协同目录所属用户id
        string postId; //协同目录所属用户的职责id
        string postName; //所属用户名字
        string orgId; //所属组织id
        string orgName; //所属组织名字
        string projectId; //所属项目id
        string projectName; //所属项目名字
        string taskId; //所属任务id
        string taskName; //所属任务名字
        int typeId; //目录类别编号，如：0-系统默认目录,1-用户添加目录
        string typeName; //目录类别名字
        Date createTime; //目录建立时间

        int fileCount; //文件数量
        CooperateFileList fileList; //本目录文件列表
    };
    ["java:type:java.util.ArrayList<CooperateDirNodeDTO>"] sequence<CooperateDirNodeDTO> CooperateSubDirList;

    ["java:getset"]
    struct CooperateDirDTO {
        //本节点信息
        string id; //协同目录树节点编号id
        string pid; //协同目录父节点树编号
        string storageId; //协同目录id
        string name; //协同目录名
        string userId; //协同目录所属用户id
        string postId; //协同目录所属用户的职责id
        string postName; //所属用户名字
        string orgId; //所属组织id
        string orgName; //所属组织名字
        string projectId; //所属项目id
        string projectName; //所属项目名字
        string taskId; //所属任务id
        string taskName; //所属任务名字
        int typeId; //目录类别编号，如：0-系统默认目录,1-用户添加目录
        string typeName; //目录类别名字
        Date createTime; //目录建立时间

        int fileCount; //文件数量
        CooperateFileList fileList; //本目录文件列表

        //子节点信息
        int subDirCount; //子目录数量
        CooperateSubDirList subDirList; //子目录列表
    };
    ["java:type:java.util.ArrayList<CooperateDirDTO>"] sequence<CooperateDirDTO> CooperateDirList;


    ["java:getset"]
    struct CooperationQueryDTO {
        string userId; //协同目录所属用户id
        string postId; //协同目录所属用户的职责id
        string orgId; //所属组织id
        string projectId; //所属项目id
        string taskId; //所属任务id
    };

    interface StorageService {
        CooperateDirList listCooperationDir(CooperationQueryDTO query); //获取文件目录列表-4
        bool modifyFileInfo(CooperateFileDTO fileInfo); //更改文件信息-?
        FileRequestDTO requestUpload(CooperateFileDTO fileInfo,int mode); //申请上传文件
        FileRequestDTO requestDownload(CooperateFileDTO fileInfo,int mode); //申请下载文件
        DownloadResultDTO downloadFrom(DownloadRequestDTO request,string address,int mode); //远程下载-?
        CooperateFileDTO uploadCallback(Map params); //文件服务器上传时上传完毕后的回调函数-2
        void downloadCallback(Map params); //文件服务器下载时下载完毕后的回调函数-?
        CooperateFileDTO finishUpload(CooperateFileDTO fileInfo,FileDTO fileDTO); //客户端通知上传文件已结束-1
        void finishDownload(CooperateFileDTO fileInfo); //客户端通知下载文件结束-?
        bool replaceFile(CooperateFileDTO fileInfo,FileDTO fileDTO); //替换实体文件-?
        bool deleteFile(CooperateFileDTO fileInfo); //删除文件-?
        bool createDirectory(string path); //创建目录-?
        bool deleteDirectory(string path,bool force); //删除目录-?
        CooperateFileDTO duplicateFile(CooperateFileDTO fileInfo,string path); //复制文件-?
        CooperateFileDTO createFileLink(CooperateFileDTO fileInfo, string path); //创建文件链接-?
        bool duplicateDirectory(string path,string parent); //复制目录-?
        CooperateFileList listFileLink(FileDTO fileDTO); //查找文件记录-?
        bool restoreFile(CooperateFileDTO fileInfo); //恢复删除的文件-?
        bool restoreDirectory(string path); //恢复删除的目录-?
        bool lockFile(CooperateFileDTO fileInfo); //锁定文件-?
        bool unlockFile(CooperateFileDTO fileInfo); //解锁文件-?
        long getFree(CooperationQueryDTO query); //获取剩余空间-?
        bool isLock(CooperateFileDTO fileInfo); //获取文件锁定状态-?
        CooperateFileDTO getFileInfo(string fileId); //获取文件信息-?
        int getLinkCount(FileDTO fileDTO); //获取文件使用数量-?
        CooperateFileDTO createVersion(CooperateFileDTO fileInfo,string version); //添加文件版本-?
        bool abortUpload(CooperateFileDTO  fileInfo); //取消文件上传-3
        bool abortDownload(CooperateFileDTO fileInfo); //取消文件下载-?
    };
};