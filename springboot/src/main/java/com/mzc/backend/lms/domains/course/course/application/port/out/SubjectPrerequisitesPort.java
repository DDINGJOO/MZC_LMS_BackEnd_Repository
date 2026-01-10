package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.SubjectPrerequisites;

import java.util.List;

/**
 * 선수과목 영속성 Port
 */
public interface SubjectPrerequisitesPort {

    /**
     * Subject ID로 선수과목 목록 조회
     */
    List<SubjectPrerequisites> findBySubjectId(Long subjectId);
}
