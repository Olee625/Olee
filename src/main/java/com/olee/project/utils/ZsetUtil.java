package com.olee.project.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ZsetUtil {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public ZsetUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 添加一个元素, zset与set最大的区别就是每个元素都有一个score，因此有个排序的辅助功能;  zadd
     *
     * @param key
     * @param value
     * @param score
     */
    public void add(String key, String value, double score) {
        Boolean result = redisTemplate.opsForZSet().add(key, value, score);
        log.debug("key:{},token:{},add方法的储存结果：{}", key, value, result);

    }

    /**
     * 删除元素
     *
     * @param key
     * @param value
     */
    public long remove(String key, String value) {
        long removeCount = redisTemplate.opsForZSet().remove(key, value);
        log.debug("key:{},token:{},remove方法删除的key数量为:{}", key, value, removeCount);
        return removeCount;
    }

    /**
     * 删除整个key
     *
     * @param key
     */
    public boolean removeKey(String key) {
        return redisTemplate.delete(key);

    }

    /**
     * 查询value对应的score   zscore
     *
     * @param key
     * @param value
     */
    public Double score(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 根据索引值移除区间元素。
     */
    public long removeRange(String key, long start, long end) {
        long removeCount = redisTemplate.opsForZSet().removeRange(key, start, end);
        redisTemplate.opsForZSet().range(key, 0, -1);
        log.debug("key:{},通过removeRange方法移除元素的个数:{}", key, removeCount);
        return removeCount;
    }

    /**
     * 设置key的过期时间。
     */
    public void setExpireOnKey(String key, long timeout, TimeUnit unit) {

        Boolean result = redisTemplate.expire(key, timeout, unit);
        log.info("key:{},通过setExpireOnKey方法设置过期时间是否成功:{}", key, result);
    }


}
