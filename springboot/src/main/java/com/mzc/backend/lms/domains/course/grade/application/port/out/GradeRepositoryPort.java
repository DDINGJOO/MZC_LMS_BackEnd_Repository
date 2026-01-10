package com.mzc.backend.lms.domains.course.grade.application.port.out;

import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.entity.Grade;
import com.mzc.backend.lms.domains.course.grade.domain.enums.GradeStatus;

import java.util.List;
import java.util.Optional;

/**
 * 성적 영속성 Port
 */
public interface GradeRepositoryPort {

    /**
     * 성적 저장
     */
    Grade save(Grade grade);

    /**
     * 강의 ID와 학생 ID로 성적 조회
     */
    Optional<Grade> findByCourseIdAndStudentId(Long courseId, Long studentId);

    /**
     * 강의 ID와 학생 ID 목록으로 성적 목록 조회
     */
    List<Grade> findByCourseIdAndStudentIdIn(Long courseId, List<Long> studentIds);

    /**
     * 강의 ID와 학생 ID 목록, 상태로 성적 목록 조회
     */
    List<Grade> findByCourseIdAndStudentIdInAndStatus(Long courseId, List<Long> studentIds, GradeStatus status);

    /**
     * 학생 ID와 상태로 성적 목록 조회 (학기 역순, 강의 순)
     */
    List<Grade> findByStudentIdAndStatusOrderByAcademicTermIdDescCourseIdAsc(Long studentId, GradeStatus status);

    /**
     * 학생 ID, 학기 ID, 상태로 성적 목록 조회
     */
    List<Grade> findByStudentIdAndAcademicTermIdAndStatusOrderByCourseIdAsc(Long studentId, Long academicTermId, GradeStatus status);
}
