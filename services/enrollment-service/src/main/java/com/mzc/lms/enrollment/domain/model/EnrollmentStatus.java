package com.mzc.lms.enrollment.domain.model;

public enum EnrollmentStatus {
    PENDING("대기"),
    ENROLLED("수강 중"),
    WAITLISTED("대기 목록"),
    WITHDRAWN("철회"),
    COMPLETED("수료"),
    FAILED("미수료"),
    CANCELLED("취소");

    private final String description;

    EnrollmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canWithdraw() {
        return this == ENROLLED || this == PENDING || this == WAITLISTED;
    }

    public boolean canComplete() {
        return this == ENROLLED;
    }

    public boolean isActive() {
        return this == ENROLLED || this == PENDING || this == WAITLISTED;
    }
}
