-- 建立清理表结构存储过程
DROP PROCEDURE IF EXISTS `clearDatabase`;
CREATE PROCEDURE `clearDatabase`()
BEGIN
	DROP TABLE IF EXISTS `maoding_const`;
	DROP TABLE IF EXISTS `maoding_storage`;
	DROP TABLE IF EXISTS `maoding_storage_file`;
  DROP TABLE IF EXISTS `maoding_storage_file_his`;
  DROP TABLE IF EXISTS `maoding_storage_file_ref`;
	DROP TABLE IF EXISTS `maoding_storage_dir`;
	DROP VIEW IF EXISTS `maoding_storage_file_tmp`;
	DROP VIEW IF EXISTS `maoding_storage_file_info`;
END;

-- 创建重置数据库存储过程
DROP PROCEDURE IF EXISTS `createDatabase`;
CREATE PROCEDURE `createDatabase`()
BEGIN
  -- 清理要重建的数据库元素
	call clearDatabase();

	-- 创建表结构
	call createTables();
	call updateViews();

	-- 在表内建立初始化数据
	call initConst();

	-- 建立索引
	call createIndex();
END;
call createDatabase();