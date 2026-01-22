package com.mzc.lms.messaging.domain.model;

public enum RoomType {
    DIRECT("1:1 채팅"),
    GROUP("그룹 채팅"),
    COURSE("강의 토론방"),
    ANNOUNCEMENT("공지 채널");

    private final String description;

    RoomType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
