package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;

import java.util.List;
import java.util.Optional;

/**
 * Professor 조회 Port
 * User 도메인에서 Professor 엔티티 조회를 위한 인터페이스
 */
public interface ProfessorQueryPort {

    /**
     * 교번으로 교수 조회
     */
    Optional<Professor> findById(Long professorId);

    /**
     * 교번 존재 여부 확인
     */
    boolean existsById(Long professorId);

    /**
     * 여러 교번으로 교수 일괄 조회
     */
    List<Professor> findAllById(List<Long> professorIds);
}
