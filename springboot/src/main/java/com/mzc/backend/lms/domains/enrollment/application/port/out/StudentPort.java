package com.mzc.backend.lms.domains.enrollment.application.port.out;

/**
 * Student(User) 도메인과의 통신을 위한 Port
 * MSA 전환 시 HTTP Client로 교체 가능
 */
public interface StudentPort {

    /**
     * 학생 정보 조회
     */
    StudentInfo getStudent(Long studentId);

    /**
     * 학생 존재 여부 확인
     */
    boolean existsById(Long studentId);

    /**
     * 사용자 이름 조회 (교수 이름 등)
     */
    String getUserName(Long userId);

    // DTO Records

    record StudentInfo(
            Long id,
            Long studentId,
            String name,
            Long departmentId,
            String departmentName
    ) {}
}
