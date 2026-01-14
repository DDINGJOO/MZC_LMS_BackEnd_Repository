package com.mzc.backend.lms.domains.board.application.port.out;

import java.util.Optional;

/**
 * 사용자 타입 조회를 위한 Port
 * Board 도메인에서 사용자 타입 확인을 위한 인터페이스
 */
public interface UserTypeQueryPort {

    /**
     * 사용자 ID로 사용자 타입 코드 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 타입 코드 (STUDENT, PROFESSOR 등)
     */
    Optional<String> findUserTypeCodeByUserId(Long userId);

    /**
     * 사용자가 학생인지 확인
     *
     * @param userId 사용자 ID
     * @return 학생 여부
     */
    boolean isStudent(Long userId);

    /**
     * 사용자가 교수인지 확인
     *
     * @param userId 사용자 ID
     * @return 교수 여부
     */
    boolean isProfessor(Long userId);
}
