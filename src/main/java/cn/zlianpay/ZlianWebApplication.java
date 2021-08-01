package cn.zlianpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.wf.jwtp.configuration.EnableJwtPermission;

@EnableJwtPermission
@EnableAsync
@MapperScan("cn.zlianpay.**.mapper")
@SpringBootApplication
public class ZlianWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ZlianWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ZlianWebApplication.class);
    }

}
