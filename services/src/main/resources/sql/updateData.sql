-- fillStorageInfo -- 从老的sky_drive表中复制数据到storage中
DROP PROCEDURE IF EXISTS `fillStorageInfo`;
CREATE PROCEDURE `fillStorageInfo`()
BEGIN
    -- 清除项目一级目录
#     delete from maoding_storage_tree where (type_id = 20 or type_id = 30);
# 		update
# 				maoding_storage_tree src
# 				inner join
# 				(select *
# 				 from (
# 							select node.id, node.path,node.type_id,node.pid
# 									,task.issue_id, task.id as task_id, task.project_id, task.company_id
# 									,if(position(task.path in node.path) > 0,substring(node.path, position(task.path in node.path) + char_length(task.path) + 1),node.path) as new_path
# 							from maoding_storage_tree node
# 									left join maoding_storage_tree parent1 on (node.pid=parent1.id)
# 									left join maoding_storage_tree parent2 on (parent1.pid=parent2.id)
# 									left join maoding_storage_tree parent3 on (parent2.pid=parent3.id)
# 									left join maoding_storage_tree parent4 on (parent3.pid=parent4.id)
# 									left join maoding_storage_tree parent5 on (parent4.pid=parent5.id)
# 									left join maoding_storage_tree parent6 on (parent5.pid=parent6.id)
# 									left join maoding_storage_tree parent7 on (parent6.pid=parent7.id)
# 									left join maoding_storage_tree parent8 on (parent7.pid=parent8.id)
# 									left join maoding_storage_tree parent9 on (parent8.pid=parent9.id)
# 									inner join maoding_task task on (concat(ifnull(parent9.pid,''),
# 																													ifnull(parent8.pid,''),
# 																													ifnull(parent7.pid,''),
# 																													ifnull(parent6.pid,''),
# 																													ifnull(parent5.pid,''),
# 																													ifnull(parent4.pid,''),
# 																													ifnull(parent3.pid,''),
# 																													ifnull(parent2.pid,''),
# 																													ifnull(parent1.pid,''),
# 																													ifnull(node.pid,''))
# 																									 like concat('%',task.id,'%'))
# 							order by length(task.path) desc
# 							) tmp
# 				 group by id
# 				) dst on (dst.id = src.id)
# 		set src.task_id=ifnull(dst.task_id,dst.issue_id),src.path=dst.new_path,
# 			src.type_id=if(src.type_id > 10 and dst.new_path like '建筑%',33,if(src.type_id=18,23,src.type_id));
END;

-- createDefaultUser -- 创建默认用户
DROP PROCEDURE IF EXISTS `createDefaultUser`;
CREATE PROCEDURE `createDefaultUser`()
BEGIN
	if not exists (select 1 from maoding_web_account where cellphone='13680809727') then
		REPLACE INTO maoding_web_account (id,user_name,password,cellphone,status,create_date,active_time)
		VALUES ('5ffee496fa814ea4b6d26a9208b00a0b','sun','E10ADC3949BA59ABBE56E057F20F883E','13680809727','0','2017-07-04 11:59:37','2017-07-03 18:57:36');
	end if;
END;

-- 复制文件实际存储路径
DROP PROCEDURE IF EXISTS `copyFileRealPath`;
CREATE PROCEDURE `copyFileRealPath`()
BEGIN
	if not exists (select 1 from maoding_storage_file where read_only_scope is not null) then
		if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='read_file_scope') then
			update maoding_storage_file
			set	read_only_scope = read_file_scope,
				read_only_key = read_file_key,
				writable_scope = write_file_scope,
				writable_key = write_file_key;
		end if;
	end if;
	if not exists (select 1 from maoding_storage_file where last_modify_role_id is not null) then
		if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='last_modify_duty_id') then
			update maoding_storage_file
			set	last_modify_role_id = last_modify_duty_id;
		end if;
	end if;
END;

-- 复制storage数据到storage_tree
DROP PROCEDURE IF EXISTS `copyStorageData`;
CREATE PROCEDURE `copyStorageData`()
	BEGIN
				if not exists (select 1 from maoding_storage_tree where id is not null) then
					if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage') then
						insert into maoding_storage_tree (id,deleted,create_time,last_modify_time,last_modify_user_id,last_modify_role_id,pid,path,type_id,node_name,file_length,task_id)
							select id,deleted,create_time,last_modify_time,last_modify_user_id,last_modify_duty_id,pid,path,type_id,node_name,file_length,task_id from maoding_storage;
				  end if;
				end if;
	END;

call createDefaultUser();
call copyFileRealPath();
call copyStorageData();