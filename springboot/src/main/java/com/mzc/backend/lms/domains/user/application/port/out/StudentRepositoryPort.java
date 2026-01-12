package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;

import java.util.Optional;

/**
 * Student 영속성 Port
 * Auth 도메인에서 Student 엔티티 접근을 위한 인터페이스
 */
public interface StudentRepositoryPort {

    /**
     * 학생 저장
     */
    Student save(Student student);

    /**
     * 학번으로 학생 조회
     */
    Optional<Student> findById(Long studentNumber);

    /**
     * 학번 존재 여부 확인
     */
    boolean existsById(Long studentNumber);
}
