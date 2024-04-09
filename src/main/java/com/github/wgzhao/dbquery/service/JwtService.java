package com.github.wgzhao.dbquery.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.sql.DataSource;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtService
{
    public final static String SECRET = "4017CCCC60E17DE5C84CF03C6CBE559413EA1606";

    @Value("${jwt.expiration.access-token}")
    private int accessTokenExpiration;

    @Value("${jwt.expiration.refresh-token}")
    private int refreshTokenExpiration;

    @Value("${jwt.expiration.reset-password}")
    private int resetPasswordTokenExpiration;

    @Value("${jwt.expiration.enable-account}")
    private int enableAccountTokenExpiration;


    private final static SecretKey KEY =  Keys.hmacShaKeyFor(SECRET.getBytes());


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        if (claims == null) {
            return null;
        }
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username)
    {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String createToken(Map<String, Object> claims, String username)
    {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(KEY)
                .compact();
    }

    public String resolveToken(HttpServletRequest request)
    {

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token, String username)
    {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Check if the token is valid and not expired
    public boolean validateToken(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//
//        try {
//            Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token);
//            return true;
//        }
//        catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
//        }
//        catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token");
//        }
//        catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token");
//        }
//        catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty");
//        }
//        catch (SignatureException ex) {
//            log.error("JWT signature does not match locally computed signature");
//        }
//        return false;
    }

    // Extract the username from the JWT token
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }
    public boolean isTokenExpired(String token)
    {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String jwtToken)
    {
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (SignatureException ex) {
            log.error("JWT signature does not match locally computed signature");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return null;
    }
}
