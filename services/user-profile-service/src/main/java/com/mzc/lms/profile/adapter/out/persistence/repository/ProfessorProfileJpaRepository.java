package com.mzc.lms.profile.adapter.out.persistence.repository;

import com.mzc.lms.profile.adapter.out.persistence.entity.ProfessorProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorProfileJpaRepository extends JpaRepository<ProfessorProfileEntity, Long> {

    Optional<ProfessorProfileEntity> findByUserId(Long userId);

    Optional<ProfessorProfileEntity> findByProfessorId(Long professorId);
}
