package com.mzc.lms.identity.application.service;

import com.mzc.lms.identity.adapter.in.web.dto.LoginRequest;
import com.mzc.lms.identity.adapter.in.web.dto.LoginResponse;
import com.mzc.lms.identity.adapter.in.web.dto.TokenRefreshRequest;
import com.mzc.lms.identity.adapter.in.web.dto.TokenRefreshResponse;
import com.mzc.lms.identity.application.port.in.AuthenticationUseCase;
import com.mzc.lms.identity.application.port.out.*;
import com.mzc.lms.identity.domain.event.UserAuthenticatedEvent;
import com.mzc.lms.identity.domain.model.RefreshToken;
import com.mzc.lms.identity.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final UserRepositoryPort userRepository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final JwtProviderPort jwtProvider;
    private final EventPublisherPort eventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.access-token-expiration:3600000}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}")
    private Long refreshTokenExpiration;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 활성화 여부 확인
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("비활성화된 계정입니다.");
        }

        // 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        // 기존 리프레시 토큰 삭제 후 새로 저장
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .deviceInfo(request.getDeviceInfo())
                .ipAddress(request.getIpAddress())
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .createdAt(LocalDateTime.now())
                .build());

        // 인증 이벤트 발행
        eventPublisher.publishUserAuthenticated(
                UserAuthenticatedEvent.of(user.getId(), user.getEmail(), user.getUserType(),
                        request.getIpAddress(), request.getDeviceInfo())
        );

        log.info("로그인 성공: userId={}, email={}", user.getId(), user.getEmail());

        return LoginResponse.of(accessToken, refreshToken, accessTokenExpiration,
                user.getId(), user.getEmail(), user.getName(), user.getUserType(), user.getUserNumber());
    }

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String oldRefreshToken = request.getRefreshToken();

        // 리프레시 토큰 유효성 검증
        if (!jwtProvider.validateToken(oldRefreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 저장된 리프레시 토큰 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리프레시 토큰입니다."));

        // 만료 확인
        if (storedToken.isExpired()) {
            refreshTokenRepository.deleteByToken(oldRefreshToken);
            throw new IllegalArgumentException("만료된 리프레시 토큰입니다.");
        }

        // 사용자 조회
        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 새 토큰 생성
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        // 리프레시 토큰 교체
        refreshTokenRepository.deleteByToken(oldRefreshToken);
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId())
                .token(newRefreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .createdAt(LocalDateTime.now())
                .build());

        log.info("토큰 갱신 성공: userId={}", user.getId());

        return TokenRefreshResponse.of(newAccessToken, newRefreshToken, accessTokenExpiration);
    }

    @Override
    @Transactional
    public void logout(String accessToken) {
        if (jwtProvider.validateToken(accessToken)) {
            Long userId = jwtProvider.extractUserId(accessToken);
            refreshTokenRepository.deleteByUserId(userId);
            log.info("로그아웃 성공: userId={}", userId);
        }
    }

    @Override
    public boolean validateToken(String token) {
        return jwtProvider.validateToken(token);
    }
}
