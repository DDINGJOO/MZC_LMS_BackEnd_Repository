package com.mzc.backend.lms.domains.course.subject.application.port.out;

import java.util.Optional;

/**
 * 이수구분 외부 Port (course/course 도메인)
 */
public interface SubjectCourseTypePort {

    /**
     * 타입 코드로 이수구분 ID 조회
     */
    Optional<Long> findIdByTypeCode(int typeCode);
}
