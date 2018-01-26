-- 清理无效存储过程
DROP PROCEDURE IF EXISTS `updateFieldForQA`;

-- 清理无效视图
DROP VIEW IF EXISTS `maoding_storage_file_tmp`;
DROP VIEW IF EXISTS `maoding_storage_file_info`;

-- 清理重建表和无效表
DROP TABLE IF EXISTS `maoding_const`;
DROP TABLE IF EXISTS `maoding_storage`;
DROP TABLE IF EXISTS `maoding_storage_file`;
DROP TABLE IF EXISTS `maoding_storage_file_his`;
DROP TABLE IF EXISTS `maoding_storage_file_ref`;
DROP TABLE IF EXISTS `maoding_storage_dir`;

-- 清理无用字段
