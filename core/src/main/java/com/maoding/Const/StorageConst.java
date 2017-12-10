package com.maoding.Const;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/20 11:44
 * 描    述 :
 */
public interface StorageConst {
    //节点类型
    final static Short STORAGE_NODE_TYPE_MAIN_FILE = 0;
    final static Short STORAGE_NODE_TYPE_REF_FILE = 1;
    final static Short STORAGE_NODE_TYPE_SYS_DIR = 10;
    final static Short STORAGE_NODE_TYPE_PROJECT_DIR = 11;
    final static Short STORAGE_NODE_TYPE_TASK_DIR = 12;
    final static Short STORAGE_NODE_TYPE_ORG_DIR = 13;
    final static Short STORAGE_NODE_TYPE_NOTICE_DIR = 14;
    final static Short STORAGE_NODE_TYPE_EXP_DIR = 15;
    final static Short STORAGE_NODE_TYPE_BACK_DIR = 16;
    final static Short STORAGE_NODE_TYPE_RECYCLE_DIR = 17;
    final static Short STORAGE_NODE_TYPE_USER_DIR = 18;

    //文件类型
    final static Short STORAGE_FILE_TYPE_UNKNOWN = 0;
    final static Short STORAGE_FILE_TYPE_CAD = 1;
    final static Short STORAGE_FILE_TYPE_CONTRACT = 3;
    final static Short STORAGE_FILE_TYPE_LOGO = 4;
    final static Short STORAGE_FILE_TYPE_AUTH = 5;
    final static Short STORAGE_FILE_TYPE_APP = 6;
    final static Short STORAGE_FILE_TYPE_INVITE = 7;
    final static Short STORAGE_FILE_TYPE_LICENCE = 8;
    final static Short STORAGE_FILE_TYPE_LEGAL = 9;
    final static Short STORAGE_FILE_TYPE_EXP = 20;
    final static Short STORAGE_FILE_TYPE_NOTICE = 21;

    //默认值
    final static Short STORAGE_NODE_TYPE_FILE_MIN = STORAGE_NODE_TYPE_MAIN_FILE;
    final static Short STORAGE_NODE_TYPE_FILE_MAX = STORAGE_NODE_TYPE_REF_FILE;
    final static Short STORAGE_NODE_TYPE_DIR_MIN = STORAGE_NODE_TYPE_SYS_DIR;
    final static Short STORAGE_NODE_TYPE_DIR_MAX = STORAGE_NODE_TYPE_USER_DIR;

    //以下常量有可能被删除
    final static Short STORAGE_NODE_TYPE_UNKNOWN = 99;
    final static Short STORAGE_NODE_TYPE_HIS_FILE = 2;
}
