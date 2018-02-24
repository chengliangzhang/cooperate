package com.maoding.CoreFileServer.Disk;

import com.maoding.Const.ApiResponseConst;
import com.maoding.Const.FileServerConst;
import com.maoding.CoreFileServer.*;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.FileUtils;
import com.maoding.Utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 14:41
 * 描    述 :
 */
@Service("diskFileServer")
public class DiskFileServer implements CoreFileServer {
    /** 日志对象 */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Integer MAX_TRY_TIMES = 6;
    private static final Integer TRY_DELAY = 100;

    public static final String FILE_NAME_SPLIT = "_";
    public static final String BASE_DIR_NAME = "scope";
    public static final String PATH_NAME = "key";
    public static final Integer DEFAULT_CHUNK_PER_SIZE = 8192;

    private static final String DEFAULT_BASE_DIR = "C:\\work\\file_server";
    private static final String DEFAULT_UNKNOWN_SCOPE_DIR = "unknown_scope";
    private static final String FILE_UPLOAD_URL = "http://localhost:8087/FileServer/upload";
    private static final String FILE_DOWNLOAD_URL = "http://localhost:8087/FileServer/download";
    private static final String KEY_SIZE = "size";
    private static final String KEY_UPLOAD_ID = "uploadId";
    private static String baseDir = null;

    private static final HashMap<Integer,Integer> modeMapConst = new HashMap<Integer,Integer>(){
        {
            put(FileServerConst.FILE_SERVER_MODE_DEFAULT, FileServerConst.FILE_SERVER_MODE_RPC);
            put(FileServerConst.FILE_SERVER_MODE_DEFAULT_COM, FileServerConst.FILE_SERVER_MODE_RPC);
            put(FileServerConst.FILE_SERVER_MODE_HTTP_GET, FileServerConst.FILE_SERVER_MODE_HTTP_POST);
            put(FileServerConst.FILE_SERVER_MODE_HTTP_POST, FileServerConst.FILE_SERVER_MODE_HTTP_POST);
            put(FileServerConst.FILE_SERVER_MODE_LOCAL, FileServerConst.FILE_SERVER_MODE_LOCAL);
        }
    };

    private static long t0 = 0; //上传计时器
    private static long pos0 = 0; //上传速度计算参数
    private static long t1 = 0; //下载计时器
    private static long pos1 = 0; //下载速度计算参数

    /**
     * 设定文件服务器地址
     *
     * @param serverAddress
     */
    @Override
    public void coreSetServerAddress(String serverAddress) {
        baseDir = serverAddress;
    }

    @Override
    public String coreGetServerAddress(){
        return getBaseDir();
    }

    private static String getBaseDir(){
        return (StringUtils.isEmpty(baseDir)) ? DEFAULT_BASE_DIR : baseDir;
    }

    /**
     * 创建文件
     *
     * @param path
     */
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public CoreFileDTO coreCreateFile(String path) {
        if (StringUtils.isEmpty(path)) return null;

        String scope = StringUtils.getDirName(path);
        if (scope == null) scope = "";
        String key = StringUtils.getFileName(path);
        assert(!StringUtils.isEmpty(key));
        CoreFileDTO file = new CoreFileDTO();
        file.setScope(scope);
        file.setKey(key);
        if (isExist(file)) {
            file.setKey(getUniqueKey(key));
        }

        makeDirs(file.getScope());

        File f = new File(getPath(file));
        try {
            boolean isOk = f.createNewFile();
            assert (isOk);
        } catch (IOException e) {
            log.warn("无法建立文件" + getPath(file) + ":" + e);
        }
        return file;
    }

    /**
     * 根据文件信息创建文件
     *
     * @param dst
     */
    @Override
    public CoreFileDTO coreCreateFile(CoreFileDTO dst,CoreFileExtraDTO createRequest) {
        if (dst == null) dst = new CoreFileDTO();
        if (isExist(dst)) {
            dst.setKey(getUniqueKey(dst.getKey()));
        }
        String path = getPath(dst);
        assert (StringUtils.isNotEmpty(path));

        ensureDirExist(StringUtils.getDirName(path));

        File dstFile = new File(path);
        try {
            boolean isOk = dstFile.createNewFile();
            assert (isOk);
        } catch (IOException e) {
            log.warn("无法建立文件" + getPath(dst) + ":" + e);
        }

        if (createRequest != null){
            if (createRequest.getSrcFile() != null){
                FileUtils.copyFile(createRequest.getSrcFile(),dstFile);
            } else if (createRequest.getFileLength() > 0) {
                FileUtils.setFileLength(dstFile, createRequest.getFileLength());
            }
        }
        return dst;
    }

    /**
     * 获取文件句柄
     *
     * @param src
     */
    @Override
    public File coreGetFile(CoreFileDTO src) {
        return (isExist(src)) ? new File(getPath(src)) : null;
    }

    /**
     * 计算文件MD5
     *
     * @param src
     */
    @Override
    public String coreCalcChecksum(CoreFileDTO src) {
        final int BUFFER_SIZE = 2048 * 1024;

        assert (isExist(src));

        long len = 0;

        MessageDigest md5Calc = null;
        try {
            md5Calc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.warn("初始化MD5计算器错误");
        }

        FileChannel in = null;
        try {
            File srcFile = new File(getPath(src));
            assert (srcFile.exists());

            in = (new FileInputStream(srcFile)).getChannel();
            while (in.position() < in.size()){
                int length = BUFFER_SIZE;
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
            log.error("读取文件" + getPath(src) + "时出错",e);
        } finally {
            FileUtils.close(in);
        }

        CoreFileCopyResult result = new CoreFileCopyResult();
        assert (md5Calc != null);
        byte[] md5Array = md5Calc.digest();
        BigInteger md5Int = new BigInteger(1, md5Array);
        return md5Int.toString();
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dst
     */
    @Override
    public CoreFileCopyResult coreCopyFile(CoreFileDTO src, CoreFileDTO dst) {
        final int BUFFER_SIZE = 2048 * 1024;

        assert (isExist(src));

        long len = 0;

        makeDirs(dst.getScope());

        //复制文件
        FileChannel in = null;
        FileChannel out = null;
        MessageDigest md5Calc = null;
        try {
            md5Calc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.warn("初始化MD5计算器错误");
        }
        try {
            File srcFile = new File(getPath(src));
            assert (srcFile.exists());

            in = (new FileInputStream(srcFile)).getChannel();
            File dstFile = new File(getPath(dst));
            out = (new FileOutputStream(dstFile)).getChannel();
            while (in.position() < in.size()){
                int length = BUFFER_SIZE;
                if ((in.size() - in.position()) < length) {
                    length = (int) (in.size() - in.position());
                }
                ByteBuffer buf = ByteBuffer.allocateDirect(length);
                in.read(buf);
                buf.flip();
                out.write(buf);
                if (md5Calc != null) {
                    byte[] calcArray = new byte[length];
                    buf.flip();
                    buf.get(calcArray,0,calcArray.length);
                    md5Calc.update(calcArray);
                }
            }
            len = in.size();
        } catch (IOException e) {
            log.error("复制文件" + getPath(src) + "时出错",e);
        } finally {
            FileUtils.close(out);
            FileUtils.close(in);
        }

        CoreFileCopyResult result = new CoreFileCopyResult();
        if (md5Calc != null) {
            byte[] md5Array = md5Calc.digest();
            BigInteger md5Int = new BigInteger(1, md5Array);
            result.setFileChecksum(md5Int.toString());
        }
        result.setFileLength(len);

        return result;
    }

    @Override
    @Deprecated
    public CoreFileDTO copyFile(CoreFileDTO basicSrc, CoreFileDTO basicDst) {
        if (isExist(basicDst)) {
            basicDst = coreCreateFile(getPath(basicDst));
        }
        CoreFileCopyResult result = coreCopyFile(basicSrc,basicDst);
        return  (result != null) ? basicDst : null;
    }

    @Override
    public long coreGetFileLength(CoreFileDTO basicSrc) {
        if (!isExist(basicSrc)) return 0;
        File src = new File(getPath(basicSrc));
        return src.length();
    }

    @Override
    public boolean coreSetFileLength(CoreFileDTO src, long fileLength) {
        boolean isOk = true;
        String path = getPath(src);
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(path,"rw");
            file.setLength(fileLength);
        } catch (FileNotFoundException e) {
            log.error("未找到文件" + path,e);
            isOk = false;
        } catch (IOException e) {
            log.error("设置文件" + path + "长度时出错",e);
            isOk = false;
        } finally {
            FileUtils.close(file);
        }
        return isOk;
    }

    /** 改名或移动文件 */
    @Override
    public CoreFileDTO coreMoveFile(CoreFileDTO src, CoreFileDTO dst) {
        if (!isExist(src)) return null;
        if (isExist(dst)) {
            dst.setKey(getUniqueKey(dst.getKey()));
        }
        File srcFile = new File(getPath(src));
        File dstFile = new File(getPath(dst));
        ensureDirExist(StringUtils.getDirName(getPath(dst)));
        boolean isSuccess = srcFile.renameTo(dstFile);
        assert (isSuccess);
        return dst;
    }

    /** 获取文件读写参数 */
    @SuppressWarnings("deprecation")
    public BasicFileRequestDTO getFileRequest(CoreFileDTO src, short mode){
        assert (src != null);
        if ((FileServerConst.OPEN_MODE_READ_ONLY.equals(mode)) && (!isExist(src))) return null;

        BasicFileRequestDTO requestDTO = new BasicFileRequestDTO();
        if (FileServerConst.OPEN_MODE_READ_WRITE.equals(mode)){
            CoreFileDTO validSrc = getValidFileDTO(src);
            requestDTO.setScope(validSrc.getScope());
            requestDTO.setKey(validSrc.getKey());
        } else {
            requestDTO.setScope(src.getScope());
            requestDTO.setKey(src.getKey());
        }
        requestDTO.setMode(FileServerConst.FILE_SERVER_MODE_RPC);

        //保持兼容性
        requestDTO.putParam(BASE_DIR_NAME,requestDTO.getScope());
        requestDTO.putParam(PATH_NAME,requestDTO.getKey());
        return requestDTO;
    }

    /**
     * 获取通过http方式上传文件数据库时的需要设置的部分参数
     *
     * @param src
     * @param callbackSetting
     */
    @Override
    @Deprecated
    public BasicFileRequestDTO getUploadRequest(CoreFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
        //补全参数
        //建立申请上传参数对象
        BasicFileRequestDTO requestDTO = new BasicFileRequestDTO();
        requestDTO.setUrl(FILE_UPLOAD_URL);
        requestDTO.setScope(getValidScope(src.getScope()));
        requestDTO.setKey(getValidKey(src.getKey()));
        requestDTO.setMode(modeMapConst.getOrDefault(mode, FileServerConst.FILE_SERVER_MODE_RPC));
        requestDTO.putParam(KEY_UPLOAD_ID,UUID.randomUUID().toString());

        //保持兼容性
        requestDTO.putParam(BASE_DIR_NAME,requestDTO.getScope());
        requestDTO.putParam(PATH_NAME,requestDTO.getKey());
        return requestDTO;
    }

    /**
     * 获取通过http方式下载文件数据库时的需要设置的部分参数
     *
     * @param src
     * @param callbackSetting
     */
    @Override
    @Deprecated
    public BasicFileRequestDTO getDownloadRequest(CoreFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
        //检查参数
        assert (src != null);
        assert (src.getKey() != null);
        
        //补全参数
        if (StringUtils.isEmpty(src.getScope())) src.setScope("");

        //建立申请下载参数对象
        BasicFileRequestDTO requestDTO = BeanUtils.createFrom(src,BasicFileRequestDTO.class);
        requestDTO.setUrl(FILE_DOWNLOAD_URL);
        requestDTO.putParam(BASE_DIR_NAME,src.getScope());
        requestDTO.putParam(PATH_NAME,src.getKey());
        requestDTO.setMode(modeMapConst.getOrDefault(mode, FileServerConst.FILE_SERVER_MODE_RPC));
        File f = new File(DEFAULT_BASE_DIR + "/" + src.getKey());
        requestDTO.putParam(KEY_SIZE,((Long)f.length()).toString());
        return requestDTO;
    }

    /**
     * 上传文件分片内容
     *
     * @param request
     */
    @Override
    @Deprecated
    public BasicUploadResultDTO upload(BasicUploadRequestDTO request) {
        //检查参数
        assert (request != null);
        assert (request.getMultipart() != null);
        assert ((request.getMultipart().getPos() != null) && (request.getMultipart().getPos() >= 0));
        assert ((request.getMultipart().getSize() != null) && (request.getMultipart().getSize() > 0));
        assert (request.getMultipart().getData() != null);
        assert (request.getMultipart().getScope() != null);
        assert (!StringUtils.isEmpty(request.getMultipart().getKey()));

        //补全参数

        //写入文件
        CoreFileDataDTO multipart = request.getMultipart();
        BasicUploadResultDTO result = BeanUtils.createFrom(request,BasicUploadResultDTO.class);
        result.setData(BeanUtils.createFrom(multipart,CoreFileDTO.class));
        RandomAccessFile rf = null;

        byte[] data = multipart.getData();
        int off = 0;
        int len = multipart.getSize();
        long pos = multipart.getPos();

        t0 = System.currentTimeMillis();

        if (!((new File(DEFAULT_BASE_DIR + "/" + multipart.getScope())).isDirectory())) (new File(DEFAULT_BASE_DIR + "/" +multipart.getScope())).mkdirs();
        for (Integer i=0; i<MAX_TRY_TIMES; i++) {
            try {
                rf = new RandomAccessFile(DEFAULT_BASE_DIR + "/" + multipart.getScope() + "/" + multipart.getKey(), "rws");
                break;
            } catch (IOException e) {
                log.warn("打开文件" + DEFAULT_BASE_DIR + "/" + multipart.getScope() + "/" + multipart.getKey() + "出错");
                try {
                    Thread.sleep(TRY_DELAY);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                rf = null;
            }
        }

        try {
            if (rf == null) throw new IOException();
            assert ((pos >= 0) && (len > 0) && (data != null) && (len <= data.length));
            if (rf.length() < pos) rf.setLength(pos + len);
            rf.seek(pos);
            rf.write(data,off,len);
            result.setChunkSize(len);
            result.setStatus(ApiResponseConst.SUCCESS);
        } catch (IOException e) {
            String msg = "写入文件" + DEFAULT_BASE_DIR + "/" + multipart.getScope() + "/" + multipart.getKey() + "时出错";
            log.warn(msg);
            result.setMsg(msg);
            result.setChunkSize(0);
            result.setStatus(ApiResponseConst.FAILED);
        } finally {
            FileUtils.close(rf);
        }

        long t = (System.currentTimeMillis() - t0);
        long rev = pos + len;
        long cal = rev - pos0;
        log.info("写入" + StringUtils.calBytes(rev) + ":用时" + t + "ms，速度"
                + StringUtils.calSpeed(cal,t));
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int writeFile(CoreFileDataDTO multipart) {
        //检查参数
        assert (multipart != null);
        assert ((multipart.getPos() != null) && (multipart.getPos() >= 0));
        assert ((multipart.getSize() != null) && (multipart.getSize() > 0));
        assert (multipart.getData() != null);

        //补全参数

        //写入文件
        RandomAccessFile rf = null;

        byte[] data = multipart.getData();
        int off = 0;
        int len = multipart.getSize();
        long pos = multipart.getPos();

        t0 = System.currentTimeMillis();

        CoreFileDTO dst = new CoreFileDTO(multipart.getScope(),multipart.getKey());
        String path = StringUtils.getDirName(getPath(dst));
        ensureDirExist(path);
        path = getPath(dst);
        for (Integer i=0; i<MAX_TRY_TIMES; i++) {
            try {
                rf = new RandomAccessFile(path, "rw");
                break;
            } catch (IOException e) {
                log.warn("打开文件" + path + "出错");
                try {
                    Thread.sleep(TRY_DELAY);
                } catch (InterruptedException e1) {
                    log.warn(e1.getMessage());
                }
                rf = null;
            }
        }

        try {
            if (rf != null) {
                assert ((pos >= 0) && (len > 0) && (data != null) && (len <= data.length));
                if (rf.length() < pos) rf.setLength(pos + len);
                rf.seek(pos);
                rf.write(data, off, len);
            }
        } catch (IOException e) {
            String msg = "写入文件" + path + "时出错";
            log.warn(msg);
            len = 0;
        } finally {
            FileUtils.close(rf);
        }

        long t = (System.currentTimeMillis() - t0);
        log.info("写入" + StringUtils.calBytes(len) + ":用时" + t + "ms，速度"
                + StringUtils.calSpeed(len,t));
        return len;

    }

    private void ensureDirExist(String dirPath){
        assert (StringUtils.isNotEmpty(dirPath));
        File fd = new File(StringUtils.formatPath(dirPath));
        if (!fd.exists()) {
            boolean isSuccess = fd.mkdirs();
            assert (isSuccess);
        }
    }

    @Deprecated
    private void makeDirs(String scope){
        File dir = new File(getPath(scope));
        if (!dir.exists()) {
            boolean isSuccess = dir.mkdirs();
            assert (isSuccess);
        }
    }

    @Deprecated
    private String getPath(String path){
        return getPath(path,null);
    }

    @Deprecated
    public static String getPath(String scope,String key){
        StringBuilder pathBuilder = new StringBuilder(DEFAULT_BASE_DIR);
        if (!StringUtils.isEmpty(scope)) pathBuilder.append(StringUtils.SPLIT_PATH).append(scope);
        if (!StringUtils.isEmpty(key)) pathBuilder.append(StringUtils.SPLIT_PATH).append(key);
        return StringUtils.formatPath(pathBuilder.toString());
    }

    public static String getPath(CoreFileDTO file){
        String baseDir = getBaseDir();
        if ((file != null) && (StringUtils.isNotEmpty(file.getServerAddress()))) baseDir = file.getServerAddress();
        String scope = DEFAULT_UNKNOWN_SCOPE_DIR;
        if ((file != null) && (StringUtils.isNotEmpty(file.getScope()))) scope = file.getScope();
        String key = UUID.randomUUID().toString();
        if ((file != null) && (StringUtils.isNotEmpty(file.getKey()))) key = file.getKey();

        String path = baseDir + StringUtils.SPLIT_PATH + scope + StringUtils.SPLIT_PATH + key;

        return StringUtils.formatPath(path);
    }

    /**
     * 下载文件分片内容
     *
     * @param request
     */
    @Override
    @Deprecated
    public BasicDownloadResultDTO download(BasicDownloadRequestDTO request) {
        //检查参数
        assert (request != null);
        assert (request.getKey() != null);
        assert ((request.getPos() != null) && (request.getPos() >= 0));

        //补全参数
        if (StringUtils.isEmpty(request.getScope())) request.setScope("");
        if ((request.getSize() == null) || (request.getSize() <= 0)) request.setSize(DEFAULT_CHUNK_PER_SIZE);

        //下载文件
        BasicDownloadResultDTO result = BeanUtils.createFrom(request,BasicDownloadResultDTO.class);
        RandomAccessFile rf = null;

        long pos = request.getPos();
        int size = request.getSize();

        if (pos == 0) {
            t1 = System.currentTimeMillis();
            log.info("=====>接收到下载请求:" + new SimpleDateFormat("HH:mm:ss.sss").format(new Date(t1)));
        }

        //打开文件
        for (Integer i=0; i<MAX_TRY_TIMES; i++) {
            try {
                rf = new RandomAccessFile(DEFAULT_BASE_DIR + "/" + request.getScope() + "/" + request.getKey(), "r");
                break;
            } catch (IOException e) {
                log.warn("打开文件" + DEFAULT_BASE_DIR + "/" + request.getScope() + "/" + request.getKey() + "出错");
                try {
                    Thread.sleep(TRY_DELAY);
                } catch (InterruptedException e1) {
                    log.warn(e1.getMessage());
                }
                rf = null;
            }
        }

        try {
            if (rf == null) throw new IOException("打开文件" + DEFAULT_BASE_DIR + "/" + request.getScope() + "/" + request.getKey() + "出错");
            //定位
            long length = rf.length();
            assert (pos >= 0) && (pos < length) && (size > 0);
            rf.seek(pos);
            //读取文件内容
            byte[] bytes = new byte[size];
            int n = rf.read(bytes);
            assert (n > 0) && (n <= size);
            bytes = Arrays.copyOfRange(bytes,0,n);
            size = n;

            //设置返回参数
            long posLast = pos + size;
            Integer chunkCount = (posLast < length) ? ((((int)(length - posLast)) / request.getChunkSize()) + 1) : 0;
            CoreFileDataDTO multipart = BeanUtils.createFrom(request,CoreFileDataDTO.class);
            multipart.setPos(pos);
            multipart.setSize(size);
            multipart.setData(bytes);

            result.setChunkCount(chunkCount);
            result.setChunkSize(size);
            result.setData(multipart);
            result.setStatus(ApiResponseConst.SUCCESS);
        } catch (IOException e) {
            log.error("下载时出错",e);
            result.setError();
        } finally {
            FileUtils.close(rf);
        }

        if (result.getData() != null) {
            long t = (System.currentTimeMillis() - t1);
            long c = result.getData().getPos() + result.getData().getSize();
            log.info("已准备好" + StringUtils.calBytes(c) + "数据，用时" + t + "ms,速度" + StringUtils.calSpeed(c,t));
        }
        return result;
    }

    @Override
    public CoreFileDataDTO readFile(CoreFileDTO file, long pos, int size) {
        //检查参数
        assert (file != null);
        assert (pos >= 0);

        if (StringUtils.isEmpty(file.getKey())) return null;

        //补全参数
        if (size <= 0) size = DEFAULT_CHUNK_PER_SIZE;

        //下载文件
        CoreFileDataDTO result = null;
        RandomAccessFile rf = null;

        t1 = System.currentTimeMillis();

        //打开文件
        for (Integer i=0; i<MAX_TRY_TIMES; i++) {
            try {
                rf = new RandomAccessFile(getPath(file), "r");
                break;
            } catch (IOException e) {
                log.warn( "打开文件" + getPath(file) + "出错");
                try {
                    Thread.sleep(TRY_DELAY);
                } catch (InterruptedException e1) {
                    log.warn(e1.getMessage());
                }
                rf = null;
            }
        }

        try {
            if ((rf != null) && (pos < rf.length())) {
                //定位
                rf.seek(pos);
                //读取文件内容
                assert (size > 0);
                byte[] bytes = new byte[size];
                int n = rf.read(bytes);
                assert (n > 0) && (n <= size);
                bytes = Arrays.copyOfRange(bytes, 0, n);
                size = n;

                //设置返回参数
                result = BeanUtils.createFrom(file,CoreFileDataDTO.class);
                result.setPos(pos);
                result.setSize(size);
                result.setData(bytes);
            }
        } catch (IOException e) {
            log.error("读取时出错",e);
            result = null;
        } finally {
            FileUtils.close(rf);
        }

        long t = (System.currentTimeMillis() - t1);
        if (result != null) {
            log.info("读取" + StringUtils.calBytes(result.getSize()) + "数据，用时" + t + "ms,速度"
                    + StringUtils.calSpeed(result.getSize(),t));
        } else {
            log.info("未能读取有效数据，用时" + t + "ms");
        }
        return result;
    }

    private CoreFileDTO getValidFileDTO(CoreFileDTO src){
        if ((src == null) || (StringUtils.isEmpty(src.getScope())) || (StringUtils.isEmpty(src.getKey()))){
            CoreFileDTO fileDTO = new CoreFileDTO();
            fileDTO.setScope(getValidScope(src));
            fileDTO.setKey(getValidKey(src));
            if (isExist(fileDTO)){
                fileDTO.setKey(getKeyWithStamp(fileDTO.getKey()));
            }
            return fileDTO;
        } else {
            return src;
        }
    }

    private String getUniqueKey(String key){
        final String TIME_STAMP_FORMAT =  StringUtils.TIME_STAMP_FORMAT;

        if (StringUtils.isEmpty(key)) {
            key = UUID.randomUUID().toString();
        } else {
            String ext = StringUtils.getFileExt(key);
            key = StringUtils.getFileNameWithoutExt(key);
            if (StringUtils.isEmpty(key)) key = UUID.randomUUID().toString();
            assert (key != null);
            if (key.contains(FILE_NAME_SPLIT)) {
                int timeStampLen = TIME_STAMP_FORMAT.length();
                int len = key.length();
                int n = key.lastIndexOf(FILE_NAME_SPLIT);
                if (timeStampLen == (len - n - FILE_NAME_SPLIT.length())){
                    key = key.substring(0,n);
                }
            }
            key += FILE_NAME_SPLIT + StringUtils.getTimeStamp(TIME_STAMP_FORMAT);
            if (StringUtils.isNotEmpty(ext)) key += ext;
        }
        return key;
    }

    @Deprecated
    private String getKeyWithStamp(String key){
        return getUniqueKey(key);
    }

    private String getValidScope(CoreFileDTO src) {
        if ((src == null) || (StringUtils.isEmpty(src.getScope()))){
            return StringUtils.getTimeStamp(StringUtils.DATE_STAMP_FORMAT);
        } else {
            return src.getScope();
        }
    }

    private String getValidKey(CoreFileDTO src) {
        if ((src == null) || (StringUtils.isEmpty(src.getKey()))){
            return UUID.randomUUID().toString() + ".txt";
        } else {
            return src.getKey();
        }
    }

    @Deprecated
    private String getValidScope(String scope){
        if (StringUtils.isEmpty(scope)){
            scope = StringUtils.getTimeStamp(StringUtils.DATE_STAMP_FORMAT);
        }
        return scope;
    }

    @Deprecated
    private String getValidKey(String key){
        if (StringUtils.isEmpty(key)){
            key = UUID.randomUUID().toString() + ".txt";
        }
        return key;
    }

    /**
     * 在文件服务器上复制文件，复制到同一空间，返回复制后的文件标识
     *
     * @param src
     */
    @Override
    public String duplicateFile(CoreFileDTO src) {
        final int BUFFER_SIZE = 2048 * 1024;

        assert (isExist(src));

        String keyOut = null;

        //复制文件
        int length = BUFFER_SIZE;
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = (new FileInputStream(getPath(src))).getChannel();
            keyOut = getKeyWithStamp(src.getKey());
            out = (new FileOutputStream(getPath(src.getScope(),keyOut))).getChannel();
            while (in.position() < in.size())
            {
                if ((in.size() - in.position()) < length) {
                    length = (int) (in.size() - in.position());
                } else {
                    length = BUFFER_SIZE;
                }
                ByteBuffer buf = ByteBuffer.allocateDirect(length);
                in.read(buf);
                buf.flip();
                out.write(buf);
//                out.force(false);
            }
        } catch (IOException e) {
            log.error("复制文件" + getPath(src) + "时出错");
        } finally {
            FileUtils.close(out);
            FileUtils.close(in);
        }
        return keyOut;
    }

    /**
     * 判断在文件服务器上是否存在指定文件
     *
     * @param src
     */
    @Override
    public Boolean isExist(CoreFileDTO src) {
        if ((src == null) || (StringUtils.isEmpty(src.getKey()))) return false;
        File f = new File(getPath(src));
        return f.exists();
    }

    /**
     * 获取文件服务器上某一空间上的所有文件
     *
     * @param scope
     */
    @Override
    public List listFile(String scope) {
        return null;
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
    public void deleteFile(CoreFileDTO src) {
        if (isExist(src)){
            File f = new File(getPath(src));
            if (!f.isDirectory() || (f.listFiles() == null)){
                boolean isSuccess = f.delete();
                assert (isSuccess);
            }
        }
    }
}
