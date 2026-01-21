package com.mzc.lms.identity.application.port.out;

import com.mzc.lms.identity.domain.model.User;

/**
 * JWT Provider Port
 */
public interface JwtProviderPort {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    Long extractUserId(String token);

    String extractEmail(String token);

    String extractUserType(String token);

    boolean validateToken(String token);

    boolean isTokenExpired(String token);
}
