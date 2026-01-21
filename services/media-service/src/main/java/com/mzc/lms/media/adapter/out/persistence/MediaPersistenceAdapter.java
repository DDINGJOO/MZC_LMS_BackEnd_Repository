package com.mzc.lms.media.adapter.out.persistence;

import com.mzc.lms.media.adapter.out.persistence.entity.MediaEntity;
import com.mzc.lms.media.adapter.out.persistence.repository.MediaJpaRepository;
import com.mzc.lms.media.application.port.out.MediaRepository;
import com.mzc.lms.media.domain.model.Media;
import com.mzc.lms.media.domain.model.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MediaPersistenceAdapter implements MediaRepository {

    private final MediaJpaRepository mediaJpaRepository;
    private final MediaMapper mediaMapper;

    @Override
    public Media save(Media media) {
        MediaEntity entity = mediaMapper.toEntity(media);
        MediaEntity savedEntity = mediaJpaRepository.save(entity);
        return mediaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Media> findById(Long id) {
        return mediaJpaRepository.findById(id)
                .map(mediaMapper::toDomain);
    }

    @Override
    public Page<Media> findAll(Pageable pageable) {
        return mediaJpaRepository.findAll(pageable)
                .map(mediaMapper::toDomain);
    }

    @Override
    public Page<Media> findByUploadedBy(Long uploadedBy, Pageable pageable) {
        return mediaJpaRepository.findByUploadedBy(uploadedBy, pageable)
                .map(mediaMapper::toDomain);
    }

    @Override
    public Page<Media> findByMediaType(MediaType mediaType, Pageable pageable) {
        return mediaJpaRepository.findByMediaType(mediaType, pageable)
                .map(mediaMapper::toDomain);
    }

    @Override
    public List<Media> findByIdIn(List<Long> ids) {
        return mediaJpaRepository.findByIdIn(ids).stream()
                .map(mediaMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        mediaJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return mediaJpaRepository.existsById(id);
    }
}
