package com.github.wgzhao.dbquery.service.impl;

import com.github.wgzhao.dbquery.dto.AuthenticationRequest;
import com.github.wgzhao.dbquery.dto.AuthenticationResponse;
import com.github.wgzhao.dbquery.entities.User;
import com.github.wgzhao.dbquery.repo.UserRepo;
import com.github.wgzhao.dbquery.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access-token}")
    private int accessTokenExpiration;

    @Value("${jwt.expiration.refresh-token}")
    private int refreshTokenExpiration;

    @Value("${jwt.expiration.reset-password}")
    private int resetPasswordTokenExpiration;

    @Value("${jwt.expiration.enable-account}")
    private int enableAccountTokenExpiration;

    @Autowired
    private final UserRepo userRepo;


    private final SecretKey key;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret, UserRepo userRepo) {
        this.userRepo = userRepo;
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String createToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Check if the token is valid and not expired
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        } catch (SignatureException ex) {
            log.error("JWT signature does not match locally computed signature");
        }
        return false;
    }

    // Extract the username from the JWT token
    public String getUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow();
        // md5 passwd password

        String encryptPasswd = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (encryptPasswd.equals(user.getPassword())) {
            // create a token
            return new AuthenticationResponse(createToken(user), user.getRole());
        } else {
            log.error("username or password not match");
            log.error("the expected password is {}, but the password is {}", user.getPassword(), encryptPasswd);
            return null;
        }

    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String jwToken = resolveToken(request);
        // set the token to expire
        if (jwToken != null) {
            Claims claims = extractAllClaims(jwToken);
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() - 1000);
            Jwts.builder()
                    .subject(claims.getSubject())
                    .issuedAt(new Date())
                    .expiration(expiryDate)
                    .signWith(key)
                    .compact();
        }
    }

    @Override
    public User register(User request) {
        return null;
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwtToken = resolveToken(request);
        if (validateToken(jwtToken)) {
            String username = getUsername(jwtToken);
            User user = userRepo.findByUsername(username).orElseThrow();
            String newToken = createToken(user);
            response.setHeader("Authorization", newToken);
            return new AuthenticationResponse(newToken, user.getRole());
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return null;
        }
    }
}
