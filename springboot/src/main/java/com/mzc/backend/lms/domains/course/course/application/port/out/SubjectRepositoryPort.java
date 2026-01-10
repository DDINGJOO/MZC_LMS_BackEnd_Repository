package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Subject 영속성 Port
 */
public interface SubjectRepositoryPort {

    /**
     * Subject 저장
     */
    Subject save(Subject subject);

    /**
     * ID로 Subject 조회
     */
    Optional<Subject> findById(Long id);

    /**
     * ID로 상세 정보 조회 (Fetch Join)
     */
    Optional<Subject> findByIdWithDetails(Long id);

    /**
     * 학과와 과목코드로 중복 체크
     */
    boolean existsByDepartmentIdAndSubjectCode(Long departmentId, String subjectCode);

    /**
     * 필터를 적용한 과목 조회
     */
    Page<Subject> findSubjectsWithFilters(Long departmentId, String keyword, Long courseTypeId, Integer credits, Pageable pageable);

    /**
     * 과목 검색
     */
    Page<Subject> searchSubjects(String query, Pageable pageable);
}