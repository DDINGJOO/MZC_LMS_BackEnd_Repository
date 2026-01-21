package com.mzc.lms.media.adapter.out.persistence.repository;

import com.mzc.lms.media.adapter.out.persistence.entity.MediaEntity;
import com.mzc.lms.media.domain.model.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaJpaRepository extends JpaRepository<MediaEntity, Long> {

    Page<MediaEntity> findByUploadedBy(Long uploadedBy, Pageable pageable);

    Page<MediaEntity> findByMediaType(MediaType mediaType, Pageable pageable);

    List<MediaEntity> findByIdIn(List<Long> ids);
}
