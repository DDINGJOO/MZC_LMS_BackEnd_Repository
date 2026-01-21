package com.mzc.lms.catalog.application.port.in;

import com.mzc.lms.catalog.domain.model.CourseType;

import java.util.List;
import java.util.Optional;

public interface CourseTypeUseCase {

    CourseType createCourseType(Integer typeCode, Integer category);

    Optional<CourseType> getCourseType(Long id);

    Optional<CourseType> getCourseTypeByTypeCode(Integer typeCode);

    List<CourseType> getAllCourseTypes();

    List<CourseType> getCourseTypesByCategory(Integer category);
}
