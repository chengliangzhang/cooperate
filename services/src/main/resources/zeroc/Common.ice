#pragma once

[["java:package:com.maoding.Common"]]
module zeroc {
    ["java:serializable:java.lang.Integer","deprecate"] sequence<byte> Integer;
    ["java:serializable:java.lang.Short","deprecate"] sequence<byte> Short;
    ["java:serializable:java.lang.Long","deprecate"] sequence<byte> Long;
    ["java:serializable:java.lang.String","deprecate"] sequence<byte> String;
    ["java:serializable:java.lang.Boolean","deprecate"] sequence<byte> Boolean;
    ["java:serializable:java.util.Date","deprecate"] sequence<byte> Date;

    sequence<byte> ByteArray;
    ["java:type:java.util.HashMap<String,String>"] dictionary<string,string> Map;
    ["java:type:java.util.ArrayList<String>"] sequence<string> StringList;
    ["java:type:java.util.ArrayList<Short>"]sequence<short> ShortList;

    ["java:getset","clr:property"]
    struct IdNameDTO {
        string id; //唯一标识
        string name; //对应名称
    };
    ["java:type:java.util.ArrayList<IdNameDTO>"] sequence<IdNameDTO> IdNameList;

    ["java:getset","clr:property"]
    struct PageQueryDTO {
        int pageIndex; //分页查询时的页面大小
        int pageSize; //分页查询时的页面大小
    };
    ["java:type:java.util.ArrayList<PageQueryDTO>"] sequence<PageQueryDTO> PageQueryList;

    ["java:getset","clr:property"]
    struct StringElementDTO {
        string projectId; //{ProjectId}替换字符串
        string projectName; //{Project}替换字符串
        string rangeId; //{RangeId}替换字符串
        string rangeName; //{Range}替换字符串
        string issueId; //{IssueId}替换字符串
        string issueName; //{Issue}替换字符串
        string issuePath; //{IssuePath}替换字符串
        string taskId; //{TaskId}替换字符串
        string taskName; //{Task}替换字符串
        string taskPath; //{TaskPath}替换字符串
        string companyId; //{CompanyId}替换字符串
        string companyName; //{Company}替换字符串
        string userId; //{UserId}替换字符串
        string userName; //{User}替换字符串
        string ownerUserId; //{OwnerUserId}替换字符串
        string ownerUserName; //{Owner}替换字符串
        string actionId; //{ActionId}替换字符串
        string actionName; //{Action}替换字符串
        string majorName; //{Major}替换字符串
        string fileVersion; //{Version}替换字符串
        string srcPath; //{SrcPath}替换字符串
        string skyPid; //{SkyPid}替换字符串
    };
    ["java:type:java.util.ArrayList<StringElementDTO>"] sequence<StringElementDTO> StringElementList;
    
    ["java:getset","clr:property","deprecate"]
    struct MemberDTO {
        string id; //唯一编号
        string userId; //用户编号
        string userName; //用户名
        short memberTypeId; //承担角色id
        string memberTypeName; //承担角色名称
    };
    ["java:type:java.util.ArrayList<MemberDTO>"] sequence<MemberDTO> MemberList;

    ["java:getset","clr:property"]
    struct CommonUpdateDTO { //通用的元素创建及更新信息
        long lastModifyTimeStamp; //最后修改时间
        string lastModifyUserId; //最后编辑用户id
        string lastModifyRoleId; //最后编辑角色id
    };

    ["java:getset","clr:property"]
    struct CommonUpdateTreeDTO { //通用的树节点元素创建及更新信息
        string pid; //修改后的父节点id
        string name; //修改后的节点名称
        string path; //修改后的节点路径
        long lastModifyTimeStamp; //申请修改的时间
        string lastModifyUserId; //申请修改的用户id
        string lastModifyRoleId; //申请修改的角色id
    };

    ["java:getset","clr:property"]
    struct CommonQueryDTO { //通用的查询条件
        string id; //记录id
        string name; //记录名称
        string typeId; //记录类型
        string projectId; //记录所属项目id
        string issueId; //记录所属签发任务id
        string taskId; //记录所属生产任务id
        string companyId; //记录所属组织id
        string lastModifyUserId; //最后更改者用户id
        string lastModifyRoleId; //最后更改者职责id
        string accountId; //查询者用户id
        long startTimeStamp; //起始时间
        long endTimeStamp; //终止时间

        string fuzzyId; //模糊匹配id字符串
    };

    ["java:getset","clr:property"]
    struct CommonQueryTreeDTO { //通用的树节点查询条件
        string id; //记录编号
        string name; //记录名称
        string typeId; //记录类型
        string projectId; //记录所属项目id
        string issueId; //记录所属签发任务id
        string taskId; //记录所属生产任务id
        string companyId; //记录所属组织id
        string lastModifyUserId; //最后更改者用户id
        string lastModifyRoleId; //最后更改者职责id
        long startTimeStamp; //起始时间
        long endTimeStamp; //终止时间

        string fuzzyId; //模糊匹配id字符串

        string pid; //记录父节点编号
        string path; //记录路径
        string parentPath; //父路径
        string rangeId; //记录分类节点编号

        string fuzzyPath; //模糊匹配路径
    };

    enum ErrorCode {
        Unknown,Assert,InvalidParameter,NoPermission,DataNotFound,DataIsInvalid
    };

    ["java:getset","clr:property"]
    exception CustomException{
        ErrorCode code = Unknown;
        string message = "未定义异常";
    };
};