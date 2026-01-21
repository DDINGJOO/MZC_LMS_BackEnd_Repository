package com.mzc.lms.board.adapter.out.persistence.repository;

import com.mzc.lms.board.adapter.out.persistence.entity.BoardEntity;
import com.mzc.lms.board.domain.model.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByType(BoardType type);

    List<BoardEntity> findByCourseId(Long courseId);

    List<BoardEntity> findByIsActiveTrue();
}
