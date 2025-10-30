package com.github.wgzhao.dbquery.service

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Slf4j
@Component
class JwtService {
    @Value("\${jwt.expiration.access-token}")
    private val accessTokenExpiration = 0

    @Value("\${jwt.expiration.refresh-token}")
    private val refreshTokenExpiration = 0

    @Value("\${jwt.expiration.reset-password}")
    private val resetPasswordTokenExpiration = 0

    @Value("\${jwt.expiration.enable-account}")
    private val enableAccountTokenExpiration = 0

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims?, T?>): T? {
        val claims = extractAllClaims(token)
        if (claims == null) {
            return null
        }
        return claimsResolver.apply(claims)
    }

    fun generateToken(username: String?): String? {
        val claims: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        return createToken(claims, username)
    }

    fun createToken(claims: MutableMap<String?, Any?>?, username: String?): String? {
        val now = Date()
        val expiryDate = Date(now.getTime() + accessTokenExpiration)

        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(Date())
            .expiration(expiryDate)
            .signWith(KEY)
            .compact()
    }

    fun resolveToken(request: HttpServletRequest): String? {
        var bearerToken: String?
        bearerToken = request.getHeader("Authorization")
        if (!StringUtils.hasText(bearerToken)) {
            bearerToken = request.getHeader("authorization")
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    fun validateToken(token: String?, username: String?): Boolean {
        val tokenUsername = extractUsername(token)
        return (tokenUsername == username && !isTokenExpired(token))
    }

    // Check if the token is valid and not expired
    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.getUsername() && !isTokenExpired(token))
    }

    // Extract the username from the JWT token
    fun extractUsername(token: String?): String {
        return extractClaim<String>(token, java.util.function.Function { obj: Claims? -> obj.getSubject() })!!
    }

    private fun extractExpiration(token: String?): Date? {
        return extractClaim<Date?>(token, Function { obj: Claims? -> obj!!.getExpiration() })
    }

    fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token)!!.before(Date())
    }

    private fun extractAllClaims(jwtToken: String?): Claims? {
        try {
            return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
        } catch (ex: ExpiredJwtException) {
            JwtService.log.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            JwtService.log.error("Unsupported JWT token")
        } catch (ex: MalformedJwtException) {
            JwtService.log.error("Invalid JWT token")
        } catch (ex: SignatureException) {
            JwtService.log.error("JWT signature does not match locally computed signature")
        } catch (ex: IllegalArgumentException) {
            JwtService.log.error("JWT claims string is empty")
        }
        return null
    }

    companion object {
        const val SECRET: String = "4017CCCC60E17DE5C84CF03C6CBE559413EA1606"

        private val KEY: SecretKey = Keys.hmacShaKeyFor(SECRET.toByteArray())
    }
}
