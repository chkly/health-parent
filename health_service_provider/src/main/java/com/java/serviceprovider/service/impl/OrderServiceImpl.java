package com.java.serviceprovider.service.impl;

import com.java.common.constant.MessageConstant;
import com.java.common.entity.Result;
import com.java.common.pojo.Member;
import com.java.common.pojo.Order;
import com.java.common.pojo.OrderSetting;
import com.java.service.OrderService;
import com.java.serviceprovider.mapper.MemberMapper;
import com.java.serviceprovider.mapper.OrderMapper;
import com.java.serviceprovider.mapper.OrderSettingMapper;
import com.java.utils.DateUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;

    //体检预约
    @Override
    public Result order(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtil.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingMapper.findByOrderDate(date);
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if(reservations >= number){
            //预约已满，不能预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberMapper.findByTelephone(telephone);
        //防止重复预约
        if(member != null){
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> list = orderMapper.findByCondition(order);
            if(list != null && list.size() > 0){
                //已经完成了预约，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingMapper.editReservationsByOrderDate(orderSetting);

        if(member == null){
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberMapper.add(member);
        }

        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                date,
                (String)map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderMapper.add(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    //根据id查询预约信息，包括体检人信息、套餐信息
    public Map findById(Integer id) throws Exception {
        Map map = orderMapper.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtil.parseDate2String(orderDate));
        }
        return map;
    }
}
