package com.maoding.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/6 20:57
 * 描    述 :
 */
public class FileUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static void close(Closeable handle) {
        if (handle != null) {
            try {
                handle.close();
            } catch (IOException e) {
                log.error("关闭文件出错",e);
            }
        }
    }
}
