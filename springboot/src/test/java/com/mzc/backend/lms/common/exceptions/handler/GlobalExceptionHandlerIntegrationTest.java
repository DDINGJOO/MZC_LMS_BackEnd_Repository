package com.mzc.backend.lms.common.exceptions.handler;

import com.mzc.backend.lms.common.exceptions.CommonErrorCode;
import com.mzc.backend.lms.common.exceptions.application.InvalidRequestException;
import com.mzc.backend.lms.common.exceptions.application.UnauthorizedException;
import com.mzc.backend.lms.common.exceptions.problem.ProblemDetailFactory;
import com.mzc.backend.lms.domains.board.exception.BoardErrorCode;
import com.mzc.backend.lms.domains.board.exception.BoardException;
import com.mzc.backend.lms.domains.user.exception.UserErrorCode;
import com.mzc.backend.lms.domains.user.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("Global Exception Handler Integration Tests")
class GlobalExceptionHandlerIntegrationTest {

    @Mock
    private HttpServletRequest request;

    private ProblemDetailFactory problemDetailFactory;
    private DomainExceptionHandler domainExceptionHandler;
    private ApplicationExceptionHandler applicationExceptionHandler;
    private InfrastructureExceptionHandler infrastructureExceptionHandler;
    private GlobalFallbackHandler globalFallbackHandler;

    @BeforeEach
    void setUp() {
        problemDetailFactory = new ProblemDetailFactory();
        domainExceptionHandler = new DomainExceptionHandler(problemDetailFactory);
        applicationExceptionHandler = new ApplicationExceptionHandler(problemDetailFactory);
        infrastructureExceptionHandler = new InfrastructureExceptionHandler(problemDetailFactory);
        globalFallbackHandler = new GlobalFallbackHandler(problemDetailFactory);

        given(request.getRequestURI()).willReturn("/api/v1/test");
    }

    @Nested
    @DisplayName("DomainExceptionHandler Tests")
    class DomainExceptionHandlerTest {

        @Test
        @DisplayName("Given UserException When handled Then returns ProblemDetail with correct status")
        void handleUserException_shouldReturnCorrectProblemDetail() {
            // Given
            UserException exception = UserException.studentNotFound("2024001");

            // When
            ResponseEntity<ProblemDetail> response = domainExceptionHandler.handleUserException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTitle()).isEqualTo("USER_DOMAIN");
            assertThat(response.getBody().getDetail()).contains("학번: 2024001");
            assertThat(response.getBody().getProperties()).containsKey("domainErrorCode");
            assertThat(response.getBody().getProperties().get("domainErrorCode")).isEqualTo("STUDENT_001");
        }

        @Test
        @DisplayName("Given BoardException When handled Then returns ProblemDetail with correct status")
        void handleBoardException_shouldReturnCorrectProblemDetail() {
            // Given
            BoardException exception = new BoardException(BoardErrorCode.POST_NOT_FOUND);

            // When
            ResponseEntity<ProblemDetail> response = domainExceptionHandler.handleBoardException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTitle()).isEqualTo("BOARD_DOMAIN");
            assertThat(response.getBody().getProperties()).containsKey("domainErrorCode");
            assertThat(response.getBody().getProperties().get("domainErrorCode")).isEqualTo("POST_001");
        }

        @Test
        @DisplayName("Given BoardException with CONFLICT status When handled Then returns 409 status")
        void handleBoardException_withConflictStatus_shouldReturn409() {
            // Given
            BoardException exception = new BoardException(BoardErrorCode.ALREADY_SUBMITTED);

            // When
            ResponseEntity<ProblemDetail> response = domainExceptionHandler.handleBoardException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    @Nested
    @DisplayName("ApplicationExceptionHandler Tests")
    class ApplicationExceptionHandlerTest {

        @Test
        @DisplayName("Given InvalidRequestException When handled Then returns 400 status")
        void handleInvalidRequestException_shouldReturn400() {
            // Given
            InvalidRequestException exception = InvalidRequestException.invalidFormat("email");

            // When
            ResponseEntity<ProblemDetail> response =
                    applicationExceptionHandler.handleInvalidRequestException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTitle()).isEqualTo("APPLICATION");
            assertThat(response.getBody().getDetail()).contains("email");
        }

        @Test
        @DisplayName("Given UnauthorizedException When handled Then returns 401 status")
        void handleUnauthorizedException_shouldReturn401() {
            // Given
            UnauthorizedException exception = UnauthorizedException.tokenExpired();

            // When
            ResponseEntity<ProblemDetail> response =
                    applicationExceptionHandler.handleUnauthorizedException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getProperties().get("errorCode"))
                    .isEqualTo(CommonErrorCode.UNAUTHORIZED.getCode());
        }

        @Test
        @DisplayName("Given IllegalArgumentException When handled Then returns 400 status")
        void handleIllegalArgumentException_shouldReturn400() {
            // Given
            IllegalArgumentException exception = new IllegalArgumentException("Invalid parameter value");

            // When
            ResponseEntity<ProblemDetail> response =
                    applicationExceptionHandler.handleIllegalArgumentException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTitle()).isEqualTo("INVALID_ARGUMENT");
            assertThat(response.getBody().getDetail()).isEqualTo("Invalid parameter value");
        }
    }

    @Nested
    @DisplayName("GlobalFallbackHandler Tests")
    class GlobalFallbackHandlerTest {

        @Test
        @DisplayName("Given RuntimeException When handled Then returns 500 status with generic message")
        void handleRuntimeException_shouldReturn500WithGenericMessage() {
            // Given
            RuntimeException exception = new RuntimeException("Unexpected error occurred");

            // When
            ResponseEntity<ProblemDetail> response =
                    globalFallbackHandler.handleRuntimeException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTitle()).isEqualTo("INTERNAL_ERROR");
            assertThat(response.getBody().getDetail()).doesNotContain("Unexpected error occurred");
        }

        @Test
        @DisplayName("Given Exception When handled Then returns 500 status with system error message")
        void handleException_shouldReturn500WithSystemErrorMessage() {
            // Given
            Exception exception = new Exception("System failure");

            // When
            ResponseEntity<ProblemDetail> response =
                    globalFallbackHandler.handleException(exception, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTitle()).isEqualTo("SYSTEM_ERROR");
        }
    }

    @Nested
    @DisplayName("ProblemDetail RFC 7807 Compliance Tests")
    class ProblemDetailComplianceTest {

        @Test
        @DisplayName("ProblemDetail should contain required RFC 7807 fields")
        void problemDetail_shouldContainRequiredFields() {
            // Given
            BoardException exception = new BoardException(BoardErrorCode.POST_NOT_FOUND);

            // When
            ResponseEntity<ProblemDetail> response = domainExceptionHandler.handleBoardException(exception, request);

            // Then
            ProblemDetail problem = response.getBody();
            assertThat(problem).isNotNull();
            assertThat(problem.getType()).isNotNull();
            assertThat(problem.getTitle()).isNotNull();
            assertThat(problem.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            assertThat(problem.getDetail()).isNotNull();
            assertThat(problem.getInstance()).isNotNull();
        }

        @Test
        @DisplayName("ProblemDetail type should follow error URI pattern")
        void problemDetail_typeShouldFollowUriPattern() {
            // Given
            BoardException exception = new BoardException(BoardErrorCode.POST_NOT_FOUND);

            // When
            ResponseEntity<ProblemDetail> response = domainExceptionHandler.handleBoardException(exception, request);

            // Then
            assertThat(response.getBody().getType().toString()).startsWith("/errors/");
        }

        @Test
        @DisplayName("ProblemDetail should contain timestamp property")
        void problemDetail_shouldContainTimestamp() {
            // Given
            UserException exception = new UserException(UserErrorCode.USER_NOT_FOUND);

            // When
            ResponseEntity<ProblemDetail> response = domainExceptionHandler.handleUserException(exception, request);

            // Then
            assertThat(response.getBody().getProperties()).containsKey("timestamp");
        }

        @Test
        @DisplayName("ProblemDetail should contain errorCode property")
        void problemDetail_shouldContainErrorCode() {
            // Given
            UserException exception = new UserException(UserErrorCode.USER_NOT_FOUND);

            // When
            ResponseEntity<ProblemDetail> response = domainExceptionHandler.handleUserException(exception, request);

            // Then
            assertThat(response.getBody().getProperties()).containsKey("errorCode");
        }
    }
}
