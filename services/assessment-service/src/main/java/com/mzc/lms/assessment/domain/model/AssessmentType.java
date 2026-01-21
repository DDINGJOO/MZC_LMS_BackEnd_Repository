package com.mzc.lms.assessment.domain.model;

public enum AssessmentType {
    QUIZ("퀴즈"),
    EXAM("시험"),
    ASSIGNMENT("과제"),
    MIDTERM("중간고사"),
    FINAL("기말고사");

    private final String description;

    AssessmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
