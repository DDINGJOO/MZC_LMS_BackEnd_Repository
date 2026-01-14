package com.mzc.backend.lms.domains.board.adapter.out.persistence;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.PostLike;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.repository.PostLikeRepositoryJpa;
import com.mzc.backend.lms.domains.board.application.port.out.PostLikeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * PostLike 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class PostLikePersistenceAdapter implements PostLikeRepositoryPort {

    private final PostLikeRepositoryJpa postLikeRepositoryJpa;

    @Override
    public PostLike save(PostLike postLike) {
        return postLikeRepositoryJpa.save(postLike);
    }

    @Override
    public Optional<PostLike> findById(Long id) {
        return postLikeRepositoryJpa.findById(id);
    }

    @Override
    public Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId) {
        return postLikeRepositoryJpa.findByUserIdAndPostId(userId, postId);
    }

    @Override
    public boolean existsByUserIdAndPostId(Long userId, Long postId) {
        return postLikeRepositoryJpa.existsByUserIdAndPostId(userId, postId);
    }

    @Override
    public List<Long> findLikedPostIdsByUserIdAndPostIds(Long userId, List<Long> postIds) {
        return postLikeRepositoryJpa.findLikedPostIdsByUserIdAndPostIds(userId, postIds);
    }

    @Override
    public long countByPostId(Long postId) {
        return postLikeRepositoryJpa.countByPostId(postId);
    }

    @Override
    public void deleteByPostId(Long postId) {
        postLikeRepositoryJpa.deleteByPostId(postId);
    }

    @Override
    public void delete(PostLike postLike) {
        postLikeRepositoryJpa.delete(postLike);
    }
}
