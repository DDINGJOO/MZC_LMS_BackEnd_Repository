package com.mzc.backend.lms.domains.course.course.application.port.in;

import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.*;

import java.util.List;

/**
 * 주차별 콘텐츠 관리 UseCase
 */
public interface CourseWeekContentUseCase {

    /**
     * 주차 생성
     */
    WeekDto createWeek(Long courseId, CreateWeekRequestDto request, Long professorId);

    /**
     * 주차 수정
     */
    WeekDto updateWeek(Long courseId, Long weekId, UpdateWeekRequestDto request, Long professorId);

    /**
     * 주차 삭제
     */
    void deleteWeek(Long courseId, Long weekId, Long professorId);

    /**
     * 콘텐츠 등록
     */
    WeekContentDto createContent(Long courseId, Long weekId, CreateWeekContentRequestDto request, Long professorId);

    /**
     * 콘텐츠 수정
     */
    WeekContentDto updateContent(Long courseId, Long weekId, Long contentId,
                                  UpdateWeekContentRequestDto request, Long professorId);

    /**
     * 콘텐츠 수정 (contentId만으로)
     */
    WeekContentDto updateContentByContentId(Long contentId, UpdateWeekContentRequestDto request, Long professorId);

    /**
     * 콘텐츠 삭제
     */
    void deleteContent(Long courseId, Long weekId, Long contentId, Long professorId);

    /**
     * 콘텐츠 삭제 (contentId만으로)
     */
    void deleteContentByContentId(Long contentId, Long professorId);

    /**
     * 주차별 콘텐츠 목록 조회
     */
    WeekContentsResponseDto getWeekContents(Long courseId, Long weekId, Long professorId);

    /**
     * 강의 주차 목록 조회
     */
    List<WeekListResponseDto> getWeeks(Long courseId, Long requesterId);

    /**
     * 콘텐츠 순서 변경
     */
    ReorderContentsResponseDto reorderContents(Long courseId, Long weekId,
                                                ReorderContentsRequestDto request, Long professorId);
}
