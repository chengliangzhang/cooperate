package com.maoding.coreUtils;

import org.slf4j.Logger;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/7 18:14
 * 描    述 :
 */
public class TraceUtils {
    public static long enter(Logger log,String func,Object... obs){
        log.info("\t===>>> 进入" + func + ":" + getJsonString(obs));
        return System.currentTimeMillis();
    }

    public static void exit(Logger log,String func,long t,Object... obs){
        log.info("\t<<<=== 退出" + func + ":" + (System.currentTimeMillis() - t) + "ms," + getJsonString(obs));
    }

    public static long info(Logger log,String title,long t,Object... obs){
        log.info("\t===>>> " + title + ":" + (System.currentTimeMillis() - t) + "ms," + getJsonString(obs));
        return System.currentTimeMillis();
    }

    private static String getJsonString(Object... obs){
        if (obs != null) {
            StringBuilder s = new StringBuilder();
            for (Object o : obs) {
                s.append(JsonUtils.obj2CleanJson(o));
            }
            return s.toString();
        } else {
            return "";
        }
    }
}
