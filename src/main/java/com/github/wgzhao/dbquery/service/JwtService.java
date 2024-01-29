package com.github.wgzhao.dbquery.service;

import com.github.wgzhao.dbquery.entities.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.function.Function;

public interface JwtService {
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String createToken(User user);
    String resolveToken(HttpServletRequest request);
    boolean validateToken(String token);

    String getUsername(String token);

    boolean isTokenExpired(String token);


}
