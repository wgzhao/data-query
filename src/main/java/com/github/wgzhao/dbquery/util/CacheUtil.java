package com.github.wgzhao.dbquery.util;

import jakarta.annotation.Resource;
import lombok.NonNull;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CacheUtil
{

    @Resource
    private RedisTemplate<String, Serializable> serializableRedisTemplate;

    public final String CACHE_KEY_PREFIX = "q_";

    public Serializable get(final String key)
    {
        return serializableRedisTemplate.opsForValue().get(CACHE_KEY_PREFIX + key);
    }

    public void set(final String key, final @NonNull Serializable value, long expireTime)
    {
        serializableRedisTemplate.opsForValue().set(CACHE_KEY_PREFIX + key, value, expireTime, TimeUnit.SECONDS);
    }

    public boolean exists(final String key)
    {
        return Boolean.TRUE.equals(serializableRedisTemplate.hasKey(CACHE_KEY_PREFIX + key));
    }

    public int deleteKeys(String selectId)
    {
        int deletedNum = 0;
        final Set<String> keys = serializableRedisTemplate.keys(CACHE_KEY_PREFIX + selectId + "_*");
        if (keys == null || keys.isEmpty()) {
            return deletedNum;
        }
        for (String key : keys) {
            serializableRedisTemplate.delete(key);
            deletedNum++;
        }
        return deletedNum;
    }
}
