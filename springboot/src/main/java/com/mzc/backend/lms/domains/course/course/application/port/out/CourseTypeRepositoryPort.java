package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseType;

import java.util.Optional;

/**
 * CourseType 영속성 Port
 */
public interface CourseTypeRepositoryPort {

    /**
     * 타입 코드로 CourseType 조회
     */
    Optional<CourseType> findByTypeCode(int typeCode);

    /**
     * ID로 CourseType 조회
     */
    Optional<CourseType> findById(Long id);
}
