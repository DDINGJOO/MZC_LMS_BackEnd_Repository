package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence;

import com.mzc.backend.lms.domains.enrollment.application.port.out.CartRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.CourseCart;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.CourseCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CartPersistenceAdapter implements CartRepositoryPort {

    private final CourseCartRepository cartRepository;

    @Override
    public CourseCart save(CourseCart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<CourseCart> findByStudentId(Long studentId) {
        return cartRepository.findByStudentId(studentId);
    }

    @Override
    public Optional<CourseCart> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return cartRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public void delete(CourseCart cart) {
        cartRepository.delete(cart);
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public void deleteAllByStudentId(Long studentId) {
        List<CourseCart> carts = cartRepository.findByStudentId(studentId);
        cartRepository.deleteAll(carts);
    }
}
