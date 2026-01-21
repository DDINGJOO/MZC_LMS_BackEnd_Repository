package com.mzc.lms.course.application.port.in;

import com.mzc.lms.course.domain.model.CourseSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CourseScheduleUseCase {

    CourseSchedule addSchedule(Long courseId, AddScheduleCommand command);

    CourseSchedule updateSchedule(Long scheduleId, UpdateScheduleCommand command);

    void removeSchedule(Long scheduleId);

    Optional<CourseSchedule> getSchedule(Long scheduleId);

    List<CourseSchedule> getSchedulesByCourse(Long courseId);

    List<CourseSchedule> getSchedulesByRoom(String room);

    List<CourseSchedule> getSchedulesByDayAndTime(DayOfWeek dayOfWeek, LocalTime time);

    boolean hasScheduleConflict(Long courseId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime);

    record AddScheduleCommand(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String room
    ) {}

    record UpdateScheduleCommand(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String room
    ) {}
}
