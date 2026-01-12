package com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 로그인 요청 DTO
 */
@Data
public class LoginRequestDto {

    @NotBlank(message = "아이디는 필수입니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    private String deviceInfo;
    private String ipAddress;

    public boolean isEmailFormat() {
        return username != null && username.contains("@");
    }
}
