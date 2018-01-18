#pragma once
#include <Common.ice>
#include <NoticeData.ice>
#include <User.ice>

[["java:package:com.maoding.Notice"]]
module zeroc {
    interface NoticeClient {
        void notice(MessageDTO msg); //接收到通知
    };

    interface NoticeService {
        void createTopic(String topic); //建立主题
        StringList listTopic(); //列出所有已建立的主题
        StringList listSubscribedTopic(string userId); //列出用户已订阅的主题
        void notice(MessageDTO message,string topic); //向指定主题发送消息
        void noticeToUser(MessageDTO message,string id); //向指定用户发送消息
        void noticeToTask(MessageDTO message,string id); //向指定任务的用户发送消息
        void noticeToProject(MessageDTO message,string id); //向指定项目的用户发送消息
        void noticeToCompany(MessageDTO message,string id); //向指定组织的用户发送消息
        void subscribeTopic(string topic, NoticeClient* client); //订阅频道
        void subscribeTopicToUser(string id, NoticeClient* client); //订阅频道
        void subscribeTopicToTask(string id, NoticeClient* client); //订阅频道
        void subscribeTopicToProject(String id, NoticeClient* client); //订阅频道
        void subscribeTopicToCompany(string id, NoticeClient* client); //订阅频道
        void subscribeTopicToTaskList(StringList idList, NoticeClient* client); //订阅频道
        void subscribeTopicToProjectList(StringList idList, NoticeClient* client); //订阅频道
        void subscribeTopicToCompanyList(StringList idList, NoticeClient* client); //订阅频道
        void unSubscribeTopic(String topic, NoticeClient* client); //取消订阅频道
        void unSubscribeTopicToUser(String id, NoticeClient* client); //取消订阅频道
        void unSubscribeTopicToTask(String id, NoticeClient* client); //取消订阅频道
        void unSubscribeTopicToProject(string id, NoticeClient* client); //取消订阅频道
        void unSubscribeTopicToCompany(string id, NoticeClient* client); //取消订阅频道
        void unSubscribeTopicToTaskList(StringList idList, NoticeClient* client); //取消订阅频道
        void unSubscribeTopicToProjectList(StringList idList, NoticeClient* client); //取消订阅频道
        void unSubscribeTopicToCompanyList(StringList idList, NoticeClient* client); //取消订阅频道
    };
};