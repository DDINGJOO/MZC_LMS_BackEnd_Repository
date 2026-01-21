package com.mzc.lms.media.domain.event;

import com.mzc.lms.media.domain.model.MediaStatus;
import com.mzc.lms.media.domain.model.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaEvent {

    private String eventId;
    private String eventType;
    private Long mediaId;
    private String fileName;
    private MediaType mediaType;
    private MediaStatus status;
    private Long uploadedBy;
    private String url;
    private LocalDateTime timestamp;

    public static MediaEvent uploaded(Long mediaId, String fileName, MediaType mediaType, Long uploadedBy, String url) {
        return MediaEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("MEDIA_UPLOADED")
                .mediaId(mediaId)
                .fileName(fileName)
                .mediaType(mediaType)
                .status(MediaStatus.PROCESSING)
                .uploadedBy(uploadedBy)
                .url(url)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static MediaEvent processed(Long mediaId, String fileName, MediaType mediaType, Long uploadedBy, String url) {
        return MediaEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("MEDIA_PROCESSED")
                .mediaId(mediaId)
                .fileName(fileName)
                .mediaType(mediaType)
                .status(MediaStatus.READY)
                .uploadedBy(uploadedBy)
                .url(url)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static MediaEvent deleted(Long mediaId, String fileName, Long uploadedBy) {
        return MediaEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("MEDIA_DELETED")
                .mediaId(mediaId)
                .fileName(fileName)
                .status(MediaStatus.DELETED)
                .uploadedBy(uploadedBy)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
