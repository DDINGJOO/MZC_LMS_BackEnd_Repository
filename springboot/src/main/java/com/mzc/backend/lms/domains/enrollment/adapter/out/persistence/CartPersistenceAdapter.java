package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence;

import com.mzc.backend.lms.domains.enrollment.application.port.out.CartRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CartRepositoryPort.CartInfo;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.CourseCart;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.CourseCartRepository;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.StudentRepository;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 장바구니 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CartPersistenceAdapter implements CartRepositoryPort {

    private final CourseCartRepository cartRepository;
    private final StudentRepository studentRepository;

    @Override
    public Long saveWithStudentId(Long studentId, Long courseId, LocalDateTime addedAt) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> EnrollmentException.studentNotFound(studentId));

        CourseCart cart = CourseCart.builder()
                .student(student)
                .courseId(courseId)
                .addedAt(addedAt)
                .build();

        CourseCart savedCart = cartRepository.save(cart);
        return savedCart.getId();
    }

    @Override
    public List<CartInfo> findByStudentId(Long studentId) {
        return cartRepository.findByStudentId(studentId).stream()
                .map(this::toCartInfo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartInfo> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return cartRepository.findByStudentIdAndCourseId(studentId, courseId)
                .map(this::toCartInfo);
    }

    @Override
    public boolean existsByStudentIdAndCourseId(Long studentId, Long courseId) {
        return cartRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<CartInfo> findAllById(List<Long> ids) {
        return cartRepository.findAllById(ids).stream()
                .map(this::toCartInfo)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(CartInfo cart) {
        cartRepository.deleteById(cart.id());
    }

    @Override
    public void deleteAll(List<CartInfo> carts) {
        List<Long> ids = carts.stream()
                .map(CartInfo::id)
                .collect(Collectors.toList());
        cartRepository.deleteAllById(ids);
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

    private CartInfo toCartInfo(CourseCart cart) {
        return new CartInfo(
            cart.getId(),
            cart.getStudent().getStudentId(),
            cart.getCourseId(),
            cart.getAddedAt()
        );
    }
}
