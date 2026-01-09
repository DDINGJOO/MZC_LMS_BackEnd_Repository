package com.mzc.backend.lms.common.exceptions.problem;

import com.mzc.backend.lms.common.exceptions.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * RFC 7807 Problem Details 표준에 따른 에러 응답 생성 팩토리
 */
@Component
public class ProblemDetailFactory {

    private static final String ERROR_TYPE_BASE_URI = "/errors/";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * CommonException 기반 ProblemDetail 생성
     */
    public ProblemDetail create(CommonException exception, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(exception.getHttpStatus());

        problem.setType(URI.create(ERROR_TYPE_BASE_URI + exception.getErrorCode().getCode()));
        problem.setTitle(exception.getExceptionType());
        problem.setDetail(exception.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));

        problem.setProperty("errorCode", exception.getErrorCode().getCode());
        problem.setProperty("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));

        return problem;
    }

    /**
     * 도메인 예외용 ProblemDetail 생성 (CommonException 기반)
     */
    public ProblemDetail createForDomain(
            CommonException exception,
            HttpServletRequest request,
            String domainErrorCode
    ) {
        ProblemDetail problem = create(exception, request);
        problem.setProperty("domainErrorCode", domainErrorCode);
        return problem;
    }

    /**
     * 도메인 예외용 ProblemDetail 생성 (RuntimeException 기반)
     * CommonException을 상속하지 않는 도메인 예외를 위한 메서드
     */
    public ProblemDetail createForDomain(
            RuntimeException exception,
            HttpServletRequest request,
            HttpStatus status,
            String domainErrorCode,
            String exceptionType
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setType(URI.create(ERROR_TYPE_BASE_URI + domainErrorCode));
        problem.setTitle(exceptionType);
        problem.setDetail(exception.getMessage());
        problem.setInstance(URI.create(request.getRequestURI()));

        problem.setProperty("errorCode", domainErrorCode);
        problem.setProperty("domainErrorCode", domainErrorCode);
        problem.setProperty("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));

        return problem;
    }

    /**
     * Validation 에러용 ProblemDetail 생성
     */
    public ProblemDetail createForValidation(
            HttpStatus status,
            String errorCode,
            String message,
            HttpServletRequest request,
            List<FieldError> fieldErrors
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setType(URI.create(ERROR_TYPE_BASE_URI + errorCode));
        problem.setTitle("VALIDATION");
        problem.setDetail(message);
        problem.setInstance(URI.create(request.getRequestURI()));

        problem.setProperty("errorCode", errorCode);
        problem.setProperty("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            List<Map<String, Object>> errors = fieldErrors.stream()
                    .map(error -> Map.<String, Object>of(
                            "field", error.getField(),
                            "rejectedValue", error.getRejectedValue() != null
                                    ? error.getRejectedValue().toString()
                                    : "null",
                            "message", error.getDefaultMessage() != null
                                    ? error.getDefaultMessage()
                                    : "Invalid value"
                    ))
                    .toList();
            problem.setProperty("fieldErrors", errors);
        }

        return problem;
    }

    /**
     * ConstraintViolation 에러용 ProblemDetail 생성
     */
    public ProblemDetail createForConstraintViolation(
            HttpStatus status,
            String errorCode,
            String message,
            HttpServletRequest request,
            List<Map<String, String>> violations
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setType(URI.create(ERROR_TYPE_BASE_URI + errorCode));
        problem.setTitle("CONSTRAINT_VIOLATION");
        problem.setDetail(message);
        problem.setInstance(URI.create(request.getRequestURI()));

        problem.setProperty("errorCode", errorCode);
        problem.setProperty("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));

        if (violations != null && !violations.isEmpty()) {
            problem.setProperty("violations", violations);
        }

        return problem;
    }

    /**
     * 일반 에러용 ProblemDetail 생성
     */
    public ProblemDetail createGeneric(
            HttpStatus status,
            String errorCode,
            String title,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setType(URI.create(ERROR_TYPE_BASE_URI + errorCode));
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setInstance(URI.create(request.getRequestURI()));

        problem.setProperty("errorCode", errorCode);
        problem.setProperty("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));

        return problem;
    }
}
