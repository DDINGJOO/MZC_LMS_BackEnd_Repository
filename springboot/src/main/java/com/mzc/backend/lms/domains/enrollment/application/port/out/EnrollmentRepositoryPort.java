package com.mzc.backend.lms.domains.enrollment.application.port.out;

import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;

import java.util.List;
import java.util.Optional;

/**
 * Enrollment 영속성을 위한 Port
 */
public interface EnrollmentRepositoryPort {

    /**
     * 수강신청 저장
     */
    Enrollment save(Enrollment enrollment);

    /**
     * ID로 수강신청 조회
     */
    Optional<Enrollment> findById(Long id);

    /**
     * 학생 ID로 수강신청 목록 조회
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * 학생 ID와 학기 ID로 수강신청 목록 조회
     */
    List<Enrollment> findByStudentIdAndAcademicTermId(Long studentId, Long academicTermId);

    /**
     * 학생이 특정 강의에 수강신청했는지 확인
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 수강신청 삭제
     */
    void delete(Enrollment enrollment);

    /**
     * ID로 수강신청 삭제
     */
    void deleteById(Long id);
}
