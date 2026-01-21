package com.mzc.lms.catalog.adapter.out.persistence;

import com.mzc.lms.catalog.adapter.out.persistence.entity.CourseTypeEntity;
import com.mzc.lms.catalog.adapter.out.persistence.repository.CourseTypeJpaRepository;
import com.mzc.lms.catalog.application.port.out.CourseTypeRepositoryPort;
import com.mzc.lms.catalog.domain.model.CourseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseTypePersistenceAdapter implements CourseTypeRepositoryPort {

    private final CourseTypeJpaRepository courseTypeJpaRepository;

    @Override
    public CourseType save(CourseType courseType) {
        CourseTypeEntity entity = CourseTypeEntity.fromDomain(courseType);
        CourseTypeEntity saved = courseTypeJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<CourseType> findById(Long id) {
        return courseTypeJpaRepository.findById(id)
                .map(CourseTypeEntity::toDomain);
    }

    @Override
    public Optional<CourseType> findByTypeCode(Integer typeCode) {
        return courseTypeJpaRepository.findByTypeCode(typeCode)
                .map(CourseTypeEntity::toDomain);
    }

    @Override
    public List<CourseType> findAll() {
        return courseTypeJpaRepository.findAll().stream()
                .map(CourseTypeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseType> findByCategory(Integer category) {
        return courseTypeJpaRepository.findByCategory(category).stream()
                .map(CourseTypeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return courseTypeJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByTypeCode(Integer typeCode) {
        return courseTypeJpaRepository.existsByTypeCode(typeCode);
    }
}
