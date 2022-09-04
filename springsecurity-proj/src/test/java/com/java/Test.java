package com.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class Test {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @org.junit.jupiter.api.Test
    public void test1(){
        String encode = passwordEncoder.encode("admin");
        System.out.println("encode:"+encode);
    }
}
