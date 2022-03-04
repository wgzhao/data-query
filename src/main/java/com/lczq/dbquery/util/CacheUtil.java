package com.lczq.dbquery.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
public class CacheUtil
{

    @Resource
    private RedisTemplate<String, Serializable> serializableRedisTemplate;

    public  final String CACHE_KEY_PREFIX = "q_";

    public Serializable get(final String key)
    {
        return serializableRedisTemplate.opsForValue().get(CACHE_KEY_PREFIX + key);
    }

    public  void set(final String key, final Serializable value, long expireTime)
    {
        serializableRedisTemplate.opsForValue().set(CACHE_KEY_PREFIX + key, value, expireTime, TimeUnit.SECONDS);
    }

    public  boolean exists(final String key)
    {
        return Boolean.TRUE.equals(serializableRedisTemplate.hasKey(CACHE_KEY_PREFIX + key));
    }
}
