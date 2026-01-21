package com.mzc.lms.course.adapter.out.persistence.entity;

import com.mzc.lms.course.domain.model.CourseSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "course_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "schedule_room", length = 50, nullable = false)
    private String room;

    public CourseSchedule toDomain() {
        return CourseSchedule.builder()
                .id(id)
                .courseId(courseId)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .room(room)
                .build();
    }

    public static CourseScheduleEntity fromDomain(CourseSchedule schedule) {
        return CourseScheduleEntity.builder()
                .id(schedule.getId())
                .courseId(schedule.getCourseId())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .room(schedule.getRoom())
                .build();
    }
}
