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


    ["java:getset","clr:property","deprecate"]
    struct MemberDTO {
        string id; //唯一编号
        string userId; //用户编号
        string userName; //用户名
        short memberTypeId; //承担角色id
        string memberTypeName; //承担角色名称
    };
    ["java:type:java.util.ArrayList<MemberDTO>"] sequence<MemberDTO> MemberList;


};