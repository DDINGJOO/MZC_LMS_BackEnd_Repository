package com.mzc.backend.lms.common.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Custom Repository 공통 Base 클래스
 *
 * EntityManager 설정 및 공통 유틸리티 메서드 제공
 * - 엔티티 기반 Repository: BaseCustomRepository<Entity> 상속
 * - DTO 전용 Repository: BaseCustomRepository<Void> 상속 또는 직접 em 사용
 *
 * @param <T> 엔티티 타입 (DTO 전용은 Void 사용)
 */
public abstract class BaseCustomRepository<T> {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> entityClass;

    /**
     * 엔티티 기반 Repository용 생성자
     */
    protected BaseCustomRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * DTO 전용 Repository용 생성자
     */
    protected BaseCustomRepository() {
        this.entityClass = null;
    }

    /**
     * 기본 엔티티 타입으로 TypedQuery 생성
     * @throws IllegalStateException entityClass가 null인 경우
     */
    protected TypedQuery<T> createQuery(String jpql) {
        if (entityClass == null) {
            throw new IllegalStateException("entityClass가 설정되지 않았습니다. createQuery(jpql, resultClass)를 사용하세요.");
        }
        return em.createQuery(jpql, entityClass);
    }

    /**
     * 지정된 결과 타입으로 TypedQuery 생성
     */
    protected <R> TypedQuery<R> createQuery(String jpql, Class<R> resultClass) {
        return em.createQuery(jpql, resultClass);
    }

    /**
     * List를 Page로 변환
     */
    protected <R> Page<R> toPage(List<R> content, Pageable pageable, long total) {
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * TypedQuery에 페이지네이션 적용
     */
    protected void applyPagination(TypedQuery<?> query, Pageable pageable) {
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    /**
     * TypedQuery에 최대 결과 수 제한 적용
     */
    protected void applyLimit(TypedQuery<?> query, int limit) {
        query.setMaxResults(limit);
    }

    /**
     * 엔티티 클래스 반환
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }
}
