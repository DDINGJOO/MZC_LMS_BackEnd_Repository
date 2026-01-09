package com.mzc.backend.lms.common.exceptions.handler;

import com.mzc.backend.lms.common.exceptions.problem.ProblemDetailFactory;
import com.mzc.backend.lms.domains.academy.exception.AcademyException;
import com.mzc.backend.lms.domains.assessment.exception.AssessmentException;
import com.mzc.backend.lms.domains.attendance.exception.AttendanceException;
import com.mzc.backend.lms.domains.board.exception.BoardException;
import com.mzc.backend.lms.domains.course.exception.CourseException;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import com.mzc.backend.lms.domains.message.exception.MessageException;
import com.mzc.backend.lms.domains.notification.exception.NotificationException;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import com.mzc.backend.lms.domains.user.user.exceptions.UserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 도메인 계층 예외 처리 핸들러
 * <p>
 * Clean Architecture의 Domain Layer에서 발생하는 비즈니스 로직 관련 예외를 처리합니다.
 * </p>
 */
@Slf4j
@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class DomainExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(
            AuthException exception,
            HttpServletRequest request
    ) {
        log.warn("[AUTH_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getAuthErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getAuthErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ProblemDetail> handleUserException(
            UserException exception,
            HttpServletRequest request
    ) {
        log.warn("[USER_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getUserErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getUserErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getUserErrorCode().getStatus())
                .body(problem);
    }

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<ProblemDetail> handleBoardException(
            BoardException exception,
            HttpServletRequest request
    ) {
        log.warn("[BOARD_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(CourseException.class)
    public ResponseEntity<ProblemDetail> handleCourseException(
            CourseException exception,
            HttpServletRequest request
    ) {
        log.warn("[COURSE_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getCourseErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getCourseErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<ProblemDetail> handleEnrollmentException(
            EnrollmentException exception,
            HttpServletRequest request
    ) {
        log.warn("[ENROLLMENT_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getEnrollmentErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getEnrollmentErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(AssessmentException.class)
    public ResponseEntity<ProblemDetail> handleAssessmentException(
            AssessmentException exception,
            HttpServletRequest request
    ) {
        log.warn("[ASSESSMENT_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getAssessmentErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getAssessmentErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(AttendanceException.class)
    public ResponseEntity<ProblemDetail> handleAttendanceException(
            AttendanceException exception,
            HttpServletRequest request
    ) {
        log.warn("[ATTENDANCE_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getAttendanceErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getAttendanceErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ProblemDetail> handleNotificationException(
            NotificationException exception,
            HttpServletRequest request
    ) {
        log.warn("[NOTIFICATION_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getNotificationErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getNotificationErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ProblemDetail> handleMessageException(
            MessageException exception,
            HttpServletRequest request
    ) {
        log.warn("[MESSAGE_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getMessageErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getMessageErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    @ExceptionHandler(AcademyException.class)
    public ResponseEntity<ProblemDetail> handleAcademyException(
            AcademyException exception,
            HttpServletRequest request
    ) {
        log.warn("[ACADEMY_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getAcademyErrorCode().getCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getAcademyErrorCode().getCode()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }
}
