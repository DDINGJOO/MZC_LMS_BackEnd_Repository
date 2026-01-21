package com.mzc.lms.media.application.port.in;

import com.mzc.lms.media.domain.model.Media;
import com.mzc.lms.media.domain.model.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MediaUseCase {

    Media uploadMedia(MultipartFile file, Long uploadedBy, String uploadedByName, String description);

    Optional<Media> getMedia(Long mediaId);

    Page<Media> getMediaList(Pageable pageable);

    Page<Media> getMediaByUploader(Long uploadedBy, Pageable pageable);

    Page<Media> getMediaByType(MediaType mediaType, Pageable pageable);

    List<Media> getMediaByIds(List<Long> mediaIds);

    Media updateDescription(Long mediaId, String description);

    void deleteMedia(Long mediaId);

    void processMedia(Long mediaId);

    String getThumbnailUrl(Long mediaId, String size);
}
