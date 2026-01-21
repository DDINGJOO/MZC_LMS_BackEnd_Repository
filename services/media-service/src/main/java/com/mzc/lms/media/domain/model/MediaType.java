package com.mzc.lms.media.domain.model;

public enum MediaType {
    IMAGE("이미지"),
    VIDEO("비디오"),
    AUDIO("오디오"),
    DOCUMENT("문서"),
    OTHER("기타");

    private final String description;

    MediaType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static MediaType fromMimeType(String mimeType) {
        if (mimeType == null) return OTHER;

        if (mimeType.startsWith("image/")) return IMAGE;
        if (mimeType.startsWith("video/")) return VIDEO;
        if (mimeType.startsWith("audio/")) return AUDIO;
        if (mimeType.equals("application/pdf") ||
            mimeType.contains("document") ||
            mimeType.contains("spreadsheet") ||
            mimeType.contains("presentation")) return DOCUMENT;

        return OTHER;
    }
}
