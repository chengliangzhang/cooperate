-- 建立更新表结构存储过程
DROP PROCEDURE IF EXISTS `updatePermissionTables`;
CREATE PROCEDURE `updatePermissionTables`()
BEGIN
  -- maoding_web_role -- 权限组表
  CREATE TABLE IF NOT EXISTS `maoding_web_role` (
    `id` varchar(32) NOT NULL COMMENT 'ID',
    `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID（此字段为空则表示当前角色是公用角色）',
    `code` varchar(32) DEFAULT NULL COMMENT '角色编码',
    `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
    `status` char(1) DEFAULT NULL COMMENT '0=生效，1＝不生效',
    `order_index` int(11) DEFAULT NULL COMMENT '角色排序',
    `create_date` datetime DEFAULT NULL,
    `create_by` varchar(50) DEFAULT NULL,
    `update_date` datetime DEFAULT NULL,
    `update_by` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `company_id` (`company_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前台角色表';

  -- maoding_web_role_permission -- 权限树表
  CREATE TABLE IF NOT EXISTS `maoding_web_role_permission` (
    `id` varchar(32) NOT NULL COMMENT 'ID',
    `role_id` varchar(32) DEFAULT NULL COMMENT '角色ID',
    `permission_id` varchar(32) DEFAULT NULL COMMENT '权限ID',
    `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `role_id` (`role_id`),
    KEY `permission_id` (`permission_id`),
    KEY `company_id` (`company_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色视图表';

  -- maoding_web_permission -- 权限表
  CREATE TABLE IF NOT EXISTS `maoding_web_permission` (
    `id` varchar(32) NOT NULL COMMENT '视图ID',
    `code` varchar(96) DEFAULT NULL COMMENT 'code值',
    `name` varchar(32) DEFAULT NULL COMMENT '权限名称',
    `pid` varchar(32) DEFAULT NULL COMMENT '父权限ID',
    `root_id` varchar(32) DEFAULT NULL COMMENT '根权限ID',
    `seq` int(11) DEFAULT NULL COMMENT '排序',
    `status` char(1) DEFAULT NULL COMMENT '0=生效，1＝不生效',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `description` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `pid` (`pid`),
    KEY `root_id` (`root_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

  -- maoding_web_user_permission -- 组织角色表
  CREATE TABLE IF NOT EXISTS `maoding_web_user_permission` (
    `id` varchar(32) NOT NULL COMMENT 'ID',
    `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID',
    `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
    `permission_id` varchar(32) DEFAULT NULL COMMENT '权限ID',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `seq` int(4) DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `company_id` (`company_id`),
    KEY `user_id` (`user_id`),
    KEY `permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前台角色权限表';
END;

-- 建立初始化权限数据过程
DROP PROCEDURE IF EXISTS `initPermission`;
CREATE PROCEDURE `initPermission`()
BEGIN
    -- 企业负责人
    REPLACE INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5fe9d1', null, 'OrgManager', '企业负责人', '0', '1', null, null, null, null);
    REPLACE INTO `maoding_web_permission` VALUES ('11', 'sys_enterprise_logout', '组织解散', '1', '1', '1000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('102', 'org_permission,sys_role_permission', '权限分配', '1', '1', '2000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('54', 'project_delete', '删除项目', '5', '5', '3000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('100', 'org_partner', '事业合伙人/分公司(创建/邀请)', '1', '1', '4000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('101', 'org_auth,sys_role_auth', '企业认证', '1', '1', '5000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('103', 'org_data_import,data_import', '历史数据导入', '5', '5', '6000', '0', null, null, null, null, '描述');

    delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5fe9d1';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',id,null,now() from maoding_web_permission
      where id in (11,102,54,100,101,103);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',11,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',102,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',54,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',100,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',101,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',103,id,now() from maoding_web_company;

    -- 系统管理员
    REPLACE INTO `maoding_web_role` VALUES ('2f84f20610314637a8d5113440c69bde', null, 'SystemManager', '系统管理员', '0', '2', '2015-12-01 16:14:03', null, null, null);
    REPLACE INTO `maoding_web_permission` VALUES ('8', 'sys_role_permission', '权限分配', '1', '1', '2000', '0', null, null, null, null, '企业中，分配每个人所拥有的权限');
    REPLACE INTO `maoding_web_permission` VALUES ('58', 'sys_role_auth', '企业认证', '1', '1', '5000', '0', null, null, null, null, '企业认证的权限');
    REPLACE INTO `maoding_web_permission` VALUES ('55', 'data_import', '历史数据导入', '5', '5', '6000', '0', null, null, null, null, '企业中历史数据打入权限');

    delete from maoding_web_role_permission where role_id = '2f84f20610314637a8d5113440c69bde';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',id,null,now() from maoding_web_permission
      where id in (8,58,55);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',8,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',58,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',55,id,now() from maoding_web_company;

    -- 组织管理
    REPLACE INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5fe9d0', null, 'GeneralManager', '组织管理', '0', '3', '2015-12-01 16:14:04', null, '2015-12-18 13:29:45', null);
    REPLACE INTO `maoding_web_permission` VALUES ('12', 'com_enterprise_edit', '组织信息管理', '2', '2', '2200', '0', null, null, null, null, '企业信息编辑 修改 如企业名称 企业简介等');
    REPLACE INTO `maoding_web_permission` VALUES ('14', 'hr_org_set,hr_employee', '组织架构设置', '2', '2', '2300', '0', null, null, null, null, '企业中相关人员，人员的添加 修改 删除');

    delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5fe9d0';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',id,null,now() from maoding_web_permission
      where id in (12,14);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',12,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',14,id,now() from maoding_web_company;

    -- 行政管理
    REPLACE INTO `maoding_web_role` VALUES ('0726c6aba7fa40918bb6e795bbe51059', null, 'AdminManager', '行政管理', '0', '4', '2015-12-10 15:59:24', null, '2015-12-18 13:29:55', null);
    REPLACE INTO `maoding_web_permission` VALUES ('110', 'summary_leave', '请假/出差汇总', '2', '2', '2500', '0', null, null, null, null, '企业中相关人员，请假/出差审批完成后的汇总记录');
    REPLACE INTO `maoding_web_permission` VALUES ('19', 'admin_notice', '通知公告发布', '4', '4', '2600', '0', null, null, null, null, '企业中相关公告信息 通知等');

    delete from maoding_web_role_permission where role_id = '0726c6aba7fa40918bb6e795bbe51059';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'0726c6aba7fa40918bb6e795bbe51059',id,null,now() from maoding_web_permission
      where id in (110,19);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0726c6aba7fa40918bb6e795bbe51059',110,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0726c6aba7fa40918bb6e795bbe51059',19,id,now() from maoding_web_company;


    -- 项目管理
    REPLACE INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5f1112', null, 'OperateManager', '项目管理', '0', '5', '2016-07-25 19:18:26', null, '2016-07-25 19:18:31', null);
    REPLACE INTO `maoding_web_permission` VALUES ('51', 'project_manager', '任务签发', '5', '5', '2650', '0', null, null, null, null, '企业中从事经营活动的相关人员进行任务的经营签发活动<br>注:系统默认新项目的经营负责人为排在任务签发第一位的人员');
    REPLACE INTO `maoding_web_permission` VALUES ('52', 'design_manager', '生产安排', '5', '5', '2660', '0', null, null, null, null, '企业中的设计负责人可对经营负责人发布过来的任务进行具体安排<br>注:系统默认新项目的设计负责人为排在生产安排第一位的人员');
    REPLACE INTO `maoding_web_permission` VALUES ('56', 'project_overview', '项目总览', '5', '5', '2670', '0', null, null, null, null, '企业中所有项目信息的查看权限');
    REPLACE INTO `maoding_web_permission` VALUES ('57', 'project_archive', '查看项目文档', '5', '5', '2680', '0', null, null, null, null, '企业中所有项目文档（设计依据/归档文件/交付文件)的查看下载');
    REPLACE INTO `maoding_web_permission` VALUES ('20', 'project_eidt,project_edit', '项目基本信息编辑', '5', '5', '2700', '0', null, null, null, null, '企业中所有项目中基本信息的编辑录入');

    delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5f1112';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',id,null,now() from maoding_web_permission
      where id in (51,52,56,57,20);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',51,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',52,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',56,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',57,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',20,id,now() from maoding_web_company;

    -- 财务管理
    REPLACE INTO `maoding_web_role` VALUES ('0fb8c188097a4d01a0ff6bb9cacb308e', null, 'FinancialManager', '财务管理', '0', '6', '2015-12-01 16:15:46', null, '2015-12-18 13:29:43', null);
    REPLACE INTO `maoding_web_permission` VALUES ('46', 'report_exp_static', '查看/费用汇总', '2', '2', '5800', '0', null, null, null, null, '查看企业所有人员的报销/费用汇总情况');
    REPLACE INTO `maoding_web_permission` VALUES ('10', 'sys_finance_type', '财务费用类别设置', '6', '6', '5810', '0', null, null, null, null, '设置企业报销/费用可申请或报销的类型进行设置');
    REPLACE INTO `maoding_web_permission` VALUES ('49', 'project_charge_manage', '项目收支管理', '6', '6', '5820', '0', null, null, null, null, '项目费用（合同回款/技术审查费/合作设计费/其他收支)的到账/付款确认，报销/费用的拨款处理');

    delete from maoding_web_role_permission where role_id = '0fb8c188097a4d01a0ff6bb9cacb308e';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',id,null,now() from maoding_web_permission
      where id in (46,10,49);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',46,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',10,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',49,id,now() from maoding_web_company;

    -- 添加默认权限
    -- 企业负责人
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,102,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 102 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,54,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 54 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,100,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 100 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,101,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 101 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,103,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 103 and company.id = role_have.company_id)
				where role_have.permission_id is null;

		-- 系统管理员
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,8,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 8 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,58,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 8)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 58 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,55,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 8)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 55 and company.id = role_have.company_id)
				where role_have.permission_id is null;

		-- 项目管理
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,56,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 56 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,57,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 57 and company.id = role_have.company_id)
				where role_have.permission_id is null;

    -- 行政管理
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
      select replace(uuid(),'-',''),company.id,role.user_id,110,now(),1
      from maoding_web_company company
        inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
        left join maoding_web_user_permission role_have on (role_have.permission_id = 110 and company.id = role_have.company_id)
      where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
      select replace(uuid(),'-',''),company.id,role.user_id,19,now(),1
      from maoding_web_company company
        inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
        left join maoding_web_user_permission role_have on (role_have.permission_id = 19 and company.id = role_have.company_id)
      where role_have.permission_id is null;
END;

call initPermission();