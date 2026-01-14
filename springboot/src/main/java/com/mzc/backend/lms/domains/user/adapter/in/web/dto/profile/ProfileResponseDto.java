package com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 프로필 조회 응답 DTO
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDto {

    // 기본 정보
    private final Long userId;
    private final String email;
    private final String name;

    // 연락처 정보
    private final String mobileNumber;
    private final String homeNumber;
    private final String officeNumber;
    private final Boolean mobileVerified;

    // 프로필 이미지
    private final String profileImageUrl;
    private final String thumbnailUrl;

    // 사용자 타입
    private final String userType;

    // 학생 정보 (userType이 STUDENT인 경우)
    private final Long studentId;
    private final Integer admissionYear;
    private final Integer grade;

    // 교수 정보 (userType이 PROFESSOR인 경우)
    private final Long professorId;
    private final LocalDate appointmentDate;

    // 학과 정보 (공통)
    private final String collegeName;
    private final String departmentName;

    /**
     * User Entity -> DTO 변환 (기본 정보만)
     */
    public static ProfileResponseDto from(User user) {
        if (user == null) {
            return null;
        }

        ProfileResponseDtoBuilder builder = ProfileResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getUserProfile() != null ? user.getUserProfile().getName() : null);

        // 연락처 정보
        if (user.getPrimaryContact() != null) {
            builder.mobileNumber(user.getPrimaryContact().getMobileNumber())
                   .homeNumber(user.getPrimaryContact().getHomeNumber())
                   .officeNumber(user.getPrimaryContact().getOfficeNumber())
                   .mobileVerified(user.getPrimaryContact().getMobileVerified());
        }

        // 프로필 이미지
        if (user.getProfileImage() != null) {
            builder.profileImageUrl(user.getProfileImage().getImageUrl())
                   .thumbnailUrl(user.getProfileImage().getThumbnailUrl());
        }

        return builder.build();
    }

    /**
     * Student Entity -> DTO 변환
     */
    public static ProfileResponseDto from(Student student) {
        if (student == null || student.getUser() == null) {
            return null;
        }

        ProfileResponseDtoBuilder builder = ProfileResponseDto.builder()
                .userId(student.getUser().getId())
                .email(student.getUser().getEmail())
                .name(student.getUser().getUserProfile() != null ? student.getUser().getUserProfile().getName() : null)
                .userType("STUDENT")
                .studentId(student.getId())
                .admissionYear(student.getAdmissionYear())
                .grade(student.getGrade());

        // 연락처 정보
        if (student.getUser().getPrimaryContact() != null) {
            builder.mobileNumber(student.getUser().getPrimaryContact().getMobileNumber())
                   .homeNumber(student.getUser().getPrimaryContact().getHomeNumber())
                   .officeNumber(student.getUser().getPrimaryContact().getOfficeNumber())
                   .mobileVerified(student.getUser().getPrimaryContact().getMobileVerified());
        }

        // 프로필 이미지
        if (student.getUser().getProfileImage() != null) {
            builder.profileImageUrl(student.getUser().getProfileImage().getImageUrl())
                   .thumbnailUrl(student.getUser().getProfileImage().getThumbnailUrl());
        }

        // 학과 정보
        if (student.getStudentDepartment() != null && student.getStudentDepartment().getDepartment() != null) {
            builder.departmentName(student.getStudentDepartment().getDepartment().getDepartmentName());
            if (student.getStudentDepartment().getDepartment().getCollege() != null) {
                builder.collegeName(student.getStudentDepartment().getDepartment().getCollege().getCollegeName());
            }
        }

        return builder.build();
    }

    /**
     * Professor Entity -> DTO 변환
     */
    public static ProfileResponseDto from(Professor professor) {
        if (professor == null || professor.getUser() == null) {
            return null;
        }

        ProfileResponseDtoBuilder builder = ProfileResponseDto.builder()
                .userId(professor.getUser().getId())
                .email(professor.getUser().getEmail())
                .name(professor.getUser().getUserProfile() != null ? professor.getUser().getUserProfile().getName() : null)
                .userType("PROFESSOR")
                .professorId(professor.getId())
                .appointmentDate(professor.getAppointmentDate());

        // 연락처 정보
        if (professor.getUser().getPrimaryContact() != null) {
            builder.mobileNumber(professor.getUser().getPrimaryContact().getMobileNumber())
                   .homeNumber(professor.getUser().getPrimaryContact().getHomeNumber())
                   .officeNumber(professor.getUser().getPrimaryContact().getOfficeNumber())
                   .mobileVerified(professor.getUser().getPrimaryContact().getMobileVerified());
        }

        // 프로필 이미지
        if (professor.getUser().getProfileImage() != null) {
            builder.profileImageUrl(professor.getUser().getProfileImage().getImageUrl())
                   .thumbnailUrl(professor.getUser().getProfileImage().getThumbnailUrl());
        }

        // 학과 정보
        if (professor.getProfessorDepartment() != null && professor.getProfessorDepartment().getDepartment() != null) {
            builder.departmentName(professor.getProfessorDepartment().getDepartment().getDepartmentName());
            if (professor.getProfessorDepartment().getDepartment().getCollege() != null) {
                builder.collegeName(professor.getProfessorDepartment().getDepartment().getCollege().getCollegeName());
            }
        }

        return builder.build();
    }
}