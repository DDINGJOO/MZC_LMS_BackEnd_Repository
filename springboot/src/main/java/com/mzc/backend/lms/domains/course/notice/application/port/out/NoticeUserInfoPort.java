package com.mzc.backend.lms.domains.course.notice.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.UserBasicInfoDto;

import java.util.Map;
import java.util.Set;

/**
 * 사용자 정보 외부 Port - notice 전용
 */
public interface NoticeUserInfoPort {

    /**
     * 사용자 ID 목록으로 기본 정보 조회
     */
    Map<Long, UserBasicInfoDto> getUserInfoMap(Set<Long> userIds);
}
