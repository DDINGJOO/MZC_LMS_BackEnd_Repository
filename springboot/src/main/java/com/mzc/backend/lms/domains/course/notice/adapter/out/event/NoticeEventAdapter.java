package com.mzc.backend.lms.domains.course.notice.adapter.out.event;

import com.mzc.backend.lms.domains.course.notice.application.port.out.NoticeEventPort;
import com.mzc.backend.lms.domains.course.notice.domain.event.CourseNoticeCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 이벤트 발행 Adapter
 */
@Component
@RequiredArgsConstructor
public class NoticeEventAdapter implements NoticeEventPort {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishNoticeCreatedEvent(CourseNoticeCreatedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
