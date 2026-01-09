package com.mzc.backend.lms.common.exceptions;

/**
 * 도메인별 에러 코드 인터페이스
 * <p>
 * 각 도메인(Board, User 등)의 비즈니스 로직 관련 에러 코드가 구현하는 인터페이스입니다.
 * ErrorCode를 확장하여 도메인 식별 기능을 추가합니다.
 * </p>
 *
 * <h3>구현체:</h3>
 * <ul>
 *   <li>AcademyErrorCode - 학사 도메인</li>
 *   <li>AssessmentErrorCode - 평가 도메인</li>
 *   <li>AttendanceErrorCode - 출석 도메인</li>
 *   <li>BoardErrorCode - 게시판 도메인</li>
 *   <li>CourseErrorCode - 강의 도메인</li>
 *   <li>EnrollmentErrorCode - 수강신청 도메인</li>
 *   <li>MessageErrorCode - 메시지 도메인</li>
 *   <li>NotificationErrorCode - 알림 도메인</li>
 *   <li>UserErrorCode - 사용자 도메인</li>
 * </ul>
 */
public non-sealed interface DomainErrorCode extends ErrorCode {

    /**
     * 도메인 이름 반환
     * 에러가 발생한 도메인을 식별하는 문자열을 반환합니다.
     * 예: "BOARD", "USER", "COURSE"
     */
    String getDomain();

    /**
     * 전체 에러 식별자 반환
     * 도메인과 에러 코드를 조합한 전체 식별자를 반환합니다.
     * 형식: DOMAIN.CODE (예: BOARD.POST_001, USER.STUDENT_001)
     */
    default String getFullCode() {
        return getDomain() + "." + getCode();
    }
}
