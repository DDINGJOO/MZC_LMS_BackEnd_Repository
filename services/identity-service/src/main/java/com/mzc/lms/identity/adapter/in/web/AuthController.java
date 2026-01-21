package com.mzc.lms.identity.adapter.in.web;

import com.mzc.lms.identity.adapter.in.web.dto.LoginRequest;
import com.mzc.lms.identity.adapter.in.web.dto.LoginResponse;
import com.mzc.lms.identity.adapter.in.web.dto.TokenRefreshRequest;
import com.mzc.lms.identity.adapter.in.web.dto.TokenRefreshResponse;
import com.mzc.lms.identity.application.port.in.AuthenticationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "인증 API")
public class AuthController {

    private final AuthenticationUseCase authenticationUseCase;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        // IP 주소 설정
        request.setIpAddress(getClientIpAddress(httpRequest));

        LoginResponse response = authenticationUseCase.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새 액세스 토큰을 발급받습니다.")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {

        TokenRefreshResponse response = authenticationUseCase.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 세션을 로그아웃합니다.")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authenticationUseCase.logout(token);
        }

        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }

    @GetMapping("/validate")
    @Operation(summary = "토큰 검증", description = "액세스 토큰의 유효성을 검증합니다.")
    public ResponseEntity<Map<String, Boolean>> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        boolean isValid = false;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            isValid = authenticationUseCase.validateToken(token);
        }

        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    @GetMapping("/health")
    @Operation(summary = "헬스체크", description = "서비스 상태를 확인합니다.")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "identity-service"));
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
