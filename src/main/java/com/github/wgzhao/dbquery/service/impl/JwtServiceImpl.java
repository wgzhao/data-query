//package com.github.wgzhao.dbquery.service.impl;
//
//import com.github.wgzhao.dbquery.service.JwtService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.UnsupportedJwtException;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.function.Function;
//
//@Slf4j
//@Service
//public class JwtServiceImpl implements JwtService {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration.access-token}")
//    private int accessTokenExpiration;
//
//    @Value("${jwt.expiration.refresh-token}")
//    private int refreshTokenExpiration;
//
//    @Value("${jwt.expiration.reset-password}")
//    private int resetPasswordTokenExpiration;
//
//    @Value("${jwt.expiration.enable-account}")
//    private int enableAccountTokenExpiration;
//
//
//    private final SecretKey key;
//
//    public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
//        this.secret = secret;
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    @Override
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    public String createToken(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);
//
//        return Jwts.builder()
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date())
//                .expiration(expiryDate)
//                .signWith(key)
//                .compact();
//    }
//
//    public String resolveToken(HttpServletRequest request) {
//
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    // Check if the token is valid and not expired
//    public boolean validateToken(String token) {
//
//        try {
//            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
//            return true;
//        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty");
//        }
//        return false;
//    }
//
//    // Extract the username from the JWT token
//    public String getUsername(String token) {
//
//        return extractAllClaims(token).getSubject();
//    }
//
//    @Override
//    public boolean isTokenExpired(String token) {
//        return extractClaim(token, Claims::getExpiration).before(new Date());
//    }
//
//    private Claims extractAllClaims(String jwtToken) {
//        return Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseSignedClaims(jwtToken)
//                .getPayload();
//    }
//}
