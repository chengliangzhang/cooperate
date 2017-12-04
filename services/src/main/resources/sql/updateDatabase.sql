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

  -- maoding_storage_file_duty -- 协同文件评审历史记录定义
	CREATE TABLE IF NOT EXISTS `maoding_storage_file_his` (
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
	-- 备份原有数据
	call backupTables();

	-- 创建新表，跳过已存在的表
# 	call createNewTables();

	-- 添加字段，跳过已存在的字段
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

	-- 重新建立索引
	call createIndex();

	-- 还原原有数据
	call restoreData();

	-- 补充新架构数据
	if not exists (select 1 from maoding_const where classic_id=0 and value_id=20) then
		INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,20,'协同文件历史类型',null);
	end if;
	if not exists (select 1 from maoding_const where classic_id=20 and value_id=0) then
		INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,0,'审核',null);
	end if;
	if not exists (select 1 from maoding_const where classic_id=20 and value_id=1) then
		INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,1,'校验',null);
	end if;

	-- -- -- -- 更改或删除数据（需证明的确有必要）

	-- -- -- -- 更改或删除字段（小心，需经过充分测试）
	if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='downloading') then
		alter table maoding_storage_file modify column `downloading` int(8) unsigned DEFAULT '0' COMMENT '当前有多少用户正在下载';
  end if;

	-- -- -- -- 清理无效表（小心，需经过充分测试）

END;
call updateDatabase();