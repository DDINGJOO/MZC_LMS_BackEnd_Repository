package com.mzc.lms.profile.application.port.in;

import com.mzc.lms.profile.domain.model.ProfessorProfile;
import com.mzc.lms.profile.domain.model.StudentProfile;
import com.mzc.lms.profile.domain.model.UserProfile;

import java.util.Optional;

public interface GetProfileUseCase {

    UserProfile getProfile(Long userId);

    Optional<StudentProfile> getStudentProfile(Long userId);

    Optional<ProfessorProfile> getProfessorProfile(Long userId);
}
