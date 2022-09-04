package com.java.job;

import com.java.common.constant.RedisConstant;
import com.java.redis.RedisOptBean;
import com.java.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
/**
 * 自定义Job，实现定时清理垃圾图片
 */
@Component
public class ClearImgJob {

    @Autowired
    private RedisOptBean redisOptBean;
    @Autowired
    private QiniuUtil qiniuUtil;

    @Scheduled(cron = "0 0 2 * * ? ")
    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = redisOptBean.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String picName : set) {
                //删除七牛云服务器上的图片
                qiniuUtil.deleteFromQiniu(picName);
                //从Redis集合中删除图片名称
                redisOptBean.setRemove(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            }
        }
    }
}
