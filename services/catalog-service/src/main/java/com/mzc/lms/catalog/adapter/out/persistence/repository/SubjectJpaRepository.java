package com.mzc.lms.catalog.adapter.out.persistence.repository;

import com.mzc.lms.catalog.adapter.out.persistence.entity.SubjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, Long>, JpaSpecificationExecutor<SubjectEntity> {

    Optional<SubjectEntity> findBySubjectCode(String subjectCode);

    boolean existsBySubjectCode(String subjectCode);

    Page<SubjectEntity> findByDepartmentId(Long departmentId, Pageable pageable);

    Page<SubjectEntity> findByCourseTypeId(Long courseTypeId, Pageable pageable);

    @Query("SELECT s FROM SubjectEntity s WHERE " +
            "LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.subjectDescription) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<SubjectEntity> search(@Param("query") String query, Pageable pageable);
}
