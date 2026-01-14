package com.mzc.backend.lms.domains.attendance.adapter.out.persistence;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.WeekAttendance;
import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.repository.WeekAttendanceJpaRepository;
import com.mzc.backend.lms.domains.attendance.application.port.out.WeekAttendanceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * WeekAttendance 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class WeekAttendancePersistenceAdapter implements WeekAttendanceRepositoryPort {

    private final WeekAttendanceJpaRepository weekAttendanceJpaRepository;

    @Override
    public WeekAttendance save(WeekAttendance weekAttendance) {
        return weekAttendanceJpaRepository.save(weekAttendance);
    }

    @Override
    public Optional<WeekAttendance> findById(Long id) {
        return weekAttendanceJpaRepository.findById(id);
    }

    @Override
    public Optional<WeekAttendance> findByStudentStudentIdAndWeek_Id(Long studentId, Long weekId) {
        return weekAttendanceJpaRepository.findByStudentStudentIdAndWeek_Id(studentId, weekId);
    }

    @Override
    public List<WeekAttendance> findByStudentStudentIdAndCourse_Id(Long studentId, Long courseId) {
        return weekAttendanceJpaRepository.findByStudentStudentIdAndCourse_Id(studentId, courseId);
    }

    @Override
    public List<WeekAttendance> findByStudentStudentId(Long studentId) {
        return weekAttendanceJpaRepository.findByStudentStudentId(studentId);
    }

    @Override
    public List<WeekAttendance> findByCourse_Id(Long courseId) {
        return weekAttendanceJpaRepository.findByCourse_Id(courseId);
    }

    @Override
    public List<WeekAttendance> findByWeek_Id(Long weekId) {
        return weekAttendanceJpaRepository.findByWeek_Id(weekId);
    }

    @Override
    public int countCompletedByStudentAndCourse(Long studentId, Long courseId) {
        return weekAttendanceJpaRepository.countCompletedByStudentAndCourse(studentId, courseId);
    }

    @Override
    public int countCompletedByWeek(Long weekId) {
        return weekAttendanceJpaRepository.countCompletedByWeek(weekId);
    }

    @Override
    public List<WeekAttendance> findIncompleteByWeek(Long weekId) {
        return weekAttendanceJpaRepository.findIncompleteByWeek(weekId);
    }

    @Override
    public List<Object[]> getAttendanceStatsByCourse(Long courseId) {
        return weekAttendanceJpaRepository.getAttendanceStatsByCourse(courseId);
    }

    @Override
    public List<WeekAttendance> findByStudentAndWeekIds(Long studentId, List<Long> weekIds) {
        return weekAttendanceJpaRepository.findByStudentAndWeekIds(studentId, weekIds);
    }
}
