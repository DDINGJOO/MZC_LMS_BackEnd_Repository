package com.mzc.lms.course.adapter.in.web.dto;

import com.mzc.lms.course.domain.model.Course;
import com.mzc.lms.course.domain.model.CourseSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CourseResponse {

    private Long id;
    private Long subjectId;
    private Long professorId;
    private Long academicTermId;
    private String sectionNumber;
    private Integer maxStudents;
    private Integer currentStudents;
    private Integer availableSeats;
    private String description;
    private Boolean isActive;
    private List<ScheduleResponse> schedules;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CourseResponse from(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .subjectId(course.getSubjectId())
                .professorId(course.getProfessorId())
                .academicTermId(course.getAcademicTermId())
                .sectionNumber(course.getSectionNumber())
                .maxStudents(course.getMaxStudents())
                .currentStudents(course.getCurrentStudents())
                .availableSeats(course.getAvailableSeats())
                .description(course.getDescription())
                .isActive(course.getIsActive())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }

    public static CourseResponse from(Course course, List<CourseSchedule> schedules) {
        return CourseResponse.builder()
                .id(course.getId())
                .subjectId(course.getSubjectId())
                .professorId(course.getProfessorId())
                .academicTermId(course.getAcademicTermId())
                .sectionNumber(course.getSectionNumber())
                .maxStudents(course.getMaxStudents())
                .currentStudents(course.getCurrentStudents())
                .availableSeats(course.getAvailableSeats())
                .description(course.getDescription())
                .isActive(course.getIsActive())
                .schedules(schedules.stream().map(ScheduleResponse::from).toList())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
