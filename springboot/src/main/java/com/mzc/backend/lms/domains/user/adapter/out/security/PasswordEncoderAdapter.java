package com.mzc.backend.lms.domains.user.adapter.out.security;

import com.mzc.backend.lms.domains.user.application.port.out.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Password Encoder Adapter
 * BCrypt 기반 비밀번호 암호화 구현
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoderAdapter() {
        this.encoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            return null;
        }
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encodedPassword);
    }
}
