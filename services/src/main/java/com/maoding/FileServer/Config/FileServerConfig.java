package com.maoding.FileServer.Config;

import com.maoding.Const.FileServerConst;
import com.maoding.FileServer.BasicFileServerInterface;
import com.maoding.FileServer.FastFDS.FastFDSServer;
import com.maoding.FileServer.Ftp.FtpServer;
import com.maoding.FileServer.Jcifs.JcifsServer;
import com.maoding.FileServer.Local.LocalServer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 12:09
 * 描    述 :
 */
@Component
@ConfigurationProperties(prefix = "fileserver")
public class FileServerConfig {
    private BasicFileServerInterface fastFDSServer;
    private BasicFileServerInterface aliyunServer;
    private BasicFileServerInterface jcifsServer;
    private BasicFileServerInterface ftpServer;
    private BasicFileServerInterface localServer;

    private Integer type;

    public void setFileServerType(Integer type) {
        this.type = type;
    }

    public Integer getFileServerType() {
        return type;
    }

    public BasicFileServerInterface getFileServer(){
        if (FileServerConst.FILE_SERVER_TYPE_ALIYUN.equals(type)) {
//            if (aliyunServer == null) aliyunServer = new AliyunServer();
            return aliyunServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_FASTFDS.equals(type)){
            if (fastFDSServer == null) fastFDSServer= new FastFDSServer();
            return fastFDSServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_CIFS.equals(type)){
            if (jcifsServer == null) jcifsServer = new JcifsServer();
            return jcifsServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_FTP.equals(type)){
            if (ftpServer == null) ftpServer = new FtpServer();
            return ftpServer;
        } else if (FileServerConst.FILE_SERVER_TYPE_LOCAL.equals(type)){
            if (localServer == null) localServer = new LocalServer();
            return localServer;
        }
        return localServer;
    }
}
