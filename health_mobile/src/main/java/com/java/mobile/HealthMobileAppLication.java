package com.java.mobile;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.java.utils","com.java.mobile","com.java.redis"})
@EnableDubbo
public class HealthMobileAppLication {
    public static void main(String[] args) {
        SpringApplication.run(HealthMobileAppLication.class,args);
    }
}
