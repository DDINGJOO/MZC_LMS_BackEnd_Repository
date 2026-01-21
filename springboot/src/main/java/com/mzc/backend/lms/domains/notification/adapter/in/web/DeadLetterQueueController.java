package com.mzc.backend.lms.domains.notification.adapter.in.web;

import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.DeadLetterMessageResponseDto;
import com.mzc.backend.lms.domains.notification.adapter.in.web.dto.DeadLetterQueueStatsDto;
import com.mzc.backend.lms.domains.notification.application.port.in.DeadLetterQueueManagementUseCase;
import com.mzc.backend.lms.domains.notification.domain.model.DeadLetterMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dead Letter Queue 관리 컨트롤러
 * ADMIN 권한 필요
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/notifications/dlq")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Dead Letter Queue", description = "알림 Dead Letter Queue 관리 API")
public class DeadLetterQueueController {

    private final DeadLetterQueueManagementUseCase dlqUseCase;

    @GetMapping
    @Operation(summary = "DLQ 메시지 목록 조회", description = "Dead Letter Queue에 있는 메시지 목록을 조회합니다.")
    public ResponseEntity<DeadLetterQueueStatsDto> getDeadLetterMessages(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {

        long totalCount = dlqUseCase.getDeadLetterQueueSize();
        List<DeadLetterMessage> messages = dlqUseCase.getDeadLetterMessages(offset, limit);

        List<DeadLetterMessageResponseDto> messageDtos = messages.stream()
                .map(DeadLetterMessageResponseDto::from)
                .collect(Collectors.toList());

        DeadLetterQueueStatsDto stats = DeadLetterQueueStatsDto.builder()
                .totalCount(totalCount)
                .messages(messageDtos)
                .offset(offset)
                .limit(limit)
                .hasMore(offset + messages.size() < totalCount)
                .build();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/count")
    @Operation(summary = "DLQ 메시지 수 조회", description = "Dead Letter Queue에 있는 메시지 수를 조회합니다.")
    public ResponseEntity<Map<String, Long>> getDeadLetterQueueSize() {
        long count = dlqUseCase.getDeadLetterQueueSize();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{messageId}")
    @Operation(summary = "DLQ 메시지 상세 조회", description = "특정 Dead Letter 메시지의 상세 정보를 조회합니다.")
    public ResponseEntity<DeadLetterMessageResponseDto> getDeadLetterMessage(
            @PathVariable String messageId) {

        return dlqUseCase.getDeadLetterMessage(messageId)
                .map(DeadLetterMessageResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{messageId}/reprocess")
    @Operation(summary = "DLQ 메시지 재처리", description = "Dead Letter 메시지를 원본 큐로 다시 발행합니다.")
    public ResponseEntity<Map<String, Object>> reprocessMessage(@PathVariable String messageId) {
        log.info("DLQ 메시지 재처리 요청: messageId={}", messageId);

        boolean success = dlqUseCase.reprocessDeadLetterMessage(messageId);

        if (success) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "메시지가 원본 큐로 재발행되었습니다.",
                    "messageId", messageId
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "메시지 재처리에 실패했습니다.",
                    "messageId", messageId
            ));
        }
    }

    @PostMapping("/reprocess-all")
    @Operation(summary = "모든 DLQ 메시지 재처리", description = "Dead Letter Queue의 모든 메시지를 원본 큐로 다시 발행합니다.")
    public ResponseEntity<Map<String, Object>> reprocessAllMessages() {
        log.info("모든 DLQ 메시지 재처리 요청");

        int[] results = dlqUseCase.reprocessAllDeadLetterMessages();
        int successCount = results[0];
        int failCount = results[1];

        if (successCount == 0 && failCount == 0) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "재처리할 메시지가 없습니다.",
                    "processedCount", 0
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "DLQ 메시지 재처리 완료",
                "successCount", successCount,
                "failCount", failCount,
                "totalCount", successCount + failCount
        ));
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "DLQ 메시지 삭제", description = "Dead Letter 메시지를 삭제합니다.")
    public ResponseEntity<Map<String, Object>> deleteMessage(@PathVariable String messageId) {
        log.info("DLQ 메시지 삭제 요청: messageId={}", messageId);

        boolean success = dlqUseCase.removeDeadLetterMessage(messageId);

        if (success) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "메시지가 삭제되었습니다.",
                    "messageId", messageId
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "메시지 삭제에 실패했습니다.",
                    "messageId", messageId
            ));
        }
    }
}
