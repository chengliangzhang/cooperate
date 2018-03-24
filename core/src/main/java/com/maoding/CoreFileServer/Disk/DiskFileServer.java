package com.maoding.CoreFileServer.Disk;

import com.maoding.Const.FileServerConst;
import com.maoding.CoreFileServer.*;
import com.maoding.CoreUtils.BeanUtils;
import com.maoding.CoreUtils.FileUtils;
import com.maoding.CoreUtils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    private String baseDir = null;

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
     * 设定文件服务器存储空间
     *
     * @param baseDir
     */
    @Override
    public void coreSetBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * 获取文件服务器存储空间
     */
    @Override
    public String coreGetBaseDir() {
        return (StringUtils.isNotEmpty(baseDir)) ? baseDir : DEFAULT_BASE_DIR;
    }

    /**
     * 设置本地镜像根目录
     *
     * @param mirrorBaseDir
     */
    @Override
    public void coreSetMirrorBaseDir(String mirrorBaseDir) {
        coreSetBaseDir(mirrorBaseDir);
    }

    /**
     * 获取本地镜像根目录
     */
    @Override
    public String coreGetMirrorBaseDir() {
        return coreGetBaseDir();
    }

    /**
     * 创建文件
     *
     * @param createRequest
     */
    @Override
    public CoreFileDTO coreCreateFile(CoreCreateFileRequest createRequest) {
        CoreFileDTO dst;
        if (createRequest != null){
            dst = new CoreFileDTO(createRequest.getPath());
        } else {
            dst = new CoreFileDTO();
        }
        return coreCreateFile(dst,createRequest);
    }

    /**
     * 获取本地文件或镜像文件
     *
     * @param src
     * @param mirrorBaseDir
     */
    @Override
    public File coreGetLocalFile(CoreFileDTO src, String mirrorBaseDir) {
        return new File(getPath(src));
    }

    /**
     * 设定文件服务器地址
     *
     * @param serverAddress
     */
    @Override
    public void coreSetServerAddress(String serverAddress) {
        if (StringUtils.isNotEmpty(serverAddress)){
            baseDir = serverAddress.contains(ADDRESS_SPLIT)
                    ? StringUtils.right(serverAddress,ADDRESS_SPLIT) : serverAddress;
        }
    }

    /**
     * 判断在文件服务器上是否存在指定文件
     *
     */
    @Override
    public Boolean coreIsExist(String key) {
        if (StringUtils.isEmpty(key)) return false;
        File f = new File(getPath(key));
        return f.exists() && f.isFile();
    }

    /**
     * 创建文件
     *
     * @param createRequest
     */
    @Override
    public String coreCreateFileNew(CoreCreateFileRequest createRequest) {
        String key = ((createRequest != null) && (StringUtils.isNotEmpty(createRequest.getKey()))) ? createRequest.getKey() : getUniqueKey();
        if (coreIsExist(key)) {
            key = getUniqueKey(key);
        }

        String path = getPath(key);
        ensureDirExist(StringUtils.getDirName(path));

        try {
            File dstFile = new File(path);
            boolean isOk = dstFile.createNewFile();
            assert (isOk);
            if (createRequest != null){
                if (createRequest.getSrcFile() != null){
                    FileUtils.copyFile(createRequest.getSrcFile(),dstFile);
                } else if (createRequest.getFileLength() > 0) {
                    FileUtils.setFileLength(dstFile, createRequest.getFileLength());
                }
            }
        } catch (IOException e) {
            log.warn("无法建立文件" + key + ":" + e);
        }

        return key;
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
        if (coreIsExist(file)) {
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
    public CoreFileDTO coreCreateFile(CoreFileDTO dst,CoreCreateFileRequest createRequest) {
        String key;
        if (dst == null) {
            dst = new CoreFileDTO();
            key = coreCreateFileNew(createRequest);
        } else {
            key = dst.getScope() + StringUtils.SPLIT_PATH + dst.getKey();
            createRequest.setKey(key);
            key = coreCreateFileNew(createRequest);
        }
        dst.setScope(StringUtils.getDirName(key));
        dst.setKey(StringUtils.getFileName(key));
        return dst;
    }

    /**
     * 计算文件MD5
     *
     * @param src
     */
    @Override
    public String coreCalcChecksum(CoreFileDTO src) {
        assert (coreIsExist(src));
        return FileUtils.calcChecksum(new File(getPath(src)));
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

        assert (coreIsExist(src));

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
        if (coreIsExist(basicDst)) {
            basicDst = coreCreateFile(getPath(basicDst));
        }
        CoreFileCopyResult result = coreCopyFile(basicSrc,basicDst);
        return  (result != null) ? basicDst : null;
    }

    @Override
    public long coreGetFileLength(CoreFileDTO basicSrc) {
        if (!coreIsExist(basicSrc)) return 0;
        File src = new File(getPath(basicSrc));
        return src.length();
    }

    @Override
    public void coreSetFileLength(CoreFileDTO src, long fileLength) {
        String path = getPath(src);
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(path,"rw");
            file.setLength(fileLength);
        } catch (FileNotFoundException e) {
            log.error("未找到文件" + path,e);
        } catch (IOException e) {
            log.error("设置文件" + path + "长度时出错",e);
        } finally {
            FileUtils.close(file);
        }
    }

    /** 改名或移动文件 */
    @Override
    public CoreFileDTO coreMoveFile(CoreFileDTO src, CoreFileDTO dst) {
        if (!coreIsExist(src)) return null;
        if (coreIsExist(dst)) {
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
        if ((FileServerConst.OPEN_MODE_READ_ONLY.equals(mode)) && (!coreIsExist(src))) return null;

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
     * 写入文件内容
     *
     * @param data 要写入的数据
     * @param key 文件标识
     */
    @Override
    public int coreWriteFile(@NotNull CoreFileDataDTO data, String key) {

        long t = System.currentTimeMillis();

        //获取参数
        int off = 0;
        long pos = data.getPos();
        byte[] dataArray = data.getData();
        assert (dataArray != null);
        int len = data.getSize();
        if (len <= 0){
            len = dataArray.length;
        }
        String path = getPath(key);

        //创建路径
        assert (StringUtils.isNotEmpty(path));
        ensureDirExist(StringUtils.getDirName(path));

        //打开文件
        RandomAccessFile rf = null;
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

        //写入文件
        try {
            if (rf != null) {
                assert ((pos >= 0) && (len > 0) && (dataArray != null) && (len <= dataArray.length));
                if (rf.length() < pos) rf.setLength(pos + len);
                rf.seek(pos);
                rf.write(dataArray, off, len);
            }
        } catch (IOException e) {
            String msg = "写入文件" + path + "时出错";
            log.warn(msg);
            len = 0;
        } finally {
            FileUtils.close(rf);
        }

        log.info("\t----> coreWriteFile写入" + StringUtils.calBytes(len) + ":用时" + (System.currentTimeMillis() - t)
                + "ms，速度" + StringUtils.calSpeed(len,t));
        return len;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int coreWriteFile(CoreFileDataDTO multipart) {
        assert (multipart != null);
        assert (multipart.getKey() != null);
        String key = multipart.getScope() + StringUtils.SPLIT_PATH + multipart.getKey();
        return coreWriteFile(multipart,key);
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

    private String getPath(String key){
        return StringUtils.formatPath(coreGetBaseDir() + StringUtils.SPLIT_PATH + key);
    }

    private String getPath(String scope,String key){
        StringBuilder pathBuilder = new StringBuilder(coreGetBaseDir());
        if (!StringUtils.isEmpty(scope)) pathBuilder.append(StringUtils.SPLIT_PATH).append(scope);
        if (!StringUtils.isEmpty(key)) pathBuilder.append(StringUtils.SPLIT_PATH).append(key);
        return StringUtils.formatPath(pathBuilder.toString());
    }

    private String getPath(CoreFileDTO file){
        StringBuilder pathBuilder = new StringBuilder(coreGetBaseDir());
        if ((file != null) && !(StringUtils.isEmpty(file.getScope()))) {
            pathBuilder.append(StringUtils.SPLIT_PATH).append(file.getScope());
        } else {
            pathBuilder.append(StringUtils.SPLIT_PATH).append(DEFAULT_UNKNOWN_SCOPE_DIR);
        }
        if ((file != null) && !(StringUtils.isEmpty(file.getKey()))) {
            pathBuilder.append(StringUtils.SPLIT_PATH).append(file.getKey());
        } else {
            pathBuilder.append(StringUtils.SPLIT_PATH).append(UUID.randomUUID().toString());
        }
        return StringUtils.formatPath(pathBuilder.toString());
    }



    @Override
    public CoreFileDataDTO coreReadFile(CoreFileDTO file, long pos, int size) {
        //检查参数
        assert (file != null);
        assert (pos >= 0);

        if (StringUtils.isEmpty(file.getKey())) return null;
        if (pos >= coreGetFileLength(file)) return null;

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
            if (coreIsExist(fileDTO)){
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
            String dir = StringUtils.getDirName(key);
            String fn = StringUtils.getFileNameWithoutExt(key);
            String ext = StringUtils.getFileExt(key);
            assert (fn != null);
            if (fn.contains(FILE_NAME_SPLIT)) {
                int timeStampLen = TIME_STAMP_FORMAT.length();
                int len = fn.length();
                int n = fn.lastIndexOf(FILE_NAME_SPLIT);
                if (timeStampLen == (len - n - FILE_NAME_SPLIT.length())){
                    fn = fn.substring(0,n);
                }
            }
            fn += FILE_NAME_SPLIT + StringUtils.getTimeStamp(TIME_STAMP_FORMAT);
            StringBuilder keyBuilder = new StringBuilder(dir);
            if (keyBuilder.length() > 0) {
                keyBuilder.append(StringUtils.SPLIT_PATH);
            }
            keyBuilder.append(fn);
            if (StringUtils.isNotEmpty(ext)) {
                keyBuilder.append(ext);
            }
            key = keyBuilder.toString();
        }
        return key;
    }
    private String getUniqueKey(){
        return getUniqueKey(null);
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
     * 判断在文件服务器上是否存在指定文件
     *
     * @param src
     */
    @Override
    public Boolean coreIsExist(CoreFileDTO src) {
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
    public void coreDeleteFile(CoreFileDTO src) {
        if (coreIsExist(src)){
            File f = new File(getPath(src));
            if (!f.isDirectory() || (f.listFiles() == null)){
                boolean isSuccess = f.delete();
                assert (isSuccess);
            }
        }
    }
}
