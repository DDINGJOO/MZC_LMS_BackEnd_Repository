package com.mzc.backend.lms.domains.user.auth.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.user.auth.dto.*;
import com.mzc.backend.lms.domains.user.auth.email.service.EmailVerificationService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import com.mzc.backend.lms.domains.user.auth.service.AuthService;
import com.mzc.backend.lms.domains.user.auth.swagger.AuthControllerSwagger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @Override
    @PostMapping("/signup/email-verification")
    public ResponseEntity<ApiResponse<ApiResponse.MessageResponse>> sendVerificationCode(
            @Valid @RequestBody EmailVerificationRequestDto dto) {
        if (!authService.isEmailAvailable(dto.getEmail())) {
            throw AuthException.emailAlreadyExists(dto.getEmail());
        }

        emailVerificationService.sendVerificationCode(dto.getEmail());

        return ResponseEntity.ok(ApiResponse.successMessage("인증 코드가 발송되었습니다."));
    }

    @Override
    @PostMapping("/signup/verify-code")
    public ResponseEntity<ApiResponse<ApiResponse.MessageResponse>> verifyCode(
            @Valid @RequestBody VerifyCodeRequestDto dto) {
        boolean verified = emailVerificationService.verifyCode(dto.getEmail(), dto.getCode());

        if (!verified) {
            throw AuthException.emailVerificationFailed();
        }

        return ResponseEntity.ok(ApiResponse.successMessage("이메일 인증이 완료되었습니다."));
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto dto) {
        String userId = authService.signup(dto);

        SignupResponseDto response = new SignupResponseDto(userId, "회원가입이 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto dto,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        dto.setIpAddress(ipAddress);

        LoginResponseDto response = authService.login(dto, ipAddress);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponseDto>> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto dto) {
        RefreshTokenResponseDto response = authService.refreshToken(dto);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<ApiResponse.MessageResponse>> logout(
            @RequestHeader(value = "Refresh-Token", required = false) String refreshToken) {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            authService.logout(refreshToken);
        }

        return ResponseEntity.ok(ApiResponse.successMessage("로그아웃되었습니다."));
    }

    @Override
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<EmailAvailabilityResponseDto>> checkEmailAvailability(
            @RequestParam String email) {
        boolean available = authService.isEmailAvailable(email);

        EmailAvailabilityResponseDto response = new EmailAvailabilityResponseDto(
                available,
                available ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다."
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                if (ip.contains(",")) {
                    return ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        return request.getRemoteAddr();
    }
}
