package com.mzc.backend.lms.domains.user.application.port.in;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.ProfileResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.ProfileUpdateRequestDto;

/**
 * 프로필 수정 UseCase
 */
public interface UpdateProfileUseCase {

    /**
     * 프로필 수정
     * @param userId 사용자 ID
     * @param request 수정 요청 DTO
     * @return 수정된 프로필 정보
     */
    ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto request);
}
