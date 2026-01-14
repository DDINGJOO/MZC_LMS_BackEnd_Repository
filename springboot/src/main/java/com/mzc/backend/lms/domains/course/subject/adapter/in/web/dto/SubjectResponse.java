package com.mzc.backend.lms.domains.course.subject.adapter.in.web.dto;

import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.SubjectPrerequisites;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 과목 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    private Long id;
    private String subjectCode;
    private String subjectName;
    private String englishName;  // 영문명은 아직 DB에 없지만 향후 추가 가능
    private Integer credits;
    private CourseTypeDto courseType;
    private DepartmentDto department;
    private String description;
    private List<PrerequisiteDto> prerequisites;
    private Integer currentTermSections;  // 현재 학기 개설 분반 수
    private Boolean isActive;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseTypeDto {
        private Long id;
        private String code;
        private String name;
        private String color;

        /**
         * Entity -> DTO 변환
         */
        public static CourseTypeDto from(com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseType courseType) {
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentDto {
        private Long id;
        private String name;
        private String college;

        /**
         * Entity -> DTO 변환
         */
        public static DepartmentDto from(com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Department department) {
            if (department == null) {
                return null;
            }
            return DepartmentDto.builder()
                    .id(department.getId())
                    .name(department.getDepartmentName())
                    .college(department.getCollege() != null ? department.getCollege().getCollegeName() : null)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrerequisiteDto {
        private Long id;
        private String subjectCode;
        private String subjectName;
        private Integer credits;
        private Boolean isMandatory;

        /**
         * Entity -> DTO 변환
         */
        public static PrerequisiteDto from(SubjectPrerequisites prerequisiteRelation) {
            if (prerequisiteRelation == null || prerequisiteRelation.getPrerequisite() == null) {
                return null;
            }
            Subject prereq = prerequisiteRelation.getPrerequisite();
            return PrerequisiteDto.builder()
                    .id(prereq.getId())
                    .subjectCode(prereq.getSubjectCode())
                    .subjectName(prereq.getSubjectName())
                    .credits(prereq.getCredits())
                    .isMandatory(prerequisiteRelation.getIsMandatory())
                    .build();
        }
    }

    /**
     * Entity -> DTO 변환
     */
    public static SubjectResponse from(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .englishName(null)  // 영문명은 아직 DB에 없음
                .credits(subject.getCredits())
                .courseType(CourseTypeDto.from(subject.getCourseType()))
                .department(DepartmentDto.from(subject.getDepartment()))
                .description(subject.getDescription())
                .prerequisites(subject.getPrerequisites() != null ?
                        subject.getPrerequisites().stream()
                                .map(PrerequisiteDto::from)
                                .toList() : List.of())
                .currentTermSections(null)  // 이 값은 서비스 레이어에서 계산 필요
                .isActive(true)  // 기본값, 필요시 서비스 레이어에서 설정
                .createdAt(subject.getCreatedAt())
                .build();
    }

    /**
     * Entity List -> DTO List 변환
     */
    public static List<SubjectResponse> from(List<Subject> subjects) {
        return subjects.stream()
                .map(SubjectResponse::from)
                .toList();
    }
}

