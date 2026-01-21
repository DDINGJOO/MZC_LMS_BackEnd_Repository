package com.mzc.lms.enrollment.adapter.out.persistence.repository;

import com.mzc.lms.enrollment.adapter.out.persistence.entity.EnrollmentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentHistoryJpaRepository extends JpaRepository<EnrollmentHistoryEntity, Long> {

    List<EnrollmentHistoryEntity> findByEnrollmentId(Long enrollmentId);

    List<EnrollmentHistoryEntity> findByEnrollmentIdOrderByChangedAtDesc(Long enrollmentId);
}
