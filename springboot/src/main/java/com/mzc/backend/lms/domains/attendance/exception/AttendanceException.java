package com.mzc.backend.lms.domains.attendance.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Attendance 도메인 예외 클래스
 * <p>
 * 출석, 출석 체크 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class AttendanceException extends CommonException {

    private final AttendanceErrorCode attendanceErrorCode;

    public AttendanceException(AttendanceErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.attendanceErrorCode = errorCode;
    }

    public AttendanceException(AttendanceErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.attendanceErrorCode = errorCode;
    }

    public AttendanceException(AttendanceErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.attendanceErrorCode = errorCode;
    }

    public AttendanceException(AttendanceErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.attendanceErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "ATTENDANCE_DOMAIN";
    }

    // 팩토리 메서드들 - 출석 관련

    public static AttendanceException attendanceNotFound(Long attendanceId) {
        return new AttendanceException(AttendanceErrorCode.ATTENDANCE_NOT_FOUND,
            String.format("출석 ID: %d", attendanceId));
    }

    public static AttendanceException weekNotFound(Long weekId) {
        return new AttendanceException(AttendanceErrorCode.WEEK_NOT_FOUND,
            String.format("주차 ID: %d", weekId));
    }

    public static AttendanceException alreadyChecked(Long userId, Long sessionId) {
        return new AttendanceException(AttendanceErrorCode.ALREADY_CHECKED,
            String.format("사용자 ID: %d, 세션 ID: %d", userId, sessionId));
    }

    public static AttendanceException checkTimeExpired() {
        return new AttendanceException(AttendanceErrorCode.CHECK_TIME_EXPIRED);
    }

    public static AttendanceException notCheckTime() {
        return new AttendanceException(AttendanceErrorCode.NOT_CHECK_TIME);
    }

    // 팩토리 메서드들 - 권한 관련

    public static AttendanceException notEnrolledStudent(Long userId, Long courseId) {
        return new AttendanceException(AttendanceErrorCode.NOT_ENROLLED_STUDENT,
            String.format("사용자 ID: %d, 강의 ID: %d", userId, courseId));
    }

    public static AttendanceException professorOnly() {
        return new AttendanceException(AttendanceErrorCode.PROFESSOR_ONLY);
    }

    public static AttendanceException weekNotInCourse(Long weekId, Long courseId) {
        return new AttendanceException(AttendanceErrorCode.WEEK_NOT_IN_COURSE,
            String.format("주차 ID: %d, 강의 ID: %d", weekId, courseId));
    }

    // 팩토리 메서드들 - 출석 세션 관련

    public static AttendanceException sessionNotFound(Long sessionId) {
        return new AttendanceException(AttendanceErrorCode.SESSION_NOT_FOUND,
            String.format("세션 ID: %d", sessionId));
    }

    public static AttendanceException sessionNotActive(Long courseId) {
        return new AttendanceException(AttendanceErrorCode.SESSION_NOT_ACTIVE,
            String.format("강의 ID: %d", courseId));
    }

    public static AttendanceException invalidAttendanceCode(String code) {
        return new AttendanceException(AttendanceErrorCode.INVALID_ATTENDANCE_CODE,
            String.format("입력된 코드: %s", code));
    }

    // 팩토리 메서드들 - 수정 관련

    public static AttendanceException invalidAttendanceStatus(String status) {
        return new AttendanceException(AttendanceErrorCode.INVALID_ATTENDANCE_STATUS,
            String.format("입력된 상태: %s", status));
    }

    public static AttendanceException updateNotAllowed(Long attendanceId) {
        return new AttendanceException(AttendanceErrorCode.UPDATE_NOT_ALLOWED,
            String.format("출석 ID: %d", attendanceId));
    }
}
