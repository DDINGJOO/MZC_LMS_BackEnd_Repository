package com.mzc.backend.lms.domains.user.application.port.in;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchCursorResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchRequestDto;

/**
 * 유저 탐색 UseCase
 */
public interface SearchUsersUseCase {

    /**
     * 유저 탐색 (커서 기반)
     * @param request 탐색 요청 DTO
     * @return 탐색 결과
     */
    UserSearchCursorResponseDto searchUsers(UserSearchRequestDto request);
}
