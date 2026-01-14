package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze;

/**
 * 패키지 구조 규칙 테스트
 *
 * freeze() 기능을 사용하여 현재 위반 사항을 기록하고,
 * 새로운 위반만 탐지합니다.
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class PackageStructureTest {

    // ============================================================
    // PKG-001: Entity 위치 규칙
    // ============================================================

    @ArchTest
    static final ArchRule entities_should_reside_in_proper_package =
        freeze(classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..adapter.out.persistence.entity..")
            .orShould().resideInAPackage("..domain..")
            .because("JPA Entity는 persistence.entity 또는 domain 패키지에 위치해야 합니다"));

    // ============================================================
    // PKG-002: Repository 위치 규칙
    // ============================================================

    @ArchTest
    static final ArchRule jpa_repositories_should_reside_in_repository_package =
        freeze(classes()
            .that().areAssignableTo(JpaRepository.class)
            .should().resideInAPackage("..adapter.out.persistence.repository..")
            .because("JPA Repository는 persistence.repository 패키지에 위치해야 합니다"));

    // ============================================================
    // PKG-003: Exception 위치 규칙
    // ============================================================

    @ArchTest
    static final ArchRule exceptions_should_reside_in_exception_package =
        freeze(classes()
            .that().haveSimpleNameEndingWith("Exception")
            .and().areAssignableTo(RuntimeException.class)
            .should().resideInAPackage("..exception..")
            .orShould().resideInAPackage("..common.exceptions..")
            .because("Exception 클래스는 exception 패키지에 위치해야 합니다"));
}
