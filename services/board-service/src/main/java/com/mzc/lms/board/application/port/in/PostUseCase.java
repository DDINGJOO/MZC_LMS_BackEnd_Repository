package com.mzc.lms.board.application.port.in;

import com.mzc.lms.board.domain.model.Post;
import com.mzc.lms.board.domain.model.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostUseCase {

    Post createPost(Long boardId, Long authorId, String authorName,
                    String title, String content, Boolean isAnonymous, Long parentId);

    Post updatePost(Long id, String title, String content);

    Post publishPost(Long id);

    Post approvePost(Long id);

    Post hidePost(Long id);

    Post deletePost(Long id);

    Post pinPost(Long id);

    Post unpinPost(Long id);

    Post incrementViewCount(Long id);

    Optional<Post> findById(Long id);

    Page<Post> findByBoardId(Long boardId, Pageable pageable);

    Page<Post> findByBoardIdAndStatus(Long boardId, PostStatus status, Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    List<Post> findPinnedPosts(Long boardId);

    Page<Post> searchPosts(Long boardId, String keyword, Pageable pageable);
}
