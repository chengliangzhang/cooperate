#pragma once
#include <data/CommonData.ice>

[["java:package:com.maoding.common"]]
module zeroc {
    interface ConstService {
        string getTitle(ConstQuery query); //获取指定常量的标题
        string getExtra(ConstQuery query); //获取指定常量的控制字符串

        VersionList listVersion(VersionQuery query); //查询版本信息
        VersionSimpleDTO getNewestVersion(VersionSimpleQuery query); //获取可用的最新版本
    };
};