package com.java.mobile.controller;

import com.java.common.constant.MessageConstant;
import com.java.common.constant.RedisConstant;
import com.java.common.entity.Result;
import com.java.common.pojo.Order;
import com.java.redis.RedisOptBean;
import com.java.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private RedisOptBean redisOptBean;

    /**
     * 体检预约
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        //从Redis中获取缓存的验证码，key为手机号+“_"+disConstant.SENDTYPE_ORDER
        String codeInRedis =String.valueOf(redisOptBean.get(telephone+"-"+ RedisConstant.SENDTYPE_ORDER));
        String validateCode = (String) map.get("validateCode");
        //校验手机验证码
        if(codeInRedis == null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result =null;
        //调用体检预约服务
        try{
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        }catch (Exception e){
            e.printStackTrace();
            //预约失败
            return result;
        }
        if(result.isFlag()){

            return new Result(true,"预约成功",map);
            //预约成功，发送短信通知
//            String orderDate = (String) map.get("orderDate");
//            try {
//                SmsUtil.sendShortMessage(telephone,orderDate);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        return result;
    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
 }
