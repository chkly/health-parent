package com.java.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder pe = this.passwordEncoder();

            auth.inMemoryAuthentication()
                    .withUser("zhangsan")
                    .password(pe.encode("123456"))
                    .roles("normal");
            auth.inMemoryAuthentication()
                    .withUser("lisi")
                    .password(pe.encode("123456"))
                    .roles("normal");
            auth.inMemoryAuthentication()
                    .withUser("admin")
                    .password(pe.encode("admin"))
                    .roles("admin","normal");
        }
    @Bean
    public PasswordEncoder passwordEncoder () {
        //创建PasawordEncoder的实现类bean， 实现类是加密算法
        return new BCryptPasswordEncoder();
    }
}

