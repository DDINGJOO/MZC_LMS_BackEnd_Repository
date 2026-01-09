package com.mzc.backend.lms.domains.academy.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Academy 도메인 예외 클래스
 * <p>
 * 학사 정보 (학과, 학기 등) 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class AcademyException extends CommonException {

    private final AcademyErrorCode academyErrorCode;

    public AcademyException(AcademyErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.academyErrorCode = errorCode;
    }

    public AcademyException(AcademyErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.academyErrorCode = errorCode;
    }

    public AcademyException(AcademyErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.academyErrorCode = errorCode;
    }

    public AcademyException(AcademyErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.academyErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "ACADEMY_DOMAIN";
    }

    // 팩토리 메서드들 - 학과 관련

    public static AcademyException departmentNotFound(Long departmentId) {
        return new AcademyException(AcademyErrorCode.DEPARTMENT_NOT_FOUND,
            String.format("학과 ID: %d", departmentId));
    }

    public static AcademyException departmentNotFound(String departmentCode) {
        return new AcademyException(AcademyErrorCode.DEPARTMENT_NOT_FOUND,
            String.format("학과 코드: %s", departmentCode));
    }

    public static AcademyException departmentAlreadyExists(String departmentName) {
        return new AcademyException(AcademyErrorCode.DEPARTMENT_ALREADY_EXISTS,
            String.format("학과명: %s", departmentName));
    }

    public static AcademyException invalidDepartmentCode(String code) {
        return new AcademyException(AcademyErrorCode.INVALID_DEPARTMENT_CODE,
            String.format("학과 코드: %s", code));
    }

    // 팩토리 메서드들 - 학기 관련

    public static AcademyException semesterNotFound(Long semesterId) {
        return new AcademyException(AcademyErrorCode.SEMESTER_NOT_FOUND,
            String.format("학기 ID: %d", semesterId));
    }

    public static AcademyException semesterNotFound(String semesterCode) {
        return new AcademyException(AcademyErrorCode.SEMESTER_NOT_FOUND,
            String.format("학기 코드: %s", semesterCode));
    }

    public static AcademyException currentSemesterNotSet() {
        return new AcademyException(AcademyErrorCode.CURRENT_SEMESTER_NOT_SET);
    }

    public static AcademyException semesterAlreadyExists(String semesterCode) {
        return new AcademyException(AcademyErrorCode.SEMESTER_ALREADY_EXISTS,
            String.format("학기 코드: %s", semesterCode));
    }

    // 팩토리 메서드들 - 학사 일정 관련

    public static AcademyException calendarNotFound(Long calendarId) {
        return new AcademyException(AcademyErrorCode.CALENDAR_NOT_FOUND,
            String.format("학사 일정 ID: %d", calendarId));
    }

    public static AcademyException invalidDateRange(String startDate, String endDate) {
        return new AcademyException(AcademyErrorCode.INVALID_DATE_RANGE,
            String.format("시작일: %s, 종료일: %s", startDate, endDate));
    }
}
