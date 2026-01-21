package com.mzc.lms.profile.adapter.in.web.dto;

import com.mzc.lms.profile.domain.model.ProfessorProfile;
import com.mzc.lms.profile.domain.model.StudentProfile;
import com.mzc.lms.profile.domain.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private Long userId;
    private String name;
    private String email;
    private String userType;
    private String profileImageUrl;
    private StudentProfileDto studentProfile;
    private ProfessorProfileDto professorProfile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProfileResponse from(UserProfile profile, StudentProfile studentProfile, ProfessorProfile professorProfile) {
        return ProfileResponse.builder()
                .userId(profile.getUserId())
                .name(profile.getName())
                .email(profile.getEmail())
                .userType(profile.getUserType())
                .profileImageUrl(profile.getProfileImageUrl())
                .studentProfile(studentProfile != null ? StudentProfileDto.from(studentProfile) : null)
                .professorProfile(professorProfile != null ? ProfessorProfileDto.from(professorProfile) : null)
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentProfileDto {
        private Long studentId;
        private Integer admissionYear;
        private Integer grade;
        private String departmentName;
        private String collegeName;

        public static StudentProfileDto from(StudentProfile profile) {
            return StudentProfileDto.builder()
                    .studentId(profile.getStudentId())
                    .admissionYear(profile.getAdmissionYear())
                    .grade(profile.getGrade())
                    .departmentName(profile.getDepartmentName())
                    .collegeName(profile.getCollegeName())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfessorProfileDto {
        private Long professorId;
        private LocalDate appointmentDate;
        private String departmentName;
        private String collegeName;
        private Integer yearsOfService;

        public static ProfessorProfileDto from(ProfessorProfile profile) {
            return ProfessorProfileDto.builder()
                    .professorId(profile.getProfessorId())
                    .appointmentDate(profile.getAppointmentDate())
                    .departmentName(profile.getDepartmentName())
                    .collegeName(profile.getCollegeName())
                    .yearsOfService(profile.getYearsOfService())
                    .build();
        }
    }
}
