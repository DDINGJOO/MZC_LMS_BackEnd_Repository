package com.mzc.backend.lms.domains.course.course.adapter.out.persistence;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.mapper.AcademicTermMapper;
import com.mzc.backend.lms.domains.academy.application.port.out.CourseQueryPort;
import com.mzc.backend.lms.domains.academy.domain.model.AcademicTermDomain;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * academy 도메인의 CourseQueryPort 구현체
 * course 도메인이 academy에 데이터를 제공하는 Adapter
 */
@Component
@RequiredArgsConstructor
public class AcademyCourseQueryAdapter implements CourseQueryPort {

    private final CourseRepository courseRepository;

    @Override
    public List<AcademicTermDomain> findDistinctAcademicTermsByProfessorId(Long professorId) {
        return courseRepository.findDistinctAcademicTermsByProfessorId(professorId).stream()
                .map(AcademicTermMapper::toDomain)
                .toList();
    }
}
