package com.mzc.backend.lms.domains.enrollment.adapter.out.event;

import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentEventPort;
import com.mzc.backend.lms.domains.enrollment.domain.event.EnrollmentCancelledEvent;
import com.mzc.backend.lms.domains.enrollment.domain.event.EnrollmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 수강신청 이벤트 Adapter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentEventAdapter implements EnrollmentEventPort {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishEnrollmentCreated(Long studentId, Long courseId, String courseName, String sectionNumber) {
        log.debug("수강신청 완료 이벤트 발행: studentId={}, courseId={}, courseName={}",
                studentId, courseId, courseName);
        eventPublisher.publishEvent(new EnrollmentCreatedEvent(studentId, courseId, courseName, sectionNumber));
    }

    @Override
    public void publishEnrollmentCancelled(Long studentId, Long courseId, String courseName, String sectionNumber) {
        log.debug("수강신청 취소 이벤트 발행: studentId={}, courseId={}, courseName={}",
                studentId, courseId, courseName);
        eventPublisher.publishEvent(new EnrollmentCancelledEvent(studentId, courseId, courseName, sectionNumber));
    }
}
