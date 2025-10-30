package com.github.wgzhao.dbquery.util

import org.springframework.util.DigestUtils
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import java.util.zip.CRC32
import java.util.zip.Checksum

/**
 * 参数工具类，提供参数合法性校验，由参数生成 Redis Key，以及签名校验
 */
object ParamUtil {
    fun generateKey(params: MutableMap<String, String>): String {
        return DigestUtils.md5DigestAsHex(sortedParams(params).toByteArray())
    }

    fun sortedParams(params: Map<String, String>): String {
        val joiner = StringJoiner("&")
        params.keys.stream().sorted().forEach { key: String? -> joiner.add(key + "=" + params.get(key)) }

        return joiner.toString()
    }

    fun lowercaseParams(params: MutableMap<String, String>): MutableMap<String, String> {
        val lowerParams: MutableMap<String, String> = HashMap<String, String>(params.size)
        for (entry in params.entries) {
            lowerParams[entry.key.lowercase(Locale.getDefault())] = entry.value
        }
        return lowerParams
    }

    /**
     * extract all query parameters from the request
     *
     * @param allParams all query parameters
     * @return a map of query parameters
     */
    fun getQueryParams(allParams: MutableMap<String, String>): MutableMap<String, String> {
        return allParams.keys
            .stream()
            .filter { key: String? -> !key!!.startsWith("_") }
            .collect(
                Collectors.toMap(
                    Function { key: String? -> key },
                    Function { key: String? -> allParams.get(key) })
            )
    }

    fun getControlParams(allParams: Map<String, String>?): Map<String?, String?> {
        if (allParams.isNullOrEmpty()) {
            return Collections.emptyMap()
        }
        return allParams.keys
            .stream()
            .filter { key: String -> key.startsWith("_") }
            .collect(
                Collectors.toMap(
                    Function { key: String? -> key },
                    Function { key: String? -> allParams[key] })
            )
    }

    fun crc32(map: MutableMap<String, String>?): Long {
        if (map.isNullOrEmpty())  return 0L

        val checksum: Checksum = CRC32()
        val bytes = sortedParams(map).toByteArray(StandardCharsets.UTF_8)
        checksum.update(bytes, 0, bytes.size)
        return checksum.value
    }
}
