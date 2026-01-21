package com.mzc.lms.media.application.port.out;

import com.mzc.lms.media.domain.model.Media;
import com.mzc.lms.media.domain.model.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MediaRepository {

    Media save(Media media);

    Optional<Media> findById(Long id);

    Page<Media> findAll(Pageable pageable);

    Page<Media> findByUploadedBy(Long uploadedBy, Pageable pageable);

    Page<Media> findByMediaType(MediaType mediaType, Pageable pageable);

    List<Media> findByIdIn(List<Long> ids);

    void deleteById(Long id);

    boolean existsById(Long id);
}
