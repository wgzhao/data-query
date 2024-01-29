//package com.github.wgzhao.dbquery.service.impl;
//
//import com.github.wgzhao.dbquery.dto.AuthenticationRequest;
//import com.github.wgzhao.dbquery.dto.AuthenticationResponse;
//import com.github.wgzhao.dbquery.entities.User;
//import com.github.wgzhao.dbquery.repo.UserRepo;
//import com.github.wgzhao.dbquery.service.AuthenticationService;
//import com.github.wgzhao.dbquery.service.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationServiceImpl implements AuthenticationService {
//    private final JwtService jwtService;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final UserRepo userRepo;
//    @Override
//    public AuthenticationResponse login(AuthenticationRequest request) {
//        Authentication authenticate = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//        User user = userRepo.findByUsername(request.getUsername())
//                .orElseThrow();
//        return  new AuthenticationResponse(jwtService.createToken(authenticate));
//    }
//
//    @Override
//    public User register(User request) {
//        return null;
//    }
//
//    @Override
//    public User refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return null;
//    }
//}
