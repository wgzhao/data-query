package com.github.wgzhao.dbquery.util

import com.github.wgzhao.dbquery.entities.Sign
import org.springframework.util.DigestUtils
import java.nio.charset.StandardCharsets
import java.util.*

object SignUtil {
    fun generateSign(): Sign {
        val sign = Sign()
        val array = ByteArray(32)
        Random().nextBytes(array)
        val appId = DigestUtils.md5DigestAsHex(array).substring(0, 16)
        Random().nextBytes(array)
        val appKey = DigestUtils.md5DigestAsHex(array)
        sign.setAppId(appId)
        sign.setAppKey(appKey)
        return sign
    }

    fun validSign(sign: String?, queryParams: MutableMap<String?, String?>, signs: Sign): Boolean {
        // sort real params
        val sb = ParamUtil.sortedParams(queryParams) + signs.toString()
        val validSign = DigestUtils.md5DigestAsHex(sb.toByteArray(StandardCharsets.UTF_8))
        return validSign == sign
    }
}
