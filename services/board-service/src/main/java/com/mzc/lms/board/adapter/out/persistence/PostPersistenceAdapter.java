package com.mzc.lms.board.adapter.out.persistence;

import com.mzc.lms.board.adapter.out.persistence.entity.PostEntity;
import com.mzc.lms.board.adapter.out.persistence.repository.PostJpaRepository;
import com.mzc.lms.board.application.port.out.PostRepository;
import com.mzc.lms.board.domain.model.Post;
import com.mzc.lms.board.domain.model.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        PostEntity entity = PostEntity.fromDomain(post);
        PostEntity savedEntity = postJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id).map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByBoardId(Long boardId, Pageable pageable) {
        return postJpaRepository.findByBoardIdOrderByCreatedAtDesc(boardId, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByBoardIdAndStatus(Long boardId, PostStatus status, Pageable pageable) {
        return postJpaRepository.findByBoardIdAndStatusOrderByCreatedAtDesc(boardId, status, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByAuthorId(Long authorId, Pageable pageable) {
        return postJpaRepository.findByAuthorIdOrderByCreatedAtDesc(authorId, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public List<Post> findByBoardIdAndIsPinnedTrue(Long boardId) {
        return postJpaRepository.findByBoardIdAndIsPinnedTrueOrderByCreatedAtDesc(boardId).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Post> searchByBoardIdAndKeyword(Long boardId, String keyword, Pageable pageable) {
        return postJpaRepository.searchByBoardIdAndKeyword(boardId, keyword, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
    }
}
