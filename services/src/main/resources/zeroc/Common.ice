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
        string classicId; //{ClassicId}替换字符串
        string classicName; //{Classic}替换字符串
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
        string actionId; //{ActionId}替换字符串
        string actionName; //{Action}替换字符串
        string majorName; //{Major}替换字符串
        string fileVersion; //{Version}替换字符串
        string srcPath; //{SrcPath}替换字符串
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

    enum ErrorCode {
        Unknown,Assert,InvalidParameter,NoPermission,DataNotFound,DataIsInvalid
    };

    ["java:getset","clr:property"]
    exception CustomException{
        ErrorCode code = Unknown;
        string msg = "未定义异常";
    };
};