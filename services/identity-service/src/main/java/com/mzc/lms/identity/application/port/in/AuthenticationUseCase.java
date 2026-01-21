package com.mzc.lms.identity.application.port.in;

import com.mzc.lms.identity.adapter.in.web.dto.LoginRequest;
import com.mzc.lms.identity.adapter.in.web.dto.LoginResponse;
import com.mzc.lms.identity.adapter.in.web.dto.TokenRefreshRequest;
import com.mzc.lms.identity.adapter.in.web.dto.TokenRefreshResponse;

/**
 * 인증 UseCase
 */
public interface AuthenticationUseCase {

    /**
     * 로그인
     */
    LoginResponse login(LoginRequest request);

    /**
     * 토큰 갱신
     */
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);

    /**
     * 로그아웃
     */
    void logout(String accessToken);

    /**
     * 토큰 검증
     */
    boolean validateToken(String token);
}
