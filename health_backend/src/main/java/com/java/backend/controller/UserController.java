package com.java.backend.controller;


import com.java.common.constant.MessageConstant;
import com.java.common.entity.Result;
import com.java.redis.RedisOptBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisOptBean redisOptBean;

    @GetMapping("/getMenus")
    public Result getMenus(){
        UserDetails userDetails =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Stream<String> roleStream = authorities.stream().filter(grantedAuthority -> grantedAuthority.getAuthority().startsWith("ROLE_"))
                .map(grantedAuthority -> grantedAuthority.getAuthority());


        List<String> list = roleStream.collect(Collectors.toList());
        String key = list.get(1);
        //获取一级菜单
        List menus = redisOptBean.lrange(key, 0, -1);
        return new Result(true, MessageConstant.GET_MENU_SUCCESS,menus);
    }

    //获取当前登录用户的用户名
    @GetMapping("/getUsername")
    public Result getUsername()throws Exception{
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

}
