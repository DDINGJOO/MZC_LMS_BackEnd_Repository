package com.mzc.lms.organization.application.port.in;

import com.mzc.lms.organization.adapter.in.web.dto.CollegeCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.CollegeUpdateRequest;
import com.mzc.lms.organization.domain.model.College;

import java.util.List;

public interface CollegeUseCase {

    College createCollege(CollegeCreateRequest request);

    College updateCollege(Long id, CollegeUpdateRequest request);

    void deleteCollege(Long id);

    College getCollege(Long id);

    College getCollegeByCode(String code);

    List<College> getAllColleges();
}
