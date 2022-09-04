package com.java.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lujun
 * @Description Redis通用操作类
 * @Date 2021/12/16 12:14
 */
@Component
public class RedisOptBean {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /***************key的通用操作**************************/
    /**
     * 设置key的过期时间
     *
     * @param key
     * @param time
     * @return
     */
    private boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 不能为null
     * @return 时间(秒)，当key不存在或已过期，返回-2.返回-1表示永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param keys 可以传一个或多个key
     */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(keys));
            }
        }
    }
    /***************String类型操作**************************/
    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.boundValueOps(key).get();
    }

    /**
     * 把数据放到缓存中
     *
     * @param key
     * @param value
     * @return
     */
    public void set(String key, Object value) {
        redisTemplate.boundValueOps(key).set(value);
    }


    /**
     * 将数据放到缓存中，并设置过期时间
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     */
    public void set(String key, Object value, long time) {
        redisTemplate.boundValueOps(key).set(value, time, TimeUnit.SECONDS);
    }

    /**
     * 递增
     *
     * @param key  键
     * @param data 要增加几(大于0)
     * @return
     */
    public long incr(String key, long data) {
        if (data < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.boundValueOps(key).increment(data);
    }

    /**
     * 递减
     *
     * @param key  键
     * @param data 要减少几(大于0)
     * @return
     */
    public long decr(String key, long data) {
        if (data < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.boundValueOps(key).decrement(data);
    }
    /***************Set类型操作**************************/
    /**
     * 根据key获取Set中的所有成员
     *
     * @param key 键
     * @return
     */
    public Set smembers(String key) {
        try {
            return redisTemplate.boundSetOps(key).members();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询value是否是set中的成员
     *
     * @param key
     * @param value
     * @return
     */
    public boolean isMember(String key, Object value) {
        try {
            return redisTemplate.boundSetOps(key).isMember(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sadd(String key, Object... values) {
        try {
            return redisTemplate.boundSetOps(key).add(values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将数据放入set缓存.并设置有效期
     *
     * @param key
     * @param time   有效期，单位秒
     * @param values
     * @return
     */
    public long saddWithTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.boundSetOps(key).add(values);
            this.expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long scard(String key) {
        try {
            return redisTemplate.boundSetOps(key).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
	/**
     * 获取key1,key2对应的Set集合的差集
     *
     * @param key1 大集合的key
     * @param key2 小集合的key
     * @return
     */
    public Set sdiff(String key1, String key2) {
        Set diff = redisTemplate.boundSetOps(key1).diff(key2);
        return diff;
    }

    /**
     * 从Set中移除值为value的成员
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.boundSetOps(key).remove(values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /***************List类型操作**************************/
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List lrange(String key, long start, long end) {
        try {
            return redisTemplate.boundListOps(key).range(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long llen(String key) {
        try {
            return redisTemplate.boundListOps(key).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；
     *              index<0时，-1，表尾，-2倒数第二个元素,依次类推
     * @return
     */
    public Object getValue(String key, long index) {
        try {
            return redisTemplate.boundListOps(key).index(index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void rpush(String key, Object value) {
        redisTemplate.boundListOps(key).rightPush(value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public void rpush(String key, Object value, long time) {
        redisTemplate.boundListOps(key).rightPush(value);
        //设置过期时间
        this.expire(key, time);

    }

    /**
     * 将list放入缓存
     *
     * @param key    键
     * @param values 值
     * @return
     */
    public void rpushAll(String key, List values) {
        redisTemplate.boundListOps(key).rightPushAll(values);
    }

    /**
     * 将list放入缓存
     *
     * @param key    键
     * @param values 值
     * @param time   时间(秒)
     * @return
     */
    public void rpushAll(String key, List values, long time) {
        redisTemplate.boundListOps(key).rightPushAll(values);
        this.expire(key, time);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.boundListOps(key).set(index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value的成员
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.boundListOps(key).remove(count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
