package com.maoding.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/6 20:57
 * 描    述 :
 */
public class FileUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static void ensureDirExist(String path){
        if (StringUtils.isNotEmpty(path)) {
            File fd = new File(StringUtils.formatPath(path));
            if (!fd.exists()) {
                boolean isSuccess = fd.mkdirs();
                assert (isSuccess);
            } else if (!fd.isDirectory()) {
                log.warn("存在同名文件");
            }
        }
    }

    public static void close(Closeable handle) {
        if (handle != null) {
            try {
                handle.close();
            } catch (IOException e) {
                log.error("关闭文件出错",e);
            }
        }
    }

    public static void setFileLength(@NotNull File file, long fileLength){
        if (file.exists()) {
            RandomAccessFile rf = null;
            try {
                rf = new RandomAccessFile(file, "rw");
                rf.setLength(fileLength);
            } catch (FileNotFoundException e) {
                log.error("未找到文件" + file.getPath(), e);
            } catch (IOException e) {
                log.error("设置文件" + file.getPath() + "长度时出错", e);
            } finally {
                FileUtils.close(rf);
            }
        } else {
            log.warn("文件" + file.getPath() + "不存在");
        }
    }

    public static void copyFile(@NotNull File srcFile, @NotNull File dstFile){
        final int BUFFER_SIZE = 2048 * 1024;

        if (srcFile.exists()) {
            //复制文件
            FileChannel in = null;
            FileChannel out = null;
            try {
                in = (new FileInputStream(srcFile)).getChannel();
                out = (new FileOutputStream(dstFile)).getChannel();
                while (in.position() < in.size()) {
                    int length = BUFFER_SIZE;
                    if ((in.size() - in.position()) < length) {
                        length = (int) (in.size() - in.position());
                    }
                    ByteBuffer buf = ByteBuffer.allocateDirect(length);
                    in.read(buf);
                    buf.flip();
                    out.write(buf);
                }
            } catch (IOException e) {
                log.error("从" + srcFile.getPath() + "复制到" + dstFile.getPath() + "时出错", e);
            } finally {
                FileUtils.close(out);
                FileUtils.close(in);
            }
        } else {
            log.warn("文件" + srcFile.getPath() + "不存在");
        }
    }
}
