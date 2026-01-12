package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth.*;
import com.mzc.backend.lms.domains.user.application.port.in.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 인증 서비스 Facade
 * UseCase 패턴을 통해 각 비즈니스 로직을 위임
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthFacadeService {

    private final SignupUseCase signupUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final CheckEmailUseCase checkEmailUseCase;

    /**
     * 회원가입
     */
    public String signup(SignupRequestDto dto) {
        log.debug("회원가입 요청: email={}", dto.getEmail());
        return signupUseCase.execute(dto);
    }

    /**
     * 로그인
     */
    public LoginResponseDto login(LoginRequestDto dto, String ipAddress) {
        log.debug("로그인 요청: username={}", dto.getUsername());
        return loginUseCase.execute(dto, ipAddress);
    }

    /**
     * 토큰 갱신
     */
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto dto) {
        log.debug("토큰 갱신 요청");
        return refreshTokenUseCase.execute(dto);
    }

    /**
     * 로그아웃
     */
    public void logout(String refreshToken) {
        log.debug("로그아웃 요청");
        logoutUseCase.execute(refreshToken);
    }

    /**
     * 이메일 사용 가능 여부 확인
     */
    public boolean isEmailAvailable(String email) {
        log.debug("이메일 중복 확인: email={}", email);
        return checkEmailUseCase.execute(email);
    }
}
