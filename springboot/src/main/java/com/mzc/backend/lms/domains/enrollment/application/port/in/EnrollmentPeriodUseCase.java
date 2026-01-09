package com.mzc.backend.lms.domains.enrollment.application.port.in;

import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.EnrollmentPeriodResponseDto;

/**
 * 수강신청 기간 조회 UseCase (Inbound Port)
 */
public interface EnrollmentPeriodUseCase {

    /**
     * 현재 활성화된 기간 조회
     * @param typeCode 기간 타입 코드 (ENROLLMENT, COURSE_REGISTRATION, ADJUSTMENT, CANCELLATION)
     *                 null이면 기본값으로 ENROLLMENT 조회
     */
    EnrollmentPeriodResponseDto getCurrentPeriod(String typeCode);
}
