package com.github.wgzhao.dbquery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password) {
        System.out.println("username: " + username + ", password: " + password);
        return "redirect:/admin";
    }
    @GetMapping("/principal")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
}
