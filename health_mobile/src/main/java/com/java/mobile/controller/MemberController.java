package com.java.mobile.controller;

import com.java.common.constant.MessageConstant;
import com.java.common.constant.RedisConstant;
import com.java.common.entity.Result;
import com.java.common.pojo.Member;
import com.java.service.MemberService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 会员登录
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private RedisTemplate redisTemplate;

    //使用手机号和验证码登录
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码
        BoundValueOperations opt = redisTemplate.boundValueOps(telephone + "-" + RedisConstant.SENDTYPE_LOGIN);
        String codeInRedis = String.valueOf(opt.get());
        //校验手机验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证码输入正确
        //判断当前用户是否为会员
        Member member = memberService.findByTelephone(telephone);
        if(member == null){
            //当前用户不是会员，自动完成注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //登录成功
        //写入Cookie，跟踪用户
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//路径
        cookie.setMaxAge(60*60*24*30);//有效期30天
        response.addCookie(cookie);
        //保存会员信息到Redis中，key为手机号，值是用户信息

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}