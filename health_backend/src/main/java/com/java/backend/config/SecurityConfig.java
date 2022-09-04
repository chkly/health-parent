package com.java.backend.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity  //启用SpringSecurity功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      //  System.out.println("进来了:" + userDetailsService);
        //基于数据库配置账号和密码
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    /**
     * 在下面的configure方法中实现常用配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/403.html");  //自定义403页面
        //自定义安全退出的接口路径及退出后跳转到登录页面
        http.logout().logoutUrl("/logout").invalidateHttpSession(true)
                .logoutSuccessUrl("/login.html").permitAll();
        http.formLogin()
                .loginPage("/login.html")  //自定义登录页面路径
                .loginProcessingUrl("/login")  //自定义登录页面的提交路径
                .defaultSuccessUrl("/pages/main.html")  //登录成功后，跳转到指定路径
                .failureUrl("/login.html")  //登录失败，跳转到/login.html
                .and()
                .authorizeRequests()
                .antMatchers("/login.html","/css/**","/js/**","/img/**","/plugins/**").permitAll()
                .antMatchers("/swagger-ui.html","/webjars/**","/v2/**","/swagger-resources/**").permitAll()

                .anyRequest()
                .authenticated();   //其它任何请求都要认证后才能访问
        http.csrf().disable();  //禁用跨站访问
        http.headers().frameOptions().sameOrigin();  //支持在同一个域中访问iframe
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //创建PasawordEncoder的实现类bean， 实现类是加密算法
        return new BCryptPasswordEncoder();

    }

}
