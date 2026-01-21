package com.mzc.lms.organization.adapter.out.persistence.repository;

import com.mzc.lms.organization.adapter.out.persistence.entity.AcademicTermEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicTermJpaRepository extends JpaRepository<AcademicTermEntity, Long> {

    List<AcademicTermEntity> findByYear(Integer year);

    @Query("SELECT t FROM AcademicTermEntity t WHERE t.startDate <= :date AND t.endDate >= :date")
    Optional<AcademicTermEntity> findCurrentTerm(@Param("date") LocalDate date);

    boolean existsByYearAndTermType(Integer year, String termType);
}
