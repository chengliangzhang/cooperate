package com.maoding.FileServer.Config;

import com.maoding.Const.FileServerConst;
import com.maoding.FileServer.BasicFileServerInterface;
import com.maoding.FileServer.FastFDS.FastFDSServer;
import com.maoding.FileServer.Ftp.FtpServer;
import com.maoding.FileServer.Jcifs.JcifsServer;
import com.maoding.FileServer.Local.LocalServer;
import org.springframework.context.annotation.Configuration;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 12:09
 * 描    述 :
 */
@Configuration
public class FileServerConfig {
    private BasicFileServerInterface fastFDSServer;
    private BasicFileServerInterface aliyunServer;
    private BasicFileServerInterface jcifsServer;
    private BasicFileServerInterface ftpServer;
    private BasicFileServerInterface localServer;

    private Integer fileServerType = FileServerConst.FILE_SERVER_TYPE_ALIYUN;

    public void setFileServerType(Integer type) {
        fileServerType = type;
    }

    public Integer getFileServerType() {
        return fileServerType;
    }

    public BasicFileServerInterface getFileServer(){
        if (FileServerConst.FILE_SERVER_TYPE_ALIYUN.equals(fileServerType)) {
//            if (aliyunServer == null) aliyunServer = new AliyunServer();
            return aliyunServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_FASTFDS.equals(fileServerType)){
            if (fastFDSServer == null) fastFDSServer= new FastFDSServer();
            return fastFDSServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_CIFS.equals(fileServerType)){
            if (jcifsServer == null) jcifsServer = new JcifsServer();
            return jcifsServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_FTP.equals(fileServerType)){
            if (ftpServer == null) ftpServer = new FtpServer();
            return ftpServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_LOCAL.equals(fileServerType)){
            if (localServer == null) localServer = new LocalServer();
            return localServer;
        }
        return null;
    }
}
