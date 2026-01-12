package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth.RefreshTokenRequestDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth.RefreshTokenResponseDto;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.RefreshToken;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.RefreshTokenRepository;
import com.mzc.backend.lms.domains.user.application.port.in.RefreshTokenUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.*;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 토큰 갱신 서비스
 * RefreshTokenUseCase 구현체 (Port 기반)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final RefreshTokenRepository refreshTokenRepository; // for revokeAllUserTokens
    private final StudentRepositoryPort studentRepositoryPort;
    private final ProfessorRepositoryPort professorRepositoryPort;
    private final JwtProviderPort jwtProviderPort;

    @Override
    @Transactional
    public RefreshTokenResponseDto execute(RefreshTokenRequestDto dto) {
        RefreshToken refreshToken = refreshTokenRepositoryPort.findByToken(dto.getRefreshToken())
                .orElseThrow(AuthException::invalidToken);

        if (!refreshToken.isValid()) {
            throw AuthException.tokenExpired();
        }

        if (!jwtProviderPort.validateToken(dto.getRefreshToken())) {
            throw AuthException.invalidToken();
        }

        User user = refreshToken.getUser();
        UserTypeInfo userTypeInfo = getUserTypeInfo(user);

        String newAccessToken = jwtProviderPort.generateAccessToken(
                user, userTypeInfo.userType, userTypeInfo.userNumber
        );
        String newRefreshToken = jwtProviderPort.generateRefreshToken(user);

        performTokenRotation(refreshToken, user, newRefreshToken);

        log.info("토큰 갱신: userId={}", user.getId());

        return RefreshTokenResponseDto.of(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTokenValid(String refreshToken) {
        Optional<RefreshToken> token = refreshTokenRepositoryPort.findByToken(refreshToken);
        if (token.isEmpty()) {
            return false;
        }

        return token.get().isValid() && jwtProviderPort.validateToken(refreshToken);
    }

    @Override
    @Transactional
    public int revokeAllUserTokens(String userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(Long.parseLong(userId));
        int count = 0;

        for (RefreshToken token : tokens) {
            if (token.isValid()) {
                token.revoke();
                refreshTokenRepositoryPort.save(token);
                count++;
            }
        }

        log.info("사용자 모든 토큰 폐기: userId={}, count={}", userId, count);
        return count;
    }

    private UserTypeInfo getUserTypeInfo(User user) {
        String userType = null;
        Long userNumber = user.getId();

        var student = studentRepositoryPort.findById(userNumber);
        if (student.isPresent()) {
            userType = "STUDENT";
        } else {
            var professor = professorRepositoryPort.findById(userNumber);
            if (professor.isPresent()) {
                userType = "PROFESSOR";
            }
        }

        return new UserTypeInfo(userType, userNumber);
    }

    private void performTokenRotation(RefreshToken oldToken, User user, String newTokenValue) {
        oldToken.revoke();
        refreshTokenRepositoryPort.save(oldToken);

        RefreshToken newToken = RefreshToken.create(
                user,
                newTokenValue,
                oldToken.getDeviceInfo(),
                oldToken.getIpAddress(),
                LocalDateTime.now().plusDays(7)
        );
        refreshTokenRepositoryPort.save(newToken);
    }

    private static class UserTypeInfo {
        final String userType;
        final Long userNumber;

        UserTypeInfo(String userType, Long userNumber) {
            this.userType = userType;
            this.userNumber = userNumber;
        }
    }
}
