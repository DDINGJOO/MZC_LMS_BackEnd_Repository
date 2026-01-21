package com.mzc.lms.catalog.application.service;

import com.mzc.lms.catalog.application.port.in.CourseTypeUseCase;
import com.mzc.lms.catalog.application.port.out.CourseTypeRepositoryPort;
import com.mzc.lms.catalog.application.port.out.EventPublisherPort;
import com.mzc.lms.catalog.domain.event.CatalogEvent;
import com.mzc.lms.catalog.domain.model.CourseType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseTypeService implements CourseTypeUseCase {

    private static final String CACHE_COURSE_TYPES = "courseTypes";

    private final CourseTypeRepositoryPort courseTypeRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = CACHE_COURSE_TYPES, allEntries = true)
    public CourseType createCourseType(Integer typeCode, Integer category) {
        if (courseTypeRepository.existsByTypeCode(typeCode)) {
            throw new IllegalArgumentException("Course type code already exists: " + typeCode);
        }

        CourseType courseType = CourseType.create(typeCode, category);
        CourseType saved = courseTypeRepository.save(courseType);
        eventPublisher.publish(CatalogEvent.courseTypeCreated(saved.getId(), saved));

        return saved;
    }

    @Override
    @Cacheable(value = CACHE_COURSE_TYPES, key = "#id")
    public Optional<CourseType> getCourseType(Long id) {
        return courseTypeRepository.findById(id);
    }

    @Override
    @Cacheable(value = CACHE_COURSE_TYPES, key = "'code:' + #typeCode")
    public Optional<CourseType> getCourseTypeByTypeCode(Integer typeCode) {
        return courseTypeRepository.findByTypeCode(typeCode);
    }

    @Override
    @Cacheable(value = CACHE_COURSE_TYPES, key = "'all'")
    public List<CourseType> getAllCourseTypes() {
        return courseTypeRepository.findAll();
    }

    @Override
    @Cacheable(value = CACHE_COURSE_TYPES, key = "'category:' + #category")
    public List<CourseType> getCourseTypesByCategory(Integer category) {
        return courseTypeRepository.findByCategory(category);
    }
}
