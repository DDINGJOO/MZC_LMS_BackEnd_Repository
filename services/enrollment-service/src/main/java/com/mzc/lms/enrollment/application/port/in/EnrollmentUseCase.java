package com.mzc.lms.enrollment.application.port.in;

import com.mzc.lms.enrollment.domain.model.Enrollment;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public interface EnrollmentUseCase {

    Enrollment createEnrollment(Long studentId, Long courseId);

    Enrollment confirmEnrollment(Long enrollmentId);

    Enrollment waitlistEnrollment(Long enrollmentId);

    Enrollment withdrawEnrollment(Long enrollmentId, String reason);

    Enrollment completeEnrollment(Long enrollmentId, Integer grade, String gradePoint);

    Enrollment cancelEnrollment(Long enrollmentId);

    Optional<Enrollment> findById(Long id);

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean isStudentEnrolled(Long studentId, Long courseId);

    long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    List<Enrollment> findWaitlistedByCourseId(Long courseId);
}
