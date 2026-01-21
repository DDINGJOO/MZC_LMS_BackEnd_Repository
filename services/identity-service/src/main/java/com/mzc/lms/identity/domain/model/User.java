package com.mzc.lms.identity.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User Domain Model (Identity Service)
 * 인증에 필요한 최소한의 사용자 정보만 포함
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private String userType;  // STUDENT, PROFESSOR, ADMIN
    private Long userNumber;  // 학번 또는 교번
    private String name;
    private boolean enabled;

    public boolean isStudent() {
        return "STUDENT".equals(userType);
    }

    public boolean isProfessor() {
        return "PROFESSOR".equals(userType);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(userType);
    }
}
