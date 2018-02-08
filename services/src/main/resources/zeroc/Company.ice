#pragma once
#include <Common.ice>
#include <CompanyData.ice>

[["java:package:com.maoding.Company"]]
module zeroc {
    interface CompanyService {
        CompanyList listCompany(QueryCompanyDTO query); //获取指定用户所属组织
        CompanyList listCompanyByUserId(string userId); //获取指定用户所属组织
        ["deprecate"] CompanyList listCompanyForCurrent(); //获取当前账号所属组织
    };
};