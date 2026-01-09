package com.mzc.backend.lms.domains.academy.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Academy 도메인 에러 코드
 * <p>
 * 학사 정보 (학과, 학기 등) 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum AcademyErrorCode implements DomainErrorCode {

    // 학과 관련 (DEPARTMENT_0XX)
    DEPARTMENT_NOT_FOUND("ACADEMY_DEPT_001", "학과를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    DEPARTMENT_ALREADY_EXISTS("ACADEMY_DEPT_002", "이미 존재하는 학과입니다", HttpStatus.CONFLICT),
    INVALID_DEPARTMENT_CODE("ACADEMY_DEPT_003", "유효하지 않은 학과 코드입니다", HttpStatus.BAD_REQUEST),

    // 학기 관련 (SEMESTER_0XX)
    SEMESTER_NOT_FOUND("ACADEMY_SEM_001", "학기를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    CURRENT_SEMESTER_NOT_SET("ACADEMY_SEM_002", "현재 학기가 설정되지 않았습니다", HttpStatus.BAD_REQUEST),
    SEMESTER_ALREADY_EXISTS("ACADEMY_SEM_003", "이미 존재하는 학기입니다", HttpStatus.CONFLICT),

    // 학사 일정 관련 (CALENDAR_0XX)
    CALENDAR_NOT_FOUND("ACADEMY_CAL_001", "학사 일정을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    INVALID_DATE_RANGE("ACADEMY_CAL_002", "유효하지 않은 날짜 범위입니다", HttpStatus.BAD_REQUEST),
    ;

    private static final String DOMAIN = "ACADEMY";

    private final String code;
    private final String message;
    private final HttpStatus status;

    AcademyErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
