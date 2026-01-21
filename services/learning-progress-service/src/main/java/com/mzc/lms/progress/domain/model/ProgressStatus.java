package com.mzc.lms.progress.domain.model;

public enum ProgressStatus {
    NOT_STARTED("시작 전"),
    IN_PROGRESS("진행 중"),
    COMPLETED("완료"),
    PAUSED("일시 중지");

    private final String description;

    ProgressStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canProgress() {
        return this == NOT_STARTED || this == IN_PROGRESS || this == PAUSED;
    }
}
