package com.mzc.lms.media.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

public class MediaUploadRequest {

    @NotNull(message = "업로더 ID는 필수입니다")
    private Long uploadedBy;

    private String uploadedByName;

    private String description;

    public MediaUploadRequest() {}

    public MediaUploadRequest(Long uploadedBy, String uploadedByName, String description) {
        this.uploadedBy = uploadedBy;
        this.uploadedByName = uploadedByName;
        this.description = description;
    }

    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }

    public String getUploadedByName() { return uploadedByName; }
    public void setUploadedByName(String uploadedByName) { this.uploadedByName = uploadedByName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
