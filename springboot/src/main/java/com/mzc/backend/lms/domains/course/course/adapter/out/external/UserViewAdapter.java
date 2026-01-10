package com.mzc.backend.lms.domains.course.course.adapter.out.external;

import com.mzc.backend.lms.domains.course.course.application.port.out.UserViewPort;
import com.mzc.backend.lms.views.UserViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 사용자 조회 외부 Adapter (views 도메인)
 */
@Component
@RequiredArgsConstructor
public class UserViewAdapter implements UserViewPort {

    private final UserViewService userViewService;

    @Override
    public String getUserName(String userId) {
        return userViewService.getUserName(userId);
    }
}
