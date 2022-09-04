package com.java.mobile.controller;

import com.java.common.constant.MessageConstant;
import com.java.common.constant.RedisConstant;
import com.java.common.entity.Result;
import com.java.redis.RedisOptBean;
import com.java.utils.SmsUtil;
import com.java.utils.ValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信验证码
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private RedisOptBean redisOptBean;

    //体检预约时发送手机验证码
    @RequestMapping("/sendSmForOrder")
    public Result sendSmForOrder(String telephone){
        Integer code = ValidateCodeUtil.generateValidateCode(6);;//生成6位数字验证码
        try {
            //发送短信
            SmsUtil.sendShortMessage(code.toString(),telephone);
        } catch (Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis
        redisOptBean.set(telephone+"-"+ RedisConstant.SENDTYPE_ORDER,code,180);
        //验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //手机快速登录时发送手机验证码
    @RequestMapping("/sendSmForLogin")
    public Result sendSmForLogin(String telephone){
        Integer code = ValidateCodeUtil.generateValidateCode(6);//生成6位数字验证码
        try {
            //发送短信
            SmsUtil.sendShortMessage(code.toString(),telephone);
        } catch (Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis
        redisOptBean.set(telephone+"-"+ RedisConstant.SENDTYPE_LOGIN,code,180);
        //验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}