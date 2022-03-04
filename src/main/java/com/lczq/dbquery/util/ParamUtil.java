package com.lczq.dbquery.util;

import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 参数工具类，提供参数合法性校验，由参数生成 Redis Key，以及签名校验
 */
public class ParamUtil
{
    public static String generateKey(Map<String,String> params)
    {

        return DigestUtils.md5DigestAsHex(sortedParams(params).getBytes());
    }

    public static String sortedParams(Map<String, String> params)
    {
        final List<String> collect = params.keySet().stream().filter(key -> key.startsWith("_")).collect(Collectors.toList());
        collect.forEach(params::remove);
        StringJoiner joiner = new StringJoiner("&");
        params.keySet().stream().sorted().forEach(key -> joiner.add(key + "=" + params.get(key)));

        return joiner.toString();
    }
}
