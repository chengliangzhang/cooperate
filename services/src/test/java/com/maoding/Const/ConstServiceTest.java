package com.maoding.Const;

import com.maoding.common.LocalConstService;
import com.maoding.common.zeroc.*;
import com.maoding.coreBase.CoreRemoteService;
import com.maoding.coreUtils.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
* ConstService Tester.
*
* @author Zhangchengliang
* @since 01/13/2018
* @version 1.0
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding"})

public class ConstServiceTest {
    private final boolean TEST_LOCAL = false;
    private final boolean SAME_TEST = false;

    @Autowired
    private ConstService localService;

    private ConstServicePrx remote = null;

    private ConstServicePrx getRemote(){
        if (remote == null) {
            CoreRemoteService<ConstServicePrx> prx = new CoreRemoteService<>();
            remote = prx.getServicePrx("ConstService@ConstServer",
                    "--Ice.Default.Locator=IceGrid/Locator:tcp -h 192.168.13.140 -p 4061",
                    ConstServicePrx.class,_ConstServicePrxI.class);
        }
        return remote;
    }

    @Test
    public void testConvertPath() throws Exception {
        StringElementDTO pathElement = new StringElementDTO();
        pathElement.setPath("a/b/c.txt");
        pathElement.setIssuePath("t1/t2");
        String s = StringUtils.formatPath(LocalConstService.convertString("/{Project}/{Classic1}/{IssuePath}/{File}",pathElement));
        Assert.assertNotNull(s);
    }

    @Test
    public void testGetNewestVersion() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;

        VersionSimpleDTO version;

        for (int i=0; i<3; i++) {
            if (isTestLocal) {
                version = localService.getNewestVersion(null, null);
            }
            assert (version != null);
            assert (StringUtils.isSame(version.getService(), "v1.1"));
            assert (StringUtils.isSame(version.getClient(), "v2.2"));
        }
    }

    @Test
    public void testGetNewestVersionSpecial() throws Exception {
        final boolean isTestLocal = SAME_TEST ? TEST_LOCAL : true;

        VersionSimpleQuery query = new VersionSimpleQuery();
        query.setService("v1.0");

        VersionSimpleDTO version;
        for (int i=0; i<5; i++) {
            if (isTestLocal) {
                version = localService.getNewestVersion(query, null);
            }
            assert (version != null);
            assert (StringUtils.isSame(version.getService(), "v1.0"));
            assert (StringUtils.isSame(version.getClient(), "v2.1"));
        }
    }

}
