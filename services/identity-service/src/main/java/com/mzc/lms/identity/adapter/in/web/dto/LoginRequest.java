package com.mzc.lms.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    private String deviceInfo;
    private String ipAddress;
}
