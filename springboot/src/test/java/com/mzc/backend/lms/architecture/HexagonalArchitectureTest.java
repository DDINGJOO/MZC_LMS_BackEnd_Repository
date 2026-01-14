package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze;

/**
 * 헥사고날 아키텍처 규칙 테스트
 *
 * 의존성 방향: Adapter -> Port -> Domain (외부 -> 내부)
 *
 * freeze() 기능을 사용하여 현재 위반 사항을 기록하고,
 * 새로운 위반만 탐지합니다.
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class HexagonalArchitectureTest {

    // ============================================================
    // HEX-001: 도메인 레이어는 외부에 의존하지 않음
    // ============================================================

    @ArchTest
    static final ArchRule domain_should_not_depend_on_adapter =
        freeze(noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter..")
            .because("도메인 레이어는 어댑터 레이어에 의존하면 안됩니다"));

    @ArchTest
    static final ArchRule domain_should_not_depend_on_application_service =
        freeze(noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..application.service..")
            .because("도메인 레이어는 애플리케이션 서비스 레이어에 의존하면 안됩니다"));

    // ============================================================
    // HEX-002: Application Port는 Adapter에 의존하지 않음
    // ============================================================

    @ArchTest
    static final ArchRule port_in_should_not_depend_on_adapter_out =
        freeze(noClasses()
            .that().resideInAPackage("..application.port.in..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter.out..")
            .because("Input Port는 Output Adapter에 의존하면 안됩니다"));

    @ArchTest
    static final ArchRule port_out_should_not_depend_on_adapter =
        freeze(noClasses()
            .that().resideInAPackage("..application.port.out..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter..")
            .because("Output Port는 Adapter에 의존하면 안됩니다"));

    // ============================================================
    // HEX-003: Service는 Port를 통해 Adapter와 통신
    // ============================================================

    @ArchTest
    static final ArchRule service_should_not_depend_on_jpa_repository =
        freeze(noClasses()
            .that().resideInAPackage("..application.service..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter.out.persistence.repository..")
            .because("서비스는 JPA Repository 구현체에 직접 의존하면 안됩니다. Repository Port를 사용하세요"));

    @ArchTest
    static final ArchRule service_should_not_depend_on_controller =
        noClasses()
            .that().resideInAPackage("..application.service..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter.in.web..")
            .andShould().haveSimpleNameEndingWith("Controller")
            .because("서비스는 컨트롤러에 의존하면 안됩니다");

    // ============================================================
    // HEX-004: Controller는 UseCase를 통해서만 Service 접근
    // ============================================================

    @ArchTest
    static final ArchRule controller_should_only_depend_on_allowed_packages =
        freeze(classes()
            .that().resideInAPackage("..adapter.in.web..")
            .and().haveSimpleNameEndingWith("Controller")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..adapter.in.web..",           // 같은 패키지 (DTO 포함)
                "..application.port.in..",      // UseCase Port
                "..application.service..",      // Service (현재 직접 주입 허용)
                "..common..",                   // 공통 모듈
                "..exception..",                // 예외 클래스
                "java..",                       // Java 기본
                "jakarta..",                    // Jakarta (validation 등)
                "lombok..",                     // Lombok
                "org.springframework..",        // Spring Framework
                "org.slf4j.."                   // Logging
            )
            .because("컨트롤러는 허용된 패키지에만 의존해야 합니다"));
}
