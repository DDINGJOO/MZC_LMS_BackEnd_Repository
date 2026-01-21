package com.mzc.lms.board.adapter.out.persistence;

import com.mzc.lms.board.adapter.out.persistence.entity.AttachmentEntity;
import com.mzc.lms.board.adapter.out.persistence.repository.AttachmentJpaRepository;
import com.mzc.lms.board.application.port.out.AttachmentRepository;
import com.mzc.lms.board.domain.model.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AttachmentPersistenceAdapter implements AttachmentRepository {

    private final AttachmentJpaRepository attachmentJpaRepository;

    @Override
    public Attachment save(Attachment attachment) {
        AttachmentEntity entity = AttachmentEntity.fromDomain(attachment);
        AttachmentEntity savedEntity = attachmentJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Attachment> findById(Long id) {
        return attachmentJpaRepository.findById(id).map(AttachmentEntity::toDomain);
    }

    @Override
    public List<Attachment> findByPostId(Long postId) {
        return attachmentJpaRepository.findByPostId(postId).stream()
                .map(AttachmentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        attachmentJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByPostId(Long postId) {
        attachmentJpaRepository.deleteByPostId(postId);
    }
}
