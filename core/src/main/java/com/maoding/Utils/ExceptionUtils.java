package com.maoding.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/3 14:21
 * 描    述 :
 */
public class ExceptionUtils {
    public static Logger getLogger(String name){
        return LoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class<?> clazz){
        return LoggerFactory.getLogger(clazz);
    }

    public static void logError(Logger log, Exception e, Boolean isShow, String msg){
        if (msg == null) msg = e.getMessage();
        log.error(msg);
        if ((isShow == null) || isShow) e.printStackTrace();
    }
    public static void logError(Logger log, Exception e, Boolean isShow){
        logError(log,e,isShow,null);
    }
    public static void logError(Logger log, Exception e){
        logError(log,e,null);
    }
    public static void logWarn(Logger log, Exception e, Boolean isShow, String msg){
        if (msg == null) msg = e.getMessage();
        log.warn(msg);
        if ((isShow == null) || isShow) e.printStackTrace();
    }
    public static void logWarn(Logger log, Exception e, Boolean isShow){
        logWarn(log,e,isShow,null);
    }
    public static void logWarn(Logger log, Exception e){
        logWarn(log,e,null);
    }
}
