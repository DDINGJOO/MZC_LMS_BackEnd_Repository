package com.mzc.lms.catalog.adapter.out.persistence.repository;

import com.mzc.lms.catalog.adapter.out.persistence.entity.CourseTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseTypeJpaRepository extends JpaRepository<CourseTypeEntity, Long> {

    Optional<CourseTypeEntity> findByTypeCode(Integer typeCode);

    boolean existsByTypeCode(Integer typeCode);

    List<CourseTypeEntity> findByCategory(Integer category);
}
