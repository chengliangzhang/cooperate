-- 建立更新表结构存储过程
DROP PROCEDURE IF EXISTS `updateTables`;
CREATE PROCEDURE `updateTables`()
BEGIN
  -- -- 文件注解附件
  CREATE TABLE IF NOT EXISTS `md_list_attachment` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `annotate_id` char(32) DEFAULT NULL COMMENT '文件注解编号',
    `attachment_file_id` char(32) DEFAULT NULL COMMENT '文件类附件编号',
    `attachment_element_id` char(32) DEFAULT NULL COMMENT '嵌入元素类附件编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 文件注解
  CREATE TABLE IF NOT EXISTS `md_tree_annotate` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父注解编号',
    `node_name` varchar(255) DEFAULT NULL COMMENT '文件注解标题',
    `path` varchar(255) DEFAULT NULL COMMENT '文件注解路径，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '文件注解类型',

    `content` text(2048) DEFAULT NULL COMMENT '文件注解正文',
    `file_id` char(32) DEFAULT NULL COMMENT '被注解的文件的编号',
    `main_file_id` char(32) DEFAULT NULL COMMENT '原始文件的编号',
    `status_id` varchar(40) DEFAULT '0' COMMENT '文件注解状态',
    `creator_user_id` char(32) DEFAULT NULL COMMENT '注解创建者用户编号',
    `creator_role_id` varchar(40) DEFAULT NULL COMMENT '注解创建者职责编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_annotate' and column_name='status_id') then
    alter table md_tree_annotate add column `status_id` varchar(40) DEFAULT '0' COMMENT '文件注解状态';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_annotate' and column_name='creator_role_id') then
    alter table md_tree_annotate add column `creator_role_id` varchar(40) DEFAULT NULL COMMENT '注解创建者职责编号';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_annotate' and column_name='creator_role_id' and data_type='varchar' and character_maximum_length>=40) then
    alter table md_tree_annotate modify column `creator_role_id` varchar(40) DEFAULT NULL COMMENT '注解创建者职责编号';
  end if;

  -- -- 内嵌HTML元素
  CREATE TABLE IF NOT EXISTS `md_list_element` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `title` varchar(255) DEFAULT NULL COMMENT '占位符',
    `data_array` longblob DEFAULT NULL COMMENT '元素内容',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_element' and column_name='title') then
    alter table md_list_element add column `title` varchar(255) DEFAULT NULL COMMENT '占位符';
  end if;

  -- -- 常量定义
	CREATE TABLE IF NOT EXISTS `md_const` (
		`classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
		`code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号',
		`title` varchar(255) NOT NULL COMMENT '常量的可显示定义',
		`extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义',
		PRIMARY KEY (`classic_id`,`code_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_const' and column_name='code_id') then
    alter table md_const add column `code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_const' and column_name='title') then
    alter table md_const add column `title` varchar(255) NOT NULL COMMENT '常量的可显示定义';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_const' and column_name='extra') then
    alter table md_const add column `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义';
  end if;

	-- -- 自定义常量定义
	CREATE TABLE IF NOT EXISTS `md_list_const_custom` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `company_id` char(32) DEFAULT NULL COMMENT '自定义常量所属组织id',
    `task_id` char(32) DEFAULT NULL COMMENT '自定义常量所属任务id',

    `classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
    `code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号',
    `title` varchar(255) NOT NULL COMMENT '常量的可显示定义',
    `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const_custom' and column_name='code_id') then
    alter table md_list_const_custom add column `code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const_custom' and column_name='title') then
    alter table md_list_const_custom add column `title` varchar(255) NOT NULL COMMENT '常量的可显示定义';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const_custom' and column_name='extra') then
    alter table md_list_const_custom add column `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义';
  end if;

	-- -- 协同节点定义
	CREATE TABLE IF NOT EXISTS `md_tree_storage` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

		`pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
		`path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
		`type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

		`task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id',
    `owner_user_id` char(32) DEFAULT NULL COMMENT '节点所属用户id',

    `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '节点文件长度',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='file_length') then
    alter table md_tree_storage add column `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '节点文件长度';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='node_name') then
    alter table md_tree_storage add column `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称';
  end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='task_id') then
		alter table md_tree_storage add column `task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='last_modify_role_id') then
		alter table md_tree_storage add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='owner_user_id') then
		alter table md_tree_storage add column `owner_user_id` char(32) DEFAULT NULL COMMENT '节点所属用户id';
	end if;


	-- -- 协同文件定义
	CREATE TABLE IF NOT EXISTS `md_list_storage_file` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',
    
		`server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型',
		`server_address` varchar(255) DEFAULT NULL COMMENT '文件服务器地址',
		`base_dir` varchar(255) DEFAULT NULL COMMENT '文件在文件服务器上的存储位置',
		`file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型',
		`file_version` varchar(20) DEFAULT NULL COMMENT '文件版本',
		`major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id',
		`main_file_id` char(32) DEFAULT NULL COMMENT '所对应的原始文件id',
		`read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称',
    `read_only_file_length` bigint(16) unsigned DEFAULT '0' COMMENT '只读文件长度',
		`read_only_file_md5` varchar(64) DEFAULT NULL COMMENT '只读文件md5校验和',
		`writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称',
    `writable_file_length` bigint(16) unsigned DEFAULT '0' COMMENT '可写文件长度',
    `writable_file_md5` varchar(64) DEFAULT NULL COMMENT '可写文件md5校验和',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='read_only_file_md5') then
    alter table md_list_storage_file add column `read_only_file_md5` varchar(64) DEFAULT NULL COMMENT '只读文件校验和';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='writable_file_md5') then
    alter table md_list_storage_file add column `writable_file_md5` varchar(64) DEFAULT NULL COMMENT '可写文件校验和';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='read_only_file_length') then
    alter table md_list_storage_file add column `read_only_file_length` bigint(16) unsigned DEFAULT '0' COMMENT '只读文件长度';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='writable_file_length') then
    alter table md_list_storage_file add column `writable_file_length` bigint(16) unsigned DEFAULT '0' COMMENT '可写文件长度';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='file_type_id') then
    alter table md_list_storage_file add column `file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='file_type_id' and data_type='varchar') then
    alter table md_list_storage_file modify column `file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='server_type_id') then
    alter table md_list_storage_file add column `server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='server_type_id' and data_type='varchar') then
    alter table md_list_storage_file modify column `server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='last_modify_role_id') then
    alter table md_list_storage_file add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='last_modify_role_id' and character_maximum_length>32) then
    alter table md_list_storage_file modify column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='major_type_id') then
    alter table md_list_storage_file add column `major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='major_type_id' and data_type='varchar') then
    alter table md_list_storage_file modify column `major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='base_dir') then
    alter table md_list_storage_file add column `base_dir` varchar(255) DEFAULT NULL COMMENT '文件在文件服务器上的存储位置';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='read_only_key') then
    alter table md_list_storage_file add column `read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='writable_key') then
    alter table md_list_storage_file add column `writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称';
  end if;


  -- -- 协同文件校审提资历史记录定义
	CREATE TABLE IF NOT EXISTS `md_list_storage_file_his` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',
    
    `main_file_id` char(32) DEFAULT NULL COMMENT '协同文件编号',
    `action_type_id` varchar(40) DEFAULT '0' COMMENT '校审动作类型',
    `remark` text(2048) DEFAULT NULL COMMENT '文件注释',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='remark') then
		alter table md_list_storage_file_his add column `remark` text(2048) DEFAULT NULL COMMENT '文件注释';
	end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='last_modify_role_id') then
    alter table md_list_storage_file_his add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='main_file_id') then
    alter table md_list_storage_file_his add column `main_file_id` char(32) DEFAULT NULL COMMENT '协同文件编号id';
  end if;

  -- -- 组织定义
  CREATE TABLE IF NOT EXISTS `md_tree_org` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
    `path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

    `company_id` char(32) DEFAULT NULL COMMENT '组织详细内容编号',
    `response_user_id` char(32) DEFAULT NULL COMMENT '负责人用户编号',
    `response_role_id` varchar(40) DEFAULT NULL COMMENT '负责人角色编号',
    `role_type_id` varchar(40) DEFAULT NULL COMMENT '默认角色编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 公司定义
  CREATE TABLE IF NOT EXISTS `md_list_org_company` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `company_name` varchar(255) DEFAULT NULL COMMENT '公司名称',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 成员定义
  CREATE TABLE IF NOT EXISTS `md_list_role` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `org_id` char(32) DEFAULT NULL COMMENT '组织id',
    `work_id` char(32) DEFAULT NULL COMMENT '工作id',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 工作定义
  CREATE TABLE IF NOT EXISTS `md_tree_work` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
    `path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

    `response_user_id` char(32) DEFAULT NULL COMMENT '负责人用户编号',
    `response_role_id` varchar(40) DEFAULT NULL COMMENT '负责人角色编号',
    `start_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '启动时间',
    `end_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '预期结束时间',
    `company_id` char(32) DEFAULT NULL COMMENT '所属公司编号',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 项目定义
  CREATE TABLE IF NOT EXISTS `md_list_work_project` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `from_company_id` char(32) DEFAULT NULL COMMENT '甲方编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 签发任务定义
  CREATE TABLE IF NOT EXISTS `md_list_work_issue` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `from_company_id` char(32) DEFAULT NULL COMMENT '来源组织编号',
    `project_id` char(32) DEFAULT NULL COMMENT '项目编号',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 生产任务定义
  CREATE TABLE IF NOT EXISTS `md_list_work_task` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `issue_id` char(32) DEFAULT NULL COMMENT '来源签发任务编号',
    `project_id` char(32) DEFAULT NULL COMMENT '项目编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 项目和任务更改历史
  CREATE TABLE IF NOT EXISTS `md_list_work_his` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `work_id` char(32) DEFAULT NULL COMMENT '工作编号',
    `start_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '启动时间',
    `end_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '预期结束时间',
    `remark` text(2048) DEFAULT NULL COMMENT '更改注释',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 用户界面
  CREATE TABLE IF NOT EXISTS `md_list_gui` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `gui_type_id` varchar(40) DEFAULT NULL COMMENT '界面参数类型',
    `user_id` char(32) DEFAULT NULL COMMENT '相关用户',
    `record_id` varchar(40) DEFAULT NULL COMMENT '相关记录',
    `order_num` int(8) unsigned DEFAULT '0' COMMENT '记录排序序号',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 财务记录
  CREATE TABLE IF NOT EXISTS `md_tree_finance` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
    `path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

    `project_id` char(32) DEFAULT NULL COMMENT '相关项目编号',
    `fee` bigint(20) DEFAULT '0' COMMENT '费用金额',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- -- 收支记录
  CREATE TABLE IF NOT EXISTS `md_list_finance_bill` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `bill_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '收发日期',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

END;
call updateTables();

-- 建立初始化常量存储过程
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- 常量分类
    delete from md_const where classic_id = 0;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,0,'常量分类',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,1,'操作权限',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,2,'合作类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,3,'任务类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,4,'财务类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,6,'财务节点类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,7,'动态类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,8,'个人任务类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,9,'邀请目的类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,10,'通知类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,12,'用户类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,13,'组织类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,14,'存储节点类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,15,'锁定状态',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,16,'同步模式',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,17,'删除状态',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,19,'文件服务器类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,20,'文件操作类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,21,'sky drive文档类型定义',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,22,'专业类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,23,'web task任务类型定义',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,24,'资料分类',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,25,'保留',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,26,'角色类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,27,'通知类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,28,'web权限组类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,29,'web权限类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,30,'web member角色类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,31,'校审意见类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,32,'校审意见状态类型',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,32,'校审意见状态类型','');
    delete from md_const where classic_id = 32;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (32,0,'未知状态','');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (32,1,'通过','');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (32,2,'不通过','');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,31,'校审意见类型','');
    delete from md_const where classic_id = 31;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (31,0,'未知类型','');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (31,1,'校验','');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (31,2,'审核','');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,30,'web member角色类型','1.类型布尔属性，1-项目角色,2-任务角色,3-任务负责人,4-设计,5-校对,6-审核；2.对应的角色;3.对应的mytask内task_type;4.对应的process内的名称');
    delete from md_const where classic_id = 30;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (30,0,'立项人','100000;23;;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (30,1,'经营负责人','100000;20;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (30,2,'设计负责人','100000;30;2;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (30,3,'任务负责人','111000;40;12,13;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (30,4,'设计','110100;41;3;设计');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (30,5,'校对','110010;42;3;校对');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (30,6,'审核','110001;43;3;审核');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,29,'web权限类型','1.类型布尔属性，1-转换需加偏移量;2.权限标识;3.对应的基准角色;4.设置所需权限');
    delete from md_const where classic_id = 29;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,100,'创建分支架构/事业合伙人','0;org_partner;100;21');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,8,'权限配置','1;sys_role_permission;60;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,58,'企业认证','0;sys_role_auth;101;22');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,103,'历史数据导入','0;data_import;102;23');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,12,'组织信息管理','0;com_enterprise_edit;110;24');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,14,'组织架构设置','0;hr_org_set,hr_employee;111;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,19,'通知公告发布','0;admin_notice;112;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,46,'查看报销、费用统计报表','0;report_exp_static;130;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,110,'查看请假、出差、工时统计报表','0;summary_leave;131;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,54,'删除项目','0;project_delete;140;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,51,'任务签发','0;project_manager;141;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,52,'生产安排','0;design_manager;142;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,37,'合同信息','0;project_view_amount;143;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,20,'项目信息/项目总览/查看项目文档','0;project_edit;144;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,401,'查看财务报表','0;finance_report;120;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,40,'费用录入','0;finance_report;121;25');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,10,'财务设置','0;sys_finance_type;122;26');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,49,'确认付款日期','0;project_charge_manage;123;27');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (29,402,'确认到账日期','0;finance_back_fee;124;28');
    
    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,28,'web权限组类型','1.包含的web权限类型');
    delete from md_const where classic_id = 28;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (28,0,'未定义权限组','');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (28,1,'后台管理','100,8,58,103');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (28,2,'组织管理','12,14,19');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (28,3,'审批报表','46,110');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (28,4,'项目管理','54,51,52,37,20');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (28,5,'财务管理','401,40,10,49,402');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,27,'通知类型','0.主题;1.标题;2.内容');
    delete from md_const where classic_id = 27;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (27,0,'未定义类型',';;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (27,1,'用户通用消息','User{UserId};用户消息;普通用户消息');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (27,2,'任务通用消息','Task{TaskId};任务消息;普通任务消息');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (27,3,'项目通用消息','Project{ProjectId};项目消息;普通项目消息');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (27,4,'组织通用消息','Company{CompanyId};组织消息;普通组织消息');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (27,5,'公共通用消息','notify:web;公共消息;普通公共消息');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,26,'角色类型','1.可分配角色；2.权限');
    delete from md_const where classic_id = 26;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,0,'-;未知角色',';');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,10,'company_manager;总公司企业负责人','11,12,13,20,30,40,41,42,43,50,60,100,101,102,110,111,112,120,121,122,123,124,130,131,140,141,142,143,144;10,11,12,13');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,11,'company_manager_1;1类公司企业负责人','20,30,40,41,42,43,51,61,100,111,112,120,121,123,124,130,131,140,141,142,143,144;11');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,12,'company_manager_2;2类公司企业负责人','20,30,40,41,42,43,52,62,111,112,120,121,123,124,130,131,140,141,142,143,144;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,13,'company_manager_3;3类公司企业负责人','20,30,40,41,42,43,53,63,111,112,120,130,131,140,141,142,143,144;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,20,'project_manager;经营负责人','21,30,31;60');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,21,'project_assistant;经营助理','30,31;60');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,23,'project_creator;立项人',';');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,30,'design_manager;设计负责人','31,40,41,42,43;70,71');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,31,'design_assistant;设计助理','40,41,42,43;70,71');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,40,'task_manager;任务负责人','41,42,43;70');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,41,'designer;设计',';');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,42,'checker;校对',';');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,43,'auditor;审核',';');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,50,'sys_manager;系统管理员','60,100,101,102,110,111,112,120,121,122,123,124,130,131,140,141,142,143,144;10,11,12,13');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,51,'sys_manager_1;1类公司系统管理员','61,100,111,112,120,121,123,124,130,131,140,141,142,143,144;11,12,13');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,52,'sys_manager_2;2类公司系统管理员','62,111,112,120,121,123,124,130,131,140,141,142,143,144;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,53,'sys_manager_3;3类公司系统管理员','63,111,112,120,130,131,140,141,142,143,144;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,60,'permission_manager;总公司权限配置管理员',';20,21,22,23,24,25,26,27,28');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,61,'permission_manager_1;1类公司权限配置管理员',';20,21,27,28');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,62,'permission_manager_2;2类公司权限配置管理员',';20,27,28');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,63,'permission_manager_3;3类公司权限配置管理员',';20');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,100,'sub_org_manager;分支架构/事业合伙人管理员',';11,12,13');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,101,'company_audit_manager;企业认证管理员',';14');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,102,'data_manager;历史数据导入管理员',';100');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,110,'org_info_manager;组织信息管理员',';30');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,111,'org_tree_manager;组织架构设置管理员',';31');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,112,'org_notify_manager;通知公告发布管理员',';90');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,120,'financial_viewer;财务报表查看者',';110');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,121,'financial_input;费用录入管理员',';121');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,122,'financial_setting_manager;财务设置管理员',';120');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,123,'pay_auditor;付款日期确认管理员',';130');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,124,'gain_auditor;到账日期确认管理员',';131');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,130,'financial_report_viewer;报销、费用统计报表查看者',';111');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,131,'work_time_report_viewer;请假、出差、工时统计报表查看者',';112');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,140,'project_delete_manager;项目删除管理员',';40,41');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,141,'issue_manager;任务签发管理员','20,21;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,142,'task_manager;生产安排管理员','30,31;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,143,'contract_manager;合同信息管理员',';51,52');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (26,144,'project_info_viewer;项目信息/项目总览/项目文档查看者',';52');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,25,'web my_task任务类型定义',null);
    delete from md_const where classic_id = 25;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (25,0,'未知分类',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (25,1,'项目角色',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (25,2,'任务角色',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (25,3,'组织角色',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (25,4,'任务用户角色',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,24,'资料分类','1.分类布尔属性,1-是根目录,2-不过滤任务;2.目录节点类型;3.下属节点类型');
    delete from md_const where classic_id = 24;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (24,0,'未知分类',';');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (24,1,'设计','00;20;0,1,18,21,22,23');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (24,2,'提资','01;30;2,3,31,32,33');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (24,3,'发布','01;40;4,10,42,43');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (24,5,'网站','01;40;4,10,42,43');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,23,'web task任务类型定义','1.任务布尔属性,1-是经营任务,2-是生产任务;2.转换时节点类型生成偏移量,3.归属的资料分类目录');
    delete from md_const where classic_id = 23;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (23,0,'生产任务','01;2;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (23,1,'签发任务','10;1;1,2,3');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (23,2,'签发生产任务','11;1;1,2,3');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,22,'专业类型',null);
    delete from md_const where classic_id = 22;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,0,'规划',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,1,'建筑',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,2,'室内',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,3,'景观',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,4,'结构',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,5,'给排水',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,6,'暖通',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,7,'电气',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (22,8,'其他',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,21,'sky drive文档类型定义','1.转换成目录的节点类型,1.转换成文件的节点类型');
    delete from md_const where classic_id = 21;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,0,'目录','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,1,'文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,2,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,3,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,4,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,5,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,6,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,7,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,8,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,9,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,10,'未知类型文件','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,20,'未知类型目录','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,21,'未知类型目录','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,30,'未知类型目录','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,40,'任务目录','41;4');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (21,50,'归档目录','42;4');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,20,'文件操作类型','1.新建节点类型;2.新建节点路径;3.文件服务器类型;4.文件服务器地址;5.服务器空间;6.通知类型');
    delete from md_const where classic_id = 20;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (20,0,'无效','1;;;;;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (20,1,'备份','3;历史版本/{SrcFileNoExt}_{Time:yyyyMMddHHmmss}{Ext};1;;;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (20,2,'校对','5;{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;;;2');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (20,3,'审核','6;{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;;;2');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (20,4,'提资','2;/{Project}/{Range2}/{IssuePath}/{Major}/{Version}/{TaskPath}/{SrcFile};1;;;3');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (20,5,'上传','4;/{Project}/{Range3}/{IssuePath}/{ProjectId}-{CompanyId}-{TaskId}-{SkyPid}-{OwnerUserId}/{SrcFile};2;;;5');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,19,'文件服务器类型','1.默认服务器地址(分号使用|代替);2.默认文件存储空间');
    delete from md_const where classic_id = 19;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (19,0,'未知',';');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (19,1,'ICE管理磁盘','127.0.0.1|127.0.0.1;c:/work/file_server');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (19,2,'直连网站空间','http://172.16.6.73:8081/filecenter;group1');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,17,'删除状态',null);
    delete from md_const where classic_id = 17;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (17,0,'未删除',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (17,1,'已删除',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,16,'同步模式',null);
    delete from md_const where classic_id = 16;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (16,0,'手动同步',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (16,1,'自动同步',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,15,'锁定状态',null);
    delete from md_const where classic_id = 15;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (15,0,'不锁定',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (15,1,'锁定',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,14,'存储节点类型','1.节点布尔属性，1-是否目录，2-是否项目，3-是否任务，4-是否设计文档，5-是否提资文档，6-是否历史版本;2.子目录类型;3.子文件类型;4.文件所属角色');
    delete from md_const where classic_id = 14;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,0,'未知类型','000100;;;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,1,'设计文件','000100;;;41');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,2,'提资资料','000010;;;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,3,'历史版本','000001;;;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,4,'网站归档文件','000000;;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,5,'提交校验文件','000100;;42');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,6,'提交审核文件','000100;;43');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,10,'未知类型目录','100000;10;1');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,11,'项目目录','110000;10;1;23');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,12,'任务目录','101000;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,13,'组织目录','100000;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,14,'通告目录','100000;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,15,'报销目录','100000;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,16,'备份目录','100001;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,17,'回收站目录','100000;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,18,'用户目录','100100;18;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,20,'设计目录','100100;23;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,21,'设计签发任务目录','101100;23;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,22,'设计生产任务目录','101100;23;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,23,'设计自定义目录','100100;23;1');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,30,'提资目录','100010;33;2;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,31,'提资任务目录','101010;33;2;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,32,'提资历史目录','100011;33;3;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,33,'提资自定义目录','100010;33;2;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,40,'档案目录','100000;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,41,'网站目录','100000;43;4;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,42,'网站归档目录','100000;43;4;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,50,'成果目录','100000;10;1;');
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (14,60,'设计依据目录','100000;10;1;');

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,11,'共享类型',null);
    delete from md_const where classic_id = 11;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (11,0,'全部共享',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,5,'文件类型',null);
    delete from md_const where classic_id = 5;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,0,'未知类型',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,1,'CAD设计文档',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,3,'合同附件',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,4,'公司logo',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,5,'认证授权书',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,6,'移动端上传轮播图片',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,7,'公司邀请二维码',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,8,'营业执照',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,9,'法人身份证信息',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,20,'报销附件',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (5,21,'通知公告附件',null);

    -- -- -- -- --
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (0,1,'操作权限',null);
    delete from md_const where classic_id = 1;
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,0,'-;无权限',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,10,'sys_enterprise_logout;解散自己负责的组织',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,11,'invite_others;邀请事业合伙人/分公司',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,12,'org_partner;创建非自己负责的事业合伙人/分公司',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,13,'disband_others;解散非自己负责的事业合伙人/分公司',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,14,'sys_role_auth;申请企业认证',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,20,'sys_role_permission;权限配置',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,21,'sys_role_permission_sub_org;权限配置-配置创建分支机构/事业合伙人管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,22,'sys_role_permission_company_audit;权限配置-配置企业认证管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,23,'sys_role_permission_data_import;权限配置-配置历史数据导入管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,24,'sys_role_permission_org_info;权限配置-配置组织信息管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,25,'sys_role_permission_financial_input;权限配置-配置费用录入管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,26,'sys_role_permission_financial_setting;权限配置-配置财务设置管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,27,'sys_role_permission_pay_confirm;权限配置-配置付款日期管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,28,'sys_role_permission_gain_confirm;权限配置-配置到账日期管理员',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,30,'com_enterprise_edit;组织信息管理',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,31,'hr_org_set,hr_employee;组织架构管理',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,40,'project_delete;删除参与项目',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,41,'delete_project_others;删除未参与项目',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,50,'project_edit;编辑参与项目基本信息',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,51,'edit_contract;查看参与项目合同信息',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,52,'view_contract_others;查看未参与项目合同信息',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,53,'list_project_others;查看未参与项目',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,60,'project_manager;编辑和发布参与项目的签发任务',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,70,'design_manager;安排自己负责的生产任务',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,71,'design_others;安排非自己负责的生产任务',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,80,'view_document;查看参与项目文档',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,81,'view_document_others;查看未参与项目文档',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,90,'admin_notice;发布通知公告',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,100,'data_import;历史数据导入',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,110,'finance_report;查看财务报表',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,111,'report_exp_static,project_view_amount;查看费用统计报表',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,112,'summary_leave;查看工时统计报表',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,120,'sys_finance_type;财务设置',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,121,'finance_fixed_edit;费用录入',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,130,'project_charge_manage;确认付款日期',null);
    REPLACE INTO md_const (classic_id,code_id,title,extra) VALUES (1,131,'finance_back_fee;确认到账日期',null);


  END;
call initConst();

-- 建立创建视图的存储过程
DROP PROCEDURE IF EXISTS updateViews;
CREATE PROCEDURE updateViews()
BEGIN
  -- -- 专业类型定义视图
  CREATE OR REPLACE VIEW `md_type_web_major` AS
    select 
      major_type.code_id as type_id,
      major_type.title as type_name
    from
      md_const major_type
    where (major_type.classic_id = 22);
      
  -- -- 服务器类型定义视图
  CREATE OR REPLACE VIEW `md_type_server` AS
    select 
      server_type.code_id as type_id,
      server_type.title as type_name,
      replace(substring(server_type.extra,
                1,char_length(substring_index(server_type.extra,';',1))),'|',';')
        as default_server_address,
      substring(server_type.extra,
                char_length(substring_index(server_type.extra,';',1))+2,
                char_length(substring_index(server_type.extra,';',2)) - char_length(substring_index(server_type.extra,';',1))-1)
        as default_base_dir
    from
      md_const server_type
    where (server_type.classic_id = 19 and server_type.code_id != 0);
      
  -- -- 文件操作定义视图
  CREATE OR REPLACE VIEW `md_type_commit` AS
    select
      commit_type.code_id as type_id,
      commit_type.title as type_name,
      substring(commit_type.extra,
                1,char_length(substring_index(commit_type.extra,';',1)))
          as node_type,
      substring(commit_type.extra,
                char_length(substring_index(commit_type.extra,';',1))+2,
                char_length(substring_index(commit_type.extra,';',2)) - char_length(substring_index(commit_type.extra,';',1))-1)
          as node_path,
      substring(commit_type.extra,
                char_length(substring_index(commit_type.extra,';',2))+2,
                char_length(substring_index(commit_type.extra,';',3)) - char_length(substring_index(commit_type.extra,';',2))-1)
          as server_type,
      substring(commit_type.extra,
                char_length(substring_index(commit_type.extra,';',3))+2,
                char_length(substring_index(commit_type.extra,';',4)) - char_length(substring_index(commit_type.extra,';',3))-1)
          as server_address,
      substring(commit_type.extra,
                char_length(substring_index(commit_type.extra,';',4))+2,
                char_length(substring_index(commit_type.extra,';',5)) - char_length(substring_index(commit_type.extra,';',4))-1)
          as base_dir,
      substring(commit_type.extra,
                char_length(substring_index(commit_type.extra,';',5))+2,
                char_length(substring_index(commit_type.extra,';',6)) - char_length(substring_index(commit_type.extra,';',5))-1)
          as notice_type
    from
      md_const commit_type
    where
      (commit_type.classic_id = 20) and (commit_type.code_id != 0);

  -- -- web公司角色类型定义视图
  CREATE OR REPLACE VIEW `md_type_web_role_company` AS
    select
      web_role_type.code_id as type_id,
      web_role_type.title as type_name,
      substring(web_role_type.extra,
                1,
                char_length(substring_index(web_role_type.extra,';',1)))
        as attr_str,
      substring(web_role_type.extra,1,1) as is_plus_offset,
      substring(web_role_type.extra,
                char_length(substring_index(web_role_type.extra,';',1))+2,
                char_length(substring_index(web_role_type.extra,';',2)) - char_length(substring_index(web_role_type.extra,';',1))-1)
        as code_id,
      substring(web_role_type.extra,
                char_length(substring_index(web_role_type.extra,';',2))+2,
                char_length(substring_index(web_role_type.extra,';',3)) - char_length(substring_index(web_role_type.extra,';',2))-1)
        as role_type,
      substring(web_role_type.extra,
                char_length(substring_index(web_role_type.extra,';',3))+2,
                char_length(substring_index(web_role_type.extra,';',4)) - char_length(substring_index(web_role_type.extra,';',3))-1)
        as need_permission
    from
      md_const web_role_type
    where
      web_role_type.classic_id = 29;

  -- -- web项目角色类型定义视图
  CREATE OR REPLACE VIEW `md_type_web_role_project` AS
    select
      web_role_type.code_id as type_id,
      web_role_type.title as type_name,
      substring(web_role_type.extra,
                1,
                char_length(substring_index(web_role_type.extra,';',1)))
        as attr_str,
      substring(web_role_type.extra,1,1) as is_project_role,
      substring(web_role_type.extra,2,1) as is_task_role,
      substring(web_role_type.extra,3,1) as is_task_leader,
      substring(web_role_type.extra,4,1) as is_task_designer,
      substring(web_role_type.extra,5,1) as is_task_checker,
      substring(web_role_type.extra,6,1) as is_task_auditor,
      substring(web_role_type.extra,
                char_length(substring_index(web_role_type.extra,';',1))+2,
                char_length(substring_index(web_role_type.extra,';',2)) - char_length(substring_index(web_role_type.extra,';',1))-1)
        as role_type,
      substring(web_role_type.extra,
                char_length(substring_index(web_role_type.extra,';',2))+2,
                char_length(substring_index(web_role_type.extra,';',3)) - char_length(substring_index(web_role_type.extra,';',2))-1)
        as mytask_task_type,
      substring(web_role_type.extra,
                char_length(substring_index(web_role_type.extra,';',3))+2,
                char_length(substring_index(web_role_type.extra,';',4)) - char_length(substring_index(web_role_type.extra,';',3))-1)
        as process_name
    from
      md_const web_role_type
    where
      web_role_type.classic_id = 30;

  -- -- skydrive上文档分类定义视图
  CREATE OR REPLACE VIEW `md_type_sky_drive` AS
    select
      sky_drive_type.code_id as type_id,
      sky_drive_type.title as type_name,
      substring(sky_drive_type.extra,
                1,
                char_length(substring_index(sky_drive_type.extra,';',1)))
        as dir_node_type,
      substring(sky_drive_type.extra,
                char_length(substring_index(sky_drive_type.extra,';',1))+2,
                char_length(substring_index(sky_drive_type.extra,';',2)) - char_length(substring_index(sky_drive_type.extra,';',1))-1)
        as file_node_type
    from
      md_const sky_drive_type
    where
      sky_drive_type.classic_id = 21;

  -- -- 文档分类定义视图
  CREATE OR REPLACE VIEW `md_type_range` AS
    select
      range_type.code_id as type_id,
      range_type.title as type_name,
      substring(range_type.extra,
                1,
                char_length(substring_index(range_type.extra,';',1)))
                         as attr_str,
      substring(range_type.extra,1,1) as is_root,
      substring(range_type.extra,2,1) as is_show_all_task,
      substring(range_type.extra,
                char_length(substring_index(range_type.extra,';',1))+2,
                char_length(substring_index(range_type.extra,';',2)) - char_length(substring_index(range_type.extra,';',1))-1)
                         as node_type,
      substring(range_type.extra,
                char_length(substring_index(range_type.extra,';',2))+2,
                char_length(substring_index(range_type.extra,';',3)) - char_length(substring_index(range_type.extra,';',2))-1)
                         as sub_node_type
    from
      md_const range_type
    where
      range_type.classic_id = 24;

  -- -- 节点类型定义视图
  CREATE OR REPLACE VIEW `md_type_node` AS
    select
      node_type.code_id as type_id,
      node_type.title as type_name,
      substring(node_type.extra,
                1,
                char_length(substring_index(node_type.extra,';',1)))
                         as attr_str,
      substring(node_type.extra,1,1) as is_directory,
      substring(node_type.extra,2,1) as is_project,
      substring(node_type.extra,3,1) as is_task,
      substring(node_type.extra,4,1) as is_design,
      substring(node_type.extra,5,1) as is_commit,
      substring(node_type.extra,6,1) as is_history,
      substring(node_type.extra,
                char_length(substring_index(node_type.extra,';',1))+2,
                char_length(substring_index(node_type.extra,';',2)) - char_length(substring_index(node_type.extra,';',1))-1)
                         as sub_dir_type,
      substring(node_type.extra,
                char_length(substring_index(node_type.extra,';',2))+2,
                char_length(substring_index(node_type.extra,';',3)) - char_length(substring_index(node_type.extra,';',2))-1)
                         as sub_file_type,
      substring(node_type.extra,
                char_length(substring_index(node_type.extra,';',3))+2,
                char_length(substring_index(node_type.extra,';',4)) - char_length(substring_index(node_type.extra,';',3))-1)
                         as owner_role_type
    from
      md_const node_type
    where
      node_type.classic_id = 14;

  -- -- task任务类型定义视图
  CREATE OR REPLACE VIEW `md_type_web_task` AS
    select
      web_task_type.code_id as type_id,
      web_task_type.title as type_name,
      substring(web_task_type.extra,
                1,
                char_length(substring_index(web_task_type.extra,';',1)))
                         as attr_str,
      substring(web_task_type.extra,1,1) as is_issue,
      substring(web_task_type.extra,2,1) as is_task,
      substring(web_task_type.extra,
                char_length(substring_index(web_task_type.extra,';',1))+2,
                char_length(substring_index(web_task_type.extra,';',2)) - char_length(substring_index(web_task_type.extra,';',1))-1)
                         as node_type_offset,
      substring(web_task_type.extra,
                char_length(substring_index(web_task_type.extra,';',2))+2,
                char_length(substring_index(web_task_type.extra,';',3)) - char_length(substring_index(web_task_type.extra,';',2))-1)
                         as in_range
    from
      md_const web_task_type
    where
      web_task_type.classic_id = 23;

  -- -- 权限定义视图
  CREATE OR REPLACE VIEW `md_type_permission` AS
    select
      permission_type.code_id as type_id,
      substring(permission_type.title,locate(';',permission_type.title) + 1) AS type_name,
      substring(permission_type.title,1,locate(';',permission_type.title) - 1) AS code_id
    from
      md_const permission_type
    where
      permission_type.classic_id = 1;

  -- -- 角色定义视图
  CREATE OR REPLACE VIEW `md_type_role` AS
    select
      role_type.code_id as type_id,
      substring(role_type.title,locate(';',role_type.title) + 1) as type_name,
      substring(role_type.title,1,locate(';',role_type.title) - 1) as code_id,
      substring(role_type.extra,
                1,
                char_length(substring_index(role_type.extra,';',1)))
        as manage_role,
      substring(role_type.extra,
                char_length(substring_index(role_type.extra,';',1))+2,
                char_length(substring_index(role_type.extra,';',2)) - char_length(substring_index(role_type.extra,';',1))-1)
        as have_permission
    from
      md_const role_type
    where
      role_type.classic_id = 26;

  -- -- 角色及可管理角色
  CREATE OR REPLACE VIEW `md_role_and_sub_role` AS
    select
      role_type.*,
      sub_role_type.type_id as sub_role_type_id,
      sub_role_type.type_name as sub_role_type_name,
      sub_role_type.code_id as sub_role_code_id,
      sub_role_type.have_permission as sub_role_have_permission
    from
      md_type_role role_type
      left join md_type_role sub_role_type on (find_in_set(sub_role_type.type_id,role_type.manage_role));

  -- -- 角色及权限
  CREATE OR REPLACE VIEW `md_role_permission` AS
    select
      concat(role_type.type_id,'-',permission_type.type_id) as type_id,
      role_type.type_id as role_type_id,
      role_type.type_name as role_type_name,
      role_type.code_id as role_code_id,
      permission_type.type_id as permission_type_id,
      permission_type.type_name as permission_type_name,
      permission_type.code_id as permission_code_id,
      find_in_set(permission_type.type_id,role_type.have_permission)>0 as is_direct_permission,
      group_concat(if(find_in_set(permission_type.type_id,sub_role_type.have_permission),concat(sub_role_type.type_id,','),'') separator '') as have_permission_sub_role_type,
      group_concat(if(find_in_set(permission_type.type_id,sub_role_type.have_permission),concat(sub_role_type.code_id,','),'') separator '') as have_permission_sub_role_code,
      group_concat(if(find_in_set(permission_type.type_id,sub_role_type.have_permission),concat(sub_role_type.type_name,','),'') separator '') as have_permission_sub_role_title
    from
      md_type_role role_type
      left join md_type_role sub_role_type on (find_in_set(sub_role_type.type_id,role_type.manage_role))
      inner join md_type_permission permission_type on (find_in_set(permission_type.type_id,role_type.have_permission)
                                                        or find_in_set(permission_type.type_id,sub_role_type.have_permission))
    group by role_type.type_id,permission_type.type_id;

  -- -- 任务表
  CREATE OR REPLACE VIEW `md_web_task` AS
      select
          task.id,
          task.task_name,
          task.task_type as type_id,
          task.task_pid as pid,
          concat(if(task_parent6.task_name is null,'',concat(task_parent6.task_name,'/')),
              if(task_parent5.task_name is null,'',concat(task_parent5.task_name,'/')),
              if(task_parent4.task_name is null,'',concat(task_parent4.task_name,'/')),
              if(task_parent3.task_name is null,'',concat(task_parent3.task_name,'/')),
              if(task_parent2.task_name is null,'',concat(task_parent2.task_name,'/')),
              if(task_parent1.task_name is null,'',concat(task_parent1.task_name,'/')),
              task.task_name) as path,
          if(task.task_type in (1,2),task.id,
              if(task_parent1.task_type in (1,2),task_parent1.id,
              if(task_parent2.task_type in (1,2),task_parent2.id,
              if(task_parent3.task_type in (1,2),task_parent3.id,
              if(task_parent4.task_type in (1,2), task_parent4.id,
              if(task_parent5.task_type in (1,2), task_parent5.id,
              if(task_parent6.task_type in (1,2), task_parent6.id,
                 null))))))) as issue_id,
          if(task.task_type in (1,2),task.task_name,
              if(task_parent1.task_type in (1,2),task_parent1.task_name,
              if(task_parent2.task_type in (1,2),task_parent2.task_name,
              if(task_parent3.task_type in (1,2),task_parent3.task_name,
              if(task_parent4.task_type in (1,2), task_parent4.task_name,
              if(task_parent5.task_type in (1,2), task_parent5.task_name,
              if(task_parent6.task_type in (1,2), task_parent6.task_name,
                 null))))))) as issue_name,
        concat(if(task_parent6.task_name is null or task_parent6.task_type not in (1,2),'',task_parent6.task_name),
               if(task_parent5.task_name is null or task_parent6.task_type not in (1,2),'',concat(if(task_parent6.task_name is null,'','/'),task_parent5.task_name)),
               if(task_parent4.task_name is null or task_parent6.task_type not in (1,2),'',concat(if(task_parent5.task_name is null,'','/'),task_parent4.task_name)),
               if(task_parent3.task_name is null or task_parent6.task_type not in (1,2),'',concat(if(task_parent4.task_name is null,'','/'),task_parent3.task_name)),
               if(task_parent2.task_name is null or task_parent6.task_type not in (1,2),'',concat(if(task_parent3.task_name is null,'','/'),task_parent2.task_name)),
               if(task_parent1.task_name is null or task_parent6.task_type not in (1,2),'',concat(if(task_parent2.task_name is null,'','/'),task_parent1.task_name)),
               if(task.task_type not in (1,2),'',concat(if(task_parent1.task_name is null,'','/'),task.task_name))) as issue_path,
        concat(if(task_parent6.task_name is null or task_parent6.task_type not in (0),'',task_parent6.task_name),
               if(task_parent5.task_name is null or task_parent6.task_type not in (0),'',concat(if(task_parent6.task_name is null,'','/'),task_parent5.task_name)),
               if(task_parent4.task_name is null or task_parent6.task_type not in (0),'',concat(if(task_parent5.task_name is null,'','/'),task_parent4.task_name)),
               if(task_parent3.task_name is null or task_parent6.task_type not in (0),'',concat(if(task_parent4.task_name is null,'','/'),task_parent3.task_name)),
               if(task_parent2.task_name is null or task_parent6.task_type not in (0),'',concat(if(task_parent3.task_name is null,'','/'),task_parent2.task_name)),
               if(task_parent1.task_name is null or task_parent6.task_type not in (0),'',concat(if(task_parent2.task_name is null,'','/'),task_parent1.task_name)),
               if(task.task_type not in (0),'',concat(if(task_parent1.task_name is null,'','/'),task.task_name))) as task_path,
          task.project_id,
          task.company_id,
          task.create_date,
          task.create_by,
          task.update_date,
          task.update_by
      from
          maoding_web_project_task task
          left join maoding_web_project_task task_parent1 ON (task_parent1.id = task.task_pid)
          left join maoding_web_project_task task_parent2 ON (task_parent2.id = task_parent1.task_pid)
          left join maoding_web_project_task task_parent3 ON (task_parent3.id = task_parent2.task_pid)
          left join maoding_web_project_task task_parent4 ON (task_parent4.id = task_parent3.task_pid)
          left join maoding_web_project_task task_parent5 ON (task_parent5.id = task_parent4.task_pid)
          left join maoding_web_project_task task_parent6 ON (task_parent6.id = task_parent5.task_pid)
      where
          (task.task_status = '0')
          and (task.task_type in (0,1,2))
      group by task.id;

  -- -- 文件节点视图与项目相关的部分
  CREATE OR REPLACE VIEW `md_node_project` AS
    select
      project.id,
      null AS pid,
      if(project.id is null,null,11) as type_id,
      project.project_name as name,
      concat('/',project.project_name) as path,
      unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp,
      date_format(project.create_date,'%Y-%m-%d %T') as create_time_text,
      unix_timestamp(if(project.update_date is null,ifnull(project.create_date,0),project.update_date)) as last_modify_time_stamp,
      date_format(ifnull(project.update_date,project.create_date),'%Y-%m-%d %T') as last_modify_time_text,
      ifnull(project.update_by,project.create_by) as owner_user_id,
      project.update_by as last_modify_user_id,
      null as last_modify_role_id,
      if(project.id is null,null,0) as read_only_file_length,
      if(project.id is null,null,0) as writable_file_length,
      null as read_only_file_md5,
      null as writable_file_md5,
      null as range_id,
      node_type.is_directory,
      node_type.is_project,
      node_type.is_task,
      node_type.is_design,
      node_type.is_commit,
      node_type.is_history,
      null as issue_id,
      null as task_id,
      project.id as project_id,
      project.company_id as company_id,
      project.id as root_id,
      node_type.owner_role_type as owner_role_id
    from
      maoding_web_project project
      inner join md_type_node node_type on (node_type.type_id = 11)
    where
      (project.pstatus = '0');

  -- -- 文件节点视图与分类相关的部分
  CREATE OR REPLACE VIEW `md_node_range` AS
    select
      concat(project.id,'-',range_type.type_id) as id,
      project.id AS pid,
      range_type.node_type as type_id,
      range_type.type_name as name,
      concat('/',ifnull(project.project_name,'未知项目'),
             '/',range_type.type_name) as path,
      unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp,
      date_format(project.create_date,'%Y-%m-%d %T') as create_time_text,
      unix_timestamp(if(project.update_date is null,ifnull(project.create_date,0),project.update_date)) as last_modify_time_stamp,
      date_format(ifnull(project.update_date,project.create_date),'%Y-%m-%d %T') as last_modify_time_text,
      ifnull(project.update_by,project.create_by) as owner_user_id,
      project.update_by as last_modify_user_id,
      null as last_modify_role_id,
      if(range_type.type_id is null,null,0) as read_only_file_length,
      if(range_type.type_id is null,null,0) as writable_file_length,
      null as read_only_file_md5,
      null as writable_file_md5,
      range_type.type_id as range_id,
      node_type.is_directory,
      node_type.is_project,
      node_type.is_task,
      node_type.is_design,
      node_type.is_commit,
      node_type.is_history,
      null as issue_id,
      null as task_id,
      project.id as project_id,
      project.company_id as company_id,
      project.id as root_id,
      node_type.owner_role_type as owner_role_id
    from
      md_type_range range_type
      inner join maoding_web_project project on (project.pstatus = '0')
      inner join md_type_node node_type on (node_type.type_id = range_type.node_type)
    where
      (range_type.type_id != 0);

  -- -- 文件节点视图与任务相关的部分
  CREATE OR REPLACE VIEW `md_node_task` AS
    select
      concat(task.id,'-',range_type.type_id) as id,
      concat(ifnull(task.pid,task.project_id),'-',range_type.type_id) AS pid,
      (range_type.node_type + web_task_type.node_type_offset) as type_id,
      task.task_name as name,
      concat('/',ifnull(project.project_name,'未知项目'),
              '/',ifnull(range_type.type_name,'未知类别'),
              '/',task.path) as path,
      unix_timestamp(ifnull(task.create_date,0)) as create_time_stamp,
      date_format(task.create_date,'%Y-%m-%d %T') as create_time_text,
      unix_timestamp(if(task.update_date is null,ifnull(task.create_date,0),task.update_date)) as last_modify_time_stamp,
      date_format(ifnull(task.update_date,task.create_date),'%Y-%m-%d %T') as last_modify_time_text,
      ifnull(task.update_by,task.create_by) as owner_user_id,
      task.update_by as last_modify_user_id,
      null as last_modify_role_id,
      if(task.id is null,null,0) as read_only_file_length,
      if(task.id is null,null,0) as writable_file_length,
      null as read_only_file_md5,
      null as writable_file_md5,
      range_type.type_id as range_id,
      node_type.is_directory,
      node_type.is_project,
      node_type.is_task,
      node_type.is_design,
      node_type.is_commit,
      node_type.is_history,
      task.issue_id,
      task.id as task_id,
      task.project_id,
      task.company_id,
      task.project_id as root_id,
      node_type.owner_role_type as owner_role_id
    from
      md_web_task task
      inner join maoding_web_project project on (task.project_id = project.id)
      inner join md_type_web_task web_task_type on (task.type_id = web_task_type.type_id)
      inner join md_type_range range_type on (find_in_set(range_type.type_id,web_task_type.in_range))
      inner join md_type_node node_type on (node_type.type_id = (range_type.node_type + web_task_type.node_type_offset));

  -- -- 文件节点视图与文件树相关部分
  CREATE OR REPLACE VIEW `md_node_storage` AS
    select
      concat(storage_tree.id,'-',range_type.type_id) as id,
      concat(if(storage_tree.pid is null,
                if(storage_tree.task_id is null,null,concat(storage_tree.task_id,'-',range_type.type_id)),
                concat(storage_tree.pid,'-',range_type.type_id))) as pid,
      storage_tree.type_id,
      storage_tree.node_name as name,
      if(storage_tree.task_id is null,
         concat(storage_tree.path),
         concat('/',ifnull(project.project_name,'未知项目'),
              '/',ifnull(range_type.type_name,'未知类别'),
              '/',ifnull(task.path,'未知任务'),
              '/',storage_tree.path)) as path,
      unix_timestamp(ifnull(storage_tree.create_time,0)) as create_time_stamp,
      date_format(storage_tree.create_time,'%Y-%m-%d %T') as create_time_text,
      unix_timestamp(ifnull(storage_tree.last_modify_time,0)) as last_modify_time_stamp,
      date_format(storage_tree.last_modify_time,'%Y-%m-%d %T') as last_modify_time_text,
      storage_tree.owner_user_id,
      storage_tree.last_modify_user_id,
      storage_tree.last_modify_role_id,
      ifnull(file_list.read_only_file_length,0) as read_only_file_length,
      ifnull(file_list.writable_file_length,0) as writable_file_length,
      file_list.read_only_file_md5,
      file_list.writable_file_md5,
      range_type.type_id as range_id,
      node_type.is_directory,
      node_type.is_project,
      node_type.is_task,
      node_type.is_design,
      node_type.is_commit,
      node_type.is_history,
      task.issue_id,
      task.id as task_id,
      task.project_id,
      task.company_id,
      task.project_id as root_id,
      node_type.owner_role_type as owner_role_id
    from
      md_tree_storage storage_tree
      inner join md_type_node node_type on (storage_tree.type_id = node_type.type_id)
      inner join md_type_range range_type on (find_in_set(node_type.type_id,range_type.sub_node_type))
      left join md_list_storage_file file_list on (storage_tree.id = file_list.id)
      left join md_web_task task on (storage_tree.task_id = task.id)
      left join maoding_web_project project on (task.project_id = project.id)
    where
      (storage_tree.deleted = 0);

  -- -- 网站节点视图
  CREATE OR REPLACE VIEW `md_node_sky_drive` AS
    select
      old_node.id,
      ifnull(old_node.pid,old_node.project_id) AS pid,
      if(old_node.file_size > 0,old_node_type.file_node_type,
         if(position('归档' in old_node.file_name)>0,42,old_node_type.dir_node_type)) as type_id,
      old_node.file_name as name,
      concat('/',project.project_name,'/',
        if(old_node_parent6.file_name is null,'',concat(old_node_parent6.file_name,'/')),
        if(old_node_parent5.file_name is null,'',concat(old_node_parent5.file_name,'/')),
        if(old_node_parent4.file_name is null,'',concat(old_node_parent4.file_name,'/')),
        if(old_node_parent3.file_name is null,'',concat(old_node_parent3.file_name,'/')),
        if(old_node_parent2.file_name is null,'',concat(old_node_parent2.file_name,'/')),
        if(old_node_parent1.file_name is null,'',concat(old_node_parent1.file_name,'/')),
        old_node.file_name) as path,
      unix_timestamp(ifnull(old_node.create_date,0)) as create_time_stamp,
      date_format(old_node.create_date,'%Y-%m-%d %T') as create_time_text,
      unix_timestamp(ifnull(old_node.update_date,ifnull(old_node.create_date,0))) as last_modify_time_stamp,
      date_format(ifnull(old_node.update_date,old_node.create_date),'%Y-%m-%d %T') as last_modify_time_text,
      ifnull(old_node.update_by,old_node.create_by) as owner_user_id,
      old_node.update_by as last_modify_user_id,
      null as last_modify_role_id,
      old_node.file_size as file_length,
      null as range_id,
      node_type.is_directory,
      node_type.is_project,
      node_type.is_task,
      node_type.is_design,
      node_type.is_commit,
      node_type.is_history,
      task.issue_id,
      old_node.task_id,
      old_node.project_id,
      old_node.company_id,
      old_node.project_id as root_id,
      node_type.owner_role_type as owner_role_id
    from
      maoding_web_project_sky_drive old_node
      inner join md_type_sky_drive old_node_type on (old_node.type = old_node_type.type_id)
      inner join md_type_node node_type on (node_type.type_id = if(old_node.file_size > 0,old_node_type.file_node_type,
                                                                   if(position('归档' in old_node.file_name) > 0,42,
                                                                      old_node_type.dir_node_type)))
      left join md_web_task task on (old_node.task_id = task.id)
      left join maoding_web_project project on (old_node.project_id = project.id)
      left join maoding_web_project_sky_drive old_node_parent1 ON (old_node_parent1.id = old_node.pid)
      left join maoding_web_project_sky_drive old_node_parent2 ON (old_node_parent2.id = old_node_parent1.pid)
      left join maoding_web_project_sky_drive old_node_parent3 ON (old_node_parent3.id = old_node_parent2.pid)
      left join maoding_web_project_sky_drive old_node_parent4 ON (old_node_parent4.id = old_node_parent3.pid)
      left join maoding_web_project_sky_drive old_node_parent5 ON (old_node_parent5.id = old_node_parent4.pid)
      left join maoding_web_project_sky_drive old_node_parent6 ON (old_node_parent6.id = old_node_parent5.pid)
    where
      (old_node.status in (0,1)) and char_length(old_node.file_name) > 0;

  -- -- 新的文件树节点通用视图
  CREATE OR REPLACE VIEW `md_node` AS
    select * from md_node_project
    union all
    select * from md_node_range
    union all
    select * from md_node_task
    union all
    select * from md_node_storage;

  -- -- 文件及附件视图
  CREATE OR REPLACE VIEW `md_file` AS
  select
    file.*,
    mirror.server_type_id AS mirror_server_type_id,
    mirror.server_address AS mirror_server_address,
    mirror.base_dir AS mirror_base_dir,
    mirror.read_only_key AS read_only_mirror_key,
    mirror.writable_key AS writable_mirror_key
  from
    md_list_storage_file file
    left join md_list_storage_file mirror on (mirror.deleted = 0 and mirror.main_file_id = file.id)
  where (file.deleted = 0);

  -- -- web项目角色视图
  CREATE OR REPLACE VIEW `md_web_role_project` AS
      select
          concat(web_role_type.type_id,'-',if(web_role_type.is_project_role,task_role.project_id,
                                              ifnull(task_role.node_id,task_role.target_id)),'-',task_role.account_id) as id,
          task_role.id as web_role_id,
          web_role_type.type_id,
          web_role_type.type_name,
          web_role_type.role_type,
          web_role_type.attr_str,
          web_role_type.is_project_role,
          web_role_type.is_task_role,
          web_role_type.is_task_leader,
          web_role_type.is_task_designer,
          web_role_type.is_task_checker,
          web_role_type.is_task_auditor,
          if(web_role_type.type_id is null,null,0) as is_company_role,
          if(project.status = '0',0,1) as is_complete,
          task_role.account_id as user_id,
          task_role.project_id as project_id,
          null as task_id,
          task_role.company_id as company_id,
          unix_timestamp(ifnull(task_role.create_date,0)) as create_time_stamp,
          date_format(task_role.create_date,'%y-%m-%d %T') as create_time_text,
          unix_timestamp(if(task_role.update_date is null,ifnull(task_role.create_date,0),task_role.update_date)) as last_modify_time_stamp,
          date_format(ifnull(task_role.update_date,task_role.create_date),'%y-%m-%d %T') as last_modify_time_text,
          account.user_name,
          project.project_name,
          null as task_name,
          company.company_name
      from
          maoding_web_project_member task_role
          inner join md_type_web_role_project web_role_type on (task_role.member_type = web_role_type.type_id and web_role_type.is_project_role = 1)
          inner join maoding_web_account account on (account.status = '0' and task_role.account_id = account.id)
          inner join maoding_web_project project on (project.pstatus = '0' and task_role.project_id = project.id)
          inner join maoding_web_company company on (company.status = '0' and task_role.company_id = company.id)
      where
          task_role.deleted = 0;

  -- -- web任务角色视图
  CREATE OR REPLACE VIEW `md_web_role_task` AS
      select
          concat(web_role_type.type_id,'-',if(web_role_type.is_project_role,task_role.project_id,
                                              ifnull(task_role.node_id,task_role.target_id)),'-',task_role.account_id) as id,
          task_role.id as web_role_id,
          web_role_type.type_id,
          web_role_type.type_name,
          web_role_type.role_type,
          web_role_type.attr_str,
          web_role_type.is_project_role,
          web_role_type.is_task_role,
          web_role_type.is_task_leader,
          web_role_type.is_task_designer,
          web_role_type.is_task_checker,
          web_role_type.is_task_auditor,
          if(web_role_type.type_id is null,null,0) as is_company_role,
          if(web_role_type.is_task_leader,
             task.complete_date is not null or mytask.complete_date is not null,
             mytask.complete_date is not null) as is_complete,
          task_role.account_id as user_id,
          task_role.project_id as project_id,
          ifnull(task_role.node_id,task_role.target_id) as task_id,
          task_role.company_id as company_id,
          unix_timestamp(ifnull(task_role.create_date,0)) as create_time_stamp,
          date_format(task_role.create_date,'%y-%m-%d %T') as create_time_text,
          unix_timestamp(if(task_role.update_date is null,ifnull(task_role.create_date,0),task_role.update_date)) as last_modify_time_stamp,
          date_format(ifnull(task_role.update_date,task_role.create_date),'%y-%m-%d %T') as last_modify_time_text,
          account.user_name,
          project.project_name,
          task.task_name,
          company.company_name
      from
          maoding_web_project_member task_role
          inner join md_type_web_role_project web_role_type on (task_role.member_type = web_role_type.type_id and web_role_type.is_task_role = 1)
          inner join maoding_web_account account on (account.status = '0' and task_role.account_id = account.id)
          inner join maoding_web_project project on (project.pstatus = '0' and task_role.project_id = project.id)
          inner join maoding_web_project_task task on (task.task_status = '0' and (task_role.node_id = task.id or task_role.target_id = task.id))
          inner join maoding_web_company company on (company.status = '0' and task_role.company_id = company.id)
          left join maoding_web_project_process_node process on (process.status = '0'
                                                                  and process.process_id = task.id
                                                                  and process.company_user_id = task_role.company_user_id
                                                                  and process.node_name = web_role_type.process_name)
          left join maoding_web_my_task mytask on (param4 = '0'
                                                      and mytask.target_id = process.id)
      where
          task_role.deleted = 0;

  -- -- web公司角色视图
  CREATE OR REPLACE VIEW `md_web_role_company` AS
    select
      concat(web_role_type.type_id,'-',company_role.company_id,'-',company_role.user_id) as id,
      company_role.id as web_role_id,
      web_role_type.type_id,
      web_role_type.type_name,
      (web_role_type.role_type +
       if(web_role_type.is_plus_offset and org_relation.type_id is not null,
          org_relation.type_id,0)) as role_type,
      if(web_role_type.type_id is null,null,'00') as attr_str,
      if(web_role_type.type_id is null,null,0) as is_project_role,
      if(web_role_type.type_id is null,null,0) as is_task_role,
      if(web_role_type.type_id is null,null,0) as is_task_leader,
      if(web_role_type.type_id is null,null,0) as is_task_designer,
      if(web_role_type.type_id is null,null,0) as is_task_checker,
      if(web_role_type.type_id is null,null,0) as is_task_auditor,
      if(web_role_type.type_id is null,null,1) as is_company_role,
      if(web_role_type.type_id is null,null,0) as is_complete,
      company_role.user_id,
      null as project_id,
      null as task_id,
      company_role.company_id as company_id,
      unix_timestamp(ifnull(company_role.create_date,0)) as create_time_stamp,
      date_format(company_role.create_date,'%y-%m-%d %T') as create_time_text,
      unix_timestamp(if(company_role.update_date is null,ifnull(company_role.create_date,0),company_role.update_date)) as last_modify_time_stamp,
      date_format(ifnull(company_role.update_date,company_role.create_date),'%y-%m-%d %T') as last_modify_time_text,
      account.user_name,
      null as project_name,
      null as task_name,
      company.company_name
    from
      maoding_web_user_permission company_role
      left join maoding_web_company_relation org_relation on (company_role.company_id = org_relation.org_id)
      inner join md_type_web_role_company web_role_type on (company_role.permission_id = web_role_type.type_id)
      inner join md_type_role role_type on (role_type.type_id = (web_role_type.role_type +
                                                                 if(web_role_type.is_plus_offset and
                                                                    org_relation.type_id is not null,
                                                                    org_relation.type_id,0)))
      inner join maoding_web_account account on (company_role.user_id = account.id)
      inner join maoding_web_company company on (company_role.company_id = company.id);

  -- -- web角色视图
  CREATE OR REPLACE VIEW `md_web_role` AS
    select * from md_web_role_project
    union all
    select * from md_web_role_company;

  -- -- web公司权限设置视图
  CREATE OR REPLACE VIEW `md_web_permission_setting` AS
    select
      web_permission_group.code_id as group_id,
      web_permission_group.title as group_name,
      web_permission.type_id as permission_id,
      web_permission.type_name as permission_name,
      web_permission.is_plus_offset,
      web_permission.role_type,
      web_permission.need_permission
    from
      md_const web_permission_group
      inner join md_type_web_role_company web_permission on (find_in_set(web_permission.type_id,web_permission_group.extra))
    where web_permission_group.classic_id = 28
    order by web_permission_group.code_id,find_in_set(web_permission.type_id,web_permission_group.extra);
END;
call updateViews();

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
-- call createIndex();

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




