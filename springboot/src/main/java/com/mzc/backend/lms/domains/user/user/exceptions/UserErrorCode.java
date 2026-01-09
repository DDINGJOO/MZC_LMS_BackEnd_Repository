package com.mzc.backend.lms.domains.user.user.exceptions;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * User 도메인 에러 코드
 * <p>
 * 사용자, 학생, 교수, 암호화 관련 에러 코드를 정의합니다.
 * </p>
 *
 * @see DomainErrorCode
 */
@Getter
public enum UserErrorCode implements DomainErrorCode {

    // User 관련 에러 (USER_0XX)
    USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_002", "이미 존재하는 사용자입니다", HttpStatus.CONFLICT),
    USER_PROFILE_NOT_FOUND("USER_003", "사용자 프로필을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    USER_CONTACT_NOT_FOUND("USER_004", "사용자 연락처를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // Student 관련 에러 (STUDENT_0XX)
    STUDENT_NOT_FOUND("STUDENT_001", "학생 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    STUDENT_ALREADY_EXISTS("STUDENT_002", "이미 존재하는 학번입니다", HttpStatus.CONFLICT),
    INVALID_STUDENT_NUMBER("STUDENT_003", "유효하지 않은 학번입니다", HttpStatus.BAD_REQUEST),
    STUDENT_DEPARTMENT_NOT_FOUND("STUDENT_004", "학생 학과 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // Professor 관련 에러 (PROFESSOR_0XX)
    PROFESSOR_NOT_FOUND("PROFESSOR_001", "교수 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PROFESSOR_ALREADY_EXISTS("PROFESSOR_002", "이미 존재하는 교번입니다", HttpStatus.CONFLICT),
    INVALID_PROFESSOR_NUMBER("PROFESSOR_003", "유효하지 않은 교번입니다", HttpStatus.BAD_REQUEST),
    PROFESSOR_DEPARTMENT_NOT_FOUND("PROFESSOR_004", "교수 학과 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 암호화 관련 에러 (ENCRYPTION_0XX)
    ENCRYPTION_FAILED("ENCRYPTION_001", "암호화 처리 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    DECRYPTION_FAILED("ENCRYPTION_002", "복호화 처리 실패", HttpStatus.INTERNAL_SERVER_ERROR),

    // 기타 에러 (USER_MISC_0XX)
    INVALID_USER_TYPE("USER_MISC_001", "유효하지 않은 사용자 타입입니다", HttpStatus.BAD_REQUEST),
    USER_DATA_INCONSISTENT("USER_MISC_002", "사용자 데이터 불일치", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private static final String DOMAIN = "USER";

    private final String code;
    private final String message;
    private final HttpStatus status;

    UserErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }

    @Override
    public String toString() {
        return String.format("UserErrorCode{domain='%s', code='%s', message='%s', status=%s}",
                DOMAIN, code, message, status);
    }
}
