package com.maoding.Common;

import com.maoding.Common.Dao.ConstDao;
import com.maoding.Common.Entity.ConstEntity;
import com.maoding.Common.zeroc.IdNameDTO;
import com.maoding.Common.zeroc.StringElementDTO;
import com.maoding.Utils.SpringUtils;
import com.maoding.Utils.StringUtils;

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
    public static final Short STORAGE_RANGE_TYPE_COMMIT = 2;

    /** 历史动作类型 */
    public static final Short STORAGE_ACTION_TYPE_UNKOWN = 0;
    public static final Short STORAGE_ACTION_TYPE_BACKUP = 1;
    public static final Short STORAGE_ACTION_TYPE_CHECK = 2;
    public static final Short STORAGE_ACTION_TYPE_AUDIT = 3;
    public static final Short STORAGE_ACTION_TYPE_COMMIT = 4;
    public static final Short STORAGE_ACTION_TYPE_ISSUE = 5;

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

    public static final Integer POS_IS_DIRECTORY = 0;
    public static final Integer POS_IS_PROJECT = 1;
    public static final Integer POS_IS_TASK = 2;
    public static final Integer POS_IS_DESIGN = 3;
    public static final Integer POS_IS_COMMIT = 4;
    public static final Integer POS_IS_HISTORY = 5;

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
                vMap.put(e.getValueId(),e);
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
                dto.setName(entry.getValue().getContent());
                list.add(dto);
            }
        }
        return list;
    }

    public static ConstEntity getConstEntity(Short classicId, Short valueId){
        Map<Short,ConstEntity> vMap = getConstMap(classicId);
        return (vMap == null) ? null : vMap.get(valueId);
    }

    public static Short getMaxValueId(Short classicId){
        Map<Short,ConstEntity> vMap = getConstMap(classicId);
        Short v = 0;
        if (vMap != null) {
            for(Map.Entry<Short,ConstEntity> entry : vMap.entrySet()){
                if (entry.getKey() > v) v = entry.getKey(); 
            }
        }
        return v;
    }

    public static String getContent(Short classicId, Short valueId){
        ConstEntity e = getConstEntity(classicId,valueId);
        return (e != null) ? e.getContent() : null;
    }

    public static String getExtra(Short classicId, Short valueId){
        ConstEntity e = getConstEntity(classicId,valueId);
        return (e != null) ? e.getContentExtra() : null;
    }

    public static List<IdNameDTO> listMajor(){
        return listName(CLASSIC_TYPE_MAJOR);
    }

    public static List<IdNameDTO> listAction(){
        return listName(CLASSIC_TYPE_ACTION);
    }

    public static String getExtraField(Short classicId, Short valueId, Integer n){
        String extra = getExtra(classicId,valueId);
        String field = null;
        if (!StringUtils.isEmpty(extra)){
            assert (extra != null);
            String[] extraElement = extra.split(ConstService.SPLIT_EXTRA);
            if (n < extraElement.length){
                field = extraElement[n];
            }
        }
        return field;
    }

    public static String getRangeName(Short rangeId){
        return getContent(CLASSIC_TYPE_STORAGE_RANGE,rangeId);
    }

    public static Short getRangeId(Short typeId){
        Map<Short,ConstEntity> rangeMap = getConstMap(CLASSIC_TYPE_STORAGE_RANGE);
        for (Map.Entry<Short,ConstEntity> rangeEntry : rangeMap.entrySet()){
            ConstEntity range = rangeEntry.getValue();
            if (range != null){
                String extra = range.getContentExtra();
                if ((extra != null) && (extra.contains(":" + typeId + ":"))) return range.getValueId();
            }
        }
        return STORAGE_RANGE_TYPE_UNKNOWN;
    }

    public static String getActionName(Short actionTypeId){
        return getContent(CLASSIC_TYPE_ACTION,actionTypeId);
    }

    public static String getTopicPrefix(Short noticeTypeId) {
        String sField = getNoticeTopic(noticeTypeId);
        if (StringUtils.isEmpty(sField)) return "";
        return sField.contains("{") ? sField.substring(0,sField.indexOf("{")) : sField;
    }
    public static String getNoticeTopic(Short noticeTypeId) {
        return getExtraField(CLASSIC_TYPE_NOTICE_TYPE,noticeTypeId,0);
    }

    public static String getNoticeTitle(Short noticeTypeId) {
        return getExtraField(CLASSIC_TYPE_NOTICE_TYPE,noticeTypeId,1);
    }

    public static String getNoticeContent(Short noticeTypeId) {
        return getExtraField(CLASSIC_TYPE_NOTICE_TYPE,noticeTypeId,2);
    }

    public static Short getActionNodeTypeId(@NotNull Short actionTypeId){
        String sField = getExtraField(CLASSIC_TYPE_ACTION,actionTypeId,0);
        if (StringUtils.isEmpty(sField)) return STORAGE_NODE_TYPE_UNKNOWN;
        else return Short.parseShort(StringUtils.left(sField,":"));
    }

    public static String getActionNodePath(@NotNull Short actionTypeId){
        String sField = getExtraField(CLASSIC_TYPE_ACTION,actionTypeId,0);
        return StringUtils.right(sField,":");
    }

    public static String getActionNodePath(@NotNull Short actionTypeId, @NotNull StringElementDTO stringElement){
        return StringUtils.formatPath(convertString(getActionNodePath(actionTypeId),stringElement));
    }

    public static Short getActionFileServerTypeId(@NotNull Short actionTypeId){
        String sField = getExtraField(CLASSIC_TYPE_ACTION,actionTypeId,1);
        if (StringUtils.isEmpty(sField)) return FILE_SERVER_TYPE_UNKNOWN;
        else return Short.parseShort(StringUtils.left(sField,":"));
    }

    public static String getActionFileServerAddress(@NotNull Short actionTypeId){
        String sField = getExtraField(CLASSIC_TYPE_ACTION,actionTypeId,1);
        return StringUtils.right(sField,":");
    }

    public static String getActionFileServerAddress(@NotNull Short actionTypeId, @NotNull StringElementDTO stringElement){
        return convertString(getActionFileServerAddress(actionTypeId),stringElement);
    }

    public static String getActionNoticeTypeIdString(Short actionTypeId){
        String sField = getExtraField(CLASSIC_TYPE_ACTION,actionTypeId,2);
        return (!StringUtils.isEmpty(sField)) ? sField : null;
    }


    public static boolean isBackupAction(Short actionTypeId){
        return STORAGE_ACTION_TYPE_BACKUP.equals(actionTypeId);
    }

    public static String convertString(String s, @NotNull StringElementDTO stringElement){
        if (s == null) return null;

        final String PROJECT_ID = "{ProjectId}";
        final String PROJECT_NAME = "{Project}";
        final String CLASSIC_NAME = "{Classic}";
        final String ISSUE_PATH = "{IssuePath}";
        final String TASK_ID = "{TaskId}";
        final String TASK_PATH = "{TaskPath}";
        final String USER_ID = "{UserId}";
        final String USER_NAME = "{User}";
        final String COMPANY_ID = "{CompanyId}";
        final String COMPANY_NAME = "{Company}";
        final String MAJOR_NAME = "{Major}";
        final String VERSION_NAME = "{Version}";
        final String ACTION_NAME = "{Action}";
        final String SRC_PATH = "{SrcPath}";
        final String SRC_DIR = "{SrcDir}";
        final String SRC_NAME = "{SrcFile}";
        final String SRC_NAME_NO_EXT = "{SrcFileNoExt}";
        final String SRC_EXT = "{Ext}";
        final String V_TIME_STAMP_START = "{Time:";
        final String V_CLASSIC_NAME_START = "{Classic";
        final String V_ACTION_NAME_START = "{Action";

        s = StringUtils.replace(s,PROJECT_ID,stringElement.getProjectId());
        s = StringUtils.replace(s,PROJECT_NAME,stringElement.getProjectName());
        s = StringUtils.replace(s,CLASSIC_NAME,stringElement.getClassicName());
        s = StringUtils.replace(s,ISSUE_PATH,stringElement.getIssuePath());
        s = StringUtils.replace(s,TASK_ID,stringElement.getTaskId());
        s = StringUtils.replace(s,TASK_PATH,stringElement.getTaskPath());
        s = StringUtils.replace(s,COMPANY_ID,stringElement.getCompanyId());
        s = StringUtils.replace(s,COMPANY_NAME,stringElement.getCompanyName());
        s = StringUtils.replace(s,USER_ID,stringElement.getUserId());
        s = StringUtils.replace(s,USER_NAME,stringElement.getUserName());
        s = StringUtils.replace(s,VERSION_NAME,stringElement.getFileVersion());
        s = StringUtils.replace(s,MAJOR_NAME,stringElement.getMajorName());
        s = StringUtils.replace(s,ACTION_NAME,stringElement.getActionName());
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
            s = StringUtils.replace(s,V_CLASSIC_NAME_START + vClassic + V_END,getRangeName(Short.parseShort(vClassic)));
        }
        if (s.contains(V_ACTION_NAME_START)){
            String vAction = s.substring(s.indexOf(V_ACTION_NAME_START) + V_ACTION_NAME_START.length(),s.indexOf(V_END,s.indexOf(V_CLASSIC_NAME_START)));
            s = StringUtils.replace(s,V_ACTION_NAME_START + vAction + V_END,getActionName(Short.parseShort(vAction)));
        }
        return s;
    }

    public static boolean isDirectoryType(Short typeId) {
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (StringUtils.isEmpty(extra)) return false;
        Character t = extra.charAt(POS_IS_DIRECTORY);
        return (t != '0');
    }

    public static boolean isProjectType(Short typeId) {
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (StringUtils.isEmpty(extra)) return false;
        Character t = extra.charAt(POS_IS_PROJECT);
        return (t != '0');
    }

    public static boolean isTaskType(Short typeId) {
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (StringUtils.isEmpty(extra)) return false;
        Character t = extra.charAt(POS_IS_TASK);
        return (t != '0');
    }

    public static boolean isDesignType(Short typeId) {
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (StringUtils.isEmpty(extra)) return false;
        Character t = extra.charAt(POS_IS_DESIGN);
        return (t != '0');
    }

    public static boolean isCommitType(Short typeId){
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (StringUtils.isEmpty(extra)) return false;
        Character t = extra.charAt(POS_IS_COMMIT);
        return (t != '0');
    }

    public static boolean isHistoryType(Short typeId){
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (StringUtils.isEmpty(extra)) return false;
        Character t = extra.charAt(POS_IS_HISTORY);
        return (t != '0');
    }

    public static boolean isSystemType(Short typeId){
        return isProjectType(typeId) || isTaskType(typeId);
    }

    public static String getTypeName(Short typeId){
        return getContent(CLASSIC_TYPE_STORAGE_NODE,typeId);
    }

    public static Short getPathType(Short typeId){
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (extra == null) return STORAGE_NODE_TYPE_DIR_UNKNOWN;
        String s = extra.substring(extra.indexOf("[")+1, extra.lastIndexOf("]"));
        return StringUtils.isEmpty(s) ? STORAGE_NODE_TYPE_DIR_UNKNOWN : Short.parseShort(s);
    }

    public static Short getFileType(Short typeId){
        String extra = getExtra(CLASSIC_TYPE_STORAGE_NODE,typeId);
        if (extra == null) return STORAGE_NODE_TYPE_UNKNOWN;
        String s = extra.substring(extra.indexOf("<")+1, extra.lastIndexOf(">"));
        return StringUtils.isEmpty(s) ? STORAGE_NODE_TYPE_UNKNOWN : Short.parseShort(s);
    }

}
