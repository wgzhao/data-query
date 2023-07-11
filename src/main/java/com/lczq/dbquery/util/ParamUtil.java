package com.lczq.dbquery.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * 参数工具类，提供参数合法性校验，由参数生成 Redis Key，以及签名校验
 */
public class ParamUtil
{
    public static String generateKey(Map<String, String> params)
    {

        return DigestUtils.md5DigestAsHex(sortedParams(params).getBytes());
    }

    public static String sortedParams(Map<String, String> params)
    {
        StringJoiner joiner = new StringJoiner("&");
        params.keySet().stream().sorted().forEach(key -> joiner.add(key + "=" + params.get(key)));

        return joiner.toString();
    }

    public static Map<String, String> lowercaseParams(Map<String, String> params)
    {
        Map<String, String> lowerParams = new HashMap<>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            lowerParams.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return lowerParams;
    }

    /**
     * extract all query parameters from the request
     *
     * @param allParams all query parameters
     * @return a map of query parameters
     */
    public static Map<String, String> getQueryParams(Map<String, String> allParams)
    {
        return allParams.keySet().stream().filter(key -> !key.startsWith("_")).collect(Collectors.toMap(key -> key, allParams::get));
    }

    public static Map<String, String> getControlParams(Map<String, String> allParams)
    {
        return allParams.keySet().stream().filter(key -> key.startsWith("_")).collect(Collectors.toMap(key -> key, allParams::get));
    }

    public static long crc32(Map<String, String> map)
    {
        Checksum checksum = new CRC32();
        byte[] bytes = sortedParams(map).getBytes(StandardCharsets.UTF_8);
        checksum.update(bytes, 0, bytes.length);
        return checksum.getValue();
    }
}
