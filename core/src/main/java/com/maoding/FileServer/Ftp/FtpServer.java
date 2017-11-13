package com.maoding.FileServer.Ftp;

import com.maoding.Bean.*;
import com.maoding.Const.ApiResponseConst;
import com.maoding.FileServer.BasicFileServerInterface;
import com.maoding.Utils.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


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
    private static final String FILE_SERVER_PATH = "\\\\idccapp25\\Downloads";
    public static final Integer DEFAULT_CHUNK_PER_SIZE = 8192;

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
        BasicUploadResultDTO result = new BasicUploadResultDTO();
        //默认参数
        result.setChunkId(request.getChunkId());
        result.setChunkSize(request.getChunkSize());
        //检查参数
        assert request != null;
        assert request.getMultipart() != null;
        assert request.getMultipart().getData() != null;
        assert (request.getChunkId() != null) && (request.getChunkId() >= 0);
        try {
            //设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            //设置以二进制流的方式传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("GBK");
            //对远程目录的处理
            String remoteFileName = new String(request.getMultipart().getKey().getBytes("GBK"), "iso-8859-1");
            //补全参数
            if ((request.getChunkPerSize() == null) && (request.getChunkPerSize() <= 0))
                request.setChunkPerSize(DEFAULT_CHUNK_PER_SIZE);
            BasicFileMultipartDTO fileDTO = request.getMultipart();
            if (StringUtils.isEmpty(fileDTO.getScope())) fileDTO.setScope("");
            if (StringUtils.isEmpty(remoteFileName)) fileDTO.setKey(UUID.randomUUID().toString() + ".txt");
            if ((fileDTO.getPos() == null) || (fileDTO.getPos() < 0))
                fileDTO.setPos((long) request.getChunkId() * request.getChunkPerSize());
            if ((fileDTO.getSize() == null) || (fileDTO.getSize() <= 0)) fileDTO.setSize(fileDTO.getData().length);
            if ((request.getChunkSize() == null) || (request.getChunkSize() <= 0))
                request.setChunkSize(fileDTO.getSize());
            //写入文件
            RandomAccessFile rf = null;
            byte[] data = fileDTO.getData();
            assert data != null;
            int off = 0;
            int len = request.getChunkSize();
            assert (len <= request.getChunkPerSize()) && (len <= data.length);
            long pos = (long) request.getChunkId() * request.getChunkPerSize();
            //创建文件夹
            if (!ftpClient.changeWorkingDirectory(FILE_SERVER_PATH + "/" + fileDTO.getScope())) {
                if (ftpClient.makeDirectory(FILE_SERVER_PATH + "/" + fileDTO.getScope())) {
                    ftpClient.changeWorkingDirectory(FILE_SERVER_PATH + "/" + fileDTO.getScope());
                }
            }
            rf = new RandomAccessFile(FILE_SERVER_PATH + "/" + fileDTO.getScope() + "/" + fileDTO.getKey(), "rw");
            if (rf.length() < pos) rf.setLength(pos + len);
            rf.seek(pos);
            rf.write(data, off, len);
            result.setStatus(ApiResponseConst.SUCCESS);

        } catch (IOException e) {
            ExceptionUtils.logError(log, e);
            result.setStatus(ApiResponseConst.FAILED);
        }
        return result;
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param remote    远程服务器文件绝对路径
     * @param ftpClient FTPClient对象
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
        boolean result = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"), "iso-8859-1"))) {
            //如果远程目录不存在，则递归创建远程服务器目录
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录失败");
                        return result;
                    }
                }

                start = end + 1;
                end = directory.indexOf("/", start);

                //检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 上传文件到服务器,新上传和断点续传
     *
     * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变
     * @param localFile  本地文件File句柄，绝对路径
     * @param ftpClient  FTPClient引用
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String remoteFile, File localFile, FTPClient ftpClient, long remoteSize, BasicUploadRequestDTO request) throws IOException {
        boolean status = false;
        //显示进度的上传
        long step = localFile.length() / request.getChunkCount();
        long process = 0;
        long localreadbytes = 0L;
        RandomAccessFile raf = new RandomAccessFile(localFile, "r");
        OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"), "iso-8859-1"));
        //断点续传
        if (remoteSize > 0) {
            ftpClient.setRestartOffset(remoteSize);
            process = remoteSize / step;
            raf.seek(remoteSize);
            localreadbytes = remoteSize;
        }
        byte[] bytes = new byte[1024];
        int c;
        while ((c = raf.read(bytes)) != -1) {
            out.write(bytes, 0, c);
            localreadbytes += c;
            if (localreadbytes / step != process) {
                process = localreadbytes / step;
                System.out.println("上传进度:" + process);
                //TODO 汇报上传状态
            }
        }
        out.flush();
        raf.close();
        out.close();
        boolean result = ftpClient.completePendingCommand();
        if (remoteSize > 0 && result) {
            status = true;
        }
        return status;
    }

    /**
     * 下载文件分片内容
     * 构造分片信息
     *
     * @param request
     */
    @Override
    public BasicDownloadResultDTO download(BasicDownloadRequestDTO request) {
        BasicDownloadResultDTO result = BeanUtils.createFrom(request, BasicDownloadResultDTO.class);
        RandomAccessFile rf = null;
        try {
            //设置被动模式
            ftpClient.enterLocalPassiveMode();
            //设置以二进制方式传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //对远程目录的处理
            String remoteFileName = request.getKey();
            if (request.getKey().contains("/")) {
                remoteFileName = request.getKey().substring(request.getKey().lastIndexOf("/") + 1);
            }
            //检查远程文件是否存在
            FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("GBK"), "iso-8859-1"));
            if (files.length != 1) {
                System.out.println("远程文件不存在");
            }
            rf = new RandomAccessFile(FILE_SERVER_PATH + "/" + request.getScope() + "/" + remoteFileName, "r");
            //定位
            long pos = (long) request.getChunkId() * request.getChunkSize();
            assert pos < rf.length();
            rf.seek(pos);
            //读取文件内容
            byte[] bytes = new byte[request.getChunkSize()];
            assert bytes != null;
            int size = rf.read(bytes);
            assert size > 0;
            if (size < bytes.length) {
                bytes = Arrays.copyOfRange(bytes, 0, size);
            }
            //设置返回参数
            Integer chunckCount = ((int) (rf.length() - pos - size)) / request.getChunkSize() + 1;
            BasicFileMultipartDTO multipart = BeanUtils.createFrom(request, BasicFileMultipartDTO.class);
            multipart.setPos(pos);
            multipart.setSize(size);
            multipart.setData(bytes);

            result.setChunkCount(chunckCount);
            result.setChunkSize(size);
            result.setData(multipart);
            result.setStatus(ApiResponseConst.SUCCESS);
        } catch (IOException e) {
            ExceptionUtils.logError(log, e);
            result.setChunkCount(0);
            result.setChunkSize(0);
            result.setData(null);
            result.setStatus(ApiResponseConst.ERROR);
        } finally {
            FileUtils.close(rf);
        }
        return result;
    }

    /**
     * 在文件服务器上复制文件，复制到同一空间，返回复制后的文件标识
     * fileName 为时间戳+随机数
     *
     * @param src
     */
    @Override
    public String duplicateFile(BasicFileDTO src) {
        InputStream is = null;
        String fileName = KeyUtils.genUniqueKey();
        try {
            ftpClient.enterLocalPassiveMode();
            // 变更工作路径
            ftpClient.changeWorkingDirectory(src.getScope());
            is = ftpClient.retrieveFileStream(new String(src.getKey().getBytes("GBK"), "iso-8859-1"));
            if (is != null) {
                //变更保存路径
//                ftpClient.changeWorkingDirectory(targetDir);
                ftpClient.storeFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
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
            log.error(e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    log.error(ioe.getMessage());
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
            log.error(e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    log.error(ioe.getMessage());
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
                    log.error(ioe.getMessage());
                }
            }
        }
    }
}
