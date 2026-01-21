package com.mzc.lms.board.adapter.in.web;

import com.mzc.lms.board.adapter.in.web.dto.*;
import com.mzc.lms.board.application.port.in.*;
import com.mzc.lms.board.application.port.out.FileStoragePort;
import com.mzc.lms.board.domain.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시판 관리 API")
public class BoardController {

    private final BoardUseCase boardUseCase;
    private final PostUseCase postUseCase;
    private final CommentUseCase commentUseCase;
    private final AttachmentUseCase attachmentUseCase;
    private final FileStoragePort fileStoragePort;

    // ========== Board APIs ==========

    @PostMapping
    @Operation(summary = "게시판 생성", description = "새로운 게시판을 생성합니다")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        Board board = boardUseCase.createBoard(
                request.getName(),
                request.getDescription(),
                request.getType(),
                request.getCourseId(),
                request.getAllowAnonymous(),
                request.getRequireApproval()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(BoardResponse.from(board));
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시판 조회", description = "게시판 ID로 게시판을 조회합니다")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id) {
        return boardUseCase.findById(id)
                .map(board -> ResponseEntity.ok(BoardResponse.from(board)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "활성 게시판 목록 조회", description = "활성 상태인 게시판 목록을 조회합니다")
    public ResponseEntity<List<BoardResponse>> getActiveBoards() {
        List<BoardResponse> boards = boardUseCase.findAllActive().stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "유형별 게시판 조회", description = "게시판 유형으로 조회합니다")
    public ResponseEntity<List<BoardResponse>> getBoardsByType(@PathVariable BoardType type) {
        List<BoardResponse> boards = boardUseCase.findByType(type).stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "코스별 게시판 조회", description = "코스 ID로 게시판을 조회합니다")
    public ResponseEntity<List<BoardResponse>> getBoardsByCourse(@PathVariable Long courseId) {
        List<BoardResponse> boards = boardUseCase.findByCourseId(courseId).stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(boards);
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시판 수정", description = "게시판 정보를 수정합니다")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean allowAnonymous,
            @RequestParam(required = false) Boolean requireApproval,
            @RequestParam(required = false) Integer displayOrder) {
        Board board = boardUseCase.updateBoard(id, name, description, allowAnonymous, requireApproval, displayOrder);
        return ResponseEntity.ok(BoardResponse.from(board));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "게시판 활성화", description = "게시판을 활성화합니다")
    public ResponseEntity<BoardResponse> activateBoard(@PathVariable Long id) {
        Board board = boardUseCase.activateBoard(id);
        return ResponseEntity.ok(BoardResponse.from(board));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "게시판 비활성화", description = "게시판을 비활성화합니다")
    public ResponseEntity<BoardResponse> deactivateBoard(@PathVariable Long id) {
        Board board = boardUseCase.deactivateBoard(id);
        return ResponseEntity.ok(BoardResponse.from(board));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시판 삭제", description = "게시판을 삭제합니다")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardUseCase.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    // ========== Post APIs ==========

    @PostMapping("/posts")
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        Post post = postUseCase.createPost(
                request.getBoardId(),
                request.getAuthorId(),
                request.getAuthorName(),
                request.getTitle(),
                request.getContent(),
                request.getIsAnonymous(),
                request.getParentId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(post));
    }

    @GetMapping("/posts/{id}")
    @Operation(summary = "게시글 조회", description = "게시글 ID로 조회합니다")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return postUseCase.findById(id)
                .map(post -> ResponseEntity.ok(PostResponse.from(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{boardId}/posts")
    @Operation(summary = "게시판별 게시글 목록 조회", description = "게시판 ID로 게시글 목록을 조회합니다")
    public ResponseEntity<Page<PostResponse>> getPostsByBoard(
            @PathVariable Long boardId,
            @RequestParam(required = false) PostStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Post> posts;
        if (status != null) {
            posts = postUseCase.findByBoardIdAndStatus(boardId, status, pageable);
        } else {
            posts = postUseCase.findByBoardId(boardId, pageable);
        }
        return ResponseEntity.ok(posts.map(PostResponse::from));
    }

    @GetMapping("/{boardId}/posts/pinned")
    @Operation(summary = "고정 게시글 조회", description = "고정된 게시글 목록을 조회합니다")
    public ResponseEntity<List<PostResponse>> getPinnedPosts(@PathVariable Long boardId) {
        List<PostResponse> posts = postUseCase.findPinnedPosts(boardId).stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{boardId}/posts/search")
    @Operation(summary = "게시글 검색", description = "키워드로 게시글을 검색합니다")
    public ResponseEntity<Page<PostResponse>> searchPosts(
            @PathVariable Long boardId,
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PostResponse> posts = postUseCase.searchPosts(boardId, keyword, pageable)
                .map(PostResponse::from);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/posts/{id}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content) {
        Post post = postUseCase.updatePost(id, title, content);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @PostMapping("/posts/{id}/publish")
    @Operation(summary = "게시글 발행", description = "게시글을 발행합니다")
    public ResponseEntity<PostResponse> publishPost(@PathVariable Long id) {
        Post post = postUseCase.publishPost(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @PostMapping("/posts/{id}/approve")
    @Operation(summary = "게시글 승인", description = "대기 중인 게시글을 승인합니다")
    public ResponseEntity<PostResponse> approvePost(@PathVariable Long id) {
        Post post = postUseCase.approvePost(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @PostMapping("/posts/{id}/pin")
    @Operation(summary = "게시글 고정", description = "게시글을 상단에 고정합니다")
    public ResponseEntity<PostResponse> pinPost(@PathVariable Long id) {
        Post post = postUseCase.pinPost(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @PostMapping("/posts/{id}/unpin")
    @Operation(summary = "게시글 고정 해제", description = "게시글의 상단 고정을 해제합니다")
    public ResponseEntity<PostResponse> unpinPost(@PathVariable Long id) {
        Post post = postUseCase.unpinPost(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @PostMapping("/posts/{id}/view")
    @Operation(summary = "조회수 증가", description = "게시글 조회수를 증가시킵니다")
    public ResponseEntity<PostResponse> incrementViewCount(@PathVariable Long id) {
        Post post = postUseCase.incrementViewCount(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long id) {
        Post post = postUseCase.deletePost(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    // ========== Comment APIs ==========

    @PostMapping("/comments")
    @Operation(summary = "댓글 생성", description = "새로운 댓글을 생성합니다")
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CreateCommentRequest request) {
        Comment comment = commentUseCase.createComment(
                request.getPostId(),
                request.getAuthorId(),
                request.getAuthorName(),
                request.getContent(),
                request.getParentCommentId(),
                request.getIsAnonymous()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.from(comment));
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "게시글별 댓글 조회", description = "게시글 ID로 댓글 목록을 조회합니다")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentUseCase.findByPostId(postId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/comments/{id}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @RequestParam String content) {
        Comment comment = commentUseCase.updateComment(id, content);
        return ResponseEntity.ok(CommentResponse.from(comment));
    }

    @PostMapping("/comments/{id}/like")
    @Operation(summary = "댓글 좋아요", description = "댓글에 좋아요를 추가합니다")
    public ResponseEntity<CommentResponse> likeComment(@PathVariable Long id) {
        Comment comment = commentUseCase.likeComment(id);
        return ResponseEntity.ok(CommentResponse.from(comment));
    }

    @DeleteMapping("/comments/{id}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long id) {
        Comment comment = commentUseCase.deleteComment(id);
        return ResponseEntity.ok(CommentResponse.from(comment));
    }

    // ========== Attachment APIs ==========

    @PostMapping(value = "/posts/{postId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드", description = "게시글에 파일을 첨부합니다")
    public ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file,
            @RequestParam Long uploadedBy) throws IOException {
        FileStoragePort.FileUploadResult uploadResult = fileStoragePort.uploadFile(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize()
        );

        Attachment attachment = attachmentUseCase.saveAttachment(
                postId,
                file.getOriginalFilename(),
                uploadResult.storedFileName(),
                uploadResult.storagePath(),
                file.getContentType(),
                file.getSize(),
                uploadResult.downloadUrl(),
                uploadedBy
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(AttachmentResponse.from(attachment));
    }

    @GetMapping("/posts/{postId}/attachments")
    @Operation(summary = "첨부파일 목록 조회", description = "게시글의 첨부파일 목록을 조회합니다")
    public ResponseEntity<List<AttachmentResponse>> getAttachmentsByPost(@PathVariable Long postId) {
        List<AttachmentResponse> attachments = attachmentUseCase.findByPostId(postId).stream()
                .map(AttachmentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(attachments);
    }

    @PostMapping("/attachments/{id}/download")
    @Operation(summary = "파일 다운로드 카운트 증가", description = "파일 다운로드 카운트를 증가시킵니다")
    public ResponseEntity<AttachmentResponse> incrementDownloadCount(@PathVariable Long id) {
        Attachment attachment = attachmentUseCase.incrementDownloadCount(id);
        return ResponseEntity.ok(AttachmentResponse.from(attachment));
    }

    @DeleteMapping("/attachments/{id}")
    @Operation(summary = "첨부파일 삭제", description = "첨부파일을 삭제합니다")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        attachmentUseCase.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}
