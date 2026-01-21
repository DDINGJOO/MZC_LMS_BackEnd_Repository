package com.mzc.lms.course.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
public class CourseSchedule {

    private Long id;
    private Long courseId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;

    public static CourseSchedule create(
            Long courseId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String room
    ) {
        validateTimeRange(startTime, endTime);

        return CourseSchedule.builder()
                .courseId(courseId)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .room(room)
                .build();
    }

    public CourseSchedule update(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String room
    ) {
        if (startTime != null && endTime != null) {
            validateTimeRange(startTime, endTime);
        }

        return CourseSchedule.builder()
                .id(this.id)
                .courseId(this.courseId)
                .dayOfWeek(dayOfWeek != null ? dayOfWeek : this.dayOfWeek)
                .startTime(startTime != null ? startTime : this.startTime)
                .endTime(endTime != null ? endTime : this.endTime)
                .room(room != null ? room : this.room)
                .build();
    }

    private static void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    public boolean conflictsWith(CourseSchedule other) {
        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }

        return !(this.endTime.isBefore(other.startTime) ||
                 this.endTime.equals(other.startTime) ||
                 this.startTime.isAfter(other.endTime) ||
                 this.startTime.equals(other.endTime));
    }

    public int getDurationMinutes() {
        return (int) java.time.Duration.between(startTime, endTime).toMinutes();
    }

    public String getTimeSlot() {
        return String.format("%s %s-%s",
                getDayOfWeekKorean(),
                startTime.toString(),
                endTime.toString());
    }

    public String getDayOfWeekKorean() {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }
}
