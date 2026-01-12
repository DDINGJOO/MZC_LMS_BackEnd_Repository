package com.mzc.backend.lms.domains.user.adapter.out.security;

import com.mzc.backend.lms.domains.user.application.port.out.JwtProviderPort;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Provider Adapter
 * JWT 토큰 생성 및 검증 구현
 */
@Component
public class JwtProviderAdapter implements JwtProviderPort {

    @Value("${security.jwt.secret:defaultJwtSecretKeyForDevelopment123456789012345678901234567890}")
    private String secretKey;

    @Value("${security.jwt.access-token-expire-time:1800000}")
    private Long accessTokenExpiration;

    @Value("${security.jwt.refresh-token-expire-time:604800000}")
    private Long refreshTokenExpiration;

    @Override
    public String generateAccessToken(User user, String userType, Long userNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("userType", userType);
        claims.put("userNumber", userNumber.toString());

        return createToken(claims, user.getEmail(), accessTokenExpiration);
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("tokenType", "REFRESH");

        return createToken(claims, user.getEmail(), refreshTokenExpiration);
    }

    @Override
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    @Override
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String extractUserType(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userType", String.class);
    }

    @Override
    public String extractUserNumber(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userNumber", String.class);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
