package com.mzc.backend.lms.domains.notification.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 알림 목록 응답 DTO
 */
@Getter
@Builder
public class NotificationListResponseDto {

    private List<NotificationResponseDto> notifications;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private boolean hasNext;
    private boolean hasPrevious;
    private long unreadCount;

    /**
     * Page to DTO 변환
     */
    public static NotificationListResponseDto from(Page<NotificationResponseDto> page, long unreadCount) {
        return NotificationListResponseDto.builder()
                .notifications(page.getContent())
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .unreadCount(unreadCount)
                .build();
    }
}
