//package com.github.wgzhao.dbquery.service;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.core.Authentication;
//
//import java.util.function.Function;
//
//public interface JwtService {
//    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
//    String createToken(Authentication authentication);
//    String resolveToken(HttpServletRequest request);
//    boolean validateToken(String token);
//
//    String getUsername(String token);
//
//    boolean isTokenExpired(String token);
//
//
//}
