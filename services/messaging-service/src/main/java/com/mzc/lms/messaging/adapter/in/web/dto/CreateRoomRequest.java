package com.mzc.lms.messaging.adapter.in.web.dto;

import com.mzc.lms.messaging.domain.model.RoomType;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class CreateRoomRequest {

    @NotNull(message = "방 유형은 필수입니다")
    private RoomType roomType;

    private String name;

    private Long courseId;

    @NotNull(message = "생성자 ID는 필수입니다")
    private Long creatorId;

    private Set<Long> participantIds;

    public CreateRoomRequest() {}

    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public Set<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(Set<Long> participantIds) { this.participantIds = participantIds; }
}
