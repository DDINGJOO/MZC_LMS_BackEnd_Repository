package com.mzc.lms.course.application.service;

import com.mzc.lms.course.application.port.in.CourseScheduleUseCase;
import com.mzc.lms.course.application.port.out.CourseRepositoryPort;
import com.mzc.lms.course.application.port.out.CourseScheduleRepositoryPort;
import com.mzc.lms.course.application.port.out.EventPublisherPort;
import com.mzc.lms.course.domain.event.CourseEvent;
import com.mzc.lms.course.domain.model.CourseSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseScheduleService implements CourseScheduleUseCase {

    private static final String CACHE_SCHEDULES = "schedules";

    private final CourseScheduleRepositoryPort scheduleRepository;
    private final CourseRepositoryPort courseRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = CACHE_SCHEDULES, allEntries = true)
    public CourseSchedule addSchedule(Long courseId, AddScheduleCommand command) {
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course not found: " + courseId);
        }

        List<CourseSchedule> conflicts = scheduleRepository.findConflictingSchedules(
                courseId, command.dayOfWeek(), command.startTime(), command.endTime(), command.room());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Schedule conflict detected with room: " + command.room());
        }

        CourseSchedule schedule = CourseSchedule.create(
                courseId,
                command.dayOfWeek(),
                command.startTime(),
                command.endTime(),
                command.room()
        );

        CourseSchedule saved = scheduleRepository.save(schedule);
        eventPublisher.publish(CourseEvent.scheduleAdded(courseId, saved.getId()));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_SCHEDULES, allEntries = true)
    public CourseSchedule updateSchedule(Long scheduleId, UpdateScheduleCommand command) {
        CourseSchedule existing = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));

        DayOfWeek newDay = command.dayOfWeek() != null ? command.dayOfWeek() : existing.getDayOfWeek();
        LocalTime newStart = command.startTime() != null ? command.startTime() : existing.getStartTime();
        LocalTime newEnd = command.endTime() != null ? command.endTime() : existing.getEndTime();
        String newRoom = command.room() != null ? command.room() : existing.getRoom();

        List<CourseSchedule> conflicts = scheduleRepository.findConflictingSchedules(
                existing.getCourseId(), newDay, newStart, newEnd, newRoom);

        conflicts = conflicts.stream()
                .filter(s -> !s.getId().equals(scheduleId))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Schedule conflict detected");
        }

        CourseSchedule updated = existing.update(
                command.dayOfWeek(),
                command.startTime(),
                command.endTime(),
                command.room()
        );

        CourseSchedule saved = scheduleRepository.save(updated);
        eventPublisher.publish(CourseEvent.scheduleUpdated(saved.getId(), saved));

        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_SCHEDULES, allEntries = true)
    public void removeSchedule(Long scheduleId) {
        CourseSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));

        scheduleRepository.deleteById(scheduleId);
        eventPublisher.publish(CourseEvent.scheduleRemoved(schedule.getCourseId(), scheduleId));
    }

    @Override
    @Cacheable(value = CACHE_SCHEDULES, key = "#scheduleId")
    public Optional<CourseSchedule> getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    @Override
    @Cacheable(value = CACHE_SCHEDULES, key = "'course:' + #courseId")
    public List<CourseSchedule> getSchedulesByCourse(Long courseId) {
        return scheduleRepository.findByCourseId(courseId);
    }

    @Override
    @Cacheable(value = CACHE_SCHEDULES, key = "'room:' + #room")
    public List<CourseSchedule> getSchedulesByRoom(String room) {
        return scheduleRepository.findByRoom(room);
    }

    @Override
    public List<CourseSchedule> getSchedulesByDayAndTime(DayOfWeek dayOfWeek, LocalTime time) {
        return scheduleRepository.findByDayOfWeekAndTimeBetween(dayOfWeek, time, time);
    }

    @Override
    public boolean hasScheduleConflict(Long courseId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        List<CourseSchedule> existingSchedules = scheduleRepository.findByCourseId(courseId);
        CourseSchedule newSchedule = CourseSchedule.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        return existingSchedules.stream()
                .anyMatch(existing -> existing.conflictsWith(newSchedule));
    }
}
