package com.mzc.backend.lms.domains.course.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Course 도메인 에러 코드
 * <p>
 * 강의, 공지사항, 강의자료 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum CourseErrorCode implements DomainErrorCode {

    // 강의 관련 (COURSE_0XX)
    COURSE_NOT_FOUND("COURSE_001", "강의를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    COURSE_ALREADY_EXISTS("COURSE_002", "이미 존재하는 강의입니다", HttpStatus.CONFLICT),
    COURSE_CLOSED("COURSE_003", "종료된 강의입니다", HttpStatus.BAD_REQUEST),

    // 공지사항 관련 (NOTICE_0XX)
    NOTICE_NOT_FOUND("COURSE_NOTICE_001", "공지사항을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    NOTICE_NOT_IN_COURSE("COURSE_NOTICE_002", "해당 강의의 공지사항이 아닙니다", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_IN_NOTICE("COURSE_NOTICE_003", "해당 공지사항의 댓글이 아닙니다", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_ALLOWED("COURSE_NOTICE_004", "해당 공지사항은 댓글이 허용되지 않습니다", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND("COURSE_NOTICE_005", "댓글을 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 권한 관련 (AUTH_0XX)
    PROFESSOR_ONLY("COURSE_AUTH_001", "해당 강의의 담당 교수만 접근 가능합니다", HttpStatus.FORBIDDEN),
    ENROLLED_OR_PROFESSOR_ONLY("COURSE_AUTH_002", "해당 강의의 수강생 또는 담당 교수만 접근 가능합니다", HttpStatus.FORBIDDEN),
    COMMENT_OWNER_ONLY("COURSE_AUTH_003", "댓글 작성자만 수정/삭제할 수 있습니다", HttpStatus.FORBIDDEN),

    // 강의자료 관련 (MATERIAL_0XX)
    MATERIAL_NOT_FOUND("COURSE_MATERIAL_001", "강의자료를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    MATERIAL_UPLOAD_FAILED("COURSE_MATERIAL_002", "강의자료 업로드에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // 주차 관련 (WEEK_0XX)
    WEEK_NOT_FOUND("COURSE_WEEK_001", "주차를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    WEEK_NOT_IN_COURSE("COURSE_WEEK_002", "해당 강의의 주차가 아닙니다", HttpStatus.BAD_REQUEST),
    WEEK_OPERATION_NOT_AUTHORIZED("COURSE_WEEK_003", "주차 작업 권한이 없습니다", HttpStatus.FORBIDDEN),
    MAX_WEEK_EXCEEDED("COURSE_WEEK_004", "주차는 최대 16주까지만 생성할 수 있습니다", HttpStatus.BAD_REQUEST),
    WEEK_ALREADY_EXISTS("COURSE_WEEK_005", "이미 존재하는 주차입니다", HttpStatus.CONFLICT),

    // 콘텐츠 관련 (CONTENT_0XX)
    CONTENT_NOT_FOUND("COURSE_CONTENT_001", "콘텐츠를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    CONTENT_NOT_IN_WEEK("COURSE_CONTENT_002", "해당 주차의 콘텐츠가 아닙니다", HttpStatus.BAD_REQUEST),
    CONTENT_NOT_IN_COURSE("COURSE_CONTENT_003", "해당 강의의 콘텐츠가 아닙니다", HttpStatus.BAD_REQUEST),
    CONTENT_OPERATION_NOT_AUTHORIZED("COURSE_CONTENT_004", "콘텐츠 작업 권한이 없습니다", HttpStatus.FORBIDDEN),
    CONTENT_FIELD_REQUIRED("COURSE_CONTENT_005", "콘텐츠 필드는 필수입니다", HttpStatus.BAD_REQUEST),
    CONTENT_ORDER_DUPLICATE("COURSE_CONTENT_006", "이미 동일한 순서의 콘텐츠가 존재합니다", HttpStatus.CONFLICT),
    CONTENT_TYPE_INVALID("COURSE_CONTENT_007", "지원하지 않는 콘텐츠 타입입니다", HttpStatus.BAD_REQUEST),
    CONTENT_CREATE_FAILED("COURSE_CONTENT_008", "콘텐츠 생성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // 요청 관련 (REQUEST_0XX)
    COURSE_ID_REQUIRED("COURSE_REQUEST_001", "강의 ID는 필수입니다", HttpStatus.BAD_REQUEST),
    WEEK_ID_REQUIRED("COURSE_REQUEST_002", "주차 ID는 필수입니다", HttpStatus.BAD_REQUEST),
    CONTENT_ID_REQUIRED("COURSE_REQUEST_003", "콘텐츠 ID는 필수입니다", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("COURSE_REQUEST_004", "잘못된 요청입니다", HttpStatus.BAD_REQUEST),
    PROFESSOR_ID_REQUIRED("COURSE_REQUEST_005", "교수 ID는 필수입니다", HttpStatus.BAD_REQUEST),
    REQUEST_INFO_REQUIRED("COURSE_REQUEST_006", "요청 정보는 필수입니다", HttpStatus.BAD_REQUEST),
    ENROLLMENT_PERIOD_ID_REQUIRED("COURSE_REQUEST_007", "수강신청 기간 ID는 필수입니다", HttpStatus.BAD_REQUEST),
    ACADEMIC_TERM_ID_REQUIRED("COURSE_REQUEST_008", "학기 ID는 필수입니다", HttpStatus.BAD_REQUEST),

    // 수강신청/학기 관련 (ENROLLMENT_0XX)
    ENROLLMENT_PERIOD_NOT_FOUND("COURSE_ENROLLMENT_001", "수강신청 기간을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    ACADEMIC_TERM_NOT_FOUND("COURSE_ENROLLMENT_002", "학기를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    ACADEMIC_TERM_NOT_LINKED("COURSE_ENROLLMENT_003", "수강신청 기간에 연결된 학기 정보가 없습니다", HttpStatus.BAD_REQUEST),
    NO_COURSES_IN_TERM("COURSE_ENROLLMENT_004", "해당 학기의 강의가 없습니다", HttpStatus.NOT_FOUND),
    DEPARTMENT_NOT_FOUND("COURSE_ENROLLMENT_005", "해당 학과는 존재하지 않습니다", HttpStatus.NOT_FOUND),

    // 교수 관련 (PROFESSOR_0XX)
    PROFESSOR_NOT_FOUND("COURSE_PROFESSOR_001", "교수를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    PROFESSOR_DEPARTMENT_NOT_FOUND("COURSE_PROFESSOR_002", "교수의 주 소속 학과를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 과목 관련 (SUBJECT_0XX)
    SUBJECT_NOT_FOUND("COURSE_SUBJECT_001", "과목을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    SUBJECT_INFO_REQUIRED("COURSE_SUBJECT_002", "과목 정보는 필수입니다", HttpStatus.BAD_REQUEST),
    SUBJECT_CODE_DUPLICATE("COURSE_SUBJECT_003", "이미 존재하는 과목 코드입니다", HttpStatus.CONFLICT),
    SUBJECT_ID_OR_SUBJECT_REQUIRED("COURSE_SUBJECT_004", "subjectId와 subject 중 하나만 제공해야 합니다", HttpStatus.BAD_REQUEST),
    COURSE_TYPE_INVALID("COURSE_SUBJECT_005", "유효하지 않은 이수구분입니다", HttpStatus.BAD_REQUEST),
    DEPARTMENT_MISMATCH("COURSE_SUBJECT_006", "학과를 찾을 수 없습니다", HttpStatus.BAD_REQUEST),
    MAJOR_SUBJECT_DEPARTMENT_MISMATCH("COURSE_SUBJECT_007", "전공과목은 해당 학과에서만 개설할 수 있습니다", HttpStatus.BAD_REQUEST),
    PREREQUISITE_NOT_FOUND("COURSE_SUBJECT_008", "선수과목을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    SEARCH_QUERY_TOO_SHORT("COURSE_SUBJECT_009", "검색어는 최소 2글자 이상이어야 합니다", HttpStatus.BAD_REQUEST),

    // 강의 개설/수정 관련 (COURSE_OP_0XX)
    COURSE_DUPLICATE("COURSE_OP_001", "이미 동일한 과목, 학기, 분반으로 개설된 강의가 있습니다", HttpStatus.CONFLICT),
    SCHEDULE_CONFLICT("COURSE_OP_002", "시간표 충돌이 발생했습니다", HttpStatus.CONFLICT),
    SECTION_DUPLICATE("COURSE_OP_003", "이미 동일한 분반으로 개설된 강의가 있습니다", HttpStatus.CONFLICT),
    MAX_STUDENTS_BELOW_CURRENT("COURSE_OP_004", "현재 수강생 수보다 적은 정원으로 설정할 수 없습니다", HttpStatus.BAD_REQUEST),
    COURSE_HAS_ENROLLMENTS("COURSE_OP_005", "수강생이 있어 강의를 취소할 수 없습니다", HttpStatus.BAD_REQUEST),
    COURSE_UPDATE_NOT_AUTHORIZED("COURSE_OP_006", "강의 수정 권한이 없습니다", HttpStatus.FORBIDDEN),
    COURSE_CANCEL_NOT_AUTHORIZED("COURSE_OP_007", "강의 취소 권한이 없습니다", HttpStatus.FORBIDDEN),
    COURSE_VIEW_NOT_AUTHORIZED("COURSE_OP_008", "강의 조회 권한이 없습니다", HttpStatus.FORBIDDEN),

    // 성적 관련 (GRADE_0XX)
    GRADING_POLICY_NOT_FOUND("COURSE_GRADE_001", "강의 평가비율을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    GRADE_CALCULATION_PERIOD_NOT_FOUND("COURSE_GRADE_002", "period_types에 GRADE_CALCULATION이 없습니다", HttpStatus.NOT_FOUND),
    GRADE_PUBLISH_PERIOD_NOT_FOUND("COURSE_GRADE_003", "period_types에 GRADE_PUBLISH가 없습니다", HttpStatus.NOT_FOUND),
    NOT_GRADE_CALCULATION_PERIOD("COURSE_GRADE_004", "성적 산출기간이 아닙니다", HttpStatus.BAD_REQUEST),
    NOT_GRADE_PUBLISH_PERIOD("COURSE_GRADE_005", "성적 공개 기간이 아닙니다", HttpStatus.BAD_REQUEST),
    COURSE_ACADEMIC_TERM_NOT_FOUND("COURSE_GRADE_006", "강의에 학기 정보가 없습니다", HttpStatus.BAD_REQUEST),
    GRADE_PUBLISH_BLOCKED("COURSE_GRADE_007", "성적 공개가 불가합니다", HttpStatus.BAD_REQUEST),
    GRADE_VIEW_NOT_AUTHORIZED("COURSE_GRADE_008", "해당 강의 성적 조회 권한이 없습니다", HttpStatus.FORBIDDEN),
    GRADE_CALCULATE_NOT_AUTHORIZED("COURSE_GRADE_009", "해당 강의 성적 산출 권한이 없습니다", HttpStatus.FORBIDDEN),
    GRADE_PUBLISH_NOT_AUTHORIZED("COURSE_GRADE_010", "해당 강의 성적 산출/공개 권한이 없습니다", HttpStatus.FORBIDDEN),
    ;

    private static final String DOMAIN = "COURSE";

    private final String code;
    private final String message;
    private final HttpStatus status;

    CourseErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
