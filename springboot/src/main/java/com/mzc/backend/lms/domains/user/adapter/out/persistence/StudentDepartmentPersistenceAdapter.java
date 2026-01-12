package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.StudentDepartment;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.StudentDepartmentRepository;
import com.mzc.backend.lms.domains.user.application.port.out.StudentDepartmentQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * StudentDepartment 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class StudentDepartmentPersistenceAdapter implements StudentDepartmentQueryPort {

    private final StudentDepartmentRepository studentDepartmentRepository;

    @Override
    public Optional<StudentDepartment> findByStudentId(Long studentId) {
        return studentDepartmentRepository.findByStudentId(studentId);
    }
}
