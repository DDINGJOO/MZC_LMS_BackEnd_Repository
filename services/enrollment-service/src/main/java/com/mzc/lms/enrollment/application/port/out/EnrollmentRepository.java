package com.mzc.lms.enrollment.application.port.out;

import com.mzc.lms.enrollment.domain.model.Enrollment;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {

    Enrollment save(Enrollment enrollment);

    Optional<Enrollment> findById(Long id);

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseIdAndStatusIn(Long studentId, Long courseId, List<EnrollmentStatus> statuses);

    long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    List<Enrollment> findByCourseIdAndStatusOrderByCreatedAtAsc(Long courseId, EnrollmentStatus status);

    void deleteById(Long id);
}
