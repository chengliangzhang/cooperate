-- 建立创建表结构存储过程
DROP PROCEDURE IF EXISTS createEmptyTables;
CREATE PROCEDURE createEmptyTables()
BEGIN
	DROP TABLE IF EXISTS `maoding_const`;
	-- maoding_const -- 常量定义
	CREATE TABLE IF NOT EXISTS `maoding_const` (
		`classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
		`value_id` smallint(4) unsigned NOT NULL COMMENT '特定分类内的常量编号',
		`content` varchar(255) NOT NULL COMMENT '常量的基本定义',
		`content_extra` varchar(255) DEFAULT NULL COMMENT '常量的扩展定义',
		PRIMARY KEY (`classic_id`,`value_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	DROP TABLE IF EXISTS `maoding_storage`;
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
		`type_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '节点类型',
		`node_name` varchar(255) DEFAULT NULL COMMENT '树节点名',
		`pid_type_id` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '父节点类型',
		`file_length` bigint(16) unsigned DEFAULT '0' COMMENT '文件长度，如果节点是目录则固定为0',
		
		`lock_user_id` varchar(32) DEFAULT NULL COMMENT '（取消，因只需锁定文件）锁定树节点的用户id',
		`detail_id` char(32) DEFAULT NULL COMMENT '（取消，因唯一编号同id）对应file/dir内的唯一编号',

		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	DROP TABLE IF EXISTS `maoding_storage_file`;
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

  DROP TABLE IF EXISTS `maoding_storage_file_his`;
  -- maoding_storage_file_his -- 协同文件校审历史记录定义
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

  DROP TABLE IF EXISTS `maoding_storage_file_ref`;
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


	DROP TABLE IF EXISTS `maoding_storage_dir`;
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

	DROP TABLE IF EXISTS `maoding_web_account`;
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

-- 建立初始化常量存储过程
DROP PROCEDURE IF EXISTS initConst;
CREATE PROCEDURE initConst()
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

  delete from maoding_const where classic_id = 21;
	-- 父节点类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,21,'父节点类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,0,'本树节点',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,1,'项目节点','project_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,2,'任务节点','task_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,3,'报销节点','exp_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,4,'通告节点','notice_id');
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (21,5,'公司节点','company_id');

  delete from maoding_const where classic_id = 20;
	-- 校审类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,20,'校审类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,0,'校验',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (20,1,'审核',null);

  delete from maoding_const where classic_id = 19;
	-- 文件服务器类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,19,'文件服务器类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,0,'本地服务器',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (19,1,'阿里云服务器',null);

  delete from maoding_const where classic_id = 17;
	-- 删除状态
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,17,'删除状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (17,0,'未删除',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (17,1,'已删除',null);

  delete from maoding_const where classic_id = 16;
	-- 同步模式
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,16,'同步模式',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (16,0,'手动同步',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (16,1,'自动同步',null);

  delete from maoding_const where classic_id = 15;
	-- 锁定状态
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,15,'锁定状态',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (15,0,'不锁定',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (15,1,'锁定',null);

  delete from maoding_const where classic_id = 14;
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

  delete from maoding_const where classic_id = 11;
	-- 共享类型
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (0,11,'共享类型',null);
	REPLACE INTO maoding_const (classic_id,value_id,content,content_extra) VALUES (11,0,'全部共享',null);

  delete from maoding_const where classic_id = 5;
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