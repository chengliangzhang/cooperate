-- 建立更新表结构存储过程
DROP PROCEDURE IF EXISTS `updateTables`;
CREATE PROCEDURE `updateTables`()
BEGIN
	-- maoding_const -- 常量定义
	CREATE TABLE IF NOT EXISTS `maoding_const` (
		`classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
		`value_id` smallint(4) unsigned NOT NULL COMMENT '特定分类内的常量编号',
		`content` varchar(255) NOT NULL COMMENT '常量的基本定义',
		`content_extra` varchar(255) DEFAULT NULL COMMENT '常量的扩展定义',
		PRIMARY KEY (`classic_id`,`value_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	-- maoding_storage -- 协同节点定义
	CREATE TABLE IF NOT EXISTS `maoding_storage` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_duty_id` char(32) DEFAULT NULL COMMENT '记录最后修改者职责id',
		`pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
		`path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
		`type_id` smallint(4) unsigned DEFAULT '0' COMMENT '节点类型',
		`node_name` varchar(255) DEFAULT NULL COMMENT '树节点名',
		`file_length` bigint(16) unsigned DEFAULT '0' COMMENT '文件长度，如果节点是目录则固定为0',
		`task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage' and column_name='task_id') then
		alter table maoding_storage add column `task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id';
	end if;


	-- maoding_storage_file -- 协同文件定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_file` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_duty_id` char(32) DEFAULT NULL COMMENT '记录最后修改者职责id',
		`server_type_id` smallint(4) unsigned DEFAULT '1' COMMENT '文件服务器类型',
		`server_address` varchar(255) DEFAULT NULL COMMENT '文件服务器地址',
		`file_type_id` smallint(4) unsigned DEFAULT '0' COMMENT '文件类型',
		`file_version` varchar(20) DEFAULT NULL COMMENT '文件版本',
		`file_checksum` varchar(64) DEFAULT NULL COMMENT '文件校验和',
		`major_id` char(32) DEFAULT NULL COMMENT '文件所属专业id',
		`main_file_id` char(32) DEFAULT NULL COMMENT '所对应的原始文件id',
		`read_file_scope` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储位置',
		`read_file_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称',
		`write_file_scope` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储位置',
		`write_file_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='write_file_scope') then
		alter table maoding_storage_file add column `write_file_scope` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储位置';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='write_file_key') then
		alter table maoding_storage_file add column `write_file_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称';
	end if;


  -- maoding_storage_file_his -- 协同文件校审提资历史记录定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_file_his` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_duty_id` char(32) DEFAULT NULL COMMENT '记录最后修改者职责id',
    `file_id` char(32) DEFAULT NULL COMMENT '协同文件编号id',
    `action_type_id` smallint(4) unsigned DEFAULT '0' COMMENT '校审动作类型',
    `remark` text(2048) DEFAULT NULL COMMENT '文件注释',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file_his' and column_name='remark') then
		alter table maoding_storage_file_his add column `remark` text(2048) DEFAULT NULL COMMENT '文件注释';
	end if;

	-- maoding_web_account -- 用户登录表
  CREATE TABLE IF NOT EXISTS  `maoding_web_account` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `user_name` varchar(30) DEFAULT NULL COMMENT '用户名（冗余）',
    `nick_name` varchar(30) DEFAULT NULL COMMENT '昵称',
    `password` varchar(32) DEFAULT NULL COMMENT '密码',
    `cellphone` varchar(11) NOT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL,
    `default_company_id` varchar(32) DEFAULT NULL COMMENT '默认企业id',
    `signature` varchar(100) DEFAULT NULL COMMENT '个性签名',
    `status` varchar(1) DEFAULT '0' COMMENT '账号状态(1:未激活，0：激活）',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `emial_code` varchar(100) DEFAULT NULL COMMENT '邮箱绑定(验证）格式：邮箱-验证码（email-code）',
    `active_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `cellphone` (`cellphone`),
    KEY `default_company_id` (`default_company_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录用户表';

  -- maoding_web_project -- 项目表
  CREATE TABLE IF NOT EXISTS  `maoding_web_project` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `company_id` varchar(32) DEFAULT NULL COMMENT '企业id',
    `company_bid` varchar(32) DEFAULT NULL COMMENT '挂靠的大B id',
    `project_type` varchar(32) DEFAULT NULL COMMENT '项目类别(冗余，目前没用到)',
    `built_type` varchar(1000) DEFAULT NULL COMMENT '建筑功能',
    `project_no` varchar(50) DEFAULT NULL COMMENT '项目编号',
    `project_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
    `base_area` varchar(50) DEFAULT NULL,
    `capacity_area` varchar(50) DEFAULT NULL,
    `total_construction_area` varchar(50) DEFAULT NULL,
    `increasing_area` varchar(50) DEFAULT NULL,
    `coverage` varchar(50) DEFAULT NULL,
    `greening_rate` varchar(50) DEFAULT NULL,
    `built_height` varchar(20) DEFAULT NULL COMMENT '建筑高度',
    `built_floor_up` varchar(10) DEFAULT NULL COMMENT '建筑层数(地上)',
    `built_floor_down` varchar(10) DEFAULT NULL COMMENT '建筑层数(地下)',
    `construct_company` varchar(32) DEFAULT NULL COMMENT '建设单位',
    `investment_estimation` decimal(20,6) DEFAULT NULL COMMENT '投资估算',
    `total_contract_amount` decimal(20,6) DEFAULT NULL COMMENT '合同总金额',
    `status` varchar(1) DEFAULT '0' COMMENT '默认为0＝进行中，1＝已暂停，2＝已完成 ，3 = 已终止',
    `pstatus` varchar(1) DEFAULT '0' COMMENT '0=生效，1=不生效',
    `project_create_date` date DEFAULT NULL COMMENT '项目创建日期',
    `is_history` int(1) DEFAULT '0' COMMENT '是否是历史导入的数据',
    `design_range` varchar(1000) DEFAULT NULL COMMENT '设计范围',
    `contract_date` date DEFAULT NULL,
    `parent_projectid` varchar(32) DEFAULT '' COMMENT '项目拆分后的父Id',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `province` varchar(50) DEFAULT NULL,
    `city` varchar(50) DEFAULT NULL,
    `county` varchar(50) DEFAULT NULL COMMENT '县或镇或区',
    `volume_ratio` varchar(50) DEFAULT NULL COMMENT '容积率',
    `detail_address` varchar(255) DEFAULT NULL,
    `helper_company_user_id` varchar(32) DEFAULT NULL COMMENT '帮助立项的人的id（company_user_id）',
    PRIMARY KEY (`id`),
    KEY `manager_id` (`helper_company_user_id`),
    KEY `process_id` (`contract_date`),
    KEY `company_id` (`company_id`),
    KEY `helper_company_user_id` (`helper_company_user_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目信息表';

    -- maoding_web_company -- 组织表
  CREATE TABLE IF NOT EXISTS  `maoding_web_company` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `company_name` varchar(100) NOT NULL COMMENT '企业名称',
    `major_type` varchar(1000) DEFAULT NULL COMMENT '专业类别',
    `certificate` varchar(1000) DEFAULT NULL COMMENT '技术资质',
    `main_field` varchar(1000) DEFAULT NULL COMMENT '擅长领域',
    `is_authentication` varchar(2) DEFAULT '0' COMMENT '是否认证(0.否，1.是，2申请认证)',
    `operator_name` varchar(32) DEFAULT NULL COMMENT '经办人',
    `reject_reason` varchar(1000) DEFAULT NULL COMMENT '认证不通过原因',
    `company_type` varchar(2) DEFAULT NULL COMMENT '公司类型(0=小B，1＝超级大B,2=大B分公司)',
    `company_email` varchar(50) DEFAULT NULL COMMENT '企业邮箱',
    `company_short_name` varchar(100) DEFAULT NULL,
    `company_fax` varchar(20) DEFAULT NULL COMMENT '企业传真',
    `server_type` varchar(200) DEFAULT NULL COMMENT '服务类型',
    `province` varchar(30) DEFAULT NULL COMMENT '企业所属省',
    `city` varchar(30) DEFAULT NULL COMMENT '企业所属市',
    `county` varchar(30) DEFAULT NULL,
    `company_comment` longtext COMMENT '企业简介',
    `legal_representative` varchar(30) DEFAULT NULL COMMENT '法人代表',
    `company_phone` varchar(30) DEFAULT NULL COMMENT '联系电话',
    `company_address` varchar(255) DEFAULT NULL COMMENT '企业地址',
    `status` varchar(1) DEFAULT '0' COMMENT '企业状态（生效0，1不生效）',
    `group_index` int(11) DEFAULT NULL COMMENT '团队排序',
    `index_show` varchar(1) DEFAULT NULL COMMENT '是否首页展示(0展示，1不展示)',
    `business_license_number` varchar(50) DEFAULT NULL COMMENT '工商营业执照号码',
    `organization_code_number` varchar(50) DEFAULT NULL COMMENT '组织机构代码证号码',
    `micro_url` varchar(100) DEFAULT '#micro/microNetworkone' COMMENT '微官网地址',
    `micro_template` varchar(1) DEFAULT '1' COMMENT '微官网模板',
    `group_id` varchar(32) DEFAULT NULL COMMENT '企业群ID',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `group_id` (`group_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织表';

  -- maoding_web_project_task -- 任务表
  CREATE TABLE IF NOT EXISTS  `maoding_web_project_task` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `from_company_id` varchar(32) DEFAULT NULL,
    `company_id` varchar(32) DEFAULT NULL COMMENT '组织id',
    `project_id` varchar(32) NOT NULL COMMENT '项目id',
    `org_id` varchar(32) DEFAULT NULL COMMENT '部门id',
    `task_name` varchar(200) NOT NULL COMMENT '任务名称',
    `task_pid` varchar(32) DEFAULT NULL COMMENT '父id',
    `task_path` text COMMENT '任务完整路径id-id',
    `task_type` int(1) DEFAULT NULL COMMENT '类型（签发设计阶段或服务内容\r\n1=设计阶段 \r\n2=签发\r\n0=生产\r\n3=签发（未发布）\r\n4=生产（未发布），5:=设计任务',
    `task_level` int(11) DEFAULT NULL COMMENT '签发次数级别',
    `task_status` varchar(1) DEFAULT '0' COMMENT '0生效，1删除,2:未发布，3：未发布（修改）',
    `task_remark` varchar(1000) DEFAULT NULL COMMENT '备注',
    `seq` int(4) DEFAULT NULL COMMENT '排序',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `is_operater_task` int(1) DEFAULT NULL COMMENT '是否是经营任务（1：经营任务，0：是经营任务，但是可以进行生产，或许直接是生产任务）',
    `end_status` int(1) DEFAULT '0' COMMENT '结束状态：0=未开始，1=已完成，2=已终止',
    `complete_date` date DEFAULT NULL COMMENT '完成时间',
    `be_modify_id` varchar(32) DEFAULT NULL COMMENT '被修改记录的id，用于修改任务，新增一条未被发布的数据，该字段记录被修改记录的id',
    `start_time` date DEFAULT NULL,
    `end_time` date DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `from_company_id` (`from_company_id`),
    KEY `company_id` (`company_id`),
    KEY `project_id` (`project_id`),
    KEY `org_id` (`org_id`),
    KEY `task_pid` (`task_pid`),
    KEY `be_modify_id` (`be_modify_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表';

  -- maoding_web_project_member -- 成员表
  CREATE TABLE IF NOT EXISTS  `maoding_web_project_member` (
    `id` varchar(32) NOT NULL,
    `project_id` varchar(32) DEFAULT NULL COMMENT '项目id',
    `company_id` varchar(32) DEFAULT NULL COMMENT 'company_user在的公司id',
    `account_id` varchar(32) DEFAULT NULL COMMENT 'account表中的id',
    `company_user_id` varchar(32) DEFAULT NULL COMMENT 'company_user表中的id',
    `member_type` int(2) DEFAULT NULL COMMENT '0：立项人，1：经营负责人，2：设计负责人,3,任务负责人,4.设计，5，校对，6，审核',
    `target_id` varchar(32) DEFAULT NULL COMMENT '目标id，所属节点id',
    `node_id` varchar(32) DEFAULT NULL COMMENT '目标2id，冗余字段，便于查询',
    `status` int(2) DEFAULT '0',
    `deleted` int(1) DEFAULT '0' COMMENT '删除标示（0：未删除，1，删除）',
    `seq` int(1) DEFAULT '0' COMMENT '排序字段（社校审人员才用到）',
    `param1` varchar(255) DEFAULT NULL,
    `param2` varchar(255) DEFAULT NULL,
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id` (`id`) USING BTREE,
    KEY `company_id` (`company_id`) USING BTREE,
    KEY `company_user_id` (`company_user_id`) USING BTREE,
    KEY `target_id` (`target_id`) USING BTREE,
    KEY `member_type` (`member_type`) USING BTREE,
    KEY `project_id` (`project_id`) USING BTREE
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目成员表';

  -- maoding_web_company -- 组织信息表
  CREATE TABLE IF NOT EXISTS `maoding_web_company` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `company_name` varchar(100) NOT NULL COMMENT '企业名称',
    `major_type` varchar(1000) DEFAULT NULL COMMENT '专业类别',
    `certificate` varchar(1000) DEFAULT NULL COMMENT '技术资质',
    `main_field` varchar(1000) DEFAULT NULL COMMENT '擅长领域',
    `is_authentication` varchar(2) DEFAULT '0' COMMENT '是否认证(0.否，1.是，2申请认证)',
    `operator_name` varchar(32) DEFAULT NULL COMMENT '经办人',
    `reject_reason` varchar(1000) DEFAULT NULL COMMENT '认证不通过原因',
    `company_type` varchar(2) DEFAULT NULL COMMENT '公司类型(0=小B，1＝超级大B,2=大B分公司)',
    `company_email` varchar(50) DEFAULT NULL COMMENT '企业邮箱',
    `company_short_name` varchar(100) DEFAULT NULL,
    `company_fax` varchar(20) DEFAULT NULL COMMENT '企业传真',
    `server_type` varchar(200) DEFAULT NULL COMMENT '服务类型',
    `province` varchar(30) DEFAULT NULL COMMENT '企业所属省',
    `city` varchar(30) DEFAULT NULL COMMENT '企业所属市',
    `county` varchar(30) DEFAULT NULL,
    `company_comment` longtext COMMENT '企业简介',
    `legal_representative` varchar(30) DEFAULT NULL COMMENT '法人代表',
    `company_phone` varchar(30) DEFAULT NULL COMMENT '联系电话',
    `company_address` varchar(255) DEFAULT NULL COMMENT '企业地址',
    `status` varchar(1) DEFAULT '0' COMMENT '企业状态（生效0，1不生效）',
    `group_index` int(11) DEFAULT NULL COMMENT '团队排序',
    `index_show` varchar(1) DEFAULT NULL COMMENT '是否首页展示(0展示，1不展示)',
    `business_license_number` varchar(50) DEFAULT NULL COMMENT '工商营业执照号码',
    `organization_code_number` varchar(50) DEFAULT NULL COMMENT '组织机构代码证号码',
    `micro_url` varchar(100) DEFAULT '#micro/microNetworkone' COMMENT '微官网地址',
    `micro_template` varchar(1) DEFAULT '1' COMMENT '微官网模板',
    `group_id` varchar(32) DEFAULT NULL COMMENT '企业群ID',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `group_id` (`group_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织表';

  -- maoding_web_permission -- 权限定义
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

  -- maoding_web_user_permission -- 组织角色定义
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

-- 建立创建视图的存储过程
DROP PROCEDURE IF EXISTS updateViews;
CREATE PROCEDURE updateViews()
BEGIN
  -- maoding_task -- 清理某些字段的文件表
  CREATE OR REPLACE VIEW `maoding_task` AS
      select
          task.id
          ,task.task_name
          ,task.task_type as type_id
          ,task.task_pid as pid
          ,concat(if(task_parent4.task_name is null,'',concat(task_parent4.task_name,'/'))
              ,if(task_parent3.task_name is null,'',concat(task_parent3.task_name,'/'))
              ,if(task_parent2.task_name is null,'',concat(task_parent2.task_name,'/'))
              ,if(task_parent1.task_name is null,'',concat(task_parent1.task_name,'/'))
              ,task.task_name) as path
          ,if(task.task_type in (1,2),task.id
              ,if(task_parent1.task_type in (1,2),task_parent1.id
              ,if(task_parent2.task_type in (1,2),task_parent2.id
              ,if(task_parent3.task_type in (1,2),task_parent3.id
              ,if(task_parent4.task_type in (1,2), task_parent4.id, null))))) as issue_id
          ,if((task.task_type in (1,2)),task.task_name
              ,if((task_parent1.task_type in (1,2)),task_parent1.task_name
              ,if((task_parent2.task_type in (1,2)),task_parent2.task_name
              ,if((task_parent3.task_type in (1,2)),task_parent3.task_name
              ,if((task_parent4.task_type in (1,2)),task_parent4.task_name,null))))) as issue_name
          ,concat(if((task_parent4.task_name is null) or (task_parent4.task_type not in (1,2)),'',concat(task_parent4.task_name))
              ,if((task_parent3.task_name is null) or (task_parent3.task_type not in (1,2)),'',concat(if(task_parent4.task_name is null,'','/'),task_parent3.task_name))
              ,if((task_parent2.task_name is null) or (task_parent2.task_type not in (1,2)),'',concat(if(task_parent3.task_name is null,'','/'),task_parent2.task_name))
              ,if((task_parent1.task_name is null) or (task_parent1.task_type not in (1,2)),'',concat(if(task_parent2.task_name is null,'','/'),task_parent1.task_name))
              ,if((task.task_name is null) or (task.task_type not in (1,2)),'',concat(if(task_parent1.task_name is null,'','/'),task.task_name))) as issue_path
          ,concat(if((task_parent4.task_name is null) or (task_parent4.task_type not in (0)),'',concat(task_parent4.task_name,'/'))
              ,if((task_parent3.task_name is null) or (task_parent3.task_type not in (0)),'',concat(task_parent3.task_name,'/'))
              ,if((task_parent2.task_name is null) or (task_parent2.task_type not in (0)),'',concat(task_parent2.task_name,'/'))
              ,if((task_parent1.task_name is null) or (task_parent1.task_type not in (0)),'',concat(task_parent1.task_name,'/'))
              ,if((task.task_name is null) or (task.task_type not in (0)),'',concat(task.task_name))) as task_path
          ,task.project_id
          ,task.company_id
          ,task.create_date
          ,task.create_by
          ,task.update_date
          ,task.update_by
          ,company.company_name
          ,project.project_name
      from
          maoding_web_project_task task
          inner join maoding_web_project project on (task.project_id = project.id)
          left join maoding_web_company company on (task.company_id = company.id)
          left join maoding_web_project_task task_parent1 ON (task_parent1.id = task.task_pid)
          left join maoding_web_project_task task_parent2 ON (task_parent2.id = task_parent1.task_pid)
          left join maoding_web_project_task task_parent3 ON (task_parent3.id = task_parent2.task_pid)
          left join maoding_web_project_task task_parent4 ON (task_parent4.id = task_parent3.task_pid)
      where
          (task.task_status = '0')
          and (task.task_type in (0,1,2))
      group by task.id;

  -- maoding_storage_root -- 根节点通用视图，带有user_id_list
  CREATE OR REPLACE VIEW `maoding_storage_root` AS
      select
          project.id
          ,null AS pid
          ,if(project.id is null,null,11) as type_id
          ,project.project_name as name
          ,concat('/',project.project_name) as path
          ,unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp
          ,date_format(project.create_date,'%Y-%m-%d %T') as create_time_text
          ,unix_timestamp(if(project.update_date is null,ifnull(project.create_date,0),project.update_date)) as last_modify_time_stamp
          ,date_format(ifnull(project.update_date,project.create_date),'%Y-%m-%d %T') as last_modify_time_text
          ,ifnull(project.update_by,project.create_by) as owner_user_id
					,null as owner_role_id
          ,if(project.id is null,null,0) as file_length
					,null as storage_path
					,null as classic_id
					,null as classic_name
          ,node_type.content as type_name
          ,substring(node_type.content_extra,1,1) as is_directory
          ,substring(node_type.content_extra,2,1) as is_project
          ,substring(node_type.content_extra,3,1) as is_task
					,substring(node_type.content_extra,4,1) as is_design
					,substring(node_type.content_extra,5,1) as is_commit
					,substring(node_type.content_extra,6,1) as is_history
          ,null as issue_id
          ,null as issue_name
          ,null as task_id
          ,null as task_name
          ,null as task_path
          ,project.id as project_id
          ,project.project_name
          ,project.company_id as company_id
          ,company.company_name as company_name
					,GROUP_CONCAT(distinct member.account_id) as user_id_list
      from
          maoding_web_project project
					inner join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id=if(project.id is null,null,11))
					inner join maoding_web_company company on (project.company_id = company.id)
					inner join maoding_web_project_member member on (member.project_id= project.id)
      where
          (project.pstatus = '0')
					and (member.deleted = 0)
      group by project.id;

  -- maoding_storage_node -- 树节点通用视图
  CREATE OR REPLACE VIEW `maoding_storage_node` AS
      select
          project.id
          ,null AS pid
          ,if(project.id is null,null,11) as type_id
          ,project.project_name as name
          ,concat('/',project.project_name) as path
          ,unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp
          ,date_format(project.create_date,'%Y-%m-%d %T') as create_time_text
          ,unix_timestamp(if(project.update_date is null,ifnull(project.create_date,0),project.update_date)) as last_modify_time_stamp
          ,date_format(ifnull(project.update_date,project.create_date),'%Y-%m-%d %T') as last_modify_time_text
          ,ifnull(project.update_by,project.create_by) as owner_user_id
					,null as owner_role_id
          ,if(project.id is null,null,0) as file_length
					,null as storage_path
					,null as classic_id
					,null as classic_name
          ,node_type.content as type_name
          ,substring(node_type.content_extra,1,1) as is_directory
          ,substring(node_type.content_extra,2,1) as is_project
          ,substring(node_type.content_extra,3,1) as is_task
					,substring(node_type.content_extra,4,1) as is_design
					,substring(node_type.content_extra,5,1) as is_commit
					,substring(node_type.content_extra,6,1) as is_history
          ,null as issue_id
          ,null as issue_name
          ,null as task_id
          ,null as task_name
          ,null as task_path
          ,project.id as project_id
          ,project.project_name
          ,project.company_id as company_id
          ,company.company_name as company_name
      from
          maoding_web_project project
					inner join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id=if(project.id is null,null,11))
					inner join maoding_web_company company on (project.company_id = company.id)
      where
          (project.pstatus = '0')
      group by project.id

  union all

      select
          concat(project.id,classic_type.value_id) as id
          ,project.id AS pid
          ,(classic_type.value_id * 10 + 10) as type_id
          ,classic_type.content as name
          ,concat('/',project.project_name,'/',classic_type.content) as path
          ,unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp
          ,date_format(project.create_date,'%Y-%m-%d %T') as create_time_text
          ,unix_timestamp(if(project.update_date is null,ifnull(project.create_date,0),project.update_date)) as last_modify_time_stamp
          ,date_format(ifnull(project.update_date,project.create_date),'%Y-%m-%d %T') as last_modify_time_text
          ,ifnull(project.update_by,project.create_by) as owner_user_id
					,null as owner_role_id
          ,if(classic_type.value_id is null,null,0) as file_length
					,null as storage_path
					,classic_type.value_id as classic_id
					,classic_type.content as classic_name
          ,node_type.content as type_name
          ,substring(node_type.content_extra,1,1) as is_directory
          ,substring(node_type.content_extra,2,1) as is_project
          ,substring(node_type.content_extra,3,1) as is_task
					,substring(node_type.content_extra,4,1) as is_design
					,substring(node_type.content_extra,5,1) as is_commit
					,substring(node_type.content_extra,6,1) as is_history
          ,null as issue_id
          ,null as issue_name
          ,null as task_id
          ,null as task_name
          ,null as task_path
          ,project.id as project_id
          ,project.project_name
          ,project.company_id as company_id
          ,company.company_name as company_name
      from
          maoding_const classic_type
					inner join maoding_web_project project on (project.pstatus = '0')
					inner join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id = (classic_type.value_id * 10 + 10))
					inner join maoding_web_company company on (project.company_id = company.id)
      where
					(classic_type.classic_id = 24) and (classic_type.value_id != 0)

  union all

			select
          concat(task.id,classic_type.value_id) as id
          ,if(task.pid is null,concat(task.project_id,classic_type.value_id),concat(task.pid,classic_type.value_id)) AS pid
          ,if(classic_type.value_id=1,if(task.type_id=0,22,21),31) as type_id
          ,task.task_name as name
          ,concat('/',task.project_name,'/',classic_type.content,'/',task.path) as path
          ,unix_timestamp(ifnull(task.create_date,0)) as create_time_stamp
          ,date_format(task.create_date,'%Y-%m-%d %T') as create_time_text
          ,unix_timestamp(if(task.update_date is null,ifnull(task.create_date,0),task.update_date)) as last_modify_time_stamp
          ,date_format(ifnull(task.update_date,task.create_date),'%Y-%m-%d %T') as last_modify_time_text
          ,ifnull(task.update_by,task.create_by) as owner_user_id
					,null as owner_role_id
					,if(task.id is null,null,0) as file_length
					,null as storage_path
					,classic_type.value_id as classic_id
					,classic_type.content as classic_name
          ,node_type.content as type_name
          ,substring(node_type.content_extra,1,1) as is_directory
          ,substring(node_type.content_extra,2,1) as is_project
          ,substring(node_type.content_extra,3,1) as is_task
					,substring(node_type.content_extra,4,1) as is_design
					,substring(node_type.content_extra,5,1) as is_commit
					,substring(node_type.content_extra,6,1) as is_history
          ,task.issue_id
          ,task.issue_name
          ,task.id as task_id
          ,task.task_name
          ,task.path as task_path
          ,task.project_id
          ,task.project_name
          ,task.company_id
          ,task.company_name
      from
          maoding_task task
					inner join maoding_const classic_type on (classic_type.classic_id = 24 and position(concat('!',task.type_id,'!') in (classic_type.content_extra)) > 0)
					inner join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id = if(classic_type.value_id=1,if(task.type_id=0,22,21),31))
			where (task.type_id in (0,1,2))
      group by task.id,classic_type.value_id

  union all

			select
          concat(node.id,classic_type.value_id) as id
          ,concat(node.pid,classic_type.value_id) as pid
          ,node.type_id
          ,node.node_name as name
          ,concat('/',task.project_name,'/',classic_type.content,'/',task.path,'/',node.path) as path
          ,unix_timestamp(ifnull(node.create_time,0)) as create_time_stamp
          ,date_format(node.create_time,'%Y-%m-%d %T') as create_time_text
          ,unix_timestamp(ifnull(node.last_modify_time,0)) as last_modify_time_stamp
          ,date_format(node.last_modify_time,'%Y-%m-%d %T') as last_modify_time_text
          ,node.last_modify_user_id as owner_user_id
          ,node.last_modify_duty_id as owner_role_id
          ,node.file_length
          ,node.path as storage_path
					,classic_type.value_id as classic_id
					,classic_type.content as classic_name
          ,node_type.content as type_name
          ,substring(node_type.content_extra,1,1) as is_directory
          ,substring(node_type.content_extra,2,1) as is_project
          ,substring(node_type.content_extra,3,1) as is_task
					,substring(node_type.content_extra,4,1) as is_design
					,substring(node_type.content_extra,5,1) as is_commit
					,substring(node_type.content_extra,6,1) as is_history
          ,task.issue_id
          ,task.issue_name
          ,task.id as task_id
          ,task.task_name
          ,task.path as task_path
          ,task.project_id
          ,task.project_name
          ,task.company_id
          ,task.company_name
      from
          maoding_storage node
					inner join maoding_const classic_type on (classic_type.classic_id = 24 and position(concat(':',node.type_id,':') in (classic_type.content_extra)) > 0)
          inner join maoding_const node_type on (node_type.classic_id = 14 and node.type_id = node_type.value_id)
					inner join maoding_task task on (node.task_id = task.id)
      where
          (node.deleted = 0)
      group by node.id;

  -- maoding_storage_old_node -- 网站节点视图
  CREATE OR REPLACE VIEW `maoding_storage_old_node` AS
      select
          old_node.id
          ,old_node.pid
          ,if(old_node.type=1,4,if(old_node.type=50,42,41)) as type_id
          ,old_node.file_name as name
          ,concat(if(old_node_parent4.file_name is null,'',concat(old_node_parent4.file_name,'/'))
              ,if(old_node_parent3.file_name is null,'',concat(old_node_parent3.file_name,'/'))
              ,if(old_node_parent2.file_name is null,'',concat(old_node_parent2.file_name,'/'))
              ,if(old_node_parent1.file_name is null,'',concat(old_node_parent1.file_name,'/'))
              ,old_node.file_name) as path
          ,unix_timestamp(ifnull(old_node.create_date,0)) as create_time_stamp
          ,date_format(old_node.create_date,'%Y-%m-%d %T') as create_time_text
          ,unix_timestamp(ifnull(old_node.update_date,ifnull(old_node.create_date,0))) as last_modify_time_stamp
          ,date_format(ifnull(old_node.update_date,old_node.create_date),'%Y-%m-%d %T') as last_modify_time_text
          ,ifnull(old_node.update_by,old_node.create_by) as owner_user_id
          ,null as owner_role_id
          ,old_node.file_size as file_length
          ,concat(if(old_node_parent4.file_name is null,'',concat(old_node_parent4.file_name,'/'))
              ,if(old_node_parent3.file_name is null,'',concat(old_node_parent3.file_name,'/'))
              ,if(old_node_parent2.file_name is null,'',concat(old_node_parent2.file_name,'/'))
              ,if(old_node_parent1.file_name is null,'',concat(old_node_parent1.file_name,'/'))
              ,old_node.file_name) as storage_path
          ,if(old_node.id is null,null,3) as classic_id
          ,if(old_node.id is null,null,'归档') as classic_name
          ,node_type.content as type_name
          ,substring(node_type.content_extra,1,1) as is_directory
          ,substring(node_type.content_extra,2,1) as is_project
          ,substring(node_type.content_extra,3,1) as is_task
          ,substring(node_type.content_extra,4,1) as is_design
          ,substring(node_type.content_extra,5,1) as is_commit
          ,substring(node_type.content_extra,6,1) as is_history
          ,task.issue_id
          ,task.issue_name
          ,old_node.task_id
          ,task.task_name
          ,task.path as task_path
          ,old_node.project_id
          ,project.project_name
          ,old_node.company_id
          ,company.company_name
      from
          maoding_web_project_sky_drive old_node
          inner join maoding_web_project project on (old_node.project_id = project.id)
          left join maoding_web_company company on (old_node.company_id = company.id)
          left join maoding_task task on (old_node.task_id = task.id)
          left join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id = if(old_node.type=1,4,if(old_node.type=50,42,41)))
          left join maoding_web_project_sky_drive old_node_parent1 ON (old_node_parent1.id = old_node.pid)
          left join maoding_web_project_sky_drive old_node_parent2 ON (old_node_parent2.id = old_node_parent1.pid)
          left join maoding_web_project_sky_drive old_node_parent3 ON (old_node_parent3.id = old_node_parent2.pid)
          left join maoding_web_project_sky_drive old_node_parent4 ON (old_node_parent4.id = old_node_parent3.pid)
      where
          (old_node.status in (0,1))
      group by old_node.id;

  -- maoding_role -- 角色视图
  CREATE OR REPLACE VIEW `maoding_role` AS
      select
           concat(if(role_classic.value_id in (1,2,3),task_role.project_id,ifnull(task_role.node_id,task_role.target_id)),role_classic.value_id) as id
          ,role_classic.value_id as type_id
          ,role_classic.content as type_name
          ,task_role.account_id as user_id
          ,task_role.project_id as project_id
          ,ifnull(task_role.node_id,task_role.target_id) as task_id
          ,task_role.company_id as company_id
          ,unix_timestamp(ifnull(task_role.create_date,0)) as create_time_stamp
          ,date_format(task_role.create_date,'%y-%m-%d %T') as create_time_text
          ,unix_timestamp(if(task_role.update_date is null,ifnull(task_role.create_date,0),task_role.update_date)) as last_modify_time_stamp
          ,date_format(ifnull(task_role.update_date,task_role.create_date),'%y-%m-%d %T') as last_modify_time_text
          ,account.user_name
          ,project.project_name
          ,task.task_name
          ,company.company_name
      from
          maoding_web_project_member task_role
          inner join maoding_const role_classic on (role_classic.classic_id = 26 and role_classic.content_extra like concat('%!',task_role.member_type,'!%'))
          inner join maoding_web_account account on (task_role.account_id = account.id)
          inner join maoding_web_project project on (task_role.project_id = project.id)
          left join maoding_web_project_task task on (ifnull(task_role.node_id,task_role.target_id) = task.id)
          inner join maoding_web_company company on (task_role.company_id = company.id)
      where
          task_role.deleted = 0
      group by task_role.id

  union all

      select
           concat(company_role.company_id,role_classic.value_id) as id
          ,role_classic.value_id as type_id
          ,role_classic.content as type_name
          ,company_role.user_id as user_id
          ,null as project_id
          ,null as task_id
          ,company_role.company_id as company_id
          ,unix_timestamp(ifnull(company_role.create_date,0)) as create_time_stamp
          ,date_format(company_role.create_date,'%y-%m-%d %T') as create_time_text
          ,unix_timestamp(if(company_role.update_date is null,ifnull(company_role.create_date,0),company_role.update_date)) as last_modify_time_stamp
          ,date_format(ifnull(company_role.update_date,company_role.create_date),'%y-%m-%d %T') as last_modify_time_text
          ,account.user_name
          ,null as project_name
          ,null as task_name
          ,company.company_name
      from
          maoding_web_user_permission company_role
          INNER JOIN maoding_web_account account on (company_role.user_id = account.id)
          inner join maoding_web_company company on (company_role.company_id = company.id)
          inner join maoding_web_permission old_permission_define on (company_role.permission_id = old_permission_define.id)
          inner join maoding_web_permission old_role_define on (old_permission_define.root_id = old_role_define.id)
          inner join maoding_const role_classic on (role_classic.classic_id = 26 and role_classic.content_extra like concat('%:',old_role_define.id,':%'))
      group by company.id,role_classic.value_id;

END;

-- 建立初始化常量存储过程
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
BEGIN
  -- 常量分类
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,0,'常量分类',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,1,'操作权限',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,2,'合作类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,3,'任务类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,4,'财务类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,6,'节点类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,7,'动态类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,8,'个人任务类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,9,'邀请目的类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,10,'通知类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,12,'用户类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,13,'组织类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,14,'存储节点类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,15,'锁定状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,16,'同步模式',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,17,'删除状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,19,'文件服务器类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,20,'文件操作类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,21,'父节点类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,22,'专业类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,23,'任务角色类型',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,24,'资料分类',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,25,'角色分类类型',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,26,'角色类型',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,27,'通知范围类型',null);

  -- 项目角色类型
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,27,'通知范围类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,0,'未定义范围',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,1,'用户','User');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,2,'任务','Task');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,3,'项目','Project');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,4,'组织','Company');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,5,'公共','Web');

  -- 角色类型
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,26,'角色类型','!!member_type,::permission_type');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,0,'未知角色',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,1,'立项人','!0!');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,2,'经营负责人','!1!');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,3,'设计负责人','!2!');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,4,'任务负责人','!3!');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,5,'设计','!4!');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,6,'校对','!5!');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,7,'审核','!6!');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,8,'系统管理',':1:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,9,'组织管理',':2:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,10,'企业负责人',':3:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,11,'行政管理',':4:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,12,'项目管理',':5:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,13,'财务管理',':6:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,14,'统计及报表',':7:');

  -- 角色分类类型
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,25,'角色分类类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,0,'未知分类',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,1,'项目角色',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,2,'任务角色',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,3,'组织角色',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,4,'任务用户角色',null);

  -- 资料分类
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,24,'资料分类','!!-包含的任务类型，::-包含的存储节点类型');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (24,0,'未知分类',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (24,1,'设计','!0!1!2!:0:1:10:18:23:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (24,2,'提资','!1!2!:2:3:32:33:');

	-- 角色类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,23,'任务角色类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,0,'立项人',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,1,'经营负责人',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,2,'设计负责人',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,3,'任务负责人',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,4,'设计',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,5,'校对',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,6,'审核',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,10,'企业负责人',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,11,'财务',null);

	-- 父节点类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,22,'专业类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,0,'规划',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,1,'建筑',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,2,'室内',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,3,'景观',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,4,'结构',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,5,'给排水',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,6,'暖通',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,7,'电气',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (22,8,'其他',null);

	-- 父节点类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,21,'父节点类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,0,'本树节点',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,1,'项目节点','project_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,2,'任务节点','task_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,3,'报销节点','exp_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,4,'通告节点','notice_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,5,'公司节点','company_id');

	-- 文件操作类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,20,'文件操作类型','0:生成的文件类型;1:存放文件的目录;2:存放文件的服务器类型;3:文件服务器地址');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,0,'无效','1;;;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,1,'备份','3;历史版本/{SrcFileNoExt}_{Time:yyyyMMddHHmmss}{Ext};;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,2,'校对','1;{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,3,'审核','1;{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,4,'提资','2;/{Project}/{Classic2}/{IssuePath}/{Major}/{Version}/{TaskPath}/{SrcPath};;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,5,'上传','4;;2;');

	-- 文件服务器类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,19,'文件服务器类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,0,'无效',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,1,'本地磁盘',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,2,'网站空间',null);

	-- 删除状态
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,17,'删除状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (17,0,'未删除',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (17,1,'已删除',null);

	-- 同步模式
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,16,'同步模式',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (16,0,'手动同步',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (16,1,'自动同步',null);

	-- 锁定状态
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,15,'锁定状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (15,0,'不锁定',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (15,1,'锁定',null);

	-- 存储节点类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,14,'存储节点类型','1:是否目录，2:是否项目，3:是否任务，4:是否设计文档，5:是否提资文档，6:是否历史版本，[]:子目录类型，<>子文件类型');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,0,'未知类型','000100[0]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,1,'设计文件','000100[0]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,2,'提资资料','000010[0]<2>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,3,'历史版本','000001[0]<3>');
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,4,'网站归档文件','000000[0]<4>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,10,'未知类型目录','100000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,11,'项目目录','110000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,12,'任务目录','101000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,13,'组织目录','100000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,14,'通告目录','100000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,15,'报销目录','100000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,16,'备份目录','100001[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,17,'回收站目录','100000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,18,'用户目录','100100[18]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,20,'设计目录','100100[23]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,21,'设计签发任务目录','101100[23]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,22,'设计生产任务目录','101100[23]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,23,'设计自定义目录','100100[23]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,30,'提资目录','100010[33]<2>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,31,'提资任务目录','101010[33]<2>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,32,'提资历史目录','100011[33]<3>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,33,'提资自定义目录','100010[33]<2>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,40,'档案目录','100000[10]<1>');
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,41,'网站目录','100000[10]<4>');
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,42,'网站归档目录','100000[10]<4>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,50,'成果目录','100000[10]<1>');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,60,'设计依据目录','100000[10]<1>');

	-- 共享类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,11,'共享类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (11,0,'全部共享',null);

	-- 文件类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,5,'文件类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,0,'未知类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,1,'CAD设计文档',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,3,'合同附件',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,4,'公司logo',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,5,'认证授权书',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,6,'移动端上传轮播图片',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,7,'公司邀请二维码',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,8,'营业执照',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,9,'法人身份证信息',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,20,'报销附件',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (5,21,'通知公告附件',null);
END;

-- 建立重置索引存储过程
DROP PROCEDURE IF EXISTS `createIndex`;
CREATE PROCEDURE `createIndex`()
BEGIN
	-- 重新创建索引
	declare sqlString VARCHAR(255);
	declare tableName VARCHAR(255);
	declare doneTable tinyint default false;
	declare curTable cursor for select table_name from information_schema.tables where (table_schema=database()) && (table_type='BASE TABLE');
	declare continue HANDLER for not found set doneTable = true;

	open curTable;
	fetch curTable into tableName;
	while (not doneTable) do
		BEGIN
				-- 创建针对名称为*_id的字段的索引
				declare fieldName VARCHAR(255);
				declare doneField tinyint default false;
				declare curField cursor for select column_name from information_schema.columns
																		where (table_schema=database()) and (table_name=tableName)
																				and ((column_name='deleted') or (column_name='pid') or (column_name='path') or (column_name like '%\_id') or (column_name like '%\_pid'));
				declare continue HANDLER for not found set doneField = true;
				open curField;
				fetch curField into fieldName;
				while (not doneField) do
					-- 删除原有字段同名索引
					BEGIN
						declare indexName VARCHAR(255);
						declare doneDrop tinyint default false;
						declare curIndex cursor for select index_name from information_schema.statistics
																				where (table_schema=database()) and (table_name=tableName) and (index_name=fieldName);
						declare continue HANDLER for not found set doneDrop = true;
						open curIndex;
						fetch curIndex into indexName;
						while (not doneDrop) do
							set @sqlString = concat("drop index ",fieldName," on ",tableName);
							prepare sqlCommand from @sqlString;
							execute sqlCommand;
							drop PREPARE sqlCommand;
							fetch curIndex into indexName;
						end while;
						close curIndex;
					END;

					-- 创建与字段同名索引
					BEGIN
						set @sqlString = concat("create index ",fieldName," on ",tableName," (",fieldName,")");
						prepare sqlCommand from @sqlString;
						execute sqlCommand;
						drop PREPARE sqlCommand;
					END;
					fetch curField into fieldName;
				end while;
				close curField;
		END;
		fetch curTable into tableName;
	end while;
	close curTable;
END;

-- 建立备份数据存储过程
DROP PROCEDURE IF EXISTS `backupData`;
CREATE PROCEDURE `backupData`()
BEGIN
	declare tableName VARCHAR(255);
	declare doneTable tinyint default false;
	declare curTable cursor for select table_name from information_schema.tables where (table_schema=database()) and (TABLE_NAME not like 'backup_%');
	declare continue HANDLER for not found set doneTable = true;

	open curTable;
	fetch curTable into tableName;
	while (not doneTable) do
		BEGIN
			-- 清理原有备份表
			set @sqlString = concat("drop table if exists `backup_",tableName,'`');
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
			-- 创建备份表
			set @sqlString = concat("create table `backup_",tableName,'` like ',tableName);
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
			-- 备份数据
			set @sqlString = concat("insert `backup_",tableName,'` select * from ',tableName);
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
		END;
		fetch curTable into tableName;
	end while;
	close curTable;
END;

-- 建立还原备份数据存储过程
DROP PROCEDURE IF EXISTS `restoreData`;
CREATE PROCEDURE `restoreData`()
BEGIN
	declare tableName VARCHAR(255);
	declare doneTable tinyint default false;
	declare curTable cursor for select a.table_name from information_schema.tables a inner join information_schema.tables b on (concat('backup_',a.TABLE_NAME) = b.TABLE_NAME)
	where (a.table_schema=database()) and (a.TABLE_NAME not like 'backup_%') and (b.table_schema=database()) and (b.TABLE_NAME like 'backup_%');
	declare continue HANDLER for not found set doneTable = true;

	open curTable;
	fetch curTable into tableName;
	while (not doneTable) do
		BEGIN
			declare fieldName VARCHAR(255);
			declare fieldLength LONG;
			declare fieldType VARCHAR(255);
			declare doneField tinyint default false;
			declare curField cursor for select a.column_name,a.CHARACTER_MAXIMUM_LENGTH,a.DATA_TYPE from information_schema.columns a inner join information_schema.columns b on (a.column_name=b.column_name)
			where (a.table_schema=database()) and (a.table_name=tableName) and (b.table_schema=database()) and (b.table_name=concat('backup_',tableName));
			declare continue HANDLER for not found set doneField = true;

			open curField;
			fetch curField into fieldName,fieldLength,fieldType;
			set @cs = "";
			set @vs = "";
			while (not doneField) do
				if (@cs != "") then
					set @cs = concat(@cs,",");
					set @vs = concat(@vs,",");
				end if;
				set @cs = concat(@cs,"`",fieldName,"`");
				if ((fieldType='varchar') or (fieldType='char') or (fieldType='text') or (fieldType='longtext')) then
					set @vs = concat(@vs,"left(`",fieldName,"`,",fieldLength,")");
				else
					set @vs = concat(@vs,"`",fieldName,"`");
				end if;
				fetch curField into fieldName,fieldLength,fieldType;
			end while;
			close curField;

			set @sqlString = concat("replace into ",tableName," (",@cs,") select ",@vs," from backup_",tableName);
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
		END;
		fetch curTable into tableName;
	end while;
	close curTable;
END;

call updateTables();
call updateViews();
call initConst();