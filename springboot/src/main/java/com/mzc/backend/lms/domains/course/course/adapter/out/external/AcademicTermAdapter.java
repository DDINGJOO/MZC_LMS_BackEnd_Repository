package com.mzc.backend.lms.domains.course.course.adapter.out.external;

import com.mzc.backend.lms.domains.course.course.application.port.out.AcademicTermPort;
import com.mzc.backend.lms.domains.academy.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.repository.AcademicTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 학기 외부 Adapter (academy 도메인)
 */
@Component("courseAcademicTermAdapter")
@RequiredArgsConstructor
public class AcademicTermAdapter implements AcademicTermPort {

    private final AcademicTermRepository academicTermRepository;

    @Override
    public Optional<AcademicTerm> findById(Long id) {
        return academicTermRepository.findById(id);
    }
}
