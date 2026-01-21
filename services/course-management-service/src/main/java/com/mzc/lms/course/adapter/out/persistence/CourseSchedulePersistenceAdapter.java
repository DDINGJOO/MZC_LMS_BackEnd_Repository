package com.mzc.lms.course.adapter.out.persistence;

import com.mzc.lms.course.adapter.out.persistence.entity.CourseScheduleEntity;
import com.mzc.lms.course.adapter.out.persistence.repository.CourseScheduleJpaRepository;
import com.mzc.lms.course.application.port.out.CourseScheduleRepositoryPort;
import com.mzc.lms.course.domain.model.CourseSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseSchedulePersistenceAdapter implements CourseScheduleRepositoryPort {

    private final CourseScheduleJpaRepository scheduleJpaRepository;

    @Override
    public CourseSchedule save(CourseSchedule schedule) {
        CourseScheduleEntity entity = CourseScheduleEntity.fromDomain(schedule);
        CourseScheduleEntity saved = scheduleJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<CourseSchedule> findById(Long id) {
        return scheduleJpaRepository.findById(id)
                .map(CourseScheduleEntity::toDomain);
    }

    @Override
    public List<CourseSchedule> findByCourseId(Long courseId) {
        return scheduleJpaRepository.findByCourseId(courseId).stream()
                .map(CourseScheduleEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseSchedule> findByRoom(String room) {
        return scheduleJpaRepository.findByRoom(room).stream()
                .map(CourseScheduleEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseSchedule> findByDayOfWeekAndTimeBetween(
            DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return scheduleJpaRepository.findByDayOfWeekAndTimeBetween(dayOfWeek, startTime).stream()
                .map(CourseScheduleEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseSchedule> findConflictingSchedules(
            Long excludeCourseId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, String room) {
        return scheduleJpaRepository.findConflictingSchedules(excludeCourseId, dayOfWeek, startTime, endTime, room)
                .stream()
                .map(CourseScheduleEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        scheduleJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByCourseId(Long courseId) {
        scheduleJpaRepository.deleteByCourseId(courseId);
    }

    @Override
    public boolean existsById(Long id) {
        return scheduleJpaRepository.existsById(id);
    }
}
