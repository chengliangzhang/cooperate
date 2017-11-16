package com.maoding.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/16 11:07
 * 描    述 :
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    /** 判断字符串是否为空，视null和""都为空 */
    public static Boolean isEmpty(String s){
        return ((s == null) || s.trim().isEmpty());
    }

    /** 判断两个字符串是否相同，视null和""为相同字符串 */
    public static Boolean isSame(String a,String b){
        return (isEmpty(a) && isEmpty(b)) ||
                (!isEmpty(a) && !isEmpty(b) && (a.equals(b)));
    }

    /** 获取类似于-p 10000的参数值 */
    public static String getParam(String str, String prefix, String sp){
        if ((str == null) || (!str.contains(prefix))) return null;

        int pos1 = str.indexOf(prefix) + prefix.length();
        String r = str.substring(pos1).trim();
        int pos2 = r.indexOf(sp);
        return prefix + ((pos2 > -1) ? r.substring(0,pos2) : r);
    }
    public static String getParam(String str, String prefix){
        return getParam(str,prefix," ");
    }

    /** 替换类似于-p 10000的参数值 */
    public static String replaceParam(String str, String prefix, String sp,String replaceTo){
        if ((str == null) || (!str.contains(prefix))) return str;
        if (isEmpty(sp)) sp = " ";

        int pos1 = str.indexOf(prefix) + prefix.length();
        String r = str.substring(pos1).trim();
        int pos2 = pos1 + r.indexOf(sp) + sp.length();
        return str.substring(0,pos1) + " " + replaceTo + ((pos2 > -1) ? str.substring(pos2) : "");
    }
    public static String replaceParam(String str, String prefix, String replaceTo){
        return replaceParam(str,prefix," ",replaceTo);
    }

    /** 标准化路径（所有路径分隔都用"/"） */
    public static String formatPath(String path) {
        if (path == null) return null;
        return path.replaceAll("\\\\", "/").trim();
    }

    /** 获取文件名 */
    public static String getFileName(String path){
        if (path == null) return null;
        path = formatPath(path);
        return path.substring(path.lastIndexOf("/")+1);
    }

    /** 获取路径名 */
    public static String getDirName(String path){
        if (path == null) return null;
        path = formatPath(path);
        return path.substring(0,path.lastIndexOf("/"));
    }
}
