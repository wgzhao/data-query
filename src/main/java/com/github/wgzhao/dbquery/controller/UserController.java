//package com.github.wgzhao.dbquery.controller;
//
//import com.github.wgzhao.dbquery.dto.AuthenticationRequest;
//import com.github.wgzhao.dbquery.dto.AuthenticationResponse;
//import com.github.wgzhao.dbquery.service.AuthenticationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final AuthenticationService authenticationService;
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public AuthenticationResponse doLogin(@RequestParam("username") String username,
//                                          @RequestParam("password") String password) {
//        System.out.println("username: " + username + ", password: " + password);
//        return authenticationService.login(new AuthenticationRequest(username, password));
//    }
//    @GetMapping("/principal")
//    public Principal getPrincipal(Principal principal) {
//        return principal;
//    }
//}
