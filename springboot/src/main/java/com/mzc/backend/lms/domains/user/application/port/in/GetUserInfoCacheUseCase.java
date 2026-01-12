package com.mzc.backend.lms.domains.user.application.port.in;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.UserBasicInfoDto;

import java.util.Map;
import java.util.Set;

/**
 * 유저 정보 캐시 조회 UseCase
 */
public interface GetUserInfoCacheUseCase {

    /**
     * 유저 ID Set을 받아 복호화된 유저 정보 Map 반환
     * @param userIds 조회할 유저 ID Set
     * @return userId를 key로 하는 UserBasicInfoDto Map
     */
    Map<Long, UserBasicInfoDto> getUserInfoMap(Set<Long> userIds);
}
