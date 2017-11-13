package com.maoding.Organization;

import com.maoding.Base.BaseLocalService;
import com.maoding.Organization.Dao.OrganizationDao;
import com.maoding.Organization.Entity.OrganizationEntity;
import com.maoding.Organization.zeroc.CompanyDTO;
import com.maoding.Organization.zeroc.OrganizationService;
import com.maoding.Organization.zeroc.OrganizationServicePrx;
import com.maoding.Organization.zeroc._OrganizationServicePrxI;
import com.maoding.Utils.BeanUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/10 19:12
 * 描    述 :
 */
@Service("organizationService")
public class OrganizationServiceImpl extends BaseLocalService<OrganizationServicePrx> implements OrganizationService,OrganizationServicePrx {
    @Autowired
    OrganizationDao organizationDao;
    
    /** 同步方式获取业务接口代理对象 */
    public static OrganizationServicePrx getInstance(String adapterName) {
        OrganizationServiceImpl prx = new OrganizationServiceImpl();
        return prx.getServicePrx("OrganizationService",adapterName,OrganizationServicePrx.class,_OrganizationServicePrxI.class);
    }
    public static OrganizationServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public List<CompanyDTO> listCompanyByUserId(String userId, Current current) {
        List<OrganizationEntity> entityList = organizationDao.selectAll();
        List<CompanyDTO> list = new ArrayList<>();
        for (OrganizationEntity entity : entityList){
            CompanyDTO dto = new CompanyDTO();
            BeanUtils.copyProperties(entity,dto);
            log.info(entity.getCreateDate().toString());
            list.add(dto);
        }
        return list;
    }
}
