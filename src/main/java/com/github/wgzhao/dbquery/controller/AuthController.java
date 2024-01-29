package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.dto.AuthenticationRequest;
import com.github.wgzhao.dbquery.dto.AuthenticationResponse;
import com.github.wgzhao.dbquery.entities.CommResponse;
import com.github.wgzhao.dbquery.service.AuthenticationService;
import com.github.wgzhao.dbquery.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;
    @PostMapping("/login")
    public AuthenticationResponse authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
            return authenticationService.login(authenticationRequest);
    }

    @GetMapping("/validate")
    public CommResponse validate(HttpServletRequest request) {
        String token = jwtService.resolveToken(request);
        if (token == null) {
            return new CommResponse(false, "Unauthorized");
        }
        if (jwtService.validateToken(token)) {
            return new CommResponse(true, "Authorized");
        } else {
            return new CommResponse(false, "Unauthorized");
        }
    }
}
