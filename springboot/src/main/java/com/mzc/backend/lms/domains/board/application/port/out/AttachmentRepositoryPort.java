package com.mzc.backend.lms.domains.board.application.port.out;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Attachment;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;

import java.util.List;
import java.util.Optional;

/**
 * Attachment 영속성을 위한 Port
 */
public interface AttachmentRepositoryPort {

    /**
     * 첨부파일 저장
     */
    Attachment save(Attachment attachment);

    /**
     * ID로 첨부파일 조회
     */
    Optional<Attachment> findById(Long id);

    /**
     * ID 목록으로 첨부파일 목록 조회
     */
    List<Attachment> findAllById(Iterable<Long> ids);

    /**
     * 게시글의 첨부파일 목록 조회
     */
    List<Attachment> findByPost(Post post);

    /**
     * 첨부파일 삭제
     */
    void delete(Attachment attachment);

    /**
     * 첨부파일 목록 저장
     */
    List<Attachment> saveAll(Iterable<Attachment> attachments);
}
