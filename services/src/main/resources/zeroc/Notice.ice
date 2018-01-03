#pragma once
#include <Common.ice>

[["java:package:com.maoding.Notice"]]
module zeroc {
    ["java:getset","clr:property"]
    struct MessageDTO { //通告消息
        string userId; //消息发布者id
        string title; //消息标题
        string content; //消息内容
    };
    ["java:type:java.util.ArrayList<MessageDTO>"] sequence<MessageDTO> MessageList;

    struct ReceiverDTO { //接受者
        string topic; //发布频道
        string projectId; //项目id
        string companyId; //组织id
        string userId; //用户id
    };
    ["java:type:java.util.ArrayList<ReceiverDTO>"] sequence<ReceiverDTO> ReceiverList;

    interface NoticeService {
        void createTopic(String topic); //建立频道
        void subscribeTopic(String topic); //订阅频道
        void sendMessage(MessageDTO message,ReceiverList receiverList); //发送消息
    };
};