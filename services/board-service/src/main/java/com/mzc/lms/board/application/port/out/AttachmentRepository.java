package com.mzc.lms.board.application.port.out;

import com.mzc.lms.board.domain.model.Attachment;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {

    Attachment save(Attachment attachment);

    Optional<Attachment> findById(Long id);

    List<Attachment> findByPostId(Long postId);

    void deleteById(Long id);

    void deleteByPostId(Long postId);
}
