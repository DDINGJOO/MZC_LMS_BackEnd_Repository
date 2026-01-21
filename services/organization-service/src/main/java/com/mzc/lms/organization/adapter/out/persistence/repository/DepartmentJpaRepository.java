package com.mzc.lms.organization.adapter.out.persistence.repository;

import com.mzc.lms.organization.adapter.out.persistence.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentJpaRepository extends JpaRepository<DepartmentEntity, Long> {

    Optional<DepartmentEntity> findByDepartmentCode(String departmentCode);

    List<DepartmentEntity> findByCollegeId(Long collegeId);

    boolean existsByDepartmentCode(String departmentCode);
}
