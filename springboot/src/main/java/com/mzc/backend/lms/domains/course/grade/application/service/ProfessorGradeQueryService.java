package com.mzc.backend.lms.domains.course.grade.application.service;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.exception.CourseException;
import com.mzc.backend.lms.domains.course.grade.application.port.in.ProfessorGradeQueryUseCase;
import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeCoursePort;
import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeEnrollmentPort;
import com.mzc.backend.lms.domains.course.grade.application.port.out.GradeRepositoryPort;
import com.mzc.backend.lms.domains.course.grade.application.port.out.UserViewPort;
import com.mzc.backend.lms.domains.course.grade.adapter.in.web.dto.ProfessorCourseGradesResponseDto;
import com.mzc.backend.lms.domains.course.grade.adapter.out.persistence.entity.Grade;
import com.mzc.backend.lms.domains.course.grade.domain.enums.GradeStatus;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 교수 성적 조회 UseCase 구현체
 */
@Service
@RequiredArgsConstructor
public class ProfessorGradeQueryService implements ProfessorGradeQueryUseCase {

    // External Ports
    private final GradeCoursePort coursePort;
    private final GradeEnrollmentPort enrollmentPort;
    private final UserViewPort userViewPort;

    // Persistence Ports
    private final GradeRepositoryPort gradeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProfessorCourseGradesResponseDto> listCourseGrades(Long courseId, Long professorId, GradeQueryStatus status) {
        Objects.requireNonNull(courseId, "courseId");
        Objects.requireNonNull(professorId, "professorId");

        Course course = coursePort.findById(courseId)
                .orElseThrow(() -> CourseException.courseNotFound(courseId));
        if (course.getProfessor() == null || course.getProfessor().getProfessorId() == null
                || !course.getProfessor().getProfessorId().equals(professorId)) {
            throw CourseException.gradeViewNotAuthorized();
        }

        String courseName = (course.getSubject() != null) ? course.getSubject().getSubjectName() : null;
        Long academicTermId = (course.getAcademicTerm() != null) ? course.getAcademicTerm().getId() : null;

        List<Enrollment> enrollments = enrollmentPort.findByCourseIdWithStudent(courseId);
        List<Long> studentIds = enrollments.stream().map(e -> e.getStudent().getStudentId()).distinct().toList();

        Map<Long, Grade> gradeMap = new HashMap<>();
        if (!studentIds.isEmpty()) {
            List<Grade> grades = (status == GradeQueryStatus.PUBLISHED)
                    ? gradeRepository.findByCourseIdAndStudentIdInAndStatus(courseId, studentIds, GradeStatus.PUBLISHED)
                    : gradeRepository.findByCourseIdAndStudentIdIn(courseId, studentIds);
            for (Grade g : grades) {
                gradeMap.put(g.getStudentId(), g);
            }
        }

        // 이름 복호화(배치)
        final Map<Long, String> nameMap = studentIds.isEmpty()
                ? new HashMap<>()
                : userViewPort.getUserNames(studentIds);

        return enrollments.stream()
                .map(e -> {
                    Long sid = e.getStudent().getStudentId();
                    Grade g = gradeMap.get(sid);
                    return ProfessorCourseGradesResponseDto.builder()
                            .courseId(courseId)
                            .academicTermId(academicTermId)
                            .courseName(courseName)
                            .student(ProfessorCourseGradesResponseDto.StudentDto.builder()
                                    .id(sid)
                                    .studentNumber(e.getStudent().getStudentNumber())
                                    .name(nameMap.get(sid))
                                    .build())
                            .midtermScore(g != null ? g.getMidtermScore() : null)
                            .finalExamScore(g != null ? g.getFinalExamScore() : null)
                            .quizScore(g != null ? g.getQuizScore() : null)
                            .assignmentScore(g != null ? g.getAssignmentScore() : null)
                            .attendanceScore(g != null ? g.getAttendanceScore() : null)
                            .finalScore(g != null ? g.getFinalScore() : null)
                            .finalGrade(g != null ? g.getFinalGrade() : null)
                            .status(g != null && g.getStatus() != null ? g.getStatus().name() : GradeStatus.PENDING.name())
                            .gradedAt(g != null ? g.getGradedAt() : null)
                            .publishedAt(g != null ? g.getPublishedAt() : null)
                            .build();
                })
                .toList();
    }
}


