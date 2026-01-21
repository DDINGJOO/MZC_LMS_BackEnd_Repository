package com.mzc.lms.media.adapter.in.web.dto;

import com.mzc.lms.media.domain.model.Media;
import org.springframework.data.domain.Page;

import java.util.List;

public class MediaListResponse {

    private List<MediaResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public MediaListResponse() {}

    public MediaListResponse(List<MediaResponse> content, int page, int size,
                             long totalElements, int totalPages, boolean hasNext, boolean hasPrevious) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public static MediaListResponse from(Page<Media> mediaPage) {
        List<MediaResponse> content = mediaPage.getContent().stream()
                .map(MediaResponse::from)
                .toList();

        return new MediaListResponse(
                content,
                mediaPage.getNumber(),
                mediaPage.getSize(),
                mediaPage.getTotalElements(),
                mediaPage.getTotalPages(),
                mediaPage.hasNext(),
                mediaPage.hasPrevious()
        );
    }

    public List<MediaResponse> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public boolean isHasNext() { return hasNext; }
    public boolean isHasPrevious() { return hasPrevious; }
}
