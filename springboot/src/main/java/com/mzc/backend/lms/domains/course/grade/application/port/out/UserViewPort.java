package com.mzc.backend.lms.domains.course.grade.application.port.out;

import java.util.List;
import java.util.Map;

/**
 * 사용자 정보 외부 Port - grade 전용
 */
public interface UserViewPort {

    /**
     * 사용자 ID 목록으로 이름 조회
     */
    Map<Long, String> getUserNames(List<Long> userIds);
}
