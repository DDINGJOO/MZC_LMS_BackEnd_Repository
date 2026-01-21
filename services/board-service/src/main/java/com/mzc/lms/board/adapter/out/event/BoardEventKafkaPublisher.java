package com.mzc.lms.board.adapter.out.event;

import com.mzc.lms.board.application.port.out.BoardEventPublisher;
import com.mzc.lms.board.domain.event.BoardEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BoardEventKafkaPublisher implements BoardEventPublisher {

    private static final String TOPIC = "board-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(BoardEvent event) {
        try {
            String key = event.getPostId() != null ? event.getPostId().toString() : "unknown";
            kafkaTemplate.send(TOPIC, key, event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("게시판 이벤트 발행 실패: {}", event, ex);
                        } else {
                            log.debug("게시판 이벤트 발행 성공: {}", event.getEventType());
                        }
                    });
        } catch (Exception e) {
            log.error("게시판 이벤트 발행 오류: {}", event, e);
        }
    }
}
