package com.bomaos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.wf.jwtp.configuration.EnableJwtPermission;

@EnableJwtPermission
@EnableAsync
@MapperScan("com.bomaos.**.mapper")
@SpringBootApplication
public class BomaosApplication /*extends SpringBootServletInitializer*/ {

    public static void main(String[] args) {
        SpringApplication.run(BomaosApplication.class, args);
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ZlianWebApplication.class);
    }*/

}
