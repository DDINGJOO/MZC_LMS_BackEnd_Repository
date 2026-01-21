package com.mzc.lms.board.application.service;

import com.mzc.lms.board.application.port.in.PostUseCase;
import com.mzc.lms.board.application.port.out.BoardEventPublisher;
import com.mzc.lms.board.application.port.out.BoardRepository;
import com.mzc.lms.board.application.port.out.PostRepository;
import com.mzc.lms.board.domain.event.BoardEvent;
import com.mzc.lms.board.domain.model.Board;
import com.mzc.lms.board.domain.model.Post;
import com.mzc.lms.board.domain.model.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService implements PostUseCase {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final BoardEventPublisher eventPublisher;

    @Override
    public Post createPost(Long boardId, Long authorId, String authorName,
                          String title, String content, Boolean isAnonymous, Long parentId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다: " + boardId));

        Post post = Post.create(boardId, authorId, authorName, title, content, isAnonymous, parentId);
        Post savedPost = postRepository.save(post);

        eventPublisher.publish(BoardEvent.postCreated(savedPost.getId(), boardId, authorId, title));

        return savedPost;
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public Post updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));
        Post updatedPost = post.update(title, content);
        return postRepository.save(updatedPost);
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public Post publishPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));

        Board board = boardRepository.findById(post.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다: " + post.getBoardId()));

        Post publishedPost = post.publish(board.getRequireApproval());
        Post savedPost = postRepository.save(publishedPost);

        if (savedPost.getStatus() == PostStatus.PUBLISHED) {
            eventPublisher.publish(BoardEvent.postPublished(savedPost.getId(), savedPost.getBoardId(),
                    savedPost.getAuthorId(), savedPost.getTitle()));
        }

        return savedPost;
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public Post approvePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));

        if (post.getStatus() != PostStatus.PENDING) {
            throw new IllegalStateException("승인 대기 상태가 아닙니다");
        }

        Post approvedPost = post.approve();
        Post savedPost = postRepository.save(approvedPost);

        eventPublisher.publish(BoardEvent.postPublished(savedPost.getId(), savedPost.getBoardId(),
                savedPost.getAuthorId(), savedPost.getTitle()));

        return savedPost;
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public Post hidePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));
        Post hiddenPost = post.hide();
        return postRepository.save(hiddenPost);
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public Post deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));
        Post deletedPost = post.delete();
        return postRepository.save(deletedPost);
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public Post pinPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));
        Post pinnedPost = post.pin();
        return postRepository.save(pinnedPost);
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public Post unpinPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));
        Post unpinnedPost = post.unpin();
        return postRepository.save(unpinnedPost);
    }

    @Override
    public Post incrementViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + id));
        Post viewedPost = post.incrementViewCount();
        return postRepository.save(viewedPost);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "#id")
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByBoardId(Long boardId, Pageable pageable) {
        return postRepository.findByBoardId(boardId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByBoardIdAndStatus(Long boardId, PostStatus status, Pageable pageable) {
        return postRepository.findByBoardIdAndStatus(boardId, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByAuthorId(Long authorId, Pageable pageable) {
        return postRepository.findByAuthorId(authorId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findPinnedPosts(Long boardId) {
        return postRepository.findByBoardIdAndIsPinnedTrue(boardId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> searchPosts(Long boardId, String keyword, Pageable pageable) {
        return postRepository.searchByBoardIdAndKeyword(boardId, keyword, pageable);
    }
}
