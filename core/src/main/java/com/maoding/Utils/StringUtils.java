package com.maoding.Utils;

import com.zeroc.Ice.Current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/16 11:07
 * 描    述 :
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    public static final String STAMP_FORMAT_FULL = "yyyyMMddHHmmss";
    public static final String DATA_STAMP_FORMAT = "yyyyMMdd";
    public static final String TIME_STAMP_FORMAT = "yyyyMMddHHmmss";
    public static final String MS_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.sss";
    public static final String DEFAULT_STAMP_FORMAT = STAMP_FORMAT_FULL;

    public static final String SPLIT_ID = ",";
    public static final String SPLIT_PATH = "/";

    private static final int KILO_BYTE = 1024;
    private static final int KILO_MS = 1000;

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

    /** 获取分隔符分隔的最后一个字符串 */
    public static String getLastSplit(String str,String split){
        if ((str == null) || (split == null)) return str;
        int pos = str.lastIndexOf(split);
        return (pos >= 0) ? str.substring(pos+split.length()) : str;
    }

    /** 获取文件名 */
    public static String getFileName(String path){
        if (path == null) return null;
        path = formatPath(path);
        return getLastSplit(path, SPLIT_PATH);
    }

    /** 获取路径名 */
    public static String getDirName(String path){
        if (path == null) return null;
        path = formatPath(path);
        int pos = path.lastIndexOf(SPLIT_PATH);
        if (pos < 0) return "";
        return path.substring(0,pos);
    }

    //带单位计算字节数
    public static String calBytes(long length){
        String unit = "B";
        if (length > KILO_BYTE){
            if ((length / KILO_BYTE) > KILO_BYTE) {
                length /= (KILO_BYTE * KILO_BYTE);
                unit = "M";
            } else {
                length /= KILO_BYTE;
                unit = "K";
            }
        }
        return length + unit;
    }

    //带单位计算字节传送速度
    public static String calSpeed(long length,long t,int digit){
        double speed = (length * KILO_MS) / t;
        String unit = "B/s";
        if (speed > KILO_BYTE){
            if ((speed / KILO_BYTE) > KILO_BYTE) {
                speed /= (KILO_BYTE * KILO_BYTE);
                unit = "M/s";
            } else {
                speed /= KILO_BYTE;
                unit = "K/s";
            }
        }
        return String.format("%." + digit + "f",speed) + unit;
    }
    public static String calSpeed(long length,long t){
        return calSpeed(length,t,1);
    }

    public static String getTimeStamp(Date date, String stampFormat){
        final String DEFAULT_STAMP_FORMAT = "yyyyMMddHHmmss";
        if (date == null) date = new Date();
        if (stampFormat == null) stampFormat = DEFAULT_STAMP_FORMAT;
        SimpleDateFormat fmt = new SimpleDateFormat(stampFormat);
        return fmt.format(date);
    }
    public static String getTimeStamp(Date date){
        return getTimeStamp(date,null);
    }
    public static String getTimeStamp(String stampFormat){
        return getTimeStamp(null,stampFormat);
    }
    public static String getTimeStamp(){
        return getTimeStamp(null,null);
    }

    public static String getRemoteIp(Current current){
        if (current == null) return null;
        String address = current.con.toString();
        assert (address != null);
        return address.substring(address.lastIndexOf("=")+1,address.lastIndexOf(":"));
    }
}
