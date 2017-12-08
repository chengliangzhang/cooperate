package com.maoding.WebService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maoding.Base.BaseLocalService;
import com.maoding.Utils.HttpUtils;
import com.maoding.WebService.zeroc.*;
import com.zeroc.Ice.Current;
import groovy.util.logging.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/4 18:10
 * 描    述 :
 */
@Service("/webService")
@Slf4j
public class WebServiceImpl extends BaseLocalService<WebServicePrx> implements WebService, WebServicePrx {
    private static final String ERROR_MESSAGE = "WebServiceImpl is fail:";
    private static final String LOGIN_URL = "http://localhost:8080/iWork/sys/login";
    private static final String GET_PROJECTS = "http://localhost:8080/iWork/project/getProjects";
    private String type = "application/json";

    private CloseableHttpClient client = null;

    /**
     * 同步方式获取业务接口代理对象
     */
    public static WebServicePrx getInstance(String adapterName) {
        WebServiceImpl prx = new WebServiceImpl();
        return prx.getServicePrx("StorageService", adapterName, WebServicePrx.class, _WebServicePrxI.class);
    }

    public static WebServicePrx getInstance() {
        return getInstance(null);
    }

    /**
     * 方法描述：登录
     * 作   者：DongLiu
     * 日   期：2017/12/4 18:14
     *
     * @return:
     * @param：uerName,pwd
     */
    @Override
    public AccountDTO getLogin(AccountDTO dto, Current current) {
        try {
            //获取Http登录信息
            HttpEntity httpEntity = getHttpEntity(dto);
            if (null != httpEntity) {
                //TODO 拼装返回信息
                getLoginStatus(dto, httpEntity);
                //关闭HttpEntity
                EntityUtils.consume(httpEntity);
            }
        } catch (Exception e) {
            log.error(ERROR_MESSAGE + "getLogin" + e.getMessage());
            dto.setStatus("1");
        }
        return dto;
    }

    /**
     * 方法描述：拼装用户登录信息
     * 作   者：DongLiu
     * 日   期：2017/12/5 14:46
     *
     * @param
     * @return
     */
    private void getLoginStatus(AccountDTO dto, HttpEntity httpEntity) throws IOException {
        String jsonStr = EntityUtils.toString(httpEntity);
        JSONObject jsStr = JSONObject.parseObject(jsonStr);
        if ("0".equals(jsStr.get("code"))) {
            dto.setStatus("0");
        }
    }

    /**
     * 方法描述：http创建连接
     * 作   者：DongLiu
     * 日   期：2017/12/5 14:42
     *
     * @param
     * @return
     */
    private HttpEntity getHttpEntity(AccountDTO dto) {
        client = HttpClients.createDefault();
        CloseableHttpResponse response = HttpUtils.postData(client, LOGIN_URL,
                type, dto);
        if (200 != response.getStatusLine().getStatusCode()) {
            log.error(ERROR_MESSAGE + "getHttpEntity is fail");
            return null;
        }
        return response.getEntity();
    }

    /**
     * 方法描述：获取项目信息
     * 作   者：DongLiu
     * 日   期：2017/12/4 18:14
     *
     * @param
     * @return
     */
    @Override
    public List<ProjectDTO> getProjects(Map<String, String> params, Current current) {
        List<ProjectDTO> projectDTOList = new ArrayList<ProjectDTO>();
        CloseableHttpResponse response = HttpUtils.postData(client, GET_PROJECTS,
                type, params);
        HttpEntity httpEntity = response.getEntity();
        try {
            if (null != httpEntity) {
                String jsonStr = EntityUtils.toString(httpEntity);
                JSONObject jsStr = JSONObject.parseObject(jsonStr);
                //拼装projectList
                projectDTOList = getProjectDTOS(jsStr);
            }
        } catch (IOException e) {
            log.error(ERROR_MESSAGE + "getProjects is fail");
        }
        return projectDTOList;
    }

    /**
     * 方法描述：拼装projectList
     * 作   者：DongLiu
     * 日   期：2017/12/5 18:37
     *
     * @param
     * @return
     */
    private List<ProjectDTO> getProjectDTOS(JSONObject jsStr) {
        List<ProjectDTO> projectDTOList;
        String jsonObject = JSONObject.toJSON(jsStr.get("data")).toString();
        JSONObject jsonObj = JSON.parseObject(jsonObject);
        projectDTOList = JSONObject.parseArray(jsonObj.get("data").toString(), ProjectDTO.class);
        return projectDTOList;
    }

    /**
     * 方法描述：基本信息
     * 作   者：DongLiu
     * 日   期：2017/12/4 18:14
     *
     * @param
     * @return
     */
    @Override
    public ProjectDTO loadProjectDetails(String projectId, Current current) {
        return null;
    }

    /**
     * 方法描述：任务签发
     * 作   者：DongLiu
     * 日   期：2017/12/4 18:14
     *
     * @param
     * @return
     */
    @Override
    public ProjectDTO getIssueInfo(String projectId, Current current) {
        return null;
    }

    /**
     * 方法描述：生产安排
     * 作   者：DongLiu
     * 日   期：2017/12/4 18:14
     *
     * @param
     * @return
     */
    @Override
    public ProjectDTO getDesignTaskList(QueryProjectTaskDTO query, Current current) {
        return null;
    }

    /**
     * 方法描述：我的任务
     * 作   者：DongLiu
     * 日   期：2017/12/4 18:14
     *
     * @param
     * @return
     */
    @Override
    public TaskDTO getMyTaskList4(Map<String, String> params, Current current) {
        return null;
    }
}
