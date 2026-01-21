package com.mzc.lms.board.adapter.out.persistence.repository;

import com.mzc.lms.board.adapter.out.persistence.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentJpaRepository extends JpaRepository<AttachmentEntity, Long> {

    List<AttachmentEntity> findByPostId(Long postId);

    void deleteByPostId(Long postId);
}
