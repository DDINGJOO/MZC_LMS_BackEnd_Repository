package com.mzc.backend.lms.domains.course.course.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import org.springframework.data.jpa.domain.Specification;

/**
 * Course 엔티티용 Specification 클래스
 * 동적 쿼리 조건을 재사용 가능한 형태로 제공
 */
public final class CourseSpecifications {

    private CourseSpecifications() {
        // 유틸리티 클래스 - 인스턴스화 방지
    }

    /**
     * 학과 ID로 필터링
     */
    public static Specification<Course> byDepartmentId(Long departmentId) {
        return (root, query, cb) ->
                departmentId == null ? null : cb.equal(root.get("subject").get("department").get("id"), departmentId);
    }

    /**
     * 과목 ID로 필터링
     */
    public static Specification<Course> bySubjectId(Long subjectId) {
        return (root, query, cb) ->
                subjectId == null ? null : cb.equal(root.get("subject").get("id"), subjectId);
    }

    /**
     * 학기 ID로 필터링
     */
    public static Specification<Course> byAcademicTermId(Long academicTermId) {
        return (root, query, cb) ->
                academicTermId == null ? null : cb.equal(root.get("academicTerm").get("id"), academicTermId);
    }

    /**
     * 교수 ID로 필터링
     */
    public static Specification<Course> byProfessorId(Long professorId) {
        return (root, query, cb) ->
                professorId == null ? null : cb.equal(root.get("professor").get("professorId"), professorId);
    }

    /**
     * 이수구분(CourseType) 코드로 필터링
     */
    public static Specification<Course> byCourseTypeCode(Integer typeCode) {
        return (root, query, cb) ->
                typeCode == null ? null : cb.equal(root.get("subject").get("courseType").get("typeCode"), typeCode);
    }

    /**
     * 학점으로 필터링
     */
    public static Specification<Course> byCredits(Integer credits) {
        return (root, query, cb) ->
                credits == null ? null : cb.equal(root.get("subject").get("credits"), credits);
    }

    /**
     * 과목명 검색 (부분 일치)
     */
    public static Specification<Course> subjectNameContains(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank() ? null : cb.like(root.get("subject").get("subjectName"), "%" + keyword + "%");
    }

    /**
     * 과목코드 검색 (정확히 일치)
     */
    public static Specification<Course> bySubjectCode(String subjectCode) {
        return (root, query, cb) ->
                subjectCode == null || subjectCode.isBlank() ? null : cb.equal(root.get("subject").get("subjectCode"), subjectCode);
    }

    /**
     * 잔여석 있는 강의만 필터링
     */
    public static Specification<Course> hasAvailableSeats() {
        return (root, query, cb) ->
                cb.lessThan(root.get("currentStudents"), root.get("maxStudents"));
    }

    /**
     * 분반 번호로 필터링
     */
    public static Specification<Course> bySectionNumber(String sectionNumber) {
        return (root, query, cb) ->
                sectionNumber == null || sectionNumber.isBlank() ? null : cb.equal(root.get("sectionNumber"), sectionNumber);
    }
}
