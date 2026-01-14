package com.mzc.backend.lms.domains.board.adapter.out.persistence;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Attachment;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.repository.AttachmentRepositoryJpa;
import com.mzc.backend.lms.domains.board.application.port.out.AttachmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Attachment 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class AttachmentPersistenceAdapter implements AttachmentRepositoryPort {

    private final AttachmentRepositoryJpa attachmentRepositoryJpa;

    @Override
    public Attachment save(Attachment attachment) {
        return attachmentRepositoryJpa.save(attachment);
    }

    @Override
    public Optional<Attachment> findById(Long id) {
        return attachmentRepositoryJpa.findById(id);
    }

    @Override
    public List<Attachment> findAllById(Iterable<Long> ids) {
        return attachmentRepositoryJpa.findAllById(ids);
    }

    @Override
    public List<Attachment> findByPost(Post post) {
        return attachmentRepositoryJpa.findByPost(post);
    }

    @Override
    public void delete(Attachment attachment) {
        attachmentRepositoryJpa.delete(attachment);
    }

    @Override
    public List<Attachment> saveAll(Iterable<Attachment> attachments) {
        return attachmentRepositoryJpa.saveAll(attachments);
    }
}
