package com.github.wgzhao.dbquery.util

import jakarta.servlet.http.HttpServletRequest

object HttpUtil {
    fun getClientIpAddr(request: HttpServletRequest): String? {
        var ip = request.getHeader("X-Forwarded-For")

        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("X-Real-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("X-Client-IP")
        }
        if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getRemoteAddr()
        }

        return ip
    }
}
