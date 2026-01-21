package com.mzc.lms.identity.application.port.out;

import com.mzc.lms.identity.domain.model.RefreshToken;

import java.util.Optional;

/**
 * RefreshToken Repository Port
 */
public interface RefreshTokenRepositoryPort {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    void deleteByToken(String token);
}
