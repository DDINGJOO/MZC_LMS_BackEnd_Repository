package com.mzc.backend.lms.domains.attendance.adapter.out.persistence;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.mapper.WeekAttendanceMapper;
import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.repository.WeekAttendanceJpaRepository;
import com.mzc.backend.lms.domains.attendance.application.port.out.WeekAttendanceRepositoryPort;
import com.mzc.backend.lms.domains.attendance.domain.model.WeekAttendanceDomain;
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
    public WeekAttendanceDomain save(WeekAttendanceDomain weekAttendance) {
        throw new UnsupportedOperationException("WeekAttendance save operation requires Entity conversion - not yet implemented");
    }

    @Override
    public Optional<WeekAttendanceDomain> findById(Long id) {
        return weekAttendanceJpaRepository.findById(id)
                .map(WeekAttendanceMapper::toDomain);
    }

    @Override
    public Optional<WeekAttendanceDomain> findByStudentIdAndWeekId(Long studentId, Long weekId) {
        return weekAttendanceJpaRepository.findByStudentStudentIdAndWeek_Id(studentId, weekId)
                .map(WeekAttendanceMapper::toDomain);
    }

    @Override
    public List<WeekAttendanceDomain> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return weekAttendanceJpaRepository.findByStudentStudentIdAndCourse_Id(studentId, courseId).stream()
                .map(WeekAttendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<WeekAttendanceDomain> findByStudentId(Long studentId) {
        return weekAttendanceJpaRepository.findByStudentStudentId(studentId).stream()
                .map(WeekAttendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<WeekAttendanceDomain> findByCourseId(Long courseId) {
        return weekAttendanceJpaRepository.findByCourse_Id(courseId).stream()
                .map(WeekAttendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<WeekAttendanceDomain> findByWeekId(Long weekId) {
        return weekAttendanceJpaRepository.findByWeek_Id(weekId).stream()
                .map(WeekAttendanceMapper::toDomain)
                .toList();
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
    public List<WeekAttendanceDomain> findIncompleteByWeek(Long weekId) {
        return weekAttendanceJpaRepository.findIncompleteByWeek(weekId).stream()
                .map(WeekAttendanceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Object[]> getAttendanceStatsByCourse(Long courseId) {
        return weekAttendanceJpaRepository.getAttendanceStatsByCourse(courseId);
    }

    @Override
    public List<WeekAttendanceDomain> findByStudentAndWeekIds(Long studentId, List<Long> weekIds) {
        return weekAttendanceJpaRepository.findByStudentAndWeekIds(studentId, weekIds).stream()
                .map(WeekAttendanceMapper::toDomain)
                .toList();
    }
}
