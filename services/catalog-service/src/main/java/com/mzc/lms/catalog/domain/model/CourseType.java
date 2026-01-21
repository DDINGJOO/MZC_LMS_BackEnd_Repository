package com.mzc.lms.catalog.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseType {

    private Long id;
    private Integer typeCode;
    private Integer category;

    public static final int MAJOR_REQ = 1;
    public static final int MAJOR_ELEC = 2;
    public static final int GEN_REQ = 3;
    public static final int GEN_ELEC = 4;

    public static final int CATEGORY_MAJOR = 0;
    public static final int CATEGORY_GENERAL = 1;

    public static CourseType create(Integer typeCode, Integer category) {
        return CourseType.builder()
                .typeCode(typeCode)
                .category(category)
                .build();
    }

    public String getTypeCodeString() {
        return switch (typeCode) {
            case MAJOR_REQ -> "MAJOR_REQ";
            case MAJOR_ELEC -> "MAJOR_ELEC";
            case GEN_REQ -> "GEN_REQ";
            case GEN_ELEC -> "GEN_ELEC";
            default -> "UNKNOWN";
        };
    }

    public String getTypeName() {
        return switch (typeCode) {
            case MAJOR_REQ -> "전공필수";
            case MAJOR_ELEC -> "전공선택";
            case GEN_REQ -> "교양필수";
            case GEN_ELEC -> "교양선택";
            default -> "미지정";
        };
    }

    public String getColor() {
        return switch (typeCode) {
            case MAJOR_REQ -> "#FFB4C8";
            case MAJOR_ELEC -> "#B4D7FF";
            case GEN_REQ -> "#FFD9B4";
            case GEN_ELEC -> "#C8E6C9";
            default -> "#E0E0E0";
        };
    }

    public static int parseTypeCode(String typeCodeString) {
        return switch (typeCodeString) {
            case "MAJOR_REQ" -> MAJOR_REQ;
            case "MAJOR_ELEC" -> MAJOR_ELEC;
            case "GEN_REQ" -> GEN_REQ;
            case "GEN_ELEC" -> GEN_ELEC;
            default -> throw new IllegalArgumentException("Invalid course type code: " + typeCodeString);
        };
    }

    public boolean isMajor() {
        return category == CATEGORY_MAJOR;
    }

    public boolean isGeneral() {
        return category == CATEGORY_GENERAL;
    }

    public boolean isRequired() {
        return typeCode == MAJOR_REQ || typeCode == GEN_REQ;
    }

    public boolean isElective() {
        return typeCode == MAJOR_ELEC || typeCode == GEN_ELEC;
    }
}
