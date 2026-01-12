package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Department;

import java.util.List;
import java.util.Optional;

/**
 * Department 조회 Port
 * User 도메인에서 Department 엔티티 조회를 위한 인터페이스
 */
public interface DepartmentQueryPort {

    /**
     * 모든 학과 조회
     */
    List<Department> findAll();

    /**
     * ID로 학과 조회
     */
    Optional<Department> findById(Long id);

    /**
     * 단과대학별 학과 목록 조회
     */
    List<Department> findByCollegeId(Long collegeId);

    /**
     * 학과 코드로 조회
     */
    Optional<Department> findByDepartmentCode(String departmentCode);
}
