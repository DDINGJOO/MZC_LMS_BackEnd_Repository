package com.mzc.lms.board.application.service;

import com.mzc.lms.board.application.port.in.AttachmentUseCase;
import com.mzc.lms.board.application.port.out.AttachmentRepository;
import com.mzc.lms.board.application.port.out.FileStoragePort;
import com.mzc.lms.board.domain.model.Attachment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttachmentService implements AttachmentUseCase {

    private final AttachmentRepository attachmentRepository;
    private final FileStoragePort fileStoragePort;

    @Override
    public Attachment saveAttachment(Long postId, String originalFileName, String storedFileName,
                                     String storagePath, String contentType, Long fileSize,
                                     String downloadUrl, Long uploadedBy) {
        Attachment attachment = Attachment.create(postId, originalFileName, storedFileName,
                storagePath, contentType, fileSize, downloadUrl, uploadedBy);
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment incrementDownloadCount(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("첨부파일을 찾을 수 없습니다: " + id));
        Attachment updatedAttachment = attachment.incrementDownloadCount();
        return attachmentRepository.save(updatedAttachment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Attachment> findById(Long id) {
        return attachmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> findByPostId(Long postId) {
        return attachmentRepository.findByPostId(postId);
    }

    @Override
    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("첨부파일을 찾을 수 없습니다: " + id));

        try {
            fileStoragePort.deleteFile(attachment.getStoragePath());
        } catch (Exception e) {
            log.warn("파일 삭제 실패: {}", attachment.getStoragePath(), e);
        }

        attachmentRepository.deleteById(id);
    }

    @Override
    public void deleteByPostId(Long postId) {
        List<Attachment> attachments = attachmentRepository.findByPostId(postId);
        for (Attachment attachment : attachments) {
            try {
                fileStoragePort.deleteFile(attachment.getStoragePath());
            } catch (Exception e) {
                log.warn("파일 삭제 실패: {}", attachment.getStoragePath(), e);
            }
        }
        attachmentRepository.deleteByPostId(postId);
    }
}
