#pragma once
#include <Common.ice>
#include <User.ice>

[["java:package:com.maoding.Project"]]
module zeroc {
    ["java:getset"]
    struct ConstructionCateDTO {
        string id;
        string name;
        string pid;
    };
    ["java:type:java.util.ArrayList<ConstructionCateDTO>"] sequence<ConstructionCateDTO> ConstructionList;

    struct CompanyDTO {
        string companyName;
        string id;
    };

    struct ManagerOfPartBDTO {
        string accountId;
        string cellphone;
        string companyId;
        string companyName;
        string companyUserId;
        string companyUserName;
        string id;
        short memberType;
        string projectId;
    };

    struct LocationDTO{
        string city;
        string county;
        string detailAddress;

    };

    struct ProjectPropertyDTO{
        string fieldName;
        string fieldValue;
        string id;
        string unit;
    };
    ["java:type:java.util.ArrayList<ProjectPropertyDTO>"] sequence<ProjectPropertyDTO> PropertyList;

    struct ProjectDTO {
        string id; //唯一标识
        string projectNo;
        string name; //项目名称
        short projectType;
        string projectTypeName;
        short projectStatus;
        string projectStatusText;
        LocationDTO projectLocation;
        PropertyList projectPropertyList;
        string companyBidName;
        CompanyDTO creatorCompany;
        CompanyDTO partyACompany;
        CompanyDTO partyBCompany;
        ManagerOfPartBDTO managerOfPartB;
        string filePath;
        string fileName;
        ConstructionList constructionList;
        long contractDateStamp;
        string contractDateText;
    };
    ["java:type:java.util.ArrayList<ProjectDTO>"] sequence<ProjectDTO> ProjectList;

    interface ProjectService {
        ProjectDTO getProjectInfoById(string id);

        ProjectList listProjectByAccount(AccountDTO account); //获取指定账号所参与项目
        ProjectList listProjectForCurrent(); //获取当前账号所参与项目
    };
};