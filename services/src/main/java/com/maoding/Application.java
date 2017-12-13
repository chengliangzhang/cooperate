package com.maoding;

import com.maoding.Utils.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@Import({SpringContextUtils.class})
@EnableAspectJAutoProxy(exposeProxy = true)

@SpringBootApplication
@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.maoding"})
public class Application extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        IceRunner iceRunner = SpringContextUtils.getBean(IceRunner.class);
        iceRunner.run(args);
    }
}
