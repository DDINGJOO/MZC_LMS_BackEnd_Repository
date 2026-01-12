package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.ProfileResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.ProfileUpdateRequestDto;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserPrimaryContact;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.application.port.in.GetMyProfileUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.UpdateProfileUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.*;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import com.mzc.backend.lms.domains.user.exception.UserErrorCode;
import com.mzc.backend.lms.domains.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 프로필 서비스
 * (Hexagonal Architecture - Application Service)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService implements GetMyProfileUseCase, UpdateProfileUseCase {

    private final UserQueryPort userQueryPort;
    private final UserProfileQueryPort userProfileQueryPort;
    private final UserProfileCommandPort userProfileCommandPort;
    private final UserPrimaryContactQueryPort userPrimaryContactQueryPort;
    private final UserPrimaryContactCommandPort userPrimaryContactCommandPort;
    private final UserProfileImageQueryPort userProfileImageQueryPort;
    private final StudentQueryPort studentQueryPort;
    private final StudentDepartmentQueryPort studentDepartmentQueryPort;
    private final ProfessorQueryPort professorQueryPort;
    private final ProfessorDepartmentQueryPort professorDepartmentQueryPort;
    private final DataEncryptionPort dataEncryptionPort;

    @Override
    public ProfileResponseDto getMyProfile(Long userId) {
        User user = userQueryPort.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        ProfileResponseDto.ProfileResponseDtoBuilder builder = ProfileResponseDto.builder()
                .userId(userId)
                .email(dataEncryptionPort.decryptEmail(user.getEmail()));

        // 프로필 정보 조회
        userProfileQueryPort.findByUserId(userId)
                .ifPresent(profile -> builder.name(dataEncryptionPort.decryptName(profile.getName())));

        // 연락처 정보 조회
        userPrimaryContactQueryPort.findByUserId(userId)
                .ifPresent(contact -> {
                    builder.mobileNumber(decryptIfNotNull(contact.getMobileNumber()))
                            .homeNumber(decryptIfNotNull(contact.getHomeNumber()))
                            .officeNumber(decryptIfNotNull(contact.getOfficeNumber()))
                            .mobileVerified(contact.getMobileVerified());
                });

        // 프로필 이미지 조회
        userProfileImageQueryPort.findByUserId(userId)
                .ifPresent(image -> builder
                        .profileImageUrl(image.getImageUrl())
                        .thumbnailUrl(image.getThumbnailUrl()));

        // 사용자 타입별 정보 조회
        populateUserTypeInfo(userId, builder);

        return builder.build();
    }

    @Override
    @Transactional
    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto request) {
        User user = userQueryPort.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 이름 업데이트
        if (request.getName() != null && !request.getName().isEmpty()) {
            updateName(user, request.getName());
        }

        // 연락처 업데이트
        if (hasContactInfo(request)) {
            updateContact(user, request);
        }

        // 수정된 프로필 조회 후 반환
        return getMyProfile(userId);
    }

    private void updateName(User user, String name) {
        String encryptedName = dataEncryptionPort.encryptName(name);

        Optional<UserProfile> profileOpt = userProfileQueryPort.findByUserId(user.getId());
        if (profileOpt.isPresent()) {
            UserProfile profile = profileOpt.get();
            profile.changeName(encryptedName);
            userProfileCommandPort.save(profile);
        } else {
            UserProfile newProfile = UserProfile.create(user, encryptedName);
            userProfileCommandPort.save(newProfile);
        }
    }

    private void updateContact(User user, ProfileUpdateRequestDto request) {
        Optional<UserPrimaryContact> contactOpt = userPrimaryContactQueryPort.findByUserId(user.getId());

        if (contactOpt.isPresent()) {
            UserPrimaryContact contact = contactOpt.get();
            updateContactFields(contact, request);
            userPrimaryContactCommandPort.save(contact);
        } else {
            String encryptedMobile = encryptIfNotNull(request.getMobileNumber());
            UserPrimaryContact newContact = UserPrimaryContact.builder()
                    .user(user)
                    .mobileNumber(encryptedMobile)
                    .homeNumber(encryptIfNotNull(request.getHomeNumber()))
                    .officeNumber(encryptIfNotNull(request.getOfficeNumber()))
                    .build();
            userPrimaryContactCommandPort.save(newContact);
        }
    }

    private void updateContactFields(UserPrimaryContact contact, ProfileUpdateRequestDto request) {
        if (request.getMobileNumber() != null) {
            contact.updateMobileNumber(dataEncryptionPort.encryptPhoneNumber(request.getMobileNumber()));
        }
        if (request.getHomeNumber() != null) {
            contact.updateHomeNumber(dataEncryptionPort.encryptPhoneNumber(request.getHomeNumber()));
        }
        if (request.getOfficeNumber() != null) {
            contact.updateOfficeNumber(dataEncryptionPort.encryptPhoneNumber(request.getOfficeNumber()));
        }
    }

    private boolean hasContactInfo(ProfileUpdateRequestDto request) {
        return request.getMobileNumber() != null ||
                request.getHomeNumber() != null ||
                request.getOfficeNumber() != null;
    }

    private String encryptIfNotNull(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return dataEncryptionPort.encryptPhoneNumber(value);
    }

    private void populateUserTypeInfo(Long userId, ProfileResponseDto.ProfileResponseDtoBuilder builder) {
        // 학생 정보 조회 시도
        Optional<Student> studentOpt = studentQueryPort.findById(userId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            builder.userType("STUDENT")
                    .studentId(student.getStudentId())
                    .admissionYear(student.getAdmissionYear())
                    .grade(student.getGrade());

            // 학과 정보 조회
            studentDepartmentQueryPort.findByStudentId(userId)
                    .ifPresent(sd -> {
                        builder.departmentName(sd.getDepartment().getDepartmentName());
                        if (sd.getDepartment().getCollege() != null) {
                            builder.collegeName(sd.getDepartment().getCollege().getCollegeName());
                        }
                    });
            return;
        }

        // 교수 정보 조회 시도
        Optional<Professor> professorOpt = professorQueryPort.findById(userId);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            builder.userType("PROFESSOR")
                    .professorId(professor.getProfessorId())
                    .appointmentDate(professor.getAppointmentDate());

            // 학과 정보 조회
            professorDepartmentQueryPort.findByProfessorId(userId)
                    .ifPresent(pd -> {
                        builder.departmentName(pd.getDepartment().getDepartmentName());
                        if (pd.getDepartment().getCollege() != null) {
                            builder.collegeName(pd.getDepartment().getCollege().getCollegeName());
                        }
                    });
        }
    }

    private String decryptIfNotNull(String encryptedValue) {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            return null;
        }
        return dataEncryptionPort.decryptPhoneNumber(encryptedValue);
    }
}
