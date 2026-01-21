package com.mzc.lms.media.adapter.in.web;

import com.mzc.lms.media.adapter.in.web.dto.MediaListResponse;
import com.mzc.lms.media.adapter.in.web.dto.MediaResponse;
import com.mzc.lms.media.adapter.in.web.dto.MediaUpdateRequest;
import com.mzc.lms.media.application.port.in.MediaUseCase;
import com.mzc.lms.media.domain.model.Media;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaUseCase mediaUseCase;

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaResponse> uploadMedia(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "uploadedBy") String uploadedBy,
            @RequestPart(value = "uploadedByName", required = false) String uploadedByName,
            @RequestPart(value = "description", required = false) String description) {

        log.info("Upload media request: fileName={}, uploadedBy={}", file.getOriginalFilename(), uploadedBy);

        Media media = mediaUseCase.uploadMedia(
                file,
                Long.parseLong(uploadedBy),
                uploadedByName,
                description
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MediaResponse.from(media));
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaResponse> getMedia(@PathVariable Long mediaId) {
        return mediaUseCase.getMedia(mediaId)
                .map(media -> ResponseEntity.ok(MediaResponse.from(media)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<MediaListResponse> getMediaList(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<Media> mediaPage = mediaUseCase.getMediaList(pageable);
        return ResponseEntity.ok(MediaListResponse.from(mediaPage));
    }

    @GetMapping("/uploader/{uploadedBy}")
    public ResponseEntity<MediaListResponse> getMediaByUploader(
            @PathVariable Long uploadedBy,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<Media> mediaPage = mediaUseCase.getMediaByUploader(uploadedBy, pageable);
        return ResponseEntity.ok(MediaListResponse.from(mediaPage));
    }

    @GetMapping("/type/{mediaType}")
    public ResponseEntity<MediaListResponse> getMediaByType(
            @PathVariable com.mzc.lms.media.domain.model.MediaType mediaType,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<Media> mediaPage = mediaUseCase.getMediaByType(mediaType, pageable);
        return ResponseEntity.ok(MediaListResponse.from(mediaPage));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<MediaResponse>> getMediaByIds(@RequestBody List<Long> mediaIds) {
        List<Media> mediaList = mediaUseCase.getMediaByIds(mediaIds);
        List<MediaResponse> responses = mediaList.stream()
                .map(MediaResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{mediaId}")
    public ResponseEntity<MediaResponse> updateMedia(
            @PathVariable Long mediaId,
            @Valid @RequestBody MediaUpdateRequest request) {

        Media media = mediaUseCase.updateDescription(mediaId, request.getDescription());
        return ResponseEntity.ok(MediaResponse.from(media));
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long mediaId) {
        mediaUseCase.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{mediaId}/thumbnail/{size}")
    public ResponseEntity<String> getThumbnailUrl(
            @PathVariable Long mediaId,
            @PathVariable String size) {

        String thumbnailUrl = mediaUseCase.getThumbnailUrl(mediaId, size);
        if (thumbnailUrl != null) {
            return ResponseEntity.ok(thumbnailUrl);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{mediaId}/process")
    public ResponseEntity<Void> processMedia(@PathVariable Long mediaId) {
        mediaUseCase.processMedia(mediaId);
        return ResponseEntity.accepted().build();
    }
}
