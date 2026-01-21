package com.mzc.lms.profile.application.port.in;

import com.mzc.lms.profile.adapter.in.web.dto.ProfileSearchRequest;
import com.mzc.lms.profile.adapter.in.web.dto.ProfileSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchProfileUseCase {

    Page<ProfileSearchResponse> searchProfiles(ProfileSearchRequest request, Pageable pageable);
}
