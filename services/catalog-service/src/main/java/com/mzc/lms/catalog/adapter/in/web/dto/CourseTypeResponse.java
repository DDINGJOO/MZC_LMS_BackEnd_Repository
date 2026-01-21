package com.mzc.lms.catalog.adapter.in.web.dto;

import com.mzc.lms.catalog.domain.model.CourseType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseTypeResponse {

    private Long id;
    private Integer typeCode;
    private String typeCodeString;
    private String typeName;
    private Integer category;
    private String categoryName;
    private String color;

    public static CourseTypeResponse from(CourseType courseType) {
        String categoryName = courseType.isMajor() ? "전공" : "교양";

        return CourseTypeResponse.builder()
                .id(courseType.getId())
                .typeCode(courseType.getTypeCode())
                .typeCodeString(courseType.getTypeCodeString())
                .typeName(courseType.getTypeName())
                .category(courseType.getCategory())
                .categoryName(categoryName)
                .color(courseType.getColor())
                .build();
    }
}
