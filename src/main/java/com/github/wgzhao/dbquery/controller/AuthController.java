package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.dto.AuthRequestDTO;
import com.github.wgzhao.dbquery.dto.CommResponse;
import com.github.wgzhao.dbquery.service.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("${app.api.manage-prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public CommResponse AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            return CommResponse.builder()
                    .code(200)
                    .message("success")
                    .result(Map.of("token", jwtService.generateToken(authRequestDTO.getUsername())))
                    .build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }
}
