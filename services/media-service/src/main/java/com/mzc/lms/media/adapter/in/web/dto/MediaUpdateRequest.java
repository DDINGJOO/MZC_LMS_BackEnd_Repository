package com.mzc.lms.media.adapter.in.web.dto;

public class MediaUpdateRequest {

    private String description;

    public MediaUpdateRequest() {}

    public MediaUpdateRequest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
