package com.mzc.backend.lms.domains.enrollment.adapter.out.external;

import com.mzc.backend.lms.domains.enrollment.application.port.out.StudentPort;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import com.mzc.backend.lms.domains.user.student.entity.Student;
import com.mzc.backend.lms.domains.user.student.repository.StudentRepository;
import com.mzc.backend.lms.views.UserViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Student(User) 도메인 Adapter
 * 현재: Repository 직접 호출 + UserViewService
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
