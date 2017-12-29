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
    final static Short STORAGE_NODE_TYPE_FILE_COMMIT = 2;
    final static Short STORAGE_NODE_TYPE_FILE_COMMIT_HIS = 3;
    final static Short STORAGE_NODE_TYPE_DIR_UNKNOWN = 10;
    final static Short STORAGE_NODE_TYPE_DIR_PROJECT = 11;
    final static Short STORAGE_NODE_TYPE_DIR_TASK = 12;
    final static Short STORAGE_NODE_TYPE_DIR_ORG = 13;
    final static Short STORAGE_NODE_TYPE_DIR_NOTICE = 14;
    final static Short STORAGE_NODE_TYPE_DIR_EXP = 15;
    final static Short STORAGE_NODE_TYPE_DIR_BACK = 16;
    final static Short STORAGE_NODE_TYPE_DIR_RECYCLE = 17;
    final static Short STORAGE_NODE_TYPE_DIR_USER = 18;
    final static Short STORAGE_NODE_TYPE_DIR_DESIGN = 20;
    final static Short STORAGE_NODE_TYPE_DIR_DESIGN_ISSUE = 21;
    final static Short STORAGE_NODE_TYPE_DIR_DESIGN_TASK = 22;
    final static Short STORAGE_NODE_TYPE_DIR_DESIGN_USER = 23;
    final static Short STORAGE_NODE_TYPE_DIR_COMMIT = 30;
    final static Short STORAGE_NODE_TYPE_DIR_COMMIT_TASK = 31;
    final static Short STORAGE_NODE_TYPE_DIR_COMMIT_HIS = 32;
    final static Short STORAGE_NODE_TYPE_DIR_COMMIT_USER = 33;
    final static Short STORAGE_NODE_TYPE_DIR_OUTPUT = 40;
    final static Short STORAGE_NODE_TYPE_DIR_OUTPUT_USER = 43;

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
    final static Short STORAGE_NODE_TYPE_FILE_MAX = STORAGE_NODE_TYPE_FILE_COMMIT_HIS;
    final static Short STORAGE_NODE_TYPE_DIR_MIN = STORAGE_NODE_TYPE_DIR_UNKNOWN;
    final static Short STORAGE_NODE_TYPE_DIR_MAX = STORAGE_NODE_TYPE_DIR_USER;

    //分类常量
    final static Short STORAGE_CLASSIC_TYPE_DESIGN = 0;
    final static Short STORAGE_CLASSIC_TYPE_COMMIT = 1;

    /** 历史动作类型 */
    final static Short STORAGE_ACTION_TYPE_CHECK = 0;
    final static Short STORAGE_ACTION_TYPE_AUDIT = 1;
    final static Short STORAGE_ACTION_TYPE_COMMIT = 2;

    static boolean isFileType(Short typeId) {
        return (STORAGE_NODE_TYPE_UNKNOWN.equals(typeId))
            || (STORAGE_NODE_TYPE_FILE_MAIN.equals(typeId))
            || (STORAGE_NODE_TYPE_FILE_COMMIT.equals(typeId))
            || (STORAGE_NODE_TYPE_FILE_COMMIT_HIS.equals(typeId));
    }

    static boolean isCommitType(Short typeId){
        return (STORAGE_NODE_TYPE_FILE_COMMIT.equals(typeId))
                || (STORAGE_NODE_TYPE_FILE_MAIN.equals(typeId))
                || (STORAGE_NODE_TYPE_FILE_COMMIT.equals(typeId))
                || (STORAGE_NODE_TYPE_FILE_COMMIT_HIS.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT_TASK.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT_HIS.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT_USER.equals(typeId));
    }

    static boolean isHistoryType(Short typeId){
        return (STORAGE_NODE_TYPE_FILE_COMMIT_HIS.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT_HIS.equals(typeId));
    }

    static boolean isSystemType(Short typeId){
        return (STORAGE_NODE_TYPE_DIR_PROJECT.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_TASK.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_ORG.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_NOTICE.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_EXP.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_BACK.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_RECYCLE.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_DESIGN.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_DESIGN_ISSUE.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_DESIGN_TASK.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT_TASK.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT_HIS.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_OUTPUT.equals(typeId));
    }

    static boolean isProjectType(Short typeId){
        return (STORAGE_NODE_TYPE_DIR_PROJECT.equals(typeId));
    }

    static boolean isIssueType(Short typeId) {
        return (STORAGE_NODE_TYPE_DIR_DESIGN_ISSUE.equals(typeId))
                || (STORAGE_NODE_TYPE_DIR_COMMIT_TASK.equals(typeId));
    }

    static boolean isTaskType(Short typeId) {
        return (STORAGE_NODE_TYPE_DIR_DESIGN_TASK.equals(typeId));
    }

    static Short getPathType(Short typeId){
        if (STORAGE_NODE_TYPE_UNKNOWN.equals(typeId)) return STORAGE_NODE_TYPE_DIR_DESIGN_USER;
        else if (STORAGE_NODE_TYPE_FILE_MAIN.equals(typeId)) return STORAGE_NODE_TYPE_DIR_DESIGN_USER;
        else if (STORAGE_NODE_TYPE_FILE_COMMIT.equals(typeId)) return STORAGE_NODE_TYPE_DIR_COMMIT_USER;
        else if (STORAGE_NODE_TYPE_FILE_COMMIT_HIS.equals(typeId)) return STORAGE_NODE_TYPE_DIR_COMMIT_HIS;
        else if (STORAGE_NODE_TYPE_DIR_UNKNOWN.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_PROJECT.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_TASK.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_ORG.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_NOTICE.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_EXP.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_BACK.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_RECYCLE.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_USER.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN.equals(typeId)) return STORAGE_NODE_TYPE_DIR_DESIGN_USER;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_ISSUE.equals(typeId)) return STORAGE_NODE_TYPE_DIR_DESIGN_USER;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_TASK.equals(typeId)) return STORAGE_NODE_TYPE_DIR_DESIGN_USER;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_USER.equals(typeId)) return STORAGE_NODE_TYPE_DIR_DESIGN_USER;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT.equals(typeId)) return STORAGE_NODE_TYPE_DIR_COMMIT_USER;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_TASK.equals(typeId)) return STORAGE_NODE_TYPE_DIR_COMMIT_USER;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_HIS.equals(typeId)) return STORAGE_NODE_TYPE_DIR_COMMIT_HIS;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_USER.equals(typeId)) return STORAGE_NODE_TYPE_DIR_COMMIT_USER;
        else if (STORAGE_NODE_TYPE_DIR_OUTPUT.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else if (STORAGE_NODE_TYPE_DIR_OUTPUT_USER.equals(typeId)) return STORAGE_NODE_TYPE_DIR_USER;
        else return STORAGE_NODE_TYPE_DIR_USER;
    }

    static String getTypeName(Short typeId){
        if (STORAGE_NODE_TYPE_UNKNOWN.equals(typeId)) return "未知类型";
        else if (STORAGE_NODE_TYPE_FILE_MAIN.equals(typeId)) return "设计文件";
        else if (STORAGE_NODE_TYPE_FILE_COMMIT.equals(typeId)) return "提资资料";
        else if (STORAGE_NODE_TYPE_FILE_COMMIT_HIS.equals(typeId)) return "历史版本";
        else if (STORAGE_NODE_TYPE_DIR_UNKNOWN.equals(typeId)) return "未知类型目录";
        else if (STORAGE_NODE_TYPE_DIR_PROJECT.equals(typeId)) return "项目目录";
        else if (STORAGE_NODE_TYPE_DIR_TASK.equals(typeId)) return "任务目录";
        else if (STORAGE_NODE_TYPE_DIR_ORG.equals(typeId)) return "组织目录";
        else if (STORAGE_NODE_TYPE_DIR_NOTICE.equals(typeId)) return "通告目录";
        else if (STORAGE_NODE_TYPE_DIR_EXP.equals(typeId)) return "报销目录";
        else if (STORAGE_NODE_TYPE_DIR_BACK.equals(typeId)) return "备份目录";
        else if (STORAGE_NODE_TYPE_DIR_RECYCLE.equals(typeId)) return "回收站目录";
        else if (STORAGE_NODE_TYPE_DIR_USER.equals(typeId)) return "用户目录";
        else if (STORAGE_NODE_TYPE_DIR_DESIGN.equals(typeId)) return "设计目录";
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_ISSUE.equals(typeId)) return "设计签发任务目录";
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_TASK.equals(typeId)) return "设计生产任务目录";
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_USER.equals(typeId)) return "设计自定义目录";
        else if (STORAGE_NODE_TYPE_DIR_COMMIT.equals(typeId)) return "提资目录";
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_TASK.equals(typeId)) return "提资任务目录";
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_HIS.equals(typeId)) return "提资历史目录";
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_USER.equals(typeId)) return "提资自定义目录";
        else if (STORAGE_NODE_TYPE_DIR_OUTPUT.equals(typeId)) return "成果根目录";
        else if (STORAGE_NODE_TYPE_DIR_OUTPUT_USER.equals(typeId)) return "成果子目录";
        else return "";
    }

    static Short getFileType(Short typeId){
        if (STORAGE_NODE_TYPE_UNKNOWN.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_FILE_MAIN.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_FILE_COMMIT.equals(typeId)) return STORAGE_NODE_TYPE_FILE_COMMIT;
        else if (STORAGE_NODE_TYPE_FILE_COMMIT_HIS.equals(typeId)) return STORAGE_NODE_TYPE_FILE_COMMIT_HIS;
        else if (STORAGE_NODE_TYPE_DIR_UNKNOWN.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_PROJECT.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_TASK.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_ORG.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_NOTICE.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_EXP.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_BACK.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_RECYCLE.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_USER.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_ISSUE.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_TASK.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_DESIGN_USER.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT.equals(typeId)) return STORAGE_NODE_TYPE_FILE_COMMIT;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_TASK.equals(typeId)) return STORAGE_NODE_TYPE_FILE_COMMIT;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_HIS.equals(typeId)) return STORAGE_NODE_TYPE_FILE_COMMIT_HIS;
        else if (STORAGE_NODE_TYPE_DIR_COMMIT_USER.equals(typeId)) return STORAGE_NODE_TYPE_FILE_COMMIT;
        else if (STORAGE_NODE_TYPE_DIR_OUTPUT.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else if (STORAGE_NODE_TYPE_DIR_OUTPUT_USER.equals(typeId)) return STORAGE_NODE_TYPE_FILE_MAIN;
        else return STORAGE_NODE_TYPE_UNKNOWN;
    }
}
