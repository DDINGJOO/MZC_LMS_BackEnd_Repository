package com.mzc.backend.lms.domains.attendance.application.port.in;

import com.mzc.backend.lms.domains.attendance.adapter.in.web.dto.CourseAttendanceDto;
import com.mzc.backend.lms.domains.attendance.adapter.in.web.dto.CourseAttendanceSummaryDto;

import java.util.List;

/**
 * 학생용 출석 조회 UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface StudentAttendanceUseCase {

    /**
     * 학생의 특정 강의 출석 현황 조회
     */
    CourseAttendanceDto getStudentCourseAttendance(Long studentId, Long courseId);

    /**
     * 학생의 전체 출석 현황 조회
     */
    List<CourseAttendanceSummaryDto> getStudentAllAttendance(Long studentId);
}
