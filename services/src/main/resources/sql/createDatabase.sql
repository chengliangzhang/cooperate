-- 创建重置数据库存储过程
DROP PROCEDURE IF EXISTS `createDatabase`;
CREATE PROCEDURE `createDatabase`()
BEGIN
  -- 清理要重建的数据库
	call clearTables();

	-- 创建表结构
	call updateTables();
	call updateViews();

	-- 在表内建立初始化数据
	call initConst();

	-- 建立索引
	call createIndex();
END;
call createDatabase();