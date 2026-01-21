package com.mzc.lms.catalog.application.port.out;

import com.mzc.lms.catalog.domain.model.CourseType;

import java.util.List;
import java.util.Optional;

public interface CourseTypeRepositoryPort {

    CourseType save(CourseType courseType);

    Optional<CourseType> findById(Long id);

    Optional<CourseType> findByTypeCode(Integer typeCode);

    List<CourseType> findAll();

    List<CourseType> findByCategory(Integer category);

    boolean existsById(Long id);

    boolean existsByTypeCode(Integer typeCode);
}
