package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.RefreshToken;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.RefreshTokenRepository;
import com.mzc.backend.lms.domains.user.application.port.out.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * RefreshToken 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    @Override
    public int deleteExpiredTokens() {
        return refreshTokenRepository.deleteExpiredOrRevokedTokens(LocalDateTime.now());
    }
}
