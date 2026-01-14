package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze;

/**
 * 네이밍 컨벤션 규칙 테스트
 *
 * freeze() 기능을 사용하여 현재 위반 사항을 기록하고,
 * 새로운 위반만 탐지합니다.
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class NamingConventionTest {

    // ============================================================
    // NAM-001: Controller 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule controllers_should_be_suffixed =
        classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Controller")
            .because("REST 컨트롤러는 'Controller' 접미사를 가져야 합니다");

    @ArchTest
    static final ArchRule controllers_should_reside_in_web_package =
        classes()
            .that().haveSimpleNameEndingWith("Controller")
            .and().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..adapter.in.web..")
            .because("컨트롤러는 adapter.in.web 패키지에 위치해야 합니다");

    // ============================================================
    // NAM-002: Service 네이밍 규칙
    // Service는 *Service 또는 *ServiceImpl 접미사 허용
    // ============================================================

    @ArchTest
    static final ArchRule services_should_be_properly_named =
        freeze(classes()
            .that().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Service")
            .orShould().haveSimpleNameEndingWith("ServiceImpl")
            .because("서비스 클래스는 'Service' 또는 'ServiceImpl' 접미사를 가져야 합니다"));

    @ArchTest
    static final ArchRule services_should_reside_in_service_package =
        freeze(classes()
            .that().areAnnotatedWith(Service.class)
            .should().resideInAPackage("..application.service..")
            .because("서비스는 application.service 패키지에 위치해야 합니다"));

    // ============================================================
    // NAM-003: UseCase 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule use_cases_should_be_suffixed =
        freeze(classes()
            .that().resideInAPackage("..application.port.in..")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("UseCase")
            .because("UseCase 인터페이스는 'UseCase' 접미사를 가져야 합니다"));

    // ============================================================
    // NAM-004: Repository Port 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule repository_ports_should_be_suffixed =
        freeze(classes()
            .that().resideInAPackage("..application.port.out..")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("RepositoryPort")
            .orShould().haveSimpleNameEndingWith("Port")
            .because("Repository Port는 'RepositoryPort' 또는 'Port' 접미사를 가져야 합니다"));

    // ============================================================
    // NAM-005: DTO 네이밍 규칙
    // Request/Response 또는 RequestDto/ResponseDto 접미사 허용
    // ============================================================

    @ArchTest
    static final ArchRule request_dtos_should_be_properly_named =
        freeze(classes()
            .that().resideInAPackage("..dto.request..")
            .should().haveSimpleNameEndingWith("Request")
            .orShould().haveSimpleNameEndingWith("RequestDto")
            .orShould().haveSimpleNameEndingWith("Dto")
            .because("Request DTO는 'Request', 'RequestDto' 또는 'Dto' 접미사를 가져야 합니다"));

    @ArchTest
    static final ArchRule response_dtos_should_be_properly_named =
        freeze(classes()
            .that().resideInAPackage("..dto.response..")
            .should().haveSimpleNameEndingWith("Response")
            .orShould().haveSimpleNameEndingWith("ResponseDto")
            .orShould().haveSimpleNameEndingWith("Dto")
            .because("Response DTO는 'Response', 'ResponseDto' 또는 'Dto' 접미사를 가져야 합니다"));
}
