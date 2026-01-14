package com.mzc.backend.lms.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * 페이징 응답 DTO
 *
 * @param content 페이지 컨텐츠 목록
 * @param pageInfo 페이지네이션 메타데이터
 * @param <T> 컨텐츠 타입
 */
public record PageResponse<T>(
    List<T> content,
    PageInfo pageInfo
) {
    /**
     * Spring Page 객체를 PageResponse로 변환 (매퍼 함수 사용)
     * Entity를 DTO로 변환할 때 사용
     *
     * @param page Spring Page 객체
     * @param mapper Entity를 DTO로 변환하는 함수
     * @param <T> 결과 DTO 타입
     * @param <E> 원본 Entity 타입
     * @return PageResponse 인스턴스
     */
    public static <T, E> PageResponse<T> from(Page<E> page, Function<E, T> mapper) {
        List<T> content = page.getContent().stream()
            .map(mapper)
            .toList();
        return new PageResponse<>(content, PageInfo.from(page));
    }

    /**
     * Spring Page 객체를 PageResponse로 변환 (직접 변환)
     * Page의 컨텐츠가 이미 원하는 타입일 때 사용
     *
     * @param page Spring Page 객체
     * @param <T> 컨텐츠 타입
     * @return PageResponse 인스턴스
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), PageInfo.from(page));
    }
}
