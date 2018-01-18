package com.maoding.Project;

import com.maoding.Base.BaseLocalService;
import com.maoding.Bean.CoreResponse;
import com.maoding.Common.Config.WebServiceConfig;
import com.maoding.Project.zeroc.ProjectDTO;
import com.maoding.Project.zeroc.ProjectService;
import com.maoding.Project.zeroc.ProjectServicePrx;
import com.maoding.Utils.BeanUtils;
import com.maoding.Utils.FileUtils;
import com.maoding.Utils.HttpUtils;
import com.zeroc.Ice.Current;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/12/22 16:13
 * 描    述 :
 */
@Service("projectService")
@Transactional(rollbackFor = Exception.class)
public class ProjectServiceImpl extends BaseLocalService<ProjectServicePrx> implements ProjectService,ProjectServicePrx{
    @Autowired
    WebServiceConfig webServiceConfig;

    @Override
    public ProjectDTO getProjectInfoById(String id, Current current) {
        CloseableHttpResponse response = HttpUtils.postData(webServiceConfig.getClient(), webServiceConfig.getLoadProjectDetailsUrl() + "/" + id);
        if (!HttpUtils.isResponseOK(response)) return null;
        CoreResponse result = HttpUtils.getResult(response);
        FileUtils.close(response);
        assert (result != null);
        assert (result.getData() != null);
        ProjectDTO projectInfo = BeanUtils.createFrom(result.getData(),ProjectDTO.class);
        return projectInfo;
    }

}
