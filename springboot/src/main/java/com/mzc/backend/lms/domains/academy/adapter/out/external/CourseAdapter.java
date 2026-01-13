package com.mzc.backend.lms.domains.academy.adapter.out.external;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.application.port.out.CourseQueryPort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Course 도메인 연동 Adapter
 */
@Component
@RequiredArgsConstructor
public class CourseAdapter implements CourseQueryPort {

    private final CourseRepository courseRepository;

    @Override
    public List<AcademicTerm> findDistinctAcademicTermsByProfessorId(Long professorId) {
        return courseRepository.findDistinctAcademicTermsByProfessorId(professorId);
    }
}
