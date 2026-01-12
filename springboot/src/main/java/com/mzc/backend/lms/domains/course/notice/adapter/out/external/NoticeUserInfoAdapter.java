package com.mzc.backend.lms.domains.course.notice.adapter.out.external;

import com.mzc.backend.lms.domains.course.notice.application.port.out.NoticeUserInfoPort;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.UserBasicInfoDto;
import com.mzc.backend.lms.domains.user.application.port.in.GetUserInfoCacheUseCase;
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

    private final GetUserInfoCacheUseCase getUserInfoCacheUseCase;

    @Override
    public Map<Long, UserBasicInfoDto> getUserInfoMap(Set<Long> userIds) {
        return getUserInfoCacheUseCase.getUserInfoMap(userIds);
    }
}
