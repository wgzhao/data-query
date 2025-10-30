package com.github.wgzhao.dbquery.config

import com.github.wgzhao.dbquery.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Slf4j
@Component
class JwtFilter

    : OncePerRequestFilter() {
    @Autowired
    private val jwtService: JwtService? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var username: String? = null
        val token = jwtService!!.resolveToken(request)
        if (token != null) {
            username = jwtService.extractUsername(token)
        }

        if (username == null) {
            filterChain.doFilter(request, response)
            return
        }
        // validate token is expired or not
        if (jwtService.isTokenExpired(token)) {
            filterChain.doFilter(request, response)
            return
        }

        val authenticationToken =
            UsernamePasswordAuthenticationToken(username, null, mutableListOf<GrantedAuthority?>())

        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authenticationToken
        filterChain.doFilter(request, response)
    }
}