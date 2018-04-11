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

--  清理无效表
DROP TABLE IF EXISTS `maoding_const`;
DROP TABLE IF EXISTS `md_list_const`;
DROP TABLE IF EXISTS `maoding_custom_const`;
DROP TABLE IF EXISTS `maoding_storage`;
DROP TABLE IF EXISTS `maoding_storage_dir`;
DROP TABLE IF EXISTS `maoding_storage_file`;
DROP TABLE IF EXISTS `maoding_storage_file_his`;
DROP TABLE IF EXISTS `maoding_storage_file_ref`;

-- 清理无效视图
DROP VIEW IF EXISTS `maoding_storage_file_tmp`;
DROP VIEW IF EXISTS `maoding_storage_file_info`;
DROP VIEW IF EXISTS `maoding_storage_node_root_copy`;
DROP VIEW IF EXISTS `maoding_storage_node_copy`;
DROP VIEW IF EXISTS `maoding_storage_root_copy`;
DROP VIEW IF EXISTS `maoding_storage_copy`;
DROP VIEW IF EXISTS `maoding_storage_old_node_copy`;
DROP VIEW IF EXISTS `maoding_range`;
DROP VIEW IF EXISTS `maoding_permission`;
DROP VIEW IF EXISTS `maoding_role`;
DROP VIEW IF EXISTS `maoding_role_permission`;
DROP VIEW IF EXISTS `maoding_role_permission_all`;
DROP VIEW IF EXISTS `maoding_task`;
DROP VIEW IF EXISTS `md_list_role_and_sub_role`;
DROP VIEW IF EXISTS `md_list_role_permission`;
DROP VIEW IF EXISTS `maoding_task`;
DROP VIEW IF EXISTS `maoding_issue`;
DROP VIEW IF EXISTS `maoding_role`;
DROP VIEW IF EXISTS `maoding_storage_all`;
DROP VIEW IF EXISTS `maoding_storage_node`;
DROP VIEW IF EXISTS `maoding_storage_node_commit`;
DROP VIEW IF EXISTS `maoding_storage_node_commit_task`;
DROP VIEW IF EXISTS `maoding_storage_node_design`;
DROP VIEW IF EXISTS `maoding_storage_node_design_task`;
DROP VIEW IF EXISTS `maoding_storage_old_node`;
DROP VIEW IF EXISTS `maoding_storage_root`;
DROP VIEW IF EXISTS `maoding_task`;
DROP VIEW IF EXISTS `maoding_task_member`;

-- 清理常量
-- delete from md_const where classic_id in (26,23);
