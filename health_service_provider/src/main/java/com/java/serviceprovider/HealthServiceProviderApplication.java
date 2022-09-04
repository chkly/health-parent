package com.java.serviceprovider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.java.redis","com.java.serviceprovider"})
@EnableDubbo
@MapperScan("com.java.serviceprovider.mapper")
public class HealthServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthServiceProviderApplication.class, args);
    }

}
