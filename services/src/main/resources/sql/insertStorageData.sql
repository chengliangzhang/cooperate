DROP PROCEDURE IF EXISTS insertStorageData;
CREATE PROCEDURE insertStorageData()
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
call insertStorageData();