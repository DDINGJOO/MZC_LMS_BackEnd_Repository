package com.mzc.lms.messaging.adapter.in.web;

import com.mzc.lms.messaging.adapter.in.web.dto.ChatRoomResponse;
import com.mzc.lms.messaging.adapter.in.web.dto.CreateRoomRequest;
import com.mzc.lms.messaging.application.port.in.ChatRoomUseCase;
import com.mzc.lms.messaging.application.port.in.MessageUseCase;
import com.mzc.lms.messaging.domain.model.ChatRoom;
import com.mzc.lms.messaging.domain.model.RoomType;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {

    private static final Logger log = LoggerFactory.getLogger(ChatRoomController.class);

    private final ChatRoomUseCase chatRoomUseCase;
    private final MessageUseCase messageUseCase;

    public ChatRoomController(ChatRoomUseCase chatRoomUseCase, MessageUseCase messageUseCase) {
        this.chatRoomUseCase = chatRoomUseCase;
        this.messageUseCase = messageUseCase;
    }

    @PostMapping
    public ResponseEntity<ChatRoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        log.info("Create room request: type={}, creator={}", request.getRoomType(), request.getCreatorId());

        ChatRoom room;

        switch (request.getRoomType()) {
            case DIRECT -> {
                if (request.getParticipantIds() == null || request.getParticipantIds().size() != 1) {
                    return ResponseEntity.badRequest().build();
                }
                Long otherUserId = request.getParticipantIds().iterator().next();
                room = chatRoomUseCase.createDirectRoom(request.getCreatorId(), otherUserId);
            }
            case GROUP -> {
                Set<Long> participants = request.getParticipantIds() != null ?
                        request.getParticipantIds() : Set.of();
                room = chatRoomUseCase.createGroupRoom(request.getName(), request.getCreatorId(), participants);
            }
            case COURSE -> {
                if (request.getCourseId() == null) {
                    return ResponseEntity.badRequest().build();
                }
                room = chatRoomUseCase.createCourseRoom(request.getName(), request.getCourseId(), request.getCreatorId());
            }
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ChatRoomResponse.from(room));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponse> getRoom(@PathVariable String roomId,
                                                    @RequestParam(required = false) Long userId) {
        return chatRoomUseCase.getRoom(roomId)
                .map(room -> {
                    int unreadCount = userId != null ? messageUseCase.getUnreadCount(roomId, userId) : 0;
                    return ResponseEntity.ok(ChatRoomResponse.from(room, unreadCount));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoomResponse>> getUserRooms(@PathVariable Long userId) {
        List<ChatRoom> rooms = chatRoomUseCase.getUserRooms(userId);

        List<ChatRoomResponse> responses = rooms.stream()
                .map(room -> {
                    int unreadCount = messageUseCase.getUnreadCount(room.getId(), userId);
                    return ChatRoomResponse.from(room, unreadCount);
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ChatRoomResponse>> getCourseRooms(@PathVariable Long courseId) {
        List<ChatRoom> rooms = chatRoomUseCase.getCourseRooms(courseId);
        List<ChatRoomResponse> responses = rooms.stream()
                .map(ChatRoomResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/direct")
    public ResponseEntity<ChatRoomResponse> findDirectRoom(@RequestParam Long user1Id,
                                                           @RequestParam Long user2Id) {
        return chatRoomUseCase.findDirectRoom(user1Id, user2Id)
                .map(room -> ResponseEntity.ok(ChatRoomResponse.from(room)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{roomId}/participants/{userId}")
    public ResponseEntity<ChatRoomResponse> addParticipant(@PathVariable String roomId,
                                                           @PathVariable Long userId) {
        ChatRoom room = chatRoomUseCase.addParticipant(roomId, userId);
        return ResponseEntity.ok(ChatRoomResponse.from(room));
    }

    @DeleteMapping("/{roomId}/participants/{userId}")
    public ResponseEntity<ChatRoomResponse> removeParticipant(@PathVariable String roomId,
                                                               @PathVariable Long userId) {
        ChatRoom room = chatRoomUseCase.removeParticipant(roomId, userId);
        return ResponseEntity.ok(ChatRoomResponse.from(room));
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(@PathVariable String roomId,
                                          @RequestParam Long userId) {
        chatRoomUseCase.leaveRoom(roomId, userId);
        return ResponseEntity.noContent().build();
    }
}
