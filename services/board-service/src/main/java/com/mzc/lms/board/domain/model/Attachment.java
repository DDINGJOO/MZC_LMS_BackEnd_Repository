package com.mzc.lms.board.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Attachment {

    private Long id;
    private Long postId;
    private String originalFileName;
    private String storedFileName;
    private String storagePath;
    private String contentType;
    private Long fileSize;
    private String downloadUrl;
    private Integer downloadCount;
    private Long uploadedBy;
    private LocalDateTime createdAt;

    public static Attachment create(Long postId, String originalFileName, String storedFileName,
                                    String storagePath, String contentType, Long fileSize,
                                    String downloadUrl, Long uploadedBy) {
        return Attachment.builder()
                .postId(postId)
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .storagePath(storagePath)
                .contentType(contentType)
                .fileSize(fileSize)
                .downloadUrl(downloadUrl)
                .downloadCount(0)
                .uploadedBy(uploadedBy)
                .build();
    }

    public Attachment incrementDownloadCount() {
        return Attachment.builder()
                .id(this.id)
                .postId(this.postId)
                .originalFileName(this.originalFileName)
                .storedFileName(this.storedFileName)
                .storagePath(this.storagePath)
                .contentType(this.contentType)
                .fileSize(this.fileSize)
                .downloadUrl(this.downloadUrl)
                .downloadCount(this.downloadCount + 1)
                .uploadedBy(this.uploadedBy)
                .createdAt(this.createdAt)
                .build();
    }

    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }

    public boolean isVideo() {
        return contentType != null && contentType.startsWith("video/");
    }

    public boolean isDocument() {
        if (contentType == null) return false;
        return contentType.equals("application/pdf") ||
               contentType.equals("application/msword") ||
               contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
               contentType.equals("application/vnd.ms-excel") ||
               contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
               contentType.equals("application/vnd.ms-powerpoint") ||
               contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }

    public String getFileExtension() {
        if (originalFileName == null) return "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return originalFileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
}
