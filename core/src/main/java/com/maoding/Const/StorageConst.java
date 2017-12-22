package com.maoding.Const;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/20 11:44
 * 描    述 :
 */
public interface StorageConst {
    //节点类型
    final static Short STORAGE_NODE_TYPE_UNKNOWN = 0;
    final static Short STORAGE_NODE_TYPE_FILE_MAIN = 1;
    final static Short STORAGE_NODE_TYPE_FILE_REF = 2;
    final static Short STORAGE_NODE_TYPE_FILE_HIS = 3;
    final static Short STORAGE_NODE_TYPE_DIR_UNKNOWN = 10;
    final static Short STORAGE_NODE_TYPE_DIR_PROJECT = 11;
    final static Short STORAGE_NODE_TYPE_DIR_TASK = 12;
    final static Short STORAGE_NODE_TYPE_DIR_ORG = 13;
    final static Short STORAGE_NODE_TYPE_DIR_NOTICE = 14;
    final static Short STORAGE_NODE_TYPE_DIR_EXP = 15;
    final static Short STORAGE_NODE_TYPE_DIR_BACK = 16;
    final static Short STORAGE_NODE_TYPE_DIR_RECYCLE = 17;
    final static Short STORAGE_NODE_TYPE_DIR_USER = 18;

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
    final static Short STORAGE_NODE_TYPE_FILE_MIN = STORAGE_NODE_TYPE_FILE_MAIN;
    final static Short STORAGE_NODE_TYPE_FILE_MAX = STORAGE_NODE_TYPE_FILE_HIS;
    final static Short STORAGE_NODE_TYPE_DIR_MIN = STORAGE_NODE_TYPE_DIR_UNKNOWN;
    final static Short STORAGE_NODE_TYPE_DIR_MAX = STORAGE_NODE_TYPE_DIR_USER;

    //以下常量有可能被删除

}
