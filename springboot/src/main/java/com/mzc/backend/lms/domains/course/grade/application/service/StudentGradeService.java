package com.mzc.backend.lms.domains.course.grade.application.service;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.grade.application.port.in.StudentGradeUseCase;
import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeCoursePort;
import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeRepositoryPort;
import com.mzc.backend.lms.domains.course.grade.adapter.in.web.dto.StudentGradeResponseDto;
import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.entity.Grade;
import com.mzc.backend.lms.domains.course.grade.domain.enums.GradeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 학생 성적 조회 UseCase 구현체
 */
@Service
@RequiredArgsConstructor
public class StudentGradeService implements StudentGradeUseCase {

    // Persistence Ports
    private final GradeRepositoryPort gradeRepository;

    // External Ports
    private final GradeCoursePort coursePort;

    @Override
    @Transactional(readOnly = true)
    public List<StudentGradeResponseDto> listPublishedGrades(Long studentId, Long academicTermId) {
        Objects.requireNonNull(studentId, "studentId");

        List<Grade> grades = (academicTermId == null)
                ? gradeRepository.findByStudentIdAndStatusOrderByAcademicTermIdDescCourseIdAsc(studentId, GradeStatus.PUBLISHED)
                : gradeRepository.findByStudentIdAndAcademicTermIdAndStatusOrderByCourseIdAsc(studentId, academicTermId, GradeStatus.PUBLISHED);

        List<Long> courseIds = grades.stream().map(Grade::getCourseId).distinct().toList();
        Map<Long, Course> courseMap = new HashMap<>();
        if (!courseIds.isEmpty()) {
            for (Course c : coursePort.findByIdInWithSubject(courseIds)) {
                courseMap.put(c.getId(), c);
            }
        }

        return grades.stream()
                .map(g -> {
                    Course c = courseMap.get(g.getCourseId());
                    String courseName = (c != null && c.getSubject() != null) ? c.getSubject().getSubjectName() : null;
                    Integer courseCredits = (c != null && c.getSubject() != null) ? c.getSubject().getCredits() : null;
                    return StudentGradeResponseDto.builder()
                            .academicTermId(g.getAcademicTermId())
                            .courseId(g.getCourseId())
                            .courseName(courseName)
                            .courseCredits(courseCredits)
                            .status(g.getStatus() != null ? g.getStatus().name() : null)
                            .midtermScore(g.getMidtermScore())
                            .finalExamScore(g.getFinalExamScore())
                            .quizScore(g.getQuizScore())
                            .assignmentScore(g.getAssignmentScore())
                            .attendanceScore(g.getAttendanceScore())
                            .finalScore(g.getFinalScore())
                            .finalGrade(g.getFinalGrade())
                            .publishedAt(g.getPublishedAt())
                            .build();
                })
                .toList();
    }
}


