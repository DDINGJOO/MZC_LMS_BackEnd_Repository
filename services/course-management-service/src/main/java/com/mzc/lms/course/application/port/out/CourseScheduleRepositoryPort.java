package com.mzc.lms.course.application.port.out;

import com.mzc.lms.course.domain.model.CourseSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CourseScheduleRepositoryPort {

    CourseSchedule save(CourseSchedule schedule);

    Optional<CourseSchedule> findById(Long id);

    List<CourseSchedule> findByCourseId(Long courseId);

    List<CourseSchedule> findByRoom(String room);

    List<CourseSchedule> findByDayOfWeekAndTimeBetween(
            DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime);

    List<CourseSchedule> findConflictingSchedules(
            Long excludeCourseId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, String room);

    void deleteById(Long id);

    void deleteByCourseId(Long courseId);

    boolean existsById(Long id);
}
