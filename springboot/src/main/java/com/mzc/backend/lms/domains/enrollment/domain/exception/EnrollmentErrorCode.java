package com.mzc.backend.lms.domains.enrollment.domain.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enrollment 도메인 에러 코드
 * <p>
 * 수강신청, 수강신청 기간 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum EnrollmentErrorCode implements DomainErrorCode {

    // 수강신청 기간 관련 (PERIOD_0XX)
    NOT_ENROLLMENT_PERIOD("ENROLLMENT_PERIOD_001", "수강신청 기간이 아닙니다", HttpStatus.BAD_REQUEST),
    INVALID_PERIOD_TYPE("ENROLLMENT_PERIOD_002", "유효하지 않은 기간 타입 코드입니다", HttpStatus.BAD_REQUEST),
    PERIOD_NOT_FOUND("ENROLLMENT_PERIOD_003", "수강신청 기간을 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 수강신청 관련 (ENROLLMENT_0XX)
    EMPTY_COURSE_LIST("ENROLLMENT_001", "강의 ID 목록이 비어있습니다", HttpStatus.BAD_REQUEST),
    COURSE_NOT_EXISTS("ENROLLMENT_002", "존재하지 않는 강의가 포함되어 있습니다", HttpStatus.BAD_REQUEST),
    ALREADY_ENROLLED("ENROLLMENT_003", "이미 수강신청한 강의입니다", HttpStatus.CONFLICT),
    ENROLLMENT_NOT_FOUND("ENROLLMENT_004", "수강신청 내역을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    MAX_CREDITS_EXCEEDED("ENROLLMENT_005", "최대 학점을 초과했습니다", HttpStatus.BAD_REQUEST),
    SCHEDULE_CONFLICT("ENROLLMENT_006", "시간표가 중복됩니다", HttpStatus.CONFLICT),
    CAPACITY_FULL("ENROLLMENT_007", "수강 인원이 초과되었습니다", HttpStatus.CONFLICT),

    // 수강 취소 관련 (CANCEL_0XX)
    CANCEL_PERIOD_ENDED("ENROLLMENT_CANCEL_001", "수강 취소 기간이 종료되었습니다", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL("ENROLLMENT_CANCEL_002", "취소할 수 없는 수강신청입니다", HttpStatus.BAD_REQUEST),

    // 데이터 조회 관련 (DATA_0XX)
    STUDENT_NOT_FOUND("ENROLLMENT_DATA_001", "학생을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    VALIDATION_FAILED("ENROLLMENT_DATA_002", "검증에 실패했습니다", HttpStatus.BAD_REQUEST),
    ;

    private static final String DOMAIN = "ENROLLMENT";

    private final String code;
    private final String message;
    private final HttpStatus status;

    EnrollmentErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
