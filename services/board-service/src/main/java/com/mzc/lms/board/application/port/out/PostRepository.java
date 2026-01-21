package com.mzc.lms.board.application.port.out;

import com.mzc.lms.board.domain.model.Post;
import com.mzc.lms.board.domain.model.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    Page<Post> findByBoardId(Long boardId, Pageable pageable);

    Page<Post> findByBoardIdAndStatus(Long boardId, PostStatus status, Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    List<Post> findByBoardIdAndIsPinnedTrue(Long boardId);

    Page<Post> searchByBoardIdAndKeyword(Long boardId, String keyword, Pageable pageable);

    void deleteById(Long id);
}
