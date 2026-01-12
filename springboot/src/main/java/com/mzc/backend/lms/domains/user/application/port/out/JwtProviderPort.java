package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;

/**
 * JWT Provider Port
 * JWT 토큰 생성 및 검증을 위한 인터페이스
 */
public interface JwtProviderPort {

    /**
     * Access Token 생성
     *
     * @param user 사용자 엔티티
     * @param userType 사용자 타입 (STUDENT/PROFESSOR)
     * @param userNumber 학번 또는 교번
     * @return 생성된 Access Token
     */
    String generateAccessToken(User user, String userType, Long userNumber);

    /**
     * Refresh Token 생성
     *
     * @param user 사용자 엔티티
     * @return 생성된 Refresh Token
     */
    String generateRefreshToken(User user);

    /**
     * 토큰에서 사용자 ID 추출
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    Long extractUserId(String token);

    /**
     * 토큰에서 이메일 추출
     *
     * @param token JWT 토큰
     * @return 이메일
     */
    String extractEmail(String token);

    /**
     * 토큰에서 사용자 타입 추출
     *
     * @param token JWT 토큰
     * @return 사용자 타입 (STUDENT/PROFESSOR)
     */
    String extractUserType(String token);

    /**
     * 토큰에서 학번/교번 추출
     *
     * @param token JWT 토큰
     * @return 학번 또는 교번
     */
    String extractUserNumber(String token);

    /**
     * 토큰 유효성 검증
     *
     * @param token JWT 토큰
     * @return 유효 여부
     */
    boolean validateToken(String token);

    /**
     * 토큰 만료 여부 확인
     *
     * @param token JWT 토큰
     * @return 만료 여부
     */
    boolean isTokenExpired(String token);
}
