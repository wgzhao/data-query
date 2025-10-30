package com.github.wgzhao.dbquery.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration(val jwtFilter: JwtFilter, val dataSource: DataSource) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        // Define patterns that Spring Security should handle using a RequestMatcher lambda
        val protectedPaths = RequestMatcher { req: HttpServletRequest ->
            val path = req.requestURI ?: req.servletPath ?: ""
            // match prefix-style paths that were previously matched with Ant-style patterns
            path.startsWith("/admin/api/v1/") || path.startsWith("/admin/")
        }

        http.csrf(Customizer { obj: CsrfConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .cors(Customizer { obj: CorsConfigurer<HttpSecurity?>? -> obj!!.disable() }) // Apply security only to specific patterns
            .securityMatcher(protectedPaths)
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(
                        "/admin/api/v1/auth/**",
                        "/api/v1/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptionHandling: ExceptionHandlingConfigurer<HttpSecurity?>? ->
                exceptionHandling!!
                    .authenticationEntryPoint { _: HttpServletRequest?, response: HttpServletResponse?, _: AuthenticationException? ->
                        response!!.sendError(
                            401,
                            "Unauthorized"
                        )
                    }
            }

        return http.build()
    }

    // Keep the rest of the beans unchanged
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService(dataSource))
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        return authenticationProvider
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager? {
        return config.getAuthenticationManager()
    }

    @Bean
    fun users(dataSource: DataSource?): UserDetailsManager {
        val user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("admin123"))
            .roles("USER")
            .build()
        val admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("USER", "ADMIN")
            .build()
        val users = JdbcUserDetailsManager(dataSource)
        if (!users.userExists("user")) {
            users.createUser(user)
        }
        if (!users.userExists("admin")) {
            users.createUser(admin)
        }
        return users
    }

    @Bean
    fun userDetailsService(dataSource: DataSource?): UserDetailsService {
        return JdbcUserDetailsManager(dataSource)
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            }
        }
    }
}