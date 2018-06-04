#pragma once

[["java:package:com.maoding.common"]]
module zeroc {
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
        string designTaskPath; //{TaskPath}替换字符串
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
        string path; //{SrcPath}替换字符串
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
    struct UpdateAskDTO { //记录更新申请
        string lastModifyUserId; //最后编辑用户编号
        string lastModifyRoleId; //最后编辑角色编号
    };

    ["java:getset","clr:property"]
    struct UpdateAskTreeDTO { //记录更新申请-树节点记录
        string typeId; //修改后的节点类型
        string pid; //修改后的父节点编号
        string path; //修改后的节点名称或路径
        string lastModifyUserId; //申请修改的用户编号
        string lastModifyRoleId; //申请修改的角色编号
    };

    ["java:getset","clr:property"]
    struct QueryAskDTO { //记录查询条件
        string id; //记录id
        string lastModifyUserId; //最后更改者用户id
        string lastModifyRoleId; //最后更改者职责id
        long startTimeStamp; //起始时间
        long endTimeStamp; //终止时间
        string accountId; //查询者用户id
    };

    ["java:getset","clr:property"]
    struct QueryAskTreeDTO { //记录查询条件-树节点记录
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
    struct DeleteAskDTO { //记录删除申请
        bool isReal; //是否真正删除，真-完全删除，不可恢复，假-标记删除，可以恢复
        string lastModifyUserId; //最后编辑用户id
        string lastModifyRoleId; //最后编辑角色id
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