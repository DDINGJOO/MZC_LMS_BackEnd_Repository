package com.mzc.lms.enrollment.adapter.out.persistence.repository;

import com.mzc.lms.enrollment.adapter.out.persistence.entity.EnrollmentEntity;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Long> {

    List<EnrollmentEntity> findByStudentId(Long studentId);

    List<EnrollmentEntity> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    List<EnrollmentEntity> findByCourseId(Long courseId);

    List<EnrollmentEntity> findByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    Optional<EnrollmentEntity> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseIdAndStatusIn(Long studentId, Long courseId, List<EnrollmentStatus> statuses);

    long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    List<EnrollmentEntity> findByCourseIdAndStatusOrderByCreatedAtAsc(Long courseId, EnrollmentStatus status);
}
