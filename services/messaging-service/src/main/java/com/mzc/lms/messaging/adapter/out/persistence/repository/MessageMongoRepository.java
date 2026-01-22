package com.mzc.lms.messaging.adapter.out.persistence.repository;

import com.mzc.lms.messaging.adapter.out.persistence.entity.MessageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMongoRepository extends MongoRepository<MessageDocument, String> {

    List<MessageDocument> findByRoomIdOrderByCreatedAtDesc(String roomId, Pageable pageable);

    @Query("{'roomId': ?0, '_id': {'$lt': ?1}}")
    List<MessageDocument> findByRoomIdAndIdLessThanOrderByCreatedAtDesc(String roomId, String beforeId, Pageable pageable);

    @Query("{'roomId': ?0, 'readByUserIds': {'$ne': ?1}}")
    List<MessageDocument> findUnreadByRoomIdAndUserId(String roomId, Long userId);

    @Query(value = "{'roomId': ?0, 'readByUserIds': {'$ne': ?1}}", count = true)
    int countUnreadByRoomIdAndUserId(String roomId, Long userId);

    void deleteByRoomId(String roomId);
}
