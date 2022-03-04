package com.lczq.dbquery.util;

import com.lczq.dbquery.entities.SignEntity;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static com.lczq.dbquery.constant.Constants.SIGN;

public class SignUtil
{
    public static SignEntity generateSign()
    {
        SignEntity signEntity = new SignEntity();
        byte[] array = new byte[32];
        new Random().nextBytes(array);
        String appId = DigestUtils.md5DigestAsHex(array).substring(0,16);
        new Random().nextBytes(array);
        String appKey = DigestUtils.md5DigestAsHex(array);
        signEntity.setAppId(appId);
        signEntity.setAppKey(appKey);
        return signEntity;
    }

    public static boolean validSign(Map<String, String> allParams, SignEntity signEntity)
    {
        String passSign = allParams.get(SIGN);
        // sort real params
        String sb = ParamUtil.sortedParams(allParams) + signEntity.toString();
        String validSign = DigestUtils.md5DigestAsHex(sb.getBytes(StandardCharsets.UTF_8));
        return validSign.equals(passSign);

    }
}
