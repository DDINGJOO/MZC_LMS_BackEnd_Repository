package com.mzc.backend.lms.domains.course.course.application.port.out;

/**
 * 사용자 조회 외부 Port (views 도메인)
 */
public interface UserViewPort {

    /**
     * 사용자 이름 조회
     */
    String getUserName(String userId);
}
