#pragma once
#include <Common.ice>
#include <FileServer.ice>

[["java:package:com.maoding.WebService"]]
module zeroc {
     ["java:getset"]
     struct ProjectDTO{//我的项目
        string id;
        string companyBid; //乙方
        string companyId; //公司id
        string accountId; //账号id（当前用户账号id）
        string address; //地址
        string appOrgId; //组织id
        string attentionId; //关注项目ID
        string buildName; //
        string busPersonInCharge; //经营负责人名称
        string companyName; //公司名称
        string companyUserId; //创建人
        string createBy; //创建人
        string createDate; //创建时间
        string currentCompanyId; //当前公司
        string designCompanyName; //设计人（接包人）
        string designPersonInCharge; //设计负责人名称
        string fromCompanyName; //发包人
        string helpCompanyUserName; //
        string isMyProject; //是否是我的项目（0，存在，1，不存在）
        int outerCooperatorFlag; //是否存在外部合作（0，不存在，1，存在）
        int overtimeFlag; //任务超时标示（0：不超时，1：超时）
        string parentProjectid; //项目拆分后的父Id
        string partyA; //甲方（建设单位）
        string partyB; //乙方
        string projectCreateDate; // 项目创建时间
        string projectName; //项目名称
        string projectNo; //项目编号
        string projectType; //项目类型
        string signDate; //合同签订日期
        string status; //项目状态
        string token; //app使用的token标示
        string totalContractAmount; //合同总金额
        string updateDate; //更新时间
     };
     ["java:getset"]
     struct TaskDTO{
         string completeDate; //完成时间
         string handlerId; //任务负责人雇员ID
         string id;
         string param1; //
         string planEndTime; //结束时间
         string planStartTime; //开始时间
         string projectId; //项目id
         string projectName; //项目名称
         string stateHtml; //状态字符串
         string stateMap;
         string statusText; //状态文字
         string targetId; //任务id（设计阶段的id）
         string taskContent; //任务内容
         string taskName; //任务名称
         int taskState; //任务状态状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
         int taskType; //任务类型任务类型（经营方：2，生产方：0（默认不传递））
         string taskTypeText; //类型
         string type; //
     };
     ["java:getset"]
     struct AccountDTO{ //用户注册DTO
        string id; //唯一编号
        string userName;//姓名
        string nickName;//昵称
        string password;//密码
        string cellphone;//电话
        string code;//验证码
        string email;//邮箱
        string defaultCompanyId;//默认企业id
        string birthday;//出生日期
        string sex;//性别
        string status;//账号状态(1:未激活，0：激活）
        string major;//专业
     };
      ["java:getset"]
     struct QueryProjectTaskDTO{//任务查询条件DTO
        int pageIndex;
        int pageSize;
        string companyId;//当前公司id
        string projectId;//当前项目
        string currentCompanyUserId;
        int isCreator;//是否是立项方,是否是立项方（1：是，0：否）
     };
    ["java:type:java.util.ArrayList<ProjectDTO>"] sequence<ProjectDTO> ProjectDTOList;
    interface WebService {
        AccountDTO getLogin(AccountDTO dto);//登录
        ProjectDTOList getProjects(Map params);//我的项目
        ProjectDTO loadProjectDetails(string projectId);//基本信息
        ProjectDTO getIssueInfo(string projectId);//任务签发
        ProjectDTO getDesignTaskList(QueryProjectTaskDTO query);//生产安排
        TaskDTO getMyTaskList4(Map params);//我的任务
    };
}