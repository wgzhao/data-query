package com.github.wgzhao.dbquery.controller;

import com.github.wgzhao.dbquery.dto.AuthRequestDTO;
import com.github.wgzhao.dbquery.dto.JwtResponseDTO;
import com.github.wgzhao.dbquery.service.JwtService;
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

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                    .build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }

//    @GetMapping("/validate")
//    public CommResponse validate(HttpServletRequest request) {
//        String token = jwtService.resolveToken(request);
//        if (token == null) {
//            return new CommResponse(false, "Unauthorized");
//        }
//        if (jwtService.validateToken(token)) {
//            return new CommResponse(true, "Authorized");
//        } else {
//            return new CommResponse(false, "Unauthorized");
//        }
//    }

//    @GetMapping("/refresh")
//    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return jwtService.refreshToken(request, response);
//    }
//
//    @GetMapping("/logout")
//    public CommResponse logout(HttpServletRequest request, HttpServletResponse response) {
//        jwtService.logout(request, response);
//        return new CommResponse(true, "Logout success");
//    }
}
