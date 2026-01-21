package com.mzc.lms.board.adapter.in.web.dto;

import com.mzc.lms.board.domain.model.Attachment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttachmentResponse {

    private Long id;
    private Long postId;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String downloadUrl;
    private Integer downloadCount;
    private Boolean isImage;
    private Boolean isVideo;
    private Boolean isDocument;
    private LocalDateTime createdAt;

    public static AttachmentResponse from(Attachment attachment) {
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .postId(attachment.getPostId())
                .originalFileName(attachment.getOriginalFileName())
                .contentType(attachment.getContentType())
                .fileSize(attachment.getFileSize())
                .downloadUrl(attachment.getDownloadUrl())
                .downloadCount(attachment.getDownloadCount())
                .isImage(attachment.isImage())
                .isVideo(attachment.isVideo())
                .isDocument(attachment.isDocument())
                .createdAt(attachment.getCreatedAt())
                .build();
    }
}
