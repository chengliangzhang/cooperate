package com.maoding.Common;

import com.maoding.common.zeroc.CommonService;
import com.maoding.common.zeroc.CommonServicePrx;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/8 11:21
 * 描    述 :
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})
public class ComonServiceTest {
    private final boolean TEST_LOCAL = false;
    private final boolean SAME_TEST = false;

    @Autowired
    private CommonService localService;

    private CommonServicePrx remote = null;

    private CommonServicePrx getRemote(){
        if (remote == null) {
            CoreRemoteService<CommonServicePrx> prx = new CoreRemoteService<>();
            remote = prx.getServicePrx("CommonService@ConstServer",
                    "--Ice.Default.Locator=IceGrid/Locator:tcp -h 192.168.13.140 -p 4061",
                    CommonServicePrx.class,prx.getClass());
        }
        return remote;
    }

    @Test
    public void testUpdateService() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;

        if (isTestLocal) {
            localService.updateService(null);
        }
    }

    @Test
    public void testGetNewestClient() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;

        String version;
        if (isTestLocal) {
            version = localService.getNewestClient(null);
        }
        assert (StringUtils.isSame(version,"v2.2"));
    }
}
