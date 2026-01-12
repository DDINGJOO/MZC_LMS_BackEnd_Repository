package com.mzc.backend.lms.domains.user.application.port.in;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.ProfileResponseDto;

/**
 * 내 프로필 조회 UseCase
 */
public interface GetMyProfileUseCase {

    /**
     * 내 프로필 조회
     * @param userId 사용자 ID
     * @return 프로필 정보
     */
    ProfileResponseDto getMyProfile(Long userId);
}
