package com.github.wgzhao.dbquery.util

import jakarta.servlet.http.HttpServletRequest

object HttpUtil {
    fun getClientIpAddr(request: HttpServletRequest): String? = request.clientIp()
}

// Extension function provides a concise, idiomatic Kotlin API.
fun HttpServletRequest.clientIp(): String? {
    val headerKeys = listOf(
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR",
        "X-Real-IP",
        "X-Client-IP"
    )

    return headerKeys.firstNotNullOfOrNull { key ->
        this.getHeader(key)
            ?.split(',')
            ?.asSequence()
            ?.map { it.trim() }
            ?.firstOrNull { it.isNotEmpty() && !it.equals("unknown", ignoreCase = true) }
    } ?: this.remoteAddr
}
