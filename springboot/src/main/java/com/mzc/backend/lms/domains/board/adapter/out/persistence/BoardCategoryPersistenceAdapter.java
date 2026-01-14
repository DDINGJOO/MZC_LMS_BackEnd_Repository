package com.mzc.backend.lms.domains.board.adapter.out.persistence;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.BoardCategory;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.enums.BoardType;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.repository.BoardCategoryRepositoryJpa;
import com.mzc.backend.lms.domains.board.application.port.out.BoardCategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * BoardCategory 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class BoardCategoryPersistenceAdapter implements BoardCategoryRepositoryPort {

    private final BoardCategoryRepositoryJpa boardCategoryRepositoryJpa;

    @Override
    public BoardCategory save(BoardCategory boardCategory) {
        return boardCategoryRepositoryJpa.save(boardCategory);
    }

    @Override
    public Optional<BoardCategory> findById(Long id) {
        return boardCategoryRepositoryJpa.findById(id);
    }

    @Override
    public Optional<BoardCategory> findByBoardType(BoardType boardType) {
        return boardCategoryRepositoryJpa.findByBoardType(boardType);
    }
}
