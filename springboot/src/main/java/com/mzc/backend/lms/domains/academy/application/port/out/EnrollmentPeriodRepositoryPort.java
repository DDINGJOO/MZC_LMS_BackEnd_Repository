package com.mzc.backend.lms.domains.academy.application.port.out;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.PeriodType;

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
    Optional<EnrollmentPeriod> findById(Long id);

    /**
     * 학기와 기간명으로 수강신청 기간 조회
     */
    Optional<EnrollmentPeriod> findByAcademicTermAndPeriodName(AcademicTerm academicTerm, String periodName);

    /**
     * 학기와 기간명 존재 여부 확인
     */
    boolean existsByAcademicTermAndPeriodName(AcademicTerm academicTerm, String periodName);

    /**
     * 학기로 수강신청 기간 목록 조회
     */
    List<EnrollmentPeriod> findByAcademicTerm(AcademicTerm academicTerm);

    /**
     * 기간 타입으로 기간 목록 조회
     */
    List<EnrollmentPeriod> findByPeriodType(PeriodType periodType);

    /**
     * 기간 타입과 학기로 기간 목록 조회
     */
    List<EnrollmentPeriod> findByPeriodTypeAndAcademicTerm(PeriodType periodType, AcademicTerm academicTerm);

    /**
     * 현재 활성화된 수강신청 기간 조회
     */
    Optional<EnrollmentPeriod> findFirstActiveEnrollmentPeriod(LocalDateTime now);

    /**
     * 현재 활성화된 강의등록 기간 조회
     */
    Optional<EnrollmentPeriod> findFirstActiveCourseRegistrationPeriod(LocalDateTime now);

    /**
     * 타입 코드로 현재 활성화된 기간 조회
     */
    Optional<EnrollmentPeriod> findFirstActivePeriodByTypeCode(String typeCode, LocalDateTime now);

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
    Optional<EnrollmentPeriod> findFirstActivePeriod(LocalDateTime now);

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
    EnrollmentPeriod save(EnrollmentPeriod enrollmentPeriod);
}
