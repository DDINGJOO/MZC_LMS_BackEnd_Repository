package com.mzc.lms.board.application.port.out;

import com.mzc.lms.board.domain.event.BoardEvent;

public interface BoardEventPublisher {
    void publish(BoardEvent event);
}
