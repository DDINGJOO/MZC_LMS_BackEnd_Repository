package com.mzc.lms.media.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class Media {

    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String storagePath;
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
    private Map<String, String> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Media create(String originalFileName, String storedFileName,
                               String storagePath, String mimeType, Long fileSize,
                               String url, Long uploadedBy, String uploadedByName) {
        return Media.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .storagePath(storagePath)
                .mimeType(mimeType)
                .mediaType(MediaType.fromMimeType(mimeType))
                .status(MediaStatus.PROCESSING)
                .fileSize(fileSize)
                .url(url)
                .thumbnailUrls(new HashMap<>())
                .uploadedBy(uploadedBy)
                .uploadedByName(uploadedByName)
                .metadata(new HashMap<>())
                .build();
    }

    public Media markAsReady() {
        return Media.builder()
                .id(this.id)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .mimeType(this.mimeType)
                .mediaType(this.mediaType)
                .status(MediaStatus.READY)
                .fileSize(this.fileSize)
                .url(this.url)
                .thumbnailUrls(this.thumbnailUrls)
                .width(this.width)
                .height(this.height)
                .duration(this.duration)
                .uploadedBy(this.uploadedBy)
                .uploadedByName(this.uploadedByName)
                .description(this.description)
                .metadata(this.metadata)
                .createdAt(this.createdAt)
                .build();
    }

    public Media markAsFailed() {
        return Media.builder()
                .id(this.id)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .mimeType(this.mimeType)
                .mediaType(this.mediaType)
                .status(MediaStatus.FAILED)
                .fileSize(this.fileSize)
                .url(this.url)
                .thumbnailUrls(this.thumbnailUrls)
                .width(this.width)
                .height(this.height)
                .duration(this.duration)
                .uploadedBy(this.uploadedBy)
                .uploadedByName(this.uploadedByName)
                .description(this.description)
                .metadata(this.metadata)
                .createdAt(this.createdAt)
                .build();
    }

    public Media delete() {
        return Media.builder()
                .id(this.id)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .mimeType(this.mimeType)
                .mediaType(this.mediaType)
                .status(MediaStatus.DELETED)
                .fileSize(this.fileSize)
                .url(this.url)
                .thumbnailUrls(this.thumbnailUrls)
                .width(this.width)
                .height(this.height)
                .duration(this.duration)
                .uploadedBy(this.uploadedBy)
                .uploadedByName(this.uploadedByName)
                .description(this.description)
                .metadata(this.metadata)
                .createdAt(this.createdAt)
                .build();
    }

    public Media withImageDimensions(int width, int height) {
        return Media.builder()
                .id(this.id)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .mimeType(this.mimeType)
                .mediaType(this.mediaType)
                .status(this.status)
                .fileSize(this.fileSize)
                .url(this.url)
                .thumbnailUrls(this.thumbnailUrls)
                .width(width)
                .height(height)
                .duration(this.duration)
                .uploadedBy(this.uploadedBy)
                .uploadedByName(this.uploadedByName)
                .description(this.description)
                .metadata(this.metadata)
                .createdAt(this.createdAt)
                .build();
    }

    public Media withThumbnails(Map<String, String> thumbnailUrls) {
        return Media.builder()
                .id(this.id)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .mimeType(this.mimeType)
                .mediaType(this.mediaType)
                .status(this.status)
                .fileSize(this.fileSize)
                .url(this.url)
                .thumbnailUrls(thumbnailUrls)
                .width(this.width)
                .height(this.height)
                .duration(this.duration)
                .uploadedBy(this.uploadedBy)
                .uploadedByName(this.uploadedByName)
                .description(this.description)
                .metadata(this.metadata)
                .createdAt(this.createdAt)
                .build();
    }

    public Media updateDescription(String description) {
        return Media.builder()
                .id(this.id)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .mimeType(this.mimeType)
                .mediaType(this.mediaType)
                .status(this.status)
                .fileSize(this.fileSize)
                .url(this.url)
                .thumbnailUrls(this.thumbnailUrls)
                .width(this.width)
                .height(this.height)
                .duration(this.duration)
                .uploadedBy(this.uploadedBy)
                .uploadedByName(this.uploadedByName)
                .description(description)
                .metadata(this.metadata)
                .createdAt(this.createdAt)
                .build();
    }

    public boolean isImage() {
        return this.mediaType == MediaType.IMAGE;
    }

    public boolean isVideo() {
        return this.mediaType == MediaType.VIDEO;
    }

    public String getFileExtension() {
        if (originalFileName == null) return "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return originalFileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
}
