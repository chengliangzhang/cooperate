#pragma once
#include <data/CommonData.ice>

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
        FileService* getFileService(string service,string config);
        NoticeService* getNoticeService(string service,string config);
        StorageService* getStorageService(string service,string config);
        UserService* getUserService(string service,string config);
    };
};