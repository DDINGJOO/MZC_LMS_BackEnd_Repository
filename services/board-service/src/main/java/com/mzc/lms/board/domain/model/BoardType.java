package com.mzc.lms.board.domain.model;

public enum BoardType {
    NOTICE("공지사항"),
    FAQ("자주 묻는 질문"),
    QNA("질문과 답변"),
    FREE("자유게시판"),
    COURSE("강좌별 게시판"),
    ANNOUNCEMENT("학습 공지");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
