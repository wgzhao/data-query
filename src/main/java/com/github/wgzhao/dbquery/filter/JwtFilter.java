package com.github.wgzhao.dbquery.filter;


import com.github.wgzhao.dbquery.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class is a Spring component that extends OncePerRequestFilter, which is a convenient base class for filter
 * implementations. It filters incoming requests and checks for a valid JWT in the Authorization header.
 * If a valid JWT is found, it authenticates the user associated with the token.
 * If the token is absent or not valid, the request is rejected.
 * <p>
 * The component has three dependencies: JwtService, UserService, and TokenService.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final  String requestPath = request.getServletPath();
        final boolean isRefresh = requestPath.equals("/api/v1/auth/refresh");
        final  String authHeader  = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            if (isRefresh) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            return;
            filterChain.doFilter(request, response);
            return;
        }
        String token = jwtService.resolveToken(request);

        if (token != null && jwtService.validateToken(token)) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.getUsername(token));

            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}