package com.mzc.backend.lms.common.exceptions.handler;

import com.mzc.backend.lms.common.exceptions.CommonErrorCode;
import com.mzc.backend.lms.common.exceptions.application.InvalidRequestException;
import com.mzc.backend.lms.common.exceptions.application.UnauthorizedException;
import com.mzc.backend.lms.common.exceptions.problem.ProblemDetailFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;

/**
 * 애플리케이션 계층 예외 처리 핸들러
 * <p>
 * Clean Architecture의 Application Layer에서 발생하는 예외를 처리합니다.
 * - Validation 예외 (MethodArgumentNotValidException, ConstraintViolationException)
 * - 인증/인가 예외 (UnauthorizedException, AccessDeniedException)
 * - 요청 형식 예외 (HttpMessageNotReadableException, etc.)
 * </p>
 */
@Slf4j
@RestControllerAdvice
@Order(2)
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    /**
     * InvalidRequestException 처리
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ProblemDetail> handleInvalidRequestException(
            InvalidRequestException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] InvalidRequest - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.create(exception, request);

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    /**
     * UnauthorizedException 처리
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedException(
            UnauthorizedException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Unauthorized - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.create(exception, request);

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(problem);
    }

    /**
     * Bean Validation 예외 처리 (@Valid 어노테이션)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Validation failed - path: {}, errors: {}",
                request.getRequestURI(),
                exception.getBindingResult().getFieldErrors().size()
        );

        ProblemDetail problem = problemDetailFactory.createForValidation(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.INVALID_INPUT.getErrCode(),
                "입력값 검증에 실패했습니다",
                request,
                exception.getBindingResult().getFieldErrors()
        );

        return ResponseEntity.badRequest().body(problem);
    }

    /**
     * ConstraintViolation 예외 처리 (@Validated 어노테이션)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Constraint violation - path: {}, violations: {}",
                request.getRequestURI(),
                exception.getConstraintViolations().size()
        );

        List<Map<String, String>> violations = exception.getConstraintViolations().stream()
                .map(this::mapConstraintViolation)
                .toList();

        ProblemDetail problem = problemDetailFactory.createForConstraintViolation(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.INVALID_INPUT.getErrCode(),
                "제약 조건 검증에 실패했습니다",
                request,
                violations
        );

        return ResponseEntity.badRequest().body(problem);
    }

    /**
     * Spring Security 인증 예외 처리
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(
            AuthenticationException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Authentication failed - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.UNAUTHORIZED,
                CommonErrorCode.UNAUTHORIZED.getErrCode(),
                "AUTHENTICATION",
                "인증에 실패했습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    /**
     * Spring Security 인가 예외 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Access denied - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.FORBIDDEN,
                "ACCESS_001",
                "AUTHORIZATION",
                "접근이 거부되었습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    /**
     * HTTP 메시지 파싱 실패 예외 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Message not readable - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.INVALID_FORMAT.getErrCode(),
                "PARSE_ERROR",
                "요청 본문을 파싱할 수 없습니다",
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    /**
     * HTTP 메서드 미지원 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Method not supported - path: {}, method: {}",
                request.getRequestURI(),
                exception.getMethod()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.METHOD_NOT_ALLOWED,
                "METHOD_001",
                "METHOD_NOT_ALLOWED",
                String.format("HTTP 메서드 '%s'는 지원되지 않습니다", exception.getMethod()),
                request
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problem);
    }

    /**
     * 미디어 타입 미지원 예외 처리
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Media type not supported - path: {}, contentType: {}",
                request.getRequestURI(),
                exception.getContentType()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "MEDIA_001",
                "UNSUPPORTED_MEDIA_TYPE",
                String.format("미디어 타입 '%s'는 지원되지 않습니다", exception.getContentType()),
                request
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(problem);
    }

    /**
     * 필수 요청 파라미터 누락 예외 처리
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Missing parameter - path: {}, parameter: {}",
                request.getRequestURI(),
                exception.getParameterName()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.REQUIRED_FIELD_MISSING.getErrCode(),
                "MISSING_PARAMETER",
                String.format("필수 파라미터 '%s'가 누락되었습니다", exception.getParameterName()),
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    /**
     * 요청 파라미터 타입 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Type mismatch - path: {}, parameter: {}, value: {}",
                request.getRequestURI(),
                exception.getName(),
                exception.getValue()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.INVALID_FORMAT.getErrCode(),
                "TYPE_MISMATCH",
                String.format("파라미터 '%s'의 타입이 올바르지 않습니다", exception.getName()),
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    /**
     * 리소스를 찾을 수 없음 예외 처리 (404)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFoundException(
            NoResourceFoundException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] Resource not found - path: {}",
                request.getRequestURI()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.NOT_FOUND,
                "RESOURCE_001",
                "NOT_FOUND",
                "요청한 리소스를 찾을 수 없습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    /**
     * IllegalArgumentException 처리 (레거시 호환)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        log.warn("[APPLICATION] IllegalArgument - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.INVALID_INPUT.getErrCode(),
                "INVALID_ARGUMENT",
                exception.getMessage(),
                request
        );

        return ResponseEntity.badRequest().body(problem);
    }

    private Map<String, String> mapConstraintViolation(ConstraintViolation<?> violation) {
        return Map.of(
                "field", violation.getPropertyPath().toString(),
                "rejectedValue", violation.getInvalidValue() != null
                        ? violation.getInvalidValue().toString()
                        : "null",
                "message", violation.getMessage()
        );
    }
}
