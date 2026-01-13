package com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 기간 타입 JPA Repository
 */
@Repository
public interface PeriodTypeJpaRepository extends JpaRepository<PeriodType, Integer> {

    /**
     * 타입 코드로 기간 타입 조회
     */
    Optional<PeriodType> findByTypeCode(String typeCode);

    /**
     * 타입 코드 존재 여부 확인
     */
    boolean existsByTypeCode(String typeCode);
}
