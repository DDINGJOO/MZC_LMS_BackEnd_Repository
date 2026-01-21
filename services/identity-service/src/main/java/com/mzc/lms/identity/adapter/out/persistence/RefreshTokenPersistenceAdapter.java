package com.mzc.lms.identity.adapter.out.persistence;

import com.mzc.lms.identity.adapter.out.persistence.entity.RefreshTokenEntity;
import com.mzc.lms.identity.adapter.out.persistence.repository.RefreshTokenJpaRepository;
import com.mzc.lms.identity.application.port.out.RefreshTokenRepositoryPort;
import com.mzc.lms.identity.domain.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = RefreshTokenEntity.builder()
                .userId(refreshToken.getUserId())
                .token(refreshToken.getToken())
                .deviceInfo(refreshToken.getDeviceInfo())
                .ipAddress(refreshToken.getIpAddress())
                .expiresAt(refreshToken.getExpiresAt())
                .createdAt(refreshToken.getCreatedAt())
                .build();

        RefreshTokenEntity saved = refreshTokenJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token)
                .map(this::toDomain);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenJpaRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenJpaRepository.deleteByToken(token);
    }

    private RefreshToken toDomain(RefreshTokenEntity entity) {
        return RefreshToken.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .token(entity.getToken())
                .deviceInfo(entity.getDeviceInfo())
                .ipAddress(entity.getIpAddress())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
