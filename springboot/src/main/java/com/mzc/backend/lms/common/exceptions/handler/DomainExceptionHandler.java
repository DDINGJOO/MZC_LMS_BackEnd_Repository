package com.mzc.backend.lms.common.exceptions.handler;

import com.mzc.backend.lms.common.exceptions.problem.ProblemDetailFactory;
import com.mzc.backend.lms.domains.board.exception.BoardException;
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
 * - UserException: 사용자 도메인 예외
 * - BoardException: 게시판 도메인 예외
 * </p>
 *
 * @see UserException
 * @see BoardException
 */
@Slf4j
@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class DomainExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    /**
     * UserException 처리
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ProblemDetail> handleUserException(
            UserException exception,
            HttpServletRequest request
    ) {
        log.warn("[USER_DOMAIN] {} - path: {}, errorCode: {}",
                exception.getMessage(),
                request.getRequestURI(),
                exception.getUserErrorCode().getErrorCode()
        );

        ProblemDetail problem = problemDetailFactory.createForDomain(
                exception,
                request,
                exception.getUserErrorCode().getErrorCode()
        );

        return ResponseEntity
                .status(exception.getUserErrorCode().getStatus())
                .body(problem);
    }

    /**
     * BoardException 처리
     */
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
}
