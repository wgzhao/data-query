package com.github.wgzhao.dbquery.util;

import com.github.wgzhao.dbquery.entities.Sign;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class SignUtil
{
    public static Sign generateSign()
    {
        Sign sign = new Sign();
        byte[] array = new byte[32];
        new Random().nextBytes(array);
        String appId = DigestUtils.md5DigestAsHex(array).substring(0,16);
        new Random().nextBytes(array);
        String appKey = DigestUtils.md5DigestAsHex(array);
        sign.setAppId(appId);
        sign.setAppKey(appKey);
        return sign;
    }

    public static boolean validSign(String sign, Map<String, String> queryParams, Sign signs)
    {
        // sort real params
        String sb = ParamUtil.sortedParams(queryParams) + signs.toString();
        String validSign = DigestUtils.md5DigestAsHex(sb.getBytes(StandardCharsets.UTF_8));
        return validSign.equals(sign);

    }
}
