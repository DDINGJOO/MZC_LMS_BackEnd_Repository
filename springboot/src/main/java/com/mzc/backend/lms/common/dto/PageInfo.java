package com.mzc.backend.lms.common.dto;

import org.springframework.data.domain.Page;

/**
 * 페이지네이션 메타데이터 정보
 *
 * @param page 현재 페이지 번호 (0-based)
 * @param size 페이지 크기
 * @param totalElements 전체 요소 수
 * @param totalPages 전체 페이지 수
 * @param first 첫 페이지 여부
 * @param last 마지막 페이지 여부
 * @param hasNext 다음 페이지 존재 여부
 * @param hasPrevious 이전 페이지 존재 여부
 */
public record PageInfo(
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last,
    boolean hasNext,
    boolean hasPrevious
) {
    /**
     * Spring Page 객체로부터 PageInfo 생성
     *
     * @param page Spring Page 객체
     * @return PageInfo 인스턴스
     */
    public static PageInfo from(Page<?> page) {
        return new PageInfo(
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious()
        );
    }
}
