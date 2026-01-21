package com.mzc.lms.organization.adapter.out.persistence.repository;

import com.mzc.lms.organization.adapter.out.persistence.entity.CollegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollegeJpaRepository extends JpaRepository<CollegeEntity, Long> {

    Optional<CollegeEntity> findByCollegeCode(String collegeCode);

    boolean existsByCollegeCode(String collegeCode);
}
