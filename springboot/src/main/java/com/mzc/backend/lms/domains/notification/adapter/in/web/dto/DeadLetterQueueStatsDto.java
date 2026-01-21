package com.mzc.backend.lms.domains.notification.adapter.in.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeadLetterQueueStatsDto {

    private long totalCount;
    private List<DeadLetterMessageResponseDto> messages;
    private int offset;
    private int limit;
    private boolean hasMore;
}
