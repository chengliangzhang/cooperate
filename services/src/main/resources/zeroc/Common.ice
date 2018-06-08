#pragma once
#include <data/CommonData.ice>

#include <CommonConst.ice>
#include <Company.ice>
#include <FileServer.ice>
#include <Notice.ice>
#include <Project.ice>
#include <Storage.ice>
#include <Task.ice>
#include <User.ice>

[["java:package:com.maoding.common"]]
module zeroc {
    interface CommonService {
        FileService* getDefaultFileService(); //获取本机配置的文件服务器
        UserService* getDefaultUserService(); //获取本机配置的用户服务器
        ConstService* getDefaultConstService(); //获取本机配置的常量服务器

        FileService* getFileService(string service,string config); //获取指定配置的文件服务器
        NoticeService* getNoticeService(string service,string config); //获取指定配置的通知服务器
        StorageService* getStorageService(string service,string config); //获取指定配置的存储服务器
        UserService* getUserService(string service,string config); //获取指定配置的用户服务器

        void updateService(); //升级本机服务
        string getNewestClient(); //获取与此文件服务器匹配的最新客户端版本
    };
};