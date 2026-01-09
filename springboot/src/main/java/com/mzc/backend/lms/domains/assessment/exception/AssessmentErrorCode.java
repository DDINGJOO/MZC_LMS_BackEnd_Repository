package com.mzc.backend.lms.domains.assessment.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Assessment 도메인 에러 코드
 * <p>
 * 퀴즈, 시험, 채점 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum AssessmentErrorCode implements DomainErrorCode {

    // 권한 관련 (AUTH_0XX)
    VIEW_NOT_AUTHORIZED("ASSESSMENT_AUTH_001", "조회 권한이 없습니다", HttpStatus.FORBIDDEN),
    CREATE_NOT_AUTHORIZED("ASSESSMENT_AUTH_002", "퀴즈/시험 생성 권한이 없습니다", HttpStatus.FORBIDDEN),
    UPDATE_NOT_AUTHORIZED("ASSESSMENT_AUTH_003", "수정 권한이 없습니다", HttpStatus.FORBIDDEN),
    DELETE_NOT_AUTHORIZED("ASSESSMENT_AUTH_004", "삭제 권한이 없습니다", HttpStatus.FORBIDDEN),
    GRADE_NOT_AUTHORIZED("ASSESSMENT_AUTH_005", "채점 권한이 없습니다", HttpStatus.FORBIDDEN),
    SUBMIT_NOT_AUTHORIZED("ASSESSMENT_AUTH_006", "본인 응시만 제출할 수 있습니다", HttpStatus.FORBIDDEN),

    // 게시판 타입 관련 (BOARD_0XX)
    INVALID_BOARD_TYPE("ASSESSMENT_BOARD_001", "QUIZ/EXAM 게시판만 허용됩니다", HttpStatus.BAD_REQUEST),
    QUIZ_TYPE_MISMATCH("ASSESSMENT_BOARD_002", "QUIZ 게시판에는 type=QUIZ만 등록할 수 있습니다", HttpStatus.BAD_REQUEST),
    EXAM_TYPE_MISMATCH("ASSESSMENT_BOARD_003", "EXAM 게시판에는 QUIZ 유형을 등록할 수 없습니다", HttpStatus.BAD_REQUEST),

    // 수강 관련 (ENROLLMENT_0XX)
    NOT_ENROLLED("ASSESSMENT_ENROLLMENT_001", "수강 중인 강의만 조회할 수 있습니다", HttpStatus.FORBIDDEN),
    NOT_ENROLLED_FOR_ATTEMPT("ASSESSMENT_ENROLLMENT_002", "수강 중인 강의만 응시할 수 있습니다", HttpStatus.FORBIDDEN),

    // 시간 관련 (TIME_0XX)
    NOT_STARTED("ASSESSMENT_TIME_001", "시작 전 퀴즈/시험은 조회할 수 없습니다", HttpStatus.BAD_REQUEST),
    NOT_STARTED_FOR_ATTEMPT("ASSESSMENT_TIME_002", "시작 전에는 응시를 시작할 수 없습니다", HttpStatus.BAD_REQUEST),
    ALREADY_ENDED("ASSESSMENT_TIME_003", "종료된 퀴즈/시험입니다", HttpStatus.BAD_REQUEST),
    SUBMISSION_TIME_EXCEEDED("ASSESSMENT_TIME_004", "제출 가능 시간이 지났습니다", HttpStatus.BAD_REQUEST),

    // 응시 상태 관련 (ATTEMPT_0XX)
    ALREADY_SUBMITTED("ASSESSMENT_ATTEMPT_001", "이미 제출한 퀴즈/시험입니다", HttpStatus.CONFLICT),
    NOT_IN_PROGRESS("ASSESSMENT_ATTEMPT_002", "응시 시작 후 제출할 수 있습니다", HttpStatus.BAD_REQUEST),
    ATTEMPT_NOT_FOUND("ASSESSMENT_ATTEMPT_003", "응시 내역을 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 채점 관련 (GRADE_0XX)
    AUTO_GRADED_QUIZ("ASSESSMENT_GRADE_001", "퀴즈는 자동채점 대상입니다", HttpStatus.BAD_REQUEST),
    ONLY_SUBMITTED_CAN_GRADE("ASSESSMENT_GRADE_002", "제출된 응시만 채점할 수 있습니다", HttpStatus.BAD_REQUEST),
    GRADING_FAILED("ASSESSMENT_GRADE_003", "퀴즈 채점에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // 문제 데이터 관련 (QUESTION_0XX)
    QUESTION_DATA_REQUIRED("ASSESSMENT_QUESTION_001", "문제 JSON(questionData)은 필수입니다", HttpStatus.BAD_REQUEST),
    INVALID_QUESTION_FORMAT("ASSESSMENT_QUESTION_002", "questionData.questions는 배열이어야 합니다", HttpStatus.BAD_REQUEST),
    QUIZ_MCQ_ONLY("ASSESSMENT_QUESTION_003", "퀴즈는 객관식(MCQ) 문항만 허용합니다", HttpStatus.BAD_REQUEST),
    MCQ_CHOICES_REQUIRED("ASSESSMENT_QUESTION_004", "MCQ 문항은 choices 배열이 필요합니다", HttpStatus.BAD_REQUEST),
    MCQ_CORRECT_INDEX_REQUIRED("ASSESSMENT_QUESTION_005", "MCQ 문항은 correctChoiceIndex가 필요합니다", HttpStatus.BAD_REQUEST),
    MCQ_INDEX_OUT_OF_RANGE("ASSESSMENT_QUESTION_006", "correctChoiceIndex 범위가 choices 크기를 벗어났습니다", HttpStatus.BAD_REQUEST),
    EMPTY_QUESTION_DATA("ASSESSMENT_QUESTION_007", "퀴즈 문제 데이터가 없습니다", HttpStatus.BAD_REQUEST),
    INVALID_QUESTION_DATA("ASSESSMENT_QUESTION_008", "퀴즈 문제 데이터 형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST),

    // 기타 (MISC_0XX)
    JSON_SERIALIZATION_FAILED("ASSESSMENT_MISC_001", "JSON 직렬화에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_STATUS_FILTER("ASSESSMENT_MISC_002", "status는 ALL|SUBMITTED|IN_PROGRESS만 허용합니다", HttpStatus.BAD_REQUEST),
    ;

    private static final String DOMAIN = "ASSESSMENT";

    private final String code;
    private final String message;
    private final HttpStatus status;

    AssessmentErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
