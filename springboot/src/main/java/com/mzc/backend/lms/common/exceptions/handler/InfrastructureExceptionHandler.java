package com.mzc.backend.lms.common.exceptions.handler;

import com.mzc.backend.lms.common.exceptions.CommonErrorCode;
import com.mzc.backend.lms.common.exceptions.problem.ProblemDetailFactory;
import com.mzc.backend.lms.util.image.ImageStorageException;
import com.mzc.backend.lms.util.lock.exception.DistributedLockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 인프라스트럭처 계층 예외 처리 핸들러
 * <p>
 * Clean Architecture의 Infrastructure Layer에서 발생하는 예외를 처리합니다.
 * - 데이터베이스 예외 (DataAccessException, DataIntegrityViolationException)
 * - 외부 API 예외 (RestClientException, ConnectException)
 * - 파일 저장소 예외 (ImageStorageException)
 * - 분산 락 예외 (DistributedLockException)
 * </p>
 */
@Slf4j
@RestControllerAdvice
@Order(3)
@RequiredArgsConstructor
public class InfrastructureExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    /**
     * 분산 락 예외 처리
     */
    @ExceptionHandler(DistributedLockException.class)
    public ResponseEntity<ProblemDetail> handleDistributedLockException(
            DistributedLockException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] Distributed lock failed - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.CONFLICT,
                "LOCK_001",
                "DISTRIBUTED_LOCK",
                "리소스에 대한 동시 접근으로 인해 요청을 처리할 수 없습니다. 잠시 후 다시 시도해주세요.",
                request
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    /**
     * 이미지 저장 예외 처리
     */
    @ExceptionHandler(ImageStorageException.class)
    public ResponseEntity<ProblemDetail> handleImageStorageException(
            ImageStorageException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] Image storage failed - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "STORAGE_001",
                "STORAGE",
                "이미지 저장 중 오류가 발생했습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    /**
     * 데이터 무결성 위반 예외 처리 (Unique 제약, FK 제약 등)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] Data integrity violation - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMostSpecificCause().getMessage()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.CONFLICT,
                CommonErrorCode.DATABASE_ERROR.getErrCode(),
                "DATA_INTEGRITY",
                "데이터 무결성 제약 조건을 위반했습니다. 중복된 데이터가 존재할 수 있습니다.",
                request
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    /**
     * 낙관적 락 실패 예외 처리
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ProblemDetail> handleOptimisticLockingFailureException(
            OptimisticLockingFailureException exception,
            HttpServletRequest request
    ) {
        log.warn("[INFRASTRUCTURE] Optimistic lock failed - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.CONFLICT,
                "LOCK_002",
                "OPTIMISTIC_LOCK",
                "동시 수정으로 인해 요청을 처리할 수 없습니다. 데이터를 다시 확인 후 시도해주세요.",
                request
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    /**
     * 데이터베이스 접근 예외 처리 (일반)
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ProblemDetail> handleDataAccessException(
            DataAccessException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] Database access error - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMostSpecificCause().getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.INTERNAL_SERVER_ERROR,
                CommonErrorCode.DATABASE_ERROR.getErrCode(),
                "DATABASE",
                "데이터베이스 처리 중 오류가 발생했습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    /**
     * 외부 API 리소스 접근 예외 처리
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ProblemDetail> handleResourceAccessException(
            ResourceAccessException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] External resource access failed - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.BAD_GATEWAY,
                CommonErrorCode.EXTERNAL_API_ERROR.getErrCode(),
                "EXTERNAL_API",
                "외부 서비스에 접근할 수 없습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(problem);
    }

    /**
     * REST 클라이언트 예외 처리 (일반)
     */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ProblemDetail> handleRestClientException(
            RestClientException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] REST client error - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.BAD_GATEWAY,
                CommonErrorCode.EXTERNAL_API_ERROR.getErrCode(),
                "EXTERNAL_API",
                "외부 API 호출 중 오류가 발생했습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(problem);
    }

    /**
     * 연결 실패 예외 처리
     */
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ProblemDetail> handleConnectException(
            ConnectException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] Connection failed - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.SERVICE_UNAVAILABLE,
                CommonErrorCode.EXTERNAL_API_ERROR.getErrCode(),
                "CONNECTION",
                "서비스에 연결할 수 없습니다. 잠시 후 다시 시도해주세요.",
                request
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }

    /**
     * 소켓 타임아웃 예외 처리
     */
    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<ProblemDetail> handleSocketTimeoutException(
            SocketTimeoutException exception,
            HttpServletRequest request
    ) {
        log.error("[INFRASTRUCTURE] Socket timeout - path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage(),
                exception
        );

        ProblemDetail problem = problemDetailFactory.createGeneric(
                HttpStatus.GATEWAY_TIMEOUT,
                CommonErrorCode.EXTERNAL_API_ERROR.getErrCode(),
                "TIMEOUT",
                "요청 처리 시간이 초과되었습니다",
                request
        );

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(problem);
    }
}
