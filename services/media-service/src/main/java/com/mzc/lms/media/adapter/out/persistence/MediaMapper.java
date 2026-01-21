package com.mzc.lms.media.adapter.out.persistence;

import com.mzc.lms.media.adapter.out.persistence.entity.MediaEntity;
import com.mzc.lms.media.domain.model.Media;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MediaMapper {

    public Media toDomain(MediaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Media.builder()
                .id(entity.getId())
                .originalFileName(entity.getOriginalFileName())
                .storedFileName(entity.getStoredFileName())
                .storagePath(entity.getStoragePath())
                .mimeType(entity.getMimeType())
                .mediaType(entity.getMediaType())
                .status(entity.getStatus())
                .fileSize(entity.getFileSize())
                .url(entity.getUrl())
                .thumbnailUrls(entity.getThumbnailUrls() != null ?
                        new HashMap<>(entity.getThumbnailUrls()) : new HashMap<>())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .duration(entity.getDuration())
                .uploadedBy(entity.getUploadedBy())
                .uploadedByName(entity.getUploadedByName())
                .description(entity.getDescription())
                .metadata(entity.getMetadata() != null ?
                        new HashMap<>(entity.getMetadata()) : new HashMap<>())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public MediaEntity toEntity(Media domain) {
        if (domain == null) {
            return null;
        }

        return MediaEntity.builder()
                .id(domain.getId())
                .originalFileName(domain.getOriginalFileName())
                .storedFileName(domain.getStoredFileName())
                .storagePath(domain.getStoragePath())
                .mimeType(domain.getMimeType())
                .mediaType(domain.getMediaType())
                .status(domain.getStatus())
                .fileSize(domain.getFileSize())
                .url(domain.getUrl())
                .thumbnailUrls(domain.getThumbnailUrls() != null ?
                        new HashMap<>(domain.getThumbnailUrls()) : new HashMap<>())
                .width(domain.getWidth())
                .height(domain.getHeight())
                .duration(domain.getDuration())
                .uploadedBy(domain.getUploadedBy())
                .uploadedByName(domain.getUploadedByName())
                .description(domain.getDescription())
                .metadata(domain.getMetadata() != null ?
                        new HashMap<>(domain.getMetadata()) : new HashMap<>())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
