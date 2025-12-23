package com.mzc.backend.lms.domains.course.course.entity;

import com.mzc.backend.lms.domains.course.course.converter.DayOfWeekConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/*
  강의 스케줄 엔티티
  course_schedules 테이블과 매핑
*/

@Entity
@Setter
@Table(name = "course_schedules")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private Long scheduleId; // 시간표 식별자
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;
	
	@Column(name = "day_of_week", nullable = false)
	@Convert(converter = DayOfWeekConverter.class)
	private DayOfWeek dayOfWeek;
	
	@Column(name = "start_time", nullable = false, columnDefinition = "TIME")
	private LocalTime startTime; // 시작 시간 (예: 09:00)
	
	@Column(name = "end_time", nullable = false, columnDefinition = "TIME")
	private LocalTime endTime; // 종료 시간 (예: 10:30)
	
	@Column(name = "schedule_room", length = 50, nullable = false)
	private String scheduleRoom; // 강의실
}
