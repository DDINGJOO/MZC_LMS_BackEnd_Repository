package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.mapper.StudentNumberSequenceMapper;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.StudentNumberSequenceRepository;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserRepository;
import com.mzc.backend.lms.domains.user.application.port.out.StudentNumberSequencePort;
import com.mzc.backend.lms.domains.user.domain.model.StudentNumberSequenceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 학번 시퀀스 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class StudentNumberSequencePersistenceAdapter implements StudentNumberSequencePort {

    private final StudentNumberSequenceRepository sequenceRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<StudentNumberSequenceDomain> findByYearAndCollegeAndDepartmentWithLock(
            Integer year, Long collegeId, Long departmentId) {
        return sequenceRepository
                .findByYearAndCollegeAndDepartmentWithLock(year, collegeId, departmentId)
                .map(StudentNumberSequenceMapper::toDomain);
    }

    @Override
    public StudentNumberSequenceDomain save(StudentNumberSequenceDomain sequence) {
        var entity = StudentNumberSequenceMapper.toEntity(sequence);
        var savedEntity = sequenceRepository.save(entity);
        return StudentNumberSequenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Integer> findMaxSequenceByPrefix(String prefix) {
        return userRepository.findMaxSequenceByPrefix(prefix);
    }
}
