package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth.SignupRequestDto;
import com.mzc.backend.lms.domains.user.application.port.in.SignupUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.*;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Department;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.DepartmentRepository;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.ProfessorDepartment;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.ProfessorDepartmentRepository;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserPrimaryContact;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserPrimaryContactRepository;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.StudentDepartment;
import com.mzc.backend.lms.domains.user.application.service.StudentNumberGeneratorService;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;

/**
 * 회원가입 서비스
 * SignupUseCase 구현체 (Port 기반)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final StudentRepositoryPort studentRepositoryPort;
    private final ProfessorRepositoryPort professorRepositoryPort;
    private final UserProfileRepositoryPort userProfileRepositoryPort;
    private final StudentDepartmentRepositoryPort studentDepartmentRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final DataEncryptorPort dataEncryptorPort;

    // 아직 Port로 전환하지 않은 의존성들
    private final DepartmentRepository departmentRepository;
    private final UserPrimaryContactRepository userPrimaryContactRepository;
    private final ProfessorDepartmentRepository professorDepartmentRepository;
    private final StudentNumberGeneratorService studentNumberGenerator;
    private final EmailVerificationServiceImpl emailVerificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String execute(SignupRequestDto dto) {
        try {
            log.info("회원가입 시작: email={}, userType={}, departmentId={}",
                    dto.getEmail(), dto.getUserType(), dto.getDepartmentId());

            validateSignupRequest(dto);

            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> {
                        log.error("학과를 찾을 수 없음: departmentId={}", dto.getDepartmentId());
                        return new IllegalArgumentException("유효하지 않은 학과입니다.");
                    });

            Long userNumber;
            if (dto.isStudent()) {
                userNumber = studentNumberGenerator.generateStudentNumber(
                        department.getCollege().getId(),
                        department.getId()
                );
            } else if (dto.isProfessor()) {
                userNumber = studentNumberGenerator.generateProfessorNumber(
                        department.getCollege().getId(),
                        department.getId()
                );
            } else {
                throw AuthException.invalidSignupData("유효하지 않은 사용자 타입");
            }

            User user = createUser(userNumber, dto);
            createUserProfile(user, dto);
            createUserContact(user, dto);

            if (dto.isStudent()) {
                createStudent(user, department, dto.getGrade());
            } else if (dto.isProfessor()) {
                createProfessor(user, department);
            }

            emailVerificationService.clearVerification(dto.getEmail());

            log.info("회원가입 완료: userId={}, email={}, userType={}",
                    user.getId(), dto.getEmail(), dto.getUserType());

            return user.getId().toString();

        } catch (Exception e) {
            log.error("회원가입 실패: email={}, error={}", dto.getEmail(), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        String encryptedEmail = dataEncryptorPort.encryptEmail(email);
        return !userRepositoryPort.existsByEmail(encryptedEmail);
    }

    private void validateSignupRequest(SignupRequestDto dto) {
        if (!dto.isPasswordMatched()) {
            throw AuthException.invalidSignupData("비밀번호가 일치하지 않습니다");
        }

        if (!emailVerificationService.isVerified(dto.getEmail())) {
            throw AuthException.emailVerificationFailed();
        }

        if (!isEmailAvailable(dto.getEmail())) {
            throw AuthException.emailAlreadyExists(dto.getEmail());
        }
    }

    private User createUser(Long id, SignupRequestDto dto) {
        User user = User.create(
                id,
                dataEncryptorPort.encryptEmail(dto.getEmail()),
                passwordEncoderPort.encode(dto.getPassword())
        );
        return userRepositoryPort.save(user);
    }

    private void createUserProfile(User user, SignupRequestDto dto) {
        String encryptedName = dataEncryptorPort.encryptName(dto.getName());
        UserProfile profile = UserProfile.create(user, encryptedName);
        userProfileRepositoryPort.save(profile);
    }

    private void createUserContact(User user, SignupRequestDto dto) {
        UserPrimaryContact contact = UserPrimaryContact.create(
                user,
                dataEncryptorPort.encryptPhoneNumber(dto.getPhoneNumber())
        );
        userPrimaryContactRepository.save(contact);
    }

    private void createStudent(User user, Department department, Integer grade) {
        if (grade == null || grade < 1 || grade > 4) {
            grade = 1;
        }

        Long studentId = user.getId();

        Student student = Student.create(studentId, user, Year.now().getValue(), grade);
        studentRepositoryPort.save(student);

        StudentDepartment studentDept = StudentDepartment.create(
                student, department, true, LocalDate.now()
        );
        studentDepartmentRepositoryPort.save(studentDept);

        log.info("학생 생성: studentId={}, grade={}", studentId, grade);
    }

    private void createProfessor(User user, Department department) {
        Long professorId = user.getId();

        Professor professor = Professor.create(professorId, user, LocalDate.now());
        professorRepositoryPort.save(professor);

        ProfessorDepartment professorDept = ProfessorDepartment.create(
                professor, department, true, LocalDate.now()
        );
        professorDepartmentRepository.save(professorDept);

        log.info("교수 생성: professorId={}", professorId);
    }
}
