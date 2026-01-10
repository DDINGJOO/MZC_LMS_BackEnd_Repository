package com.mzc.backend.lms.domains.course.subject.application.port.out;

import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 과목 영속성 Port
 */
public interface SubjectRepositoryPort {

    /**
     * ID로 과목 상세 조회 (연관 엔티티 포함)
     */
    Optional<Subject> findByIdWithDetails(Long subjectId);

    /**
     * 필터링된 과목 페이지 조회
     */
    Page<Subject> findSubjectsWithFilters(
            Long departmentId,
            String keyword,
            Long courseTypeId,
            Integer credits,
            Pageable pageable
    );

    /**
     * 과목 검색 (과목명 또는 과목코드)
     */
    Page<Subject> searchSubjects(String query, Pageable pageable);
}
