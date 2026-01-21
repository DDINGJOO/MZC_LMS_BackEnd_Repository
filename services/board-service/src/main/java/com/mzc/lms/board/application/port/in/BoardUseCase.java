package com.mzc.lms.board.application.port.in;

import com.mzc.lms.board.domain.model.Board;
import com.mzc.lms.board.domain.model.BoardType;

import java.util.List;
import java.util.Optional;

public interface BoardUseCase {

    Board createBoard(String name, String description, BoardType type,
                      Long courseId, Boolean allowAnonymous, Boolean requireApproval);

    Board updateBoard(Long id, String name, String description,
                      Boolean allowAnonymous, Boolean requireApproval, Integer displayOrder);

    Board activateBoard(Long id);

    Board deactivateBoard(Long id);

    Optional<Board> findById(Long id);

    List<Board> findByType(BoardType type);

    List<Board> findByCourseId(Long courseId);

    List<Board> findAllActive();

    void deleteBoard(Long id);
}
