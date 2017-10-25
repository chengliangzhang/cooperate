package com.maoding.Aliyun;

import com.maoding.Base.BaseFileServerInterface;
import com.maoding.Bean.BasicFileDTO;
import com.maoding.Bean.BasicHttpRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 10:05
 * 描    述 :
 */
@Service("aliyunServer")
public class AliyunServer implements BaseFileServerInterface{
    /**
     * 获取通过http方式上传文件数据库时的需要设置的部分参数
     *
     * @param src
     */
    @Override
    public BasicHttpRequestDTO getUploadRequestForHttp(BasicFileDTO src) {
        return null;
    }

    /**
     * 获取通过http方式下载文件数据库时的需要设置的部分参数
     *
     * @param src
     */
    @Override
    public BasicHttpRequestDTO getDownloadRequestForHttp(BasicFileDTO src) {
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
        return null;
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

    }
}
