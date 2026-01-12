package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;

import java.util.List;
import java.util.Optional;

/**
 * Student 조회 Port
 * User 도메인에서 Student 엔티티 조회를 위한 인터페이스
 */
public interface StudentQueryPort {

    /**
     * 학번으로 학생 조회
     */
    Optional<Student> findById(Long studentId);

    /**
     * 학번 존재 여부 확인
     */
    boolean existsById(Long studentId);

    /**
     * 여러 학번으로 학생 일괄 조회
     */
    List<Student> findAllById(List<Long> studentIds);
}
