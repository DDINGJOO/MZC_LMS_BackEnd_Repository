package com.mzc.lms.media.application.service;

import com.mzc.lms.media.application.port.in.MediaUseCase;
import com.mzc.lms.media.application.port.out.ImageProcessorPort;
import com.mzc.lms.media.application.port.out.MediaEventPublisher;
import com.mzc.lms.media.application.port.out.MediaRepository;
import com.mzc.lms.media.application.port.out.MediaStoragePort;
import com.mzc.lms.media.domain.event.MediaEvent;
import com.mzc.lms.media.domain.model.Media;
import com.mzc.lms.media.domain.model.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaService implements MediaUseCase {

    private final MediaRepository mediaRepository;
    private final MediaStoragePort mediaStoragePort;
    private final ImageProcessorPort imageProcessorPort;
    private final MediaEventPublisher mediaEventPublisher;

    @Value("${media.image.thumbnail.widths:150,300,600}")
    private String thumbnailWidths;

    @Value("${media.image.thumbnail.enabled:true}")
    private boolean thumbnailEnabled;

    @Override
    @Transactional
    public Media uploadMedia(MultipartFile file, Long uploadedBy, String uploadedByName, String description) {
        try {
            String originalFileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            Long fileSize = file.getSize();

            // Store the original file
            MediaStoragePort.StorageResult storageResult = mediaStoragePort.store(
                    file.getInputStream(),
                    originalFileName,
                    contentType,
                    fileSize
            );

            // Create media entity
            Media media = Media.create(
                    originalFileName,
                    storageResult.storedFileName(),
                    storageResult.storagePath(),
                    contentType,
                    fileSize,
                    storageResult.url(),
                    uploadedBy,
                    uploadedByName
            );

            if (description != null && !description.isEmpty()) {
                media = media.updateDescription(description);
            }

            Media savedMedia = mediaRepository.save(media);

            // Publish upload event for async processing
            mediaEventPublisher.publish(MediaEvent.uploaded(
                    savedMedia.getId(),
                    savedMedia.getOriginalFileName(),
                    savedMedia.getMediaType(),
                    savedMedia.getUploadedBy(),
                    savedMedia.getUrl()
            ));

            log.info("Media uploaded: id={}, fileName={}, type={}",
                    savedMedia.getId(), savedMedia.getOriginalFileName(), savedMedia.getMediaType());

            return savedMedia;

        } catch (IOException e) {
            log.error("Failed to upload media: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload media", e);
        }
    }

    @Override
    public Optional<Media> getMedia(Long mediaId) {
        return mediaRepository.findById(mediaId);
    }

    @Override
    public Page<Media> getMediaList(Pageable pageable) {
        return mediaRepository.findAll(pageable);
    }

    @Override
    public Page<Media> getMediaByUploader(Long uploadedBy, Pageable pageable) {
        return mediaRepository.findByUploadedBy(uploadedBy, pageable);
    }

    @Override
    public Page<Media> getMediaByType(MediaType mediaType, Pageable pageable) {
        return mediaRepository.findByMediaType(mediaType, pageable);
    }

    @Override
    public List<Media> getMediaByIds(List<Long> mediaIds) {
        return mediaRepository.findByIdIn(mediaIds);
    }

    @Override
    @Transactional
    public Media updateDescription(Long mediaId, String description) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Media not found: " + mediaId));

        Media updatedMedia = media.updateDescription(description);
        return mediaRepository.save(updatedMedia);
    }

    @Override
    @Transactional
    public void deleteMedia(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Media not found: " + mediaId));

        // Delete from storage
        mediaStoragePort.delete(media.getStoragePath());
        if (media.isImage()) {
            mediaStoragePort.deleteThumbnails(media.getStoragePath());
        }

        // Mark as deleted
        Media deletedMedia = media.delete();
        mediaRepository.save(deletedMedia);

        // Publish delete event
        mediaEventPublisher.publish(MediaEvent.deleted(
                media.getId(),
                media.getOriginalFileName(),
                media.getUploadedBy()
        ));

        log.info("Media deleted: id={}", mediaId);
    }

    @Override
    @Async("mediaProcessingExecutor")
    @Transactional
    public void processMedia(Long mediaId) {
        log.info("Processing media: id={}", mediaId);

        try {
            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new IllegalArgumentException("Media not found: " + mediaId));

            Media processedMedia = media;

            if (media.isImage() && thumbnailEnabled) {
                processedMedia = processImage(media);
            }

            processedMedia = processedMedia.markAsReady();
            Media savedMedia = mediaRepository.save(processedMedia);

            // Publish processed event
            mediaEventPublisher.publish(MediaEvent.processed(
                    savedMedia.getId(),
                    savedMedia.getOriginalFileName(),
                    savedMedia.getMediaType(),
                    savedMedia.getUploadedBy(),
                    savedMedia.getUrl()
            ));

            log.info("Media processed successfully: id={}", mediaId);

        } catch (Exception e) {
            log.error("Failed to process media: id={}, error={}", mediaId, e.getMessage(), e);

            mediaRepository.findById(mediaId).ifPresent(media -> {
                Media failedMedia = media.markAsFailed();
                mediaRepository.save(failedMedia);
            });
        }
    }

    private Media processImage(Media media) {
        try {
            InputStream inputStream = mediaStoragePort.retrieve(media.getStoragePath());

            // Read input stream into byte array for multiple uses
            byte[] imageBytes = toByteArray(inputStream);

            // Get image info
            ImageProcessorPort.ImageInfo imageInfo = imageProcessorPort.getImageInfo(
                    new ByteArrayInputStream(imageBytes)
            );

            Media updatedMedia = media.withImageDimensions(imageInfo.width(), imageInfo.height());

            // Create thumbnails
            int[] widths = parseThumbnailWidths();
            Map<String, String> thumbnailUrls = new HashMap<>();

            for (int width : widths) {
                if (width < imageInfo.width()) {
                    InputStream thumbnailStream = imageProcessorPort.createThumbnail(
                            new ByteArrayInputStream(imageBytes),
                            width
                    );

                    MediaStoragePort.StorageResult thumbnailResult = mediaStoragePort.storeThumbnail(
                            thumbnailStream,
                            media.getStoredFileName(),
                            String.valueOf(width)
                    );

                    thumbnailUrls.put(String.valueOf(width), thumbnailResult.url());
                }
            }

            return updatedMedia.withThumbnails(thumbnailUrls);

        } catch (Exception e) {
            log.error("Failed to process image: {}", e.getMessage(), e);
            return media;
        }
    }

    @Override
    public String getThumbnailUrl(Long mediaId, String size) {
        return mediaRepository.findById(mediaId)
                .map(media -> {
                    Map<String, String> thumbnails = media.getThumbnailUrls();
                    if (thumbnails != null && thumbnails.containsKey(size)) {
                        return thumbnails.get(size);
                    }
                    return media.getUrl();
                })
                .orElse(null);
    }

    private int[] parseThumbnailWidths() {
        String[] parts = thumbnailWidths.split(",");
        int[] widths = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            widths[i] = Integer.parseInt(parts[i].trim());
        }
        return widths;
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
