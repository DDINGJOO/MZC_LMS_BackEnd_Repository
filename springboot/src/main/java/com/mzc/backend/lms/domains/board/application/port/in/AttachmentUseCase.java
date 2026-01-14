package com.mzc.backend.lms.domains.board.application.port.in;

import com.mzc.backend.lms.domains.board.adapter.in.web.dto.response.AttachmentResponseDto;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Attachment;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Comment;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 첨부파일 UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface AttachmentUseCase {

    /**
     * 단일 파일 업로드
     */
    AttachmentResponseDto uploadFile(MultipartFile file, String ignoredType, Post post, Comment comment);

    /**
     * 다중 파일 업로드
     */
    List<AttachmentResponseDto> uploadMultipleFiles(List<MultipartFile> files, String ignoredType);

    /**
     * 첨부파일 조회
     */
    AttachmentResponseDto getAttachment(Long attachmentId);

    /**
     * 첨부파일 삭제
     */
    void deleteAttachment(Long attachmentId);

    /**
     * 파일 다운로드를 위한 실제 파일 가져오기
     */
    File getFile(Long attachmentId);

    /**
     * attachmentIds로 Attachment 엔티티 목록 조회
     */
    List<Attachment> getAttachmentsByIds(List<Long> attachmentIds);
}
