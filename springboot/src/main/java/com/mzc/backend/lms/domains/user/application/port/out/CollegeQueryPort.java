package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.College;

import java.util.List;
import java.util.Optional;

/**
 * College 조회 Port
 * User 도메인에서 College 엔티티 조회를 위한 인터페이스
 */
public interface CollegeQueryPort {

    /**
     * 모든 단과대학 조회
     */
    List<College> findAll();

    /**
     * ID로 단과대학 조회
     */
    Optional<College> findById(Long id);

    /**
     * 단과대학 코드로 조회
     */
    Optional<College> findByCollegeCode(String collegeCode);
}
