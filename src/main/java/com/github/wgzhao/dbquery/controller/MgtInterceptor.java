package com.github.wgzhao.dbquery.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class MgtInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String token = request.getHeader("token");
        String validToken = "KUMHYyA6btNnRbsbW5D0fSdyVJUwrTwQ";
        if (token == null || !token.equals(validToken))
        {
            response.sendError(403);
            return false;
        }
        return true;
    }
}
