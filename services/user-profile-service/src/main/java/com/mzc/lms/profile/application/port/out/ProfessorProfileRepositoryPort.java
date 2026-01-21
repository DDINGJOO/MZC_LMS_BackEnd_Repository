package com.mzc.lms.profile.application.port.out;

import com.mzc.lms.profile.domain.model.ProfessorProfile;

import java.util.Optional;

public interface ProfessorProfileRepositoryPort {

    Optional<ProfessorProfile> findByUserId(Long userId);

    Optional<ProfessorProfile> findByProfessorId(Long professorId);

    ProfessorProfile save(ProfessorProfile professorProfile);
}
