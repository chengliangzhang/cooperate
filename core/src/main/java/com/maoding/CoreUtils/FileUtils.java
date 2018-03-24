package com.maoding.CoreUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/6 20:57
 * 描    述 :
 */
public class FileUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    private static final int DEFAULT_BUFFER_SIZE = 2048 * 1024;

    public static void ensureDirExist(String dir){
        if (StringUtils.isNotEmpty(dir)) {
            File fd = new File(StringUtils.formatPath(dir));
            if (!fd.exists()) {
                boolean isSuccess = fd.mkdirs();
                assert (isSuccess);
            } else if (!fd.isDirectory()) {
                log.warn("存在同名文件");
            }
        }
    }

    public static void ensurePathExist(String path){
        ensureDirExist(StringUtils.getDirName(path));
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
        if (srcFile.exists()) {
            //复制文件
            FileChannel in = null;
            FileChannel out = null;
            try {
                in = (new FileInputStream(srcFile)).getChannel();
                out = (new FileOutputStream(dstFile)).getChannel();
                while (in.position() < in.size()) {
                    int length = DEFAULT_BUFFER_SIZE;
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
                close(out);
                close(in);
            }
        } else {
            log.warn("文件" + srcFile.getPath() + "不存在");
        }
    }

    public static String calcChecksum(@NotNull File srcFile) {
        assert (srcFile.exists() && srcFile.isFile());

        MessageDigest md5Calc = null;
        long len = 0;

        try {
            md5Calc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.warn("初始化MD5计算器错误");
        }

        FileChannel in = null;
        try {
            in = (new FileInputStream(srcFile)).getChannel();
            while (in.position() < in.size()){
                int length = DEFAULT_BUFFER_SIZE;
                if ((in.size() - in.position()) < length) {
                    length = (int) (in.size() - in.position());
                }
                ByteBuffer buf = ByteBuffer.allocateDirect(length);
                in.read(buf);
                byte[] calcArray = new byte[length];
                buf.flip();
                buf.get(calcArray,0,calcArray.length);
                assert (md5Calc != null);
                md5Calc.update(calcArray);
            }
            len = in.size();
        } catch (IOException e) {
            log.error("读取文件" + srcFile.getPath() + "时出错",e);
        } finally {
            close(in);
        }

        assert (md5Calc != null);
        byte[] md5Array = md5Calc.digest();
        BigInteger md5Int = new BigInteger(1, md5Array);
        return md5Int.toString();
    }

}
