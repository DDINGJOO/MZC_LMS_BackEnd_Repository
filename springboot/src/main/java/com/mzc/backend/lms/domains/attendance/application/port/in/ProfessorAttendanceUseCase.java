package com.mzc.backend.lms.domains.attendance.application.port.in;

import com.mzc.backend.lms.domains.attendance.adapter.in.web.dto.CourseAttendanceOverviewDto;
import com.mzc.backend.lms.domains.attendance.adapter.in.web.dto.StudentAttendanceDto;
import com.mzc.backend.lms.domains.attendance.adapter.in.web.dto.WeekStudentAttendanceDto;

import java.util.List;

/**
 * 교수용 출석 조회 UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface ProfessorAttendanceUseCase {

    /**
     * 교수의 강의 전체 출석 현황 조회
     */
    CourseAttendanceOverviewDto getProfessorCourseAttendance(Long professorId, Long courseId);

    /**
     * 교수의 강의 학생별 출석 목록 조회
     */
    List<StudentAttendanceDto> getProfessorStudentAttendances(Long professorId, Long courseId);

    /**
     * 교수의 주차별 학생 출석 현황 조회
     */
    List<WeekStudentAttendanceDto> getProfessorWeekAttendances(Long professorId, Long courseId, Long weekId);
}
