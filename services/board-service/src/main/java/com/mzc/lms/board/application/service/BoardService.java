package com.mzc.lms.board.application.service;

import com.mzc.lms.board.application.port.in.BoardUseCase;
import com.mzc.lms.board.application.port.out.BoardRepository;
import com.mzc.lms.board.domain.model.Board;
import com.mzc.lms.board.domain.model.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService implements BoardUseCase {

    private final BoardRepository boardRepository;

    @Override
    public Board createBoard(String name, String description, BoardType type,
                            Long courseId, Boolean allowAnonymous, Boolean requireApproval) {
        Board board = Board.create(name, description, type, courseId, allowAnonymous, requireApproval);
        return boardRepository.save(board);
    }

    @Override
    @CacheEvict(value = "boards", key = "#id")
    public Board updateBoard(Long id, String name, String description,
                            Boolean allowAnonymous, Boolean requireApproval, Integer displayOrder) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다: " + id));
        Board updatedBoard = board.update(name, description, allowAnonymous, requireApproval, displayOrder);
        return boardRepository.save(updatedBoard);
    }

    @Override
    @CacheEvict(value = "boards", key = "#id")
    public Board activateBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다: " + id));
        Board activatedBoard = board.activate();
        return boardRepository.save(activatedBoard);
    }

    @Override
    @CacheEvict(value = "boards", key = "#id")
    public Board deactivateBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다: " + id));
        Board deactivatedBoard = board.deactivate();
        return boardRepository.save(deactivatedBoard);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "boards", key = "#id")
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> findByType(BoardType type) {
        return boardRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> findByCourseId(Long courseId) {
        return boardRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "activeBoards")
    public List<Board> findAllActive() {
        return boardRepository.findByIsActiveTrue();
    }

    @Override
    @CacheEvict(value = {"boards", "activeBoards"}, allEntries = true)
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
