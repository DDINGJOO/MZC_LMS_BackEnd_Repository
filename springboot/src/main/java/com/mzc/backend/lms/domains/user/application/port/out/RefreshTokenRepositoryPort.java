package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.RefreshToken;

import java.util.Optional;

/**
 * RefreshToken 영속성 Port
 */
public interface RefreshTokenRepositoryPort {

    /**
     * 리프레시 토큰 저장
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * 토큰 값으로 조회
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * 사용자 ID로 모든 토큰 삭제
     */
    void deleteByUserId(Long userId);

    /**
     * 토큰 삭제
     */
    void delete(RefreshToken refreshToken);

    /**
     * 만료된 토큰 삭제
     */
    int deleteExpiredTokens();
}
