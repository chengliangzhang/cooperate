package com.maoding.Common;

import com.maoding.Common.Dao.ConstDao;
import com.maoding.Common.Entity.ConstEntity;
import com.maoding.Common.zeroc.IdNameDTO;
import com.maoding.Common.zeroc.StringElementDTO;
import com.maoding.CoreUtils.DigitUtils;
import com.maoding.CoreUtils.SpringUtils;
import com.maoding.CoreUtils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/12 19:21
 * 描    述 :
 */
public class ConstService {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(ConstService.class);

    public static final Short CLASSIC_TYPE_CONST = 0;
    public static final Short CLASSIC_TYPE_RIGHT = 1;
    public static final Short CLASSIC_TYPE_COOPERATOR = 2;
    public static final Short CLASSIC_TYPE_TASK = 3;
    public static final Short CLASSIC_TYPE_FEE = 4;
    public static final Short CLASSIC_TYPE_NODE = 6;
    public static final Short CLASSIC_TYPE_LOG = 7;
    public static final Short CLASSIC_TYPE_WORK = 8;
    public static final Short CLASSIC_TYPE_INVITE = 9;
    public static final Short CLASSIC_TYPE_NOTICE = 10;
    public static final Short CLASSIC_TYPE_USER = 12;
    public static final Short CLASSIC_TYPE_COMPANY = 13;
    public static final Short CLASSIC_TYPE_STORAGE_NODE = 14;
    public static final Short CLASSIC_TYPE_LOCK = 15;
    public static final Short CLASSIC_TYPE_SYNC = 16;
    public static final Short CLASSIC_TYPE_DELETE = 17;
    public static final Short CLASSIC_TYPE_FILE_SERVER = 19;
    public static final Short CLASSIC_TYPE_ACTION = 20;
    public static final Short CLASSIC_TYPE_PNODE = 21;
    public static final Short CLASSIC_TYPE_MAJOR = 22;
    public static final Short CLASSIC_TYPE_ROLE_TASK = 23;
    public static final Short CLASSIC_TYPE_STORAGE_RANGE = 24;
    public static final Short CLASSIC_TYPE_ROLE = 25;
    public static final Short CLASSIC_TYPE_ROLE_TYPE = 26;
    public static final Short CLASSIC_TYPE_NOTICE_TYPE = 27;
    public static final Short CLASSIC_TYPE_WEB_PERMISSION_GROUP = 28; //web权限组类型
    public static final Short CLASSIC_TYPE_WEB_PERMISSION = 29; //web权限类型
    public static final Short CLASSIC_TYPE_WEB_ROLE = 30; //web member角色类型
    public static final Short CLASSIC_TYPE_ANNOTATE = 31; //校审意见类型
    public static final Short CLASSIC_TYPE_ANNOTATE_STATUS = 32; //校审意见状态类型

    //校审意见状态类型
    public static final Short ANNOTATE_STATUS_TYPE_UNKNOWN = 0; //未知状态
    public static final Short ANNOTATE_STATUS_TYPE_PASS = 1; //通过
    public static final Short ANNOTATE_STATUS_TYPE_REFUSE = 2; //不通过

    //校审意见类型
    public static final Short ANNOTATE_TYPE_UNKNOWN = 0; //未知类型
    public static final Short ANNOTATE_TYPE_CHECK = 1; //校验
    public static final Short ANNOTATE_TYPE_AUDIT = 2; //审核

    //节点类型
    public static final Short STORAGE_NODE_TYPE_UNKNOWN = 0;
    public static final Short STORAGE_NODE_TYPE_FILE_MAIN = 1;
    public static final Short STORAGE_NODE_TYPE_FILE_COMMIT = 2;
    public static final Short STORAGE_NODE_TYPE_FILE_COMMIT_HIS = 3;
    public static final Short STORAGE_NODE_TYPE_DIR_UNKNOWN = 10;
    public static final Short STORAGE_NODE_TYPE_DIR_PROJECT = 11;
    public static final Short STORAGE_NODE_TYPE_DIR_TASK = 12;
    public static final Short STORAGE_NODE_TYPE_DIR_ORG = 13;
    public static final Short STORAGE_NODE_TYPE_DIR_NOTICE = 14;
    public static final Short STORAGE_NODE_TYPE_DIR_EXP = 15;
    public static final Short STORAGE_NODE_TYPE_DIR_BACK = 16;
    public static final Short STORAGE_NODE_TYPE_DIR_RECYCLE = 17;
    public static final Short STORAGE_NODE_TYPE_DIR_USER = 18;
    public static final Short STORAGE_NODE_TYPE_DIR_DESIGN = 20;
    public static final Short STORAGE_NODE_TYPE_DIR_DESIGN_ISSUE = 21;
    public static final Short STORAGE_NODE_TYPE_DIR_DESIGN_TASK = 22;
    public static final Short STORAGE_NODE_TYPE_DIR_DESIGN_USER = 23;
    public static final Short STORAGE_NODE_TYPE_DIR_COMMIT = 30;
    public static final Short STORAGE_NODE_TYPE_DIR_COMMIT_TASK = 31;
    public static final Short STORAGE_NODE_TYPE_DIR_COMMIT_HIS = 32;
    public static final Short STORAGE_NODE_TYPE_DIR_COMMIT_USER = 33;
    public static final Short STORAGE_NODE_TYPE_DIR_OUTPUT = 40;
    public static final Short STORAGE_NODE_TYPE_DIR_OUTPUT_WEB = 41;
    public static final Short STORAGE_NODE_TYPE_DIR_OUTPUT_WEB_ARCHIVE = 42;
    public static final Short STORAGE_NODE_TYPE_DIR_OUTPUT_USER = 43;

    //文件类型
    public static final Short STORAGE_FILE_TYPE_UNKNOWN = 0;
    public static final Short STORAGE_FILE_TYPE_CAD = 1;
    public static final Short STORAGE_FILE_TYPE_CONTRACT = 3;
    public static final Short STORAGE_FILE_TYPE_LOGO = 4;
    public static final Short STORAGE_FILE_TYPE_AUTH = 5;
    public static final Short STORAGE_FILE_TYPE_APP = 6;
    public static final Short STORAGE_FILE_TYPE_INVITE = 7;
    public static final Short STORAGE_FILE_TYPE_LICENCE = 8;
    public static final Short STORAGE_FILE_TYPE_LEGAL = 9;
    public static final Short STORAGE_FILE_TYPE_EXP = 20;
    public static final Short STORAGE_FILE_TYPE_NOTICE = 21;
    public static final Short STORAGE_FILE_TYPE_MIRROR = 22;

    //默认值
    public static final Short STORAGE_NODE_TYPE_FILE_MIN = STORAGE_NODE_TYPE_FILE_MAIN;
    public static final Short STORAGE_NODE_TYPE_FILE_MAX = STORAGE_NODE_TYPE_FILE_COMMIT_HIS;
    public static final Short STORAGE_NODE_TYPE_DIR_MIN = STORAGE_NODE_TYPE_DIR_UNKNOWN;
    public static final Short STORAGE_NODE_TYPE_DIR_MAX = STORAGE_NODE_TYPE_DIR_USER;

    //分类常量
    public static final Short STORAGE_RANGE_TYPE_UNKNOWN = 0;
    public static final Short STORAGE_RANGE_TYPE_DESIGN = 1;
    public static final Short STORAGE_RANGE_TYPE_CA = 2;
    public static final Short STORAGE_RANGE_TYPE_COMMIT = 3;

    /** 历史动作类型 */
    public static final Short STORAGE_ACTION_TYPE_UNKOWN = 0;
    public static final Short STORAGE_ACTION_TYPE_BACKUP = 1;
    public static final Short STORAGE_ACTION_TYPE_CHECK = 2;
    public static final Short STORAGE_ACTION_TYPE_AUDIT = 3;
    public static final Short STORAGE_ACTION_TYPE_COMMIT = 4;
    public static final Short STORAGE_ACTION_TYPE_ISSUE = 5;
    public static final Short STORAGE_ACTION_TYPE_ASK_CA = 6;

    /** 通知类型 */
    public static final Short NOTICE_TYPE_UNDEFINE = 0;
    public static final Short NOTICE_TYPE_USER = 1;
    public static final Short NOTICE_TYPE_TASK = 2;
    public static final Short NOTICE_TYPE_PROJECT = 3;
    public static final Short NOTICE_TYPE_COMPANY = 4;
    public static final Short NOTICE_TYPE_COMMON = 5;

    /** 文件服务器类型 */
    public static final Short FILE_SERVER_TYPE_UNKNOWN = 0;
    public static final Short FILE_SERVER_TYPE_DISK = 1;
    public static final Short FILE_SERVER_TYPE_WEB = 2;
    public static final Short FILE_SERVER_TYPE_FASTFDS = 3;
    public static final Short FILE_SERVER_TYPE_ALIYUN = 4;
    public static final Short FILE_SERVER_TYPE_CIFS = 5;
    public static final Short FILE_SERVER_TYPE_FTP = 6;
    public static final Short FILE_SERVER_TYPE_ICE = 7;

    /** web角色类型 */
    public static final Short WEB_ROLE_PROJECT_CREATOR = 0;
    public static final Short WEB_ROLE_PROJECT_ISSUE = 1;
    public static final Short WEB_ROLE_PROJECT_DESIGN = 2;
    public static final Short WEB_ROLE_TASK_RESPONSE = 3;
    public static final Short WEB_ROLE_TASK_DESIGN = 4;
    public static final Short WEB_ROLE_TASK_CHECK = 5;
    public static final Short WEB_ROLE_TASK_AUDIT = 6;


    public static final Integer POS_IS_DIRECTORY = 0;
    public static final Integer POS_IS_PROJECT = 1;
    public static final Integer POS_IS_TASK = 2;
    public static final Integer POS_IS_DESIGN = 3;
    public static final Integer POS_IS_COMMIT = 4;
    public static final Integer POS_IS_HISTORY = 5;

    public static final String MODE_TRUE = "1";
    public static final String MODE_FALSE = "0";
    public static final String MODE_BOTH = "0,1";

    public static final String SPLIT_EXTRA = ";";
    public static final String V_END = "}";

    private static ConstDao constDao = null;
    private static Map<Short,Map<Short,ConstEntity>> constMap = null;
    public static Map<Short,Map<Short,ConstEntity>> getConstMap(){
        if (constMap == null) {
            if (constDao == null) {
                constDao = SpringUtils.getBean(ConstDao.class);
            }
            assert (constDao != null);
            Map<Short,Map<Short,ConstEntity>> cMap = new HashMap<>();
            List<ConstEntity> constList = constDao.selectAll();
            for (ConstEntity e : constList){
                Map<Short, ConstEntity> vMap = cMap.computeIfAbsent(e.getClassicId(), k -> new HashMap<>());
                vMap.put(e.getCodeId(),e);
            }
            constMap = cMap;
        }
        return constMap;
    }
    
    public static Map<Short,ConstEntity> getConstMap(Short classicId){
        return getConstMap().get(classicId);
    }

    public static List<IdNameDTO> listName(Short classicId){
        List<IdNameDTO> list = new ArrayList<>();
        Map<Short,ConstEntity> vMap = getConstMap(classicId);
        if (vMap != null) {
            for(Map.Entry<Short,ConstEntity> entry : vMap.entrySet()){
                IdNameDTO dto = new IdNameDTO();
                dto.setId(entry.getKey().toString());
                dto.setName(entry.getValue().getTitle());
                list.add(dto);
            }
        }
        return list;
    }

    public static ConstEntity getConstEntity(Short classicId, Short valueId){
        Map<Short,ConstEntity> vMap = getConstMap(classicId);
        return (vMap == null) ? null : vMap.get((valueId != null) ? valueId : 0);
    }

    public static Short getMaxCodeId(Short classicId){
        Map<Short,ConstEntity> vMap = getConstMap(classicId);
        Short v = 0;
        if (vMap != null) {
            for(Map.Entry<Short,ConstEntity> entry : vMap.entrySet()){
                if (entry.getKey() > v) v = entry.getKey(); 
            }
        }
        return v;
    }

    public static String getTitle(Short classicId, Short codeId){
        return getTitle(classicId,codeId,1);
    }

    public static String getTitle(Short classicId, Short codeId, int n){
        return getTitle(classicId,codeId,null,n);
    }

    public static String getTitle(Short classicId, Short codeId, StringElementDTO stringElement){
        return getTitle(classicId,codeId,null,1);
    }

    public static String getTitle(Short classicId, Short codeId, StringElementDTO stringElement, int n){
        String s = null;
        ConstEntity e = getConstEntity(classicId,codeId);
        if (e != null){
            s = e.getTitle(n);
            if (stringElement != null) {
                s = convertString(s,stringElement);
            }
        }
        return s;
    }

    public static String getExtra(Short classicId, Short codeId){
        return getExtra(classicId,codeId,1);
    }

    public static String getExtra(Short classicId, Short codeId, int n){
        return getExtra(classicId,codeId,null,n);
    }

    public static String getExtra(Short classicId, Short codeId, StringElementDTO stringElement){
        return getExtra(classicId,codeId,stringElement,1);
    }

    public static String getExtra(Short classicId, Short codeId, StringElementDTO stringElement, int n){
        String s = null;
        ConstEntity e = getConstEntity(classicId,codeId);
        if (e != null){
            s = e.getExtra(n);
            if (stringElement != null) {
                s = convertString(s,stringElement);
            }
        }
        return s;
    }

    public static boolean isSpecial(String attr, int index){
        return (attr != null) && (attr.length() > index) && (attr.charAt(index) != '0');
    }

    public static List<IdNameDTO> listMajor(){
        return listName(CLASSIC_TYPE_MAJOR);
    }

    public static List<IdNameDTO> listAction(){
        return listName(CLASSIC_TYPE_ACTION);
    }

    public static String getRangeName(Short rangeId){
        return getTitle(CLASSIC_TYPE_STORAGE_RANGE,rangeId);
    }

    public static String getRangeId(@NotNull String typeId){
        Short rangeId = null;
        Map<Short, ConstEntity> rangeMap = getConstMap(CLASSIC_TYPE_STORAGE_RANGE);
        for (Map.Entry<Short, ConstEntity> rangeEntry : rangeMap.entrySet()) {
            ConstEntity range = rangeEntry.getValue();
            if (range != null) {
                String typeIds = range.getExtra(3);
                if ((typeIds != null) && (typeIds.contains(typeId))) {
                    rangeId = range.getCodeId();
                    break;
                }
            }
        }
        return (rangeId != null) ? rangeId.toString() : STORAGE_RANGE_TYPE_UNKNOWN.toString();
    }

    public static String getActionName(Short actionTypeId){
        return getTitle(CLASSIC_TYPE_ACTION,actionTypeId);
    }

    public static String getTopicPrefix(Short noticeTypeId) {
        String sField = getNoticeTopic(noticeTypeId);
        if (StringUtils.isEmpty(sField)) return "";
        return sField.contains("{") ? sField.substring(0,sField.indexOf("{")) : sField;
    }

    public static String getNoticeTopic(Short noticeTypeId) {
        return getNoticeTopic(noticeTypeId,null);
    }

    public static String getNoticeTopic(Short noticeTypeId, StringElementDTO stringElement) {
        return getExtra(CLASSIC_TYPE_NOTICE_TYPE,noticeTypeId,stringElement,1);
    }

    public static String getNoticeTitle(Short noticeTypeId) {
        return getExtra(CLASSIC_TYPE_NOTICE_TYPE,noticeTypeId,2);
    }

    public static String getNoticeContent(Short noticeTypeId) {
        return getNoticeContent(noticeTypeId,null);
    }

    public static String getNoticeContent(Short noticeTypeId, StringElementDTO stringElement) {
        return getExtra(CLASSIC_TYPE_NOTICE_TYPE,noticeTypeId, stringElement,3);
    }

    public static Short getActionNodeTypeId(@NotNull Short actionTypeId){
        return DigitUtils.parseShort(getExtra(CLASSIC_TYPE_ACTION,actionTypeId,2));
    }

    public static String getActionNodePath(@NotNull Short actionTypeId){
        return getActionNodePath(actionTypeId,null);
    }

    public static String getActionNodePath(@NotNull Short actionTypeId, StringElementDTO stringElement){
        return getExtra(CLASSIC_TYPE_ACTION,actionTypeId,stringElement,3);
    }

    public static Short getActionFileServerTypeId(@NotNull Short actionTypeId){
        return DigitUtils.parseShort(getExtra(CLASSIC_TYPE_ACTION,actionTypeId,4));
    }

    public static String getActionFileServerAddress(@NotNull Short actionTypeId){
        return getActionFileServerAddress(actionTypeId,null);
    }

    public static String getActionFileServerAddress(@NotNull Short actionTypeId, @NotNull StringElementDTO stringElement){
        return getExtra(CLASSIC_TYPE_ACTION,actionTypeId,stringElement,5);
    }

    public static String getActionFileServerBaseDir(@NotNull Short actionTypeId){
        return getActionFileServerBaseDir(actionTypeId,null);
    }

    public static String getActionFileServerBaseDir(@NotNull Short actionTypeId, @NotNull StringElementDTO stringElement){
        return getExtra(CLASSIC_TYPE_ACTION,actionTypeId,stringElement,6);
    }

    public static String getActionNoticeTypeIdString(Short actionTypeId){
        return getExtra(CLASSIC_TYPE_ACTION,actionTypeId,7);
    }


    public static boolean isBackupAction(Short actionTypeId){
        return STORAGE_ACTION_TYPE_BACKUP.equals(actionTypeId);
    }

    public static String convertString(String s, @NotNull StringElementDTO stringElement){
        if (s == null) return null;

        final String PROJECT_ID = "{ProjectId}";
        final String PROJECT_NAME = "{Project}";
        final String RANGE_ID = "{RangeId}";
        final String RANGE_NAME = "{Range}";
        final String ISSUE_PATH = "{IssuePath}";
        final String TASK_ID = "{TaskId}";
        final String TASK_NAME = "{Task}";
        final String TASK_PATH = "{TaskPath}";
        final String USER_ID = "{UserId}";
        final String USER_NAME = "{User}";
        final String OWNER_USER_ID = "{OwnerUserId}";
        final String OWNER_NAME = "{OwnerUserName}";
        final String COMPANY_ID = "{CompanyId}";
        final String COMPANY_NAME = "{Company}";
        final String MAJOR_NAME = "{Major}";
        final String VERSION_NAME = "{Version}";
        final String ACTION_NAME = "{Action}";
        final String SKY_PID = "{SkyPid}";
        final String SRC_PATH = "{SrcPath}";
        final String SRC_DIR = "{SrcDir}";
        final String SRC_NAME = "{SrcFile}";
        final String SRC_NAME_NO_EXT = "{SrcFileNoExt}";
        final String SRC_EXT = "{Ext}";
        final String V_TIME_STAMP_START = "{Time:";
        final String V_CLASSIC_NAME_START = "{Range";
        final String V_ACTION_NAME_START = "{Action";

        s = StringUtils.replace(s,PROJECT_ID,stringElement.getProjectId());
        s = StringUtils.replace(s,PROJECT_NAME,stringElement.getProjectName());
        s = StringUtils.replace(s,RANGE_ID,stringElement.getRangeId());
        s = StringUtils.replace(s,RANGE_NAME,stringElement.getRangeName());
        s = StringUtils.replace(s,ISSUE_PATH,stringElement.getIssuePath());
        s = StringUtils.replace(s,TASK_ID,stringElement.getTaskId());
        s = StringUtils.replace(s,TASK_NAME,stringElement.getTaskName());
        s = StringUtils.replace(s,TASK_PATH,stringElement.getTaskPath());
        s = StringUtils.replace(s,COMPANY_ID,stringElement.getCompanyId());
        s = StringUtils.replace(s,COMPANY_NAME,stringElement.getCompanyName());
        s = StringUtils.replace(s,USER_ID,stringElement.getUserId());
        s = StringUtils.replace(s,USER_NAME,stringElement.getUserName());
        s = StringUtils.replace(s,OWNER_USER_ID,stringElement.getOwnerUserId());
        s = StringUtils.replace(s,OWNER_NAME,stringElement.getOwnerUserName());
        s = StringUtils.replace(s,VERSION_NAME,stringElement.getFileVersion());
        s = StringUtils.replace(s,MAJOR_NAME,stringElement.getMajorName());
        s = StringUtils.replace(s,ACTION_NAME,stringElement.getActionName());
        s = StringUtils.replace(s,SKY_PID,stringElement.getSkyPid());
        s = StringUtils.replace(s,SRC_PATH,stringElement.getSrcPath());
        s = StringUtils.replace(s,SRC_DIR,StringUtils.getDirName(stringElement.getSrcPath()));
        s = StringUtils.replace(s,SRC_NAME,StringUtils.getFileName(stringElement.getSrcPath()));
        s = StringUtils.replace(s,SRC_NAME_NO_EXT,StringUtils.getFileNameWithoutExt(stringElement.getSrcPath()));
        s = StringUtils.replace(s,SRC_EXT,StringUtils.getFileExt(stringElement.getSrcPath()));

        if (s.contains(V_TIME_STAMP_START)){
            String fmt = s.substring(s.indexOf(V_TIME_STAMP_START) + V_TIME_STAMP_START.length(),s.indexOf(V_END,s.indexOf(V_TIME_STAMP_START)));
            String timeTxt = StringUtils.getTimeStamp(StringUtils.getStringOrDefault(fmt,StringUtils.DEFAULT_STAMP_FORMAT));
            s = StringUtils.replace(s,V_TIME_STAMP_START + fmt + V_END,timeTxt);
        }
        if (s.contains(V_CLASSIC_NAME_START)){
            String vClassic = s.substring(s.indexOf(V_CLASSIC_NAME_START) + V_CLASSIC_NAME_START.length(),s.indexOf(V_END,s.indexOf(V_CLASSIC_NAME_START)));
            s = StringUtils.replace(s,V_CLASSIC_NAME_START + vClassic + V_END,getRangeName(DigitUtils.parseShort(vClassic)));
        }
        if (s.contains(V_ACTION_NAME_START)){
            String vAction = s.substring(s.indexOf(V_ACTION_NAME_START) + V_ACTION_NAME_START.length(),s.indexOf(V_END,s.indexOf(V_CLASSIC_NAME_START)));
            s = StringUtils.replace(s,V_ACTION_NAME_START + vAction + V_END,getActionName(DigitUtils.parseShort(vAction)));
        }
        return s;
    }

    public static boolean isUnknownDirectoryType(@NotNull String typeId){
        return (isCustomType(typeId)) || (STORAGE_NODE_TYPE_DIR_UNKNOWN.equals(DigitUtils.parseShort(typeId)));
    }

    public static boolean isUnknownFileType(@NotNull String typeId){
        return (isCustomType(typeId)) || (STORAGE_NODE_TYPE_UNKNOWN.equals(DigitUtils.parseShort(typeId)));
    }

    public static boolean isCustomType(@NotNull String typeId){
        final Integer CUSTOM_TYPE_LENGTH = 32;
        return typeId.length() >= CUSTOM_TYPE_LENGTH;
    }


    public static boolean isDirectoryType(@NotNull String typeId) {
        return isSpecial(getExtra(CLASSIC_TYPE_STORAGE_NODE, DigitUtils.parseShort(typeId)),POS_IS_DIRECTORY);
    }

    public static boolean isProjectType(@NotNull String typeId) {
        return isSpecial(getExtra(CLASSIC_TYPE_STORAGE_NODE,DigitUtils.parseShort(typeId)),POS_IS_PROJECT);
    }

    public static boolean isTaskType(@NotNull String typeId) {
        return isSpecial(getExtra(CLASSIC_TYPE_STORAGE_NODE,DigitUtils.parseShort(typeId)),POS_IS_TASK);
    }

    public static boolean isDesignType(@NotNull String typeId) {
        return isSpecial(getExtra(CLASSIC_TYPE_STORAGE_NODE,DigitUtils.parseShort(typeId)),POS_IS_DESIGN);
    }

    public static boolean isCommitType(@NotNull String typeId){
        return isSpecial(getExtra(CLASSIC_TYPE_STORAGE_NODE,DigitUtils.parseShort(typeId)),POS_IS_COMMIT);
    }

    public static boolean isHistoryType(@NotNull String typeId){
        return isSpecial(getExtra(CLASSIC_TYPE_STORAGE_NODE,DigitUtils.parseShort(typeId)),POS_IS_HISTORY);
    }

    public static boolean isSystemType(@NotNull String typeId){
        return isProjectType(typeId) || isTaskType(typeId);
    }

    public static String getTypeName(@NotNull String typeId){
        try {
            return getTitle(CLASSIC_TYPE_STORAGE_NODE, DigitUtils.parseShort(typeId));
        } catch (NumberFormatException e) {
            log.warn("节点类型存在问题");
            return getTitle(CLASSIC_TYPE_STORAGE_NODE, STORAGE_NODE_TYPE_UNKNOWN);
        }
    }

    public static String getPathType(@NotNull String typeId){
        return getExtra(CLASSIC_TYPE_STORAGE_NODE, DigitUtils.parseShort(typeId), 2);
    }

    public static String getFileType(@NotNull String typeId){
        return getExtra(CLASSIC_TYPE_STORAGE_NODE, DigitUtils.parseShort(typeId), 3);
    }
}
