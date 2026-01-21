package com.mzc.lms.organization.application.port.in;

import com.mzc.lms.organization.adapter.in.web.dto.AcademicTermCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.AcademicTermUpdateRequest;
import com.mzc.lms.organization.domain.model.AcademicTerm;

import java.util.List;
import java.util.Optional;

public interface AcademicTermUseCase {

    AcademicTerm createAcademicTerm(AcademicTermCreateRequest request);

    AcademicTerm updateAcademicTerm(Long id, AcademicTermUpdateRequest request);

    void deleteAcademicTerm(Long id);

    AcademicTerm getAcademicTerm(Long id);

    Optional<AcademicTerm> getCurrentTerm();

    List<AcademicTerm> getAllTerms();

    List<AcademicTerm> getTermsByYear(Integer year);
}
