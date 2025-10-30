package com.github.wgzhao.dbquery.controller

import com.github.wgzhao.dbquery.dto.ApiResponse
import com.github.wgzhao.dbquery.dto.AuthRequestDTO
import com.github.wgzhao.dbquery.service.JwtService
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import java.util.Map

@RestController
@CrossOrigin
@RequestMapping("\${app.api.manage-prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication APIs")
class AuthController {
    private val jwtService: JwtService? = null
    private val authenticationManager: AuthenticationManager? = null


    @PostMapping("/login")
    fun authenticateAndGetToken(@RequestBody authRequestDTO: AuthRequestDTO): ApiResponse<MutableMap<String?, String?>?> {
        val authentication = authenticationManager!!.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequestDTO.username,
                authRequestDTO.password
            )
        )
        if (authentication.isAuthenticated) {
            return ApiResponse.Companion.success<MutableMap<String?, String?>?>(
                Map.of<String?, String?>("token", jwtService!!.generateToken(authRequestDTO.username))
            )
        } else {
            throw UsernameNotFoundException("invalid user request..!!")
        }
    }
}
