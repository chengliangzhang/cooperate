package com.maoding.FileServer;

import com.maoding.Base.BaseController;
import com.maoding.Bean.ApiResponse;
import com.maoding.FileServer.zeroc.FileServicePrx;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/30 20:22
 * 描    述 :
 */
@RestController
@RequestMapping("/FileServer")
public class FileServiceController extends BaseController {

    private FileServicePrx fileService = FileServiceImpl.getInstance();

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ApiResponse upload(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public ApiResponse download(HttpServletRequest request) {
        return null;
    }
}
