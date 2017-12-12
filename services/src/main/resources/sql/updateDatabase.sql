-- 建立备份数据存储过程
DROP PROCEDURE IF EXISTS backupTables;
CREATE PROCEDURE backupTables()
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
DROP PROCEDURE IF EXISTS restoreData;
CREATE PROCEDURE restoreData()
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

-- 建立创建表结构存储过程
DROP PROCEDURE IF EXISTS createNewTables;
CREATE PROCEDURE createNewTables()
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
		`type_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '文件关系类型',
		`node_name` varchar(255) DEFAULT NULL COMMENT '树节点名',
		`file_length` bigint(16) unsigned DEFAULT '0' COMMENT '文件长度，如果节点是目录则固定为0',
		
		`lock_user_id` varchar(32) DEFAULT NULL COMMENT '（取消，因只需锁定文件）锁定树节点的用户id',
		`detail_id` char(32) DEFAULT NULL COMMENT '（取消，因唯一编号同id）对应file/dir内的唯一编号',

		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	-- maoding_storage_file -- 协同文件定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_file` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_duty_id` char(32) DEFAULT NULL COMMENT '记录最后修改者职责id',
		`file_server_type_id` smallint(4) unsigned DEFAULT '1' COMMENT '文件服务器类型',
		`file_scope` varchar(255) DEFAULT NULL COMMENT '在文件服务器上的存储位置',
		`file_key` varchar(255) DEFAULT NULL COMMENT '在文件服务器上的存储名称',
		`file_type_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '文件类型',
		`file_checksum` varchar(8) DEFAULT NULL COMMENT '文件校验和',
		`file_version` varchar(20) DEFAULT NULL COMMENT '文件版本',
		`last_modify_address` varchar(20) DEFAULT NULL COMMENT '最后上传的地址',
		`sync_mode_id` smallint(4) unsigned DEFAULT '0' COMMENT '同步模式，0-手动同步，1-自动更新',
		`downloading` int(8) unsigned DEFAULT '0' COMMENT '当前有多少用户正在下载',
		`upload_file_server_type_id` smallint(4) unsigned DEFAULT '1' COMMENT '上传时使用的文件服务器类型',
		`upload_scope` varchar(255) DEFAULT NULL COMMENT '上传时使用的文件服务器存储位置',
		`upload_key` varchar(255) DEFAULT NULL COMMENT '上传时使用的文件服务器存储名称',
		`upload_id` varchar(255) DEFAULT NULL COMMENT '上传时使用的文件服务器上传任务编号',
		`creator_duty_id` char(32) DEFAULT NULL COMMENT '协同文件创建者的用户职责id',
		`creator_org_id` char(32) DEFAULT NULL COMMENT '协同文件创建者所属组织的id',
		`creator_user_id` char(32) DEFAULT NULL COMMENT '协同文件创建者的用户id',

		`uploaded_scope` varchar(255) DEFAULT NULL COMMENT '（取消，改名为uploadxxx)已上传到文件服务器的新scope值',
		`uploaded_key` varchar(255) DEFAULT NULL COMMENT '（取消，改名为uploadxxx)已上传到文件服务器的新key值',
		`specialty_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '（暂定取消，可以用file_type_id代替）文件的专业id',
		`file_length` bigint(16) unsigned DEFAULT '0' COMMENT '（取消，移入Storage)文件长度',
		`locking` tinyint(1) unsigned DEFAULT '0' COMMENT '（取消，使用lock_user_id代替）是否锁定修改，0-不锁定，1-锁定',
		`type_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '（取消，改名为file_type_id)文件类型',
		`file_name` varchar(255) DEFAULT NULL COMMENT '取消，同storage内node_name(文件名)',

		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- maoding_storage_file_his -- 协同文件评审历史记录定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_file_his` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_duty_id` char(32) DEFAULT NULL COMMENT '记录最后修改者职责id',
    `file_id` char(32) DEFAULT NULL COMMENT '协同文件编号id',
    `action_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '校审动作类型',

		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- maoding_storage_file_ref -- 协同文件参考文件定义
  CREATE TABLE IF NOT EXISTS `maoding_storage_file_ref` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_duty_id` char(32) DEFAULT NULL COMMENT '记录最后修改者职责id',
    `file_id` char(32) DEFAULT NULL COMMENT '协同文件编号id',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	-- maoding_storage_dir -- 协同目录定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_dir` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_duty_id` char(32) DEFAULT NULL COMMENT '记录最后修改者职责id',
		`project_id` char(32) DEFAULT NULL COMMENT '所属项目id,目录如果不属于任何一个项目，项目id为空',
		`task_id` char(32) DEFAULT NULL COMMENT '所属任务id，目录如果不属于任何一个任务，任务id为空',

		`user_id` char(32) DEFAULT NULL COMMENT '（取消，移入file)协同目录所属用户id',
		`duty_id` char(32) DEFAULT NULL COMMENT '（取消，移入file)协同目录所属用户的职责id',
		`org_id` char(32) DEFAULT NULL COMMENT '（取消，移入file)所属组织id,项目目录在立项时创建，创建组织为立项组织',
		`type_id` smallint(4) unsigned DEFAULT NULL COMMENT '(取消，目录不需要设定特殊类别）目录类别，如：系统默认目录、用户添加目录',
		`full_name` varchar(255) DEFAULT NULL COMMENT '(取消，同storage内path)全路径名',

		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

END;

-- 建立重置索引存储过程
DROP PROCEDURE IF EXISTS createIndex;
CREATE PROCEDURE createIndex()
BEGIN
	-- 重新创建索引
	declare sqlString VARCHAR(255);
	declare tableName VARCHAR(255);
	declare doneTable tinyint default false;
	declare curTable cursor for select table_name from information_schema.tables where (table_schema=database());
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






-- 创建数据库过程
DROP PROCEDURE IF EXISTS updateDatabase;
CREATE PROCEDURE updateDatabase()
BEGIN
  DECLARE classic_id_value smallint default 0;

	-- 备份原有数据
	call backupTables();

	-- 创建新表，跳过已存在的表
 	call createNewTables();

	-- 添加字段，跳过已存在的字段
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file_his' and column_name='action_id') then
		alter table maoding_storage_file_his add column `action_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '校审动作类型';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='file_server_type_id') then
		alter table maoding_storage_file add column `file_server_type_id` smallint(4) unsigned DEFAULT '1' COMMENT '文件服务器类型';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='file_type_id') then
		alter table maoding_storage_file add column `file_type_id` smallint(4) unsigned DEFAULT '0' COMMENT '同步模式，0-手动同步，1-自动更新';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage' and column_name='lock_user_id') then
		alter table maoding_storage add column `lock_user_id` varchar(32) DEFAULT NULL COMMENT '锁定的用户id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage' and column_name='file_length') then
		alter table maoding_storage add column `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '文件长度，如果节点是目录则固定为0';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='upload_file_server_type_id') then
		alter table maoding_storage_file add column `upload_file_server_type_id` smallint(4) unsigned DEFAULT '1' COMMENT '上传时使用的文件服务器类型';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='upload_scope') then
		alter table maoding_storage_file add column `upload_scope` varchar(255) DEFAULT NULL COMMENT '上传时使用的文件服务器存储位置';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='upload_key') then
		alter table maoding_storage_file add column `upload_key` varchar(255) DEFAULT NULL COMMENT '上传时使用的文件服务器存储名称';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='upload_id') then
		alter table maoding_storage_file add column `upload_id` varchar(255) DEFAULT NULL COMMENT '上传时使用的文件服务器上传任务编号';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='creator_org_id') then
		alter table maoding_storage_file add column `creator_org_id` char(32) DEFAULT NULL COMMENT '协同文件创建者所属组织的id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='creator_user_id') then
		alter table maoding_storage_file add column `creator_user_id` char(32) DEFAULT NULL COMMENT '协同文件创建者的用户id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage' and column_name='pid_type_id') then
		alter table maoding_storage add column `pid_type_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '父节点类型';
	end if;

	-- 重新建立索引
	call createIndex();

	-- 补充新架构数据
  if not exists (select 1 from maoding_web_account where cellphone='13680809727') then
    INSERT INTO maoding_web_account (id,user_name,password,cellphone,status,create_date,active_time)
      VALUES ('5ffee496fa814ea4b6d26a9208b00a0b','sun','E10ADC3949BA59ABBE56E057F20F883E','13680809727','0','2017-07-04 11:59:37','2017-07-03 18:57:36');
  end if;
	if ((select count(*) from information_schema.TABLES where TABLE_SCHEMA=database() and
			(TABLE_NAME in ('maoding_web_project_sky_drive','maoding_web_project','maoding_web_project_task',
												'maoding_web_exp_main','maoding_web_notice','maoding_web_company'))) = 6) then
		BEGIN
			-- 导入文档
			replace into maoding_storage (id,create_time,last_modify_time,last_modify_user_id,pid,path,node_name,type_id,file_length)
				select a.id,a.create_date,ifnull(a.update_date,a.create_date),ifnull(a.update_by,a.create_by),a.pid,
					if(e.id is not null,concat('/',f.company_name,'/',e.notice_title,'/',a.file_name),
						 if(d.id is not null,concat('/',f.company_name,'/',d.exp_no,'/',a.file_name),
								if(c.id is not null,concat('/',b.project_name,'/',
																					 (select GROUP_CONCAT(y.task_name order by length(y.task_path) SEPARATOR '/')
																						from maoding_web_project_task x
																							inner join maoding_web_project_task y on (x.task_path like concat(y.task_path,'%'))
																						where x.id = c.id),'/',a.file_name),
									 if(b.id is not null,concat('/',b.project_name,'/',a.file_name),
											concat('/',f.company_name,'/',a.file_name))))) as path,
					a.file_name,
					if((a.file_path is not null) or (a.file_size!=0) or (a.file_ext_name is not null),0,11),
					ifnull(a.file_size,0)
				from maoding_web_project_sky_drive a
					left join maoding_web_project b on (a.project_id=b.id)
					left join maoding_web_project_task c on (a.task_id=c.id)
					left join maoding_web_exp_main d on (a.target_id=d.id)
					left join maoding_web_notice e on (a.target_id=e.id)
					left join maoding_web_company f on (a.company_id=f.id)
				where ((b.id is not null) or (c.id is not null) or (d.id is not null) or (e.id is not null) or (f.company_name is not null))
							and ((c.id is null) or ((c.id is not null) and ((a.file_path is not null) or (a.file_size!=0) or (a.file_ext_name is not null))))
							and (a.file_name is not null) and (a.file_name != '');

			-- 更新目录id及对应的表类型
			drop table if exists id_convert;
			create temporary table id_convert (id char(32) NOT NULL,pid char(32),ptype smallint(4));
			insert into id_convert (id,pid,ptype) select x.id,
																					ifnull(e.id,ifnull(d.id,ifnull(c.id,ifnull(b.id,ifnull(f.id,x.pid))))),
																					if(e.id is not null,1,if(d.id is not null,2,
																																	 if(c.id is not null,3,if(b.id is not null,4,
																																														if(f.id is not null,5,0)))))
																				from maoding_storage x
																					left join maoding_storage y on (x.pid = y.id)
																					left join maoding_web_project_sky_drive a on (x.id=a.id)
																					left join maoding_web_project b on (a.project_id=b.id)
																					left join maoding_web_project_task c on (a.task_id=c.id)
																					left join maoding_web_exp_main d on (a.target_id=d.id)
																					left join maoding_web_notice e on (a.target_id=e.id)
																					left join maoding_web_company f on (a.company_id=f.id);

			update maoding_storage a inner join id_convert b on (a.id=b.id) set a.pid=b.pid,a.pid_type_id=b.ptype;
			drop table id_convert;
		END;
	end if;
	-- -- -- -- 更改或删除数据（需证明的确有必要）
	-- 父节点类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,21,'父节点类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,0,'本树节点',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,1,'项目节点','project_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,2,'任务节点','task_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,3,'报销节点','exp_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,4,'通告节点','notice_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,5,'公司节点','company_id');
	-- 存储节点类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,14,'存储节点类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,0,'未知类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,1,'主文件',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,2,'参考文件',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,3,'历史文件',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,10,'未知类型目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,11,'项目目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,12,'任务目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,13,'组织目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,14,'通告目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,15,'报销目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,16,'备份目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,17,'回收站目录',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (14,18,'用户目录',null);
  -- 校审类型
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,20,'校审类型',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,0,'校验',null);
  REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,1,'审核',null);
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

	-- -- -- -- 更改或删除字段（小心，需经过充分测试）
  if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file_his' and column_name='action_id') then
    alter table maoding_storage_file_his modify column `action_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '校审动作类型';
  end if;
	if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='downloading') then
		alter table maoding_storage_file modify column `downloading` int(8) unsigned DEFAULT '0' COMMENT '当前有多少用户正在下载';
  end if;

	-- -- -- -- 清理无效表（小心，需经过充分测试）

END;
call updateDatabase();