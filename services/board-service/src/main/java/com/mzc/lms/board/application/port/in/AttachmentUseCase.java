package com.mzc.lms.board.application.port.in;

import com.mzc.lms.board.domain.model.Attachment;

import java.util.List;
import java.util.Optional;

public interface AttachmentUseCase {

    Attachment saveAttachment(Long postId, String originalFileName, String storedFileName,
                              String storagePath, String contentType, Long fileSize,
                              String downloadUrl, Long uploadedBy);

    Attachment incrementDownloadCount(Long id);

    Optional<Attachment> findById(Long id);

    List<Attachment> findByPostId(Long postId);

    void deleteAttachment(Long id);

    void deleteByPostId(Long postId);
}
