package com.mzc.lms.board.adapter.out.persistence.entity;

import com.mzc.lms.board.domain.model.Attachment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments",
        indexes = {
                @Index(name = "idx_attachment_post", columnList = "post_id"),
                @Index(name = "idx_attachment_uploader", columnList = "uploaded_by")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "stored_file_name", nullable = false, length = 255)
    private String storedFileName;

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "download_url", length = 500)
    private String downloadUrl;

    @Column(name = "download_count")
    private Integer downloadCount;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static AttachmentEntity fromDomain(Attachment attachment) {
        return AttachmentEntity.builder()
                .id(attachment.getId())
                .postId(attachment.getPostId())
                .originalFileName(attachment.getOriginalFileName())
                .storedFileName(attachment.getStoredFileName())
                .storagePath(attachment.getStoragePath())
                .contentType(attachment.getContentType())
                .fileSize(attachment.getFileSize())
                .downloadUrl(attachment.getDownloadUrl())
                .downloadCount(attachment.getDownloadCount())
                .uploadedBy(attachment.getUploadedBy())
                .createdAt(attachment.getCreatedAt())
                .build();
    }

    public Attachment toDomain() {
        return Attachment.builder()
                .id(this.id)
                .postId(this.postId)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .contentType(this.contentType)
                .fileSize(this.fileSize)
                .downloadUrl(this.downloadUrl)
                .downloadCount(this.downloadCount)
                .uploadedBy(this.uploadedBy)
                .createdAt(this.createdAt)
                .build();
    }
}
