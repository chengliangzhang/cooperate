-- 清理无效存储过程
DROP PROCEDURE IF EXISTS `updateFieldForQA`;
DROP PROCEDURE IF EXISTS `clearDatabase`;
DROP PROCEDURE IF EXISTS `clearTables`;
DROP PROCEDURE IF EXISTS `createTables`;
DROP PROCEDURE IF EXISTS `fillStorageInfo`;
DROP FUNCTION IF EXISTS `getIssueId`;
DROP FUNCTION IF EXISTS `getPath`;
DROP FUNCTION IF EXISTS `getTaskPath`;
DROP PROCEDURE IF EXISTS `restoreData`;
DROP PROCEDURE IF EXISTS `backupData`;

-- 清理无用字段/重建表
-- DROP TABLE IF EXISTS `maoding_const`;
-- DROP TABLE IF EXISTS `maoding_storage_tree`;
-- DROP TABLE IF EXISTS `maoding_storage_file`;
-- DROP TABLE IF EXISTS `maoding_storage_file_his`;
-- call updateTables();

--  清理无效表
DROP TABLE IF EXISTS `maoding_storage_file_ref`;
DROP TABLE IF EXISTS `maoding_storage_dir`;
DROP TABLE IF EXISTS `maoding_storage`;

-- 清理无效视图
DROP VIEW IF EXISTS `maoding_storage_file_tmp`;
DROP VIEW IF EXISTS `maoding_storage_file_info`;
DROP VIEW IF EXISTS `maoding_storage_node_root_copy`;
DROP VIEW IF EXISTS `maoding_storage_node_copy`;
DROP VIEW IF EXISTS `maoding_storage_root_copy`;
DROP VIEW IF EXISTS `maoding_storage_copy`;

