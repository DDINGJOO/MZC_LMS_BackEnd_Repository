package com.mzc.lms.catalog.adapter.in.web.dto;

import com.mzc.lms.catalog.domain.model.Subject;
import com.mzc.lms.catalog.domain.model.CourseType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SubjectResponse {

    private Long id;
    private String subjectCode;
    private String subjectName;
    private String subjectDescription;
    private Long departmentId;
    private CourseTypeDto courseType;
    private Integer credits;
    private Integer theoryHours;
    private Integer practiceHours;
    private String description;
    private Boolean isActive;
    private List<PrerequisiteDto> prerequisites;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class CourseTypeDto {
        private Long id;
        private String code;
        private String name;
        private String color;

        public static CourseTypeDto from(CourseType courseType) {
            if (courseType == null) {
                return null;
            }
            return CourseTypeDto.builder()
                    .id(courseType.getId())
                    .code(courseType.getTypeCodeString())
                    .name(courseType.getTypeName())
                    .color(courseType.getColor())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PrerequisiteDto {
        private Long id;
        private String subjectCode;
        private String subjectName;
        private Integer credits;
        private Boolean isMandatory;
    }

    public static SubjectResponse from(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .subjectDescription(subject.getSubjectDescription())
                .departmentId(subject.getDepartmentId())
                .credits(subject.getCredits())
                .theoryHours(subject.getTheoryHours())
                .practiceHours(subject.getPracticeHours())
                .description(subject.getDescription())
                .isActive(subject.getIsActive())
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }

    public static SubjectResponse from(Subject subject, CourseType courseType, List<PrerequisiteDto> prerequisites) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .subjectDescription(subject.getSubjectDescription())
                .departmentId(subject.getDepartmentId())
                .courseType(CourseTypeDto.from(courseType))
                .credits(subject.getCredits())
                .theoryHours(subject.getTheoryHours())
                .practiceHours(subject.getPracticeHours())
                .description(subject.getDescription())
                .isActive(subject.getIsActive())
                .prerequisites(prerequisites)
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }
}
