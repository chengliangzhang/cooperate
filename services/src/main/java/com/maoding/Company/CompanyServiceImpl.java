package com.maoding.Company;

import com.maoding.Base.BaseLocalService;
import com.maoding.Company.zeroc.CompanyDTO;
import com.maoding.Company.zeroc.CompanyService;
import com.maoding.Company.zeroc.CompanyServicePrx;
import com.maoding.Company.zeroc._CompanyServicePrxI;
import com.zeroc.Ice.Current;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/10/10 19:12
 * 描    述 :
 */
@Service("companyService")
public class CompanyServiceImpl extends BaseLocalService<CompanyServicePrx> implements CompanyService,CompanyServicePrx {
//    @Autowired
//    OrganizationDao organizationDao;

    /** 同步方式获取业务接口代理对象 */
    public static CompanyServicePrx getInstance(String adapterName) {
        CompanyServiceImpl prx = new CompanyServiceImpl();
        return prx.getServicePrx("CompanyService",adapterName,CompanyServicePrx.class,_CompanyServicePrxI.class);
    }
    public static CompanyServicePrx getInstance() {
        return getInstance(null);
    }

    @Override
    public List<CompanyDTO> listCompanyForCurrent(Current current) {
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
