package com.mzc.backend.lms.domains.attendance.application.port.in;

import com.mzc.backend.lms.domains.attendance.application.event.ContentCompletedEvent;

/**
 * 출석 이벤트 처리 UseCase (Inbound Port)
 * Event Listener에서 이 인터페이스를 호출
 */
public interface AttendanceEventUseCase {

    /**
     * 콘텐츠 완료 이벤트 처리
     * Video Server에서 발행한 이벤트를 수신하여 출석 상태 갱신
     */
    void processContentCompleted(ContentCompletedEvent event);
}
