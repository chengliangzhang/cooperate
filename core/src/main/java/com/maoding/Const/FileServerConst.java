package com.maoding.Const;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/26 9:40
 * 描    述 :
 */
public interface FileServerConst {
    final static Integer FILE_SERVER_TYPE_FASTFDS = 1;
    final static Integer FILE_SERVER_TYPE_ALIYUN = 2;
    final static Integer FILE_SERVER_TYPE_CIFS = 3;
    final static Integer FILE_SERVER_TYPE_FTP = 4;
    final static Integer FILE_SERVER_TYPE_LOCAL = 5;

    final static Integer FILE_SERVER_MODE_DEFAULT = 0;
    final static Integer FILE_SERVER_MODE_DEFAULT_COM = 1;
    final static Integer FILE_SERVER_MODE_HTTP_GET = 2;
    final static Integer FILE_SERVER_MODE_HTTP_POST = 3;
    final static Integer FILE_SERVER_MODE_OSS = 4;
    final static Integer FILE_SERVER_MODE_LOCAL = 5;
    final static Integer FILE_SERVER_MODE_RPC = 6;

    final static Integer DEFAULT_CHUNK_SIZE = 8192;

    final static Integer FILE_SERVER_TYPE_FAKE_FASTFDS = -FILE_SERVER_TYPE_FASTFDS;
    final static Integer FILE_SERVER_TYPE_FAKE_ALIYUN = -FILE_SERVER_TYPE_ALIYUN;
    final static Integer FILE_SERVER_TYPE_FAKE_CIFS = -FILE_SERVER_TYPE_CIFS;
    final static Integer FILE_SERVER_TYPE_FAKE_FTP = -FILE_SERVER_TYPE_FTP;
    final static Integer FILE_SERVER_TYPE_FAKE_LOCAL = -FILE_SERVER_TYPE_LOCAL;
}
