package com.java;

import com.java.common.pojo.Setmeal;
import com.java.redis.RedisOptBean;
import com.java.serviceprovider.mapper.SetmealMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResiTest {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private RedisOptBean redisOptBean;

    @Test
    public void testCache(){
        Setmeal setmeal12 = setmealMapper.findById(12);
            redisOptBean.set("sermeal12",setmeal12,60);
        Setmeal setmeal13 = setmealMapper.findById(13);
            redisOptBean.set("sermeal12",setmeal13,120);


    }
}
