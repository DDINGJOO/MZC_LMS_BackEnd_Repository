package com.mzc.lms.media.domain.model;

public enum MediaStatus {
    UPLOADING("업로드 중"),
    PROCESSING("처리 중"),
    READY("준비됨"),
    FAILED("실패"),
    DELETED("삭제됨");

    private final String description;

    MediaStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
