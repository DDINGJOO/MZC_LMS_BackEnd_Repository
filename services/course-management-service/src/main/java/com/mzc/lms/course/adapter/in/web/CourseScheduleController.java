package com.mzc.lms.course.adapter.in.web;

import com.mzc.lms.course.adapter.in.web.dto.ScheduleRequest;
import com.mzc.lms.course.adapter.in.web.dto.ScheduleResponse;
import com.mzc.lms.course.adapter.in.web.dto.ScheduleUpdateRequest;
import com.mzc.lms.course.application.port.in.CourseScheduleUseCase;
import com.mzc.lms.course.domain.model.CourseSchedule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/schedules")
@RequiredArgsConstructor
@Tag(name = "CourseSchedule", description = "강좌 시간표 관리 API")
public class CourseScheduleController {

    private final CourseScheduleUseCase scheduleUseCase;

    @PostMapping
    @Operation(summary = "시간표 추가", description = "강좌에 시간표를 추가합니다")
    public ResponseEntity<ScheduleResponse> addSchedule(
            @PathVariable Long courseId,
            @Valid @RequestBody ScheduleRequest request
    ) {
        CourseScheduleUseCase.AddScheduleCommand command = new CourseScheduleUseCase.AddScheduleCommand(
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                request.getRoom()
        );

        CourseSchedule schedule = scheduleUseCase.addSchedule(courseId, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ScheduleResponse.from(schedule));
    }

    @GetMapping
    @Operation(summary = "강좌 시간표 조회", description = "강좌의 모든 시간표를 조회합니다")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByCourse(@PathVariable Long courseId) {
        List<CourseSchedule> schedules = scheduleUseCase.getSchedulesByCourse(courseId);
        return ResponseEntity.ok(schedules.stream().map(ScheduleResponse::from).toList());
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "시간표 조회", description = "ID로 시간표를 조회합니다")
    public ResponseEntity<ScheduleResponse> getSchedule(
            @PathVariable Long courseId,
            @PathVariable Long scheduleId
    ) {
        return scheduleUseCase.getSchedule(scheduleId)
                .map(schedule -> ResponseEntity.ok(ScheduleResponse.from(schedule)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary = "시간표 수정", description = "시간표를 수정합니다")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long courseId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleUpdateRequest request
    ) {
        CourseScheduleUseCase.UpdateScheduleCommand command = new CourseScheduleUseCase.UpdateScheduleCommand(
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                request.getRoom()
        );

        CourseSchedule schedule = scheduleUseCase.updateSchedule(scheduleId, command);
        return ResponseEntity.ok(ScheduleResponse.from(schedule));
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "시간표 삭제", description = "시간표를 삭제합니다")
    public ResponseEntity<Void> removeSchedule(
            @PathVariable Long courseId,
            @PathVariable Long scheduleId
    ) {
        scheduleUseCase.removeSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
