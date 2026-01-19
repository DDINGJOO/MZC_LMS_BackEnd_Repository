package com.mzc.backend.lms.integration.user;

import com.mzc.backend.lms.domains.enrollment.application.port.out.StudentPort;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.StudentRepository;
import com.mzc.backend.lms.views.UserViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * User → Enrollment 통합 Adapter
 *
 * Enrollment 도메인이 User 도메인의 학생 데이터에 접근할 때 사용
 * integration 패키지에 위치하여 도메인 간 순환 의존성 방지
 *
 * MSA 전환 시: HTTP Client로 교체
 */
@Component
@RequiredArgsConstructor
public class StudentAdapter implements StudentPort {

    private final StudentRepository studentRepository;
    private final UserViewService userViewService;

    @Override
    public StudentInfo getStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> EnrollmentException.studentNotFound(studentId));

        String name = userViewService.getUserName(student.getStudentId().toString());

        Long departmentId = null;
        String departmentName = null;
        if (student.getStudentDepartment() != null && student.getStudentDepartment().getDepartment() != null) {
            departmentId = student.getStudentDepartment().getDepartment().getId();
            departmentName = student.getStudentDepartment().getDepartment().getDepartmentName();
        }

        return new StudentInfo(
                student.getId(),
                student.getStudentId(),
                name != null ? name : "학생",
                departmentId,
                departmentName
        );
    }

    @Override
    public boolean existsById(Long studentId) {
        return studentRepository.existsById(studentId);
    }

    @Override
    public String getUserName(Long userId) {
        return userViewService.getUserName(userId.toString());
    }
}
