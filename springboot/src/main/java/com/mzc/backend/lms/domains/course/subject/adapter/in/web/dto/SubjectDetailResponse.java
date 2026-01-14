package com.mzc.backend.lms.domains.course.subject.adapter.in.web.dto;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 과목 상세 응답 DTO (개설 강좌 정보 포함)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDetailResponse {
    private Long id;
    private String subjectCode;
    private String subjectName;
    private String englishName;
    private Integer credits;
    private SubjectResponse.CourseTypeDto courseType;
    private SubjectResponse.DepartmentDto department;
    private String description;
    private List<String> objectives;
    private List<SubjectResponse.PrerequisiteDto> prerequisites;
    private List<CourseInfoDto> courses;  // 개설된 강좌들
    private Boolean isActive;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseInfoDto {
        private Long id;
        private String section;
        private ProfessorDto professor;
        private TermDto term;
        private Integer currentStudents;
        private Integer maxStudents;

        /**
         * Entity -> DTO 변환
         */
        public static CourseInfoDto from(Course course) {
            if (course == null) {
                return null;
            }
            return CourseInfoDto.builder()
                    .id(course.getId())
                    .section(course.getSectionNumber())
                    .professor(course.getProfessor() != null ? ProfessorDto.from(course.getProfessor()) : null)
                    .term(course.getAcademicTerm() != null ? TermDto.from(course.getAcademicTerm()) : null)
                    .currentStudents(course.getCurrentStudents())
                    .maxStudents(course.getMaxStudents())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfessorDto {
        private Long id;
        private String name;

        /**
         * Entity -> DTO 변환
         */
        public static ProfessorDto from(com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor professor) {
            if (professor == null) {
                return null;
            }
            return ProfessorDto.builder()
                    .id(professor.getId())
                    .name(professor.getUser() != null && professor.getUser().getUserProfile() != null ?
                          professor.getUser().getUserProfile().getName() : null)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermDto {
        private Integer year;
        private String termType;

        /**
         * Entity -> DTO 변환
         */
        public static TermDto from(com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm term) {
            if (term == null) {
                return null;
            }
            return TermDto.builder()
                    .year(term.getYear())
                    .termType(term.getTermType())
                    .build();
        }
    }

    /**
     * Entity -> DTO 변환
     */
    public static SubjectDetailResponse from(Subject subject) {
        return SubjectDetailResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .englishName(null)  // 영문명은 아직 DB에 없음
                .credits(subject.getCredits())
                .courseType(SubjectResponse.CourseTypeDto.from(subject.getCourseType()))
                .department(SubjectResponse.DepartmentDto.from(subject.getDepartment()))
                .description(subject.getDescription())
                .objectives(List.of())  // objectives는 별도 처리 필요
                .prerequisites(subject.getPrerequisites() != null ?
                        subject.getPrerequisites().stream()
                                .map(SubjectResponse.PrerequisiteDto::from)
                                .toList() : List.of())
                .courses(subject.getCourses() != null ?
                        subject.getCourses().stream()
                                .map(CourseInfoDto::from)
                                .toList() : List.of())
                .isActive(true)  // 기본값
                .createdAt(subject.getCreatedAt())
                .build();
    }

    /**
     * Entity List -> DTO List 변환
     */
    public static List<SubjectDetailResponse> from(List<Subject> subjects) {
        return subjects.stream()
                .map(SubjectDetailResponse::from)
                .toList();
    }
}

