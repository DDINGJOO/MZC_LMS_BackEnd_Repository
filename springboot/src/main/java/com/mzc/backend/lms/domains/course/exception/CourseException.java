package com.mzc.backend.lms.domains.course.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Course 도메인 예외 클래스
 * <p>
 * 강의, 공지사항, 강의자료 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class CourseException extends CommonException {

    private final CourseErrorCode courseErrorCode;

    public CourseException(CourseErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.courseErrorCode = errorCode;
    }

    public CourseException(CourseErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.courseErrorCode = errorCode;
    }

    public CourseException(CourseErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.courseErrorCode = errorCode;
    }

    public CourseException(CourseErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.courseErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "COURSE_DOMAIN";
    }

    // 팩토리 메서드들

    public static CourseException courseNotFound(Long courseId) {
        return new CourseException(CourseErrorCode.COURSE_NOT_FOUND,
            String.format("강의 ID: %d", courseId));
    }

    public static CourseException courseAlreadyExists(String courseName) {
        return new CourseException(CourseErrorCode.COURSE_ALREADY_EXISTS,
            String.format("강의명: %s", courseName));
    }

    public static CourseException courseClosed(Long courseId) {
        return new CourseException(CourseErrorCode.COURSE_CLOSED,
            String.format("강의 ID: %d", courseId));
    }

    public static CourseException noticeNotFound(Long noticeId) {
        return new CourseException(CourseErrorCode.NOTICE_NOT_FOUND,
            String.format("공지사항 ID: %d", noticeId));
    }

    public static CourseException noticeNotInCourse(Long noticeId, Long courseId) {
        return new CourseException(CourseErrorCode.NOTICE_NOT_IN_COURSE,
            String.format("공지사항 ID: %d, 강의 ID: %d", noticeId, courseId));
    }

    public static CourseException commentNotInNotice(Long commentId, Long noticeId) {
        return new CourseException(CourseErrorCode.COMMENT_NOT_IN_NOTICE,
            String.format("댓글 ID: %d, 공지사항 ID: %d", commentId, noticeId));
    }

    public static CourseException commentNotAllowed(Long noticeId) {
        return new CourseException(CourseErrorCode.COMMENT_NOT_ALLOWED,
            String.format("공지사항 ID: %d", noticeId));
    }

    public static CourseException professorOnly() {
        return new CourseException(CourseErrorCode.PROFESSOR_ONLY);
    }

    public static CourseException enrolledOrProfessorOnly() {
        return new CourseException(CourseErrorCode.ENROLLED_OR_PROFESSOR_ONLY);
    }

    public static CourseException commentOwnerOnly() {
        return new CourseException(CourseErrorCode.COMMENT_OWNER_ONLY);
    }

    public static CourseException commentNotFound(Long commentId) {
        return new CourseException(CourseErrorCode.COMMENT_NOT_FOUND,
            String.format("댓글 ID: %d", commentId));
    }

    public static CourseException materialNotFound(Long materialId) {
        return new CourseException(CourseErrorCode.MATERIAL_NOT_FOUND,
            String.format("강의자료 ID: %d", materialId));
    }

    public static CourseException materialUploadFailed(Throwable cause) {
        return new CourseException(CourseErrorCode.MATERIAL_UPLOAD_FAILED, cause);
    }

    // 주차 관련 팩토리 메서드
    public static CourseException weekNotFound(Long weekId) {
        return new CourseException(CourseErrorCode.WEEK_NOT_FOUND,
            String.format("주차 ID: %d", weekId));
    }

    public static CourseException weekNotInCourse(Long weekId, Long courseId) {
        return new CourseException(CourseErrorCode.WEEK_NOT_IN_COURSE,
            String.format("주차 ID: %d, 강의 ID: %d", weekId, courseId));
    }

    public static CourseException weekOperationNotAuthorized(String operation) {
        return new CourseException(CourseErrorCode.WEEK_OPERATION_NOT_AUTHORIZED,
            String.format("%s 권한이 없습니다", operation));
    }

    public static CourseException maxWeekExceeded() {
        return new CourseException(CourseErrorCode.MAX_WEEK_EXCEEDED);
    }

    public static CourseException weekAlreadyExists(int weekNumber) {
        return new CourseException(CourseErrorCode.WEEK_ALREADY_EXISTS,
            String.format("주차: %d", weekNumber));
    }

    // 콘텐츠 관련 팩토리 메서드
    public static CourseException contentNotFound(Long contentId) {
        return new CourseException(CourseErrorCode.CONTENT_NOT_FOUND,
            String.format("콘텐츠 ID: %d", contentId));
    }

    public static CourseException contentNotInWeek(Long contentId, Long weekId) {
        return new CourseException(CourseErrorCode.CONTENT_NOT_IN_WEEK,
            String.format("콘텐츠 ID: %d, 주차 ID: %d", contentId, weekId));
    }

    public static CourseException contentNotInCourse(Long contentId, Long courseId) {
        return new CourseException(CourseErrorCode.CONTENT_NOT_IN_COURSE,
            String.format("콘텐츠 ID: %d, 강의 ID: %d", contentId, courseId));
    }

    public static CourseException contentOperationNotAuthorized(String operation) {
        return new CourseException(CourseErrorCode.CONTENT_OPERATION_NOT_AUTHORIZED,
            String.format("%s 권한이 없습니다", operation));
    }

    public static CourseException contentFieldRequired(String field) {
        return new CourseException(CourseErrorCode.CONTENT_FIELD_REQUIRED,
            String.format("필드: %s", field));
    }

    public static CourseException contentOrderDuplicate(int order) {
        return new CourseException(CourseErrorCode.CONTENT_ORDER_DUPLICATE,
            String.format("순서: %d", order));
    }

    public static CourseException contentTypeInvalid(String contentType) {
        return new CourseException(CourseErrorCode.CONTENT_TYPE_INVALID,
            String.format("타입: %s", contentType));
    }

    public static CourseException contentCreateFailed() {
        return new CourseException(CourseErrorCode.CONTENT_CREATE_FAILED);
    }

    // 요청 관련 팩토리 메서드
    public static CourseException courseIdRequired() {
        return new CourseException(CourseErrorCode.COURSE_ID_REQUIRED);
    }

    public static CourseException weekIdRequired() {
        return new CourseException(CourseErrorCode.WEEK_ID_REQUIRED);
    }

    public static CourseException contentIdRequired() {
        return new CourseException(CourseErrorCode.CONTENT_ID_REQUIRED);
    }

    public static CourseException invalidRequest() {
        return new CourseException(CourseErrorCode.INVALID_REQUEST);
    }

    public static CourseException professorIdRequired() {
        return new CourseException(CourseErrorCode.PROFESSOR_ID_REQUIRED);
    }

    public static CourseException requestInfoRequired() {
        return new CourseException(CourseErrorCode.REQUEST_INFO_REQUIRED);
    }

    public static CourseException enrollmentPeriodIdRequired() {
        return new CourseException(CourseErrorCode.ENROLLMENT_PERIOD_ID_REQUIRED);
    }

    public static CourseException academicTermIdRequired() {
        return new CourseException(CourseErrorCode.ACADEMIC_TERM_ID_REQUIRED);
    }

    // 수강신청/학기 관련 팩토리 메서드
    public static CourseException enrollmentPeriodNotFound(Long enrollmentPeriodId) {
        return new CourseException(CourseErrorCode.ENROLLMENT_PERIOD_NOT_FOUND,
            String.format("수강신청 기간 ID: %d", enrollmentPeriodId));
    }

    public static CourseException academicTermNotFound(Long academicTermId) {
        return new CourseException(CourseErrorCode.ACADEMIC_TERM_NOT_FOUND,
            String.format("학기 ID: %d", academicTermId));
    }

    public static CourseException academicTermNotLinked() {
        return new CourseException(CourseErrorCode.ACADEMIC_TERM_NOT_LINKED);
    }

    public static CourseException noCoursesInTerm() {
        return new CourseException(CourseErrorCode.NO_COURSES_IN_TERM);
    }

    public static CourseException departmentNotFound() {
        return new CourseException(CourseErrorCode.DEPARTMENT_NOT_FOUND);
    }

    // 교수 관련 팩토리 메서드
    public static CourseException professorNotFound(Long professorId) {
        return new CourseException(CourseErrorCode.PROFESSOR_NOT_FOUND,
            String.format("교수 ID: %d", professorId));
    }

    public static CourseException professorDepartmentNotFound(Long professorId) {
        return new CourseException(CourseErrorCode.PROFESSOR_DEPARTMENT_NOT_FOUND,
            String.format("교수 ID: %d", professorId));
    }

    // 과목 관련 팩토리 메서드
    public static CourseException subjectNotFound(Long subjectId) {
        return new CourseException(CourseErrorCode.SUBJECT_NOT_FOUND,
            String.format("과목 ID: %d", subjectId));
    }

    public static CourseException subjectInfoRequired() {
        return new CourseException(CourseErrorCode.SUBJECT_INFO_REQUIRED);
    }

    public static CourseException subjectCodeDuplicate(String subjectCode) {
        return new CourseException(CourseErrorCode.SUBJECT_CODE_DUPLICATE,
            String.format("과목 코드: %s", subjectCode));
    }

    public static CourseException subjectIdOrSubjectRequired() {
        return new CourseException(CourseErrorCode.SUBJECT_ID_OR_SUBJECT_REQUIRED);
    }

    public static CourseException courseTypeInvalid(String courseType) {
        return new CourseException(CourseErrorCode.COURSE_TYPE_INVALID,
            String.format("이수구분: %s", courseType));
    }

    public static CourseException departmentMismatch(Long departmentId) {
        return new CourseException(CourseErrorCode.DEPARTMENT_MISMATCH,
            String.format("학과 ID: %d", departmentId));
    }

    public static CourseException majorSubjectDepartmentMismatch(String subjectDept, String professorDept) {
        return new CourseException(CourseErrorCode.MAJOR_SUBJECT_DEPARTMENT_MISMATCH,
            String.format("과목 학과: %s, 교수 학과: %s", subjectDept, professorDept));
    }

    public static CourseException prerequisiteNotFound(Long prerequisiteId) {
        return new CourseException(CourseErrorCode.PREREQUISITE_NOT_FOUND,
            String.format("선수과목 ID: %d", prerequisiteId));
    }

    public static CourseException searchQueryTooShort() {
        return new CourseException(CourseErrorCode.SEARCH_QUERY_TOO_SHORT);
    }

    // 강의 개설/수정 관련 팩토리 메서드
    public static CourseException courseDuplicate() {
        return new CourseException(CourseErrorCode.COURSE_DUPLICATE);
    }

    public static CourseException scheduleConflict(String dayName, String startTime, String endTime) {
        return new CourseException(CourseErrorCode.SCHEDULE_CONFLICT,
            String.format("%s %s-%s", dayName, startTime, endTime));
    }

    public static CourseException sectionDuplicate() {
        return new CourseException(CourseErrorCode.SECTION_DUPLICATE);
    }

    public static CourseException maxStudentsBelowCurrent(int currentStudents, int maxStudents) {
        return new CourseException(CourseErrorCode.MAX_STUDENTS_BELOW_CURRENT,
            String.format("현재 수강생: %d명, 요청 정원: %d명", currentStudents, maxStudents));
    }

    public static CourseException courseHasEnrollments(long enrollmentCount) {
        return new CourseException(CourseErrorCode.COURSE_HAS_ENROLLMENTS,
            String.format("수강생 수: %d명", enrollmentCount));
    }

    public static CourseException courseUpdateNotAuthorized() {
        return new CourseException(CourseErrorCode.COURSE_UPDATE_NOT_AUTHORIZED);
    }

    public static CourseException courseCancelNotAuthorized() {
        return new CourseException(CourseErrorCode.COURSE_CANCEL_NOT_AUTHORIZED);
    }

    public static CourseException courseViewNotAuthorized() {
        return new CourseException(CourseErrorCode.COURSE_VIEW_NOT_AUTHORIZED);
    }

    // 성적 관련 팩토리 메서드
    public static CourseException gradingPolicyNotFound(Long courseId) {
        return new CourseException(CourseErrorCode.GRADING_POLICY_NOT_FOUND,
            String.format("강의 ID: %d", courseId));
    }

    public static CourseException gradeCalculationPeriodNotFound() {
        return new CourseException(CourseErrorCode.GRADE_CALCULATION_PERIOD_NOT_FOUND);
    }

    public static CourseException gradePublishPeriodNotFound() {
        return new CourseException(CourseErrorCode.GRADE_PUBLISH_PERIOD_NOT_FOUND);
    }

    public static CourseException notGradeCalculationPeriod() {
        return new CourseException(CourseErrorCode.NOT_GRADE_CALCULATION_PERIOD);
    }

    public static CourseException notGradePublishPeriod() {
        return new CourseException(CourseErrorCode.NOT_GRADE_PUBLISH_PERIOD);
    }

    public static CourseException courseAcademicTermNotFound(Long courseId) {
        return new CourseException(CourseErrorCode.COURSE_ACADEMIC_TERM_NOT_FOUND,
            String.format("강의 ID: %d", courseId));
    }

    public static CourseException gradePublishBlocked(String reason, Long courseId) {
        return new CourseException(CourseErrorCode.GRADE_PUBLISH_BLOCKED,
            String.format("%s courseId=%d", reason, courseId));
    }

    public static CourseException gradeViewNotAuthorized() {
        return new CourseException(CourseErrorCode.GRADE_VIEW_NOT_AUTHORIZED);
    }

    public static CourseException gradeCalculateNotAuthorized() {
        return new CourseException(CourseErrorCode.GRADE_CALCULATE_NOT_AUTHORIZED);
    }

    public static CourseException gradePublishNotAuthorized() {
        return new CourseException(CourseErrorCode.GRADE_PUBLISH_NOT_AUTHORIZED);
    }
}
