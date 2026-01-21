package com.mzc.lms.board.application.port.out;

import com.mzc.lms.board.domain.model.Board;
import com.mzc.lms.board.domain.model.BoardType;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Board save(Board board);

    Optional<Board> findById(Long id);

    List<Board> findByType(BoardType type);

    List<Board> findByCourseId(Long courseId);

    List<Board> findByIsActiveTrue();

    void deleteById(Long id);
}
