package com.mzc.backend.lms.domains.course.notice.adapter.out.external;

import com.mzc.backend.lms.domains.course.notice.application.port.out.NoticeUserInfoPort;
import com.mzc.backend.lms.domains.user.profile.dto.UserBasicInfoDto;
import com.mzc.backend.lms.domains.user.profile.service.UserInfoCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 사용자 정보 외부 Adapter - notice 전용
 */
@Component
@RequiredArgsConstructor
public class NoticeUserInfoAdapter implements NoticeUserInfoPort {

    private final UserInfoCacheService userInfoCacheService;

    @Override
    public Map<Long, UserBasicInfoDto> getUserInfoMap(Set<Long> userIds) {
        return userInfoCacheService.getUserInfoMap(userIds);
    }
}
