package com.github.wgzhao.dbquery.filter;

import com.github.wgzhao.dbquery.service.JwtService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


// add @Component to enable the filter
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final  String requestPath = request.getServletPath();
        List<String> whiteApiList = List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/register",
                "/api/v1/auth/validate",
                "/api/v1/auth/refresh",
                "/api/v1/auth/logout",
                "/api/v1/query"
        );
        log.info("current function = JwFilter");

        if (whiteApiList.contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("get token = {}", request.getHeader("Authorization"));
        final boolean isRefresh = requestPath.equals("/api/v1/auth/refresh");

        final  String authHeader  = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("no authentication header or illegal header : '{}'", authHeader);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        String token = null;

        try {
            token = jwtService.resolveToken(request);
            if (token != null && jwtService.validateToken(token)) {
                filterChain.doFilter(request, response);
            } else {
                if (isRefresh) {
                    filterChain.doFilter(request, response);
                    return;
                }
                log.error("invalid token: '{}'", token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        } catch (SignatureException e) {
            log.error("invalid token: '{}'", token);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        String username = jwtService.getUsername(token);
        if (username == null) {
            if (! isRefresh) {
                filterChain.doFilter(request, response);
            }
        }
    }

}