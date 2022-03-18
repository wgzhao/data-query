package com.lczq.dbquery.controller;

import org.springframework.web.servlet.HandlerInterceptor;

public class MgtInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception
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
//        if (request.getSession().getAttribute("user") == null)
//        {
//            response.sendRedirect("/login");
//            return false;
//        }
//        return true;
//    }
}
