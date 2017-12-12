package com.maoding.Organization;

import com.maoding.Base.BaseLocalService;
import com.maoding.Organization.zeroc.*;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/10 19:12
 * 描    述 :
 */
@Service("organizationService")
public class OrganizationServiceImpl extends BaseLocalService<OrganizationServicePrx> implements OrganizationService,OrganizationServicePrx {
//    @Autowired
//    OrganizationDao organizationDao;

    /** 同步方式获取业务接口代理对象 */
    public static OrganizationServicePrx getInstance(String adapterName) {
        OrganizationServiceImpl prx = new OrganizationServiceImpl();
        return prx.getServicePrx("OrganizationService",adapterName,OrganizationServicePrx.class,_OrganizationServicePrxI.class);
    }
    public static OrganizationServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public List<OrganizationDTO> listOrganizationByUserId(String userId, Current current) {
        return null;
    }

    @Override
    public List<OrganizationDTO> listOrganizationForCurrent(Current current) {
        return null;
    }

    @Override
    public List<CompanyDTO> listCompanyByUserId(String userId, Current current) {
        return null;
//        List<OrganizationEntity> entityList = organizationDao.selectAll();
//        List<CompanyDTO> list = new ArrayList<>();
//        for (OrganizationEntity entity : entityList){
//            CompanyDTO dto = new CompanyDTO();
//            BeanUtils.copyProperties(entity,dto);
//            list.add(dto);
//        }
//        return list;
    }
}
