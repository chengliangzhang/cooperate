package com.maoding;

import com.maoding.Organization.zeroc.CompanyDTO;
import com.maoding.Organization.zeroc.OrganizationService;
import com.maoding.Utils.SpringContextUtils;
import com.zeroc.Ice.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Import({SpringContextUtils.class})
@EnableAspectJAutoProxy(exposeProxy = true)

@SpringBootApplication
@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.maoding"})
public class Application extends SpringApplication {
    @Autowired
    private OrganizationService organizationService;



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        List<String> params = new ArrayList<>();
        params.addAll(Arrays.asList(args));
        params.add("--Ice.Config=services/src/main/resources/properties/ice-config.properties");
        com.zeroc.IceBox.Server.main(params.toArray(new String[params.size()]));
    }

    @RequestMapping(value = "iWork/org/listCompanyByRemote", method = RequestMethod.GET)
    @ResponseBody
    public String listCompanyByRemote(){
        List<CompanyDTO> list = organizationService.listCompanyByUserId("",(Current)null);
        StringBuilder s = new StringBuilder();
        for (CompanyDTO dto : list) {
            s.append(dto.getId()).append(":").append(dto.getCompanyName()).append("<br/>");
        }
        return s.toString();
    }
}
