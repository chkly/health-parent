package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.java.redis","com.java","com.java.utils"})
@EnableScheduling
public class JobApp {
    public static void main(String[] args) {
        SpringApplication.run(JobApp.class,args);
    }
}
