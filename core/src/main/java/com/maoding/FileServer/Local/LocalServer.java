package com.maoding.FileServer.Local;

import com.maoding.Bean.*;
import com.maoding.Const.ApiResponseConst;
import com.maoding.Const.FileServerConst;
import com.maoding.FileServer.BasicFileServerInterface;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.ExceptionUtils;
import com.maoding.Utils.FileUtils;
import com.maoding.Utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/1 14:41
 * 描    述 :
 */
@Service("localServer")
public class LocalServer implements BasicFileServerInterface {
    /** 日志对象 */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Integer MAX_TRY_TIMES = 6;
    private static final Integer TRY_DELAY = 100;

    public static final String FILE_NAME_SPLIT = "_";
    public static final String BASE_DIR_NAME = "scope";
    public static final String PATH_NAME = "key";
    public static final Integer DEFAULT_CHUNK_PER_SIZE = 8192;

    private static final String FILE_SERVER_PATH = "C:\\work\\file_server";
    private static final String FILE_UPLOAD_URL = "http://localhost:8087/FileServer/upload";
    private static final String FILE_DOWNLOAD_URL = "http://localhost:8087/FileServer/download";
    private static final String KEY_SIZE = "size";
    private static final String KEY_UPLOAD_ID = "uploadId";

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

    @Override
    public BasicFileDTO copyFile(BasicFileDTO basicSrc, BasicFileDTO basicDst) {
        final int BUFFER_SIZE = 2048 * 1024;
        if (isExist(basicDst)) {
            basicDst.setKey(getKeyWithStamp(basicDst.getKey()));
        }
        assert (isExist(basicSrc));
        assert (!isExist(basicDst));

        makeDirs(basicDst.getScope());

        //复制文件
        FileChannel in = null;
        FileChannel out = null;
        try {
            File srcFile = new File(getPath(basicSrc));
            assert (srcFile.exists());
            in = (new FileInputStream(srcFile)).getChannel();
            File dstFile = new File(getPath(basicDst));
            assert (!dstFile.exists());
            out = (new FileOutputStream(dstFile)).getChannel();
            while (in.position() < in.size())
            {
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
            log.error("复制文件" + getPath(basicSrc) + "时出错");
        } finally {
            FileUtils.close(out);
            FileUtils.close(in);
        }

        return basicDst;
    }

    @Override
    public long getFileLength(BasicFileDTO basicSrc) {
        if (!isExist(basicSrc)) return 0;
        File src = new File(getPath(basicSrc));
        return src.length();
    }

    @Override
    public boolean setFileLength(BasicFileDTO basicSrc, long fileLength) {
        boolean isOk = true;
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(getPath(basicSrc.getScope(),basicSrc.getKey()),"rw");
            file.setLength(fileLength);
        } catch (FileNotFoundException e) {
            log.error("未找到文件" + getPath(basicSrc.getScope(),basicSrc.getKey()));
            isOk = false;
        } catch (IOException e) {
            log.error("设置文件" + getPath(basicSrc.getScope(),basicSrc.getKey()) + "长度时出错");
            isOk = false;
        } finally {
            FileUtils.close(file);
        }
        return isOk;
    }

    /** 改名或移动文件 */
    @Override
    public BasicFileDTO moveFile(BasicFileDTO src, BasicFileDTO dst) {
        if (isExist(dst)) {
            dst.setKey(getKeyWithStamp(dst.getKey()));
        }
        File srcFile = new File(getPath(src));
        File dstFile = new File(getPath(dst));
        makeDirs(dst.getScope());
        boolean isSuccess = srcFile.renameTo(dstFile);
        assert (isSuccess);
        return dst;
    }

    /** 获取文件读写参数 */
    public BasicFileRequestDTO getFileRequest(BasicFileDTO src,short mode){
        assert (src != null);
        if ((FileServerConst.OPEN_MODE_READ_ONLY.equals(mode)) && (!isExist(src))) return null;

        BasicFileRequestDTO requestDTO = new BasicFileRequestDTO();
        if (FileServerConst.OPEN_MODE_READ_WRITE.equals(mode)){
            BasicFileDTO validSrc = getValidFileDTO(src);
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
    public BasicFileRequestDTO getUploadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
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
    public BasicFileRequestDTO getDownloadRequest(BasicFileDTO src, Integer mode, BasicCallbackDTO callbackSetting) {
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
        File f = new File(FILE_SERVER_PATH + "/" + src.getKey());
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
        BasicFileMultipartDTO multipart = request.getMultipart();
        BasicUploadResultDTO result = BeanUtils.createFrom(request,BasicUploadResultDTO.class);
        result.setData(BeanUtils.createFrom(multipart,BasicFileDTO.class));
        RandomAccessFile rf = null;

        byte[] data = multipart.getData();
        int off = 0;
        int len = multipart.getSize();
        long pos = multipart.getPos();

        t0 = System.currentTimeMillis();

        if (!((new File(FILE_SERVER_PATH + "/" + multipart.getScope())).isDirectory())) (new File(FILE_SERVER_PATH + "/" +multipart.getScope())).mkdirs();
        for (Integer i=0; i<MAX_TRY_TIMES; i++) {
            try {
                rf = new RandomAccessFile(FILE_SERVER_PATH + "/" + multipart.getScope() + "/" + multipart.getKey(), "rws");
                break;
            } catch (IOException e) {
                ExceptionUtils.logWarn(log,e,false,"打开文件" + FILE_SERVER_PATH + "/" + multipart.getScope() + "/" + multipart.getKey() + "出错");
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
            String msg = "写入文件" + FILE_SERVER_PATH + "/" + multipart.getScope() + "/" + multipart.getKey() + "时出错";
            ExceptionUtils.logWarn(log,e,false,msg);
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
    public int writeFile(BasicFileMultipartDTO multipart) {
        //检查参数
        assert (multipart != null);
        assert ((multipart.getPos() != null) && (multipart.getPos() >= 0));
        assert ((multipart.getSize() != null) && (multipart.getSize() > 0));
        assert (multipart.getData() != null);
        assert (!StringUtils.isEmpty(multipart.getKey()));

        //补全参数

        //写入文件
        RandomAccessFile rf = null;

        byte[] data = multipart.getData();
        int off = 0;
        int len = multipart.getSize();
        long pos = multipart.getPos();

        t0 = System.currentTimeMillis();

        makeDirs(multipart.getScope());
        for (Integer i=0; i<MAX_TRY_TIMES; i++) {
            try {
                rf = new RandomAccessFile(getPath(multipart.getScope(),multipart.getKey()), "rw");
                break;
            } catch (IOException e) {
                ExceptionUtils.logWarn(log,e,false,"打开文件" + getPath(multipart.getScope(),multipart.getKey()) + "出错");
                try {
                    Thread.sleep(TRY_DELAY);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
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
            String msg = "写入文件" + getPath(multipart.getScope(),multipart.getKey()) + "时出错";
            ExceptionUtils.logWarn(log,e,false,msg);
            len = 0;
        } finally {
            FileUtils.close(rf);
        }

        long t = (System.currentTimeMillis() - t0);
        log.info("写入" + StringUtils.calBytes(len) + ":用时" + t + "ms，速度"
                + StringUtils.calSpeed(len,t));
        return len;

    }

    private void makeDirs(String scope){
        File dir = new File(getPath(scope));
        if (!dir.exists()) {
            boolean isSuccess = dir.mkdirs();
            assert (isSuccess);
        }
    }

    private String getPath(String path){
        return getPath(path,null);
    }

    private String getPath(BasicFileDTO file){
        assert (file != null);
        return getPath(file.getScope(),file.getKey());
    }

    private String getPath(String scope,String key){
        StringBuilder pathBuilder = new StringBuilder(FILE_SERVER_PATH);
        if (!StringUtils.isEmpty(scope)) pathBuilder.append(StringUtils.SPLIT_PATH).append(scope);
        if (!StringUtils.isEmpty(key)) pathBuilder.append(StringUtils.SPLIT_PATH).append(key);
        return StringUtils.formatPath(pathBuilder.toString());
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
                rf = new RandomAccessFile(FILE_SERVER_PATH + "/" + request.getScope() + "/" + request.getKey(), "r");
                break;
            } catch (IOException e) {
                ExceptionUtils.logWarn(log, e, false, "打开文件" + FILE_SERVER_PATH + "/" + request.getScope() + "/" + request.getKey() + "出错");
                try {
                    Thread.sleep(TRY_DELAY);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                rf = null;
            }
        }

        try {
            if (rf == null) throw new IOException("打开文件" + FILE_SERVER_PATH + "/" + request.getScope() + "/" + request.getKey() + "出错");
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
            BasicFileMultipartDTO multipart = BeanUtils.createFrom(request,BasicFileMultipartDTO.class);
            multipart.setPos(pos);
            multipart.setSize(size);
            multipart.setData(bytes);

            result.setChunkCount(chunkCount);
            result.setChunkSize(size);
            result.setData(multipart);
            result.setStatus(ApiResponseConst.SUCCESS);
        } catch (IOException e) {
            ExceptionUtils.logError(log,e);
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
    public BasicFileMultipartDTO readFile(BasicFileDTO file, long pos, int size) {
        //检查参数
        assert (file != null);
        assert (pos >= 0);

        if (StringUtils.isEmpty(file.getKey())) return null;

        //补全参数
        if (size <= 0) size = DEFAULT_CHUNK_PER_SIZE;

        //下载文件
        BasicFileMultipartDTO result = null;
        RandomAccessFile rf = null;

        t1 = System.currentTimeMillis();

        //打开文件
        for (Integer i=0; i<MAX_TRY_TIMES; i++) {
            try {
                rf = new RandomAccessFile(getPath(file), "r");
                break;
            } catch (IOException e) {
                ExceptionUtils.logWarn(log, e, false, "打开文件" + getPath(file) + "出错");
                try {
                    Thread.sleep(TRY_DELAY);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
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
                result = BeanUtils.createFrom(file,BasicFileMultipartDTO.class);
                result.setPos(pos);
                result.setSize(size);
                result.setData(bytes);
            }
        } catch (IOException e) {
            ExceptionUtils.logError(log,e);
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

    private BasicFileDTO getValidFileDTO(BasicFileDTO src){
        if ((src == null) || (StringUtils.isEmpty(src.getScope())) || (StringUtils.isEmpty(src.getKey()))){
            BasicFileDTO fileDTO = new BasicFileDTO();
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

    private String getKeyWithStamp(String key){
        assert (!StringUtils.isEmpty(key));
        int n = key.indexOf(FILE_NAME_SPLIT);
        if (n == StringUtils.TIME_STAMP_FORMAT.length()) key = key.substring(n + FILE_NAME_SPLIT.length());
        assert (!StringUtils.isEmpty(key));
        return StringUtils.getTimeStamp(StringUtils.TIME_STAMP_FORMAT) + FILE_NAME_SPLIT + key;
    }

    private String getValidScope(BasicFileDTO src) {
        if ((src == null) || (StringUtils.isEmpty(src.getScope()))){
            return StringUtils.getTimeStamp(StringUtils.DATE_STAMP_FORMAT);
        } else {
            return src.getScope();
        }
    }

    private String getValidKey(BasicFileDTO src) {
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
    public String duplicateFile(BasicFileDTO src) {
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
    public Boolean isExist(BasicFileDTO src) {
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
    public List<String> listFile(String scope) {
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
    public void deleteFile(BasicFileDTO src) {
        if (isExist(src)){
            File f = new File(getPath(src));
            if (!f.isDirectory() || (f.listFiles() == null)){
                boolean isSuccess = f.delete();
                assert (isSuccess);
            }
        }
    }
}
