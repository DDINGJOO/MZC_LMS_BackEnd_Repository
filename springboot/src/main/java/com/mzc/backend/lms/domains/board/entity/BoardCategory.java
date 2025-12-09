package com.mzc.backend.lms.domains.board.entity;

import com.mzc.backend.lms.domains.board.enums.BoardType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시판 설정 엔터티
 * 각 게시판 유형별(공지, 자유, 질문 등) 기능 활성화 여부를 관리
 */
@Entity
@Table(name = "board_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 게시판 유형 (Unique)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false, unique = true, length = 30)
    private BoardType boardType;

    /**
     * 댓글 허용 여부
     */
    @Column(name = "allow_comments", nullable = false)
    private boolean allowComments;

    /**
     * 첨부파일 허용 여부
     */
    @Column(name = "allow_attachments", nullable = false)
    private boolean allowAttachments;

    /**
     * 익명 작성 허용 여부
     */
    @Column(name = "allow_anonymous", nullable = false)
    private boolean allowAnonymous;

    // 생성자
    public BoardCategory(BoardType boardType, boolean allowComments, boolean allowAttachments, boolean allowAnonymous) {
        this.boardType = boardType;
        this.allowComments = allowComments;
        this.allowAttachments = allowAttachments;
        this.allowAnonymous = allowAnonymous;
    }

    // 비즈니스 로직
    
    /**
     * 게시판 설정 업데이트
     */
    public void updateSettings(boolean allowComments, boolean allowAttachments, boolean allowAnonymous) {
        this.allowComments = allowComments;
        this.allowAttachments = allowAttachments;
        this.allowAnonymous = allowAnonymous;
    }
}
