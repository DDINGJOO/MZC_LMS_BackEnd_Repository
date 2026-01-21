package com.mzc.lms.assessment.domain.model;

public enum SubmissionStatus {
    NOT_STARTED("미시작"),
    IN_PROGRESS("진행 중"),
    SUBMITTED("제출 완료"),
    GRADED("채점 완료"),
    RETURNED("반환됨");

    private final String description;

    SubmissionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canSubmit() {
        return this == NOT_STARTED || this == IN_PROGRESS;
    }

    public boolean canGrade() {
        return this == SUBMITTED;
    }
}
