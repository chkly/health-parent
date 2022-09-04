package com.java.backend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.java.utils","com.java.backend","com.java.redis"})
@EnableDubbo
public class HealthBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthBackendApplication.class, args);
    }

}
