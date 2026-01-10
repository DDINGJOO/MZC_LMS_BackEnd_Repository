package com.mzc.backend.lms.domains.course.subject.adapter.out.external;

import com.mzc.backend.lms.domains.course.subject.application.port.out.SubjectCourseTypePort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 이수구분 외부 Adapter (course/course 도메인)
 */
@Component
@RequiredArgsConstructor
public class SubjectCourseTypeAdapter implements SubjectCourseTypePort {

    private final CourseTypeRepository courseTypeRepository;

    @Override
    public Optional<Long> findIdByTypeCode(int typeCode) {
        return courseTypeRepository.findByTypeCode(typeCode)
                .map(ct -> ct.getId());
    }
}
