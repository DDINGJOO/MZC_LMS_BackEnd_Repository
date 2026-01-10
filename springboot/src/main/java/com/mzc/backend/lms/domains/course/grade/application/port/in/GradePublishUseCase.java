package com.mzc.backend.lms.domains.course.grade.application.port.in;

import java.time.LocalDateTime;

/**
 * 성적 산출 및 공개 UseCase
 */
public interface GradePublishUseCase {

    /**
     * 성적 산출기간이 종료된 학기들에 대해 자동 공개 처리 (스케줄러용)
     */
    void publishEndedTerms(LocalDateTime now);

    /**
     * 특정 학기의 성적 공개 (수동 실행용)
     */
    void publishTermIfAllowed(Long academicTermId, LocalDateTime now);

    /**
     * 특정 강의의 성적 산출 (수동 실행용)
     */
    void calculateCourseIfAllowed(Long courseId, LocalDateTime now);

    /**
     * 특정 강의의 성적 공개 (수동 실행용)
     */
    void publishCourseIfAllowed(Long courseId, LocalDateTime now);
}
