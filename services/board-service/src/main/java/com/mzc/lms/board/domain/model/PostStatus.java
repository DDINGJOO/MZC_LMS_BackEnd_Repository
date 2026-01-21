package com.mzc.lms.board.domain.model;

public enum PostStatus {
    DRAFT("작성중"),
    PENDING("승인대기"),
    PUBLISHED("게시됨"),
    HIDDEN("숨김"),
    DELETED("삭제됨");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
