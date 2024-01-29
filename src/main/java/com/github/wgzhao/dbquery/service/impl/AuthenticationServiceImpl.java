package com.github.wgzhao.dbquery.service.impl;

import com.github.wgzhao.dbquery.dto.AuthenticationRequest;
import com.github.wgzhao.dbquery.dto.AuthenticationResponse;
import com.github.wgzhao.dbquery.entities.User;
import com.github.wgzhao.dbquery.repo.UserRepo;
import com.github.wgzhao.dbquery.service.AuthenticationService;
import com.github.wgzhao.dbquery.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final UserRepo userRepo;
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow();
        // md5 passwd password
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(request.getPassword().getBytes());
            String encryptPasswd = DatatypeConverter.printHexBinary(md5.digest()).toLowerCase();
            if (encryptPasswd.equals(user.getPassword())) {
                // create a token
                return new AuthenticationResponse(jwtService.createToken(user));
            } else {
                log.error("username or password not match");
                log.error("the expected password is {}, but the password is {}", user.getPassword(), encryptPasswd);
                return null;
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("md5 algorithm not found", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User register(User request) {
        return null;
    }

    @Override
    public User refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return null;
    }
}
