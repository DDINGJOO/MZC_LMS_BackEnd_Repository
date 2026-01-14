package com.mzc.backend.lms.domains.course.grade.adapter.in.web.dto;

import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.entity.Grade;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class StudentGradeResponseDto {
    private Long academicTermId;
    private Long courseId;
    private String courseName;
    private Integer courseCredits;
    private String status; // PUBLISHED

    private BigDecimal midtermScore;
    private BigDecimal finalExamScore;
    private BigDecimal quizScore;
    private BigDecimal assignmentScore;
    private BigDecimal attendanceScore;

    private BigDecimal finalScore;
    private String finalGrade;
    private LocalDateTime publishedAt;

    /**
     * Entity -> DTO 변환 (기본 정보만, courseName과 courseCredits는 별도 설정 필요)
     */
    public static StudentGradeResponseDto from(Grade grade) {
        return StudentGradeResponseDto.builder()
                .academicTermId(grade.getAcademicTermId())
                .courseId(grade.getCourseId())
                .courseName(null)  // 서비스 레이어에서 설정
                .courseCredits(null)  // 서비스 레이어에서 설정
                .status(grade.getStatus() != null ? grade.getStatus().name() : null)
                .midtermScore(grade.getMidtermScore())
                .finalExamScore(grade.getFinalExamScore())
                .quizScore(grade.getQuizScore())
                .assignmentScore(grade.getAssignmentScore())
                .attendanceScore(grade.getAttendanceScore())
                .finalScore(grade.getFinalScore())
                .finalGrade(grade.getFinalGrade())
                .publishedAt(grade.getPublishedAt())
                .build();
    }

    /**
     * Entity List -> DTO List 변환
     */
    public static List<StudentGradeResponseDto> from(List<Grade> grades) {
        return grades.stream()
                .map(StudentGradeResponseDto::from)
                .toList();
    }
}


