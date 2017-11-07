#pragma once

[["java:package:com.maoding.Common"]]
module zeroc {
    sequence<byte> ByteArray;
    ["java:serializable:java.lang.Integer"] sequence<byte> Integer;
    ["java:serializable:java.lang.Short"] sequence<byte> Short;
    ["java:serializable:java.lang.Long"] sequence<byte> Long;
    ["java:serializable:java.lang.String"] sequence<byte> String;
    ["java:serializable:java.lang.Boolean"] sequence<byte> Boolean;
    ["java:serializable:java.util.Date"] sequence<byte> Date;
    ["java:type:java.util.HashMap<String,String>"] dictionary<string,string> Map;
};