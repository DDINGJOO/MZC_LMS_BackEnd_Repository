package com.mzc.lms.messaging.domain.model;

public enum MessageType {
    TEXT("텍스트"),
    IMAGE("이미지"),
    FILE("파일"),
    SYSTEM("시스템 메시지"),
    REPLY("답장");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
