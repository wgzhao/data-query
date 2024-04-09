//package com.github.wgzhao.dbquery.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.util.Objects;
//
//public class AdminInterceptor implements HandlerInterceptor {
//    @SuppressWarnings("null")
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
//    {
//        if (Objects.equals(request.getMethod(), "OPTIONS"))
//        {
//            return true;
//        }
//        String token = request.getHeader("token");
//        String validToken = "KUMHYyA6btNnRbsbW5D0fSdyVJUwrTwQ";
//        if (token == null || !token.equals(validToken))
//        {
//            response.sendError(403);
//            return false;
//        }
//        return true;
//    }
//
//
//
//}
