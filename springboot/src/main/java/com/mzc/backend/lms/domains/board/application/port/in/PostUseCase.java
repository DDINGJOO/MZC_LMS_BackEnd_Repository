package com.mzc.backend.lms.domains.board.application.port.in;

import com.mzc.backend.lms.domains.board.adapter.in.web.dto.request.PostCreateRequestDto;
import com.mzc.backend.lms.domains.board.adapter.in.web.dto.request.PostUpdateRequestDto;
import com.mzc.backend.lms.domains.board.adapter.in.web.dto.response.PostListResponseDto;
import com.mzc.backend.lms.domains.board.adapter.in.web.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시글 UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface PostUseCase {

    /**
     * 게시글 생성 (boardType 기반, 2단계 업로드)
     */
    PostResponseDto createPost(String boardTypeStr, PostCreateRequestDto request, Long authorId);

    /**
     * 게시글 목록 조회 (boardType 기반, 해시태그 필터링 지원)
     */
    Page<PostListResponseDto> getPostListByBoardType(String boardTypeStr, String search, String hashtag, Pageable pageable, Long currentUserId);

    /**
     * 게시글 상세 조회 (조회수 증가)
     */
    PostResponseDto getPost(String boardTypeStr, Long postId, Long currentUserId);

    /**
     * 게시글 상세 조회 (조회수 증가) - 레거시 메서드
     * @deprecated boardType을 받는 getPost(String, Long) 사용 권장
     */
    @Deprecated
    PostResponseDto getPost(Long postId);

    /**
     * 게시글 목록 조회 (페이징 + 검색)
     */
    Page<PostListResponseDto> getPostList(Long categoryId, String keyword, Pageable pageable);

    /**
     * 게시글 수정
     */
    PostResponseDto updatePost(Long postId, PostUpdateRequestDto request, List<MultipartFile> files, Long updatedBy);

    /**
     * 게시글 삭제 (Soft Delete)
     */
    void deletePost(Long postId);

    /**
     * 게시글 좋아요 토글 (중복 방지)
     */
    boolean toggleLike(Long postId, Long userId);

    /**
     * 사용자의 게시글 좋아요 여부 조회
     */
    boolean isLikedByUser(Long postId, Long userId);
}
