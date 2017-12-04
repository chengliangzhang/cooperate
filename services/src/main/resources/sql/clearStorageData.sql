DROP PROCEDURE IF EXISTS clearStorageData;
CREATE PROCEDURE clearStorageData()
BEGIN
  delete from maoding_storage_file_his;
	delete from maoding_storage_file;
	delete from maoding_storage_dir;
	delete from maoding_storage;
END;
call clearStorageData();