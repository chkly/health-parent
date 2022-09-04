package com.java.common.constant;

/**
 * @author lujun
 * @Description Redis常量类
 * @Date 2021/11/21 15:26
 */
public class RedisConstant {
    //套餐图片所有图片名称(Redis的set集合的key)
    public static final String  SETMEAL_PIC_RESOURCES="pic_resources";
    //套餐图片保存在数据库中的图片名称(Redis的Set集合的key)
    public static final String  SETMEAL_PIC_DB_RESOURCES="pic_db_resources";
    public static final String SENDTYPE_ORDER = "001";//用于缓存体检预约时发送的验证码
    public static final String SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
    public static final String SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码

}

