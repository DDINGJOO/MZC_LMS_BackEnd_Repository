package com.mzc.backend.lms.domains.board.adapter.out.persistence;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.BoardCategory;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.repository.PostRepositoryJpa;
import com.mzc.backend.lms.domains.board.application.port.out.PostRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Post 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostRepositoryPort {

    private final PostRepositoryJpa postRepositoryJpa;

    @Override
    public Post save(Post post) {
        return postRepositoryJpa.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepositoryJpa.findById(id);
    }

    @Override
    public Optional<Post> findWithAllById(Long id) {
        return postRepositoryJpa.findById(id);
    }

    @Override
    public List<Post> findByCategory(BoardCategory category) {
        return postRepositoryJpa.findByCategory(category);
    }

    @Override
    public Page<Post> findByCategory(BoardCategory category, Pageable pageable) {
        return postRepositoryJpa.findByCategory(category, pageable);
    }

    @Override
    public Page<Post> findByTitleContaining(String title, Pageable pageable) {
        return postRepositoryJpa.findByTitleContaining(title, pageable);
    }

    @Override
    public Page<Post> findByCategoryAndTitleContaining(BoardCategory category, String title, Pageable pageable) {
        return postRepositoryJpa.findByCategoryAndTitleContaining(category, title, pageable);
    }

    @Override
    public Page<Post> findByCategoryAndHashtagName(BoardCategory category, String hashtagName, Pageable pageable) {
        return postRepositoryJpa.findByCategoryAndHashtagName(category, hashtagName, pageable);
    }

    @Override
    public Page<Post> findByCategoryAndTitleContainingAndHashtagName(BoardCategory category, String title, String hashtagName, Pageable pageable) {
        return postRepositoryJpa.findByCategoryAndTitleContainingAndHashtagName(category, title, hashtagName, pageable);
    }

    @Override
    public Page<Post> findByCategoryAndDepartmentId(BoardCategory category, Long departmentId, Pageable pageable) {
        return postRepositoryJpa.findByCategoryAndDepartmentId(category, departmentId, pageable);
    }

    @Override
    public Page<Post> findByCategoryAndDepartmentIdAndTitleContaining(BoardCategory category, Long departmentId, String title, Pageable pageable) {
        return postRepositoryJpa.findByCategoryAndDepartmentIdAndTitleContaining(category, departmentId, title, pageable);
    }

    @Override
    public Optional<Post> findByIdWithHashtags(Long postId) {
        return postRepositoryJpa.findByIdWithHashtags(postId);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepositoryJpa.findAll(pageable);
    }
}
