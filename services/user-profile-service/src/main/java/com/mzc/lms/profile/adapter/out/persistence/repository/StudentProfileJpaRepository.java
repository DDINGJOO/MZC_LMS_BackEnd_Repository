package com.mzc.lms.profile.adapter.out.persistence.repository;

import com.mzc.lms.profile.adapter.out.persistence.entity.StudentProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileJpaRepository extends JpaRepository<StudentProfileEntity, Long> {

    Optional<StudentProfileEntity> findByUserId(Long userId);

    Optional<StudentProfileEntity> findByStudentId(Long studentId);
}
