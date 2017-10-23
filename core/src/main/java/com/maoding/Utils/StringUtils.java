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

        String r = str.substring(str.indexOf(prefix) + prefix.length());
        return (str.contains(sp)) ? prefix + r.trim().substring(0,r.indexOf(sp)) : prefix + r.trim();
    }
    public static String getParam(String str, String prefix){
        return getParam(str,prefix," ");
    }
}
