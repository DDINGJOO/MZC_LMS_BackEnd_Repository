package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.StudentRepository;
import com.mzc.backend.lms.domains.user.application.port.out.StudentQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Student 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class StudentPersistenceAdapter implements StudentQueryPort {

    private final StudentRepository studentRepository;

    @Override
    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public boolean existsById(Long studentId) {
        return studentRepository.existsById(studentId);
    }

    @Override
    public List<Student> findAllById(List<Long> studentIds) {
        return studentRepository.findAllById(studentIds);
    }
}
