package com.maoding.CoreFileServer.MaodingWeb;

import com.maoding.CoreFileServer.BasicFileMultipartDTO;
import com.maoding.CoreFileServer.CoreFileServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/1/22 14:03
 * 描    述 :
 */
@Service("webFileServer")
public class WebFileServer implements CoreFileServer {
    /** 日志对象 */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public int writeFile(BasicFileMultipartDTO data) {
        // 设定服务地址
        String serverUrl ="http://127.0.0.1:8087/FileServer/upload";

        // 设定要上传的普通Form Field及其对应的value
        // 类FormFieldKeyValuePair的定义见后面的代码
        ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<>();
        ffkvp.add(new FormFieldKeyValuePair("username", "Patrick"));
        ffkvp.add(new FormFieldKeyValuePair("password", "HELLOPATRICK"));
        ffkvp.add(new FormFieldKeyValuePair("hobby", "Computer programming"));

        // 设定要上传的文件。UploadFileItem见后面的代码
        ArrayList<UploadFileItem> ufi = new ArrayList<UploadFileItem>();
        ufi.add(new UploadFileItem("upload1", "E:\\Asturias.mp3"));
        ufi.add(new UploadFileItem("upload2", "E:\\full.jpg"));
        ufi.add(new UploadFileItem("upload3", "E:\\dyz.txt"));

        // 类HttpPostEmulator的定义，见后面的代码
        HttpPostEmulator hpe = new HttpPostEmulator();
        String response = null;
        try {
            response = hpe.sendHttpPostRequest(serverUrl, ffkvp, ufi);
        } catch (Exception e) {
            log.error("上传出错",e);
        }
        log.info("Responsefrom server is: " + response);

        return 0;
    }
}
