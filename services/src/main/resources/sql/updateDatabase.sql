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

	-- maoding_storage_tree -- 协同节点定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_tree` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_role_id` varchar(70) DEFAULT NULL COMMENT '记录最后修改者职责id',
		`pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
		`path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
		`type_id` smallint(4) unsigned DEFAULT '0' COMMENT '节点类型',
		`node_name` varchar(255) DEFAULT NULL COMMENT '树节点名',
		`file_length` bigint(16) unsigned DEFAULT '0' COMMENT '文件长度，如果节点是目录则固定为0',
		`task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_tree' and column_name='task_id') then
		alter table maoding_storage_tree add column `task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_tree' and column_name='last_modify_role_id') then
		alter table maoding_storage_tree add column `last_modify_role_id` varchar(70) DEFAULT NULL COMMENT '记录最后修改者职责id';
	end if;


	-- maoding_storage_file -- 协同文件定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_file` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_role_id` varchar(70) DEFAULT NULL COMMENT '记录最后修改者职责id',
		`server_type_id` smallint(4) unsigned DEFAULT '1' COMMENT '文件服务器类型',
		`server_address` varchar(255) DEFAULT NULL COMMENT '文件服务器地址',
		`file_type_id` smallint(4) unsigned DEFAULT '0' COMMENT '文件类型',
		`file_version` varchar(20) DEFAULT NULL COMMENT '文件版本',
		`file_checksum` varchar(64) DEFAULT NULL COMMENT '文件校验和',
		`major_type_id` smallint(4) unsigned DEFAULT '0' NULL COMMENT '文件所属专业id',
		`main_file_id` char(32) DEFAULT NULL COMMENT '所对应的原始文件id',
		`read_only_scope` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储位置',
		`read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称',
		`writable_scope` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储位置',
		`writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='write_file_scope') then
		alter table maoding_storage_file add column `write_file_scope` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储位置';
	end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='last_modify_role_id') then
    alter table maoding_storage_file add column `last_modify_role_id` varchar(70) DEFAULT NULL COMMENT '记录最后修改者职责id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='major_type_id') then
    alter table maoding_storage_file add column `major_type_id` smallint(4) unsigned DEFAULT '0' NULL COMMENT '文件所属专业id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='read_only_scope') then
    alter table maoding_storage_file add column `read_only_scope` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储位置';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='read_only_key') then
    alter table maoding_storage_file add column `read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='writable_scope') then
    alter table maoding_storage_file add column `writable_scope` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储位置';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='writable_key') then
    alter table maoding_storage_file add column `writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称';
  end if;


  -- maoding_storage_file_his -- 协同文件校审提资历史记录定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_file_his` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(70) DEFAULT NULL COMMENT '记录最后修改者职责id',
    `file_id` char(32) DEFAULT NULL COMMENT '协同文件编号id',
    `action_type_id` smallint(4) unsigned DEFAULT '0' COMMENT '校审动作类型',
    `remark` text(2048) DEFAULT NULL COMMENT '文件注释',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file_his' and column_name='remark') then
		alter table maoding_storage_file_his add column `remark` text(2048) DEFAULT NULL COMMENT '文件注释';
	end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file_his' and column_name='last_modify_role_id') then
    alter table maoding_storage_file_his add column `last_modify_role_id` varchar(70) DEFAULT NULL COMMENT '记录最后修改者职责id';
  end if;

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

  -- maoding_storage_root_copy -- 新的根节点附加属性视图，带有user_id_list字段
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

  -- maoding_storage_copy -- 新的树节点通用视图
  CREATE OR REPLACE VIEW `maoding_storage_copy` AS
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
      (project.pstatus = '0')
    group by project.id

    union all

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
      (range_type.classic_id = 24) and (range_type.value_id != 0)

    union all

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
    where (task.type_id in (0,1,2))
    group by task.id,range_type.value_id

    union all

    select
       concat(storage_tree.id,'-',range_type.value_id) as id
      ,concat(storage_tree.pid,'-',range_type.value_id) as pid
      ,storage_tree.type_id
      ,storage_tree.node_name as name
      ,concat('/',task.project_name,'/',range_type.content,'/',task.path,'/',storage_tree.path) as path
      ,unix_timestamp(ifnull(storage_tree.create_time,0)) as create_time_stamp
      ,date_format(storage_tree.create_time,'%Y-%m-%d %T') as create_time_text
      ,unix_timestamp(ifnull(storage_tree.last_modify_time,0)) as last_modify_time_stamp
      ,date_format(storage_tree.last_modify_time,'%Y-%m-%d %T') as last_modify_time_text
      ,storage_tree.last_modify_user_id as owner_user_id
      ,storage_tree.last_modify_role_id as last_modify_role_id
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
      (storage_tree.deleted = 0)
    group by storage_tree.id ;

  -- maoding_storage_old_node -- 网站节点视图
  CREATE OR REPLACE VIEW `maoding_storage_old_node` AS
      select
          old_node.id
          ,old_node.pid
          ,if(old_node.type in (1,3),4,if(old_node.type=40,42,41)) as type_id
          ,old_node.file_name as name
          ,concat('/',project.project_name,'/'
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
          ,null as last_modify_role_id
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
          left join maoding_const node_type on (node_type.classic_id = 14 and node_type.value_id = if(old_node.type in (1,3),4,if(old_node.type=40,42,41)))
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
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,6,'财务节点类型',null);
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
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,21,'保留',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,22,'专业类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,23,'保留',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,24,'资料分类',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,25,'保留',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,26,'角色类型',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,27,'通知类型',null);

  -- -- -- -- --
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,27,'通知类型','0.主题;1.标题;2.内容');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,0,'未定义类型',';;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,1,'用户通用消息','User{UserId};用户消息;普通用户消息');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,2,'任务通用消息','Task{TaskId};任务消息;普通任务消息');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,3,'项目通用消息','Project{ProjectId};项目消息;普通项目消息');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,4,'组织通用消息','Company{CompanyId};组织消息;普通组织消息');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (27,5,'公共通用消息','notify:web;公共消息;普通公共消息');

  -- -- -- -- --
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,26,'角色类型','!!-web项目角色类型,::-web数据库组织角色类型');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (26,0,'未知角色','');
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

  -- -- -- -- --
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,25,'保留',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,0,'未知分类',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,1,'项目角色',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,2,'任务角色',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,3,'组织角色',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (25,4,'任务用户角色',null);

  -- -- -- -- --
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,24,'资料分类','1.分类文件夹属性;2.包含的子节点类型，!!-包含的任务类型，::-包含的存储节点类型');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (24,0,'未知分类',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (24,1,'设计','20;!0!1!2!:0:1:10:18:23:');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (24,2,'提资','30;!1!2!:2:3:32:33:');

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,23,'保留','0.角色分类类型');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,0,'立项人','1');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,1,'经营负责人','1');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,2,'设计负责人','1');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,3,'任务负责人','2');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,4,'设计','2');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,5,'校对','2');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,6,'审核','2');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,10,'企业负责人','3');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (23,11,'财务','3');

  -- -- -- -- --
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

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,21,'保留',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,0,'本树节点',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,1,'项目节点','project_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,2,'任务节点','task_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,3,'报销节点','exp_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,4,'通告节点','notice_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,5,'公司节点','company_id');

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,20,'文件操作类型','0.新建节点类型:节点路径;1.文件服务器类型:文件服务器地址;2.通知类型(:分隔)');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,0,'无效','1;;;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,1,'备份','3:历史版本/{SrcFileNoExt}_{Time:yyyyMMddHHmmss}{Ext};1;');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,2,'校对','1:{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;2');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,3,'审核','1:{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;2');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,4,'提资','2:/{Project}/{Classic2}/{IssuePath}/{Major}/{Version}/{TaskPath}/{SrcPath};1;3');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,5,'上传',';2;5');

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,19,'文件服务器类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,0,'无效',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,1,'本地磁盘',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,2,'网站空间',null);

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,17,'删除状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (17,0,'未删除',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (17,1,'已删除',null);

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,16,'同步模式',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (16,0,'手动同步',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (16,1,'自动同步',null);

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,15,'锁定状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (15,0,'不锁定',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (15,1,'锁定',null);

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,14,'存储节点类型','[]<>前-节点属性，1:是否目录，2:是否项目，3:是否任务，4:是否设计文档，5:是否提资文档，6:是否历史版本，[]:子目录类型，<>子文件类型');
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

  -- -- -- -- --
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,11,'共享类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (11,0,'全部共享',null);

  -- -- -- -- --
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
call createIndex();