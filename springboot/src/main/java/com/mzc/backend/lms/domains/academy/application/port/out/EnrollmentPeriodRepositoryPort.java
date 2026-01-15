package com.mzc.backend.lms.domains.academy.application.port.out;

import com.mzc.backend.lms.domains.academy.domain.model.EnrollmentPeriodDomain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 수강신청 기간 Repository Port
 */
public interface EnrollmentPeriodRepositoryPort {

    /**
     * ID로 기간 조회
     */
    Optional<EnrollmentPeriodDomain> findById(Long id);

    /**
     * 학기 ID와 기간명으로 수강신청 기간 조회
     */
    Optional<EnrollmentPeriodDomain> findByAcademicTermIdAndPeriodName(Long academicTermId, String periodName);

    /**
     * 학기 ID와 기간명 존재 여부 확인
     */
    boolean existsByAcademicTermIdAndPeriodName(Long academicTermId, String periodName);

    /**
     * 학기 ID로 수강신청 기간 목록 조회
     */
    List<EnrollmentPeriodDomain> findByAcademicTermId(Long academicTermId);

    /**
     * 기간 타입 ID로 기간 목록 조회
     */
    List<EnrollmentPeriodDomain> findByPeriodTypeId(Integer periodTypeId);

    /**
     * 기간 타입 ID와 학기 ID로 기간 목록 조회
     */
    List<EnrollmentPeriodDomain> findByPeriodTypeIdAndAcademicTermId(Integer periodTypeId, Long academicTermId);

    /**
     * 현재 활성화된 수강신청 기간 조회
     */
    Optional<EnrollmentPeriodDomain> findFirstActiveEnrollmentPeriod(LocalDateTime now);

    /**
     * 현재 활성화된 강의등록 기간 조회
     */
    Optional<EnrollmentPeriodDomain> findFirstActiveCourseRegistrationPeriod(LocalDateTime now);

    /**
     * 타입 코드로 현재 활성화된 기간 조회
     */
    Optional<EnrollmentPeriodDomain> findFirstActivePeriodByTypeCode(String typeCode, LocalDateTime now);

    /**
     * 수강신청 기간이 활성화되어 있는지 확인
     */
    boolean existsActiveEnrollmentPeriod(LocalDateTime now);

    /**
     * 강의등록 기간이 활성화되어 있는지 확인
     */
    boolean existsActiveCourseRegistrationPeriod(LocalDateTime now);

    /**
     * 현재 활성화된 기간 조회
     */
    Optional<EnrollmentPeriodDomain> findFirstActivePeriod(LocalDateTime now);

    /**
     * 특정 학기의 특정 타입 기간이 활성인지 확인
     */
    boolean existsActivePeriodByTypeCodeAndAcademicTermId(String typeCode, Long academicTermId, LocalDateTime now);

    /**
     * 특정 학기의 강의등록 기간이 활성화되어 있는지 확인
     */
    boolean existsActiveCourseRegistrationPeriodByAcademicTermId(Long academicTermId, LocalDateTime now);

    /**
     * 기간 저장
     */
    EnrollmentPeriodDomain save(EnrollmentPeriodDomain enrollmentPeriod);
}
