package com.mzc.lms.media.adapter.in.web.dto;

import com.mzc.lms.media.domain.model.Media;
import com.mzc.lms.media.domain.model.MediaStatus;
import com.mzc.lms.media.domain.model.MediaType;

import java.time.LocalDateTime;
import java.util.Map;

public class MediaResponse {

    private Long id;
    private String originalFileName;
    private String mimeType;
    private MediaType mediaType;
    private MediaStatus status;
    private Long fileSize;
    private String url;
    private Map<String, String> thumbnailUrls;
    private Integer width;
    private Integer height;
    private Integer duration;
    private Long uploadedBy;
    private String uploadedByName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MediaResponse() {}

    public MediaResponse(Long id, String originalFileName, String mimeType, MediaType mediaType,
                         MediaStatus status, Long fileSize, String url, Map<String, String> thumbnailUrls,
                         Integer width, Integer height, Integer duration, Long uploadedBy,
                         String uploadedByName, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.mimeType = mimeType;
        this.mediaType = mediaType;
        this.status = status;
        this.fileSize = fileSize;
        this.url = url;
        this.thumbnailUrls = thumbnailUrls;
        this.width = width;
        this.height = height;
        this.duration = duration;
        this.uploadedBy = uploadedBy;
        this.uploadedByName = uploadedByName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MediaResponse from(Media media) {
        return new MediaResponse(
                media.getId(),
                media.getOriginalFileName(),
                media.getMimeType(),
                media.getMediaType(),
                media.getStatus(),
                media.getFileSize(),
                media.getUrl(),
                media.getThumbnailUrls(),
                media.getWidth(),
                media.getHeight(),
                media.getDuration(),
                media.getUploadedBy(),
                media.getUploadedByName(),
                media.getDescription(),
                media.getCreatedAt(),
                media.getUpdatedAt()
        );
    }

    public Long getId() { return id; }
    public String getOriginalFileName() { return originalFileName; }
    public String getMimeType() { return mimeType; }
    public MediaType getMediaType() { return mediaType; }
    public MediaStatus getStatus() { return status; }
    public Long getFileSize() { return fileSize; }
    public String getUrl() { return url; }
    public Map<String, String> getThumbnailUrls() { return thumbnailUrls; }
    public Integer getWidth() { return width; }
    public Integer getHeight() { return height; }
    public Integer getDuration() { return duration; }
    public Long getUploadedBy() { return uploadedBy; }
    public String getUploadedByName() { return uploadedByName; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
