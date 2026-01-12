package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.application.port.in.LogoutUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그아웃 서비스
 * LogoutUseCase 구현체 (Port 기반)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Override
    @Transactional
    public boolean execute(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return true;
        }

        refreshTokenRepositoryPort.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.revoke();
                    refreshTokenRepositoryPort.save(token);
                    log.info("로그아웃: userId={}", token.getUser().getId());
                });

        return true;
    }
}
