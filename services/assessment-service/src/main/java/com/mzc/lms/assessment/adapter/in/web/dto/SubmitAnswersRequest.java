package com.mzc.lms.assessment.adapter.in.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SubmitAnswersRequest {

    @NotEmpty(message = "답안은 필수입니다")
    private Map<Long, String> answers;
}
