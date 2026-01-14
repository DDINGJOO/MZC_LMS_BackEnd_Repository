package com.mzc.backend.lms.domains.course.subject.adapter.in.web.dto;

import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 과목 간단 검색 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectSearchResponse {
    private Long id;
    private String subjectCode;
    private String subjectName;
    private Integer credits;
    private String courseType;  // 간단한 이름만
    private String department;  // 간단한 이름만

    /**
     * Entity -> DTO 변환
     */
    public static SubjectSearchResponse from(Subject subject) {
        return SubjectSearchResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .credits(subject.getCredits())
                .courseType(subject.getCourseType() != null ? subject.getCourseType().getTypeName() : null)
                .department(subject.getDepartment() != null ? subject.getDepartment().getDepartmentName() : null)
                .build();
    }

    /**
     * Entity List -> DTO List 변환
     */
    public static List<SubjectSearchResponse> from(List<Subject> subjects) {
        return subjects.stream()
                .map(SubjectSearchResponse::from)
                .toList();
    }
}

