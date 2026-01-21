package com.mzc.lms.assessment.domain.model;

public enum QuestionType {
    MULTIPLE_CHOICE("객관식"),
    TRUE_FALSE("참거짓"),
    SHORT_ANSWER("단답형"),
    ESSAY("논술형"),
    FILE_UPLOAD("파일 제출");

    private final String description;

    QuestionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAutoGradable() {
        return this == MULTIPLE_CHOICE || this == TRUE_FALSE;
    }
}
