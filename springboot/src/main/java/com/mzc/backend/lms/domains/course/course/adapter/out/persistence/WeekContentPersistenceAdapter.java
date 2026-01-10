package com.mzc.backend.lms.domains.course.course.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.course.application.port.out.WeekContentRepositoryPort;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.WeekContent;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.WeekContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 주차별 콘텐츠 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class WeekContentPersistenceAdapter implements WeekContentRepositoryPort {

    private final WeekContentRepository weekContentRepository;

    @Override
    public WeekContent save(WeekContent content) {
        return weekContentRepository.save(content);
    }

    @Override
    public Optional<WeekContent> findById(Long id) {
        return weekContentRepository.findById(id);
    }

    @Override
    public List<WeekContent> findByWeekIdOrderByDisplayOrder(Long weekId) {
        return weekContentRepository.findByWeekIdOrderByDisplayOrder(weekId);
    }

    @Override
    public void delete(WeekContent content) {
        weekContentRepository.delete(content);
    }

    @Override
    public void deleteByWeekId(Long weekId) {
        weekContentRepository.deleteByWeekId(weekId);
    }

    @Override
    public boolean existsByWeekIdAndDisplayOrder(Long weekId, Integer displayOrder) {
        return weekContentRepository.existsByWeekIdAndDisplayOrder(weekId, displayOrder);
    }
}
