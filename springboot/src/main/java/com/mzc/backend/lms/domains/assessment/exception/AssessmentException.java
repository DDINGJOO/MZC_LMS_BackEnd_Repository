package com.mzc.backend.lms.domains.assessment.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Assessment 도메인 예외 클래스
 * <p>
 * 퀴즈, 시험, 채점 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class AssessmentException extends CommonException {

    private final AssessmentErrorCode assessmentErrorCode;

    public AssessmentException(AssessmentErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.assessmentErrorCode = errorCode;
    }

    public AssessmentException(AssessmentErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.assessmentErrorCode = errorCode;
    }

    public AssessmentException(AssessmentErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.assessmentErrorCode = errorCode;
    }

    public AssessmentException(AssessmentErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.assessmentErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "ASSESSMENT_DOMAIN";
    }

    // 팩토리 메서드들 - 권한 관련

    public static AssessmentException viewNotAuthorized() {
        return new AssessmentException(AssessmentErrorCode.VIEW_NOT_AUTHORIZED);
    }

    public static AssessmentException createNotAuthorized() {
        return new AssessmentException(AssessmentErrorCode.CREATE_NOT_AUTHORIZED);
    }

    public static AssessmentException updateNotAuthorized() {
        return new AssessmentException(AssessmentErrorCode.UPDATE_NOT_AUTHORIZED);
    }

    public static AssessmentException deleteNotAuthorized() {
        return new AssessmentException(AssessmentErrorCode.DELETE_NOT_AUTHORIZED);
    }

    public static AssessmentException gradeNotAuthorized() {
        return new AssessmentException(AssessmentErrorCode.GRADE_NOT_AUTHORIZED);
    }

    public static AssessmentException submitNotAuthorized() {
        return new AssessmentException(AssessmentErrorCode.SUBMIT_NOT_AUTHORIZED);
    }

    // 팩토리 메서드들 - 게시판 타입 관련

    public static AssessmentException invalidBoardType() {
        return new AssessmentException(AssessmentErrorCode.INVALID_BOARD_TYPE);
    }

    public static AssessmentException quizTypeMismatch() {
        return new AssessmentException(AssessmentErrorCode.QUIZ_TYPE_MISMATCH);
    }

    public static AssessmentException examTypeMismatch() {
        return new AssessmentException(AssessmentErrorCode.EXAM_TYPE_MISMATCH);
    }

    // 팩토리 메서드들 - 수강 관련

    public static AssessmentException notEnrolled(Long courseId) {
        return new AssessmentException(AssessmentErrorCode.NOT_ENROLLED,
            String.format("강의 ID: %d", courseId));
    }

    public static AssessmentException notEnrolledForAttempt(Long courseId) {
        return new AssessmentException(AssessmentErrorCode.NOT_ENROLLED_FOR_ATTEMPT,
            String.format("강의 ID: %d", courseId));
    }

    // 팩토리 메서드들 - 시간 관련

    public static AssessmentException notStarted(Long assessmentId) {
        return new AssessmentException(AssessmentErrorCode.NOT_STARTED,
            String.format("퀴즈/시험 ID: %d", assessmentId));
    }

    public static AssessmentException notStartedForAttempt(Long assessmentId) {
        return new AssessmentException(AssessmentErrorCode.NOT_STARTED_FOR_ATTEMPT,
            String.format("퀴즈/시험 ID: %d", assessmentId));
    }

    public static AssessmentException alreadyEnded(Long assessmentId) {
        return new AssessmentException(AssessmentErrorCode.ALREADY_ENDED,
            String.format("퀴즈/시험 ID: %d", assessmentId));
    }

    public static AssessmentException submissionTimeExceeded() {
        return new AssessmentException(AssessmentErrorCode.SUBMISSION_TIME_EXCEEDED);
    }

    // 팩토리 메서드들 - 응시 상태 관련

    public static AssessmentException alreadySubmitted(Long attemptId) {
        return new AssessmentException(AssessmentErrorCode.ALREADY_SUBMITTED,
            String.format("응시 ID: %d", attemptId));
    }

    public static AssessmentException notInProgress() {
        return new AssessmentException(AssessmentErrorCode.NOT_IN_PROGRESS);
    }

    public static AssessmentException attemptNotFound(Long attemptId) {
        return new AssessmentException(AssessmentErrorCode.ATTEMPT_NOT_FOUND,
            String.format("응시 ID: %d", attemptId));
    }

    // 팩토리 메서드들 - 채점 관련

    public static AssessmentException autoGradedQuiz() {
        return new AssessmentException(AssessmentErrorCode.AUTO_GRADED_QUIZ);
    }

    public static AssessmentException onlySubmittedCanGrade() {
        return new AssessmentException(AssessmentErrorCode.ONLY_SUBMITTED_CAN_GRADE);
    }

    public static AssessmentException gradingFailed(Throwable cause) {
        return new AssessmentException(AssessmentErrorCode.GRADING_FAILED, cause);
    }

    // 팩토리 메서드들 - 문제 데이터 관련

    public static AssessmentException questionDataRequired() {
        return new AssessmentException(AssessmentErrorCode.QUESTION_DATA_REQUIRED);
    }

    public static AssessmentException invalidQuestionFormat() {
        return new AssessmentException(AssessmentErrorCode.INVALID_QUESTION_FORMAT);
    }

    public static AssessmentException quizMcqOnly() {
        return new AssessmentException(AssessmentErrorCode.QUIZ_MCQ_ONLY);
    }

    public static AssessmentException mcqChoicesRequired() {
        return new AssessmentException(AssessmentErrorCode.MCQ_CHOICES_REQUIRED);
    }

    public static AssessmentException mcqCorrectIndexRequired() {
        return new AssessmentException(AssessmentErrorCode.MCQ_CORRECT_INDEX_REQUIRED);
    }

    public static AssessmentException mcqIndexOutOfRange(int index, int size) {
        return new AssessmentException(AssessmentErrorCode.MCQ_INDEX_OUT_OF_RANGE,
            String.format("index: %d, choices 크기: %d", index, size));
    }

    public static AssessmentException emptyQuestionData() {
        return new AssessmentException(AssessmentErrorCode.EMPTY_QUESTION_DATA);
    }

    public static AssessmentException invalidQuestionData(String reason) {
        return new AssessmentException(AssessmentErrorCode.INVALID_QUESTION_DATA, reason);
    }

    // 팩토리 메서드들 - 기타

    public static AssessmentException jsonSerializationFailed(Throwable cause) {
        return new AssessmentException(AssessmentErrorCode.JSON_SERIALIZATION_FAILED, cause);
    }

    public static AssessmentException invalidStatusFilter(String status) {
        return new AssessmentException(AssessmentErrorCode.INVALID_STATUS_FILTER,
            String.format("입력된 값: %s", status));
    }
}
