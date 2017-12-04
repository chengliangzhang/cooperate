-- 建立创建表结构存储过程
DROP PROCEDURE IF EXISTS createEmptyTables;
CREATE PROCEDURE createEmptyTables()
BEGIN
	-- maoding_const -- 常量定义
	DROP TABLE IF EXISTS `maoding_const`;
	CREATE TABLE IF NOT EXISTS `maoding_const` (
		`classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
		`value_id` smallint(4) unsigned NOT NULL COMMENT '特定分类内的常量编号',
		`content` varchar(255) NOT NULL COMMENT '常量的基本定义',
		`content_extra` varchar(255) DEFAULT NULL COMMENT '常量的扩展定义',
		PRIMARY KEY (`classic_id`,`value_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	-- maoding_storage -- 协同节点定义
	DROP TABLE IF EXISTS `maoding_storage`;
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
	DROP TABLE IF EXISTS `maoding_storage_file`;
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
  DROP TABLE IF EXISTS `maoding_storage_file_his`;
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
	DROP TABLE IF EXISTS `maoding_storage_dir`;
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

-- 建立初始化常量存储过程
DROP PROCEDURE IF EXISTS initConst;
CREATE PROCEDURE initConst()
BEGIN
	DECLARE classic_id_value smallint default 0;

  -- 常量分类
	set classic_id_value = 0;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'常量分类',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'操作权限',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,2,'合作类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,3,'任务类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,4,'财务类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,5,'文件类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,6,'节点类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,7,'动态类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,8,'个人任务类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,9,'邀请目的类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,10,'通知类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,11,'目录类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,12,'用户类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,13,'组织类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,14,'文件关系类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,15,'锁定状态',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,16,'同步模式',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,17,'删除状态',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,19,'文件服务器类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,20,'协同文件历史类型',null);

	-- 协同文件历史类型
	set classic_id_value = 20;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'审核',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'校验',null);

	-- 文件服务器类型
  set classic_id_value = 19;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'本地服务器',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'阿里云服务器',null);

	-- 删除状态
	set classic_id_value = 17;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'未删除',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'已删除',null);

	-- 同步模式
	set classic_id_value = 16;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'手动同步',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'自动同步',null);

	-- 锁定状态
	set classic_id_value = 15;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'不锁定',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'锁定',null);

	-- 文件关系类型
	set classic_id_value = 14;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'最新文件',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'参考文件',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,2,'历史版本',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,3,'系统目录',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,4,'用户目录',null);
  INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,99,'尚未建立的树节点',null);

	-- 目录类型
	set classic_id_value = 11;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'全部共享',null);

	-- 文件类型
	set classic_id_value = 5;
	DELETE FROM maoding_const WHERE classic_id=classic_id_value;
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,0,'未知类型',null);
	INSERT INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (classic_id_value,1,'CAD设计文档',null);
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
DROP PROCEDURE IF EXISTS createDatabase;
CREATE PROCEDURE createDatabase()
BEGIN
	-- 创建表结构，如果表已存在则删除原先的表
	call createEmptyTables();

	-- 在表内建立初始化数据
	call initConst();

	-- 建立索引
	call createIndex();
END;
call createDatabase();