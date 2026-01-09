package com.mzc.backend.lms.common.exceptions.handler;

import com.mzc.backend.lms.common.exceptions.CommonErrorCode;
import com.mzc.backend.lms.common.exceptions.CommonException;
import com.mzc.backend.lms.common.exceptions.problem.ProblemDetailFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 폴백 예외 처리 핸들러
 * <p>
 * 다른 핸들러에서 처리되지 않은 모든 예외를 처리합니다.
 * 가장 낮은 우선순위(@Order(Ordered.LOWEST_PRECEDENCE))로 설정되어
 * 마지막 안전망 역할을 합니다.
 * </p>
 *
 * <h3>처리 대상:</h3>
 * <ul>
 *   <li>CommonException을 상속하지만 특정 핸들러가 없는 예외</li>
 *   <li>예상치 못한 RuntimeException</li>
 *   <li>시스템 레벨의 Exception</li>
 * </ul>
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalFallbackHandler {

    private final ProblemDetailFactory problemDetailFactory;

    /**
     * CommonException 기반 예외 폴백 처리
     * <p>
     * 특정 핸들러가 정의되지 않은 CommonException 서브클래스를 처리합니다.
     * </p>
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ProblemDetail> handleCommonException(
            CommonException exception,
            HttpServletRequest request
    ) {
        log.error("[FALLBACK] Unhandled CommonException - type: {}, path: {}, message: {}",
                exception.getExceptionType(),
                request.getRequestURI(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.create(exception, request);

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    /**
     * RuntimeException 폴백 처리
     * <p>
     * 예상치 못한 런타임 예외를 처리합니다.
     * 보안을 위해 상세 에러 메시지는 로그에만 남기고,
     * 클라이언트에는 일반적인 메시지를 반환합니다.
     * </p>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        log.error("[FALLBACK] Unhandled RuntimeException - path: {}, type: {}, message: {}",
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.INTERNAL_SERVER_ERROR,
                CommonErrorCode.INTERNAL_SERVER_ERROR.getErrCode(),
                "INTERNAL_ERROR",
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                request
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    /**
     * Exception 폴백 처리 (최후의 안전망)
     * <p>
     * 모든 예외의 최종 처리자입니다.
     * Checked Exception을 포함한 모든 예외를 처리합니다.
     * </p>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error("[FALLBACK] Unhandled Exception - path: {}, type: {}, message: {}",
                request.getRequestURI(),
                exception.getClass().getName(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.INTERNAL_SERVER_ERROR,
                CommonErrorCode.INTERNAL_SERVER_ERROR.getErrCode(),
                "SYSTEM_ERROR",
                "시스템 오류가 발생했습니다. 관리자에게 문의해주세요.",
                request
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
