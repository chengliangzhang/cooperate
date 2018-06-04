package com.maoding.Company;


import com.maoding.company.dao.CompanyDao;
import com.maoding.company.entity.CompanyEntity;
import com.maoding.company.zeroc.CompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/25 15:08
 * 描    述 :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})
public class CompanyServiceImplTest {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyDao companyDao;

    @Test
    public void testInsert() throws Exception {
        CompanyEntity entity = new CompanyEntity();
        entity.setCompanyName("测测插入");
        entity.reset();
        entity.setCreateBy("aaaaa");
        entity.setLastModifyTime(new Date());
        entity.setLastModifyUserId("bbbbbb");
        companyDao.insert(entity);
    }
}