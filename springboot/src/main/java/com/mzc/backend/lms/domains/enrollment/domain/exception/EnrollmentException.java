package com.mzc.backend.lms.domains.enrollment.domain.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Enrollment 도메인 예외 클래스
 * <p>
 * 수강신청, 수강신청 기간 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class EnrollmentException extends CommonException {

    private final EnrollmentErrorCode enrollmentErrorCode;

    public EnrollmentException(EnrollmentErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.enrollmentErrorCode = errorCode;
    }

    public EnrollmentException(EnrollmentErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.enrollmentErrorCode = errorCode;
    }

    public EnrollmentException(EnrollmentErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.enrollmentErrorCode = errorCode;
    }

    public EnrollmentException(EnrollmentErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.enrollmentErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "ENROLLMENT_DOMAIN";
    }

    // 팩토리 메서드들

    public static EnrollmentException notEnrollmentPeriod() {
        return new EnrollmentException(EnrollmentErrorCode.NOT_ENROLLMENT_PERIOD);
    }

    public static EnrollmentException invalidPeriodType(String typeCode) {
        return new EnrollmentException(EnrollmentErrorCode.INVALID_PERIOD_TYPE,
            String.format("타입 코드: %s", typeCode));
    }

    public static EnrollmentException periodNotFound(Long periodId) {
        return new EnrollmentException(EnrollmentErrorCode.PERIOD_NOT_FOUND,
            String.format("기간 ID: %d", periodId));
    }

    public static EnrollmentException emptyCourseList() {
        return new EnrollmentException(EnrollmentErrorCode.EMPTY_COURSE_LIST);
    }

    public static EnrollmentException courseNotExists(Long courseId) {
        return new EnrollmentException(EnrollmentErrorCode.COURSE_NOT_EXISTS,
            String.format("강의 ID: %d", courseId));
    }

    public static EnrollmentException alreadyEnrolled(Long courseId) {
        return new EnrollmentException(EnrollmentErrorCode.ALREADY_ENROLLED,
            String.format("강의 ID: %d", courseId));
    }

    public static EnrollmentException enrollmentNotFound(Long enrollmentId) {
        return new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND,
            String.format("수강신청 ID: %d", enrollmentId));
    }

    public static EnrollmentException maxCreditsExceeded(int currentCredits, int maxCredits) {
        return new EnrollmentException(EnrollmentErrorCode.MAX_CREDITS_EXCEEDED,
            String.format("현재 학점: %d, 최대 학점: %d", currentCredits, maxCredits));
    }

    public static EnrollmentException scheduleConflict(String conflictInfo) {
        return new EnrollmentException(EnrollmentErrorCode.SCHEDULE_CONFLICT, conflictInfo);
    }

    public static EnrollmentException capacityFull(Long courseId) {
        return new EnrollmentException(EnrollmentErrorCode.CAPACITY_FULL,
            String.format("강의 ID: %d", courseId));
    }

    public static EnrollmentException cancelPeriodEnded() {
        return new EnrollmentException(EnrollmentErrorCode.CANCEL_PERIOD_ENDED);
    }

    public static EnrollmentException cannotCancel(Long enrollmentId) {
        return new EnrollmentException(EnrollmentErrorCode.CANNOT_CANCEL,
            String.format("수강신청 ID: %d", enrollmentId));
    }

    public static EnrollmentException studentNotFound(Long studentId) {
        return new EnrollmentException(EnrollmentErrorCode.STUDENT_NOT_FOUND,
            String.format("학생 ID: %d", studentId));
    }

    public static EnrollmentException validationFailed(String details) {
        return new EnrollmentException(EnrollmentErrorCode.VALIDATION_FAILED, details);
    }
}
