package com.mzc.backend.lms.domains.attendance.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Attendance 도메인 에러 코드
 * <p>
 * 출석, 출석 체크 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum AttendanceErrorCode implements DomainErrorCode {

    // 출석 관련 (ATTENDANCE_0XX)
    ATTENDANCE_NOT_FOUND("ATTENDANCE_001", "출석 기록을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    WEEK_NOT_FOUND("ATTENDANCE_005", "주차를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    ALREADY_CHECKED("ATTENDANCE_002", "이미 출석 체크되었습니다", HttpStatus.CONFLICT),
    CHECK_TIME_EXPIRED("ATTENDANCE_003", "출석 체크 시간이 종료되었습니다", HttpStatus.BAD_REQUEST),
    NOT_CHECK_TIME("ATTENDANCE_004", "출석 체크 시간이 아닙니다", HttpStatus.BAD_REQUEST),

    // 권한 관련 (AUTH_0XX)
    NOT_ENROLLED_STUDENT("ATTENDANCE_AUTH_001", "해당 강의의 수강생이 아닙니다", HttpStatus.FORBIDDEN),
    PROFESSOR_ONLY("ATTENDANCE_AUTH_002", "담당 교수만 접근 가능합니다", HttpStatus.FORBIDDEN),
    WEEK_NOT_IN_COURSE("ATTENDANCE_AUTH_003", "해당 주차가 이 강의에 속하지 않습니다", HttpStatus.BAD_REQUEST),

    // 출석 세션 관련 (SESSION_0XX)
    SESSION_NOT_FOUND("ATTENDANCE_SESSION_001", "출석 세션을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    SESSION_NOT_ACTIVE("ATTENDANCE_SESSION_002", "활성화된 출석 세션이 없습니다", HttpStatus.BAD_REQUEST),
    INVALID_ATTENDANCE_CODE("ATTENDANCE_SESSION_003", "유효하지 않은 출석 코드입니다", HttpStatus.BAD_REQUEST),

    // 수정 관련 (UPDATE_0XX)
    INVALID_ATTENDANCE_STATUS("ATTENDANCE_UPDATE_001", "유효하지 않은 출석 상태입니다", HttpStatus.BAD_REQUEST),
    UPDATE_NOT_ALLOWED("ATTENDANCE_UPDATE_002", "출석 상태 수정이 불가합니다", HttpStatus.BAD_REQUEST),
    ;

    private static final String DOMAIN = "ATTENDANCE";

    private final String code;
    private final String message;
    private final HttpStatus status;

    AttendanceErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
