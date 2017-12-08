package com.maoding.WebService;

import com.maoding.WebService.zeroc.AccountDTO;
import com.maoding.WebService.zeroc.WebService;
import com.maoding.WebService.zeroc.WebServicePrx;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/4 18:32
 * 描    述 :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WebServiceImplTest {
    @Autowired
    private WebService webService;
    private WebServicePrx webServicePrx = WebServiceImpl.getInstance();

    private String cellphone = "18589035085";
    private String pwd = "30116992";

    @Test
    public void getLogin() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCellphone(cellphone);
        accountDTO.setPassword(pwd);
        accountDTO = webService.getLogin(accountDTO, null);
        Assert.assertEquals("0", accountDTO.getStatus());
    }

    @Test
    public void getProjects() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCellphone(cellphone);
        accountDTO.setPassword(pwd);
        webService.getLogin(accountDTO, null);
        Assert.assertEquals("0", accountDTO.getStatus());
        Map<String, String> params = new HashMap<>();
        webService.getProjects(params, null);
    }

    @Test
    public void loadProjectDetails() throws Exception {
    }

    @Test
    public void getIssueInfo() throws Exception {
    }

    @Test
    public void getDesignTaskList() throws Exception {
    }

    @Test
    public void getMyTaskList4() throws Exception {
    }

}