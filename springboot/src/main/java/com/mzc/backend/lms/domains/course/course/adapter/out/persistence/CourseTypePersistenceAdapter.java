package com.mzc.backend.lms.domains.course.course.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.course.application.port.out.CourseTypeRepositoryPort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseType;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * CourseType 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CourseTypePersistenceAdapter implements CourseTypeRepositoryPort {

    private final CourseTypeRepository courseTypeRepository;

    @Override
    public Optional<CourseType> findByTypeCode(int typeCode) {
        return courseTypeRepository.findByTypeCode(typeCode);
    }

    @Override
    public Optional<CourseType> findById(Long id) {
        return courseTypeRepository.findById(id);
    }
}
