package com.mzc.backend.lms.domains.enrollment.application.port.out;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Course 도메인과의 통신을 위한 Port
 * MSA 전환 시 HTTP Client로 교체 가능
 */
public interface CoursePort {

    /**
     * 강의 정보 조회
     */
    CourseInfo getCourse(Long courseId);

    /**
     * 학기 ID로 강의 목록 조회
     */
    List<CourseInfo> findByAcademicTermId(Long academicTermId);

    /**
     * 강의 정보 조회 (비관적 락)
     * 수강신청 시 정원 체크를 위해 사용
     */
    CourseInfo getCourseWithLock(Long courseId);

    /**
     * 여러 강의 정보 조회
     */
    List<CourseInfo> getCourses(List<Long> courseIds);

    /**
     * 수강 인원 증가
     */
    void increaseCurrentStudents(Long courseId);

    /**
     * 수강 인원 감소
     */
    void decreaseCurrentStudents(Long courseId);

    /**
     * 필수 선수과목 Subject ID 목록 조회
     */
    List<Long> getMandatoryPrerequisiteSubjectIds(Long subjectId);

    // DTO Records

    record CourseInfo(
            Long id,
            Long subjectId,
            String subjectCode,
            String subjectName,
            String sectionNumber,
            int credits,
            int maxStudents,
            int currentStudents,
            Long professorId,
            Long academicTermId,
            String courseTypeCode,
            String courseTypeName,
            int courseTypeId,
            Long departmentId,
            String departmentName,
            List<ScheduleInfo> schedules
    ) {
        public boolean isFull() {
            return currentStudents >= maxStudents;
        }
    }

    record ScheduleInfo(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String classroom
    ) {}
}
