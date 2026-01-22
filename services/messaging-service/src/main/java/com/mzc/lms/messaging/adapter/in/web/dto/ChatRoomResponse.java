package com.mzc.lms.messaging.adapter.in.web.dto;

import com.mzc.lms.messaging.domain.model.ChatRoom;
import com.mzc.lms.messaging.domain.model.RoomType;

import java.time.LocalDateTime;
import java.util.Set;

public class ChatRoomResponse {

    private String id;
    private String name;
    private RoomType roomType;
    private Long courseId;
    private Long creatorId;
    private Set<Long> participantIds;
    private String lastMessagePreview;
    private LocalDateTime lastMessageAt;
    private int unreadCount;
    private LocalDateTime createdAt;

    public ChatRoomResponse() {}

    public static ChatRoomResponse from(ChatRoom room) {
        return from(room, 0);
    }

    public static ChatRoomResponse from(ChatRoom room, int unreadCount) {
        ChatRoomResponse response = new ChatRoomResponse();
        response.id = room.getId();
        response.name = room.getName();
        response.roomType = room.getRoomType();
        response.courseId = room.getCourseId();
        response.creatorId = room.getCreatorId();
        response.participantIds = room.getParticipantIds();
        response.lastMessagePreview = room.getLastMessagePreview();
        response.lastMessageAt = room.getLastMessageAt();
        response.unreadCount = unreadCount;
        response.createdAt = room.getCreatedAt();
        return response;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public RoomType getRoomType() { return roomType; }
    public Long getCourseId() { return courseId; }
    public Long getCreatorId() { return creatorId; }
    public Set<Long> getParticipantIds() { return participantIds; }
    public String getLastMessagePreview() { return lastMessagePreview; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public int getUnreadCount() { return unreadCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
