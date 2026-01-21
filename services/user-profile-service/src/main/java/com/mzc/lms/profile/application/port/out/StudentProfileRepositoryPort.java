package com.mzc.lms.profile.application.port.out;

import com.mzc.lms.profile.domain.model.StudentProfile;

import java.util.Optional;

public interface StudentProfileRepositoryPort {

    Optional<StudentProfile> findByUserId(Long userId);

    Optional<StudentProfile> findByStudentId(Long studentId);

    StudentProfile save(StudentProfile studentProfile);
}
