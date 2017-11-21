package com.maoding.Storage;

import com.maoding.Base.BaseController;
import com.maoding.Bean.ApiResponse;
import com.maoding.FileServer.zeroc.FileDTO;
import com.maoding.FileServer.zeroc.FileRequestDTO;
import com.maoding.Storage.Dto.RequestUploadParamsDTO;
import com.maoding.Storage.zeroc.CooperateFileDTO;
import com.maoding.Storage.zeroc.StorageService;
import com.maoding.Storage.zeroc.StorageServicePrx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/30 20:22
 * 描    述 :
 */
@RestController
@RequestMapping("/StorageServer")
public class StorageServiceController extends BaseController {
    @Autowired
    private StorageService storageService;

    private StorageServicePrx storageServicePrx = null;

    @RequestMapping(value = "/listFileLink", method = RequestMethod.POST)
    public ApiResponse setFileServerType(@RequestBody FileDTO fileDTO) {
        List<CooperateFileDTO> list = storageService.listFileLink(fileDTO,null);
        return ApiResponse.success(list);
    }

    @RequestMapping(value = "/requestUpload", method = RequestMethod.POST)
    public ApiResponse requestUpload(@RequestBody RequestUploadParamsDTO params) {
        CooperateFileDTO fileInfo = params.getFileInfo();
        int mode = params.getMode();
        storageServicePrx = StorageServiceImpl.getInstance();
        FileRequestDTO fileRequestDTO = storageServicePrx.requestUpload(fileInfo,mode);
        return ApiResponse.success(fileRequestDTO);
    }

}
