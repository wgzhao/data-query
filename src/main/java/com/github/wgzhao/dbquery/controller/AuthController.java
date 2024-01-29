//package com.github.wgzhao.dbquery.controller;
//
//import com.github.wgzhao.dbquery.dto.AuthenticationRequest;
//import com.github.wgzhao.dbquery.dto.AuthenticationResponse;
//import com.github.wgzhao.dbquery.service.impl.JwtServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/api/v1/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtServiceImpl tokenProvider;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authenticationRequest.getUsername(),
//                        passwordEncoder.encode(authenticationRequest.getPassword())
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.createToken(authentication);
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//    }
//}
