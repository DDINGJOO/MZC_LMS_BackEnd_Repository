package com.mzc.backend.lms.domains.course.course.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.course.application.port.out.SubjectPrerequisitesPort;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.SubjectPrerequisites;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.repository.SubjectPrerequisitesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 선수과목 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class SubjectPrerequisitesPersistenceAdapter implements SubjectPrerequisitesPort {

    private final SubjectPrerequisitesRepository subjectPrerequisitesRepository;

    @Override
    public List<SubjectPrerequisites> findBySubjectId(Long subjectId) {
        return subjectPrerequisitesRepository.findBySubjectId(subjectId);
    }
}
