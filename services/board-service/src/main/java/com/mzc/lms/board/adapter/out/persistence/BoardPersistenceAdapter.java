package com.mzc.lms.board.adapter.out.persistence;

import com.mzc.lms.board.adapter.out.persistence.entity.BoardEntity;
import com.mzc.lms.board.adapter.out.persistence.repository.BoardJpaRepository;
import com.mzc.lms.board.application.port.out.BoardRepository;
import com.mzc.lms.board.domain.model.Board;
import com.mzc.lms.board.domain.model.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements BoardRepository {

    private final BoardJpaRepository boardJpaRepository;

    @Override
    public Board save(Board board) {
        BoardEntity entity = BoardEntity.fromDomain(board);
        BoardEntity savedEntity = boardJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardJpaRepository.findById(id).map(BoardEntity::toDomain);
    }

    @Override
    public List<Board> findByType(BoardType type) {
        return boardJpaRepository.findByType(type).stream()
                .map(BoardEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Board> findByCourseId(Long courseId) {
        return boardJpaRepository.findByCourseId(courseId).stream()
                .map(BoardEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Board> findByIsActiveTrue() {
        return boardJpaRepository.findByIsActiveTrue().stream()
                .map(BoardEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        boardJpaRepository.deleteById(id);
    }
}
