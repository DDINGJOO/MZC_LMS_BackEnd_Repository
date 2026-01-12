package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;

import java.util.Optional;

/**
 * Professor 영속성 Port
 * Auth 도메인에서 Professor 엔티티 접근을 위한 인터페이스
 */
public interface ProfessorRepositoryPort {

    /**
     * 교수 저장
     */
    Professor save(Professor professor);

    /**
     * 교번으로 교수 조회
     */
    Optional<Professor> findById(Long professorNumber);

    /**
     * 교번 존재 여부 확인
     */
    boolean existsById(Long professorNumber);
}
