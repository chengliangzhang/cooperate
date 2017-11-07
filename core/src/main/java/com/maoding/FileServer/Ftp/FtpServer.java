package com.maoding.FileServer.Ftp;

import com.maoding.FileServer.BasicFileServerInterface;
import com.maoding.Bean.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/31 10:33
 * 描    述 :
 */
@Service("ftpServer")
public class FtpServer implements BasicFileServerInterface {
    protected static final Logger log = (Logger) LoggerFactory.getLogger(FtpServer.class);
    public FTPClient ftpClient = new FTPClient();

    /**
     * 获取通过http方式上传文件数据库时的需要设置的部分参数
     *
     * @param src
     * @param callbackSetting
     */
    @Override
    public BasicFileRequestDTO getUploadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
        return null;
    }

    /**
     * 获取通过http方式下载文件数据库时的需要设置的部分参数
     *
     * @param src
     * @param callbackSetting
     */
    @Override
    public BasicFileRequestDTO getDownloadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
        return null;
    }

    /**
     * 上传文件分片内容
     *
     * @param request
     */
    @Override
    public BasicUploadResultDTO upload(BasicUploadRequestDTO request) {

        return null;
    }

    /**
     * 下载文件分片内容
     *
     * @param request
     */
    @Override
    public BasicDownloadResultDTO download(BasicDownloadRequestDTO request) {
        return null;
    }

    /**
     * 在文件服务器上复制文件，复制到同一空间，返回复制后的文件标识
     *
     * @param src
     */
    @Override
    public String duplicateFile(BasicFileDTO src) {
        return null;
    }

    /**
     * 判断在文件服务器上是否存在指定文件
     *
     * @param src
     */
    @Override
    public Boolean isExist(BasicFileDTO src) {
        FTPFile[] files;
        boolean status = false;
        try {
            //切换目录
            ftpClient.changeWorkingDirectory(new String(src.getScope().getBytes("GBK"), "iso-8859-1"));
            files = ftpClient.listFiles(src.getKey());
            if (0 > files.length) {
                status = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return status;
    }

    /**
     * 获取文件服务器上某一空间上的所有文件
     *
     * @param scope
     */
    @Override
    public List<String> listFile(String scope) {
        List fileList = new ArrayList();
        FTPFile[] files;
        try {
            files = ftpClient.listFiles(scope);
            fileList = Arrays.asList(files);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return fileList;
    }

    /**
     * 获取文件服务器上的所有空间
     */
    @Override
    public List<String> listScope() {
        return null;
    }

    /**
     * 在文件服务器上删除指定文件
     *
     * @param src
     */
    @Override
    public void deleteFile(BasicFileDTO src) {
        try {
            //切换目录
            ftpClient.changeWorkingDirectory(new String(src.getScope().getBytes("GBK"), "iso-8859-1"));
            //设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            //设置以二进制流的方式传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("GBK");
            //对远程目录的处理
            String remoteFileName = src.getKey();
            if (src.getKey().contains("/")) {
                remoteFileName = src.getKey().substring(src.getKey().lastIndexOf("/") + 1);
            }
            //检查远程文件是否存在
            FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("GBK"), "iso-8859-1"));
            if (1 == files.length || remoteFileName.equals(files[0].getName())) {
                ftpClient.deleteFile(remoteFileName);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }
}
