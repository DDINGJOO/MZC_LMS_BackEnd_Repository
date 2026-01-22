package com.mzc.lms.messaging.adapter.out.persistence.repository;

import com.mzc.lms.messaging.adapter.out.persistence.entity.ChatRoomEntity;
import com.mzc.lms.messaging.domain.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, String> {

    @Query("SELECT r FROM ChatRoomEntity r WHERE :userId MEMBER OF r.participantIds ORDER BY r.lastMessageAt DESC NULLS LAST")
    List<ChatRoomEntity> findByParticipantId(@Param("userId") Long userId);

    List<ChatRoomEntity> findByCourseIdOrderByCreatedAtDesc(Long courseId);

    @Query("SELECT r FROM ChatRoomEntity r WHERE r.roomType = :roomType AND :userId MEMBER OF r.participantIds")
    List<ChatRoomEntity> findByRoomTypeAndParticipantId(@Param("roomType") RoomType roomType, @Param("userId") Long userId);

    @Query("SELECT r FROM ChatRoomEntity r WHERE r.roomType = 'DIRECT' AND :user1Id MEMBER OF r.participantIds AND :user2Id MEMBER OF r.participantIds")
    Optional<ChatRoomEntity> findDirectRoom(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
