package com.mzc.backend.lms.domains.course.grade.adapter.in.web.dto;

import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.entity.Grade;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProfessorCourseGradesResponseDto {
    private Long courseId;
    private Long academicTermId;
    private String courseName;

    private StudentDto student;

    private BigDecimal midtermScore;
    private BigDecimal finalExamScore;
    private BigDecimal quizScore;
    private BigDecimal assignmentScore;
    private BigDecimal attendanceScore;
    private BigDecimal finalScore;
    private String finalGrade;
    private String status; // PENDING/GRADED/PUBLISHED
    private LocalDateTime gradedAt;
    private LocalDateTime publishedAt;

    @Getter
    @Builder
    public static class StudentDto {
        private Long id;
        private Long studentNumber;
        private String name;

        /**
         * Entity -> DTO 변환
         */
        public static StudentDto from(Student student) {
            if (student == null) {
                return null;
            }
            return StudentDto.builder()
                    .id(student.getId())
                    .studentNumber(student.getStudentNumber())
                    .name(student.getUser() != null && student.getUser().getUserProfile() != null ?
                          student.getUser().getUserProfile().getName() : null)
                    .build();
        }
    }

    /**
     * Entity -> DTO 변환 (기본 정보만, courseName과 student는 별도 설정 필요)
     */
    public static ProfessorCourseGradesResponseDto from(Grade grade) {
        return ProfessorCourseGradesResponseDto.builder()
                .courseId(grade.getCourseId())
                .academicTermId(grade.getAcademicTermId())
                .courseName(null)  // 서비스 레이어에서 설정
                .student(null)  // 서비스 레이어에서 설정
                .midtermScore(grade.getMidtermScore())
                .finalExamScore(grade.getFinalExamScore())
                .quizScore(grade.getQuizScore())
                .assignmentScore(grade.getAssignmentScore())
                .attendanceScore(grade.getAttendanceScore())
                .finalScore(grade.getFinalScore())
                .finalGrade(grade.getFinalGrade())
                .status(grade.getStatus() != null ? grade.getStatus().name() : null)
                .gradedAt(grade.getGradedAt())
                .publishedAt(grade.getPublishedAt())
                .build();
    }

    /**
     * Entity List -> DTO List 변환
     */
    public static List<ProfessorCourseGradesResponseDto> from(List<Grade> grades) {
        return grades.stream()
                .map(ProfessorCourseGradesResponseDto::from)
                .toList();
    }
}


