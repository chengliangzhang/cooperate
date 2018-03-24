-- 建立更新表结构存储过程
DROP PROCEDURE IF EXISTS `updateTables`;
CREATE PROCEDURE `updateTables`()
BEGIN
  -- -- 校审意见
  CREATE TABLE IF NOT EXISTS `md_list_suggestion` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `name` varchar(255) DEFAULT NULL COMMENT '名称',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `type_id` varchar(40) DEFAULT NULL COMMENT '校审意见类别编号',
    `content` text(2048) DEFAULT NULL COMMENT '校审意见详细内容',
    `main_file_id` char(32) DEFAULT NULL COMMENT '校审文件编号',
    `status_type_id` varchar(40) DEFAULT NULL COMMENT '校审意见状态编号',
    `creator_user_id` char(32) DEFAULT NULL COMMENT '校审意见创建者用户编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_suggestion' and column_name='main_file_id') then
    alter table md_list_suggestion add column `main_file_id` char(32) DEFAULT NULL COMMENT '校审文件编号';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_suggestion' and column_name='main_file_id' and data_type='char') then
    alter table md_list_suggestion modify column `main_file_id` char(32) DEFAULT NULL COMMENT '校审文件编号';
  end if;

  -- -- 批注评论
  CREATE TABLE IF NOT EXISTS `md_list_annotate` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `name` varchar(255) DEFAULT NULL COMMENT '名称',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `content` text(2048) DEFAULT NULL COMMENT '批注评论正文',
    `relatedId` char(32) DEFAULT NULL COMMENT '校审意见编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_annotate' and column_name='name') then
    alter table md_list_annotate add column `name` varchar(255) DEFAULT NULL COMMENT '名称';
  end if;

  -- -- 内嵌HTML元素
  CREATE TABLE IF NOT EXISTS `md_list_element` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `name` varchar(255) DEFAULT NULL COMMENT '名称',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',
    
    `data_array` longblob DEFAULT NULL COMMENT '元素内容',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_element' and column_name='name') then
    alter table md_list_element add column `name` varchar(255) DEFAULT NULL COMMENT '名称';
  end if;

	-- -- 常量定义
	CREATE TABLE IF NOT EXISTS `md_list_const` (
		`classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
		`code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号',
		`title` varchar(255) NOT NULL COMMENT '常量的可显示定义',
		`extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义',
		PRIMARY KEY (`classic_id`,`code_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const' and column_name='code_id') then
    alter table md_list_const add column `code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const' and column_name='title') then
    alter table md_list_const add column `title` varchar(255) NOT NULL COMMENT '常量的可显示定义';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const' and column_name='extra') then
    alter table md_list_const add column `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义';
  end if;

	-- -- 自定义常量定义
	CREATE TABLE IF NOT EXISTS `md_list_custom_const` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `name` varchar(255) DEFAULT NULL COMMENT '名称',
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

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_custom_const' and column_name='name') then
    alter table md_list_custom_const add column `name` varchar(255) DEFAULT NULL COMMENT '名称';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_custom_const' and column_name='code_id') then
    alter table md_list_custom_const add column `code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_custom_const' and column_name='title') then
    alter table md_list_custom_const add column `title` varchar(255) NOT NULL COMMENT '常量的可显示定义';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_custom_const' and column_name='extra') then
    alter table md_list_custom_const add column `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义';
  end if;

	-- -- 协同节点定义
	CREATE TABLE IF NOT EXISTS `md_tree_storage` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
    `name` varchar(255) DEFAULT NULL COMMENT '名称',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',
    
		`pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
		`path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
		`type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',
    
		`node_length` bigint(16) unsigned DEFAULT '0' COMMENT '节点长度，如果节点是目录则固定为0',
		`task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id',
    `owner_user_id` char(32) DEFAULT NULL COMMENT '节点所属用户id',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='name') then
    alter table md_tree_storage add column `name` varchar(255) DEFAULT NULL COMMENT '名称';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='node_length') then
    alter table md_tree_storage add column `node_length` bigint(16) unsigned DEFAULT '0' COMMENT '节点长度，如果节点是目录则固定为0';
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
	CREATE TABLE IF NOT EXISTS `md_tree_storage_file` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
    `name` varchar(255) DEFAULT NULL COMMENT '名称',
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
		`file_checksum` varchar(64) DEFAULT NULL COMMENT '文件校验和',
		`major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id',
		`main_file_id` char(32) DEFAULT NULL COMMENT '所对应的原始文件id',
		`read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称',
		`writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='name') then
    alter table md_tree_storage_file add column `name` varchar(255) DEFAULT NULL COMMENT '名称';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='file_type_id') then
    alter table md_tree_storage_file add column `file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='file_type_id' and data_type='varchar') then
    alter table md_tree_storage_file modify column `file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='server_type_id') then
    alter table md_tree_storage_file add column `server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='server_type_id' and data_type='varchar') then
    alter table md_tree_storage_file modify column `server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='last_modify_role_id') then
    alter table md_tree_storage_file add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='last_modify_role_id' and character_maximum_length>32) then
    alter table md_tree_storage_file modify column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='major_type_id') then
    alter table md_tree_storage_file add column `major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='major_type_id' and data_type='varchar') then
    alter table md_tree_storage_file modify column `major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='base_dir') then
    alter table md_tree_storage_file add column `base_dir` varchar(255) DEFAULT NULL COMMENT '文件在文件服务器上的存储位置';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='read_only_key') then
    alter table md_tree_storage_file add column `read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file' and column_name='writable_key') then
    alter table md_tree_storage_file add column `writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称';
  end if;


  -- -- 协同文件校审提资历史记录定义
	CREATE TABLE IF NOT EXISTS `md_tree_storage_file_his` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
    `name` varchar(255) DEFAULT NULL COMMENT '名称',
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

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file_his' and column_name='name') then
    alter table md_tree_storage_file_his add column `name` varchar(255) DEFAULT NULL COMMENT '名称';
  end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file_his' and column_name='remark') then
		alter table md_tree_storage_file_his add column `remark` text(2048) DEFAULT NULL COMMENT '文件注释';
	end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file_his' and column_name='last_modify_role_id') then
    alter table md_tree_storage_file_his add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage_file_his' and column_name='main_file_id') then
    alter table md_tree_storage_file_his add column `main_file_id` char(32) DEFAULT NULL COMMENT '协同文件编号id';
  end if;
END;

-- 建立创建视图的存储过程
DROP PROCEDURE IF EXISTS updateViews;
CREATE PROCEDURE updateViews()
BEGIN
  -- -- 任务表
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

  -- -- 文件根节点通用视图，带有user_id_list
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
      ,null as last_modify_role_id
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

  -- -- 文件树节点通用视图
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
      ,null as last_modify_role_id
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
      ,null as last_modify_role_id
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
      ,null as last_modify_role_id
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
       concat(storage_tree.id,classic_type.value_id) as id
      ,concat(storage_tree.pid,classic_type.value_id) as pid
      ,storage_tree.type_id
      ,storage_tree.node_name as name
      ,concat('/',task.project_name,'/',classic_type.content,'/',task.path,'/',storage_tree.path) as path
      ,unix_timestamp(ifnull(storage_tree.create_time,0)) as create_time_stamp
      ,date_format(storage_tree.create_time,'%Y-%m-%d %T') as create_time_text
      ,unix_timestamp(ifnull(storage_tree.last_modify_time,0)) as last_modify_time_stamp
      ,date_format(storage_tree.last_modify_time,'%Y-%m-%d %T') as last_modify_time_text
      ,storage_tree.last_modify_user_id as owner_user_id
      ,storage_tree.last_modify_role_id as last_modify_role_id
      ,storage_tree.file_length
      ,storage_tree.path as storage_path
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
      maoding_storage_tree storage_tree
      inner join maoding_const classic_type on (classic_type.classic_id = 24 and position(concat(':',storage_tree.type_id,':') in (classic_type.content_extra)) > 0)
      inner join maoding_const node_type on (node_type.classic_id = 14 and storage_tree.type_id = node_type.value_id)
      inner join maoding_task task on (storage_tree.task_id = task.id)
    where
      (storage_tree.deleted = 0)
    group by storage_tree.id;

  -- -- 新的文件根节点附加属性视图，带有user_id_list字段
  CREATE OR REPLACE VIEW `maoding_storage_root_copy` AS
    select
      project.id
      ,GROUP_CONCAT(distinct member.account_id) as user_id_list
    from
      maoding_web_project project
      inner join maoding_web_project_member member on (member.deleted = 0 and member.project_id= project.id)
    where
      (project.pstatus = '0')
    group by project.id ;

  -- -- 文件节点视图与项目相关的部分
  CREATE OR REPLACE VIEW `maoding_storage_project` AS
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
      ,project.update_by as last_modify_user_id
      ,null as last_modify_role_id
      ,if(project.id is null,null,0) as file_length
      ,null as range_id
      ,substring(node_type.content_extra,1,1) as is_directory
      ,substring(node_type.content_extra,2,1) as is_project
      ,substring(node_type.content_extra,3,1) as is_task
      ,substring(node_type.content_extra,4,1) as is_design
      ,substring(node_type.content_extra,5,1) as is_commit
      ,substring(node_type.content_extra,6,1) as is_history
      ,null as issue_id
      ,null as task_id
      ,project.id as project_id
      ,project.company_id as company_id
      ,project.id as root_id
    from
      maoding_web_project project
      inner join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id=if(project.id is null,null,11))
    where
      (project.pstatus = '0');

  -- -- 文件节点视图与分类相关的部分
  CREATE OR REPLACE VIEW `maoding_storage_range` AS
    select
       concat(project.id,'-',range_type.value_id) as id
      ,project.id AS pid
      ,if(position(';' in range_type.content_extra) > 1,substring(range_type.content_extra,1,position(';' in range_type.content_extra)-1),10) as type_id
      ,range_type.content as name
      ,concat('/',project.project_name,'/',range_type.content) as path
      ,unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp
      ,date_format(project.create_date,'%Y-%m-%d %T') as create_time_text
      ,unix_timestamp(if(project.update_date is null,ifnull(project.create_date,0),project.update_date)) as last_modify_time_stamp
      ,date_format(ifnull(project.update_date,project.create_date),'%Y-%m-%d %T') as last_modify_time_text
      ,ifnull(project.update_by,project.create_by) as owner_user_id
      ,project.update_by as last_modify_user_id
      ,null as last_modify_role_id
      ,if(range_type.value_id is null,null,0) as file_length
      ,range_type.value_id as range_id
      ,substring(node_type.content_extra,1,1) as is_directory
      ,substring(node_type.content_extra,2,1) as is_project
      ,substring(node_type.content_extra,3,1) as is_task
      ,substring(node_type.content_extra,4,1) as is_design
      ,substring(node_type.content_extra,5,1) as is_commit
      ,substring(node_type.content_extra,6,1) as is_history
      ,null as issue_id
      ,null as task_id
      ,project.id as project_id
      ,project.company_id as company_id
      ,project.id as root_id
    from
      maoding_const range_type
      inner join maoding_web_project project on (project.pstatus = '0')
      inner join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id = if(position(';' in range_type.content_extra) > 1,substring(range_type.content_extra,1,position(';' in range_type.content_extra)-1),10))
    where
      (range_type.classic_id = 24) and (range_type.value_id not in (0,3));

  -- -- 文件节点视图与任务相关的部分
  CREATE OR REPLACE VIEW `maoding_storage_task` AS
    select
       concat(task.id,'-',range_type.value_id) as id
      ,if(task.pid is null,concat(task.project_id,'-',range_type.value_id),concat(task.pid,'-',range_type.value_id)) AS pid
      ,if(position(';' in range_type.content_extra) > 1,substring(range_type.content_extra,1,position(';' in range_type.content_extra)-1),10) + if(task.type_id in (1,2),1,2) as type_id
      ,task.task_name as name
      ,concat('/',task.project_name,'/',range_type.content,'/',task.path) as path
      ,unix_timestamp(ifnull(task.create_date,0)) as create_time_stamp
      ,date_format(task.create_date,'%Y-%m-%d %T') as create_time_text
      ,unix_timestamp(if(task.update_date is null,ifnull(task.create_date,0),task.update_date)) as last_modify_time_stamp
      ,date_format(ifnull(task.update_date,task.create_date),'%Y-%m-%d %T') as last_modify_time_text
      ,ifnull(task.update_by,task.create_by) as owner_user_id
      ,task.update_by as last_modify_user_id
      ,null as last_modify_role_id
      ,if(task.id is null,null,0) as file_length
      ,range_type.value_id as range_id
      ,substring(node_type.content_extra,1,1) as is_directory
      ,substring(node_type.content_extra,2,1) as is_project
      ,substring(node_type.content_extra,3,1) as is_task
      ,substring(node_type.content_extra,4,1) as is_design
      ,substring(node_type.content_extra,5,1) as is_commit
      ,substring(node_type.content_extra,6,1) as is_history
      ,task.issue_id
      ,task.id as task_id
      ,task.project_id
      ,task.company_id
      ,task.project_id as root_id
    from
      maoding_task task
      inner join maoding_const range_type on (range_type.classic_id = 24 and position(concat('!',task.type_id,'!') in (range_type.content_extra)) > 0)
      inner join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id = if(position(';' in range_type.content_extra) > 1,substring(range_type.content_extra,1,position(';' in range_type.content_extra)-1),10) + if(task.type_id in (1,2),1,2))
    where (task.type_id in (0,1,2));

  -- -- 文件节点视图与文件树相关部分
  CREATE OR REPLACE VIEW `maoding_storage_storage` AS
    select
       concat(storage_tree.id,'-',range_type.value_id) as id
      ,if(storage_tree.pid is null,concat(storage_tree.task_id,'-',range_type.value_id),concat(storage_tree.pid,'-',range_type.value_id)) as pid
      ,storage_tree.type_id
      ,storage_tree.node_name as name
      ,concat('/',task.project_name,'/',range_type.content,'/',task.path,'/',storage_tree.path) as path
      ,unix_timestamp(ifnull(storage_tree.create_time,0)) as create_time_stamp
      ,date_format(storage_tree.create_time,'%Y-%m-%d %T') as create_time_text
      ,unix_timestamp(ifnull(storage_tree.last_modify_time,0)) as last_modify_time_stamp
      ,date_format(storage_tree.last_modify_time,'%Y-%m-%d %T') as last_modify_time_text
      ,storage_tree.owner_user_id
      ,storage_tree.last_modify_user_id
      ,storage_tree.last_modify_role_id
      ,storage_tree.file_length
      ,range_type.value_id as range_id
      ,substring(node_type.content_extra,1,1) as is_directory
      ,substring(node_type.content_extra,2,1) as is_project
      ,substring(node_type.content_extra,3,1) as is_task
      ,substring(node_type.content_extra,4,1) as is_design
      ,substring(node_type.content_extra,5,1) as is_commit
      ,substring(node_type.content_extra,6,1) as is_history
      ,task.issue_id
      ,task.id as task_id
      ,task.project_id
      ,task.company_id
      ,task.project_id as root_id
    from
      maoding_storage_tree storage_tree
      inner join maoding_const range_type on (range_type.classic_id = 24 and position(concat(':',storage_tree.type_id,':') in (range_type.content_extra)) > 0)
      inner join maoding_const node_type on (node_type.classic_id = 14 and storage_tree.type_id = node_type.value_id)
      inner join maoding_task task on (storage_tree.task_id = task.id)
    where
      (storage_tree.deleted = 0);

  -- -- 网站节点视图
  CREATE OR REPLACE VIEW `maoding_storage_old_node` AS
    select
      old_node.id
      ,if(old_node.pid is null,concat(old_node.project_id,'-',range_type.value_id),old_node.pid) AS pid
      ,if(old_node.type in (0,2,30,40,50) or old_node.file_size = 0,
          if(position(';' in range_type.content_extra) > 1,
              substring(range_type.content_extra,1,
                position(';' in range_type.content_extra)-1),10) +
              if(old_node.type in (40,50),2,1),4) as type_id
      ,old_node.file_name as name
      ,concat('/',project.project_name,'/',range_type.content,'/'
        ,if(old_node_parent4.file_name is null,'',concat(old_node_parent4.file_name,'/'))
        ,if(old_node_parent3.file_name is null,'',concat(old_node_parent3.file_name,'/'))
        ,if(old_node_parent2.file_name is null,'',concat(old_node_parent2.file_name,'/'))
        ,if(old_node_parent1.file_name is null,'',concat(old_node_parent1.file_name,'/'))
        ,old_node.file_name) as path
      ,unix_timestamp(ifnull(old_node.create_date,0)) as create_time_stamp
      ,date_format(old_node.create_date,'%Y-%m-%d %T') as create_time_text
      ,unix_timestamp(ifnull(old_node.update_date,ifnull(old_node.create_date,0))) as last_modify_time_stamp
      ,date_format(ifnull(old_node.update_date,old_node.create_date),'%Y-%m-%d %T') as last_modify_time_text
      ,ifnull(old_node.update_by,old_node.create_by) as owner_user_id
      ,old_node.update_by as last_modify_user_id
      ,null as last_modify_role_id
      ,old_node.file_size as file_length
      ,if(old_node.id is null,null,3) as range_id
      ,substring(node_type.content_extra,1,1) as is_directory
      ,substring(node_type.content_extra,2,1) as is_project
      ,substring(node_type.content_extra,3,1) as is_task
      ,substring(node_type.content_extra,4,1) as is_design
      ,substring(node_type.content_extra,5,1) as is_commit
      ,substring(node_type.content_extra,6,1) as is_history
      ,task.issue_id
      ,old_node.task_id
      ,old_node.project_id
      ,old_node.company_id
      ,old_node.project_id as root_id
    from
      maoding_web_project_sky_drive old_node
      inner join maoding_web_project project on (old_node.project_id = project.id)
      inner join maoding_const range_type on (range_type.classic_id = 24 and position(concat('|',old_node.type,'|') in (range_type.content_extra)) > 0)
      inner join maoding_const node_type on (node_type.classic_id = 14
                                             and node_type.value_id = if(old_node.type in (0,2,30,40,50) or old_node.file_size = 0,
                                                 if(position(';' in range_type.content_extra) > 1,
                                                    substring(range_type.content_extra,1,
                                                      position(';' in range_type.content_extra)-1),10) +
                                                  if(old_node.type in (40,50),2,1),4))
      left join maoding_task task on (old_node.task_id = task.id)
      left join maoding_web_project_sky_drive old_node_parent1 ON (old_node_parent1.id = old_node.pid)
      left join maoding_web_project_sky_drive old_node_parent2 ON (old_node_parent2.id = old_node_parent1.pid)
      left join maoding_web_project_sky_drive old_node_parent3 ON (old_node_parent3.id = old_node_parent2.pid)
      left join maoding_web_project_sky_drive old_node_parent4 ON (old_node_parent4.id = old_node_parent3.pid)
    where
      (old_node.status in (0,1));

  -- -- 新的文件树节点通用视图
  CREATE OR REPLACE VIEW `maoding_storage_copy` AS
    select * from maoding_storage_project
    union all
    select * from maoding_storage_range
    union all
    select * from maoding_storage_task
    union all
    select * from maoding_storage_storage;

  -- -- 角色视图
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

  -- -- 文档分类定义视图
  CREATE OR REPLACE VIEW `maoding_range` AS
    select
      range_type.code_id as id,
      range_type.code_id as code_id,
      range_type.title as type_name,
      substring(range_type.extra,
                1,
                char_length(substring_index(range_type.extra,';',1)))
        as type_id,
      substring(range_type.extra,
                char_length(substring_index(range_type.extra,';',2))+2,
                char_length(substring_index(range_type.extra,';',3)) - char_length(substring_index(range_type.extra,';',2))-1)
        as sub_node_type,
      substring(range_type.extra,
                char_length(substring_index(range_type.extra,';',1))+2,
                char_length(substring_index(range_type.extra,';',2)) - char_length(substring_index(range_type.extra,';',1))-1)
        as web_mapper
    from
      md_list_const range_type
    where
      range_type.classic_id = 24;

  -- -- 权限定义视图
  CREATE OR REPLACE VIEW `maoding_permission` AS
    select
      permission_type.code_id as id,
      substring(permission_type.title,1,locate(';',permission_type.title) - 1) AS code_id,
      substring(permission_type.title,locate(';',permission_type.title) + 1) AS title
    from
      md_list_const permission_type
    where
      permission_type.classic_id = 1;

  -- -- 角色定义视图
  CREATE OR REPLACE VIEW `maoding_role` AS
    select
      role_type.code_id as id,
      substring(role_type.title,1,locate(';',role_type.title) - 1) as code_id,
      substring(role_type.title,locate(';',role_type.title) + 1) as title,
      substring(role_type.extra,
                1,
                char_length(substring_index(role_type.extra,';',1)))
        as manage_role,
      substring(role_type.extra,
                char_length(substring_index(role_type.extra,';',1))+2,
                char_length(substring_index(role_type.extra,';',2)) - char_length(substring_index(role_type.extra,';',1))-1)
        as have_permission,
      substring(role_type.extra,
                char_length(substring_index(role_type.extra,';',2))+2)
        as web_mapper
    from
      md_list_const role_type
    where
      role_type.classic_id = 26;

  -- -- 角色可管理角色
  CREATE OR REPLACE VIEW `maoding_role_manage_role` AS
    select
      role_type.*,
      sub_role_type.id as sub_role_id,
      sub_role_type.code_id as sub_role_code_id,
      sub_role_type.title as sub_role_title,
      sub_role_type.have_permission as sub_role_have_permission
    from
      maoding_role role_type
      left join maoding_role sub_role_type on (find_in_set(sub_role_type.id,role_type.manage_role));

  -- -- 角色直接权限
  CREATE OR REPLACE VIEW `maoding_role_permission` AS
    select
      role.*,
      permission.id as permission_id,
      permission.code_id as permission_code_id,
      permission.title as permission_title
    from
      maoding_role role
      left join maoding_permission permission on (find_in_set(permission.id,role.have_permission));

  -- -- 角色所有权限
  CREATE OR REPLACE VIEW `maoding_role_permission_all` AS
    select
      role.id,role.code_id,role.title,role.manage_role,role.have_permission,
      permission.id as permission_id,
      permission.code_id as permission_code_id,
      permission.title as permission_title
    from
      maoding_role_manage_role role
      left join maoding_permission permission on (find_in_set(permission.id,role.have_permission) or
                                                   find_in_set(permission.id,role.sub_role_have_permission))
    group by role.id,permission.id;
END;

-- 建立初始化常量存储过程
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
BEGIN
  -- 常量分类
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,0,'常量分类',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,1,'操作权限',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,2,'合作类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,3,'任务类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,4,'财务类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,6,'财务节点类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,7,'动态类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,8,'个人任务类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,9,'邀请目的类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,10,'通知类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,12,'用户类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,13,'组织类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,14,'存储节点类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,15,'锁定状态',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,16,'同步模式',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,17,'删除状态',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,19,'文件服务器类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,20,'文件操作类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,21,'保留',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,22,'专业类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,23,'保留',null);
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,24,'资料分类',null);
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,25,'保留',null);
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,26,'角色类型',null);
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,27,'通知类型',null);

  -- -- -- -- --
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,27,'通知类型','0.主题;1.标题;2.内容');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,0,'未定义类型',';;');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,1,'用户通用消息','User{UserId};用户消息;普通用户消息');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,2,'任务通用消息','Task{TaskId};任务消息;普通任务消息');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,3,'项目通用消息','Project{ProjectId};项目消息;普通项目消息');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,4,'组织通用消息','Company{CompanyId};组织消息;普通组织消息');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,5,'公共通用消息','notify:web;公共消息;普通公共消息');

  -- -- -- -- --
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,26,'角色类型','1.可分配角色，^^-可分配角色编号，2.权限，||-权限编号，3.web对应关系，!!-web项目角色类型,::-web组织角色类型');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,0,'未知角色',';;');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,1,'立项人',';;!0!');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,2,'project_manager;经营负责人',';1,2,3;!1!');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,3,'design_manager;设计负责人','4;1,2;!2!');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,4,'任务负责人','^5^6^7^;;!3!');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,5,'设计',';;!4!');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,6,'校对',';;!5!');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,7,'审核',';;!6!');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,8,'系统管理',';;:1:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,9,'组织管理',';;:2:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,10,'org_manager;企业负责人','2,3,14;:1:2:3:;:3:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,15,'1类事业合伙人企业负责人',';;:3:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,16,'2类事业合伙人企业负责人',';;:3:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,17,'3类事业合伙人企业负责人',';;:3:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,11,'行政管理',';;:4:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,12,'项目管理',';;:5:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,13,'财务管理',';;:6:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,14,'后台管理',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,18,'组织管理',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,19,'审批报表',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,20,'项目管理',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,21,'财务管理',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,22,'创建分支架构/事业合伙人',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,23,'权限配置',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,24,'企业认证',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,25,'历史数据导入',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,26,'组织信息管理',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,27,'组织架构设置',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,28,'通知公告发布',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,29,'查看报销、费用统计报表',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,30,'查看请假、出差、工时统计报表',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,31,'删除项目',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,32,'任务签发',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,33,'生产安排',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,34,'合同信息',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,35,'项目信息/项目总览/查看项目文档',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,36,'查看财务报表',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,37,'费用录入',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,38,'财务设置',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,39,'确认付款日期',';;:7:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,40,'确认到账日期',';;:7:');

  -- -- -- -- --
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,25,'保留',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,0,'未知分类',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,1,'项目角色',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,2,'任务角色',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,3,'组织角色',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,4,'任务用户角色',null);

  -- -- -- -- --
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,24,'资料分类','1.分类文件夹属性;2.可转换的输入类型，!!-任务类型，||-老节点类型;3.包含的储存节点类型，::-子节点类型');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,0,'未知分类',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,1,'设计','20;!0!1!2!;:0:1:10:18:21:22:23:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,2,'提资','30;!1!2!;:2:3:31:32:33:');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,3,'发布','40;|0|1|2|3|4|5|6|7|8|9|10|20|21|30|40|50|;:4:42:43:');

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,23,'保留','0.角色分类类型');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,0,'立项人','1');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,1,'经营负责人','1');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,2,'设计负责人','1');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,3,'任务负责人','2');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,4,'设计','2');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,5,'校对','2');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,6,'审核','2');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,10,'企业负责人','3');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,11,'财务','3');

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,22,'专业类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,0,'规划',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,1,'建筑',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,2,'室内',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,3,'景观',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,4,'结构',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,5,'给排水',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,6,'暖通',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,7,'电气',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,8,'其他',null);

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,21,'保留',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,0,'本树节点',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,1,'项目节点','project_id');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,2,'任务节点','task_id');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,3,'报销节点','exp_id');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,4,'通告节点','notice_id');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,5,'公司节点','company_id');

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,20,'文件操作类型','0.新建节点类型:节点路径;1.文件服务器类型:文件服务器地址;2.通知类型(:分隔)');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,0,'无效','1;;;');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,1,'备份','3:历史版本/{SrcFileNoExt}_{Time:yyyyMMddHHmmss}{Ext};1;');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,2,'校对','1:{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;2');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,3,'审核','1:{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;2');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,4,'提资','2:/{Project}/{Classic2}/{IssuePath}/{Major}/{Version}/{TaskPath}/{SrcPath};1;3');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,5,'上传',';2;5');

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,19,'文件服务器类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (19,0,'无效',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (19,1,'本地磁盘',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (19,2,'网站空间',null);

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,17,'删除状态',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (17,0,'未删除',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (17,1,'已删除',null);

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,16,'同步模式',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (16,0,'手动同步',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (16,1,'自动同步',null);

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,15,'锁定状态',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (15,0,'不锁定',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (15,1,'锁定',null);

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,14,'存储节点类型','[]<>前-节点属性，1:是否目录，2:是否项目，3:是否任务，4:是否设计文档，5:是否提资文档，6:是否历史版本，[]:子目录类型，<>子文件类型');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,0,'未知类型','000100[0]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,1,'设计文件','000100[0]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,2,'提资资料','000010[0]<2>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,3,'历史版本','000001[0]<3>');
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,4,'网站归档文件','000000[0]<4>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,10,'未知类型目录','100000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,11,'项目目录','110000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,12,'任务目录','101000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,13,'组织目录','100000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,14,'通告目录','100000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,15,'报销目录','100000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,16,'备份目录','100001[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,17,'回收站目录','100000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,18,'用户目录','100100[18]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,20,'设计目录','100100[23]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,21,'设计签发任务目录','101100[23]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,22,'设计生产任务目录','101100[23]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,23,'设计自定义目录','100100[23]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,30,'提资目录','100010[33]<2>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,31,'提资任务目录','101010[33]<2>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,32,'提资历史目录','100011[33]<3>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,33,'提资自定义目录','100010[33]<2>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,40,'档案目录','100000[10]<1>');
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,41,'网站目录','100000[10]<4>');
  REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,42,'网站归档目录','100000[10]<4>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,50,'成果目录','100000[10]<1>');
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,60,'设计依据目录','100000[10]<1>');

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,11,'共享类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (11,0,'全部共享',null);

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,5,'文件类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,0,'未知类型',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,1,'CAD设计文档',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,3,'合同附件',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,4,'公司logo',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,5,'认证授权书',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,6,'移动端上传轮播图片',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,7,'公司邀请二维码',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,8,'营业执照',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,9,'法人身份证信息',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,20,'报销附件',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,21,'通知公告附件',null);

  -- -- -- -- --
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,1,'操作权限',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,0,'-;无权限',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,1,'org_partner;创建非自己负责的事业合伙人/分公司',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,2,'disband_others;解散非自己负责的事业合伙人/分公司',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,3,'sys_enterprise_logout;解散自己负责的组织',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,4,'invite_others;邀请事业合伙人/分公司',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,5,'sys_role_permission;权限配置',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,6,'sys_role_auth;申请企业认证',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,7,'data_import;历史数据导入',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,8,'com_enterprise_edit;组织信息管理',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,9,'hr_org_set,hr_employee;组织架构管理',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,10,'admin_notice;发布通知公告',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,11,'finance_report;查看财务报表',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,12,'finance_fixed_edit;费用录入',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,13,'sys_finance_type;财务设置',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,14,'project_charge_manage;确认付款日期',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,15,'finance_back_fee;确认到账日期',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,16,'report_exp_static;查看费用统计报表',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,17,'summary_leave;查看工时统计报表',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,18,'project_delete;删除参与项目',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,19,'delete_project_others;删除未参与项目',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,20,'project_manager;编辑和发布参与项目的签发任务',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,21,'design_manager;安排自己负责的生产任务',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,22,'design_others;安排非自己负责的生产任务',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,23,'project_view_amount;查看参与项目合同信息',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,24,'view_contract_others;查看未参与项目合同信息',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,25,'edit_contract;编辑参与项目基本信息',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,26,'project_edit;查看参与项目文档',null);
	REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,27,'view_document_others;查看未参与项目文档',null);

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
-- call createIndex();