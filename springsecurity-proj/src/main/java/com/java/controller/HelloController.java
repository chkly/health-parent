package com.java.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    @PreAuthorize(value = "hasAnyRole('admin','normal')")
    public String hello(){
        return "hello normal,admin都访问";
    }
    @GetMapping("/helloAdmin")
    @PreAuthorize(value = "hasRole('admin')")
    public String helloAdmin(){
        return "hello admin访问";
    }
}
