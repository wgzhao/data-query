package com.github.wgzhao.dbquery.util

import jakarta.annotation.Resource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.concurrent.TimeUnit

@Service
class CacheUtil {
    @Resource
    private val serializableRedisTemplate: RedisTemplate<String?, Serializable?>? = null

    val CACHE_KEY_PREFIX: String = "q_"

    fun get(key: String?): Serializable? {
        return serializableRedisTemplate!!.opsForValue().get(CACHE_KEY_PREFIX + key)
    }

    fun set(key: String?, value: Serializable, expireTime: Long) {
        serializableRedisTemplate!!.opsForValue().set(CACHE_KEY_PREFIX + key, value, expireTime, TimeUnit.SECONDS)
    }

    fun exists(key: String?): Boolean {
        return java.lang.Boolean.TRUE == serializableRedisTemplate!!.hasKey(CACHE_KEY_PREFIX + key)
    }

    fun deleteKeys(selectId: String?): Int {
        var deletedNum = 0
        val keys = serializableRedisTemplate!!.keys(CACHE_KEY_PREFIX + selectId + "_*")
        if (keys.isEmpty()) {
            return 0
        }
        for (key in keys) {
            if (key != null) {
                serializableRedisTemplate.delete(key)
            }
            deletedNum++
        }
        return deletedNum
    }
}
