package com.mzc.backend.lms.domains.academy.adapter.out.persistence.repository;

import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.EnrollmentPeriod;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 수강신청 기간 JPA Repository
 */
@Repository
public interface EnrollmentPeriodJpaRepository extends JpaRepository<EnrollmentPeriod, Long> {

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
     * 기간명으로 수강신청 기간 목록 조회
     */
    List<EnrollmentPeriod> findByPeriodName(String periodName);

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
    @Query("SELECT ep FROM EnrollmentPeriod ep " +
            "JOIN FETCH ep.periodType pt " +
            "WHERE pt.typeCode = 'ENROLLMENT' " +
            "AND ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now " +
            "ORDER BY ep.startDatetime ASC")
    Optional<EnrollmentPeriod> findFirstActiveEnrollmentPeriod(@Param("now") LocalDateTime now);

    /**
     * 현재 활성화된 강의등록 기간 조회
     */
    @Query("SELECT ep FROM EnrollmentPeriod ep " +
            "JOIN FETCH ep.periodType pt " +
            "WHERE pt.typeCode = 'COURSE_REGISTRATION' " +
            "AND ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now " +
            "ORDER BY ep.startDatetime ASC")
    Optional<EnrollmentPeriod> findFirstActiveCourseRegistrationPeriod(@Param("now") LocalDateTime now);

    /**
     * 타입 코드로 현재 활성화된 기간 조회
     */
    @Query("SELECT ep FROM EnrollmentPeriod ep " +
            "JOIN FETCH ep.periodType pt " +
            "WHERE pt.typeCode = :typeCode " +
            "AND ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now " +
            "ORDER BY ep.startDatetime ASC")
    Optional<EnrollmentPeriod> findFirstActivePeriodByTypeCode(
            @Param("typeCode") String typeCode,
            @Param("now") LocalDateTime now);

    /**
     * 수강신청 기간이 활성화되어 있는지 확인
     */
    @Query("SELECT COUNT(ep) > 0 FROM EnrollmentPeriod ep " +
            "JOIN ep.periodType pt " +
            "WHERE pt.typeCode = 'ENROLLMENT' " +
            "AND ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now")
    boolean existsActiveEnrollmentPeriod(@Param("now") LocalDateTime now);

    /**
     * 강의등록 기간이 활성화되어 있는지 확인
     */
    @Query("SELECT COUNT(ep) > 0 FROM EnrollmentPeriod ep " +
            "JOIN ep.periodType pt " +
            "WHERE pt.typeCode = 'COURSE_REGISTRATION' " +
            "AND ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now")
    boolean existsActiveCourseRegistrationPeriod(@Param("now") LocalDateTime now);

    /**
     * 현재 활성화된 기간 조회
     */
    @Query("SELECT ep FROM EnrollmentPeriod ep " +
            "JOIN FETCH ep.periodType pt " +
            "WHERE ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now " +
            "ORDER BY ep.startDatetime ASC")
    Optional<EnrollmentPeriod> findFirstActivePeriod(@Param("now") LocalDateTime now);

    /**
     * 특정 학기의 특정 타입 기간이 활성인지 확인
     */
    @Query("SELECT COUNT(ep) > 0 FROM EnrollmentPeriod ep " +
            "JOIN ep.periodType pt " +
            "WHERE pt.typeCode = :typeCode " +
            "AND ep.academicTerm.id = :academicTermId " +
            "AND ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now")
    boolean existsActivePeriodByTypeCodeAndAcademicTermId(
            @Param("typeCode") String typeCode,
            @Param("academicTermId") Long academicTermId,
            @Param("now") LocalDateTime now);

    /**
     * 특정 학기의 강의등록 기간이 활성화되어 있는지 확인
     */
    @Query("SELECT COUNT(ep) > 0 FROM EnrollmentPeriod ep " +
            "JOIN ep.periodType pt " +
            "WHERE pt.typeCode = 'COURSE_REGISTRATION' " +
            "AND ep.academicTerm.id = :academicTermId " +
            "AND ep.startDatetime <= :now " +
            "AND ep.endDatetime >= :now")
    boolean existsActiveCourseRegistrationPeriodByAcademicTermId(
            @Param("academicTermId") Long academicTermId,
            @Param("now") LocalDateTime now);
}
