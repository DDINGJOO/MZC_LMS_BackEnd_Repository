package com.mzc.backend.lms.domains.course.notice.application.port.out;

import com.mzc.backend.lms.domains.course.notice.domain.event.CourseNoticeCreatedEvent;

/**
 * 이벤트 발행 Port
 */
public interface NoticeEventPort {

    /**
     * 공지사항 생성 이벤트 발행
     */
    void publishNoticeCreatedEvent(CourseNoticeCreatedEvent event);
}
