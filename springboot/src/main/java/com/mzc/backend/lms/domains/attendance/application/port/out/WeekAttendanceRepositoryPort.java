package com.mzc.backend.lms.domains.attendance.application.port.out;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.WeekAttendance;

import java.util.List;
import java.util.Optional;

/**
 * 주차별 출석 영속성을 위한 Port
 */
public interface WeekAttendanceRepositoryPort {

    /**
     * 출석 저장
     */
    WeekAttendance save(WeekAttendance weekAttendance);

    /**
     * ID로 출석 조회
     */
    Optional<WeekAttendance> findById(Long id);

    /**
     * 학생 ID와 주차 ID로 출석 조회
     */
    Optional<WeekAttendance> findByStudentStudentIdAndWeek_Id(Long studentId, Long weekId);

    /**
     * 학생의 특정 강의 출석 목록 조회
     */
    List<WeekAttendance> findByStudentStudentIdAndCourse_Id(Long studentId, Long courseId);

    /**
     * 학생의 모든 출석 목록 조회
     */
    List<WeekAttendance> findByStudentStudentId(Long studentId);

    /**
     * 특정 강의의 모든 학생 출석 목록 조회 (교수용)
     */
    List<WeekAttendance> findByCourse_Id(Long courseId);

    /**
     * 특정 주차의 모든 학생 출석 목록 조회 (교수용)
     */
    List<WeekAttendance> findByWeek_Id(Long weekId);

    /**
     * 학생의 특정 강의 출석 완료 주차 수 조회
     */
    int countCompletedByStudentAndCourse(Long studentId, Long courseId);

    /**
     * 특정 강의의 출석 완료 학생 수 조회 (주차별)
     */
    int countCompletedByWeek(Long weekId);

    /**
     * 특정 강의, 주차의 미완료 학생 목록 조회
     */
    List<WeekAttendance> findIncompleteByWeek(Long weekId);

    /**
     * 학생-강의별 출석 통계 조회
     */
    List<Object[]> getAttendanceStatsByCourse(Long courseId);

    /**
     * 학생 ID와 주차 ID 목록으로 출석 조회
     */
    List<WeekAttendance> findByStudentAndWeekIds(Long studentId, List<Long> weekIds);
}
