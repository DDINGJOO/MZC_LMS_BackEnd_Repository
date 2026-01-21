package com.mzc.lms.course.adapter.in.web.dto;

import com.mzc.lms.course.domain.model.CourseSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
public class ScheduleResponse {

    private Long id;
    private Long courseId;
    private DayOfWeek dayOfWeek;
    private String dayOfWeekKorean;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private String timeSlot;
    private Integer durationMinutes;

    public static ScheduleResponse from(CourseSchedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .courseId(schedule.getCourseId())
                .dayOfWeek(schedule.getDayOfWeek())
                .dayOfWeekKorean(schedule.getDayOfWeekKorean())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .room(schedule.getRoom())
                .timeSlot(schedule.getTimeSlot())
                .durationMinutes(schedule.getDurationMinutes())
                .build();
    }
}
