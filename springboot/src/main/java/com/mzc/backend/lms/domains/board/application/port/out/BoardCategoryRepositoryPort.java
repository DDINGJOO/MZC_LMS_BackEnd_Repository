package com.mzc.backend.lms.domains.board.application.port.out;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.BoardCategory;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.enums.BoardType;

import java.util.Optional;

/**
 * BoardCategory 영속성을 위한 Port
 */
public interface BoardCategoryRepositoryPort {

    /**
     * 게시판 카테고리 저장
     */
    BoardCategory save(BoardCategory boardCategory);

    /**
     * ID로 게시판 카테고리 조회
     */
    Optional<BoardCategory> findById(Long id);

    /**
     * 게시판 유형으로 카테고리 조회
     */
    Optional<BoardCategory> findByBoardType(BoardType boardType);
}
