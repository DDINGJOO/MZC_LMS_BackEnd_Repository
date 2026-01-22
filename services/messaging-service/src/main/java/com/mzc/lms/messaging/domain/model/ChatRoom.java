package com.mzc.lms.messaging.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class ChatRoom {

    private String id;
    private String name;
    private RoomType roomType;
    private Long courseId;
    private Long creatorId;
    private Set<Long> participantIds;
    private String lastMessagePreview;
    private LocalDateTime lastMessageAt;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatRoom createDirect(Long user1Id, Long user2Id) {
        Set<Long> participants = new HashSet<>();
        participants.add(user1Id);
        participants.add(user2Id);

        return ChatRoom.builder()
                .roomType(RoomType.DIRECT)
                .creatorId(user1Id)
                .participantIds(participants)
                .isActive(true)
                .build();
    }

    public static ChatRoom createGroup(String name, Long creatorId, Set<Long> participantIds) {
        Set<Long> participants = new HashSet<>(participantIds);
        participants.add(creatorId);

        return ChatRoom.builder()
                .name(name)
                .roomType(RoomType.GROUP)
                .creatorId(creatorId)
                .participantIds(participants)
                .isActive(true)
                .build();
    }

    public static ChatRoom createCourseRoom(String name, Long courseId, Long creatorId) {
        Set<Long> participants = new HashSet<>();
        participants.add(creatorId);

        return ChatRoom.builder()
                .name(name)
                .roomType(RoomType.COURSE)
                .courseId(courseId)
                .creatorId(creatorId)
                .participantIds(participants)
                .isActive(true)
                .build();
    }

    public ChatRoom addParticipant(Long userId) {
        Set<Long> newParticipants = new HashSet<>(this.participantIds);
        newParticipants.add(userId);

        return ChatRoom.builder()
                .id(this.id)
                .name(this.name)
                .roomType(this.roomType)
                .courseId(this.courseId)
                .creatorId(this.creatorId)
                .participantIds(newParticipants)
                .lastMessagePreview(this.lastMessagePreview)
                .lastMessageAt(this.lastMessageAt)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .build();
    }

    public ChatRoom removeParticipant(Long userId) {
        Set<Long> newParticipants = new HashSet<>(this.participantIds);
        newParticipants.remove(userId);

        return ChatRoom.builder()
                .id(this.id)
                .name(this.name)
                .roomType(this.roomType)
                .courseId(this.courseId)
                .creatorId(this.creatorId)
                .participantIds(newParticipants)
                .lastMessagePreview(this.lastMessagePreview)
                .lastMessageAt(this.lastMessageAt)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .build();
    }

    public ChatRoom updateLastMessage(String preview, LocalDateTime timestamp) {
        return ChatRoom.builder()
                .id(this.id)
                .name(this.name)
                .roomType(this.roomType)
                .courseId(this.courseId)
                .creatorId(this.creatorId)
                .participantIds(this.participantIds)
                .lastMessagePreview(preview)
                .lastMessageAt(timestamp)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .build();
    }

    public boolean isParticipant(Long userId) {
        return participantIds != null && participantIds.contains(userId);
    }
}
